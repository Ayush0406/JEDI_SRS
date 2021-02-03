package com.flipkart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.flipkart.bean.Course;
import com.flipkart.bean.Grades;
import com.flipkart.constant.SQLQueriesConstant;
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
	 */
	public void addCourse(Student student, int courseID){
		Course course = coursesDaoOperation.getCourseByID(courseID);
		if(course == null) {
			logger.info(">>>>>>>> Invalid Course ID <<<<<<<<<<\n");
		}
		else if(getCourse(student, courseID)){
			logger.info("You have already added this course.\n");
		}
		else if(coursesDaoOperation.noOfEnrolledStudents(courseID)>=10){
			logger.info("Course " + courseID + " is full. Please add some other course.\n");
		}
		else{
			try{
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
			catch(SQLException e) {
				logger.warn(e.getMessage() + "\n");
			}
			catch(Exception e) {
				logger.warn(e.getMessage() + "\n");
			}
		}
	}

	/**
	 * Method to remove a course from a students registered courses list. Entry is removed from 
	 * RegisteredCourses table
	 * 
	 * @param student Student Object containing information regarding the student
	 * @param courseID The courseID whose entry is to be removed
	 */
	public void dropCourse(Student student, int courseID){
		Course course = coursesDaoOperation.getCourseByID(courseID);
		if(course == null) {
			logger.info(">>>>>>>> Invalid Course ID <<<<<<<<<<\n");
		}
		else if(!getCourse(student, courseID)){
			logger.info("You have not registered for this course.\n");
		}
		else {
			try {
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
			catch(SQLException e) {
				logger.warn(e.getMessage() + "\n");
			}
			catch(Exception e) {
				logger.warn(e.getMessage() + "\n");
			}
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
	 * @return An ArrayList containing the Course objects of courses that a student is enrolled in.
	 */
	public ArrayList<Course> getEnrolledCourses(Student student){
		ArrayList<Course> enrolledCourses = new ArrayList<>();

		try{
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
		}
		catch(SQLException e) {
			logger.warn(e.getMessage() + "\n");
		}
		catch(Exception e) {
			logger.warn(e.getMessage() + "\n");
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
	 */
	public ArrayList<Grades> getGrades(int studentID){
		ArrayList<Grades> grades = new ArrayList<>();

		try{
			connection = DBConnection.getConnection();
			ps = connection.prepareStatement(SQLQueriesConstant.GET_GRADES_QUERY);

			ps.setInt(1,studentID);
			ResultSet resultSet = ps.executeQuery();
			while(resultSet.next()){
				Grades grade = new Grades();
				grade.setCourseID(resultSet.getInt("courseId"));
				grade.setCourseName(resultSet.getString("courseName"));
				grade.setGrade(resultSet.getString("grade"));
				grade.setStudentId(resultSet.getInt("studentId"));
				grades.add(grade);
			}
		}
		catch(SQLException e) {
			logger.warn(e.getMessage() + "\n");
		}
		catch(Exception e) {
			logger.warn(e.getMessage() + "\n");
		}

		return grades;
	}
	
	/**
	 * Method to set the payment status of a Student to true
	 * 
	 * @param student Student object containing the student information
	 */
	public void setPaymentStatus(Student student, String method) {
		try{
			connection = DBConnection.getConnection();
			ps = connection.prepareStatement(SQLQueriesConstant.SET_PAYMENT_STATUS_QUERY);

			ps.setInt(1, student.getUserId());
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
	 * Method to register a student with a given id
	 * 
	 * @param student Student object containing the student information
	 * @param id The id that the student is to be registered with.
	 */
	@Override
	public void registerStudent(Student student, int id) {
		try {
			connection = DBConnection.getConnection();
			ps = connection.prepareStatement(SQLQueriesConstant.REGISTER_STUDENT_QUERY);

			ps.setInt(1, id);
			ps.setString(2,  student.getUserName());
			ps.setString(3,  student.getEmail());
			ps.setInt(4, student.getRollNo());
			ps.setString(5, student.getBranch());
			ps.setBoolean(6, student.getIsRegistered());
			ps.setBoolean(7, student.getPaymentStatus());
			
			ps.executeUpdate();
		}
		catch(SQLException e) {
			logger.warn(e.getMessage() + "\n");
		}
		catch(Exception e) {
			logger.warn(e.getMessage() + "\n");
		}
		finally {
			// connection.close();
		}
	}
	
	/**
	 * Method to update the info of a student in the student and user database
	 * 
	 * @param student Student object containing the student information
	 */
	public void updateInfo(Student student){
		try{
			connection = DBConnection.getConnection();
			ps = connection.prepareStatement(SQLQueriesConstant.UPDATE_STUDENT_CREDENTIAL_QUERY);

			ps.setInt(1,student.getAge());
			ps.setString(2,student.getAddress());
			ps.setString(3,student.getContact());
			ps.setString(4,student.getGender());
			ps.setString(5,student.getNationality());
			ps.setInt(6,student.getUserId());

			ps.executeUpdate();


			ps = connection.prepareStatement(SQLQueriesConstant.UPDATE_STUDENT_QUERY);
			ps.setString(1, student.getUserName());
			ps.setInt(2,student.getUserId());

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

	@Override
	public void setPaymentStatus(Student student) {
		// TODO Auto-generated method stub
		
	}
}
