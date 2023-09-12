package com.app.activeparks.util;

import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.MapTileIndex;

public class MapTilerTileSource extends OnlineTileSourceBase {

    private static final String BASE_URL = "https://tiles.openstreetmap.org.ua/tile/{z}/{x}/{y}.png";

    public MapTilerTileSource() {
        super("MapTiler", 1, 15, 512, ".png", new String[] {});
    }

    @Override
    public String getTileURLString(long pMapTileIndex) {
        return BASE_URL.replace("{z}", Long.toString(MapTileIndex.getZoom(pMapTileIndex)))
                .replace("{x}", Long.toString(MapTileIndex.getX(pMapTileIndex))).replace("{y}", Long.toString(MapTileIndex.getY(pMapTileIndex)));
    }
}