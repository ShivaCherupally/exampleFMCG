package com.fmcg.MapModule;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Proxim on 10/18/2017.
 */

public class RouteDirection {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
