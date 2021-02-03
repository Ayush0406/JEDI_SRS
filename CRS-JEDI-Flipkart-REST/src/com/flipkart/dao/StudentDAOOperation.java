package com.flipkart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.flipkart.bean.Course;
import com.flipkart.bean.Grades;
import com.flipkart.constant.SQLQueriesConstant;
import com.flipkart.exception.StudentCRSException;

import org.apache.log4j.Logger;

import com.flipkart.bean.Student;
import com.flipkart.util.DBConnection;

/**
 * Primary Class undertaking all database queries related to Student Operations
 * 
 * @author JEDI 04
 */

public class StudentDAOOperation implements StudentDAOInterface {
	private static Logger logger = Logger.getLogger(StudentDAOOperation.class);
	Connection connection = null;
	PreparedStatement ps = null;
	CoursesDAOOperation coursesDaoOperation = CoursesDAOOperation.getInstance();

	private static StudentDAOOperation instance = null;

	private StudentDAOOperation()
	{

	}

	synchronized public static StudentDAOOperation getInstance()
	{
		if(instance == null)
		{
			instance = new StudentDAOOperation();
		}
		return instance;
	}

	
	
	/**
	 * Method to get the student object from student ID
	 * 
	 * @param student The student object containing the student related information to make an entry
	 * @throws StudentCRSException, Exception
	 */
	public Student getStudentByID(int studentID) throws StudentCRSException, Exception {
		Student student = null;
		connection = DBConnection.getConnection();
		ps = connection.prepareStatement(SQLQueriesConstant.GET_STUDENT_BY_ID_QUERY);
		
		ps.setInt(1, studentID);
		
		ResultSet result = ps.executeQuery();
		if(!result.next()) {
			throw new StudentCRSException("Student with id: " + studentID + " doesn't exist");
		}
		
		student = new Student();
		student.setEmail(result.getString("email"));
		student.setBranch(result.getString("branch"));
		student.setUserId(result.getInt("id"));
		boolean bool = result.getInt("isRegistered") == 1 ? true : false;
		student.setIsRegistered(bool);
		student.setUserName(result.getString("name"));
		student.setRollNo(result.getInt("rollno"));	
		student.setApproved(result.getBoolean("isApproved"));
		student.setPaymentStatus(result.getBoolean("paymentStatus"));
		student.setAge(result.getInt("age"));
		student.setContact(result.getString("contact"));
		student.setNationality(result.getString("nationality"));
		student.setGender(result.getString("gender"));
		student.setAddress(result.getString("address"));
		return student;
	}
	
	
	
	/**
	 * Method to fetch Student Object from database given email of student
	 * 
	 * @param email String containing the email of the student whose object is to be fetched
	 * @return The Student Object containing all the information regarding to a student
	 */
	public Student getStudentByEmail(String email) {
		Student student = new Student();
		try {
			connection = DBConnection.getConnection();
			ps = connection.prepareStatement(SQLQueriesConstant.GET_STUDENT_BY_EMAIL_QUERY);
			
			ps.setString(1, email);
			
			ResultSet result = ps.executeQuery();
			result.next();
			student.setEmail(result.getString("email"));
			student.setBranch(result.getString("branch"));
			student.setUserId(result.getInt("id"));
			boolean bool = result.getInt("isRegistered") == 1 ? true : false;
			student.setIsRegistered(bool);
			student.setUserName(result.getString("name"));
			student.setRollNo(result.getInt("rollno"));	
			student.setApproved(result.getBoolean("isApproved"));
			student.setPaymentStatus(result.getBoolean("paymentStatus"));
			student.setAge(result.getInt("age"));
			student.setContact(result.getString("contact"));
			student.setNationality(result.getString("nationality"));
			student.setGender(result.getString("gender"));
			student.setAddress(result.getString("address"));
		}
		catch(SQLException e) {
			logger.warn(e.getMessage() + "\n");
		}
		catch(Exception e) {
			logger.warn(e.getMessage() + "\n");
		}
		return student;
	}

	/**
	 * Method to add a entry to the RegisteredCourses with the studentID and the courseID, i.e to add 
	 * a course to a students registered courses list.
	 * 
	 * @param student The student object containing the student related information to make an entry
	 * @param courseID The courseID with which an entry is to be made
	 * @throws StudentCRSException, Exception
	 */
	public void addCourse(Student student, int courseID) throws StudentCRSException, Exception {
		Course course = coursesDaoOperation.getCourseByID(courseID);
		if(course == null) {
			logger.info(">>>>>>>> Invalid Course ID <<<<<<<<<<\n");
			throw new StudentCRSException("Course ID: " + courseID + " is invalid.");
		}
		else if(getCourse(student, courseID)){
			logger.info("You have already added this course.\n");
			throw new StudentCRSException("You have already added this course.");
		}
		else if(coursesDaoOperation.noOfEnrolledStudents(courseID)>=10){
			logger.info("Course " + courseID + " is full. Please add some other course.\n");
			throw new StudentCRSException("Course " + courseID + " is full. Please add some other course.");
		}
		else if(getNoOfCourses(student) == 6) {
			throw new StudentCRSException("You have already registered for 6 courses can't add more.");
		}
		else{
			connection = DBConnection.getConnection();
			ps = connection.prepareStatement(SQLQueriesConstant.ADD_COURSE_STUDENT_QUERY);

			ps.setInt(1,student.getUserId());
			ps.setInt(2,courseID);

			int added = ps.executeUpdate();
			if(added>0){
				logger.info("Course " + courseID + " added successfully.\n");
			}

			addCourseToGrades(student.getUserId(),courseID);
		}
	}

	/**
	 * Method to remove a course from a students registered courses list. Entry is removed from 
	 * RegisteredCourses table
	 * 
	 * @param student Student Object containing information regarding the student
	 * @param courseID The courseID whose entry is to be removed
	 * @throws StudentCRSException, Exception
	 */
	public void dropCourse(Student student, int courseID) throws StudentCRSException, Exception {
		Course course = coursesDaoOperation.getCourseByID(courseID);
		if(course == null) {
			logger.info(">>>>>>>> Invalid Course ID <<<<<<<<<<\n");
			throw new StudentCRSException("Course ID: " + courseID + " is invalid.");
		}
		else if(!getCourse(student, courseID)){
			logger.info("You have not registered for this course.\n");
			throw new StudentCRSException("You have not registered for this course.");
		}
		else if(getNoOfCourses(student) == 4) {
			throw new StudentCRSException("You have registered for only 4 courses can't drop any course.");
		}
		else {
			connection = DBConnection.getConnection();
			ps = connection.prepareStatement(SQLQueriesConstant.DROP_COURSE_STUDENT_QUERY);

			ps.setInt(1, student.getUserId());
			ps.setInt(2, courseID);

			int dropped = ps.executeUpdate();
			if(dropped>0) {
				logger.info("Course " + courseID + " deleted successfully\n");
			}

			deleteCourseFromGrades(student.getUserId(),courseID);
		}
	}

	/**
	 * Method to get the number of courses enrolled by a student
	 * 
	 * @param student Student object containing the student information necessary to fetch the number of courses
	 * @return The number of courses a student is enrolled in
	 */
	public int getNoOfCourses(Student student){
		int count = 0;
		try{
			connection = DBConnection.getConnection();
			ps = connection.prepareStatement(SQLQueriesConstant.GET_NO_OF_COURSES_QUERY);

			ps.setInt(1,student.getUserId());
			ResultSet resultSet = ps.executeQuery();
			if(resultSet.next()) {
				count = resultSet.getInt(1);
			}
		}
		catch(SQLException e) {
			logger.warn(e.getMessage() + "\n");
		}
		catch(Exception e) {
			logger.warn(e.getMessage() + "\n");
		}
		return count;
	}
	
	/**
	 * Method to fetch the course object for a student given the courseID
	 * 
	 * @param student Student object containing the student information
	 * @param courseID The course ID whose course data is to be fetched
	 * @return Returns true if such a course if found else false.
	 */
	public boolean getCourse(Student student, int courseID){
		int count = 0;

		try{
			connection = DBConnection.getConnection();
			ps = connection.prepareStatement(SQLQueriesConstant.GET_COURSE_QUERY);

			ps.setInt(1,student.getUserId());
			ps.setInt(2,courseID);

			ResultSet resultSet = ps.executeQuery();
			if(resultSet.next()) {
				count = resultSet.getInt(1);
			}
		}
		catch(SQLException e) {
			logger.warn(e.getMessage() + "\n");
		}
		catch(Exception e) {
			logger.warn(e.getMessage() + "\n");
		}

		return count>0;
	}

	/**
	 * Method to add an entry to the grades table for a student
	 * 
	 * @param studentID The id of the student whose entry is to be made
	 * @param courseID The id of the course whose entry is to be made
	 */
	public void addCourseToGrades(int studentID, int courseID){
		try{
			connection = DBConnection.getConnection();
			ps = connection.prepareStatement(SQLQueriesConstant.ADD_COURSE_TO_GRADES_QUERY);

			ps.setInt(1,studentID);
			ps.setInt(2,courseID);

			ps.executeUpdate();
		}
		catch(SQLException e) {
			logger.warn(e.getMessage() + "\n");
		}
		catch(Exception e) {
			logger.warn(e.getMessage() + "\n");
		}
	}
	
	/**
	 * Method to delete an entry to the grades table for a student
	 * 
	 * @param studentID The id of the student whose entry is to be deleted
	 * @param courseID The id of the course whose entry is to be deleted
	 */
	public void deleteCourseFromGrades(int studentID, int courseID){
		try{
			connection = DBConnection.getConnection();
			ps = connection.prepareStatement(SQLQueriesConstant.DELETE_COURSE_FROM_GRADES_QUERY);

			ps.setInt(1,studentID);
			ps.setInt(2,courseID);

			ps.executeUpdate();
		}
		catch(SQLException e) {
			logger.warn(e.getMessage() + "\n");
		}
		catch(Exception e) {
			logger.warn(e.getMessage() + "\n");
		}
	}


	/**
	 * Method to fetch an ArrayList containing the Course objects of courses that a student is enrolled in.
	 * 
	 * @param student Student object containing the student information
	 * @return An ArrayList containing the Course objects of courses that a student is enrolled in
	 * @throws StudentCRSException, Exception
	 */
	public ArrayList<Course> getEnrolledCourses(Student student) throws StudentCRSException, Exception {
		ArrayList<Course> enrolledCourses = new ArrayList<>();
		connection = DBConnection.getConnection();
		ps = connection.prepareStatement(SQLQueriesConstant.GET_ENROLLED_COURSES_QUERY);

		ps.setInt(1,student.getUserId());
		ResultSet resultSet = ps.executeQuery();
		while(resultSet.next()){
			Course course = new Course();
			course.setCourseID(resultSet.getInt("courseID"));
			course.setCourseName(resultSet.getString("name"));
			course.setCredits(resultSet.getInt("credits"));
			enrolledCourses.add(course);
		}
		return enrolledCourses;
	}

	/**
	 * Method to set the registration status of a student to true.
	 * 
	 * @param student Student object containing the student information
	 */
	public void setRegistrationStatus(Student student){
		try{
			connection = DBConnection.getConnection();
			ps = connection.prepareStatement(SQLQueriesConstant.SET_REGISTRATION_STATUS_QUERY);

			ps.setInt(1,student.getUserId());
			ps.executeUpdate();
		}
		catch(SQLException e) {
			logger.warn(e.getMessage() + "\n");
		}
		catch(Exception e) {
			logger.warn(e.getMessage() + "\n");
		}
	}

	/**
	 * Method to fetch the Grade objects of given student
	 * 
	 * @param studentID The studentId whose grades is to be fetched
	 * @return returns an ArrayList of Grades containing grade objects corresponding to grades obtained by a student
	 * @throws StudentCRSException, Exception
	 */
	public ArrayList<Grades> getGrades(int studentID) throws StudentCRSException, Exception {
		ArrayList<Grades> grades = new ArrayList<>();
		connection = DBConnection.getConnection();
		ps = connection.prepareStatement(SQLQueriesConstant.GET_GRADES_QUERY);

		ps.setInt(1,studentID);
		ResultSet resultSet = ps.executeQuery();
		boolean f = false;
		while(resultSet.next()){
			Grades grade = new Grades();
			grade.setCourseID(resultSet.getInt("courseId"));
			grade.setCourseName(resultSet.getString("courseName"));
			grade.setGrade(resultSet.getString("grade"));
			grade.setStudentId(resultSet.getInt("studentId"));
			grades.add(grade);
			f = true;
		}
		if(!f) throw new StudentCRSException("Student hasn't registered for any course yet.");
		return grades;
	}
	
	/**
	 * Method to set the payment status of a Student to true
	 * 
	 * @param student Student object containing the student information
	 * @throws StudentCRSException, Exception
	 */
	public void setPaymentStatus(Student student, String method) throws StudentCRSException, Exception {
		connection = DBConnection.getConnection();
		ps = connection.prepareStatement(SQLQueriesConstant.SET_PAYMENT_STATUS_QUERY);

		ps.setString(1, method);
		ps.setInt(2, student.getUserId());
		ps.executeUpdate();
	}

	/**
	 * Method to register a student with a given id
	 * 
	 * @param student Student object containing the student information
	 * @param id The id that the student is to be registered with.
	 * @throws StudentCRSException, Exception
	 */
	@Override
	public void registerStudent(Student student, int id) throws StudentCRSException, Exception {
		connection = DBConnection.getConnection();
		ps = connection.prepareStatement(SQLQueriesConstant.REGISTER_STUDENT_QUERY);

		ps.setInt(1, id);
		ps.setString(2, student.getUserName());
		ps.setString(3, student.getEmail());
		ps.setInt(4, student.getRollNo());
		ps.setString(5, student.getBranch());
		ps.setBoolean(6, student.getIsRegistered());
		ps.setBoolean(7, student.getPaymentStatus());

		ps.executeUpdate();
	}
	
	/**
	 * Method to update the info of a student in the student and user database
	 * 
	 * @param student Student object containing the student information
	 */
	public void updateInfo(Student student) throws Exception {
		connection = DBConnection.getConnection();
		
		ps = connection.prepareStatement(SQLQueriesConstant.UPDATE_STUDENT_CREDENTIAL_QUERY);

		ps.setInt(1,student.getAge());
		ps.setString(2,student.getAddress());
		ps.setString(3,student.getContact());
		ps.setString(4,student.getGender());
		ps.setString(5,student.getNationality());
		ps.setInt(6,student.getUserId());

		ps.executeUpdate();
		
		logger.info(student.getUserId());

		ps = connection.prepareStatement(SQLQueriesConstant.UPDATE_STUDENT_QUERY);
		ps.setString(1, student.getUserName());
		ps.setInt(2,student.getUserId());

		ps.executeUpdate();
	}

	/**
	 * To retrieve the status of registration window from database
	 *
	 * @return True if registration window is open else False
 	 */
	public boolean getRegistrationSystemStatus(){
		boolean status = false;

		try{
			connection = DBConnection.getConnection();
			ps = connection.prepareStatement(SQLQueriesConstant.GET_REGISTRATION_WINDOW_STATUS_QUERY);

			ResultSet resultSet = ps.executeQuery();

			if(resultSet.next()){
				status = resultSet.getBoolean("isOpen");
			}
		}
		catch (SQLException e){
			logger.warn(e.getMessage() + '\n');
		}
		catch (Exception e){
			logger.warn(e.getMessage() + '\n');
		}

		return status;
	}
}
