package com.flipkart.bean;

/**
 * Class to interact with variables present in Admin Class
 * using Getters and Setters Methods
 * @author JEDI04
 */
import com.flipkart.bean.User;

/**
 * Manages the setting, retrieving and updating the attributes
 * of an Admin
 * @author JEDI04
 *
 */
public class Admin extends User {
	private int adminId;
	private String adminName;
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
