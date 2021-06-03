package com.jpmorgan.sales.model;

import java.math.BigDecimal;

public class Product {

	private String productType;
	private BigDecimal price;
	private int quantity;
	private BigDecimal adjustment;
	private BigDecimal newPrice = new BigDecimal(0).setScale(8, BigDecimal.ROUND_HALF_UP);

	public Product() {

	}

	public Product(String productType, BigDecimal price, int quantity) {
		super();
		this.productType = productType;
		this.price = price;
		this.quantity = quantity;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAdjustment() {
		return adjustment;
	}

	public void setAdjustment(BigDecimal adjustment) {
		if (adjustment == null) {
			toString();
		}
		this.adjustment = adjustment;
	}

	public BigDecimal getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(BigDecimal newPrice) {
		this.newPrice = newPrice;
	}

	@Override
	public String toString() {
		return String.format("|%-18s|%-11d|%-11.2f|", productType, quantity, newPrice);
	}
}
