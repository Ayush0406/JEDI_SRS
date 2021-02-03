package com.flipkart.RESTController;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.flipkart.dao.AdminDAOOperation;
import com.flipkart.exception.AdminCRSException;
import com.flipkart.exception.StudentCRSException;
import com.flipkart.helper.AddAdminHelper;
import com.flipkart.helper.AddProfessorHelper;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.flipkart.bean.Admin;
import com.flipkart.bean.Course;
import com.flipkart.bean.Professor;
import com.flipkart.bean.ReportCard;
import com.flipkart.bean.Student;
import com.flipkart.service.AdminOperation;
import com.flipkart.service.StudentOperation;
import com.google.gson.Gson;
import com.flipkart.util.ResponseHelpers;
import com.flipkart.util.ValidationOperation;

/**
 * Handles Admin related functionality through rest services
 * 
 * Validators NotNull-checks for null values, Size checks for
 * length of string and DecimalMin/DecimalMax checks for integer range
 * @author JEDI04
 * 
 */
@Path("/admin")
public class AdminRESTAPI {
	AdminOperation adminOperation = AdminOperation.getInstance();
	AdminDAOOperation adminDAOOperation = AdminDAOOperation.getInstance();

	/**
	 * Generates the report card of a student containing the course name, courseId and grade obtained in the course.
	 * 
	 * @param id Student ID
	 * @return Response object containing report card as an ArrayList of JsonObjects.
	 * @throws ValidationException
	 */
	@GET
	@Path("/getReportCard")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getReportCard(
			@NotNull
			@DecimalMin(value = "100", message = "Student ID has to be greater than 100")
			@Digits(fraction = 0, integer = 3, message = "Student ID has to be of 3 digits")
			@QueryParam("id") Integer id) throws ValidationException
	{
		ArrayList<JSONObject> reportCard = new ArrayList<JSONObject>();
		StudentOperation studentOperation = StudentOperation.getInstance();
		try {
			Student student = studentOperation.getStudentByID(id);
			reportCard = adminOperation.generateReportCard(id);
		}
		catch(StudentCRSException e)
		{
			return ResponseHelpers.badRequest(reportCard, e.getMessage());
		}
		catch(AdminCRSException e)
		{
			return ResponseHelpers.badRequest(reportCard, e.getMessage());
		}
		catch(SQLException e)
		{
			return ResponseHelpers.somethingWentWrong("Some error occured in SQL.");
		}
		catch(Exception e)
		{
			return ResponseHelpers.somethingWentWrong(reportCard);
		}
		
		if(reportCard.size() == 0)
			return ResponseHelpers.badRequest(reportCard, "Unable to generate report card for id : "  + id);
		return ResponseHelpers.success(reportCard, "Report Card for " + id + "successfully generated.");
	}

	/**
	 * Returns list of registered students
	 * 
	 * @return Response object containing list of registered students
	 */
	@GET
	@Path("/getRegisteredStudents")
	@Produces(MediaType.APPLICATION_JSON)

	public Response getRegisteredStudents()
	{
		ArrayList<JSONObject> students = new ArrayList<JSONObject>();
		try {
			students = adminOperation.getRegisteredStudents();
		}
		catch(AdminCRSException e)
		{
			return ResponseHelpers.badRequest(students, "No registered students found.");
		}
		catch(SQLException e)
		{
			return ResponseHelpers.somethingWentWrong("Some error occured in SQL.");
		}
		catch(Exception e)
		{
			return ResponseHelpers.somethingWentWrong(students);
		}
		
		if(students.size() == 0)
			return ResponseHelpers.badRequest(students, "No registered students found.");
		return ResponseHelpers.success(students, "Success");
	}

	/**
	 * Functionality to add professor
	 * 
	 * @param str Details of professor to be added
	 * @return Response object containing status
	 * @throws ValidationException
	 */
	@POST
	@Path("/addProfessor")
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProfessor(@Valid AddProfessorHelper helper) throws ValidationException
	{
		String password = helper.getPassword();
		Professor prof = helper.getProf();
		int status = 0;
		try{
			status = adminOperation.addProfessor(password, prof);
		}
		catch(AdminCRSException e)
		{
			return ResponseHelpers.badRequest(status, "Professor entry " + prof.getEmail() + " already exists in database.");
		}
		catch(Exception e)
		{
			return ResponseHelpers.somethingWentWrong("Some internal error occured.");
		}
		
		if(status == 0)
		{
			return ResponseHelpers.badRequest(status, "Professor entry " + prof.getEmail() + " already exists in the database.");
		}
		return ResponseHelpers.success(status, "Prof. " + prof.getUserName() + " added.");		
	}

	/**
	 * Functionality to add admin
	 * 
	 * @param helper Admin helper object containing deatils of admin
	 * @return Response object containing status
	 * @throws ValidationException
	 */
	@POST
	@Path("/addAdmin")
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAdmin(@Valid AddAdminHelper helper) throws ValidationException {
		Admin admin = helper.getAdmin();
		String password = helper.getPassword();
		int status = 0;
		try
		{
			status = adminOperation.addAdmin(admin, password);
		}
		catch(AdminCRSException e)
		{
			return ResponseHelpers.badRequest(status, "Admin entry " + admin.getEmail() + " already exists in the database.");	
		}
		catch(Exception e)
		{
			return ResponseHelpers.somethingWentWrong("Some internal error occured.");
		}
		
		if(status == 0)
		{
			return ResponseHelpers.badRequest(status, "Admin entry " + admin.getEmail() + " already exists in database.");
		}
		return ResponseHelpers.success(status, "Admin " + admin.getUserName() + " added.");
	}

	/**
	 * Opens the Registration Window for course registration
	 * 
	 * @return Response
	 */

	@PUT
	@Path("/openRegistration")
	@Produces(MediaType.APPLICATION_JSON)
	public Response openRegistration() {
		try {
			boolean res = adminOperation.startRegistrationWindow();
			if (res) {
				return ResponseHelpers.success(null, "Registration Opened");
			}
			return ResponseHelpers.badRequest(null, "Request to open registration failed");
		} catch (Exception e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
	}

	/**
	 * Closes the Registration Window for course registration
	 * 
	 * @return Response
	 */

	@PUT
	@Path("/closeRegistration")
	@Produces(MediaType.APPLICATION_JSON)
	public Response closeRegistration() {
		try {
			boolean res = adminOperation.closeRegistrationWindow();
			if (res) {
				return ResponseHelpers.success(null, "Registration Closed");
			}
			return ResponseHelpers.badRequest(null, "Request to close registration failed");
		} catch (Exception e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
	}

	/**
	 * Adds new course in course catalog
	 * 
	 * @param course
	 * @return Response
	 * @throws ValidationException
	 */
	@POST
	@Path("/addCourse")
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCourse(@Valid Course course) throws ValidationException {
		try {
			boolean res = adminOperation.addCourse(course);
			if (res) {
				return ResponseHelpers.successPost(course, "Course Added Successfully");
			}
			return ResponseHelpers.badRequest(null, "Course Add Failed");
		} catch (AdminCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		} catch (Exception e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
	}

	/**
	 * Deletes course from course catalog and course table
	 * 
	 * @param courseID
	 * @return Response
	 * @throws ValidationException
	 */
	@DELETE
	@Path("/deleteCourse")
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCourse(
			@NotNull
			@DecimalMin(value = "100", message = "courseID value has to be of 3 digits")
			@Digits(fraction = 0, integer = 3)
			@QueryParam("courseID") Integer courseID) throws ValidationException{
		try {
			boolean res = adminOperation.deleteCourse(courseID);
			return ResponseHelpers.success(null, "Course Deleted Successfully");
		}catch(AdminCRSException e){
			return ResponseHelpers.badRequest(null, e.getMessage());
		}catch(Exception e){
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
	}

	/**
	 * Allot course to the professor
	 * 
	 * @param courseID
	 * @param professorID
	 * @return Response object containing message
	 * @throws ValidationException
	 */
	
	@PUT 
	@Path("/allotProfessor")
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response allotProfessor(
			@NotNull
			@DecimalMin(value = "100", message = "courseID value has to be of 3 digits")
			@Digits(fraction = 0, integer = 3)
			@QueryParam("courseID") Integer courseID, 
			
			@NotNull
			@DecimalMin(value = "100", message = "ProfessorID value has to be of 3 digits")
			@Digits(fraction = 0, integer = 3)
			@QueryParam("professorID") Integer professorID) throws ValidationException{
		try {
			boolean res = adminOperation.allotCourse(courseID,professorID);
			return ResponseHelpers.success(null, "Course Alloted Successfully");
			
		}catch(AdminCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}catch(Exception e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
	}

	/**
	 * approveStudent Used to approve the newly registered students in order to
	 * allow them to login and register for courses.
	 * @param studentID
	 * @return Response object containing message
	 * @throws ValidationException
	 */
	@PUT
	@Path("/approveStudent")
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response approveStudent(
			@NotNull
			@DecimalMin(value = "100", message = "studentID value has to be of 3 digits")
			@Digits(fraction = 0, integer = 3)
			@QueryParam("studentID") Integer studentID) throws ValidationException{
		try {
		boolean res = adminOperation.approveStudents(studentID);
		if(res==true) {
			return ResponseHelpers.success(null, "Student Approved!");
		}
		else{
			return ResponseHelpers.badRequest(null, "Some Error Occured");
		}
		}catch(AdminCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
		catch(Exception e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
	}
	
	/**
	 * Shows list of all professors
	 * @return Response object
	 */
	@GET
	@Path("/showProfessor")
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showProfessor(){
		ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
		try {
			arr = adminDAOOperation.showprofessor();
			return ResponseHelpers.success(arr, "List of Professors");
		}catch(AdminCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
		catch(Exception e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
	}
	
	/**
	 * Shows list of all courses
	 * @return Response object
	 */
	@GET
	@Path("/showCourses")
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showCourses(){
		ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
		try {
			arr = adminDAOOperation.showcourses();
			return ResponseHelpers.success(arr, "List of Courses");
		}catch(AdminCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
		catch(Exception e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
	}
	
	
	/**
	 * Shows list of students who are not approved
	 * @return Response object
	 */
	@GET
	@Path("/showUnapproved")
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showUnapproved(){
		ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
		try {
			arr = adminDAOOperation.showunapproved();
			return ResponseHelpers.success(arr, "List of Unappoved Students");
		}catch(AdminCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
		catch(Exception e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
	}
}
