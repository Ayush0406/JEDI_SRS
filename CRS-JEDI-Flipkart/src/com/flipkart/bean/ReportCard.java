package com.flipkart.bean;

import java.util.ArrayList;

/**
 * Class to interact with variables present in Report Class
 * using Getters and Setters Methods
 * @author JEDI04
 */
public class ReportCard {
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
