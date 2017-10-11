package com.fmcg.models;

/**
 * Created by Shiva on 6/6/2017.
 */

public class RouteDetailsData
{
	private int zoneId;
	private int routeId;
	private String zoneName;
	private String routeName;
	private String targetAmount;
	private boolean isSelected;




	public RouteDetailsData(final int zoneId, final int routeId, final String zoneName, final String routeName, final String targetAmount, final boolean isSelected)
	{
		this.zoneId = zoneId;
		this.routeId = routeId;
		this.zoneName = zoneName;
		this.routeName = routeName;
		this.targetAmount = targetAmount;
		this.isSelected = isSelected;

	}

	public int getZoneId()
	{
		return zoneId;
	}

	public void setZoneId(final int zoneId)
	{
		this.zoneId = zoneId;
	}

	public int getRouteId()
	{
		return routeId;
	}

	public void setRouteId(final int routeId)
	{
		this.routeId = routeId;
	}


	public String getZoneName()
	{
		return zoneName;
	}

	public void setZoneName(final String zoneName)
	{
		this.zoneName = zoneName;
	}

	public String getRouteName()
	{
		return routeName;
	}

	public void setRouteName(final String routeName)
	{
		this.routeName = routeName;
	}

	public String getTargetAmount()
	{
		return targetAmount;
	}

	public void setTargetAmount(final String targetAmount)
	{
		this.targetAmount = targetAmount;
	}

	public boolean isSelected()
	{
		return isSelected;
	}

	public void setSelected(final boolean selected)
	{
		isSelected = selected;
	}


}
