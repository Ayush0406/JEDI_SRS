package com.flipkart.dao;

import java.sql.*;

import com.flipkart.constant.SQLQueriesConstant;
import com.flipkart.exception.StudentCRSException;

import org.apache.log4j.Logger;

import com.flipkart.bean.User;
import com.flipkart.util.DBConnection;

/**
 * Primary Class undertaking all database queries related to User Operations
 *
 * @author JEDI 04
 */
public class UserDAOOperation implements UserDAOInterface {
	
	private static Logger logger = Logger.getLogger(UserDAOOperation.class);
	Connection connection = null;
	PreparedStatement ps = null; 

	private static UserDAOOperation instance = null;

	private UserDAOOperation()
	{

	}

	synchronized public static UserDAOOperation getInstance()
	{
		if(instance == null)
		{
			instance = new UserDAOOperation();
		}
		return instance;
	}
	
	/**
	 * Method to verify login credentials, i.e email and password
	 * 
	 * @param email The email address for which verification is needed
	 * @param password The password entered corresponding to the email address.
	 */
	public int verifyLoginCredentials(String email, String password) {
		int role = -1;
		try {
			connection = DBConnection.getConnection();
			ps = connection.prepareStatement(SQLQueriesConstant.VERIFY_LOGIN_CREDENTIALS_QUERY);
			
			ps.setString(1, email);
			ps.setString(2, password);
			
			ResultSet result = ps.executeQuery();

			while(result.next()) {
				role = result.getInt("role");
			}
			return role;
		} 
		catch(SQLException e) {
			logger.warn(e.getMessage() + "\n");
		}
		catch(Exception e) {
			logger.warn(e.getMessage() + "\n");
		}
		return role;
	}
	
	/**
	 * Method to check whether or not an email address is present.
	 * 
	 * @param email The email address whose presence is to be verified.
	 * @throws StudentCRSException, Exception
	 */
	@Override
	public boolean checkEmailAvailability(String email) throws StudentCRSException, Exception{
		connection = DBConnection.getConnection();
		ps = connection.prepareStatement(SQLQueriesConstant.CHECK_EMAIL_AVAILABILITY_QUERY);
		ps.setString(1, email);

		ResultSet result = ps.executeQuery();
		if (result.next())
			return false;
		return true;
	}
	
	/**
	 * Method to make an entry into credentials table for given User Object
	 * 
	 * @param user User Object containing the necessary information of the user whose entry is to made.
	 * @param password The password with which the entry is to be made.
	 * @throws StudentCRSException, Exception
	 */
	@Override
	public int registerUser(User user, String password) throws StudentCRSException, Exception {
		int id = -1;
		connection = DBConnection.getConnection();
		ps = connection.prepareStatement(SQLQueriesConstant.REGISTER_USER_QUERY, Statement.RETURN_GENERATED_KEYS);

		ps.setInt(1, Integer.parseInt(user.getRole()));
		ps.setString(2,  user.getEmail());
		ps.setString(3,  password);
		ps.setBoolean(4,  user.isApproved());
		ps.setString(5, user.getAddress());
		ps.setInt(6, user.getAge());
		ps.setString(7, user.getGender());
		ps.setString(8,  user.getContact());
		ps.setString(9, user.getNationality());
		
		ps.executeUpdate();
		ResultSet resultSet = ps.getGeneratedKeys();
		
		resultSet.next();
		id = resultSet.getInt(1);
		return id;
	}

}
