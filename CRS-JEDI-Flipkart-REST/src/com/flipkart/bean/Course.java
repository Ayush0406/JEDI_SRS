package com.flipkart.bean;

import java.util.ArrayList;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to interact with variables present in Course Class
 * using Getters and Setters Methods
 * Validators NotNull-checks for null values, Size checks for
 * length of string and DecimalMin/DecimalMax checks for integer range
 * @author JEDI04
 */

@XmlRootElement(name = "course")
public class Course {
	
	@NotNull
	@DecimalMin(value = "100", message = "CourseID has to be of 3 digits")
	@Digits(fraction = 0, integer = 3)
    private int courseID;
    
	@NotNull
    @Size(min = 2, max = 30, message = "The length of Course Name should be between 2 to 30")
    private String courseName;
    
    private int professorAllotted;
    
    @NotNull
    @DecimalMin(value = "2", message = "Credits shall be minimum of 2 unit")
    @DecimalMax(value = "10", message = "Credits should not be more than 10 units")
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
