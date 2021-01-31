package com.flipkart.bean;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to interact with variables present in Professor Class
 * using Getters and Setters Methods
 * @author JEDI04
 */

@XmlRootElement(name = "professor")
public class Professor extends User {
	
	@NotNull
    @Size(min = 2, max = 30, message = "The length of department should be between 2 to 25")
	private String department;
    
	private ArrayList<String> listOfCourseAssigned = new ArrayList<>();
	
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public ArrayList<String> getListOfCourseAssigned() {
		return listOfCourseAssigned;
	}

	public void setListOfCourseAssigned(ArrayList<String> listOfCourseAssigned) {
		this.listOfCourseAssigned = listOfCourseAssigned;
	}
	
}
