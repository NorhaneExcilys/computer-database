package persistance;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * <b>DAO is the class that enable a connection to a database thanks to JDBC.</b>
 * A DAO is characterized by the following informations:
 * <ul>
 * <li> A connection</li>
 * </ul>
 * @author elgharbi
 *
 */

public class DAO {
	
	private static String database;
	private static String dbuser;
	private static String dbpassword;
	
	/**
	 * contains the singleton dao
	 */
	private static DAO dao;

	/**
	 * builds dao if it isn't created or return the actual databaseService
	 * @return the actual databaseService
	 */
	public static DAO getInstance() {
		if (dao == null) {
			dao = new DAO();
		}
		return dao;
	}
	
	public DAO () {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("config.properties");

			// load a properties file
			prop.load(input);

			// get the property value
			database = prop.getProperty("database");
			dbuser = prop.getProperty("dbuser");
			dbpassword = prop.getProperty("dbpassword");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		loadDriver();
	}
	
	public Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(database, dbuser, dbpassword);
		} catch (SQLException e) {

		}
		return connection;
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
	
}
