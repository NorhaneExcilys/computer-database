package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseService {

	private static DatabaseService _databaseService;
	
	private Connection connexion;
	
	private DatabaseService() {
		connexion = null;
	}

	public static DatabaseService getInstance() {
		if (_databaseService == null) {
			return new DatabaseService();
		}
		return _databaseService;
	}
	
	/**
	 * Loading of the driver JDBC for MySQL
	 */
	public void loadDriver() {
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		} catch ( ClassNotFoundException e ) {
		    
		}
	}
	
	/* Connexion à la base de données computer-database-db */
	public void connectDatabase() {
		String url = "jdbc:mysql://localhost:3306/computer-database-db";
		String user = "admincdb";
		String password = "qwerty1234";
		try {
			connexion = DriverManager.getConnection(url, user, password);
			/* Ici, nous placerons nos requêtes vers la BDD */
		} catch ( SQLException e ) {
		    /* Gérer les éventuelles erreurs ici */
		}
	}
	
	/* Connexion à la base de données */
	public void connectDatabase(String url, String user, String password) {
		try {
			connexion = DriverManager.getConnection(url, user, password);
			/* Ici, nous placerons nos requêtes vers la BDD */
		} catch ( SQLException e ) {
		    /* Gérer les éventuelles erreurs ici */
		}
	}
	
	public ResultSet executeQuery(String query) {
		ResultSet queryResult = null;
		try {
			/* Création de l'objet gérant les requêtes */
			Statement statement = connexion.createStatement();
			/* Exécution d'une requête de lecture */
			queryResult = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return queryResult;
	}
	
	public int executeUpdate(String query) {
		int queryResult = -1;
		try {
			/* Création de l'objet gérant les requêtes */
			Statement statement = connexion.createStatement();
			/* Exécution d'une requête de lecture */
			queryResult = statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return queryResult;
	}
	
	/* Fermeture de la connexion */
	@Override
	protected void finalize() {
		if (connexion != null) {
	        try {
	            connexion.close();
	        } catch (SQLException ignore) {
	            /* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
	        }
	    }
	}
}
