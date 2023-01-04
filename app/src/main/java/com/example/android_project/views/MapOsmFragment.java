package com.example.android_project.views;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.android_project.R;
import com.example.android_project.data.models.fuel_price.GasStation;
import com.example.android_project.view_models.AuthViewModel;
import com.example.android_project.view_models.MapViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.search.SearchBar;

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

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapOsmFragment extends Fragment {

    private static final String TAG = "MapOsmFragment";

    protected static final int DEFAULT_INACTIVITY_DELAY_IN_MILLISECOND = 200;

    private ActivityResultLauncher<String[]> activityResultLauncher;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    private Location userLocation;
    private Marker userMarker;

    private MapView mapView;
    private AuthViewModel authViewModel;

    private MapViewModel mapViewModel;

    private RadiusMarkerClusterer radiusMarkerClusterer;

    private FrameLayout bottomSheet;

    public MapOsmFragment() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    boolean areAllGranted = true;
                    for(Boolean b : result.values()) {
                        areAllGranted = areAllGranted && b;
                    }

                    if(areAllGranted) {
                        startLocationUpdates();
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkLocationPermission()) {
            activityResultLauncher.launch(new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        locationRequest = new LocationRequest.Builder(1000)
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .setMinUpdateDistanceMeters(100)
                .build();

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);

        final Observer<List<GasStation>> observerGasStations = gasStations -> {
            mapView.getOverlays().clear();

            this.radiusMarkerClusterer = new RadiusMarkerClusterer(requireContext());
            mapView.getOverlays().add(radiusMarkerClusterer);

            drawMarkers(gasStations);
            mapView.invalidate();
        };

        mapViewModel.getGasStations().observe(this, observerGasStations);
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

        bottomSheet = requireView().findViewById(R.id.map_standard_bottom_sheet);

        SearchBar searchBar = requireView().findViewById(R.id.map_search_bar);
        searchBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.accountIcon) {
                authViewModel.logout();
            }
            return true;
        });

        NavController navController = Navigation.findNavController(view);
        authViewModel.isLogged().observe(getViewLifecycleOwner(), isLogged -> {
            if (isLogged) {
                Log.d("MapOsmFragment", "User logged");
            } else {
                navController.navigate(
                        R.id.authNavigation,
                        null,
                        new NavOptions.Builder()
                                .setEnterAnim(android.R.anim.slide_in_left)
                                .setExitAnim(android.R.anim.slide_out_right)
                                .build()
                );
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                Location location = locationResult.getLastLocation();

                if (location != null) {

                    GeoPoint markerPoint = new GeoPoint(location.getLatitude(), location.getLongitude());

                    if (userLocation == null) {
                        IMapController mapController = mapView.getController();
                        mapController.setZoom(15.0);
                        mapController.setCenter(markerPoint);
                    }

                    userLocation = location;

                    if (userMarker != null) {
                        userMarker.setPosition(markerPoint);
                    } else {
                        drawUserMarker();
                    }

                }
            }
        };

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = requireContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        mapView = requireView().findViewById(R.id.map_map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        mapView.setMultiTouchControls(true);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        mapView.addMapListener(new DelayedMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                getGasStationOnScreen();
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                if (event.getSource().getZoomLevelDouble() < event.getZoomLevel()) {
                    getGasStationOnScreen();
                }
                return false;
            }
        }, DEFAULT_INACTIVITY_DELAY_IN_MILLISECOND));

        mapView.invalidate();
    }

    private void getGasStationOnScreen() {
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
        );
    }

    private void drawUserMarker() {
        if (userLocation != null && mapView != null) {
            GeoPoint markerPoint = new GeoPoint(userLocation.getLatitude(), userLocation.getLongitude());

            userMarker = new Marker(mapView);

            userMarker.setIcon(createCustomIcon(R.drawable.person_pin_circle,R.color.main_dark_800));
            userMarker.setPosition(markerPoint);

            mapView.getOverlays().add(userMarker);
        }

    }

    private void drawMarkers(List<GasStation> gasStations) {
        drawUserMarker();

        for (GasStation gasStation : gasStations) {
            Marker marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(gasStation.getLat(), gasStation.getLon()));
            marker.setIcon(createCustomIcon(R.drawable.local_gas_station,R.color.secondary_dark_800));

//            marker.setOnMarkerClickListener((marker1, mapView1) -> {
//            });

            radiusMarkerClusterer.add(marker);
        }
    }

    private Drawable createCustomIcon(int drawable, int color) {
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(requireContext(), drawable);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(requireContext(), color));

        return wrappedDrawable;
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
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
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