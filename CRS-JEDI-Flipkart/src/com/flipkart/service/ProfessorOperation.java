package com.flipkart.service;

import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.flipkart.bean.Professor;
import com.flipkart.bean.Student;
import com.flipkart.dao.ProfessorDAOOperation;
import com.flipkart.exception.ProfessorCRSException;

public class ProfessorOperation implements ProfessorInterface {

	private static Logger logger = Logger.getLogger(ProfessorOperation.class);
	private ProfessorDAOOperation professorDaoOperation = new ProfessorDAOOperation();

	/*
	 * Method to get and display enrolled student in a particular course whose
	 * grades are currently Not Available
	 */
	@Override
	public void viewStudentsEnrolled(int courseId) {
		logger.info("Inside viewStudentEnrolled");
		try {
			ArrayList<Student> studentsEnrolled = professorDaoOperation.getEnrolledStudents(courseId);
			if (studentsEnrolled.size() > 0) {
				logger.info("\n\n");
				logger.info(
						"====================================================================================================");

				logger.info("StudentID		StudentName		StudentEmail			Branch");
				for (Student st : studentsEnrolled) {
					logger.info(st.getUserId() + "			" + st.getUserName() + "			" + st.getEmail()
							+ "		" + st.getBranch());
				}
				logger.info(
						"====================================================================================================");
			} else {
				throw new ProfessorCRSException("No student is currently ungraded in the course");
			}
		} catch (ProfessorCRSException e) {
			logger.info("\n\n");
			logger.error(e.getMessage());
			logger.info("\n\n");
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	/*
	 * Get and display grades of all students in a course linked with courseId
	 */
	@Override
	public void viewGrades(int courseId) {
		try {
			ArrayList<Student> studentsEnrolled = professorDaoOperation.getStudents(courseId);
			if (studentsEnrolled.size() > 0) {
				professorDaoOperation.showGrades(studentsEnrolled, courseId);
			} else {
				throw new ProfessorCRSException("No Student Present!!!");
			}
		} catch (ProfessorCRSException e) {
			logger.info("\n\n");
			logger.error(e.getMessage());
			logger.info("\n\n");
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	/*
	 * Grade Students in a batch with mentioned courseId
	 */
	@Override
	public Boolean gradeStudents(int courseId) {
		try {
			ArrayList<Student> studentsEnrolled = professorDaoOperation.getEnrolledStudents(courseId);
			if (studentsEnrolled.size() > 0) {
				professorDaoOperation.setGrades(studentsEnrolled, courseId);
			} else {
				throw new ProfessorCRSException("No Student to Grade!!!");
			}
		} catch (ProfessorCRSException e) {
			logger.info("\n\n");
			logger.error(e.getMessage());
			logger.info("\n\n");
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

		return true;
	}

	/*
	 * Get and display all courses alloted to the professor with given courseId
	 */
	@Override
	public void showCourses(int professorId) {
		logger.info("Inside showCourses");
		professorDaoOperation.showCourses(professorId);
	}

	/*
	 * Returns professor object associated with given email
	 */
	public Professor getProfessorByEmail(String email) {
		Professor p = professorDaoOperation.getProfessorByEmail(email);
		return p;
	}

	/*
	 * Updates Student grade and returns upadte status as boolean
	 */
	public boolean updateStudentGrade(int courseId, int studentId, String grade) {
		return professorDaoOperation.updateStudentGrades(courseId, studentId, grade);
	}
}
