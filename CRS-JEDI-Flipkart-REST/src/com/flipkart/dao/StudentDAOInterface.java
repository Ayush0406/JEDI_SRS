package com.flipkart.dao;

import java.util.*;

import com.flipkart.bean.Course;
import com.flipkart.bean.Grades;
import com.flipkart.bean.Student;
import com.flipkart.exception.StudentCRSException;

/**
 * Interface to Show the structure of
 * StudentDAO Operation Class
 * @author JEDI04
 */
public interface StudentDAOInterface {
	public Student getStudentByEmail(String email);
	public void addCourse(Student student, int courseID) throws Exception;
	public void dropCourse(Student student, int courseID) throws Exception;
	public ArrayList<Course> getEnrolledCourses(Student student) throws Exception;

	public void setRegistrationStatus(Student student);

	public ArrayList<Grades> getGrades(int studentID) throws Exception;

	public int getNoOfCourses(Student student);

	public boolean getCourse(Student student, int courseID);
	
	public void setPaymentStatus(Student student, String method) throws Exception;
	
	public void registerStudent(Student student, int id) throws Exception;

	public void addCourseToGrades(int studentID, int courseID);

	public void deleteCourseFromGrades(int studentID, int courseID);

	public void updateInfo(Student student) throws Exception;

	public boolean getRegistrationSystemStatus();
	
	public Student getStudentByID(int studentID) throws StudentCRSException, Exception;
}
