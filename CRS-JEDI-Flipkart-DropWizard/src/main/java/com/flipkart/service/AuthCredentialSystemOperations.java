package com.flipkart.service;

import org.apache.log4j.Logger;

import com.flipkart.bean.Student;
import com.flipkart.bean.User;
import com.flipkart.dao.StudentDAOOperation;
import com.flipkart.dao.UserDAOOperation;
import com.flipkart.exception.CommonException;
import com.flipkart.exception.StudentCRSException;

/**
 * Class to handle all authentication processes.
 *
 * @author JEDI 04
 */

public class AuthCredentialSystemOperations implements AuthCredentialSystemInterface {

	private Logger logger = Logger.getLogger(AuthCredentialSystemOperations.class);
	private static UserDAOOperation userDaoOperation = UserDAOOperation.getInstance();
	private static StudentDAOOperation studentDaoOperation = StudentDAOOperation.getInstance();

	private static AuthCredentialSystemOperations instance = null;

	private AuthCredentialSystemOperations()
	{

	}

	synchronized public static AuthCredentialSystemOperations getInstance()
	{
		if(instance == null)
		{
			instance = new AuthCredentialSystemOperations();
		}
		return instance;
	}

	/**
	 * Method to login into the application and get the role of the user
	 *
	 * @param username Email of the user trying to login
	 * @param password Password of the user trying to login
	 * @return Role of the used
	 */
	public int login(String username, String password) {
		int role = -1;

		try{
			role = userDaoOperation.verifyLoginCredentials(username, password);
			if(role != 1 && role != 2 && role != 3) {
				throw new CommonException(">>>>>>>>> Invalid Credentials <<<<<<<<<");
			}
		}
		catch(CommonException e) {
			logger.warn(e.getMessage() + "\n");
		}
		catch (Exception e){
			logger.warn(e.getMessage());
		}

		return role;
	}

	/**
	 * Method to verify whether the email entered exists or not
	 *
	 * @param email Email of the user trying to login
	 * @return True if the email exists else False
	 */
	public boolean checkEmailAvailability(String email) {
		boolean available = false;
		try {
			available = userDaoOperation.checkEmailAvailability(email);
			if(!available) {
				throw new CommonException(">>>>> Email is not available <<<<<<");
			}
		}
		catch(CommonException e) {
			logger.warn(e.getMessage() + "\n");
		}
		catch(Exception e) {
			logger.warn(e.getMessage());
		}
		return available;
	}

	/**
	 * Method to register a new student into the system
	 *
	 * @param user Object containing all information about user
	 * @param student Object containing all information about student
	 * @param password Password of the user
	 * @throws StudentCRSException, Exception
	 */
	public void registerStudent(User user, Student student, String password) throws StudentCRSException, Exception {
		int id = -1;
		if(userDaoOperation.checkEmailAvailability(user.getEmail()) == false) {
			throw new CommonException("Another user has already registered with this email");
		}
		try {
			id = registerUser(user, password);
			studentDaoOperation.registerStudent(student, id);
		}
		catch(Exception e) {
			logger.info(e.getMessage());
			throw e;
		}
	}

	/**
	 * Method to register a new professor or administrator into the system
	 *
	 * @param user Object containing all information about user
	 * @param password Password of the user
	 * @return The id of the newly registered user
	 * @throws StudentCRSException, Exception
	 */
	@Override
	public int registerUser(User user, String password) throws StudentCRSException, Exception {
		int id = -1;
		id = userDaoOperation.registerUser(user, password);
		return id;
	}

	
}
