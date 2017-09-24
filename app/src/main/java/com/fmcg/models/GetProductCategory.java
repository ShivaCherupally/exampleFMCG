package com.fmcg.models;


import org.json.JSONException;
import org.json.JSONObject;

public class GetProductCategory
{
	public String ProductId;
	public String ProductName;
	public String ProductPrice;
	public String VAT;
	public String GST;
	public String Quantity;
	public String Frees;


	public String getFres()
	{
		return Frees;
	}

	public void setFres(final String Fres)
	{
		Frees = Fres;
	}

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
		jsonObject.putOpt("Frees", Frees);
		return jsonObject;
	}
}
