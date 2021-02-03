package com.flipkart.dao;

import java.util.*;

import com.flipkart.bean.Course;
import com.flipkart.exception.CommonException;

/**
 * Interface to Show the structure of
 * CourseDAO Operation Class
 * @author JEDI04
 */
public interface CoursesDAOInterface {
	public Course getCourseByID(int courseID);
	public ArrayList<Course> getAllCourses() throws CommonException, Exception;

	public int noOfEnrolledStudents(int courseID);
}
