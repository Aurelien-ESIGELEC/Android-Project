package com.example.android_project.views.pages;

import static com.example.android_project.utils.Utils.createCustomIcon;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.android_project.R;
import com.example.android_project.data.models.address.SearchAddress;
import com.example.android_project.data.models.fuel_price.GasStation;
import com.example.android_project.view_models.AuthViewModel;
import com.example.android_project.view_models.MapViewModel;
import com.example.android_project.views.adapters.SearchAddressListAdapter;
import com.example.android_project.views.bottom_sheets.GasStationFragment;
import com.example.android_project.views.bottom_sheets.ListStationBottomSheetFragment;
import com.example.android_project.views.bottom_sheets.MenuBottomSheetFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDragHandleView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.google.android.material.snackbar.Snackbar;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapOsmFragment extends Fragment {

    private static final String TAG = "MapOsmFragment";

    protected static final int DEFAULT_INACTIVITY_DELAY_IN_MILLISECOND = 1000;

    private ActivityResultLauncher<String[]> activityResultLauncher;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    private Marker userMarker;
    private Marker searchMarker;

    private MapView mapView;

    // View models
    private AuthViewModel authViewModel;
    private MapViewModel mapViewModel;

    private LinearProgressIndicator progressIndicator;

    private RadiusMarkerClusterer radiusMarkerClusterer;

    private HashMap<String, Marker> gasStationMarkerHashMap;

    public MapOsmFragment() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    boolean areAllGranted = true;
                    for (Boolean b : result.values()) {
                        areAllGranted = areAllGranted && b;
                    }

                    if (areAllGranted) {
                        startLocationUpdates();
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkLocationPermission()) {
            activityResultLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }

        gasStationMarkerHashMap = new HashMap<>();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        locationRequest = new LocationRequest.Builder(1000)
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .setMinUpdateDistanceMeters(100)
                .build();

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);

        mapViewModel.getGasStations().observe(this, gasStations -> {
            if (gasStations != null && !gasStations.isEmpty()) {
                Handler handler = new Handler();
                handler.postDelayed(() -> progressIndicator.setVisibility(View.INVISIBLE), 500);
                drawGasStationMarkers(gasStations);
            }
        });

        mapViewModel.getUserLocation().observe(requireActivity(), this::drawUserMarker);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_osm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Redirect to LoginPage when not logged
        authViewModel.isLogged().observe(getViewLifecycleOwner(), isLogged -> {
            if (isLogged == null || isLogged) {
                Log.d(TAG, "User logged");
            } else {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(
                        R.id.authNavigation/*,
                        null,
                        new NavOptions.Builder()
                                .setEnterAnim(android.R.anim.slide_in_left)
                                .setExitAnim(android.R.anim.slide_out_right)
                                .build()
                */);
            }
        });

        SearchBar searchBar = requireView().findViewById(R.id.map_search_bar);
        SearchView searchView = requireView().findViewById(R.id.map_search_view);
        RecyclerView recyclerView = requireView().findViewById(R.id.map_rv_search_result);
        FloatingActionButton floatingActionButton = requireView().findViewById(R.id.map_fab_location);
        ExtendedFloatingActionButton extendedFloatingActionButton =
                requireView().findViewById(R.id.map_fab_list_gas_station);

        progressIndicator = requireView().findViewById(R.id.map_station_progress_indicator);
        progressIndicator.setVisibility(View.INVISIBLE);

        floatingActionButton.setOnClickListener(view1 -> {
            searchBar.setText("");
            this.zoomOnCurrentLocation();
        });

        LinearLayout bottomSheet = requireView().findViewById(R.id.map_standard_bottom_sheet);

        BottomSheetDragHandleView bottomSheetDragHandleView = requireView().findViewById(R.id.drag_handle);
        AppBarLayout appBarLayout = requireView().findViewById(R.id.station_app_bar_layout);

        MaterialToolbar toolbar = requireView().findViewById(R.id.station_top_app_bar);

        mapViewModel.getSelectedStation().observe(getViewLifecycleOwner(), gasStation -> {
            if (gasStation != null) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map_fcv_bottom_sheet, GasStationFragment.class, null)
                        .commit();

                BottomSheetBehavior<LinearLayout> standardBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                standardBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

                toolbar.setNavigationOnClickListener(view1 -> standardBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED));

                //Manage the behavior of the bottom sheet when changing state
                standardBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                            bottomSheetDragHandleView.setVisibility(View.GONE);
                            toolbar.setTitle(gasStation.getAddress());
                            appBarLayout.setVisibility(View.VISIBLE);
                        } else {
                            bottomSheetDragHandleView.setVisibility(View.VISIBLE);
                            appBarLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                    }
                });

                mapView.addMapListener(new MapListener() {
                    @Override
                    public boolean onScroll(ScrollEvent event) {
                        standardBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        return false;
                    }

                    @Override
                    public boolean onZoom(ZoomEvent event) {
                        standardBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        return false;
                    }
                });
//                bottomSheet.setVisibility(View.VISIBLE);
//                GasStationInfoAdapter gasStationInfoAdapter = new GasStationInfoAdapter(gasStation);
//                bottomSheetRecyclerView.setAdapter(gasStationInfoAdapter);
//                bottomSheetRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//                mapViewModel.getDistanceBetweenLocationAndGasStation(gasStation).observe(getViewLifecycleOwner(), aDouble -> {
//                    gasStationInfoAdapter.notifyItemChanged(0);
//                });
            }
        });

        // Set the list of result for the search and its ClickListener
        mapViewModel.getListResultSearch().observe(getViewLifecycleOwner(), searchAddresses -> {
            recyclerView.setAdapter(new SearchAddressListAdapter(searchAddresses, position -> {
                SearchAddress searchAddress = searchAddresses.get(position);

                if (!searchAddress.getFullName().isEmpty()) {
                    searchBar.setText(searchAddress.getFullName() + " " + searchAddress.getFullCity());
                } else {
                    searchBar.setText(searchAddress.getFullCity());
                }

                searchView.hide();
                this.setSearchMarker(searchAddress);
                this.zoomOnSearchedLocation(searchAddress);
            }));
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        });

        // Set the action to do on back pressed when the SearchView is opened
        requireActivity()
                .getOnBackPressedDispatcher()
                .addCallback(
                        getViewLifecycleOwner(),
                        new OnBackPressedCallback(true) {
                            @Override
                            public void handleOnBackPressed() {
                                if (searchView.isShowing()) {
                                    searchView.hide();
                                } else {
                                    requireActivity().onBackPressed();
                                }
                            }
                        }
                );

        // Behavior when the search is launched
        searchView.getEditText()
                .setOnEditorActionListener(
                        (v, actionId, event) -> {
                            this.mapViewModel.setSearchValue(String.valueOf(searchView.getText()));
                            this.mapViewModel.getAddressBySearch(
                                    String.valueOf(searchView.getText())
                            ).observe(getViewLifecycleOwner(), searchAddresses -> Log.d(TAG, "setOnEditorActionListener: " + searchAddresses));
                            return false;
                        });

        // Behavior when the profile icon is clicked on the searchbar
        searchBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.accountIcon) {
                MenuBottomSheetFragment modalBottomSheet = new MenuBottomSheetFragment();
                modalBottomSheet.show(requireActivity().getSupportFragmentManager(), MenuBottomSheetFragment.TAG);
            }
            return true;
        });

        extendedFloatingActionButton.setOnClickListener(view1 -> {
            if (mapViewModel.getGasStations().getValue() != null) {
                ListStationBottomSheetFragment modalBottomSheet = new ListStationBottomSheetFragment();
                modalBottomSheet.show(requireActivity().getSupportFragmentManager(), ListStationBottomSheetFragment.TAG);
            } else {
                Snackbar.make(
                        view,
                        "No station displayed",
                        Snackbar.LENGTH_SHORT
                ).show();
            }

        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                Location location = locationResult.getLastLocation();

                setLocation(location);
            }
        };

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = requireContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        mapView = requireView().findViewById(R.id.map_map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        mapView.getController().setZoom(5.0);

        if (checkLocationPermission()) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            this.setLocation(location);
                            this.zoomOnCurrentLocation();
                        } else {
                            zoomOnFrance();
                        }
                    });
        } else {
            zoomOnFrance();
        }

        mapView.setMultiTouchControls(true);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        mapView.addMapListener(new DelayedMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {

                double zoomLevel = event.getSource().getZoomLevelDouble();
                getGasStationOnScreen(zoomLevel);
                return true;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                if (event.getSource().getZoomLevelDouble() < event.getZoomLevel()) {
                    double zoomLevel = event.getZoomLevel();
                    getGasStationOnScreen(zoomLevel);
                }
                return true;
            }
        }, DEFAULT_INACTIVITY_DELAY_IN_MILLISECOND));

        mapView.invalidate();

        radiusMarkerClusterer = new RadiusMarkerClusterer(requireContext());
        mapView.getOverlays().add(radiusMarkerClusterer);
    }

    private void getGasStationOnScreen(double zoomLevel) {
        Log.d(TAG, "getGasStationOnScreen: " +zoomLevel);
        if (zoomLevel >= 11) {
            progressIndicator.setVisibility(View.VISIBLE);
            BoundingBox box = mapView.getProjection().getBoundingBox();
            float[] dist = new float[1];
            Location.distanceBetween(
                    mapView.getProjection().getNorthEast().getLatitude(),
                    mapView.getProjection().getNorthEast().getLongitude(),
                    mapView.getProjection().getSouthWest().getLatitude(),
                    mapView.getProjection().getSouthWest().getLongitude(),
                    dist
            );

            mapViewModel.updateListStationsByLocation(
                    (float) box.getCenterLatitude(),
                    (float) box.getCenterLongitude(),
                    dist[0]/2
            ).observe(getViewLifecycleOwner(), gasStations -> {});
        }


    }

    private void drawUserMarker(Location userLocation) {
        if (userLocation != null && mapView != null) {
            GeoPoint markerPoint = new GeoPoint(userLocation.getLatitude(), userLocation.getLongitude());

            if (userMarker == null) {
                userMarker = new Marker(mapView);

                userMarker.setIcon(
                        createCustomIcon(
                                requireContext(),
                                R.drawable.person_pin_circle,
                                R.color.main_dark_800
                        )
                );

                userMarker.setOnMarkerClickListener((marker, mapView1) -> true);

                mapView.getOverlays().add(userMarker);
            }

            userMarker.setPosition(markerPoint);

            mapView.invalidate();
        }
    }

    private void drawGasStationMarkers(List<GasStation> gasStations) {
        Log.d(TAG, "drawGasStationMarkers: ");

        for (GasStation gasStation : gasStations) {
            if (!gasStationMarkerHashMap.containsKey(gasStation.getId())) {
                Marker marker = new Marker(mapView);
                marker.setPosition(new GeoPoint(gasStation.getLat(), gasStation.getLon()));
                marker.setIcon(createCustomIcon(requireContext(), R.drawable.local_gas_station, R.color.secondary_dark_800));

                marker.setOnMarkerClickListener((marker1, mapView1) -> {

                    for (Marker marker2: radiusMarkerClusterer.getItems()) {
                        marker2.setIcon(createCustomIcon(requireContext(), R.drawable.local_gas_station, R.color.secondary_dark_800));
                    }

                    mapViewModel.setSelectedStation(gasStation);
                    marker.setIcon(createCustomIcon(requireContext(), R.drawable.local_gas_station, R.color.teal_200));

                    radiusMarkerClusterer.invalidate();
                    mapView.invalidate();
                    return true;
                });

                gasStationMarkerHashMap.put(gasStation.getId(), marker);

                radiusMarkerClusterer.add(marker);
            }
        }

        radiusMarkerClusterer.invalidate();
        mapView.invalidate();
    }

    private void setLocation(Location location) {
        if (location != null) {
            this.mapViewModel.setUserLocation(location);
        }
    }

    private void zoomOnCurrentLocation() {
        Location userLocation = mapViewModel.getUserLocation().getValue();
        if (userLocation != null) {
            GeoPoint markerPoint = new GeoPoint(
                    userLocation.getLatitude(),
                    userLocation.getLongitude()
            );
            IMapController mapController = mapView.getController();
            mapController.animateTo(markerPoint, 15.0, 1L);
        }
    }

    private void zoomOnSearchedLocation(SearchAddress address) {
        if (address != null) {
            GeoPoint markerPoint = new GeoPoint(
                    address.getLat(),
                    address.getLon()
            );
            IMapController mapController = mapView.getController();
            mapController.animateTo(markerPoint, 15.0, 1L);
        }
    }

    private void setSearchMarker(SearchAddress address) {
        if (address != null) {
            GeoPoint markerPoint = new GeoPoint(address.getLat(), address.getLon());

            if (searchMarker == null) {
                searchMarker = new Marker(mapView);

                searchMarker.setIcon(
                        createCustomIcon(
                                requireContext(),
                                R.drawable.location_on,
                                R.color.main_500
                        )
                );

                searchMarker.setOnMarkerClickListener((marker, mapView1) -> true);

                mapView.getOverlays().add(searchMarker);
            }

            searchMarker.setPosition(markerPoint);

        }
    }

    private void zoomOnFrance() {
        if (mapView != null) {
            IMapController mapController = mapView.getController();
            mapController.animateTo(
                    new GeoPoint(46.227638, 2.213749),
                    8.0,
                    1L
            );
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (checkLocationPermission()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        stopLocationUpdates();
    }

    private Boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationUpdates() {
        if (checkLocationPermission()) {
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        }
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}