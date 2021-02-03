package com.flipkart.service;

import java.util.Scanner;


import org.apache.log4j.Logger;

import com.flipkart.bean.Admin;
import com.flipkart.bean.Course;
import com.flipkart.bean.Professor;
import com.flipkart.dao.AdminDAOOperation;
import com.flipkart.exception.AdminCRSException;
import com.flipkart.util.ValidationOperation;

/**
 * Performs All the Admin Operations
 * and Extends Admin Interface
 * @author JEDI04
 *
 */

public class AdminOperation implements AdminInterface {

	public static Logger logger = Logger.getLogger(AdminOperation.class);
	AdminDAOOperation adminDAO = AdminDAOOperation.getInstance();
	
	// Method for lazy loading
	private static AdminOperation instance = null;
	
	private AdminOperation()
	{
		
	}
	
	synchronized public static AdminOperation getInstance()
	{
		if(instance == null)
		{
			instance = new AdminOperation();
		}
		return instance;
	}

	/**
	 * Generate Report Card for a given Student Id
	 * 
	 */
	@Override
	public void generateReportCard() {
		// TODO Auto-generated method stub
		if(adminDAO.getStudents()==false) {
			logger.info("   =======================================");
			return ;
		}
		logger.info("Please Enter Student ID to Generate Report Card");
		Scanner sc = new Scanner(System.in);
		int sid = sc.nextInt();
		adminDAO.printGrades(sid);
		return;
	}

	/**
	 * Adds New Professor to the table and throws Exception 
	 * if Failed to Add
	 */
	@Override
	public void addProfessor() {
		// TODO Auto-generated method stub
		try {
			Scanner sc = new Scanner(System.in);
	
			logger.info("Enter the new email : ");
			
			String email = ValidationOperation.readEmail();
			if (email.equals("-1")){
				throw new AdminCRSException("Professor Not Added\n");
			}
			String pwd1 = ValidationOperation.readPassword();
	
			logger.info("Please enter name : ");
			Professor prof = new Professor();
			prof.setEmail(email);
			prof.setUserName(sc.nextLine());
			logger.info("Enter designation : ");
			prof.setRole(sc.nextLine());
			logger.info("Enter department : ");
			prof.setDepartment(sc.nextLine());
			logger.info("Enter address : ");
			prof.setAddress(sc.nextLine());
			logger.info("Enter Age : ");
			prof.setAge(Integer.parseInt(sc.nextLine()));
			logger.info("Enter Gender : (male/female) : ");
			prof.setGender(sc.nextLine());
			logger.info("Enter contact number : ");
			prof.setContact(sc.nextLine());
			logger.info("Enter nationality : ");
			prof.setNationality(sc.nextLine());
	
			int res = adminDAO.addProfessor(pwd1, prof);
			if (res == 1) {
				logger.info("Professor successfully added.");
			} else {
				logger.info("Unable to add professor.");
			}
		}catch(AdminCRSException e) {
			logger.warn(e.getMessage());
		}
		catch(Exception e) {
			logger.warn(e.getMessage());
		}
	}

	/**
	 * Adds New Admin to the table and throws Exception
	 * if Failed to Add
	 */
	@Override
	public void addAdmin() {
		// TODO Auto-generated method stub
		
		try {
			Scanner sc = new Scanner(System.in);
			logger.info("Enter the new email : ");
	
			
			String email = ValidationOperation.readEmail();
			if (email.equals("-1")){
				throw new AdminCRSException("Admin Not Added\n");
			}
			String pwd1 = ValidationOperation.readPassword();
	
			Admin admin = new Admin();
			admin.setEmail(email);
			logger.info("Enter name : ");
			admin.setUserName(sc.nextLine());
			logger.info("Enter address : ");
			admin.setAddress(sc.nextLine());
			logger.info("Enter Age : ");
			admin.setAge(Integer.parseInt(sc.nextLine()));
			logger.info("Enter Gender : (male/female) : ");
			admin.setGender(sc.nextLine());
			logger.info("Enter contact number : ");
			admin.setContact(sc.nextLine());
			logger.info("Enter nationality : ");
			admin.setNationality(sc.nextLine());
	
			int res = adminDAO.addAdmin(pwd1, admin);
			if (res == 1) {
				logger.info("Admin added successfully");
			} else {
				throw new AdminCRSException("Unable to add admin");
			}
		}catch(AdminCRSException e)
		{
			logger.warn(e.getMessage());
		}
		catch(Exception e)
		{
			logger.warn(e.getMessage());
		}
	}

	/**
	 * Approves New Student's Registration After Viewing
	 * the details 
	 */
	@Override
	public void approveStudents() {
		// TODO Auto-generated method stub
		adminDAO.approveStudent();
	}

	/**
	 * Adds course to catalog with the entered details
	 * throws exception if duplicate course id is
	 * provided by the admin
	 */
	@Override
	public void addCourse() {
		// TODO Auto-generated method stub
		//// Exception related to existing course ID
		try {
			logger.info("In addCourse method");
			Course course = new Course();
			Scanner sc = new Scanner(System.in);
			logger.info("Enter Course Name");
			course.setCourseName(sc.nextLine());
			logger.info("Enter Course Id");
			course.setCourseID(sc.nextInt());
			logger.info("Enter Number of Credits");
			course.setCredits(sc.nextInt());
			boolean res = adminDAO.addCourse(course);
			if(res==false)
				throw new AdminCRSException("Failed to Add New Course");
			logger.info("====================================");
			logger.info("Add Course Status : " + res);
			logger.info("====================================");
		} catch (AdminCRSException e) {
			logger.info(e.getMessage());
		}
		catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * Deletes course from course catalog and 
	 * course tables with the entered course id 
	 * by admin
	 */
	@Override
	public void deleteCourse() {
		// TODO Auto-generated method stub
		try {
			adminDAO.showcourses();
			Scanner sc = new Scanner(System.in);
			logger.info("Please enter CourseId to be deleted from the above list");
			int x = sc.nextInt();
			boolean res = adminDAO.deleteCourse(x);
			if(res==false)
				throw new AdminCRSException("Failed to Delete Course");
			logger.info("====================================");
			logger.info("Delete Course Status : " + res);
			logger.info("====================================");
		} catch (AdminCRSException e) {
			logger.info(e.getMessage());
		}
		catch (Exception e) {
			logger.info(e.getMessage());
		}
	}
	
	
	
	/**
	 * Allot course to professor with the entered 
	 * course ID and Professor ID
	 * by Admin
	 */
	@Override
	public void allotCourse() {
		Scanner sc = new Scanner(System.in);
		try {
			adminDAO.showcourses();
			adminDAO.showprofessor();
			logger.info("Please enter Professor ID from the above list");
			int professorId = sc.nextInt();
			logger.info("Please enter Course ID from the above list");
			int courseId = sc.nextInt();
			adminDAO.allotCourses(courseId,professorId);
		} catch(Exception e) {
			logger.info(e.getMessage());
		}
	}
	
	/**
	 * Opens registration window.
	 */
	public void startRegistrationWindow()
	{
		try {
			adminDAO.startRegistrationWindow();
		}
		catch(Exception e)
		{
			logger.info(e.getMessage());
		}
	}
	
	
	/**
	 * Closes registration window.
	 */
	public void closeRegistrationWindow()
	{
		try {
			adminDAO.closeRegistrationWindow();
		}
		catch(Exception e)
		{
			logger.info(e.getMessage());
		}
	}
	
	/**
	 * Displays list of registered students
	 */
}
