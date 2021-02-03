/**
 * 
 */
package com.flipkart.rest;

import java.util.ArrayList;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.flipkart.bean.Course;
import com.flipkart.bean.Professor;
import com.flipkart.exception.ProfessorCRSException;
import com.flipkart.service.ProfessorOperation;
import com.flipkart.util.ResponseHelpers;

/**
 * 
 * Handles all Http requests related to professor operations Validators
 * NotNull-checks for null values, Size checks for length of string and
 * DecimalMin/DecimalMax checks for integer range
 * 
 * @author JEDI04
 *
 */

@Path("/professor")
public class ProfessorRESTAPI {
	ProfessorOperation professorOperation = ProfessorOperation.getInstance();

	/**
	 * Get the list of alloted courses
	 * 
	 * @param professorId
	 * @return Response
	 * @throws ValidationException
	 */
	@GET
	@Path("/allottedCourses")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllottedCourses(
			@DecimalMin(value = "100", message = "ProfessorID value has to be of 3 digits") @Digits(fraction = 0, integer = 3) @NotNull @QueryParam("professorId") Integer professorId)
			throws ValidationException {
		try {

			String professorName = professorOperation.getProfessorById(professorId);
			if (professorName == null) {
				return ResponseHelpers.badRequest(null, "No Professor with " + professorId + " exists");
			}
			ArrayList<JSONObject> al = professorOperation.showCourses(professorId);
			if (al.size() > 0) {
				return ResponseHelpers.success(al, "Success");
			}
			return ResponseHelpers.badRequest(null, "No Allotted Courses");
		} catch (ProfessorCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		} catch (Exception e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}

	}

	/**
	 * Method to view enrolled students
	 * 
	 * @param courseID course Id
	 * @return Response containing list of enrolled students
	 * @throws ValidationException
	 */
	@GET
	@Path("/enrolledStudents")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEnrolledStudents(
			@DecimalMin(value = "100", message = "courseID value has to be of 3 digits") @Digits(fraction = 0, integer = 3) @NotNull @QueryParam("courseID") Integer courseID)
			throws ValidationException {
		try {
			ArrayList<JSONObject> arr = professorOperation.viewStudentsEnrolled(courseID);
			if (arr.size() == 0) {
				return ResponseHelpers.badRequest(null, "No Enrolled Students Found in course id " + courseID);
			}

			return ResponseHelpers.success(arr, "Success");
		} catch (ProfessorCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		} catch (Exception e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
	}

	/**
	 * Method to view student grades
	 * 
	 * @param courseID course id
	 * @return Response returns students grades
	 * @throws ValidationException
	 */
	@GET
	@Path("/viewGrades")
	@Produces(MediaType.APPLICATION_JSON)
	public Response viewGrades(
			@DecimalMin(value = "100", message = "courseID value has to be of 3 digits") @Digits(fraction = 0, integer = 3) @NotNull @QueryParam("courseID") Integer courseID)
			throws ValidationException {
		try {
			ArrayList<JSONObject> al = professorOperation.viewGrades(courseID);
			JSONObject obj = new JSONObject();
			if (al.size() > 0) {
				return ResponseHelpers.success(al, "Success");
			}
			return ResponseHelpers.badRequest(null, "No student to view grades");
		} catch (ProfessorCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		} catch (Exception e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
	}

	/**
	 * Method to view update grade
	 * 
	 * @param courseID  course id
	 * @param studentID student id
	 * @param grade     grade
	 * @return response containing status of grade updated
	 * @throws ValidationException
	 */
	@PUT
	@Path("/updateGrade")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateGrade(
			@DecimalMin(value = "100", message = "courseID value has to be of 3 digits") @Digits(fraction = 0, integer = 3) @NotNull @FormParam("courseID") Integer courseID,

			@DecimalMin(value = "100", message = "StudentID value has to be of 3 digits") @Digits(fraction = 0, integer = 3) @NotNull @FormParam("studentID") Integer studentID,
			@NotNull @Size(min = 1, max = 2, message = "The length of Grade should be between 1 to 2") @FormParam("grade") String grade)
			throws ValidationException {
		try {
			boolean res = professorOperation.updateStudentGrade(courseID, studentID, grade);
			if (res) {
				return ResponseHelpers.success("Grade Updated", "Success");
			}
			return ResponseHelpers.badRequest(null, "Update grade failed as student doesn't exist in the course");
		}

		catch (ProfessorCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		} catch (Exception e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
	}

	/**
	 * Method to view student grades
	 * 
	 * @param professorId professorID
	 * @return Response returns students grades
	 * @throws ValidationException
	 */
	@GET
	@Path("/professorDetails")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProfessorById(
			@DecimalMin(value = "100", message = "professorID value has to be of 3 digits") @Digits(fraction = 0, integer = 3) @NotNull @QueryParam("professorID") Integer professorID)
			throws ValidationException {
		try {
			Professor professor = professorOperation.getProfessorById2(professorID);
			if (professor == null) {
				return ResponseHelpers.badRequest(null, "No professor found with id " + professorID);
			}
			return ResponseHelpers.success(professor, "Success");
		} catch (ProfessorCRSException e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		} catch (Exception e) {
			return ResponseHelpers.badRequest(null, e.getMessage());
		}
	}
}
