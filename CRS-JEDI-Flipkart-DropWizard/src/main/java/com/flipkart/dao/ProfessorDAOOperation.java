package com.flipkart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.flipkart.bean.Professor;
import com.flipkart.bean.Student;
import com.flipkart.constant.SQLQueriesConstant;
import com.flipkart.exception.AdminCRSException;
import com.flipkart.exception.ProfessorCRSException;
import com.flipkart.util.DBConnection;
import com.mysql.cj.protocol.Resultset;

/**
 * Undertakes all operations for professor related queries to the database
 * 
 * @author JEDI04
 */
public class ProfessorDAOOperation implements ProfessorDAOInterface {
	private static Logger logger = Logger.getLogger(ProfessorDAOOperation.class);
	Connection con;
	PreparedStatement stmt;
	PreparedStatement ps = null;

	private static ProfessorDAOOperation instance = null;

	private ProfessorDAOOperation() {

	}

	synchronized public static ProfessorDAOOperation getInstance() {
		if (instance == null) {
			instance = new ProfessorDAOOperation();
		}
		return instance;
	}

	/**
	 * Method creates and returns professor object using email ID from the database
	 * 
	 * @param email Email Id of professor
	 * @return Professor Object
	 * @throws SQLException, Exception
	 */
	public Professor getProfessorByEmail(String email) throws SQLException, Exception {
		Professor professor = new Professor();

		con = DBConnection.getConnection();
		String str = SQLQueriesConstant.GET_PROFESSOR_BY_EMAIL;
		stmt = con.prepareStatement(str);
		stmt.setString(1, email);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			professor = new Professor();
			professor.setUserId(rs.getInt(1));
			professor.setUserName(rs.getString(2));
			professor.setEmail(rs.getString("email"));
			professor.setRole(rs.getString("role"));
			professor.setDepartment(rs.getString(5));
		}

		return professor;
	}

	/**
	 * Gets Professor Name from the professorId
	 * 
	 * @param professorID Professor Id
	 * @return Professor Name corresponding to the given id
	 * @throws SQLException, Exception
	 */

	public String getProfessorById(int professorID) throws SQLException, Exception {
		String professorName = null;
		con = DBConnection.getConnection();
		stmt = con.prepareStatement(SQLQueriesConstant.GET_PROFESSOR_BY_ID_QUERY);

		stmt.setInt(1, professorID);

		ResultSet resultSet = stmt.executeQuery();
		if (resultSet.next()) {
			professorName = resultSet.getString("name");
		}
		return professorName;
	}

	/**
	 * Method Displays list of courses alloted to the professor after getting from
	 * the database
	 * 
	 * @param professorId Professor Id
	 * @return List of courses allotted to the professor
	 * @throws SQLException, Exception
	 */
	public ArrayList<JSONObject> showCourses(int professorId) throws SQLException, Exception {
		ArrayList<JSONObject> arr = new ArrayList<JSONObject>();

		con = DBConnection.getConnection();
		String str = SQLQueriesConstant.SHOW_COURSES_PROFESSOR_QUERY;
		stmt = con.prepareStatement(str);
		stmt.setInt(1, professorId);
		ResultSet rs = stmt.executeQuery();
		logger.info("===============================================");
		logger.info(String.format("%-8s\t%-15s\t%-15s", "ID", "CourseName", "Credits"));
		while (rs.next()) {
			JSONObject obj = new JSONObject();
			logger.info(String.format("%-8s\t%-15s\t%-15s", rs.getInt(1), rs.getString(2), rs.getString(3)));
			obj.put("id", rs.getInt(1));
			obj.put("CourseName", rs.getString(2));
			obj.put("Credits", rs.getInt(3));
			arr.add(obj);
		}
		logger.info("================================================\n\n");

		return arr;
	}

	/**
	 * Returns list of all student objects associated with the particular course id
	 * whose grade is Not Available from the database
	 * 
	 * @param courseId Course Id
	 * @return returns list of enrolled students
	 * @throws SQLException, Exception
	 */
	public ArrayList<JSONObject> getEnrolledStudents(int courseId) throws SQLException, Exception {
		
		ArrayList<JSONObject> arr = new ArrayList<JSONObject>();

		con = DBConnection.getConnection();
		String str = SQLQueriesConstant.GET_COURSE_INFO_BY_ID;
		ps = con.prepareStatement(str);
		ps.setInt(1, courseId);
		ResultSet rs = ps.executeQuery();
		if(rs.next()==false)
			throw new  ProfessorCRSException("No Course with ID: " + courseId + " exists");
		str = SQLQueriesConstant.GET_ENROLLED_STUDENTS_PROFESSOR_QUERY;
		stmt = con.prepareStatement(str);
		stmt.setInt(1, courseId);
		rs = stmt.executeQuery();
		rs = stmt.executeQuery();
		while (rs.next()) {
			Student st = new Student();
			JSONObject obj = new JSONObject();
			obj.put("id", rs.getInt("id"));
			obj.put("name", rs.getString("name"));
			obj.put("email", rs.getString("email"));
			obj.put("branch", rs.getString("branch"));
			arr.add(obj);
		}

		return arr;
	}

	/**
	 * Updates grades of multiple student belonging to a particular courseId whose
	 * grades are currently not available
	 * 
	 * @param toGrade  List of students to be graded
	 * @param courseId course Id
	 * @throws SQLException, Exception
	 */
	public void setGrades(ArrayList<Student> toGrade, int courseId) throws SQLException, Exception {

		try {
			Scanner sc = new Scanner(System.in);
			con = DBConnection.getConnection();
			for (Student st : toGrade) {
				logger.info("Please Enter Grade for " + st.getUserName());
				String grd = sc.next();
				if (grd == "")
					grd = sc.next();
				String str = SQLQueriesConstant.SET_GRADES_PROFESSOR_QUERY;
				stmt = con.prepareStatement(str);
				stmt.setString(1, grd);
				stmt.setInt(2, st.getUserId());
				stmt.setInt(3, courseId);
				int isUpdated = stmt.executeUpdate();
				if (isUpdated > 0) {
					logger.info("Uploaded grade");
				} else {
					logger.info("Couldn't upload try again");
				}
			}
		} catch (Exception e) {
			logger.warn(e);
		}
	}

	/**
	 * Updates grades of a single student belonging to a particular courseId
	 * 
	 * @param courseId  course id of the course whose grade has to be updated
	 * @param studentId student id whose grade has to be updated
	 * @param grades    grade to be entered
	 * @return true if grades updated else false
	 * @throws SQLException, Exception
	 */
	public boolean updateStudentGrades(int courseId, int studentId, String grades) throws SQLException, Exception {

		con = DBConnection.getConnection();
		String str = SQLQueriesConstant.GET_COURSE_INFO_BY_ID;
		ps = con.prepareStatement(str);
		ps.setInt(1, courseId);
		ResultSet rs = ps.executeQuery();
		if(rs.next()==false)
			throw new  ProfessorCRSException("No Course with ID: " + courseId + " exists");
		
		str = SQLQueriesConstant.GET_PROFESSOR_STUDENTS_QUERY;
		ps = con.prepareStatement(str);
		ps.setInt(1, studentId);
		rs = ps.executeQuery();
		if(rs.next()==false)
			throw new  ProfessorCRSException("No student with ID: " + studentId + " exists");
		
		str = SQLQueriesConstant.UPDATE_GRADES_PROFESSOR_QUERY;
		stmt = con.prepareStatement(str);
		stmt.setString(1, grades);
		stmt.setInt(2, courseId);
		stmt.setInt(3, studentId);
		int status = stmt.executeUpdate();
		if (status > 0) {
			return true;
		}

		return false;
	}

	/**
	 * Show Grades of all enrolled students associated with the courseId
	 * 
	 * @param enrolledStudent List of enrolled student
	 * @param courseId        course id
	 * @return List of students containing with grades
	 * @throws SQLException, Exception
	 */
	public ArrayList<JSONObject> showGrades(ArrayList<Student> enrolledStudent, int courseId)
			throws SQLException, Exception {
		ArrayList<JSONObject> al = new ArrayList<JSONObject>();

		con = DBConnection.getConnection();
		String str = SQLQueriesConstant.GET_COURSE_INFO_BY_ID;
		ps = con.prepareStatement(str);
		ps.setInt(1, courseId);
		ResultSet rs = ps.executeQuery();
		if(rs.next()==false)
			throw new  ProfessorCRSException("No Course with ID: " + courseId + " exists");
		
		logger.info("=======================================================");
		logger.info(String.format("%-8s\t%-15s\t%-15s", "UserID", "StudentName", "Grade Obtained"));
		for (Student st : enrolledStudent) {
			JSONObject obj = new JSONObject();
			str = SQLQueriesConstant.SHOW_GRADES_PROFESSOR_QUERY;
			stmt = con.prepareStatement(str);
			stmt.setInt(1, st.getUserId());
			stmt.setInt(2, courseId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				logger.info(
						String.format("%-8s\t%-15s\t%-15s", st.getUserId(), st.getUserName(), rs.getString("grade")));
				obj.put("UserID", st.getUserId());
				obj.put("Student Name", st.getUserName());
				obj.put("Grade", rs.getString("grade"));
				al.add(obj);
			}
		}
		logger.info("=======================================================");

		return al;
	}

	/**
	 * Returns list of all student objects associated with the particular course id
	 * from the database
	 * 
	 * @param courseId course id
	 * @return List of students in the course
	 * @throws SQLException, Exception
	 */
	public ArrayList<Student> getStudents(int courseId) throws SQLException, Exception {
		ArrayList<Student> al = new ArrayList<Student>();

		con = DBConnection.getConnection();
		String str = SQLQueriesConstant.GET_COURSE_INFO_BY_ID;
		ps = con.prepareStatement(str);
		ps.setInt(1, courseId);
		ResultSet rs = ps.executeQuery();
		if(rs.next()==false)
			throw new  ProfessorCRSException("No Course with ID: " + courseId + " exists");
		
		str = SQLQueriesConstant.GET_STUDENTS_PROFESSOR_QUERY;
		stmt = con.prepareStatement(str);
		stmt.setInt(1, courseId);
		rs = stmt.executeQuery();

		while (rs.next()) {
			Student st = new Student();
			st.setUserId(rs.getInt("id"));
			st.setUserName(rs.getString("name"));
			st.setEmail(rs.getString("email"));
			st.setBranch(rs.getString("branch"));
			al.add(st);
		}

		return al;
	}
	
	
	
	/**
	 * Method creates and returns professor object using professor ID from the database
	 * 
	 * @param email id of professor
	 * @return Professor Object
	 * @throws SQLException, Exception
	 */
	public Professor getProfessorById2(int professorId) throws SQLException, Exception {
		Professor professor = null;

		con = DBConnection.getConnection();
		String str = SQLQueriesConstant.GET_PROFESSOR_OBJECT_BY_ID_QUERY;
		stmt = con.prepareStatement(str);
		stmt.setInt(1, professorId);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			professor = new Professor();
			professor.setUserId(rs.getInt(1));
			professor.setUserName(rs.getString(2));
			professor.setEmail(rs.getString("email"));
			professor.setRole(rs.getString("role"));
			professor.setDepartment(rs.getString(5));
			professor.setApproved(true);
		}

		return professor;
	}

}
