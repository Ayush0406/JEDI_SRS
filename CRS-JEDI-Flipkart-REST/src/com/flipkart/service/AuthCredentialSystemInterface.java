package com.flipkart.service;


import com.flipkart.bean.Admin;
import com.flipkart.bean.Professor;
import com.flipkart.bean.Student;
import com.flipkart.bean.User;

public interface AuthCredentialSystemInterface {
	public int login(String username, String password);
	
	public boolean checkEmailAvailability(String email);

	public void registerStudent(User user, Student student, String password) throws Exception;
	
	public int registerUser(User user, String password) throws Exception;
}
