package com.jpmorgan.sales.service;

import static com.jpmorgan.sales.util.SalesNotificationUtil.getPrecession;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.jpmorgan.sales.model.AdjustmentOperation;
import com.jpmorgan.sales.model.Message;
import com.jpmorgan.sales.model.Product;

public class ProductService {
	
	private final Map<String, Product> productMap = new HashMap<>();
	
	public Collection<Product> getProducts() {
		return productMap.values();
	}

	public Map<String, Product> getProductsMap() {
		return Collections.unmodifiableMap(productMap);
	}
	
	public void updateProductDetailsForExistingProductType(Message message) {
		Product product = productMap.get(message.getProduct().getProductType());
		product.setQuantity(product.getQuantity() + message.getProduct().getQuantity());
		product.setProductType(message.getProduct().getProductType());
		product.setNewPrice(getPrecession(product.getNewPrice().doubleValue()
				+ message.getProduct().getQuantity() * message.getProduct().getPrice().doubleValue()));
	}

	public void adjustProductPrice(Message message) {
		Product product = productMap.get(message.getProduct().getProductType());
		message.getProduct().setPrice(product.getNewPrice());
		applyAdjustment(message.getOperation(), product, message.getProduct().getAdjustment());
		message.getProduct().setQuantity(product.getQuantity());
		message.getProduct().setNewPrice(product.getNewPrice());
	}
	
	public void createProductForNewProductType(Message message) {
		Product product = new Product();
		product.setQuantity(message.getProduct().getQuantity());
		product.setProductType(message.getProduct().getProductType());
		product.setNewPrice(
				getPrecession(message.getProduct().getQuantity() * message.getProduct().getPrice().doubleValue()));
		productMap.put(message.getProduct().getProductType(), product);
	}
	
	public void applyAdjustment(AdjustmentOperation operation, Product product, BigDecimal adjustmentPrice) {
		double newPrice = 0.0;
		if (AdjustmentOperation.ADD == operation) {
			newPrice = applyAddOperation(product, adjustmentPrice);
		} else if (AdjustmentOperation.SUB == operation) {
			newPrice = applySubtractOperation(product, adjustmentPrice);
		} else if (AdjustmentOperation.MULTIPLY == operation) {
			newPrice = applyMultiplyOperation(product, adjustmentPrice);
		}
		product.setNewPrice(getPrecession(newPrice));
	}

	private double applyMultiplyOperation(Product product, BigDecimal adjustmentPrice) {
		double newPrice = 0.0;
		if (product.getNewPrice().doubleValue() != 0.0) {
			newPrice = product.getNewPrice().doubleValue() * product.getQuantity() * adjustmentPrice.doubleValue();
		} else {
			newPrice = product.getQuantity() * adjustmentPrice.doubleValue();
		}

		return newPrice;
	}

	private double applySubtractOperation(Product product, BigDecimal adjustmentPrice) {
		return product.getNewPrice().doubleValue() - product.getQuantity() * adjustmentPrice.doubleValue();
	}

	private double applyAddOperation(Product product, BigDecimal adjustmentPrice) {
		return product.getNewPrice().doubleValue() + product.getQuantity() * adjustmentPrice.doubleValue();
	}
}
