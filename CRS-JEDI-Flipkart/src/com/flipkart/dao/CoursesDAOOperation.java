package com.flipkart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.flipkart.service.AdminOperation;
import org.apache.log4j.Logger;

import com.flipkart.bean.Course;
import com.flipkart.constant.SQLQueriesConstant;
import com.flipkart.util.DBConnection;

/**
 * Primary Class undertaking all database queries related to Course Database
 * 
 * @author JEDI 04
 */
public class CoursesDAOOperation implements CoursesDAOInterface {
	private static Logger logger = Logger.getLogger(CoursesDAOOperation.class);
	Connection connection = null;
	PreparedStatement ps = null;

	private static CoursesDAOOperation instance = null;

	private CoursesDAOOperation()
	{

	}

	synchronized public static CoursesDAOOperation getInstance()
	{
		if(instance == null)
		{
			instance = new CoursesDAOOperation();
		}
		return instance;
	}

	/**
	 * Method to fetch the Course Object for the given course Id
	 * 
	 * @param courseID The id of the course whose Course object is to be fetched
	 * @return return the Course Object containing the information of the course
	 */
	public Course getCourseByID(int courseID){
		Course course = null;

		try{
			connection = DBConnection.getConnection();
			ps = connection.prepareStatement(SQLQueriesConstant.GET_COURSE_BY_ID_QUERY);

			ps.setInt(1,courseID);
			ResultSet resultSet = ps.executeQuery();
			while(resultSet.next()){
				course = new Course();
				course.setCourseID(resultSet.getInt("id"));
				course.setCourseName(resultSet.getString("name"));
				course.setCredits(resultSet.getInt("credits"));
				course.setProfessorAllotted(resultSet.getInt("professorId"));
			}

		}
		catch(SQLException e) {
			logger.warn(e.getMessage() + "\n");
		}
		catch(Exception e) {
			logger.warn(e.getMessage() + "\n");
		}

		return course;
	}
	
	/**
	 * Method to get all courses and their details from course and course catalog database
	 * 
	 * @return ArrayList of Courses containing all courses and their details fetched 
	 * from course and course catalog database
	 */
	public ArrayList<Course> getAllCourses() {
		ArrayList<Course> courses = new ArrayList<>();

		try{
			connection = DBConnection.getConnection();
			ps = connection.prepareStatement(SQLQueriesConstant.GET_ALL_COURSES_QUERY);

			ResultSet resultSet = ps.executeQuery();

			while(resultSet.next()){
				Course course = new Course();
				course.setCourseID(resultSet.getInt("courseId"));
				course.setCourseName(resultSet.getString("courseName"));
				course.setCredits(resultSet.getInt("credits"));
				course.setProfessorAllotted(resultSet.getInt("professorId"));
				courses.add(course);
			}
		}
		catch(SQLException e) {
			logger.warn(e.getMessage() + "\n");
		}
		catch(Exception e) {
			logger.warn(e.getMessage() + "\n");
		}

		return courses;
	}

	/**
	 * Method to get the number of enrolled students in a course with the given course id
	 * 
	 * @param courseID The Id of the course whose enrolled student number we want to find out
	 */
	public int noOfEnrolledStudents(int courseID){
		int students = 0;

		try{
			connection = DBConnection.getConnection();
			ps = connection.prepareStatement(SQLQueriesConstant.NO_ENROLLED_STUDENTS_QUERY);

			ps.setInt(1,courseID);
			ResultSet resultSet = ps.executeQuery();
			if(resultSet.next()){
				students = resultSet.getInt(1);
			}
		}
		catch(SQLException e) {
			logger.warn(e.getMessage() + "\n");
		}
		catch(Exception e) {
			logger.warn(e.getMessage() + "\n");
		}

		return students;
	}
}
