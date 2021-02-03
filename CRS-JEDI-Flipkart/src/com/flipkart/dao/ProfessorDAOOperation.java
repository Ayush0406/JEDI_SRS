package com.flipkart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.flipkart.bean.Professor;
import com.flipkart.bean.Student;
import com.flipkart.constant.SQLQueriesConstant;
import com.flipkart.util.DBConnection;
import com.mysql.cj.protocol.Resultset;

/**
 * Undertakes all operations for professor related 
 * queries to the database
 * 
 * @author JEDI04
 */
public class ProfessorDAOOperation implements ProfessorDAOInterface {
	private static Logger logger = Logger.getLogger(ProfessorDAOOperation.class);
	Connection con;
	PreparedStatement stmt;

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
	 */
	public Professor getProfessorByEmail(String email) {
		Professor professor = new Professor();
		try {

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
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return professor;
	}

	/**
	 * Gets Professor Name from the professorId
	 * 
	 * @param professorID Professor Id
	 * @return Professor Name corresponding to the given id
	 */
	
	public String getProfessorById(int professorID) {
		String professorName = null;

		try {
			con = DBConnection.getConnection();
			// String sqlQuery = "SELECT name FROM professor WHERE id = ?";
			stmt = con.prepareStatement(SQLQueriesConstant.GET_PROFESSOR_BY_ID_QUERY);

			stmt.setInt(1, professorID);

			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				professorName = resultSet.getString("name");
			}
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

		return professorName;
	}

	/**
	 * Method Displays list of courses alloted to the professor after getting from
	 * the database
	 * 
	 * @param professorId Professor Id
	 */
	public void showCourses(int professorId) {
		try {
			con = DBConnection.getConnection();
			String str = SQLQueriesConstant.SHOW_COURSES_PROFESSOR_QUERY;
			stmt = con.prepareStatement(str);
			stmt.setInt(1, professorId);
			ResultSet rs = stmt.executeQuery();
			logger.info("===============================================");
			logger.info(String.format("%-8s\t%-15s\t%-15s", "ID","CourseName","Credits"));
			while (rs.next()) {
				logger.info(String.format("%-8s\t%-15s\t%-15s", rs.getInt(1),rs.getString(2),rs.getString(3)));
			}
			logger.info("================================================\n\n");
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * Returns list of all student objects associated with the particular course id
	 * whose grade is Not Available from the database
	 * 
	 * @param courseId Course Id
	 * @return returns list of enrolled students
	 */
	public ArrayList<Student> getEnrolledStudents(int courseId) {
		ArrayList<Student> al = new ArrayList<Student>();
		try {
			con = DBConnection.getConnection();
			String str = SQLQueriesConstant.GET_ENROLLED_STUDENTS_PROFESSOR_QUERY;
			stmt = con.prepareStatement(str);
			stmt.setInt(1, courseId);
			ResultSet rs = stmt.executeQuery();
			rs = stmt.executeQuery();

			while (rs.next()) {
				Student st = new Student();
				st.setUserId(rs.getInt("id"));
				st.setUserName(rs.getString("name"));
				st.setEmail(rs.getString("email"));
				st.setBranch(rs.getString("branch"));
				al.add(st);
			}

		} catch (SQLException e) {
			logger.info(e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return al;
	}

	/**
	 * Updates grades of multiple student belonging to a particular courseId whose
	 * grades are currently not available
	 * 
	 * @param toGrade  List of students to be graded
	 * @param courseId course Id
	 */
	public void setGrades(ArrayList<Student> toGrade, int courseId) {

		Scanner sc = new Scanner(System.in);
		try {
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
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return;
	}

	/**
	 * Updates grades of a single student belonging to a particular courseId
	 * 
	 * @param courseId course id of the course whose grade has to be updated
	 * @param studentId student id whose grade has to be updated
	 * @param grades grade to be entered
	 * @return true if grades updated else false
	 */
	public boolean updateStudentGrades(int courseId, int studentId, String grades) {
		try {
			con = DBConnection.getConnection();
			String str = SQLQueriesConstant.UPDATE_GRADES_PROFESSOR_QUERY;
			stmt = con.prepareStatement(str);
			stmt.setString(1, grades);
			stmt.setInt(2, courseId);
			stmt.setInt(3, studentId);
			int status = stmt.executeUpdate();
			if (status > 0) {
				return true;
			}
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return false;
	}

	/**
	 * Show Grades of all enrolled students associated with the courseId
	 * 
	 * @param enrolledStudent List of enrolled student
	 * @param courseId course id
	 */
	public void showGrades(ArrayList<Student> enrolledStudent, int courseId) {
		try {
			con = DBConnection.getConnection();
			logger.info("=======================================================");
			logger.info(String.format("%-8s\t%-15s\t%-15s", "UserID","StudentName","Grade Obtained"));
			for (Student st : enrolledStudent) {
				String str = SQLQueriesConstant.SHOW_GRADES_PROFESSOR_QUERY;
				stmt = con.prepareStatement(str);
				stmt.setInt(1, st.getUserId());
				stmt.setInt(2, courseId);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					logger.info(String.format("%-8s\t%-15s\t%-15s", st.getUserId(),st.getUserName(),rs.getString("grade")));
				}
			}
			logger.info("=======================================================");
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return;
	}

	/**
	 * Returns list of all student objects associated with the particular course id
	 * from the database
	 * 
	 * @param courseId course id
	 * @return List of students in the course
	 */
	public ArrayList<Student> getStudents(int courseId) {
		ArrayList<Student> al = new ArrayList<Student>();
		try {
			con = DBConnection.getConnection();
			String str = SQLQueriesConstant.GET_STUDENTS_PROFESSOR_QUERY;
			stmt = con.prepareStatement(str);
			stmt.setInt(1, courseId);
			ResultSet rs = stmt.executeQuery();
			rs = stmt.executeQuery();

			while (rs.next()) {
				Student st = new Student();
				st.setUserId(rs.getInt("id"));
				st.setUserName(rs.getString("name"));
				st.setEmail(rs.getString("email"));
				st.setBranch(rs.getString("branch"));
				al.add(st);
			}

		} catch (SQLException e) {
			logger.info(e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return al;
	}

}
