package com.flipkart.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Manages the connection with MySQL server
 * @author JEDI04
 */
public class DBConnection {
	public static Connection connection = null;
	private static Logger logger = Logger.getLogger(DBConnection.class);

	/**
	 * Fetches and establishes a connection with MySQL server
	 * with the mentioned database and login credentials
	 * @return Connection which is used to fire queries to the connected database 
	 */
	public static Connection getConnection() {
		if (connection != null)
            return connection;
        else {
            try {
            	Properties prop = new Properties();
                InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream("./configs.properties");
                prop.load(inputStream);
                String driver = prop.getProperty("driver");
                String url = prop.getProperty("url");
                String user = prop.getProperty("user");
                String password = prop.getProperty("password");
                Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return connection;
       }
	}

	/**
	 * Terminates connection with the database if instance 
	 * of connection is not null
	 */
	public static void closeConnection() {
		if(connection != null) {
			try {
				connection.close();
				logger.info("Connection Closed");
			} catch(Exception e) {
				logger.warn(e.getMessage());
			}
		}
		else {
			logger.info("Connection already closed");
		}
	}
}
