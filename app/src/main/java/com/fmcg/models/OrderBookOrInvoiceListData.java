package com.fmcg.models;

/**
 * Created by Shiva on 6/9/2017.
 */

public class OrderBookOrInvoiceListData
{
	private int employeeId;
	private int routeId;
	private String orderNumber;
	private String orderDate;
	private String shopName;
	private String Status;
	private int noOfProducts;
	private int subTotalAmount;
	private double taxAmount;
	private double totalAmount;

	public OrderBookOrInvoiceListData(final int employeeId, final int routeId, final String orderNumber, final String orderDate, final String shopName, final String status,
	                                  final int noOfProducts, final int subTotalAmount,
	                                  final double taxAmount, final double totalAmount)
	{
		this.employeeId = employeeId;
		this.routeId = routeId;
		this.orderNumber = orderNumber;
		this.orderDate = orderDate;
		this.shopName = shopName;
		Status = status;
		this.noOfProducts = noOfProducts;
		this.subTotalAmount = subTotalAmount;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
	}

	public int getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(final int employeeId)
	{
		this.employeeId = employeeId;
	}

	public int getRouteId()
	{
		return routeId;
	}

	public void setRouteId(final int routeId)
	{
		this.routeId = routeId;
	}

	public String getOrderNumber()
	{
		return orderNumber;
	}

	public void setOrderNumber(final String orderNumber)
	{
		this.orderNumber = orderNumber;
	}

	public String getOrderDate()
	{
		return orderDate;
	}

	public void setOrderDate(final String orderDate)
	{
		this.orderDate = orderDate;
	}

	public String getShopName()
	{
		return shopName;
	}

	public void setShopName(final String shopName)
	{
		this.shopName = shopName;
	}

	public String getStatus()
	{
		return Status;
	}

	public void setStatus(final String status)
	{
		Status = status;
	}

	public int getNoOfProducts()
	{
		return noOfProducts;
	}

	public void setNoOfProducts(final int noOfProducts)
	{
		this.noOfProducts = noOfProducts;
	}

	public int getSubTotalAmount()
	{
		return subTotalAmount;
	}

	public void setSubTotalAmount(final int subTotalAmount)
	{
		this.subTotalAmount = subTotalAmount;
	}

	public double getTaxAmount()
	{
		return taxAmount;
	}

	public void setTaxAmount(final double taxAmount)
	{
		this.taxAmount = taxAmount;
	}

	public double getTotalAmount()
	{
		return totalAmount;
	}

	public void setTotalAmount(final double totalAmount)
	{
		this.totalAmount = totalAmount;
	}


}
