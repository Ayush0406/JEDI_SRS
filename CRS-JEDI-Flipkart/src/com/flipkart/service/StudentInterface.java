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

    public void showCourses();
    
    public ArrayList<Course> getAllCourses();

    public ArrayList<Grades> viewGrades(int studentId);

    public void makePayment(Student student, String method);

    public boolean updateInfo(Student student);

    public boolean addCourse(Student student,int courseId);

    public boolean deleteCourse(Student student,int courseId);
    
    public boolean registerCourses(ArrayList<Integer> courseCart, Student student);

    public void viewRegisteredCourses(Student student);
    
    public Student getStudentByEmail(String email);

    public void setRegistrationStatus(Student student);
    
    public int getNumberOfEnrolledCourses(Student student);

    public boolean getRegistrationSystemStatus();

}
