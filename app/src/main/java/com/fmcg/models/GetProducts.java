package com.fmcg.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by RuchiTiwari on 5/5/2017.
 */

public class GetProducts
{

	private String ProductId;
	private String ProductName;
	private String ProductPrice;
	private String VAT;
	private String GST;
	private String Quantity;

	public String getProductId()
	{
		return ProductId;
	}

	public void setProductId(final String productId)
	{
		ProductId = productId;
	}

	public String getProductName()
	{
		return ProductName;
	}

	public void setProductName(final String productName)
	{
		ProductName = productName;
	}

	public String getProductPrice()
	{
		return ProductPrice;
	}

	public void setProductPrice(final String productPrice)
	{
		ProductPrice = productPrice;
	}

	public String getVAT()
	{
		return VAT;
	}

	public void setVAT(final String VAT)
	{
		this.VAT = VAT;
	}

	public String getGST()
	{
		return GST;
	}

	public void setGST(final String GST)
	{
		this.GST = GST;
	}

	public String getQuantity()
	{
		return Quantity;
	}

	public void setQuantity(final String quantity)
	{
		Quantity = quantity;
	}


	@Override
	public String toString()
	{
		String json = "";
		try
		{
			json = toJSON().toString();
		}
		catch (Exception e)
		{
		}
		return "GetProductCategory: " + json;
	}

	public JSONObject toJSON() throws JSONException
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject.putOpt("ProductId", ProductId);
		jsonObject.putOpt("ProductName", ProductName);
		jsonObject.putOpt("ProductPrice", ProductPrice);
		jsonObject.putOpt("VAT", VAT);
		jsonObject.putOpt("GST", GST);
		jsonObject.putOpt("Quantity", Quantity);
		return jsonObject;
	}
}
