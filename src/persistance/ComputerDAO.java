package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;



import model.Company;
import model.Computer;

/**
 * <b>DAOComputer is the class that enables to performs action on the database computer.</b>
 * A DAOComputer is characterized by the following informations:
 * <ul>
 * <li> A daoComputer</li>
 * <li> A dao</li>
 * </ul>
 * @author elgharbi
 *
 */
public class ComputerDAO {
	
	private final static String GET_ALL = "SELECT id, name, introduced, discontinued, company_id  FROM computer;";
	private final static String GET_BY_ID = "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE id = ?;";
	private final static String ADD = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUE (?, ?, ?, ?);";
	private final static String UPDATE = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;";
	private final static String DELETE = "DELETE FROM computer WHERE id = ?";
	

	private static ComputerDAO computerDAO;

	private CompanyDAO companyDAO;
	private DAO dao;
	
	/**
	 * builds DAOComputer defined by dao
	 * @param dao the dao
	 * @param daoCompany the daoCompany
	 */
	private ComputerDAO() {
		this.dao = DAO.getInstance();
		this.companyDAO = CompanyDAO.getInstance();
	}
	
	/**
	 * builds DAOComputer if it isn't created or return the actual daoComputer
	 * @param dao the dao
	 * @param daoCompany the companyDAO
	 * @return the actual computerDAO
	 */
	public static ComputerDAO getInstance() {
		if (computerDAO == null) {
			computerDAO = new ComputerDAO();
		}
		return computerDAO;
	}
	
	/**
	 * return the list of all computers in the database computer
	 * @return the list of all computers
	 */
	public List<Computer> getAll() {
		List<Computer> allComputers = new ArrayList<Computer>();
		
		try (Connection connection = dao.getConnection()) {
			ResultSet queryResult = connection.createStatement().executeQuery(GET_ALL);
			while (queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				LocalDate introducedDate = stringToLocalDate(queryResult.getString("introduced"));
				LocalDate discontinuedDate = stringToLocalDate(queryResult.getString("discontinued"));
				long currentCompanyId = queryResult.getLong("company_id");
				Computer currentComputer = new Computer(currentId, currentName, introducedDate, discontinuedDate, companyDAO.getById(currentCompanyId));
				allComputers.add(currentComputer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return allComputers;
	}
	
	
	public LocalDate stringToLocalDate(String strDate) {
		// TODO Si la date est Null
		
		LocalDate localDate = null;
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
		if (strDate != null) {
			try {
				localDate = LocalDate.parse(strDate, formatter);
			}
			catch (DateTimeParseException ex) {
				ex.printStackTrace();
			}
		}
		
		return localDate;
	}
	
	
	/**
	 * return the computer chosen by identifier
	 * @param id the identifier of the computer
	 * @return the computer chosen by identifier
	 */
	public Computer getById(long id) {
		
		// TODO Return optional
		
		Computer computer = null;
		
		try (Connection connection = dao.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID);
			preparedStatement.setLong(1, id);
			
			ResultSet queryResult = preparedStatement.executeQuery();
			
			if(queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				LocalDate introducedDate = stringToLocalDate(queryResult.getString("introduced"));
				LocalDate discontinuedDate = stringToLocalDate(queryResult.getString("discontinued"));
				int currentCompanyId = queryResult.getInt("company_id");
				computer = new Computer(currentId, currentName, introducedDate, discontinuedDate, companyDAO.getById(currentCompanyId));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return computer;
	}

	/**
	 * Adds a computer to the database
	 * @param computer the added computer
	 * @return 1 if the computer is added and 0 if not
	 */
	public int addComputer(Computer computer) {
		String name = computer.getName();
		Company company = computer.getCompany();
		
		// Introduced Date
		LocalDate introducedDate = computer.getIntroducedDate();
		java.sql.Date sqlIntroducedDate = null;
		if (introducedDate != null) {
			sqlIntroducedDate = java.sql.Date.valueOf(introducedDate);
		}
		
		// Discontinued Date
		LocalDate discontinuedDate = computer.getDiscontinuedDate();
		java.sql.Date sqlDiscontinuedDate = null;
		if (discontinuedDate != null) {
			sqlDiscontinuedDate = java.sql.Date.valueOf(discontinuedDate);
		}
		
		int queryResult = -1;
		try {
			Connection connection = dao.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(ADD);

			preparedStatement.setString(1, name);
			preparedStatement.setDate(2, sqlIntroducedDate);
			preparedStatement.setDate(3, sqlDiscontinuedDate);
			if (company == null) {
				preparedStatement.setNull(4, java.sql.Types.NUMERIC);
			}
			else {
				preparedStatement.setLong(4, company == null ? null : company.getId());
			}
			
			queryResult = preparedStatement.executeUpdate();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return queryResult;
	}
	
	/**
	 * Updates a computer to the database
	 * @param computer the updated computer
	 * @return 1 if the computer is updated and 0 if not
	 */
	public int updateComputerById(Computer computer) {
		long computerId = computer.getId();
		Company company = computer.getCompany();
		String computerName = computer.getName();
		int queryResult = -1;
		
		// Introduced Date
		LocalDate introducedDate = computer.getIntroducedDate();
		java.sql.Date sqlIntroducedDate = null;
		if (introducedDate != null) {
			sqlIntroducedDate = java.sql.Date.valueOf(introducedDate);
		}
		
		// Discontinued Date
		LocalDate discontinuedDate = computer.getDiscontinuedDate();
		java.sql.Date sqlDiscontinuedDate = null;
		if (discontinuedDate != null) {
			sqlDiscontinuedDate = java.sql.Date.valueOf(discontinuedDate);
		}
		
		try (Connection connection = dao.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
			preparedStatement.setString(1, computerName);
			preparedStatement.setDate(2, sqlIntroducedDate);
			preparedStatement.setDate(3, sqlDiscontinuedDate);
			if (company == null) {
				preparedStatement.setNull(4, java.sql.Types.NUMERIC);
			}
			else {
				preparedStatement.setLong(4, company == null ? null : company.getId());
			}
			preparedStatement.setLong(5, computerId);
			queryResult = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return queryResult;
	}
	
	/**
	 * Deletes a computer to the database
	 * @param id the id of the computer to delete
	 * @return 1 if the computer is deleted and 0 if not
	 */
	public int deleteComputerById(int id) {
		// TODO Bien gérer le retour
		int queryResult = -1;
		
		try (Connection connection = dao.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
			preparedStatement.setLong(1, id);
			queryResult = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return queryResult;
	}
	
	/**
	 * Return true if the id of the computer is correct and false if not
	 * @param id the identifier of the computer
	 * @return true if the identifier of the computer is correct and false if not
	 */
	public boolean isCorrectId(long id) {
		// TODO Attention ca fonctionne pas, on renvoit quoi ? 
		
		try (Connection connection = dao.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID);
			preparedStatement.setLong(1, id);
			ResultSet queryResult = preparedStatement.executeQuery();
			if (!queryResult.next()) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
}
