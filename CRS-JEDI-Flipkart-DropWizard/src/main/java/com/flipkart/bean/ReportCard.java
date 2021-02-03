package com.flipkart.bean;

import java.util.ArrayList;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

/**
 * Class to interact with variables present in Report Class
 * using Getters and Setters Methods
 * @author JEDI04
 */
public class ReportCard {
	@DecimalMin(value = "100", message = "studentID has to be of 3 digits")
	@Digits(fraction = 0, integer = 3)
	private int studentId;
	private ArrayList<String> grades = new ArrayList<>();
	
	
	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public ArrayList<String> getGrades() {
		return grades;
	}

	public void setGrades(ArrayList<String> grades) {
		this.grades = grades;
	}
	
	
}
