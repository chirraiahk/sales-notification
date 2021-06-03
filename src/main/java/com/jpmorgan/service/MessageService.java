package com.jpmorgan.service;

import static com.jpmorgan.sales.util.SalesNotificationUtil.getPrecession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jpmorgan.sales.model.Message;
import com.jpmorgan.sales.model.Product;

public class MessageService {

	private final List<Message> messageList = new ArrayList<>();
	private final Map<String, Product> productMap = new HashMap<>();

	public void addMessage(Message message) {
		if (message != null) {
			messageList.add(message);
			if (!message.isPriceAdjustmentRequired()) {
				if (message.getProduct() != null && productMap.containsKey(message.getProduct().getProductType())) {
					updateProductDetailsForExistingProductType(message);
				} else {
					createProductForNewProductType(message);
				}
			}
		}
	}

	private void createProductForNewProductType(Message message) {
		Product product = new Product();
		product.setQuantity(message.getProduct().getQuantity());
		product.setProductType(message.getProduct().getProductType());
		product.setNewPrice(
				getPrecession(message.getProduct().getQuantity() * message.getProduct().getPrice().doubleValue()));
		productMap.put(message.getProduct().getProductType(), product);
	}

	private void updateProductDetailsForExistingProductType(Message message) {
		Product product = productMap.get(message.getProduct().getProductType());
		product.setQuantity(product.getQuantity() + message.getProduct().getQuantity());
		product.setProductType(message.getProduct().getProductType());
		product.setNewPrice(getPrecession(product.getNewPrice().doubleValue()
				+ message.getProduct().getQuantity() * message.getProduct().getPrice().doubleValue()));
	}

}
