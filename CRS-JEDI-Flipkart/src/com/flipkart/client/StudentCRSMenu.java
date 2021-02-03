package com.flipkart.client;

import com.flipkart.bean.*;
import com.flipkart.service.*;

import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * User Interactive menu for students
 * 
 * @author JEDI 04
 */
public class StudentCRSMenu {
	private static Logger logger = Logger.getLogger(StudentCRSMenu.class);
	private StudentInterface studentOperation = StudentOperation.getInstance();
	private CourseOperation courseOperation = CourseOperation.getInstance();
	private Student student = new Student();

	/**
	 * Main Student Client which displays and manages all student related operations
	 * 
	 */
	public void studentClient() {
		Scanner input = null;

		try {
			input = new Scanner(System.in);
			int choice;
			logger.info("Welcome " + student.getUserName() + "!\n");
			do {
				showChoices();
				choice = input.nextInt();

				switch (choice) {
				case -1:
					logger.info(".....Logged Out.....\n");
					break;
				case 1:
					viewStudentDetails();
					break;
				case 2:
					studentOperation.showCourses();
					break;
				case 3:
					registerCourses();
					break;
				case 4:
					addCourse();
					break;
				case 5:
					dropCourse();
					break;
				case 6:
					studentOperation.viewRegisteredCourses(student);
					break;
				case 7:
					viewGrades();
					break;
				case 8:
					makePayment();
					break;
				case 9:
					updateInfo();
					break;
				default:
					logger.info("Invalid choice.\n");
					break;
				}
			} while (choice != -1);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}

	/**
	 * Initializes StudentCRSMenu class and calls studentClient to show respective
	 * menu.
	 * 
	 * @param email The email of the user who tried to login
	 */
	public void init(String email) {
		student = studentOperation.getStudentByEmail(email);
		LocalDateTime ldt = LocalDateTime.now();
		if (student.isApproved()) {
			logger.info("Login Time : " + ldt.getDayOfMonth() + "/" + ldt.getMonthValue() + "/" + ldt.getYear() + " " + ldt.getHour() + ":" + ldt.getMinute() + "\n");
			studentClient();
		} else {
			logger.info("Student not yet approved by Admin.\n");
		}
	}

	/**
	 * Method to handle registration related dialogue and operations
	 * 
	 */
	public void registerCourses() {
		Scanner input = null;
		try {
			if (student.getIsRegistered()) {
				logger.info("You have already registered.\n");
				return;
			}
			else if(studentOperation.getRegistrationSystemStatus() == false){
				logger.info("Registration Window is closed.\n");
				return;
			}

			int courseCounter = 0;
			ArrayList<Integer> courseCart = new ArrayList<>();
			logger.info("================COURSE REGISTRATION================\n");
			while (true) {
				logger.info("Enter 1 to view available courses.");
				logger.info("Enter 2 to add course.");
				logger.info("Enter 3 to delete course.");
				logger.info("Enter 4 to view course cart.");
				logger.info("Enter 5 to finish registration process.");
				logger.info("Enter 6 to cancel registration process.");
				input = new Scanner(System.in);
				int operation = Integer.parseInt(input.nextLine());
				if (operation == 1) {
					ArrayList<Course> courses = studentOperation.getAllCourses();
					logger.info("================AVAILABLE COURSES================\n");
					logger.info("Course ID    Course Name    Credits    Status");
					courses.forEach(course -> {
						int remaining = 10 - courseOperation.noOfEnrolledStudents(course.getCourseID());
						String status = "Full";
						if (courseCart.contains(course.getCourseID())) {
							status = "Chosen";
						} else if (remaining > 0) {
							status = Integer.toString(remaining) + " seats left";
						}
						logger.info(String.format("%-9d    %-11s    %-7d    %-6s", course.getCourseID(), course.getCourseName(), course.getCredits(), status));
					});
					
					logger.info("=================================================\n");
				} else if (operation == 2) {
					logger.info("Enter course ID to be added: ");
					int courseID = Integer.parseInt(input.nextLine());
					// addCourse(student,courseID);
					if (courseCart.contains(courseID)) {
						logger.info("Course " + courseID + " already in course cart\n");
					} else if (courseOperation.getCourseById(courseID) == null) {
						logger.info("Course " + courseID + " doesn't exist\n");
					} else if (courseOperation.noOfEnrolledStudents(courseID) >= 10) {
						logger.info("Course " + courseID + " is full. Please add some other course.\n");
					} else {
						courseCart.add(courseID);
						courseCounter++;
						logger.info("Course " + courseID + " added to Course Cart.\n");
					}
				} else if (operation == 3) {
					logger.info("Enter course ID to be dropped: ");
					int courseID = Integer.parseInt(input.nextLine());
					// deleteCourse(student,courseID);
					if (!courseCart.contains(courseID)) {
						logger.info("Course " + courseID + " not in cart\n");
					} else {
						courseCart.remove(Integer.valueOf(courseID));
						courseCounter--;
						logger.info("Course " + courseID + " deleted to Course Cart.\n");
					}
				} else if (operation == 4) {
					logger.info("============Course Cart============\n");
					logger.info("Course IDs:");
					courseCart.forEach(courseId -> logger.info(courseId));
					logger.info("====================================\n");
				} else if (operation == 5) {
					if (courseCounter >= 4 && courseCounter <= 6) {
						studentOperation.registerCourses(courseCart, student);
						break;
					} else if (courseCounter < 4) {
						logger.info("Less than 4 courses registered. Add more courses.\n");
					} else if (courseCounter > 6) {
						logger.info("More than 6 courses registered. Drop few courses.\n");
					}
				} else if (operation == 6) {
					logger.info("......... Exiting from Registration Process ...........\n");
					break;
				}
			}
			logger.info("==============================================\n");
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * Utility Function to Display all student operations
	 * 
	 */
	public void showChoices() {
		logger.info("Select an operation: ");
		logger.info("1. View student details");
		logger.info("2. Show courses");
		logger.info("3. Register courses");
		logger.info("4. Add a course");
		logger.info("5. Drop a course");
		logger.info("6. View registered courses");
		logger.info("7. View grades");
		logger.info("8. Make payment");
		logger.info("9. Update personal info");
		logger.info("-1 to Logout");
	}

	/**
	 * Method to add a course to a students registered courses list.
	 * 
	 */
	public void addCourse() {
		Scanner input = null;
		try {
			if (!student.getIsRegistered()) {
				logger.info("Student needs to register courses to add course.\n");
				return;
			} else if (studentOperation.getNumberOfEnrolledCourses(student) >= 6) {
				logger.info("Cannot add more courses. You already have 6 courses.\n");
				return;
			}
			else if(studentOperation.getRegistrationSystemStatus() == false){
				logger.info("Registration Window is closed.\n");
				return;
			}

			logger.info("Enter course ID to be added");
			input = new Scanner(System.in);
			int courseID = input.nextInt();
			studentOperation.addCourse(student, courseID);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}

	/**
	 *
	 * Method to drop a course from a students registered courses list.
	 */
	public void dropCourse() {
		Scanner input = null;
		try {
			if (!student.getIsRegistered()) {
				logger.info("Student needs to register courses to drop course\n");
				return;
			} else if (studentOperation.getNumberOfEnrolledCourses(student) == 4) {
				logger.info("Cannot drop the course. You only have 4 courses\n");
				return;
			}
			else if(studentOperation.getRegistrationSystemStatus() == false){
				logger.info("Registration Window is closed.\n");
				return;
			}

			logger.info("Enter course ID to be dropped");
			input = new Scanner(System.in);
			int courseID = input.nextInt();
			studentOperation.deleteCourse(student, courseID);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}

	/**
	 * Method to view grades for the logged in student
	 * 
	 */
	public void viewGrades() {
		if (!student.getIsRegistered()) {
			logger.info("You have not yet registered.\n");
		} else {
			studentOperation.viewGrades(student.getUserId());
		}
	}
	
	/**
	 * Method to make payment
	 * 
	 */
	public void makePayment() {
		Scanner sc = null;
		try {
			if (!student.getIsRegistered()) {
				logger.info("Please complete your course registration to make payment\n");
			} else if (student.getPaymentStatus()) {
				logger.info("Payment already made");
			} else {
				logger.info("Available options: \n");
				logger.info("1. To pay via Net banking");
				logger.info("2. To pay via Debit card");
				logger.info("3. To use Scholarship");
				logger.info("4. To cancel payment");
				sc = new Scanner(System.in);
				int choice = sc.nextInt();
				String method = null;
				
				// operations based on payment method
				switch (choice) {
				case 1: // for net banking method
					logger.info("You have chosen net banking");
					method = "Netbanking";
					break;
				case 2: // for credit card method
					logger.info("You have chosen debit card");
					method = "Debit Card";
					break;
				case 3: // for scholarship
					logger.info("You have chosen to use Scholarship");
					method = "Scholarship";
					break;
				case 4:
					logger.info(">>>>>>> Exiting <<<<<<<\n");
					break;
				default:
					logger.info("Invalid choice----Exiting----");
					logger.info("===========================================\n\n");
				}

				if (choice != 4) {
					logger.info(">>> Proceed to make payment <<<");
					studentOperation.makePayment(student,method);

					NotificationSystemOperation.paymentSuccessful();
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * Method to update info of student
	 * 
	 */
	public void updateInfo() {
		logger.info("================UPDATE INFO================\n");
		Scanner sc = null;

		try {
			sc = new Scanner(System.in);
			int choice;
			do {
				logger.info("Enter 1 to view student details.");
				logger.info("Enter 2 to update Name.");
				logger.info("Enter 3 to update Age.");
				logger.info("Enter 4 to update Address.");
				logger.info("Enter 5 to update Contact.");
				logger.info("Enter 6 to update Gender.");
				logger.info("Enter 7 to update Nationality.");
				logger.info("Enter 8 to confirm update.");
				logger.info("Enter -1 to exit.");

				choice = sc.nextInt();
				sc.nextLine();

				switch (choice) {
				case -1:
					logger.info(">>>>> Exiting without update <<<<<\n");
					break;
				case 1:
					viewStudentDetails();
					break;
				case 2:
					logger.info("Enter name:");
					String name = sc.nextLine();
					student.setUserName(name);
					break;
				case 3:
					logger.info("Enter age:");
					int age = Integer.parseInt(sc.nextLine());
					student.setAge(age);
					break;
				case 4:
					logger.info("Enter address:");
					String address = sc.nextLine();
					student.setAddress(address);
					break;
				case 5:
					logger.info("Enter contact:");
					String contact = sc.nextLine();
					student.setContact(contact);
					break;
				case 6:
					logger.info("Enter gender:");
					String gender = sc.nextLine();
					student.setGender(gender);
					break;
				case 7:
					logger.info("Enter nationality:");
					String nationality = sc.nextLine();
					student.setNationality(nationality);
					break;
				case 8:
					studentOperation.updateInfo(student);
					choice = -1;
					logger.info(">>>>>>> Student Information Updated. <<<<<<<\n");
					break;
				default:
					logger.info("Invalid choice.\n");
				}
			} while (choice != -1);
			
			student = studentOperation.getStudentByEmail(student.getEmail());
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}

	/**
	 * Showing student details, like ID, Name, Age, Address and more.
	 * 
	 */
	public void viewStudentDetails() {
		logger.info("============== Student Details ===============");
		logger.info("Student ID: " + student.getUserId());
		logger.info("Name: " + student.getUserName());
		logger.info("Age: " + student.getAge());
		logger.info("Address: " + student.getAddress());
		logger.info("Contact: " + student.getContact());
		logger.info("Branch: " + student.getBranch());
		logger.info("Gender: " + student.getGender());
		logger.info("Nationality: " + student.getNationality());
		logger.info("Registration status: " + (student.getIsRegistered() == true ? "Complete" : "Pending"));
		logger.info("Payment status: " + (student.getPaymentStatus() == true ? "Complete" : "Pending"));
		logger.info("===============================================\n");
	}
}