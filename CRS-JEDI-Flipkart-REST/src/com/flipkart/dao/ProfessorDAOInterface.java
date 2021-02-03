package com.flipkart.dao;

import com.flipkart.bean.Professor;
import com.flipkart.bean.Student;

import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONObject;

/**
 * Interface to Show the structure of
 * ProfessorDAO Operation Class
 * @author JEDI04
 */
public interface ProfessorDAOInterface {
	public Professor getProfessorByEmail(String email) throws SQLException, Exception;
	public String getProfessorById(int professorID) throws SQLException, Exception;
	public ArrayList<JSONObject> showCourses(int professorId) throws SQLException, Exception;
	public ArrayList<JSONObject> getEnrolledStudents(int courseId) throws SQLException, Exception;
	public void setGrades(ArrayList<Student>toGrade,int courseId) throws SQLException, Exception;
	public boolean updateStudentGrades(int courseId,int studentId, String grades) throws SQLException, Exception;
	public ArrayList<JSONObject> showGrades(ArrayList<Student>enrolledStudent,int courseId) throws SQLException, Exception;
	public ArrayList<Student> getStudents(int courseId) throws SQLException, Exception;
}
