package com.flipkart.exception;

/**
 * 
 * @author JEDI04
 * 
 * Exception class designed to handle errors corresponding to Common issues
 *
 */
public class CommonException extends Exception {
	private static final long serialVersionUID = 2L;
	private String message;
	
	public CommonException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}

}
