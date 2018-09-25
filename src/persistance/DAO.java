package persistance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import exception.DatabaseException;

/**
 * <b>DAO is the class that enable a connection to a database thanks to JDBC.</b>
 * A DAO is characterized by the following informations:
 * @author elgharbi
 *
 */

public class DAO {
	
	private static String database;
	private static String dbuser;
	private static String dbpassword;
	
	private static DAO dao;

	/**
	 * builds dao if it isn't created or return the actual DAO
	 * @return the actual DAO
	 */
	public static DAO getInstance() {
		if (dao == null) {
			dao = new DAO();
		}
		return dao;
	}
	
	public DAO () {
		fillDbProperties();
		loadDriver();
	}
	
	/**
	 * Returns a new connection to the database
	 * @return a new connection to the database
	 * @throws DatabaseException 
	 */
	public Connection getConnection() throws DatabaseException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(database, dbuser, dbpassword);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		}
		return connection;
	}
	
	/**
	 * Load of the driver JDBC for MySQL
	 */
	public void loadDriver() {
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * Fill in the database identifiers database, dbuser and dbpassword
	 */
	public void fillDbProperties() {
		Properties properties = new Properties();

		try (InputStream input = new FileInputStream("config.properties")) {
			properties.load(input);
			database = properties.getProperty("database");
			dbuser = properties.getProperty("dbuser");
			dbpassword = properties.getProperty("dbpassword");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
