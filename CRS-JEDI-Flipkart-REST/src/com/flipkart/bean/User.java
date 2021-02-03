package com.flipkart.bean;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to interact with variables present in User Class
 * using Getters and Setters Methods
 * Validators NotNull-checks for null values, Size checks for
 * length of string and DecimalMin/DecimalMax checks for integer range
 * @author JEDI04
 */

@XmlRootElement(name = "user")
public class User {
	@NotNull
	protected int userId;
	
	@NotNull
    @Size(min = 2, max = 30, message = "The length of Username should be between 2 to 25")
	protected String userName;
    
	@NotNull
    @Size(min = 2, max = 30, message = "The length of role should be between 2 to 25")
	protected String role;
    
    @NotNull
    @Pattern(message = "Invalid Email Address->" +
            "Valid emails:user@gmail.com or my.user@domain.com etc.",
            regexp = "^[a-zA-Z0-9_!#$%&�*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	protected String email;
    
	protected boolean isLoggedIn;
	protected boolean isApproved;
	
	@NotNull
    @Size(min = 2, max = 30, message = "The length of Address should be between 2 to 25")
	protected String address;
    
    @NotNull
    @DecimalMin(value = "10", message = "Age shall be minimum of 10 years")
	protected int age;
    
	@NotNull
    @Size(min = 2, max = 30, message = "The length of Gender should be between 2 to 25")
	protected String gender;
	
	@NotNull
    @Size(min = 10, max = 30, message = "The length of Contact should be between 10 to 12")
	protected String contact;
    
	@NotNull
    @Size(min = 2, max = 30, message = "The length of Nationality should be between 2 to 25")
	protected String nationality;

    
    
    
	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

}

