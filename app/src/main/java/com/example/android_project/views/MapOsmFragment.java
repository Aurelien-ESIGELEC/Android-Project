package com.example.android_project.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_project.R;
import com.example.android_project.data.models.fuel_price.GasStation;
import com.example.android_project.view_models.AuthViewModel;
import com.example.android_project.view_models.MapViewModel;

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
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapOsmFragment extends Fragment /*implements LocationListener*/ {

    private static final int PERMISSION_GPS=100;
    protected static final int DEFAULT_INACTIVITY_DELAY_IN_MILLISECOND = 200;

    private MapView mapView;
    private AuthViewModel authViewModel;

    private MapViewModel mapViewModel;

    private Marker userMarker;

    private List<Marker> markerGasStationList;

    private RadiusMarkerClusterer radiusMarkerClusterer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        markerGasStationList = new ArrayList<>();
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        mapViewModel  = new ViewModelProvider(requireActivity()).get(MapViewModel.class);

        final Observer<List<GasStation>> observerGasStations = gasStations -> {
            for (GasStation gasStation : gasStations) {
                Marker marker = new Marker(mapView);
                marker.setPosition(new GeoPoint(gasStation.getLat(), gasStation.getLon()));
                Drawable unwrappedDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.local_gas_station);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable, Color.YELLOW);
                marker.setIcon(wrappedDrawable);
                radiusMarkerClusterer.add(marker);
                markerGasStationList.add(marker);
            }
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
        NavController navController = Navigation.findNavController(view);
        authViewModel.isLogged().observe(getViewLifecycleOwner(), isLogged -> {
            if (isLogged) {
                Log.d("MapOsmFragment","User logged");
            } else {
                navController.navigate(R.id.authNavigation);
            }
        });

//        LocationManager lm = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
//
//        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(requireActivity(),
//                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSION_GPS
//            );
//        } else {
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 0, MapOsmFragment.this);
//        }
//
        //load/initialize the osmdroid configuration, this can be done
        Context ctx = requireContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        mapView = requireView().findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        mapView.setMultiTouchControls(true);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

//        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(mapView);
//        mRotationGestureOverlay.setEnabled(true);
//        mapView.setMultiTouchControls(true);
//        mapView.getOverlays().add(mRotationGestureOverlay);

        this.radiusMarkerClusterer = new RadiusMarkerClusterer(requireContext());
        mapView.getOverlays().add(radiusMarkerClusterer);

        mapView.addMapListener(new DelayedMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                updateOnChange();
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                Log.v("MapOsmFragment", String.valueOf(event.getSource().getZoomLevelDouble()));
                Log.v("MapOsmFragment", String.valueOf(event.getZoomLevel()));
                if (event.getSource().getZoomLevelDouble() < event.getZoomLevel()) {
                    updateOnChange();
                }
                return false;
            }
        }, DEFAULT_INACTIVITY_DELAY_IN_MILLISECOND));

        IMapController mapController = mapView.getController();
        mapController.setZoom(9.5);
        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);
//
//        this.userMarker = new Marker(mapView);
//
//        Drawable unwrappedDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.person_pin_circle);
//        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
//        DrawableCompat.setTint(wrappedDrawable, Color.BLUE);
//        this.userMarker.setIcon(wrappedDrawable);
//        this.userMarker.setPosition(startPoint);
//        mapView.getOverlays().add(userMarker);
//
//        mapView.invalidate();
    }

//    @Override
//    public void onLocationChanged(@NonNull Location location) {
//        if (this.userMarker != null) {
//            userMarker.setPosition(
//                    new GeoPoint(location.getLatitude(), location.getLongitude())
//            );
//        }
//    }

    private void updateOnChange() {
        BoundingBox box = mapView.getProjection().getBoundingBox();
        float [] dist = new float[1];
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

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        mapView.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        mapView.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
}