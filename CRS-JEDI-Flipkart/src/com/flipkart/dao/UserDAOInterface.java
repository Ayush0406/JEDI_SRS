package com.flipkart.dao;

import com.flipkart.bean.User;

/**
 * Interface to Show the structure of
 * UserDAO Operation Class
 * @author JEDI04
 */
public interface UserDAOInterface {

	public int verifyLoginCredentials(String email, String password);
	public boolean checkEmailAvailability(String email);
	public int registerUser(User user, String password);
}
