package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
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
		
}
