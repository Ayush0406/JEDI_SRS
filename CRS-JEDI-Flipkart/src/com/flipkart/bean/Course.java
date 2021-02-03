package com.flipkart.bean;

import java.util.ArrayList;

/**
 * Class to interact with variables present in Course Class
 * using Getters and Setters Methods
 * @author JEDI04
 */
public class Course {
    private int courseID;
    private String courseName;
    private int professorAllotted;
    private int credits;
    private ArrayList<Student> listOfEnrolledStudents = new ArrayList<>();
    private String prerequisites;

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

    public void setProfessorAllotted(int professorID){
        this.professorAllotted = professorID;
    }

    public int getProfessorAllotted(){
        return professorAllotted;
    }

    public void setCredits(int credits){
        this.credits = credits;
    }

    public int getCredits(){
        return credits;
    }

    public void setListOfEnrolledStudents(ArrayList<Student> students){
        listOfEnrolledStudents.addAll(students);
    }

    public ArrayList<Student> getListOfEnrolledStudents(){
        return listOfEnrolledStudents;
    }

    public void setPrerequisites(String prerequisites){
        this.prerequisites = prerequisites;
    }

    public String getPrerequisites(){
        return prerequisites;
    }
}
