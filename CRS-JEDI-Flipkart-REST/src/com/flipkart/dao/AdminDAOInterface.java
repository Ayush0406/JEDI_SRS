/**
 * 
 */
package com.flipkart.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONObject;

import com.flipkart.bean.Admin;
import com.flipkart.bean.Course;
import com.flipkart.bean.Professor;
import com.flipkart.exception.AdminCRSException;

/**
 * Interface to Show the structure of
 * AdminDAO Operation Class
 * @author JEDI04
 */
public interface AdminDAOInterface {
	public ArrayList<JSONObject> printGrades(int studentId) throws AdminCRSException, Exception;
	public boolean verifyEmail(String email) throws AdminCRSException;
	public int addAdmin(String password, Admin admin) throws AdminCRSException, Exception;
	public int addProfessor(String password, Professor prof) throws AdminCRSException, Exception;
	public boolean allotCourses(int courseId, int professorID) throws AdminCRSException,Exception;
	public boolean addCourse(Course course) throws SQLException, Exception; 
	public boolean deleteCourse(int courseId) throws AdminCRSException,Exception; 
	public boolean approveStudent(int studentID) throws AdminCRSException,Exception;
	public boolean startRegistrationWindow() throws SQLException, Exception;
	public boolean closeRegistrationWindow() throws SQLException, Exception;
	public ArrayList<JSONObject> showcourses() throws AdminCRSException,Exception;
	public ArrayList<JSONObject> showprofessor() throws AdminCRSException,Exception;
	public ArrayList<JSONObject> showunapproved() throws AdminCRSException,Exception;
}
