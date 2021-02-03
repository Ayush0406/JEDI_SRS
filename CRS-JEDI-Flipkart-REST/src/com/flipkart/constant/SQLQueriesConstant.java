package com.flipkart.constant;

import java.security.PublicKey;

/**
 * SQL Constants used for DAO Operations
 * @author jedi04
 *
 */
public class SQLQueriesConstant {

	// SQL Queries for User
	public static final String VERIFY_LOGIN_CREDENTIALS_QUERY = "SELECT role FROM credentials WHERE email=? AND password=?";
	public static final String CHECK_EMAIL_AVAILABILITY_QUERY = "SELECT * FROM credentials WHERE email=?";
	public static final String REGISTER_USER_QUERY = "INSERT INTO credentials(role, email, password, isApproved, address, age, gender, contact, nationality) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";


	// SQL Queries for Student
	public static final String GET_STUDENT_BY_EMAIL_QUERY = "SELECT st.email, st.name, st.rollno, st.branch, st.id, st.isRegistered, cr.isApproved, st.paymentStatus, cr.age, cr.address, cr.gender, cr.nationality, cr.contact FROM student as st INNER JOIN credentials as cr ON st.id = cr.id AND st.email=?";
	public static final String GET_STUDENT_BY_ID_QUERY = "SELECT st.email, st.name, st.rollno, st.branch, st.id, st.isRegistered, cr.isApproved, st.paymentStatus, cr.age, cr.address, cr.gender, cr.nationality, cr.contact FROM student as st INNER JOIN credentials as cr ON st.id = cr.id AND st.id=?";
	public static final String REGISTER_STUDENT_QUERY = "INSERT INTO student(id, name, email, rollno, branch, isRegistered, paymentStatus) values(?, ?, ?, ?, ?, ?, ?)";
	public static final String ADD_COURSE_STUDENT_QUERY = "INSERT INTO RegisteredCourses(studentID, courseID) values(?,?)";
	public static final String DROP_COURSE_STUDENT_QUERY = "DELETE FROM RegisteredCourses where studentID = ? and courseID = ?";
	public static final String GET_NO_OF_COURSES_QUERY = "SELECT COUNT(*) FROM RegisteredCourses WHERE studentID=?";
	public static final String GET_COURSE_QUERY = "SELECT COUNT(*) FROM RegisteredCourses WHERE studentID=? AND courseID=?";
	public static final String GET_ENROLLED_COURSES_QUERY = "SELECT rc.courseID, c.name, c.credits FROM RegisteredCourses as rc INNER JOIN course as c ON rc.courseID = c.id AND studentID=?";
	public static final String SET_REGISTRATION_STATUS_QUERY = "UPDATE student SET isRegistered = 1 where id = ?";
	public static final String GET_GRADES_QUERY = "SELECT grades.courseId, course.name as courseName, grades.grade, grades.studentId FROM grades INNER JOIN course ON grades.courseId = course.id AND grades.studentId=?";
	public static final String SET_PAYMENT_STATUS_QUERY = "UPDATE student SET paymentStatus=true, paymentDate=now(), paymentMethod=? WHERE id=?";
	public static final String ADD_COURSE_TO_GRADES_QUERY = "INSERT INTO grades(studentId, courseId) values(?,?)";
	public static final String DELETE_COURSE_FROM_GRADES_QUERY = "DELETE FROM grades WHERE studentID = ? AND courseID = ?";
	public static final String UPDATE_STUDENT_CREDENTIAL_QUERY = "UPDATE credentials SET age=?,address=?,contact=?,gender=?,nationality=? WHERE id=?";
	public static final String UPDATE_STUDENT_QUERY = "UPDATE student SET name=? WHERE id=?";
	public static final String GET_REGISTRATION_WINDOW_STATUS_QUERY = "SELECT isOpen FROM registrationDetails";

	// SQL Queries for Professor
	public static final String GET_PROFESSOR_BY_EMAIL = "select * from professor where email=?";
	public static final String SHOW_COURSES_PROFESSOR_QUERY = "Select id,name,credits from course where professorId=?";
	public static final String GET_ENROLLED_STUDENTS_PROFESSOR_QUERY = "select id,name,email,branch from student where id in (select studentId from grades where courseId = ? and grade = 'NA')";
	public static final String SET_GRADES_PROFESSOR_QUERY = "update grades set grade = ? where studentId = ? and courseId = ?";
	public static final String UPDATE_GRADES_PROFESSOR_QUERY = "update grades set grade=? where courseId =? and studentId=?";
	public static final String SHOW_GRADES_PROFESSOR_QUERY = "Select grade from grades where studentId = ? and courseId = ?";
	public static final String GET_STUDENTS_PROFESSOR_QUERY = "select id,name,email,branch from student where id in (select studentId from grades where courseId = ?)";
	public static final String GET_PROFESSOR_BY_ID_QUERY = "SELECT name FROM professor WHERE id = ?";
	public static final String GET_PROFESSOR_OBJECT_BY_ID_QUERY = "SELECT * FROM professor WHERE id = ?";
	public static final String GET_PROFESSOR_STUDENTS_QUERY = "Select * from student where id = ?";

	
	// SQL Queries for Course
	public static final String GET_COURSE_BY_ID_QUERY = "SELECT * FROM course WHERE id=?";
	public static final String GET_ALL_COURSES_QUERY = "SELECT cc.courseId, cc.courseName, cc.credits, c.professorId FROM courseCatalog AS cc INNER JOIN course AS c ON cc.courseId = c.id";
	public static final String NO_ENROLLED_STUDENTS_QUERY = "SELECT COUNT(*) FROM RegisteredCourses WHERE courseID=?";
	
	// SQL Queries for Admin
	
	public static final String GET_STUDENT_ID_BY_EMAIL = "select id from credentials where email=?";
	public static final String ADD_USER_TO_CREDENTIALS = "insert into credentials(role, email, password, isApproved, address, age, gender, contact, nationality) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String GET_STUDENT_NAME_BY_ID = "select name from student where id = ?";
	public static final String GET_GRADES_BY_STUDENT_ID = "select c.name,g.courseId,g.grade,s.name as sname from ((course as c inner join grades as g on c.id=g.courseId) inner join student as s on g.studentId=s.id) where s.id=?";
	public static final String ADD_COURSE_IN_CATALOG = "insert into courseCatalog (courseName,courseId,credits) values (?,?,?)";
	public static final String DELETE_COURSE_IN_CATALOG = "delete from courseCatalog where courseId = ?";
	public static final String GET_COURSE_NAME_BY_ID = "select name from course where id=?";
	public static final String UPDATE_PROFESSOR_IN_COURSE = "update course set professorId = ? where id=?";
	public static final String GET_COURSE_CATALAOG_QUERY = "select courseName,credits from courseCatalog where courseId=?";
	public static final String ADD_NEWCOURSE_IN_COURSE = "insert into course(id,name,professorId,credits) values(?,?,?,?)";
	public static final String GET_STUDENT_DETAILS = "select c.*, s.name from credentials as c join student as s on s.id = c.id where c.isApproved = 0 and c.role=1";
	public static final String UPDATE_USER_IN_CREDENTIALS = "update credentials set isApproved=1 where id = ?";
	public static final String DELETE_USER_FROM_CREDENTIALS = "Delete from credentials where id = ?";
	public static final String DELETE_STUDENT_BY_ID = "Delete from student where id = ?";
	public static final String ADD_NEW_ADMIN = "insert into admin values(?, ?, ?)";
	public static final String ADD_NEW_PROFESSOR = "insert into professor values(?, ?, ?, ?, ?)";
	public static final String GET_PROFESSOR_NAME_BY_ID = "select name from professor where id=?";
	public static final String DELETE_COURSE_BY_ID = "delete from course where id = ?";
	public static final String OPEN_REGISTRATION_WINDOW = "update registrationDetails set isOpen=true";
	public static final String CLOSE_REGISTRATION_WINDOW = "update registrationDetails set isOpen=false";
	public static final String GET_COURSE_INFO = "select courseId,courseName from courseCatalog";
	public static final String GET_COURSE_INFO_BY_ID = "select courseId,courseName from courseCatalog where courseId=?";
	public static final String GET_PROFESSOR_INFO_BY_ID = "select id,name from professor";
	public static final String GET_REGISTERED_STUDENTS = "select id,name from student where isRegistered=1";
	public static final String GET_ISAPPROVED_FROM_CREDENTIALS = "select isApproved from credentials where id=? and role=1";
	
}