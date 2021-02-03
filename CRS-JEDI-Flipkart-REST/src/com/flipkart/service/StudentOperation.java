package com.flipkart.service;
import com.flipkart.bean.*;
import com.flipkart.dao.ProfessorDAOOperation;
import com.flipkart.exception.StudentCRSException;
import org.apache.log4j.Logger;

import com.flipkart.dao.CoursesDAOOperation;
import com.flipkart.dao.StudentDAOOperation;

import java.sql.SQLException;
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
	 * Method to get the student object from student ID
	 * 
	 * @param student The student object containing the student related information to make an entry
	 * @throws StudentCRSException, Exception
	 */
    public Student getStudentByID(int studentID) throws StudentCRSException, Exception {
    	Student student = null;
    	student = studentDaoOperation.getStudentByID(studentID);
    	return student;
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
     * @throws StudentCRSException, Exception
     */
    public ArrayList<Course> getAllCourses() throws StudentCRSException, Exception {
    	ArrayList<Course> courses = null;
        courses = coursesDaoOperation.getAllCourses();
    	return courses;
    }

    /**
     * Method to print details of all courses available in course catalog
     *
     */
    public void showCourses(){
        try {
			try {
			    ArrayList<Course> courses = coursesDaoOperation.getAllCourses();
			    logger.info("================AVAILABLE COURSES================\n");
			    logger.info("Course ID    Course Name    Credits    Professor Allotted");
			    courses.forEach((course) ->{
			    	String professorAllotted = null;
					try {
						professorAllotted = professorDAOOperation.getProfessorById(course.getProfessorAllotted());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	if(professorAllotted == null) {
			    		professorAllotted = "Not yet alloted";
			    	}
			    	logger.info(String.format("%9d    %11s    %7d    %18s", course.getCourseID(), course.getCourseName(), course.getCredits(), professorAllotted));
			    });
			    logger.info("=================================================\n");
			}
			catch (Exception e){
			    logger.warn(e.getMessage());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
		    logger.warn(e.getMessage());
		}
    }


    /**
     * Method to print grades attained by student in each of his registered courses
     *
     * @param studentId User ID of the student
     * @return grades ArrayList containing grades attained by a student
     * @throws StudentCRSException, Exception
     */
    public ArrayList<Grades> viewGrades(int studentId) throws StudentCRSException, Exception {
    	ArrayList<Grades> grades = null;
		grades = studentDaoOperation.getGrades(studentId);
        logger.info("======================GRADES===================\n");
        logger.info("Course ID    Course Name    Grade");
        grades.forEach(grade -> {
        	logger.info(String.format("%9d    %11s    %5s", grade.getCourseId(), grade.getCourseName(), grade.getGrade()));
        });
        logger.info("=================================================\n");
    	return grades;
    }

    /**
     * Method to access the fee payment process
     *
     * @param student Object containing all information about a student
     * @throws StudentCRSException, Exception
     */
    public void makePayment(Student student, String method) throws StudentCRSException, Exception {
    	if(student.getIsRegistered() == false) {
    		throw new StudentCRSException("Student has not yet completed course registration.");
    	}
    	else if(student.getPaymentStatus() == true) {
    		throw new StudentCRSException("Payment has already been made.");
    	}
    	else {
    		studentDaoOperation.setPaymentStatus(student, method);
        	student.setPaymentStatus(true);
    	}
    }

    /**
     * Method accessed by student to update personal information
     *
     * @param student Object containing all information about a student
     * @return True when update successful else False
     * @throws StudentCRSException, Exception
     */
    public boolean updateInfo(Student student) throws StudentCRSException, Exception {
        studentDaoOperation.updateInfo(student);
        return true;
    }

    /**
     * Operation for adding a course to registered courses of a student
     *
     * @param student Object containing all information about a student
     * @param courseId ID of the course to be added
     * @return True when course added successfully else False
     * @throws StudentCRSException, Exception
     */
    public boolean addCourse(Student student, int courseId) throws StudentCRSException, Exception {
        studentDaoOperation.addCourse(student,courseId);
        return true;
    }

    /**
     * Operation for deleting a course to registered courses of a student
     *
     * @param student Object containing all information about a student
     * @param courseId ID of course to be deleted
     * @return True when course deleted successfully else False
     * @throws StudentCRSException, Exception
     */
    public boolean deleteCourse(Student student, int courseId) throws StudentCRSException, Exception {
    	studentDaoOperation.dropCourse(student,courseId);
        return false;
    }

    /**
     * Method to register courses selected by the student
     *
     * @param courseCart ArrayList that contains current course selections
     * @param student Object containing all information about a student
     * @return True when courses registered successfully else False
     * @throws StudentCRSException, Exception
     */
    public boolean registerCourses(ArrayList<Integer> courseCart, Student student) throws StudentCRSException, Exception {
    	if(student.getIsRegistered()) {
    		throw new StudentCRSException("Student has already registered courses.");
    	}
    	else if(courseCart.size() < 4) {
    		throw new StudentCRSException("Less than 4 courses selected");
    	}
    	else if(courseCart.size() > 6){
    		throw new StudentCRSException("More than 6 courses selected");
    	}
    	else if(getRegistrationSystemStatus() == false){
    		throw new StudentCRSException("Registration Window is closed");
		}
    	else {
    		try {
    			for(Integer courseID: courseCart) {
        			Course course = coursesDaoOperation.getCourseByID(courseID);
        			if(course == null) {
        				throw new StudentCRSException("Course ID: " + courseID + " is invalid.");
        			}
        			else if(coursesDaoOperation.noOfEnrolledStudents(courseID)>=10){
        				throw new StudentCRSException("Course " + courseID + " is full.");
        			}
        		}
    			for(Integer courseID: courseCart) {
        			studentDaoOperation.addCourse(student, courseID);
        		}
    			logger.info("Proceed to make payment\n");
                setRegistrationStatus(student);
                student.setIsRegistered(true);
    		}
    		catch(StudentCRSException e) {
    			throw new StudentCRSException(e.getMessage() + " Aborting registration process.");
    		}
    	}
        return true;
    }
    /**
     * Method to fetch the registered courses for a student
     * @param student Student object containing data about the student
     * @return Returns the list of registered courses
     * @throws StudentCRSException, Exception
     */
    public ArrayList<Course> getRegisteredCourses(Student student) throws StudentCRSException, Exception {
    	ArrayList<Course> courses = null;
		if(!student.getIsRegistered()){
            throw new StudentCRSException("You have not registered any courses yet. Please register courses.\n");
        }
        else {
            courses = studentDaoOperation.getEnrolledCourses(student);
            logger.info("================REGISTERED COURSES================\n");
            logger.info("Course ID    Course Name    Credits");
            courses.forEach(course -> {
            	logger.info(String.format("%9d    %11s    %7d", course.getCourseID(), course.getCourseName(), course.getCredits()));
            });
            logger.info("==================================================\n");
        }
    	return courses;
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
                	logger.info(String.format("%9d    %11s    %7d", course.getCourseID(), course.getCourseName(), course.getCredits()));
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



