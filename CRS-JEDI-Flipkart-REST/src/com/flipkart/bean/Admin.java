package com.flipkart.bean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Class to interact with variables present in Admin Class
 * using Getters and Setters Methods
 * @author JEDI04
 */
import com.flipkart.bean.User;

/**
 * Manages the setting, retrieving and updating the attributes
 * of an Admin
 * Validators NotNull-checks for null values, Size checks for
 * length of string and DecimalMin/DecimalMax checks for integer range
 * @author JEDI04
 *
 */
public class Admin extends User {
	private int adminId;
	
	@Size(min = 2, max = 30, message = "The length of Username should be between 2 to 25")
	private String adminName;
	
	@Pattern(message = "Invalid Email Address->" +
            "Valid emails:user@gmail.com or my.user@domain.com etc.",
            regexp = "^[a-zA-Z0-9_!#$%&�*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	private String adminEmail;
	
	public int getAdminId() {
		return adminId;
	}
	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getAdminEmail() {
		return adminEmail;
	}
	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}
}
