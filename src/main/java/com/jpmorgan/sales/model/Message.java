package com.jpmorgan.sales.model;

public class Message {

	private String inputMessage;
	private boolean isPriceAdjustmentRequired;
	private Product product;
	private AdjustmentOperation operation;

	public String getInputMessage() {
		return inputMessage;
	}

	public void setInputMessage(String inputMessage) {
		this.inputMessage = inputMessage;
	}

	public boolean isPriceAdjustmentRequired() {
		return isPriceAdjustmentRequired;
	}

	public void setPriceAdjustmentRequired(boolean isPriceAdjustmentRequired) {
		this.isPriceAdjustmentRequired = isPriceAdjustmentRequired;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public AdjustmentOperation getOperation() {
		return operation;
	}

	public void setOperation(AdjustmentOperation operation) {
		this.operation = operation;
	}

	@Override
	public String toString() {
		return "Performed " + operation + " " + product.getAdjustment() + " to " + product.getQuantity()
				+ " " + product.getProductType() + " and price adjusted from " + product.getPrice().setScale(2) + " to "
				+ product.getNewPrice().setScale(2);
	}

}
