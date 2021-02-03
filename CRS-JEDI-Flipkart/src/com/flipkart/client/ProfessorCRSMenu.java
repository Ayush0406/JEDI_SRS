package com.flipkart.client;

import com.flipkart.bean.*;
import com.flipkart.service.*;

import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Scanner;

/**
 *  User Interactive menu if user logs in is a professor
 *  
 *  @author JEDI04
 */
public class ProfessorCRSMenu {
	private static Logger logger = Logger.getLogger(ProfessorCRSMenu.class);
	private ProfessorInterface professorOperation = ProfessorOperation.getInstance();

	/**
	 *  Manages the major roles of professor
	 *  based on user inputs
	 *  @param professor Professor Object containing information of the logged in professor
	 */
	public void professorClient(Professor professor) {
		logger.info("Welcome Professor " + professor.getUserName() + "!!");
		int choice;
		Scanner input = null;
		try {
			input = new Scanner(System.in);
			do {
				showChoices();
				choice = input.nextInt();

				switch (choice) {
				case -1:
					logger.info(".....Logging Out.....\n");
					break;
				case 1:
					gradeStudents(professor.getUserId());
					break;
				case 2:
					allotedCourse(professor.getUserId());
					break;
				case 3:
					allotedCourse(professor.getUserId());
					viewStudents();
					break;
				case 4:
					allotedCourse(professor.getUserId());
					viewGrades();
					break;
				default:
					logger.info("Invalid choice\n");
					break;
				}
			} while (choice != -1);
		}
		catch(Exception e) {
			
		}
	
	}

	/**
	 * Initializes professor object with 
	 * values in the database
	 * fetching from the email
	 * @param email Professor email
	 */
	public void init(String email) {
		logger.info("\n");
		logger.info("Login Time : " + LocalDateTime.now());
		Professor professor = professorOperation.getProfessorByEmail(email);
		professorClient(professor);
	}

	/**
	 * Grade students with 2 options
	 * 1) Grade Students with a particular courseId
	 * 2) Update student grades by providing courseId and studentId
	 * 
	 * @param professorId Id of Professor
	 */
	public void gradeStudents(int professorId) {
		Scanner sc = null;
		try {
			sc = new Scanner(System.in);
			logger.info("Please select an operation: ");
			logger.info("1. Grade Students by CourseId");
			logger.info("2. Update Grade of Particular Student");
			int choice = sc.nextInt();
			allotedCourse(professorId);
			if (choice == 2) {
				updateStudentGrades();
			} 
			else {
				try {
					logger.info("Enter courseId");
					int courseId = sc.nextInt();
					professorOperation.gradeStudents(courseId);
				} catch (Exception e) {
					logger.info(e.getMessage() + "\n");
				}
			}
		}
		catch(Exception e) {
			logger.info(e.getMessage() + "\n");
		}	
	}

	/**
	 * Displays List of all courses that are allotted
	 * to the professor carrying a particular professorId
	 * 
	 * @param professorId professor id
	 */
	public void allotedCourse(int professorId) {
		professorOperation.showCourses(professorId);
	}

	/**
	 * Displays menu for professor interaction
	 * to perform various operations
	 * 
	 */
	public static void showChoices() {
		logger.info("Please select an operation: ");
		logger.info("1. Grade Students");
		logger.info("2. View Alloted Courses");
		logger.info("3. View Students Enrolled in a course ");
		logger.info("4. View Students Grade");
		logger.info("-1 to Logout");
	}

	/**
	 * Updates grades for a student with given courseId
	 * and studentId
	 * 
	 */
	public void updateStudentGrades() {
		Scanner sc = new Scanner(System.in);
		try {
			logger.info("Enter courseId");
			int courseId = sc.nextInt();
			logger.info("Enter studentId");
			int studentId = sc.nextInt();
			logger.info("Enter Grade");
			String grade = sc.next();
			boolean status = professorOperation.updateStudentGrade(courseId, studentId, grade);
			logger.info("Update Grade Status : " + status + "\n\n");

		} catch (Exception e) {
			logger.info(e.getMessage() + "\n");
		}
	}

	/**
	 * Method to view enrolled student in a particular course
	 * whose grades are currently Not Available
	 */
	public void viewStudents() {
		Scanner sc = null;
		try {
			sc = new Scanner(System.in);
			logger.info("	Enter courseId");
			int courseId = sc.nextInt();
			professorOperation.viewStudentsEnrolled(courseId);
		} catch (Exception e) {
			logger.info(e.getMessage() + "\n");
		}
	}
	
	/**
	 * View grades of all students in a course 
	 *  by providing the courseId
	 */
	public void viewGrades() {
		Scanner sc = null;
		try {
			sc = new Scanner(System.in);
			logger.info("Enter courseId");
			int courseId = sc.nextInt();
			professorOperation.viewGrades(courseId);
		} catch (Exception e) {
			logger.info(e.getMessage() + "\n");
		}
	}
	
	
}