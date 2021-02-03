package com.flipkart.service;

import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.flipkart.bean.Professor;
import com.flipkart.bean.Student;
import com.flipkart.dao.ProfessorDAOOperation;
import com.flipkart.exception.ProfessorCRSException;

/**
 * 
 * Handles professor related actions in the backend
 * @author JEDI04
 *
 */

public class ProfessorOperation implements ProfessorInterface {

	private static Logger logger = Logger.getLogger(ProfessorOperation.class);
	private ProfessorDAOOperation professorDaoOperation = ProfessorDAOOperation.getInstance();
	
	private static ProfessorOperation instance = null;
	
	private ProfessorOperation() {
		// TODO Auto-generated constructor stub
	}
	
	synchronized public static ProfessorOperation getInstance() {
		if (instance == null) {
			instance = new ProfessorOperation();
		}
		return instance;
	}
	
	/**
	 * Method to get and display enrolled student in a particular course whose
	 * grades are currently Not Available
	 * 
	 * @param courseId Course Id 
	 * @throws ProfessorCRSException, Exception
	 */
	
	public ArrayList<JSONObject> viewStudentsEnrolled(int courseId) throws ProfessorCRSException, Exception  {		
		return professorDaoOperation.getEnrolledStudents(courseId);
	}

	/**
	 * Get and display grades of all students in a course linked with courseId
	 * 
	 * @param courseId Course Id
	 * @throws ProfessorCRSException, Exception
	 */
	public ArrayList<JSONObject> viewGrades(int courseId) throws ProfessorCRSException, Exception {
		ArrayList<JSONObject> al = new ArrayList<JSONObject>();
			ArrayList<Student>studentsEnrolled = professorDaoOperation.getStudents(courseId);
			if (studentsEnrolled.size() > 0) {
				al = professorDaoOperation.showGrades(studentsEnrolled, courseId);
			} else {
				throw new ProfessorCRSException("No Student Present!!!");
			}
		
		
		return al;
	}

	/**
	 * Get and display all courses alloted to the professor with given courseId
	 * 
	 * @param professorId Professor Id
	 * @throws ProfessorCRSException, Exception
	 */
	public ArrayList<JSONObject> showCourses(int professorId) throws ProfessorCRSException, Exception{
		ArrayList<JSONObject> ar = professorDaoOperation.showCourses(professorId);
		return ar;
	}
	
	/**
	 * Method to return the professor name whose ID is provided
	 * @param id The ID of the professor whose name is to be fetched
	 * @return The name of the professor whose ID was passed
	 * @throws ProfessorCRSException, Exception
	 */
	public String getProfessorById(int id) throws ProfessorCRSException, Exception {
		String profName = null;
		try {
			profName = professorDaoOperation.getProfessorById(id);
		}
		catch(Exception e) {
			logger.warn(e.getMessage());
		}
		return profName;
	}

	/**
	 * Returns professor object associated with given email
	 * 
	 * @param email professor email to be verified
	 * @return Professor object containing information of the professor
	 * @throws ProfessorCRSException, Exception
	 */
	public Professor getProfessorByEmail(String email) throws ProfessorCRSException, Exception{
		Professor p = professorDaoOperation.getProfessorByEmail(email);
		return p;
	}

	/**
	 * Updates Student grade and returns update status as boolean
	 * 
	 * @param courseId
	 * @param studentId
	 * @param grade Grade that has to be allotted
	 * @return boolean Returns true if update grade operation successful else returns false
	 * 
	 */
	public boolean updateStudentGrade(int courseId, int studentId, String grade) throws ProfessorCRSException, Exception{
		return professorDaoOperation.updateStudentGrades(courseId, studentId, grade);
	}
	
	
	
	/**
	 * Returns professor object associated with given id
	 * 
	 * @param email professor email to be verified
	 * @return Professor object containing information of the professor
	 * @throws ProfessorCRSException, Exception
	 */
	public Professor getProfessorById2(int professorId) throws ProfessorCRSException, Exception{
		Professor professor = professorDaoOperation.getProfessorById2(professorId);
		return professor;
	}

}
