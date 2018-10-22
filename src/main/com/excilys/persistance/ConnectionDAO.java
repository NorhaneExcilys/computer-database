package com.excilys.persistance;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import com.excilys.exception.DatabaseException;

/**
 * <b>ConnectionDAO is the class that enable a connection to a database.</b>
 * A ConnectionDAO is characterized by the following informations:
 * an hikariConfig
 * an hikariDataSource
 * @author elgharbi
 *
 */

@Repository
public class ConnectionDAO {
	
	private HikariConfig hikariConfig;
	private HikariDataSource hikariDataSource;
	
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