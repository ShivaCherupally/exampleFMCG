package com.fmcg.models;

/**
 * Created by RuchiTiwari on 5/3/2017.
 */

public class Product
{
	private String itemNumber = "";
	private String description = "";
	private Double price = 0.00;
	private int quantity = 0;
	private Double ext = 0.00;

	public Product(String itemNumber, String description, Double price) {
		super();
		this.itemNumber = itemNumber;
		this.description = description;
		this.price = price;
	}

	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Double getExt() {
		return ext;
	}
	public void setExt(Double ext) {
		this.ext = ext;
	}
}
