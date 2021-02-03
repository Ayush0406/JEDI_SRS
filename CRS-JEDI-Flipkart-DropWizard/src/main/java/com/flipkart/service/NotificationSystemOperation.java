package com.flipkart.service;

import org.apache.log4j.Logger;

/**
 * Handles notifications sent to a student
 * @author JEDI04
 *
 */

public class NotificationSystemOperation{
	public static Logger logger = Logger.getLogger(NotificationSystemOperation.class);
	
	/**
	 * To notify course registration successful
	 */
	public static void courseRegistrationComplete()
	{
		logger.info("Notification: Course registration successful. Please proceed to make payment.\n");
	}
	
	/**
	 * To notify payment successful
	 */
	public static void paymentSuccessful()
	{
		logger.info("Notification: Payment successful.");
	}
	
	/**
	 * To notify that student registration has been approved by Admin
	 */
	public static void studentApproved()
	{
		logger.info("Notification: Congratulations! You have been approved.");
	}
}
