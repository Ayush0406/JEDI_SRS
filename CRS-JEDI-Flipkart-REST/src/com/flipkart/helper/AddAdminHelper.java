package com.flipkart.helper;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.flipkart.bean.Admin;
import com.flipkart.bean.Professor;


/**
 * Helper class to facilitate the additon of a new admin from the Admin's console.
 * Validators NotNull-checks for null values, Size checks for
 * length of string and DecimalMin/DecimalMax checks for integer range
 * @author JEDI04
 */
public class AddAdminHelper {
	
	@NotNull
	@Valid
	private Admin admin;
	
	@NotNull
	@Size(min = 4, max = 30, message = "Password length should between 4 and 30 characters.")
	private String password;
	
	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	
}
