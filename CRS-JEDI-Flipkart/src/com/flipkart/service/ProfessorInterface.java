package com.flipkart.service;

import com.flipkart.bean.Professor;
import com.flipkart.exception.ProfessorCRSException;


/**
 * Interface to Show the structure of
 * Professor operation Class
 * @author JEDI04
 */
public interface ProfessorInterface {
	public void viewGrades(int courseId);
	public Boolean gradeStudents(int courseId);
	public void showCourses(int professorId);
	public Professor getProfessorByEmail(String email);
	public void viewStudentsEnrolled(int courseId);
	public boolean updateStudentGrade(int courseId,int studentId, String grade);
}
