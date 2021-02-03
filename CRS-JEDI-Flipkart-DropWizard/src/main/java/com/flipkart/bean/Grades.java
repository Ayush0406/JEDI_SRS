package com.flipkart.bean;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Class to interact with variables present in Grades Class
 * using Getters and Setters Methods
 * @author JEDI04
 */


public class Grades {
	
	@DecimalMin(value = "100", message = "studentID has to be of 3 digits")
	@Digits(fraction = 0, integer = 3)
	@NotNull 
	private int studentId;
	
	@DecimalMin(value = "100", message = "courseID has to be of 3 digits")
	@Digits(fraction = 0, integer = 3)
	@NotNull 
	private int courseID;
	
	@NotNull 
	@Size(min = 2, max = 30, message = "The length of Course Name should be between 2 to 25")
	private String courseName;
	
	@NotNull 
	@Size(min = 2, max = 30, message = "The length of Grade should be between 2 to 25")
	private String grade;
	
	
	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getCourseId(){
		return courseID;
	}

	public void setCourseID(int courseID) {
		this.courseID = courseID;
	}

	public String getCourseName(){
		return courseName;
	}

	public void setCourseName(String courseName){
		this.courseName = courseName;
	}

	public String getGrade(){
		return grade;
	}

	public void setGrade(String grade){
		this.grade = grade;
	}
	
}
