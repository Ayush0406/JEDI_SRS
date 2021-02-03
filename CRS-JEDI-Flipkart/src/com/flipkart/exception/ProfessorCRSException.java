package com.flipkart.exception;

/**
 * 
 * @author JEDI04
 * 
 * Exception class designed to handle errors corresponding to Professor related
 * issues
 *
 */
public class ProfessorCRSException extends Exception {
	private static final long serialVersionUID = 3L;
	private String message;
	
	public ProfessorCRSException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
