package com.simplilearn.simplilearn_jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;

import com.simplilearn.simplilearn_jdbc.bean.User;

public class _04_PrStSelectById {

	public static void main(String[] args) {
		
		String dbUrl = "jdbc:mysql://localhost:3306/mydb";
		String dbUsername = "root";
		String dbPassword = "Astro78*llOvw67%";
		
		
		//Create the Select SQL Statement
		// Here in SQL with the JDBC library the index will always start in 1
		 String sql = "SELECT * FROM USER WHERE ID_USER = ?";	
		
		/*
		* Concatenating values to the SQL string allows Injection Attacks.
		* For instance: 1000 OR USERNAME='jdoe'
		*
		* Thats why in real life we use PreparedStatements instead of Statements
		*
		* Benefits of PreparedStatements:
		* 1. Avoids SQL Injection Attacks
		* 2. Precompile the SQL statement. They run faster.
		* NOTE: JDBC will remove the SQL injection in the parameter and will keep the part that
		* does not SQL injection. 
		* 
		* For instance: 2 OR USERNAME='jdoe' // Display the data of the ID_USER = 2.
		*/
		
		  String input = null;
	        try(Scanner scan = new Scanner(System.in)){
	            System.out.println("Enter the User ID: ");
	            input = scan.nextLine();
	        }

		//Print out the Query
		System.out.println("Query: " + sql);
		
		
		//Execute the Query
		 try(Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		            PreparedStatement prSt = con.prepareStatement(sql);) {

			
			/*
			Note that the Connection, PreparedStatement are closed  
			automatically by the try() with resources.
			
			We cannot create the Result Set in the previous try() because the ResultSet 
			requires the value of the placeholder  of the SQL statement.
			
			String sql = "SELECT * FROM USER WHERE ID_USER = ?"
															 |
															 |--Placeholder that needs to replace
			
			NOTE: Remember that in JDBC library indices start from 1, other JAVA libraries
			indices start from 0, as Java Collections.
			
			
			The index is for the place holders, not for the characters in the String;
			
			For instance: 
			
			sql = "SELECT * FROM USER WHERE ID_USER = ? AND USERNAME = ?"
			
			-The first place holder "?" has index 1.
			-The second place holder "?" has index 2.
			
			*/
			
			
			 prSt.setString(1, input); //Replace the placeholder with the input
				
			//We need another try() with resources for the ResultSet so JAVA will 
			// automatically close the ResultSet.
			
			//The inner try() with resources does not have catch() block, so if an exception				
			//occurs in the inner try(), it will go up to the outer try() with resources
			//and will get caught by the catch() block of the outer try() with resources.
				
			 try(ResultSet rs = prSt.executeQuery()){

	                createUser(input, rs);
	            }

	        } catch (SQLException ex) {

	            System.err.println("Error while accessing the DB! , " + ex.getSQLState() + ", " + ex.getMessage());
	        }

	    }

	// We move this code from the main() method to make more readable
	
	private static void createUser(String input, ResultSet rs) throws SQLException {

        User user = null;

        if (rs.next()) {

            user = new User();

            int idUser = rs.getInt("ID_USER");
            String username = rs.getString("USERNAME");
            String password = rs.getString("PASSWORD");
            String firstName = rs.getString("FIRST_NAME");
            String lastName = rs.getString("LAST_NAME");
            Date birth = rs.getDate("BIRTH");
            String status = rs.getString("STATUS");

            /*
             * Populate a User object.
             */
            user.setIdUser(idUser);
            user.setUsername(username);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setBirth(birth);
            user.setStatus(status);

            System.out.println("User found!!, User ID: " + input);

            System.out.println(user);
        }
        else {
            System.out.println("User not found, User ID: " + input);
        }
    }

}