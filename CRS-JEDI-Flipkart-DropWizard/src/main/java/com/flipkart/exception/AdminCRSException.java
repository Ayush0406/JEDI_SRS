package com.flipkart.exception;

/**
 * 
 * Exception class designed to handle errors corresponding to Administrator related
 * issues
 * @author JEDI04
 */
public class AdminCRSException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;
	
	public AdminCRSException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
