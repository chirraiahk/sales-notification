package com.jpmorgan.sales.service;

import static com.jpmorgan.sales.model.AdjustmentOperation.ADD;
import static com.jpmorgan.sales.model.AdjustmentOperation.MULTIPLY;
import static com.jpmorgan.sales.model.AdjustmentOperation.SUB;
import static com.jpmorgan.sales.model.AdjustmentOperation.UNKNOWN;
import static com.jpmorgan.sales.util.SalesNotificationUtil.getPrecession;
import static com.jpmorgan.sales.util.SalesNotificationUtil.parsePrice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.jpmorgan.sales.model.Message;
import com.jpmorgan.sales.model.Product;

public class MessageService {

	private final List<Message> messageList = new ArrayList<>();
	private final ProductService productService = new ProductService();

	public void addMessage(Message message) {
		if (message != null) {
			messageList.add(message);
			if (!message.isPriceAdjustmentRequired()) {
				if (message.getProduct() != null
						&& productService.getProductsMap().containsKey(message.getProduct().getProductType())) {
					productService.updateProductDetailsForExistingProductType(message);
				} else {
					productService.createProductForNewProductType(message);
				}
			}
		}
	}

	public Message parseInputMessage(String message) {
		if (message != null && !message.isEmpty()) {
			String[] messageArray = message.trim().split("\\s+");
			String word = messageArray[0];
			if (word.matches("Add|Subtract|Multiply")) {
				return parseAdjustmentMessage(message);
			} else if (word.matches("^\\d+")) {
				return parseQuantityMessage(message);
			} else if (messageArray.length == 3 && messageArray[1].contains("at")) {
				return parsePriceMessage(message);
			} else {
				System.err.println("Incorrect sales input message");
			}
		}
		return null;
	}

	private Message parsePriceMessage(String msg) {
		if (msg != null && !msg.isEmpty()) {
			String[] messageArray = msg.trim().split("\\s+");
			if (messageArray.length != 3) {
				return null;
			}
			String type = parseProductType(messageArray[0]);
			double price = parsePrice(messageArray[2]);
			Product product = new Product(type, getPrecession(price), 1);
			return createMessage(msg, product, false);
		}
		return null;
	}

	private Message parseQuantityMessage(String msg) {
		if (msg != null && !msg.isEmpty()) {
			String[] messageArray = msg.trim().split("\\s+");
			if (messageArray.length != 7) {
				return null;
			}
			String productType = parseProductType(messageArray[3]);
			double price = parsePrice(messageArray[5]);
			int quantity = Integer.parseInt(messageArray[0]);
			Product product = new Product(productType, getPrecession(price), quantity);
			return createMessage(msg, product, false);
		}
		return null;
	}

	private Message parseAdjustmentMessage(String msg) {
		String[] messageArray = msg.trim().split("\\s+");
		if (messageArray.length != 3) {
			return null;
		}
		Product product = new Product();
		double price = parsePrice(messageArray[1]);
		product.setAdjustment(getPrecession(price));
		String type = parseProductType(messageArray[2]);
		product.setProductType(type);
		Message message = createMessage(msg, product, true);
		setAdjustMentOperationType(messageArray[0], message);
		return message;
	}

	private void setAdjustMentOperationType(String operation, Message message) {
		if ("add".equalsIgnoreCase(operation)) {
			message.setOperation(ADD);
		} else if ("subtract".equalsIgnoreCase(operation)) {
			message.setOperation(SUB);
		} else if ("multiply".equalsIgnoreCase(operation)) {
			message.setOperation(MULTIPLY);
		} else {
			message.setOperation(UNKNOWN);
		}

	}

	/**
	 * transforming the product type to maintain the uniformity if the message
	 * contains singular/plural names of products. Example : we may get mango and
	 * mangoes. apple and apples.. etc
	 * 
	 */
	public static String parseProductType(String productType) {
		String parsedType = "";
		String typeWithoutLastChar = productType.substring(0, productType.length() - 1);
		if (productType.endsWith("o")) {
			parsedType = String.format("%soes", typeWithoutLastChar);
		} else if (productType.endsWith("y")) {
			parsedType = String.format("%sies", typeWithoutLastChar);
		} else if (productType.endsWith("h")) {
			parsedType = String.format("%shes", typeWithoutLastChar);
		} else if (!productType.endsWith("s")) {
			parsedType = String.format("%ss", productType);
		} else {
			parsedType = String.format("%s", productType);
		}
		return parsedType.toLowerCase();
	}

	private Message createMessage(String msg, Product product, boolean isAdjustmentRequired) {
		Message message = new Message();
		message.setInputMessage(msg);
		message.setProduct(product);
		message.setPriceAdjustmentRequired(isAdjustmentRequired);
		return message;
	}

	public List<Message> getMessageList() {
		return messageList;
	}

	public List<Message> getAdjustmentMessages() {
		return messageList.stream().filter(Message::isPriceAdjustmentRequired).collect(Collectors.toList());
	}

	public void adjustPrice(Message message) {
		productService.adjustProductPrice(message);
	}

	public Collection<Product> getProducts() {
		return productService.getProducts();
	}
}
