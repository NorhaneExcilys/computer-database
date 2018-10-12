package persistance;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import exception.DatabaseException;

/**
 * <b>ConnectionDAO is the class that enable a connection to a database.</b>
 * A ConnectionDAO is characterized by the following informations:
 * an hikariConfig
 * an hikariDataSource
 * a connectionDAO
 * @author elgharbi
 *
 */

public class ConnectionDAO {
	
	private HikariConfig hikariConfig;
	private HikariDataSource hikariDataSource;
	
	private static ConnectionDAO connectionDAO;

	/**
	 * builds connectionDAO if it isn't created or return the actual connectionDAO
	 * @return the actual connectionDAO
	 */
	public static ConnectionDAO getInstance() {
		if (connectionDAO == null) {
			connectionDAO = new ConnectionDAO();
		}
		return connectionDAO;
	}
	
	public ConnectionDAO () {
		hikariConfig = new HikariConfig("/home/elgharbi/eclipse-workspace/ComputerDatabase/datasource.properties");
	    hikariDataSource = new HikariDataSource(hikariConfig);
	}
	
	public Connection getConnection() throws DatabaseException {
		try {
			return hikariDataSource.getConnection();
		} catch (SQLException e) {
			throw new DatabaseException("Impossible to connect to the database" + e.getMessage());
		}
	}
	
}