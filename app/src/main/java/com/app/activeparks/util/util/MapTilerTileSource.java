package com.app.activeparks.util.util;

import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.MapTileIndex;

public class MapTilerTileSource extends OnlineTileSourceBase {

    private static final String BASE_URL = "https://api.maptiler.com/maps/70344ef8-97c5-4025-a3a1-3e2f5ac3643a/{z}/{x}/{y}.png?key=13tuuIyDJi8oyTjwcONc";

    public MapTilerTileSource() {
        super("MapTiler", 1, 15, 512, ".png", new String[] {});
    }

    @Override
    public String getTileURLString(long pMapTileIndex) {
        return BASE_URL.replace("{z}", Long.toString(MapTileIndex.getZoom(pMapTileIndex)))
                .replace("{x}", Long.toString(MapTileIndex.getX(pMapTileIndex))).replace("{y}", Long.toString(MapTileIndex.getY(pMapTileIndex)));
    }
}