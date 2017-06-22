package com.fmcg.models;

import org.json.JSONException;
import org.json.JSONObject;

public class GetOrderSummary
{
    /*public String Quantity;
    public String Frees;
    public String ProductName;
    public String Price;
    public String VAT;
    public String GST;
    public String SubTotalAmount;
    public String TaxAmount;
    public String TotalAmount;*/

	public int SubTotalAmount;
	public int Quantity;
	public int Price;
	public int Frees;
	public double TotalAmount;
	public double VAT;
	public String ProductName;
	public double TaxAmount;
	public int GST;

	public int getSubTotalAmount()
	{
		return SubTotalAmount;
	}

	public void setSubTotalAmount(final int subTotalAmount)
	{
		SubTotalAmount = subTotalAmount;
	}

	public int getQuantity()
	{
		return Quantity;
	}

	public void setQuantity(final int quantity)
	{
		Quantity = quantity;
	}

	public int getPrice()
	{
		return Price;
	}

	public void setPrice(final int price)
	{
		Price = price;
	}

	public int getFrees()
	{
		return Frees;
	}

	public void setFrees(final int frees)
	{
		Frees = frees;
	}

	public double getTotalAmount()
	{
		return TotalAmount;
	}

	public void setTotalAmount(final double totalAmount)
	{
		TotalAmount = totalAmount;
	}

	public double getVAT()
	{
		return VAT;
	}

	public void setVAT(final double VAT)
	{
		this.VAT = VAT;
	}

	public String getProductName()
	{
		return ProductName;
	}

	public void setProductName(final String productName)
	{
		ProductName = productName;
	}

	public double getTaxAmount()
	{
		return TaxAmount;
	}

	public void setTaxAmount(final double taxAmount)
	{
		TaxAmount = taxAmount;
	}

	public int getGST()
	{
		return GST;
	}

	public void setGST(final int GST)
	{
		this.GST = GST;
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
		return "GetOrderSummary: " + json;
	}

	public JSONObject toJSON() throws JSONException
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject.putOpt("SubTotalAmount", SubTotalAmount);
		jsonObject.putOpt("Quantity", Quantity);
		jsonObject.putOpt("Price", Price);
		jsonObject.putOpt("Frees", Frees);
		jsonObject.putOpt("TotalAmount", TotalAmount);
		jsonObject.putOpt("VAT", VAT);
		jsonObject.putOpt("ProductName", ProductName);
		jsonObject.putOpt("TaxAmount", TaxAmount);
		jsonObject.putOpt("GST", GST);
		return jsonObject;
	}
}
