package com.fmcg.models;

/**
 * Created by RuchiTiwari on 5/9/2017.
 */

public class ShopNamesData
{
	public String shopId;
	public String ShopName;

	public ShopNamesData(String shopId, String ShopName)
	{
		this.shopId = shopId;
		this.ShopName = ShopName;
	}

	public String getShopId()
	{
		return shopId;
	}

	public void setShopId(final String shopId)
	{
		this.shopId = shopId;
	}

	public String getShopName()
	{
		return ShopName;
	}

	public void setShopName(final String shopName)
	{
		ShopName = shopName;
	}
}


