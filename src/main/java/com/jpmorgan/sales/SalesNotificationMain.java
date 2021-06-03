package com.jpmorgan.sales;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.jpmorgan.sales.service.SalesNotificationService;

public class SalesNotificationMain {

	public static void main(String[] arg) {
		SalesNotificationService notificationService = new SalesNotificationService();
		BufferedReader inputFile = null;
		try {
			String line;
			inputFile = new BufferedReader(new FileReader("src/main/resources/sales-input.txt")); // read the file
			while ((line = inputFile.readLine()) != null) {
				notificationService.processSalesNotificationMessage(line); // process line by line
			}
		} catch (IOException exception) {
			System.err.println("error while reading input");
			exception.printStackTrace();
		} finally {
			if (inputFile != null) {
				try {
					inputFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
