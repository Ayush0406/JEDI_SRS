package com.flipkart.exception;


/**
 * 
 * @author JEDI04
 * 
 * Exception class designed to handle errors corresponding to Student related
 * issues
 *
 */
public class StudentCRSException extends Exception {
	private static final long serialVersionUID = 4L;
	private String message;
	
	public StudentCRSException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
