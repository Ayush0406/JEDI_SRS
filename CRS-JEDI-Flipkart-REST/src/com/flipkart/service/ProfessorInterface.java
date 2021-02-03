package com.flipkart.service;

import java.util.ArrayList;

import org.json.JSONObject;

import com.flipkart.bean.Professor;
import com.flipkart.exception.ProfessorCRSException;


/**
 * Interface to Show the structure of
 * Professor operation Class
 * @author JEDI04
 */
public interface ProfessorInterface {
	public ArrayList<JSONObject> viewGrades(int courseId) throws ProfessorCRSException, Exception;
	public ArrayList<JSONObject> showCourses(int professorId) throws ProfessorCRSException, Exception;
	public Professor getProfessorByEmail(String email) throws ProfessorCRSException, Exception;
	public ArrayList<JSONObject> viewStudentsEnrolled(int courseId) throws ProfessorCRSException, Exception;
	public boolean updateStudentGrade(int courseId,int studentId, String grade) throws ProfessorCRSException, Exception;
	public String getProfessorById(int id) throws ProfessorCRSException, Exception;
}
