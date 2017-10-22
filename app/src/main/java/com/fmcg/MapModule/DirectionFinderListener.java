package com.fmcg.MapModule;

import com.fmcg.models.GetShopsArray;

import java.util.List;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */
public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<RouteDirection> route);
}
