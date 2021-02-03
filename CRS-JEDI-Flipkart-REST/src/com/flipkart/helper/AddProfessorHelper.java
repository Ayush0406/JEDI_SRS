package com.flipkart.helper;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.flipkart.bean.Professor;

/**
 * Helper class to facilitate the additon of a new professor from the Admin's console.
 * Validators NotNull-checks for null values, Size checks for
 * length of string and DecimalMin/DecimalMax checks for integer range
 * @author JEDI04
 * 
 */
public class AddProfessorHelper {
	@NotNull
	@Valid
	private Professor prof;
	
	@NotNull
	@Size(min = 4, max = 30, message = "Password length should be greater than 4 characters")
	private String password;
	
	public Professor getProf() {
		return prof;
	}
	public void setProf(Professor prof) {
		this.prof = prof;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
