package com.flipkart.bean;

import java.util.ArrayList;

public class Course {
    private int courseID;
    private String courseName;
    private String professorAllotted;
    private int credits;
    private ArrayList<String> listOfEnrolledStudents = new ArrayList<>();

    public void setCourseID(int courseID){
        this.courseID = courseID;
    }

    public int getCourseID(){
        return courseID;
    }

    public void setCourseName(String courseName){
        this.courseName = courseName;
    }

    public String getCourseName(){
        return courseName;
    }

    public void setProfessorAllotted(String professorName){
        this.professorAllotted = professorName;
    }

    public String getProfessorAllotted(){
        return professorAllotted;
    }

    public void setCredits(int credits){
        this.credits = credits;
    }

    public int getCredits(){
        return credits;
    }

    public void setListOfEnrolledStudents(ArrayList<String> students){
        listOfEnrolledStudents.addAll(students);
    }

    public ArrayList<String> getListOfEnrolledStudents(){
        return listOfEnrolledStudents;
    }
}