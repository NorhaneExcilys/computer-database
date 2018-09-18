package persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
		loadDriver();
	}
	
	public Connection getConnection() {
		String url = "jdbc:mysql://localhost:3306/computer-database-db";
		String user = "admincdb";
		String password = "qwerty1234";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, user, password);
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
