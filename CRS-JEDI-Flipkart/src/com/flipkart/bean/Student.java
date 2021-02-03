package com.flipkart.bean;
import java.util.ArrayList;

/**
 * Class to interact with variables present in Student Class
 * using Getters and Setters Methods
 * @author JEDI04
 */

public class Student extends User{
	
	/**
	 * Manages the setting, retrieving and updating the attributes
	 * of a Student
	 * @author JEDI04
	 *
	 */
	private int rollNo;
	private boolean isRegistered;
	private ArrayList<Integer> enrolledCourses = new ArrayList<>();
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
	
	public ArrayList<Integer> getEnrolledCourses() {
		return enrolledCourses;
	}
	
	public void setEnrolledCourses(ArrayList<Integer> enrolledCourses) {
		this.enrolledCourses = enrolledCourses;
	}
	
	public String getBranch() {
		return branch;
	}
	
	public void setBranch(String branch) {
		this.branch = branch;
	}
}
