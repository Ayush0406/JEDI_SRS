package com.flipkart.service;

import com.flipkart.bean.Course;
/**
 * Interface to Show the structure of
 * Course Operation Class
 * @author JEDI04
 */

public interface CourseInterface {

    public int noOfEnrolledStudents(int courseID);
    
    public Course getCourseById(int courseID);
}
