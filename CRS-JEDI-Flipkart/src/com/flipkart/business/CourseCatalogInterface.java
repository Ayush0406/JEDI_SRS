package com.flipkart.business;

import com.flipkart.bean.Course;

public interface CourseCatalogInterface {
    public boolean addCourse(Course course);

    public boolean deleteCourse(int courseID);

    public void getCourseDetail(int courseID);

    public void assignProfessor(int courseID);
}
