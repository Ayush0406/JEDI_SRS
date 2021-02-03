/**
 * 
 */
package com.flipkart.util;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.flipkart.service.AuthCredentialSystemOperations;

/**
 * 
 * Class containing validation methods
 * @author JEDI04
 */
public class ValidationOperation {
	private static Logger logger = Logger.getLogger(ValidationOperation.class);
	private static AuthCredentialSystemOperations authentication = AuthCredentialSystemOperations.getInstance();
	
	/**
	 * Method to take a valid email input
	 * 
	 * @return Returns the email that was taken as input
	 */
	public static String readEmail() {
		String str = "-1";
		Scanner sc = null;
		try {
			sc = new Scanner(System.in);
			boolean available = false;
			do {
				str = sc.nextLine();
				if(str.compareTo("-1") == 0) 
					break;
				
				available = authentication.checkEmailAvailability(str);
				if(!available) {
					logger.info(">>>> Enter a new email or -1 to exit. <<<<<<<");
				}
			}while(!available);
		}
		catch (Exception e) {
			logger.warn(e.getMessage());
		}
		return str;
	}
	
	/**
	 * Method to take a valid password input
	 * 
	 * @return Returns the password that was taken as input
	 */
	public static String readPassword() {
		String pwd1 = "-1", pwd2 = "-1";
		Scanner sc = null;
		try {
			sc = new Scanner(System.in);
			do {
				logger.info("Enter password: ");
				pwd1 = sc.nextLine();
				
				logger.info("Re enter password: ");
				pwd2 = sc.nextLine();
				
				if(pwd1.compareTo(pwd2) != 0) {
					logger.info("Passwords dont match.\n\n");
				}
			}while(pwd1.compareTo(pwd2) != 0);
		}
		catch(Exception e) {
			logger.warn(e.getMessage() + "\n");
		}
		return pwd1;
	}
}
