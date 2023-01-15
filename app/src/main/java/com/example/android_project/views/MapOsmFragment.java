package com.example.android_project.views;

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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.android_project.R;
import com.example.android_project.data.models.address.SearchAddress;
import com.example.android_project.data.models.fuel_price.GasStation;
import com.example.android_project.view_models.AuthViewModel;
import com.example.android_project.view_models.MapViewModel;
import com.example.android_project.views.adapters.GasStationInfoAdapter;
import com.example.android_project.views.adapters.SearchAddressListAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

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

    protected static final int DEFAULT_INACTIVITY_DELAY_IN_MILLISECOND = 2000;

    private ActivityResultLauncher<String[]> activityResultLauncher;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    private Location userLocation;
    private Marker userMarker;
    private Marker searchMarker;

    private MapView mapView;
    private AuthViewModel authViewModel;

    private MapViewModel mapViewModel;

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
                drawGasStationMarkers(gasStations);
            }
        });
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

        FrameLayout bottomSheet = requireView().findViewById(R.id.map_standard_bottom_sheet);
        RecyclerView bottomSheetRecyclerView = requireView().findViewById(R.id.map_bottom_sheet_recycler_view);

        bottomSheet.setVisibility(View.INVISIBLE);

        mapViewModel.getSelectedStation().observe(getViewLifecycleOwner(), gasStation -> {
            if (gasStation != null) {
                BottomSheetBehavior<FrameLayout> standardBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                standardBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomSheetRecyclerView.setAdapter(new GasStationInfoAdapter(gasStation));
                bottomSheetRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                bottomSheet.setVisibility(View.VISIBLE);
            } else {
                bottomSheet.setVisibility(View.INVISIBLE);
            }
        });

        SearchBar searchBar = requireView().findViewById(R.id.map_search_bar);
        SearchView searchView = requireView().findViewById(R.id.map_search_view);
        RecyclerView recyclerView = requireView().findViewById(R.id.map_rv_search_result);

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

        searchView.getEditText()
                .setOnEditorActionListener(
                        (v, actionId, event) -> {
                            this.mapViewModel.setSearchValue(String.valueOf(searchView.getText()));
                            this.mapViewModel.getAddressBySearch(
                                    String.valueOf(searchView.getText())
                            ).observe(getViewLifecycleOwner(), searchAddresses -> Log.d(TAG, "setOnEditorActionListener: " + searchAddresses));
                            return false;
                        });

        searchBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.accountIcon) {
                MenuBottomSheetFragment modalBottomSheet = new MenuBottomSheetFragment();
                modalBottomSheet.show(requireActivity().getSupportFragmentManager(), MenuBottomSheetFragment.TAG);
            }
            return true;
        });

        FloatingActionButton floatingActionButton = requireView().findViewById(R.id.map_fab_location);
        floatingActionButton.setOnClickListener(view1 -> this.zoomOnCurrentLocation());

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                Location location = locationResult.getLastLocation();

                setLocationAndMarker(location);
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
                            this.setLocationAndMarker(location);
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
                    dist[0]
            ).observe(getViewLifecycleOwner(), gasStations -> {
            });
        }


    }

    private void drawUserMarker() {
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
                    mapViewModel.setSelectedStation(gasStation);
                    return true;
                });

                gasStationMarkerHashMap.put(gasStation.getId(), marker);

                radiusMarkerClusterer.add(marker);
            }
        }

        radiusMarkerClusterer.invalidate();
        mapView.invalidate();
    }

    private void setLocationAndMarker(Location location) {
        if (location != null) {
            userLocation = location;

            if (userMarker != null) {
                userMarker.setPosition(
                        new GeoPoint(
                                location.getLatitude(),
                                location.getLongitude()
                        )
                );
            } else {
                drawUserMarker();
            }

            mapView.invalidate();
        }
    }

    private void zoomOnCurrentLocation() {
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