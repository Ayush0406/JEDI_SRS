package com.flipkart.RESTController;

import java.util.*;
import javax.validation.ValidationException;
import javax.validation.constraints.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.flipkart.bean.Course;
import com.flipkart.bean.Grades;
import com.flipkart.bean.Student;
import com.flipkart.bean.User;
import com.flipkart.exception.CommonException;
import com.flipkart.exception.StudentCRSException;
import com.flipkart.service.AuthCredentialSystemOperations;
import com.flipkart.service.StudentOperation;
import com.flipkart.util.ResponseHelpers;

/**
 * Class to handle all Student related API operations.
 * 
 * Validators NotNull-checks for null values, Size checks for
 * length of string and DecimalMin/DecimalMax checks for integer range
 * @author JEDI04
 *
 */
@Path("/student")
public class StudentRESTAPI {

	private StudentOperation studentOperation = StudentOperation.getInstance();
	private AuthCredentialSystemOperations authentication = AuthCredentialSystemOperations.getInstance();
	private static Logger logger = Logger.getLogger(StudentOperation.class);
	private Student student = null;
	private User user = null;
	
	
	/**
     * Method to get the details of a student.
     *
     * @param id of the student whose details are to be fetched 
     * @return Response success when request serviced else bad request or something went wrong.
     * @throws ValidationException  
     */
	@GET
	@Path("/details/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentDetails(
			@NotNull
			@DecimalMin(value = "100", message = "studentID value has to be of 3 digits")
			@Digits(fraction = 0, integer = 3)
			@PathParam("id") int id) throws ValidationException {
		try {
			student = studentOperation.getStudentByID(id);
			return ResponseHelpers.success(student, "Success");
		}
		catch(StudentCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
		catch(Exception e) {
			return ResponseHelpers.somethingWentWrong(null);
		}
	}
	
	
	/**
     * Method to get all the courses in course catalog.
     *
     * @return Response success when request serviced else bad request or something went wrong.
     */
	@GET
	@Path("/allCourses")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCourses() { 
		try {
			ArrayList<Course> allCourses = studentOperation.getAllCourses();
			return ResponseHelpers.success(allCourses, "Success Updated");
		}
		catch(CommonException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
		catch(Exception e) {
			return ResponseHelpers.somethingWentWrong(null);
		}		
	}
	
	
	/**
     * Method to get the courses a student has registered for.
     *
     * @param id of the student whose details are to be fetched 
     * @return Response success when request serviced else bad request or something went wrong.
     * @throws ValidationException  
     */
	@GET
	@Path("/registeredCourses/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRegisteredCourses(
			@NotNull
			@DecimalMin(value = "100", message = "studentID value has to be of 3 digits")
			@Digits(fraction = 0, integer = 3)
			@PathParam("id") int id) throws ValidationException {
		try {
			student = studentOperation.getStudentByID(id);
			ArrayList<Course> registeredCourses = studentOperation.getRegisteredCourses(student);
			return ResponseHelpers.success(registeredCourses, "Success");
		}
		catch(StudentCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
		catch(Exception e) {
			return ResponseHelpers.somethingWentWrong(null);
		}
	}
	
	
	/**
     * Method to get the grades a student has attained.
     *
     * @param id of the student whose details are to be fetched 
     * @return Response success when request serviced else bad request or something went wrong.
     * @throws ValidationException  
     */
	@GET
	@Path("/grades/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGrades(
			@NotNull
			@DecimalMin(value = "100", message = "studentID value has to be of 3 digits")
			@Digits(fraction = 0, integer = 3)
			@PathParam("id") int id) throws ValidationException {
		try {
			student = studentOperation.getStudentByID(id);
			ArrayList<Grades> grades = studentOperation.viewGrades(id);
			return ResponseHelpers.success(grades, "Success");
		}
		catch(StudentCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
		catch(Exception e) {
			return ResponseHelpers.somethingWentWrong(null);
		}
	}
	
	
	/**
     * Method to add a course to registered courses of a student.
     *
     * @param courseID ID of the course which is to be added
     * @param studentID ID of student   
     * @return Response success when request serviced else bad request or something went wrong.
     * @throws ValidationException  
     */
	@POST
	@Path("/addCourse")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCourse(
			@NotNull
			@DecimalMin(value = "100", message = "courseID value has to be of 3 digits")
			@Digits(fraction = 0, integer = 3)
			@FormParam("courseID") int courseID,
			
			@NotNull
			@DecimalMin(value = "100", message = "studentID value has to be of 3 digits")
			@Digits(fraction = 0, integer = 3)
			@FormParam("studentID") int studentID) throws ValidationException {
		
		try {
			student = studentOperation.getStudentByID(studentID);
			studentOperation.addCourse(student, courseID);
			return ResponseHelpers.successPost(courseID, "Course with ID " + courseID + " succesfully added");
		}
		catch(StudentCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
		catch(Exception e) {
			return ResponseHelpers.somethingWentWrong(null);
		}
	}
	
	
	/**
     * Method to delete a course to registered courses of a student.
     *
     * @param courseID ID of the course which is to be deleted
     * @param studentID ID of student   
     * @return Response success when request serviced else bad request or something went wrong.
     * @throws ValidationException  
     */
	@DELETE
	@Path("/deleteCourse")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCourse(
			@NotNull
			@DecimalMin(value = "100", message = "courseID value has to be of 3 digits")
			@Digits(fraction = 0, integer = 3)
			@FormParam("courseID") int courseID, 
			
			@NotNull
			@DecimalMin(value = "100", message = "studentID value has to be of 3 digits")
			@Digits(fraction = 0, integer = 3)
			@FormParam("studentID") int studentID) throws ValidationException {
		try {
			student = studentOperation.getStudentByID(studentID);
			studentOperation.deleteCourse(student, courseID);
			return ResponseHelpers.success(courseID, "Course with ID " + courseID + " succesfully deleted");
		}
		catch(StudentCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
		catch(Exception e) {
			return ResponseHelpers.somethingWentWrong(null);
		}
	}
	
	
	/**
     * Method to register courses for a student.
     *
     * @param courseCart ArrayList that contains current course selections
     * @param studentID ID of student   
     * @return Response success when request serviced else bad request or something went wrong.
     * @throws ValidationException  
     */
	@POST
	@Path("/registerCourses/{studentID}")
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerCourses(
			@NotNull
			ArrayList<Integer> courseCart,
			
			@NotNull
			@DecimalMin(value = "100", message = "studentID value has to be of 3 digits")
			@Digits(fraction = 0, integer = 3)
			@PathParam("studentID") int studentID) throws ValidationException {
		try {
			student = studentOperation.getStudentByID(studentID);
			studentOperation.registerCourses(courseCart, student);
			return ResponseHelpers.successPost(courseCart, "Successfully registered for courses");
		}
		catch(StudentCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
		catch(Exception e) {
			return ResponseHelpers.somethingWentWrong(null);
		}
	}
	
	
	/**
     * Method to register a new student into the system.
     *
     * @param email Email of the new student
     * @param password Password of the new student
     * @param name Name of the new student
     * @param age Age of the new student
     * @param address Address of the new student
     * @param contact Contact of the new student
     * @param gender Gender of the new student
     * @param nationality Nationality of the new student
     * @param branch Branch of the new student
     * @param rollno Roll number of the new student   
     * @return Response success when request serviced else bad request or something went wrong.
     * @throws ValidationException  
     */
	@POST
	@Path("/registerStudent")
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerStudent(
			@Pattern(message = "Invalid Email Address->" + "Valid emails:user@gmail.com or my.user@domain.com etc.",
		            regexp = "^[a-zA-Z0-9_!#$%&*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
			@Size(min = 5, max = 30, message = "The length of email should be between 5 to 30")
			@FormParam("email") String email, 
			
			@Size(min = 5, max = 30, message = "The length of password should be between 5 to 30")
			@FormParam("password") String password,
			
			@Size(min = 3, max = 30, message = "The length of name should be between 5 to 30")
			@FormParam("name") String name, 
			
			@DecimalMin(value = "15", message = "Student shall be minimum of age 15 yr")
		    @DecimalMax(value = "30", message = "Student can not have age more than 30 yr")
			@FormParam("age") Integer age, 
			
			@Size(min = 3, max = 30, message = "The length of address should be between 5 to 30")
			@FormParam("address") String address, 
			
			@Size(min = 5, max = 30, message = "The length of contact should be between 5 to 30")
			@FormParam("contact") String contact, 
			
			@Pattern(message = "Please either enter male/female",
            regexp = "^male$|^female$")
			@FormParam("gender") String gender,
			
			@Size(min = 3, max = 30, message = "The length of nationality should be between 5 to 30")
			@FormParam("nationality") String nationality, 
			
			@Size(min = 2, max = 30, message = "The length of branch should be between 5 to 30")
			@FormParam("branch") String branch, 
			
			@NotNull
			@DecimalMin(value = "1000", message = "Roll value has to be of 4 digits")
			@Digits(fraction = 0, integer = 4)
			@FormParam("rollno") int rollno) throws ValidationException {
		
		try {
			student = new Student();
			user = new User();
			
			user.setEmail(email);
			student.setEmail(email);
			
			user.setUserName(name);
			student.setUserName(name);
			
			user.setAddress(address);
			student.setAddress(address);
			
			user.setAge(age);
			student.setAge(age);
			
			user.setGender(gender);
			student.setGender(gender);
			
			user.setContact(contact);
			student.setContact(contact);
			
			user.setNationality(nationality);
			student.setNationality(nationality);
			
			student.setBranch(branch);
			
			student.setRollNo(rollno);
			
			user.setApproved(false);
			user.setRole("1");
			student.setIsRegistered(false);
			student.setPaymentStatus(false);
			
			authentication.registerStudent(user, student, password);
			student = studentOperation.getStudentByEmail(email);
			return ResponseHelpers.successPost(student, "Successfully registered");
		}
		catch(CommonException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
		catch(Exception e) {
			return ResponseHelpers.somethingWentWrong(null);
		}
	}

	
	/**
     * Method to update details of a student.
     *
     * @param studentID ID of the student
     * @param name Name of the student
     * @param age Age of the student
     * @param address Address of the student
     * @param contact Contact of the student
     * @param gender Gender of the student
     * @param nationality Nationality of the student   
     * @return Response success when request serviced else bad request or something went wrong.
     * @throws ValidationException  
     */
	@PUT
	@Path("/updateInfo/{studentID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateInfo(
			@NotNull
			@DecimalMin(value = "100", message = "studentID value has to be of 3 digits")
			@Digits(fraction = 0, integer = 3)
			@PathParam("studentID") Integer studentID, 
			
			@Size(min = 5, max = 30, message = "The length of name should be between 5 to 30")
			@FormParam("name") String name, 
			
			@DecimalMin(value = "15", message = "Student shall be minimum of age 15 yr")
		    @DecimalMax(value = "30", message = "Student can not have age more than 30 yr")
			@FormParam("age") Integer age,
			
			@Size(min = 5, max = 30, message = "The length of address should be between 5 to 30")
			@FormParam("address") String address,
			
			@Size(min = 5, max = 30, message = "The length of contact should be between 5 to 30")
			@FormParam("contact") String contact,
			
			@Pattern(message = "Please either enter male/female",
            regexp = "^male$|^female$")
			@FormParam("gender") String gender,
			
			@Size(min = 5, max = 30, message = "The length of nationality should be between 5 to 30")
			@FormParam("nationality") String nationality) throws ValidationException {
		try {
			student = studentOperation.getStudentByID(studentID);
			student.setUserName(name);
			student.setAge(age);
			student.setAddress(address);
			student.setContact(contact);
			student.setGender(gender);
			student.setNationality(nationality);
			
			studentOperation.updateInfo(student);
			return ResponseHelpers.success(student, "Successfully updated info");
		}
		catch(StudentCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
		catch(Exception e) {
			logger.warn(e.getMessage());
			return ResponseHelpers.somethingWentWrong(null);
		}
	}	
	
	
	/**
     * Method to make payment after course registration.
     *
     * @param studentID ID of student
     * @param method Payment method chosen by student   
     * @return Response success when request serviced else bad request or something went wrong.
     * @throws ValidationException  
     */
	@PUT
	@Path("/payment/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response makePayment(
			@NotNull
			@DecimalMin(value = "100", message = "studentID value has to be of 3 digits")
			@Digits(fraction = 0, integer = 3)
			@PathParam("id") int studentID, 
			
			@Size(min = 5, max = 30, message = "The length of method of payment should be between 5 to 30")
			@FormParam("method") String method) {
		try {
			student = studentOperation.getStudentByID(studentID);
			studentOperation.makePayment(student,method);
			return ResponseHelpers.success(studentID, "Payment Successful");
		}
		catch(StudentCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
		catch(Exception e) {
			logger.warn(e.getMessage());
			return ResponseHelpers.somethingWentWrong(null);
		}
	}
		
}
