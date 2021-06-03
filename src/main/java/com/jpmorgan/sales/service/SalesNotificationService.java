package com.jpmorgan.sales.service;

import com.jpmorgan.sales.model.Message;
import static com.jpmorgan.sales.report.GenerateReport.printAdjustment;
import static com.jpmorgan.sales.report.GenerateReport.printTenSalesReport;

public class SalesNotificationService {
	private final MessageService messageService = new MessageService();

	/**
	 * 1. Parse the input message and store it in Message object. 
	 * 2. Add the message object to messageList if the object is not null.
	 * 3. If the message contains adjustMent operation (i.add/substract/multiply) then adjust the price.
	 * 4. Display total sales and price for 10 sales and adjustment report for 50 sales.
	 */
	public void processSalesNotificationMessage(String inputMessage) {
		Message message = messageService.parseInputMessage(inputMessage);
		if (message != null) {
			messageService.addMessage(message);
			if (message.isPriceAdjustmentRequired()) {
				messageService.adjustPrice(message);
			}
		}
		if (messageService.getMessageList().size() % 10 == 0) {
			printTenSalesReport(messageService.getProducts());
		}
		if (messageService.getMessageList().size() % 50 == 0) {
			printAdjustment(messageService.getAdjustMentMessages());
		}
	}
}
