package com.jpmorgan.sales.report;

import java.util.Collection;
import java.util.List;

import com.jpmorgan.sales.model.Message;
import com.jpmorgan.sales.model.Product;

public class GenerateReport {
	public static void printTenSalesReport(Collection<Product> products) {
		System.out.println("10 sales appended to log");
		System.out.println("*************** Log Report *****************");
		System.out.println("|Product           |Quantity   |Value      |");
		products.forEach(System.out::println);
		System.out.println("-------------------------------------------");
		System.out.println(String.format("|%-30s|%-11.2f|", "Total Sales",
				products.stream().mapToDouble(p -> p.getNewPrice().doubleValue()).sum()));
		;
		System.out.println("-------------------------------------------");
		System.out.println("End\n\n");
	}

	public static void printAdjustment(List<Message> message) {
		System.out.println("The following are the adjustment records made;\n");
		message.forEach(System.out::println);
		System.out.println("\n\n");
	}
}
