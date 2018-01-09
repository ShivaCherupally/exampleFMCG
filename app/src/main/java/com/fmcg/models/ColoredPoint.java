package com.fmcg.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Shiva on 12/24/2017.
 */

public class ColoredPoint
{
	public LatLng coords;
	public int color;

	public ColoredPoint(LatLng coords, int color) {
		this.coords = coords;
		this.color = color;
	}
}
