package com.flipkart.service;
import com.flipkart.bean.*;
import com.flipkart.dao.ProfessorDAOOperation;
import com.flipkart.exception.StudentCRSException;
import org.apache.log4j.Logger;

import com.flipkart.bean.Student;
import com.flipkart.dao.CoursesDAOOperation;
import com.flipkart.dao.StudentDAOOperation;
import com.flipkart.bean.Course;

import java.util.ArrayList;



/**
 * Class to handle all student related operations. Accessed by Student CRS Menu
 *
 * @author JEDI 04
 */

public class StudentOperation implements StudentInterface {

    private static Logger logger = Logger.getLogger(StudentOperation.class);
    CoursesDAOOperation coursesDaoOperation = CoursesDAOOperation.getInstance();
    StudentDAOOperation studentDaoOperation = StudentDAOOperation.getInstance();
    ProfessorDAOOperation professorDAOOperation = ProfessorDAOOperation.getInstance();

    private static StudentOperation instance = null;

    private StudentOperation()
    {

    }

    synchronized public static StudentOperation getInstance()
    {
        if(instance == null)
        {
            instance = new StudentOperation();
        }
        return instance;
    }



    /**
     * Method to get the number of courses a student has registered for.
     *
     * @param student Object containing all student information
     * @return count of courses enrolled by student
     */
    public int getNumberOfEnrolledCourses(Student student) {
    	int count = 0;
    	try {
    		count = studentDaoOperation.getNoOfCourses(student);
    		if(count == 0){
    		    throw new StudentCRSException("No courses registered.\n");
            }
    	}
    	catch (StudentCRSException e){
    	    logger.warn(e.getMessage());
    	    return 0;
        }
    	catch (Exception e) {
    		 logger.warn(e.getMessage());
    	}
    	return count;
    }

    /**
     * Operation to get all the courses that are present in the Course Catalog
     *
     * @return List of course objects each containing information about a course
     */
    public ArrayList<Course> getAllCourses() {
    	ArrayList<Course> courses = null;
    	try{
            courses = coursesDaoOperation.getAllCourses();
            if(courses == null){
                throw new StudentCRSException("No courses available.\n");
            }
        }
    	catch (StudentCRSException e){
    	    logger.warn(e.getMessage());
        }
        catch (Exception e){
            logger.warn(e.getMessage());
        }
    	return courses;
    }

    /**
     * Method to print details of all courses available in course catalog
     *
     */
    public void showCourses(){
        try{
            ArrayList<Course> courses = coursesDaoOperation.getAllCourses();
            logger.info("====================AVAILABLE COURSES====================\n");
            logger.info("Course ID    Course Name    Credits    Professor Allotted");
            courses.forEach((course) ->{
            	String professorAllotted = professorDAOOperation.getProfessorById(course.getProfessorAllotted());
            	if(professorAllotted == null) {
            		professorAllotted = "Not yet alloted";
            	}
            	logger.info(String.format("%-9d    %-11s    %-7d    %-18s", course.getCourseID(), course.getCourseName(), course.getCredits(), professorAllotted));
            });
            logger.info("=========================================================\n");
        }
        catch (Exception e){
            logger.warn(e.getMessage());
        }
    }

    /**
     * Method to print grades attained by student in each of his registered courses
     *
     * @param studentId User ID of the student
     */
    public ArrayList<Grades> viewGrades(int studentId){
    	ArrayList<Grades> grades = null;
    	
    	try{
            grades = studentDaoOperation.getGrades(studentId);
            logger.info("======================GRADES===================\n");
            logger.info("Course ID    Course Name    Grade");
            grades.forEach(grade -> {
            	logger.info(String.format("%-9d    %-11s    %-5s", grade.getCourseId(), grade.getCourseName(), grade.getGrade()));
            });
            logger.info("=================================================\n");
        }
        catch(Exception e){
            logger.warn(e.getMessage());
        }
    	
    	return grades;
    }

    /**
     * Method to access the fee payment process
     *
     * @param student Object containing all information about a student
     */
    public void makePayment(Student student, String method){
        try {
        	studentDaoOperation.setPaymentStatus(student,method);
        	student.setPaymentStatus(true);
        }
        catch(Exception e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * Method accessed by student to update personal information
     *
     * @param student Object containing all information about a student
     * @return True when update successful else False
     */
    public boolean updateInfo(Student student){

        try {
            studentDaoOperation.updateInfo(student);
            return true;
        }
        catch (Exception e){
            logger.warn(e.getMessage());
        }

        return false;
    }

    /**
     * Operation for adding a course to registered courses of a student
     *
     * @param student Object containing all information about a student
     * @param courseId ID of the course to be added
     * @return True when course added successfully else False
     */
    public boolean addCourse(Student student, int courseId){
        try{
            studentDaoOperation.addCourse(student,courseId);
            return true;
        }
        catch(Exception e){
            logger.warn(e.getMessage());
        }

        return false;
    }

    /**
     * Operation for deleting a course to registered courses of a student
     *
     * @param student Object containing all information about a student
     * @param courseId ID of course to be deleted
     * @return True when course deleted successfully else False
     */
    public boolean deleteCourse(Student student, int courseId){

        try{
            studentDaoOperation.dropCourse(student,courseId);
            return true;
        }
        catch(Exception e){
            logger.warn(e.getMessage());
        }

        return false;
    }

    /**
     * Method to register courses selected by the student
     *
     * @param courseCart ArrayList that contains current course selections
     * @param student Object containing all information about a student
     * @return True when courses registered successfully else False
     */
    public boolean registerCourses(ArrayList<Integer> courseCart, Student student) {
    	try {
    		courseCart.forEach(courseID -> {
    			studentDaoOperation.addCourse(student, courseID);
    		});
            logger.info("Proceed to make payment\n");
            setRegistrationStatus(student);
            student.setIsRegistered(true);
            
            return true;
    	}
    	catch(Exception e) {
            logger.warn(e.getMessage());
    	}
    	return false;
    }


    /**
     * Method to view all the courses currently registered by the student
     *
     * @param student Object containing all information about a student
     */
    public void viewRegisteredCourses(Student student){

        try{
            if(!student.getIsRegistered()){
                logger.info("You have not registered any courses yet. Please register courses.\n");
            }
            else {
                ArrayList<Course> enrolledCourses = studentDaoOperation.getEnrolledCourses(student);
                logger.info("================REGISTERED COURSES================\n");
                logger.info("Course ID    Course Name    Credits");
                enrolledCourses.forEach(course -> {
                	logger.info(String.format("%-9d    %-11s    %-7d", course.getCourseID(), course.getCourseName(), course.getCredits()));
                });
                logger.info("==================================================\n");
            }
        }
        catch (Exception e){
            logger.warn(e.getMessage());
        }

    }


    /**
     * Method to get all information of a student as a student object
     *
     * @param email String that is the email of the user
     * @return A student object containing all information about a student
     */
    public Student getStudentByEmail(String email) {
        Student st = null;

        try {
            st = studentDaoOperation.getStudentByEmail(email);
            if(st==null){
                throw new StudentCRSException("Student Not Found!");
            }
        }
        catch (StudentCRSException e){
            logger.warn(e.getMessage());
        }
        catch (Exception e){
            logger.warn(e.getMessage());
        }

        return st;
    }

    /**
     * Method to set the registration status of the student as registered
     *
     * @param student Object containing all information about a student
     */
    public void setRegistrationStatus(Student student){
        try{
            studentDaoOperation.setRegistrationStatus(student);
        }
        catch (Exception e){
            logger.warn(e.getMessage());
        }
    }

    /**
     * To get the status of the registration window as open or closed
     *
     * @return True if registration window is open else False
     */
    public boolean getRegistrationSystemStatus(){
        boolean status = false;

        try{
            status = studentDaoOperation.getRegistrationSystemStatus();
        }
        catch (Exception e){
            logger.warn(e.getMessage());
        }
        return status;
    }
}



