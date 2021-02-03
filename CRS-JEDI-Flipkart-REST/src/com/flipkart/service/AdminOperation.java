package com.flipkart.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.flipkart.bean.Admin;
import com.flipkart.bean.Course;
import com.flipkart.bean.Professor;
import com.flipkart.dao.AdminDAOOperation;
import com.flipkart.exception.AdminCRSException;
import com.flipkart.exception.ProfessorCRSException;
import com.flipkart.exception.StudentCRSException;
import com.flipkart.util.ValidationOperation;

/**
 * Performs All the Admin Operations and Extends Admin Interface
 * 
 * @author JEDI04
 *
 */

public class AdminOperation implements AdminInterface {

	public static Logger logger = Logger.getLogger(AdminOperation.class);
	AdminDAOOperation adminDAO = AdminDAOOperation.getInstance();

	// Method for lazy loading
	private static AdminOperation instance = null;

	private AdminOperation() {

	}

	synchronized public static AdminOperation getInstance() {
		if (instance == null) {
			instance = new AdminOperation();
		}
		return instance;
	}

	/**
	 * Generate Report Card for a given Student Id
	 * 
	 * @param sid Student ID of the student whose report card is to be generated.
	 * 
	 * @return Report card as an ArrayList of JsonObjects.
	 * @throws AdminCRSException,Exception
	 */
	@Override
	public ArrayList<JSONObject> generateReportCard(int sid) throws AdminCRSException, Exception
	{
		ArrayList<JSONObject> reportCard =  adminDAO.printGrades(sid);
		return reportCard;
	}
	
	
	/**
	 * Generated a lost of all the students who have successfully registered for courses and completed their requirements.
	 * 
	 * @return ArrayList of JsonObjects containing the name and id of the registered students.
	 * @throws AdminCRSException,Exception
	 */
	public ArrayList<JSONObject> getRegisteredStudents() throws AdminCRSException, Exception
	{
		return adminDAO.getRegisteredStudents();
	}

	/**
	 * Adds New Professor to the table and throws Exception if Failed to Add
	 * 
	 * @return Returns 1 if Professor is successfully added, else 0.
	 * @throws AdminCRSException,Exception
	 */
	public int addProfessor(String password, Professor prof) throws AdminCRSException, Exception{
		if(adminDAO.verifyEmail(prof.getEmail()) == false)
		{
			throw new AdminCRSException("Professor already exists in the database.");
		}
		
		int res = 0;
		res = adminDAO.addProfessor(password, prof);
		if (res == 1) {
			logger.info("Professor successfully added.");
		} else {
			logger.info("Unable to add professor.");
		}
		return res;
	}

	/**
	 * Adds New Admin to the table and throws Exception if Failed to Add
	 * 
	 * @param admin	Admin object 
	 * @param pwd1 Password
	 * @return Returns 1 if admin is successfully added, else 0.
	 * @throws AdminCRSException,Exception
	 */
	@Override
	public int addAdmin(Admin admin, String pwd1) throws AdminCRSException, Exception{
		if(adminDAO.verifyEmail(admin.getEmail()) == false)
		{
			throw new AdminCRSException("Admin already exists in the database.");
		}
		
		int res = adminDAO.addAdmin(pwd1, admin);
		if (res == 1) {
			logger.info("Admin added successfully");
		} else {
			logger.info("Unable to add admin");
		}
		return res;
	}

	/**
	 * Approves New Student's Registration
	 * @param studentID
	 * @return true if student is approved successfully else returns false
	 * @throws AdminCRSException,Exception
	 */
	@Override
	public boolean approveStudents(int studentID) throws AdminCRSException,Exception {
		// TODO Auto-generated method stub
		return adminDAO.approveStudent(studentID);
	}

	/**
	 * Deletes course from course catalog and 
	 * course tables with the entered course id by admin
	 * @param courseId Course Id of the course to be deleted.
	 * @return True if the course is successfully deleted. False otherwise.
	 * @throws AdminCRSException,Exception
	 */
	@Override
	public boolean deleteCourse(int courseID) throws AdminCRSException,Exception {
		// TODO Auto-generated method stub
		return adminDAO.deleteCourse(courseID);
	}

	/**
	 * Allot course to the professor
	 * @param courseID
	 * @param professorID
	 * @return true if course is alloted successfully else return false
	 * @throws AdminCRSException,Exception
	 */
	@Override
	public boolean allotCourse(int courseID, int professorID) throws AdminCRSException,Exception {
		return adminDAO.allotCourses(courseID,professorID);
	}

	/**
	 * Opens registration window.
	 * 
	 * @throws Exception
	 */
	public boolean startRegistrationWindow() throws Exception {
		boolean res = false;
		res = adminDAO.startRegistrationWindow();

		return res;
	}

	/**
	 * Closes registration window.
	 * @throws Exception 
	 */
	public boolean closeRegistrationWindow() throws Exception {
		boolean res = false;
		res = adminDAO.closeRegistrationWindow();

		return res;
	}

	/**
	 * Displays list of registered students
	 * 
	 * @param course Course object
	 * @throws ProfessorCRSException,Exception
	 */

	public boolean addCourse(Course course) throws AdminCRSException, Exception {
		// TODO Auto-generated method stub
		//// Exception related to existing course ID
		boolean res = false;
		res = adminDAO.addCourse(course);
		if (res == false)
			throw new AdminCRSException("Failed to Add New Course");
		logger.info("====================================");
		logger.info("Add Course Status : " + res);
		logger.info("====================================");

		return res;
	}
	
}
