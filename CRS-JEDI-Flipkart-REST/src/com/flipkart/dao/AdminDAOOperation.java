package com.flipkart.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import javax.validation.ValidationException;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import com.flipkart.bean.Admin;
import com.flipkart.bean.Course;
import com.flipkart.bean.Professor;
import com.flipkart.util.DBConnection;
import com.google.gson.annotations.JsonAdapter;
import com.flipkart.constant.SQLQueriesConstant;
import com.flipkart.exception.AdminCRSException;

/**
 * Data Access object for Admin class which is responsible for all the
 * interactions that happen with the SQL database.
 * 
 * @author JEDI04
 *
 */

public class AdminDAOOperation implements AdminDAOInterface {

	private static Logger logger = Logger.getLogger(AdminDAOOperation.class);
	Connection connection = DBConnection.getConnection();
	PreparedStatement ps = null;

//	code for lazy loading
	private static AdminDAOOperation instance = null;

	private AdminDAOOperation() {

	}

	/**
	 * Singleton implementation.
	 * 
	 * @return Instance of AdminDAOOperatioon class.
	 */

	synchronized public static AdminDAOOperation getInstance() {
		if (instance == null) {
			instance = new AdminDAOOperation();
		}
		return instance;
	}

	/**
	 * 
	 * Verifies Email Address at the time of registration
	 * 
	 * 
	 * @param email Email address of the new user to be added.
	 * @return False if the email already exists in database else returns true.
	 * @throws AdminCRSException,Exception
	 */

	public boolean verifyEmail(String email) {
		try {
			String sqlQuery = SQLQueriesConstant.GET_STUDENT_ID_BY_EMAIL;
			ps = connection.prepareStatement(sqlQuery);

			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return false;
			}
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return true;
	}

	/**
	 * addAdmin is used to add the new Admin details in Credentials table and Admin
	 * table
	 * 
	 * @param password User provided password for the new Admin to be added.
	 * @param admin    Other details of the admin sent inside Admin bean class by
	 *                 using its attributes.
	 * 
	 * @return Returns 1 if new admin is successfully added. Else returns 0.
	 * @throws AdminCRSException,Exception
	 */
	public int addAdmin(String password, Admin admin) throws AdminCRSException, Exception{
		String sqlQuery = SQLQueriesConstant.ADD_USER_TO_CREDENTIALS;
		ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);

		ps.setInt(1, 3);
		ps.setString(2, admin.getEmail());
		ps.setString(3, password);
		ps.setInt(4, 1);
		ps.setString(5, admin.getAddress());
		ps.setInt(6, admin.getAge());
		ps.setString(7, admin.getGender());
		ps.setString(8, admin.getContact());
		ps.setString(9, admin.getNationality());

		int res = ps.executeUpdate();
		if(res == 0)
			throw new AdminCRSException("Failed to add admin to the credentials table.");
		ResultSet rs = ps.getGeneratedKeys();

		rs.next();
		admin.setUserId(rs.getInt(1));

		String adminQuery = SQLQueriesConstant.ADD_NEW_ADMIN;
		ps = connection.prepareStatement(adminQuery);
		ps.setInt(1, admin.getUserId());
		ps.setString(2, admin.getUserName());
		ps.setString(3, admin.getEmail());
		int res2 = ps.executeUpdate();
		
		if(res2 == 0)
			throw new AdminCRSException("Failed to add new professor to the professor table.");
		return 1;
	}

	/**
	 * It is responsible for adding the new Professor in Credentials table and
	 * Professor table
	 * 
	 * @param password New password of the professor.
	 * @param prof     Details of the new professor sent using the Professor bean
	 *                 class' attributes.
	 * @return Returns 1 if professor is successfully added. Else returns 0.
	 * 
	 */	
	public int addProfessor(String password, Professor prof) throws AdminCRSException, Exception{
		String credQuery = SQLQueriesConstant.ADD_USER_TO_CREDENTIALS;
		ps = connection.prepareStatement(credQuery, Statement.RETURN_GENERATED_KEYS);
		ps.setInt(1, 2);
		ps.setString(2, prof.getEmail());
		ps.setString(3, password);
		ps.setInt(4, 1);
		ps.setString(5, prof.getAddress());
		ps.setInt(6, prof.getAge());
		ps.setString(7, prof.getGender());
		ps.setString(8, prof.getContact());
		ps.setString(9, prof.getNationality());

		int res = ps.executeUpdate();
		if(res == 0)
			throw new AdminCRSException("Failed to add professor to the credentials table.");
		ResultSet rs = ps.getGeneratedKeys();

		rs.next();
		prof.setUserId(rs.getInt(1));

		String profQuery = SQLQueriesConstant.ADD_NEW_PROFESSOR;
		ps = connection.prepareStatement(profQuery);
		ps.setInt(1, prof.getUserId());
		ps.setString(2, prof.getUserName());
		ps.setString(3, prof.getEmail());
		ps.setString(4, prof.getRole());
		ps.setString(5, prof.getDepartment());
		int res2 = ps.executeUpdate();
		
		if(res2 == 0)
			throw new AdminCRSException("Failed to add new professor to the professor table.");
		return 1;
	}

	/**
	 * printGrades Prints the Report of card of a particular Student given its
	 * Student ID.
	 * 
	 * @param studentId StudentID of the student whose report card is to be
	 *                  generated.
	 * @throws AdminCRSException,Exception
	 */

	
	public ArrayList<JSONObject> printGrades(int studentId) throws AdminCRSException, Exception{
		ArrayList<JSONObject> grades = new ArrayList<JSONObject>();
			String str = SQLQueriesConstant.GET_GRADES_BY_STUDENT_ID;
			ps = connection.prepareStatement(str);
			ps.setInt(1, studentId);
			ResultSet rs = ps.executeQuery();

			String name = "";
			if (rs.next())
				name = rs.getString("sname");
			else {
				logger.info("Student with ID " + studentId + " has not registered for any course!");
				logger.info("=======================================");
				throw new AdminCRSException("Student has not registered for any courses.");
			}
			logger.info("=======================================");
			logger.info("        Report Card of " + name + " :");
			logger.info("=======================================");
			logger.info("CourseID     Course Name     Grade");
			if (name != "") {
				do {
					logger.info(rs.getInt("courseId") + "          " + rs.getString("name") + "         "
							+ rs.getString("grade"));

					JSONObject grade = new JSONObject();
					grade.put("courseId", rs.getInt("courseId"));
					grade.put("name", rs.getString("name"));
					grade.put("grade", rs.getString("grade"));

					grades.add(grade);
				} while (rs.next());
			}
			logger.info("=======================================");

		return grades;
	}

	/**
	 * Approves New Student's Registration
	 * @param studentID
	 * @return true if student is approved successfully else returns false
	 * @throws AdminCRSException,Exception
	 */
	@Override
	public boolean approveStudent(int studentID) throws AdminCRSException,Exception {
		String str = SQLQueriesConstant.GET_ISAPPROVED_FROM_CREDENTIALS;
		ps = connection.prepareStatement(str);
		ps.setInt(1,studentID);
		ResultSet rs = ps.executeQuery();
		if(rs.next()==false) {
			throw new AdminCRSException("No student with id: "+ studentID + " exists");
		}
		if(rs.getInt("isApproved")==1) {
			throw new AdminCRSException("Student with id: " + studentID + " already Approved");
		}
		str = SQLQueriesConstant.UPDATE_USER_IN_CREDENTIALS;
		ps = connection.prepareStatement(str);
		ps.setInt(1,studentID);
		ps.executeUpdate();
		return true;
	}

	/**
	 * Add a new course to the courseCatalog only if course id provided by the Admin
	 * is unique
	 * 
	 * @param course The details of the course encapsulated in the course class
	 *               attributes.
	 * 
	 * 
	 * @return True if the course is successfully added. False otherwise.
	 * @throws SQLException, Exception
	 */
	public boolean addCourse(Course course) throws SQLException, Exception {

		String str = SQLQueriesConstant.ADD_COURSE_IN_CATALOG;
		ps = connection.prepareStatement(str);
		ps.setString(1, course.getCourseName());
		ps.setInt(2, course.getCourseID());
		ps.setInt(3, course.getCredits());
		int val = ps.executeUpdate();
		if (val > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Deletes a course from course catalog and course tables where id matches the
	 * given courseId.
	 * @param courseId Course Id of the course to be deleted.
	 * @return True if the course is successfully deleted. False otherwise.
	 * @throws AdminCRSException,Exception
	 */
	public boolean deleteCourse(int courseId) throws AdminCRSException,Exception {
		String str = SQLQueriesConstant.GET_COURSE_INFO_BY_ID;
		ps = connection.prepareStatement(str);
		ps.setInt(1, courseId);
		ResultSet rs = ps.executeQuery();
		if(rs.next()==false)
			throw new AdminCRSException("No Course with ID: " + courseId + " exists");
		str = SQLQueriesConstant.DELETE_COURSE_IN_CATALOG;
		ps = connection.prepareStatement(str);
		ps.setInt(1, courseId);
		int val = ps.executeUpdate();
		str = SQLQueriesConstant.DELETE_COURSE_BY_ID;
		ps = connection.prepareStatement(str);
		ps.setInt(1, courseId);
		ps.executeUpdate();
		if (val > 0) {
			return true;
		}
		throw new AdminCRSException("Failed to Delete course");
	}

	/**
	 * Assign course with particular course ID to the corresponding professor with
	 * ID entered by the Admin. In case, already some other professor is assigned to
	 * the course, it is modified.
	 * @param courseId    
	 * @param professorID 
	 * @return true if course is alloted successfully else return false
	 * @throws AdminCRSException,Exception
	 */
	public boolean allotCourses(int courseId, int professorID) throws AdminCRSException,Exception {
		String str = SQLQueriesConstant.GET_PROFESSOR_NAME_BY_ID;
		ps = connection.prepareStatement(str);
		ps.setInt(1, professorID);
		ResultSet rs = ps.executeQuery();
		if (rs.next() == false) {
			throw new AdminCRSException("No professor with ID: " + professorID + " exists");
		}
		str = SQLQueriesConstant.GET_COURSE_NAME_BY_ID;
		ps = connection.prepareStatement(str);
		ps.setInt(1, courseId);
		rs = ps.executeQuery();
		if (rs.next()) {
			str = SQLQueriesConstant.UPDATE_PROFESSOR_IN_COURSE;
			ps = connection.prepareStatement(str);
			ps.setInt(1, professorID);
			ps.setInt(2, courseId);
			int status = ps.executeUpdate();
			if (status > 0) {
				return true;
			} else {
				throw new AdminCRSException("Some Error Occurred");
			}
		} else {
			str = SQLQueriesConstant.GET_COURSE_CATALAOG_QUERY;
			ps = connection.prepareStatement(str);
			ps.setInt(1, courseId);
			rs = ps.executeQuery();
			if (rs.next() == false) {
				throw new AdminCRSException("No Course with ID: " + courseId + " exists");
			}
			do {
				str = SQLQueriesConstant.ADD_NEWCOURSE_IN_COURSE;
				ps = connection.prepareStatement(str);
				ps.setInt(1, courseId);
				ps.setString(2, rs.getString("courseName"));
				ps.setInt(3, professorID);
				ps.setInt(4, rs.getInt("credits"));
				ps.executeUpdate();
			} while (rs.next());
			return true;
		}
	}

	/**
	 * Opens registration window.
	 * @throws SQLException, Exception
	 */
	public boolean startRegistrationWindow() throws SQLException, Exception {

		boolean res = false;

		ps = connection.prepareStatement(SQLQueriesConstant.OPEN_REGISTRATION_WINDOW);
		if (ps.executeUpdate() > 0) {
			res = true;
			logger.info("Registration Window Started.\n");
		}

		return res;
	}

	/**
	 * Closes registration window.
	 * @throws SQLException, Exception
	 */
	public boolean closeRegistrationWindow() throws SQLException, Exception {
		boolean res = false;
		ps = connection.prepareStatement(SQLQueriesConstant.CLOSE_REGISTRATION_WINDOW);
		if (ps.executeUpdate() > 0) {
			res = true;
			logger.info("Registration Window Closed.\n");
		}
		return res;
	}

	/**
	 * Shows list of all courses
	 * @return ArrayList of JSON object containing courses information
	 * @throws AdminCRSException,Exception
	 */
	public ArrayList<JSONObject> showcourses() throws AdminCRSException,Exception{
		ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
		String s = SQLQueriesConstant.GET_COURSE_INFO;
		ps = connection.prepareStatement(s);
		ResultSet courl = ps.executeQuery();
		if (courl.next() == false) {
			throw new AdminCRSException("Sorry No Courses to Show");
		} else {
			do {
				JSONObject obj = new JSONObject();
				obj.put("courseId", courl.getInt("courseId"));
				obj.put("courseName", courl.getString("courseName"));
				arr.add(obj);
			} while (courl.next());
		}
		if(arr.size()==0)
			throw new AdminCRSException("Sorry No Courses to Show");
		return arr;
	}

	/**
	 * Shows list of all professors
	 * @return ArrayList of JSON object containing Professor information
	 * @throws AdminCRSException,Exception
	 */
	public ArrayList<JSONObject> showprofessor() throws AdminCRSException,Exception{
		ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
		String s = SQLQueriesConstant.GET_PROFESSOR_INFO_BY_ID;
		ps = connection.prepareStatement(s);
		ResultSet profl = ps.executeQuery();
		if (profl.next() == false) {
			throw new AdminCRSException("Sorry No Professor to Show");
		} else {
			do {
				JSONObject obj = new JSONObject();
				obj.put("id", profl.getInt("id"));
				obj.put("name", profl.getString("name"));
				arr.add(obj);
			} while (profl.next());
		}
		if(arr.size()==0)
			throw new AdminCRSException("Sorry No Professor to Show");
		return arr;
	}
	
	
	/**
	 * Shows list of all unapproved students
	 * @return ArrayList of JSON object containing student information who are unapproved.
	 * @throws AdminCRSException,Exception
	 */
	public ArrayList<JSONObject> showunapproved() throws AdminCRSException,Exception{
		ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
		String s = SQLQueriesConstant.GET_STUDENT_DETAILS;
		ps = connection.prepareStatement(s);
		ResultSet stul = ps.executeQuery();
		if (stul.next() == false) {
			throw new AdminCRSException("No student left Unapproved");
		} else {
			do {
				JSONObject obj = new JSONObject();
				obj.put("Student Name",stul.getString("name"));
				obj.put("Student ID",stul.getInt("id"));
				obj.put("Email" , stul.getString("email"));
				obj.put("Address" , stul.getString("address"));
				obj.put("Age" , stul.getInt("age"));
				obj.put("Gender" , stul.getString("gender"));
				obj.put("Contact Number" , stul.getString("contact"));
				obj.put("Nationality" , stul.getString("nationality"));
				arr.add(obj);
			} while (stul.next());
		}
		if(arr.size()==0)
			throw new AdminCRSException("No student left Unapproved");
		return arr;
	}

	/**
	 * Displays List of all registered students
	 * 
	 * @return ArrayList with details of registered students.
	 * @throws AdminCRSException,Exception
	 */
	public ArrayList<JSONObject> getRegisteredStudents() throws AdminCRSException, Exception{
		ArrayList<JSONObject> students = new ArrayList<JSONObject>();

		String  s = SQLQueriesConstant.GET_REGISTERED_STUDENTS;
		ps = connection.prepareStatement(s);
		ResultSet stl = ps.executeQuery();
		logger.info("=======================================");
		if (stl.next() == false) {
			throw new AdminCRSException("No Student Exists.");
		} else {
			logger.info(String.format("%-10s\t%-15s", "StudentID", "Student Name"));
			do {
				logger.info(String.format("%-10d\t%-15s", stl.getInt("id"), stl.getString("name")));
				JSONObject student = new JSONObject();
				student.put("studentId", stl.getInt("id"));
				student.put("name", stl.getString("name"));
				
				students.add(student);
			} while (stl.next());
			logger.info("=======================================");
			return students;
		}		

	}
}
