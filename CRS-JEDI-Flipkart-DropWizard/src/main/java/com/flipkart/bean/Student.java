package com.flipkart.bean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Class to interact with variables present in Student Class
 * using Getters and Setters Methods
 * Validators NotNull-checks for null values, Size checks for
 * length of string and DecimalMin/DecimalMax checks for integer range
 * @author JEDI04
 */

public class Student extends User{
	
	/**
	 * Manages the setting, retrieving and updating the attributes
	 * of a Student
	 * @author JEDI04
	 *
	 */
	
	@NotNull 
	private int rollNo;
	
	private boolean isRegistered;
	
	@NotNull 
	@Size(min = 2, max = 30, message = "The length of Branch should be between 2 to 25")
	private String branch;
	
	
	private boolean paymentStatus;
	
	public boolean getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public void setRollNo(int rollNo) {
		this.rollNo = rollNo;
	}
	
	public int getRollNo() {
		return rollNo;
	}
	
	public boolean getIsRegistered() {
		return isRegistered;
	}
	
	public void setIsRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}
	
	
	public String getBranch() {
		return branch;
	}
	
	public void setBranch(String branch) {
		this.branch = branch;
	}
}
