package com.flipkart.service;

import com.flipkart.bean.Course;
import com.flipkart.dao.CoursesDAOOperation;

import com.flipkart.exception.CommonException;
import org.apache.log4j.Logger;

/**
 * Class to handle all course related operations.
 *
 * @author JEDI 04
 */
public class CourseOperation implements CourseInterface {

    private static Logger logger = Logger.getLogger(CourseOperation.class);
    
    CoursesDAOOperation coursesDaoOperation = CoursesDAOOperation.getInstance();

	private static CourseOperation instance = null;

	private CourseOperation()
	{

	}

	synchronized public static CourseOperation getInstance()
	{
		if(instance == null)
		{
			instance = new CourseOperation();
		}
		return instance;
	}

	/**
	 * Method to get the number of students enrolled in a particular course
	 *
	 * @param courseID ID of the course
	 * @return count of students enrolled in the course
	 */
	@Override
	public int noOfEnrolledStudents(int courseID) {
		int count = 0;
		try {
			count = coursesDaoOperation.noOfEnrolledStudents(courseID);
		}
		catch (Exception e) {
			logger.warn(e.getMessage());
		}
		return count;
	}

	/**
	 * Method to get the course object which contains all information about a course
	 *
	 * @param courseID ID of the course
	 * @return Course object containing all information about the course
	 */
	@Override
	public Course getCourseById(int courseID) {
		Course course = null;
		try {
			course = coursesDaoOperation.getCourseByID(courseID);
			if(course == null){
				throw new CommonException("No course with this ID");
			}
		}
		catch (CommonException e){
			logger.warn(e.getMessage());
		}
		catch(Exception e) {
			logger.warn(e.getMessage());
		}
		return course;
	}

}
