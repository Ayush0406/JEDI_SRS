package com.flipkart.service;

import java.util.ArrayList;

import javax.validation.Valid;

import org.json.JSONObject;

import com.flipkart.bean.Admin;
import com.flipkart.bean.Professor;
import com.flipkart.exception.AdminCRSException;
import com.flipkart.exception.StudentCRSException;

/**
 * Interface to Show the structure of
 * Admin Operation Class
 * @author JEDI04
 */

public interface AdminInterface {
	public ArrayList<JSONObject> generateReportCard(int sid) throws StudentCRSException, Exception;
	public boolean allotCourse(int courseID, int professorID) throws AdminCRSException, Exception;
	public int addProfessor(String password, Professor prof) throws AdminCRSException,Exception;
	public int addAdmin(Admin admin, String pwd1) throws AdminCRSException, Exception;
	public boolean approveStudents(int studentID) throws AdminCRSException, Exception;
	public boolean deleteCourse(int courseID) throws AdminCRSException, Exception;
	public boolean startRegistrationWindow() throws Exception;
	public boolean closeRegistrationWindow() throws Exception;
}
