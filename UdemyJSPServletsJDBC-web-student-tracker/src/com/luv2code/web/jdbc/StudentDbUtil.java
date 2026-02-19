package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	private DataSource dataSource;
	public StudentDbUtil(DataSource theDataSource) {
		this.dataSource = theDataSource;
	}
	
	public List<Student> getStudents() throws Exception{
		List<Student> students = new ArrayList<>();
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			// get a connection
			myConn = dataSource.getConnection();
			// create sql statement
		    myStmt = myConn.createStatement();
		    String sql = "SELECT * FROM student ORDER BY last_name";
		    // execute the sql
		    myRs = myStmt.executeQuery(sql);
		    // process result set
		    while (myRs.next()) {
		    	// retrieve data from result set row
		    	int id = myRs.getInt("id");
		    	String firstName = myRs.getString("first_name");
		    	String lastName = myRs.getString("last_name");
		    	String email = myRs.getString("email");
		    	// create new student object and add it to the list of students
		    	students.add(new Student(id, firstName, lastName, email));
		    }
		    return students;
		} finally {
			// close JDBC objects;
			close(myConn, myStmt, myRs);
		}
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		try {
			if (myRs != null) {
				myRs.close();
			}
			if (myStmt != null) {
				myStmt.close();
			}
			if (myConn != null) {
				myConn.close(); // doesn't really close it... just puts back in connection pool
			}
		}
		catch(Exception exc) {
			exc.printStackTrace();
		}		
	}

	public void addStudent(Student theStudent) throws SQLException {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try {
			// get a connection
			myConn = dataSource.getConnection();
			// create sql for insert
			String sql = "INSERT INTO student (first_name,last_name,email) VALUES (?,?,?)";
			myStmt = myConn.prepareStatement(sql);
			// set the param values for the student
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			// execute sql insert
			myStmt.execute();
		} finally {
			// clean up JDBC objects
		    close(myConn, myStmt, null);
		}
	}

	public Student getStudent(String theStudentId) throws Exception {
		Student theStudent = null;
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		try {
			// get a connection
			myConn = dataSource.getConnection();
			// create sql for query
			String sql = "SELECT * FROM student WHERE id=?";
			myStmt = myConn.prepareStatement(sql);
			// set the param values for the student
			studentId = Integer.parseInt(theStudentId);
			myStmt.setInt(1, studentId);
			// execute the sql
			myRs = myStmt.executeQuery();
			// return the student
			if (myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				// use the studentId during construction
				theStudent = new Student(studentId, firstName, lastName, email);
			} else {
				throw new Exception("Could not find student id: " + studentId);
			}
			return theStudent;
		}
		finally {
			// clean up JDBC objects
			close(myConn, myStmt, myRs);
		}
	}

	public void updateStudent(Student theStudent) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		int id = theStudent.getId();
		try {
			// get a connection
			myConn = dataSource.getConnection();
			// create the sql
			String sql = "UPDATE student SET first_name=?,last_name=?,email=? WHERE id=?";
			myStmt = myConn.prepareStatement(sql);
			// set the params
			String firstName = theStudent.getFirstName();
			String lastName = theStudent.getLastName();
			String email = theStudent.getEmail();
			myStmt.setString(1, firstName);
			myStmt.setString(2, lastName);
			myStmt.setString(3, email);
			myStmt.setInt(4, id);
			// execute the update sql
			myStmt.execute();
		}
		finally {
			// clean up the JDBC object
			close(myConn, myStmt, null);
		}
	}
		
}
