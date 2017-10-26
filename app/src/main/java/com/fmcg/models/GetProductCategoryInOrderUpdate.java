package com.fmcg.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shiva on 7/3/2017.
 */

public class GetProductCategoryInOrderUpdate
{
	public int ProductId;
	public String ProductName;
	public String ProductDescription;
	public double ProductPrice;
	public double VAT;
	public double GST;
	public int Quantity;
	public int Frees;

	public double getProductPrice()
	{
		return ProductPrice;
	}

	public void setProductPrice(final double productPrice)
	{
		ProductPrice = productPrice;
	}

	public double getVAT()
	{
		return VAT;
	}

	public void setVAT(final double VAT)
	{
		this.VAT = VAT;
	}

	public double getGST()
	{
		return GST;
	}

	public void setGST(final double GST)
	{
		this.GST = GST;
	}

	public int getProductId()
	{
		return ProductId;
	}

	public void setProductId(final int productId)
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

	public String getProductDescription()
	{
		return ProductDescription;
	}

	public void setProductDescription(final String productDescription)
	{
		ProductDescription = productDescription;
	}

	public void setGST(final int GST)
	{
		this.GST = GST;
	}

	public int getQuantity()
	{
		return Quantity;
	}

	public void setQuantity(final int quantity)
	{
		Quantity = quantity;
	}

	public int getFrees()
	{
		return Frees;
	}

	public void setFrees(final int frees)
	{
		Frees = frees;
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
