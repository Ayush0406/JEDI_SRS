package com.flipkart.client;

import java.util.Scanner;

import org.apache.log4j.Logger;


import com.flipkart.bean.Student;
import com.flipkart.bean.User;
import com.flipkart.service.AuthCredentialSystemOperations;
import com.flipkart.util.ValidationOperation;

/**
 * The Main Entry Point of our application and shows choices for Registration or Login
 * 
 * @author jedi04
 */
public class MainCRSApplication {
	private static Logger logger = Logger.getLogger(MainCRSApplication.class);
	private static AuthCredentialSystemOperations authentication = AuthCredentialSystemOperations.getInstance();
	private static Scanner sc = null;

	/**
	 * The Main method for MainCRSApplication
	 * 
	 * @param args Unused
	 */
	public static void main(String[] args) {
		logger.info("Welcome!!!");
		try {
			sc = new Scanner(System.in);
			int choice;
			do {
				logger.info("1. Student Registration");
				logger.info("2. Login");
				logger.info("3. Exit Application");
				
				choice = sc.nextInt();
				sc.nextLine();
				
				switch(choice) {
					case 1: // Student Registrations
						studentRegistration();
						break;
					case 2: // Login
						login();
						break;
					case 3:
						logger.info(">>>>>>> Exiting Application <<<<<<<<");
						break;
					default:
						logger.info("Invalid choice.\n");
				}
			}while(choice != 3);
		}
		catch(Exception e) {
			logger.warn(e.getMessage() + "\n");
		}
	}
	
	/**
	 * Function to initiate and go through the student registration process
	 * 
	 */
	public static void studentRegistration() {
		logger.info("================ Student Registration =============");
		try {
			
			logger.info("Enter email: ");
			String email = ValidationOperation.readEmail();
			if(email.compareTo("-1") == 0)
				return;
			
			String password = ValidationOperation.readPassword();
			
			User user = new User();
			Student student = new Student();
			
			user.setEmail(email);
			student.setEmail(email);
			
			logger.info("Please enter name : ");
			user.setUserName(sc.nextLine());
			student.setUserName(user.getUserName());
			
			logger.info("Enter address : ");
			user.setAddress(sc.nextLine());
			student.setAddress(user.getAddress());
			
			logger.info("Enter Age : ");
			user.setAge(Integer.parseInt(sc.nextLine()));
			student.setAge(user.getAge());
			
			logger.info("Enter Gender : (male/female) : ");
			user.setGender(sc.nextLine());
			student.setGender(user.getGender());
			
			logger.info("Enter contact number : ");
			user.setContact(sc.nextLine());
			student.setContact(user.getContact());
			
			logger.info("Enter nationality : ");
			user.setNationality(sc.nextLine());
			student.setNationality(user.getNationality());
			
			logger.info("Enter branch : ");
			student.setBranch(sc.nextLine());
			
			logger.info("Enter rollno : ");
			student.setRollNo(Integer.parseInt(sc.nextLine()));
			
			user.setApproved(false);
			user.setRole("1");
			student.setIsRegistered(false);
			student.setPaymentStatus(false);
			
			authentication.registerStudent(user, student, password);
			
			logger.info(">>>>>>>>>>>>> Student Registration Successful. Admin will approve you within 24hrs. <<<<<<<<<\n");
		}
		catch(Exception e) {
			logger.warn(e.getMessage() + "\n");
		}
	}
	
	/**
	 * Function to initiate and go through the login process and then transfer control to respective clients based 
	 * on role of the user.
	 * 
	 */
	public static void login() {
		try {
			logger.info("============Login============");
			
			logger.info("Enter your email: ");
			String email = sc.nextLine();
			
			logger.info("Enter password: ");
			String pass = sc.nextLine();
			
			int res = authentication.login(email, pass);
			
			switch(res) {
				case 1: // Student
					StudentCRSMenu studentClient = new StudentCRSMenu();
					studentClient.init(email);
					break;
				case 2: // Professor
					ProfessorCRSMenu professorClient = new ProfessorCRSMenu();
					professorClient.init(email);
					break;
				case 3: // Admin
					AdminCRSMenu adminClient = new AdminCRSMenu();
					adminClient.AdminClient();
					break;
					
				default: 
						logger.info("Invalid credentials");
						break;
			}
			// res.getEmail();
			// res.getRole();
		}
		catch(Exception e) {
//			e.printStackTrace();
			logger.warn(e.getMessage() + "\n");
		}
	}
}
