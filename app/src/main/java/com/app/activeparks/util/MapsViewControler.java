package com.app.activeparks.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.app.activeparks.data.model.event.RoutePoint;
import com.app.activeparks.data.model.sportsgrounds.ItemSportsground;
import com.app.activeparks.data.model.sportsgrounds.Sportsgrounds;
import com.technodreams.activeparks.BuildConfig;
import com.technodreams.activeparks.R;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class MapsViewControler {

    private MapsViewListener mapsViewListener;

    private Context ctx;

    public OnlineTileSourceBase MAPNIK = new XYTileSource("Map",
            7, 18, 256, ".png", new String[] {
            "https://api.maptiler.com/maps/streets/",
            "https://b.tile.openstreetmap.fr/hot/",
            "https://c.tile.openstreetmap.fr/hot/" },"© OpenStreetMap contributors");

    private MapView mapView;

    private DirectedLocationOverlay myLocationOverlay;
    private MapController mapController;
    private RoadManager roadManager;
    private int latitude, longitude;

    public MapsViewControler(MapView mapview, Context context) {
        this.ctx = context;
        mapView = mapview;
        mapView.setTileSource(MAPNIK);
        myLocationOverlay = new DirectedLocationOverlay(ctx);
        mapView.getOverlays().add(myLocationOverlay);
        mapView.setMaxZoomLevel(15.0);
        mapView.setMinZoomLevel(7.0);
        mapView.setMultiTouchControls(true);
        mapView.setTilesScaledToDpi(true);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        mapController = (MapController) mapView.getController();
        mapController.setCenter(new GeoPoint(50.46710, 30.37593));
        mapController.setZoom(10.0);
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                int lat = (int)mapView.getMapCenter().getLatitude();
                int lon = (int)mapView.getMapCenter().getLongitude();

                if (lat != latitude || lon != longitude){
                    if (mapsViewListener != null) {
                        mapsViewListener.onPositionStatus(true);
                        mapsViewListener.onPosition(mapView.getMapCenter().getLatitude(), mapView.getMapCenter().getLongitude());
                    }
                }else {
                    if (mapsViewListener != null) {
                        mapsViewListener.onPositionStatus(false);
                    }
                }

                return false;
            }
        });

        roadManager = new OSRMRoadManager(ctx, BuildConfig.APPLICATION_ID+"/"+BuildConfig.VERSION_NAME);


        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx),mapView);
        mLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(mLocationOverlay);

    }

    public void setSportsgroundList(Sportsgrounds sportsgrounds){
        mapView.getOverlays().removeAll(mapView.getOverlays());

        mapView.invalidate();

        latitude = (int)mapView.getMapCenter().getLatitude();
        longitude = (int)mapView.getMapCenter().getLongitude();

        for (ItemSportsground sportsground : sportsgrounds.getSportsground()) {

                        Marker startMarker = new Marker(mapView);
                        startMarker.setIcon(ctx.getResources().getDrawable(R.drawable.map_icon_mini).mutate());
                        startMarker.setPosition(new GeoPoint(sportsground.getLocation().get(0), sportsground.getLocation().get(1)));
                        startMarker.setDraggable(true);
                        startMarker.setRelatedObject(sportsground);
                        startMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker, MapView mapView) {
                                mapsViewListener.onClick((ItemSportsground) marker.getRelatedObject());
                                return false;
                            }
                        });
                        mapView.getOverlays().add(startMarker);
        }
    }

    public void setMarker(final double aLatitude, final double aLongitude){
        mapView.getOverlays().removeAll(mapView.getOverlays());

        mapView.invalidate();
            Marker startMarker = new Marker(mapView);
            startMarker.setIcon(ctx.getResources().getDrawable(R.drawable.map_icon_mini).mutate());
            startMarker.setPosition(new GeoPoint(aLatitude, aLongitude));
            startMarker.setDraggable(true);
            mapView.getOverlays().add(startMarker);
            mapController.setCenter(new GeoPoint(aLatitude, aLongitude));
    }

    public void setRoutePint(List<RoutePoint> route){
        Polyline line = new Polyline(mapView);

        line.setColor(Color.parseColor("#1BA8B2"));
        mapController.setCenter(new GeoPoint(route.get(0).getLocation().get(0), route.get(0).getLocation().get(1)));

        for(RoutePoint item: route) {

            Marker startMarker = new Marker(mapView);
            line.addPoint(new GeoPoint(item.getLocation().get(0), item.getLocation().get(1)));
            startMarker.setPosition(new GeoPoint(item.getLocation().get(0), item.getLocation().get(1)));
            startMarker.setIcon(ctx.getResources().getDrawable(R.drawable.map_icon_mini).mutate());
            mapView.getOverlays().add(startMarker);
        }
        mapController.setZoom(13.0);
        mapView.getOverlays().add(line);
        mapView.invalidate();
    }

    public void setZoomMax(){
        mapController.setZoom(mapView.getZoomLevelDouble()+0.2);
    }

    public void setZoomMin(){
        mapController.setZoom(mapView.getZoomLevelDouble()-0.2);
    }

    public void setPositionMap(double lat, double lon){
        mapController.setCenter(new GeoPoint(lat, lon));
    }

    public void setPosition(){
        //mapView.back
    }

    public interface MapsViewListener{
        void onClick(ItemSportsground sportsground);
        void onPositionStatus(boolean status);
        void onPosition(double latitude, double longitude);
    }

    public MapsViewControler setOnCliclListener(MapsViewListener mapsViewListener){
        this.mapsViewListener = mapsViewListener;
        return this;
    }
}
