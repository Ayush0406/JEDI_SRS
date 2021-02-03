package com.flipkart.service;

import com.flipkart.bean.Course;
import com.flipkart.bean.Grades;
import com.flipkart.bean.Student;

import java.util.ArrayList;

/**
 * Interface to Show the structure of
 * Student Operation Class 
 * @author JEDI04
 */

public interface StudentInterface {
    
    public ArrayList<Course> getAllCourses() throws Exception;

    public ArrayList<Grades> viewGrades(int studentId) throws Exception;

    public void makePayment(Student student, String method) throws Exception;

    public boolean updateInfo(Student student) throws Exception;

    public boolean addCourse(Student student,int courseId) throws Exception;

    public boolean deleteCourse(Student student,int courseId) throws Exception;
    
    public boolean registerCourses(ArrayList<Integer> courseCart, Student student) throws Exception;

    public void viewRegisteredCourses(Student student);
    
    public Student getStudentByEmail(String email);
    
    public Student getStudentByID(int studentID) throws Exception;

    public void setRegistrationStatus(Student student);
    
    public int getNumberOfEnrolledCourses(Student student);

    public boolean getRegistrationSystemStatus();
    
    public ArrayList<Course> getRegisteredCourses(Student student) throws Exception;
}
