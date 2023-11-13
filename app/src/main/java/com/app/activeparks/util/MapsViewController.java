package com.app.activeparks.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;

import com.app.activeparks.data.model.points.RoutePoint;
import com.app.activeparks.data.model.sportsgrounds.ItemSportsground;
import com.app.activeparks.data.model.sportsgrounds.Sportsgrounds;
import com.technodreams.activeparks.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;

public class MapsViewController implements MapEventsReceiver {

    private MapsViewListener mapsViewListener;

    private Context ctx;

    public OnlineTileSourceBase MAPNIK = new XYTileSource("Map",
            5, 20, 256, ".png", new String[]{
            "https://tiles.openstreetmap.org.ua/tile/",
            "https://tiles.openstreetmap.org.ua/tile/",
            "https://tiles.openstreetmap.org.ua/tile/"}, "© OpenStreetMap contributors");

    private MapView mapView;
    private MyLocationNewOverlay myLocationOverlay;
    private MapController mapController;
    private int latitude, longitude;

    public boolean homeView = false;

    public MapsViewController(MapView mapview, Context context) {

        this.ctx = context;
        mapView = mapview;

        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));


        //mapView.setTileSource(new MapTilerTileSource());

        mapView.setTileSource(new MapTilerTileSource());
        mapView.setMultiTouchControls(true);
        mapView.setTilesScaledToDpi(true);
        mapController = (MapController) mapView.getController();
        mapController.setCenter(new GeoPoint(50.44812, 30.51814));
        mapController.setZoom(11.0);
        mapView.setMinZoomLevel(7.0);
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                int lat = (int) mapView.getMapCenter().getLatitude();
                int lon = (int) mapView.getMapCenter().getLongitude();

                if (lat != latitude || lon != longitude) {
                    if (mapsViewListener != null) {
                        mapsViewListener.onPositionStatus(true);
                        mapsViewListener.onPosition(mapView.getMapCenter().getLatitude(), mapView.getMapCenter().getLongitude());
                    }
                } else {
                    if (mapsViewListener != null) {
                        mapsViewListener.onPositionStatus(false);
                    }
                }

                return false;
            }
        });

        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), mapView);
        mapView.setMultiTouchControls(true);
        mapView.getOverlayManager().add(myLocationOverlay);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.setDrawAccuracyEnabled(true);
        myLocationOverlay.runOnFirstFix(() -> {
            // zoom to current location
            GeoPoint myLocation = myLocationOverlay.getMyLocation();
            try {
                if (mapView != null) {
                    Marker myMarker = new Marker(mapView);
                    myMarker.setIcon(context.getResources().getDrawable(R.drawable.ic_user_map).mutate());
                    myMarker.setPosition(myLocation);
                    mapView.getOverlayManager().add(myMarker);
                    if (homeView == true) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            mapView.getController().animateTo(myLocation);
                        });
                    }
                }
            } catch (Exception e) {
            }
        });
    }

    public void onResume() {
        mapView.onResume();
    }

    public void onPause() {
        mapView.onPause();
    }

    public void onDestroy() {
        myLocationOverlay.disableMyLocation();
        mapView.getOverlayManager().remove(myLocationOverlay);
        mapView.onDetach();
    }

    public void mylocation() {
        GeoPoint myLocation = myLocationOverlay.getMyLocation();
        mapView.getController().animateTo(myLocation);
    }

    public void setSportsgroundList(Sportsgrounds sportsgrounds) {
        mapView.getOverlays().removeAll(mapView.getOverlays());

        mapView.invalidate();

        latitude = (int) mapView.getMapCenter().getLatitude();
        longitude = (int) mapView.getMapCenter().getLongitude();

        for (ItemSportsground sportsground : sportsgrounds.getSportsground()) {

            Marker startMarker = new Marker(mapView);
            startMarker.setIcon(ctx.getResources().getDrawable(R.drawable.ic_material_location).mutate());
            startMarker.setPosition(new GeoPoint(sportsground.getLocation().get(0), sportsground.getLocation().get(1)));
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

    public void setMarker(final double aLatitude, final double aLongitude) {
        mapView.getOverlays().removeAll(mapView.getOverlays());
        Marker startMarker = new Marker(mapView);
        startMarker.setIcon(ctx.getResources().getDrawable(R.drawable.ic_material_location).mutate());
        startMarker.setPosition(new GeoPoint(aLatitude, aLongitude));
        startMarker.setInfoWindow(null);
        mapView.getOverlays().add(startMarker);
        mapController.setZoom(12.0);
        mapController.setCenter(new GeoPoint(aLatitude, aLongitude));
        mapView.invalidate();
    }

    public void selectMarker(Double lat, Double lon) {
        new Handler(Looper.getMainLooper()).post(() -> {
            mapView.getController().animateTo(new GeoPoint(lat, lon));
        });
        mapController.setZoom(20.0);
    }

    public void setRoutePint(List<RoutePoint> route) {
        Polyline line = new Polyline(mapView);

        line.setColor(Color.parseColor("#1BA8B2"));
        mapController.setCenter(new GeoPoint(route.get(0).getLocation().get(0), route.get(0).getLocation().get(1)));

        int index = 2;
        for (RoutePoint item : route) {

            Marker startMarker = new Marker(mapView);
            line.addPoint(new GeoPoint(item.getLocation().get(0), item.getLocation().get(1)));
            startMarker.setPosition(new GeoPoint(item.getLocation().get(0), item.getLocation().get(1)));
            if (item.getPointIndex() == 0) {
                startMarker.setIcon(ctx.getResources().getDrawable(R.drawable.ic_map_marker).mutate());
            }else if (item.getType() == 0) {
                Bitmap markerBitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_material_location); // Завантажуємо зображення маркера
                Bitmap mutableBitmap = markerBitmap.copy(Bitmap.Config.ARGB_8888, true); // Копіюємо зображення маркера для редагування
                Canvas canvas = new Canvas(mutableBitmap); // Створюємо канву для редагування зображення маркера
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // Створюємо пензель для малювання тексту
                paint.setColor(Color.WHITE); // Встановлюємо колір тексту
                paint.setTextSize(26); // Встановлюємо розмір тексту
                canvas.drawText("" + index, 16, 30, paint); // Малюємо номер на маркері
                Drawable markerDrawable = new BitmapDrawable(ctx.getResources(), mutableBitmap); // Перетворюємо зображення маркера в Drawable
                startMarker.setIcon(markerDrawable);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

                index = index + 1;
            }else{
                startMarker.setIcon(ctx.getResources().getDrawable(R.drawable.ic_default_dot).mutate());
            }
            mapView.getOverlays().add(startMarker);
        }
        mapController.setZoom(18.0);
        mapView.getOverlays().add(line);
        mapView.invalidate();
    }


    public void setZoomMax() {
        mapController.setZoom(mapView.getZoomLevelDouble() + 0.2);
    }

    public void setZoomMin() {
        mapController.setZoom(mapView.getZoomLevelDouble() - 0.2);
    }

    public void setPositionMap(double lat, double lon) {
        new Handler(Looper.getMainLooper()).post(() -> {
            mapView.getController().animateTo(new GeoPoint(lat, lon));
        });
    }

    public void setPosition() {
        //mapView.back
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }

    public interface MapsViewListener {
        void onClick(ItemSportsground sportsground);

        void onPositionStatus(boolean status);

        void onPosition(double latitude, double longitude);
    }

    public MapsViewController setOnCliclListener(MapsViewListener mapsViewListener) {
        this.mapsViewListener = mapsViewListener;
        return this;
    }

}
