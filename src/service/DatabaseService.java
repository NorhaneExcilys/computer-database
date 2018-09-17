package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <b>DatabaseService is the class that enable a connection to a database thanks to JDBC.</b>
 * A DatabaseService is characterized by the following informations:
 * <ul>
 * <li> A connection</li>
 * </ul>
 * @author elgharbi
 *
 */

public class DatabaseService {

	/**
	 * contains the singleton databaseService
	 */
	private static DatabaseService databaseService;
	
	/**
	 * contains the connection of this databaseService 
	 */
	private Connection connection;
	
	/**
	 * builds DatabaseService and initialize the attribute connection to null
	 */
	private DatabaseService() {
		connection = null;
	}

	/**
	 * builds DatabaseService if it isn't created or return the actual databaseService
	 * @return the actual databaseService
	 */
	public static DatabaseService getInstance() {
		if (databaseService == null) {
			return new DatabaseService();
		}
		return databaseService;
	}
	
	
	public Connection getConnection() {
		return this.connection;
	}
	
	/**
	 * Load of the driver JDBC for MySQL
	 */
	public void loadDriver() {
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		} catch ( ClassNotFoundException e ) {
		    
		}
	}
	
	/**
	 * Connects to the database computer-database-db
	 */
	public void connectDatabase() {
		String url = "jdbc:mysql://localhost:3306/computer-database-db";
		String user = "admincdb";
		String password = "qwerty1234";
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {

		}
	}
	
	/**
	 * Connects to the database defined by the following parameters
	 * @param url the url of the database
	 * @param user the user to connect to the database
	 * @param password the associate password
	 */
	public void connectDatabase(String url, String user, String password) {
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch ( SQLException e ) {

		}
	}
	
	/**
	 * Execute and return the result of a reading query
	 * @param query the reading query
	 * @return the result of the query
	 */
	public ResultSet executeQuery(String query) {
		ResultSet queryResult = null;
		try {
			Statement statement = connection.createStatement();
			queryResult = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return queryResult;
	}
	
	/**
	 * Execute and return the result of an update query
	 * @param query the query
	 * @return the result of the query
	 */
	public int executeUpdate(String query) {
		int queryResult = -1;
		try {
			Statement statement = connection.createStatement();
			queryResult = statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return queryResult;
	}
	

	
	/**
	 * Close the actual connection
	 */
	@Override
	protected void finalize() {
		if (connection != null) {
	        try {
	        	connection.close();
	        } catch (SQLException ignore) {
	            
	        }
	    }
	}
}
