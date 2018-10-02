package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import exception.DatabaseException;
import exception.UnknowCompanyException;
import exception.UnknowComputerException;
import model.Company;
import model.Computer;

/**
 * <b>DAOComputer is the class that enables to performs action on the database computer.</b>
 * A DAOComputer is characterized by the following informations:
 * <ul>
 * <li> A daoCompany</li>
 * <li> A daoComputer</li>
 * <li> A dao</li>
 * </ul>
 * @author elgharbi
 *
 */
public class ComputerDAO {
	
	private final static String GET_COUNT = "SELECT COUNT(id) AS count FROM computer";
	private final static String GET_ALL = "SELECT id, name, introduced, discontinued, company_id  FROM computer;";
	private final static String GET_BY_PAGE = "SELECT id, name, introduced, discontinued, company_id FROM computer LIMIT ? OFFSET ?;";
	private final static String GET_BY_ID = "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE id = ?;";
	private final static String SEARCH_ON_NAME = "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE name LIKE '%?%'";
	private final static String ADD = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUE (?, ?, ?, ?);";
	private final static String UPDATE = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;";
	private final static String DELETE = "DELETE FROM computer WHERE id = ?";
	
	private static ComputerDAO computerDAO;

	private CompanyDAO companyDAO;
	private DAO dao;
	
	/**
	 * builds DAOComputer
	 */
	private ComputerDAO() {
		this.dao = DAO.getInstance();
		this.companyDAO = CompanyDAO.getInstance();
	}
	
	/**
	 * builds DAOComputer if it isn't created or return the actual daoComputer
	 * @return the actual computerDAO
	 */
	public static ComputerDAO getInstance() {
		if (computerDAO == null) {
			computerDAO = new ComputerDAO();
		}
		return computerDAO;
	}
	
	/**
	 * return the number of computer in the database computer
	 * @return the number of computer in the database computer
	 * @throws DatabaseException
	 */
	public int getCount() throws DatabaseException {
		int computerNumber = -1;
		try (Connection connection = dao.getConnection()) {
			ResultSet queryResult = connection.createStatement().executeQuery(GET_COUNT);
			while (queryResult.next()) {
				computerNumber = queryResult.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		}
		return computerNumber;
	}
	
	/**
	 * return the list of computer for a given page
	 * @param computerNumber the number of computer per page
	 * @param pageNumber the actual page
	 * @return the list of the computer for a given page
	 * @throws DatabaseException
	 * @throws UnknowCompanyException
	 */
	public List<Computer> getByPage(int computerNumber, int pageNumber) throws DatabaseException, UnknowCompanyException {
		List<Computer> computers = new ArrayList<Computer>();
		try (Connection connection = dao.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_PAGE);
			preparedStatement.setLong(1, computerNumber);
			preparedStatement.setInt(2, computerNumber * (pageNumber-1));
			ResultSet queryResult = preparedStatement.executeQuery();
			while (queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				Optional<LocalDate> introducedDate = stringToLocalDate(queryResult.getString("introduced"));
				Optional<LocalDate> discontinuedDate = stringToLocalDate(queryResult.getString("discontinued"));
				long currentCompanyId = queryResult.getLong("company_id");
				Computer currentComputer = new Computer(currentId, currentName, introducedDate, discontinuedDate, currentCompanyId > 0 ? companyDAO.getById(currentCompanyId) : Optional.empty());
				computers.add(currentComputer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		}
		return computers;
	}
	
	/**
	 * return the list of all computers in the database computer
	 * @return the list of all computers
	 * @throws DatabaseException 
	 * @throws UnknowCompanyException 
	 */
	public List<Computer> getAll() throws DatabaseException, UnknowCompanyException {
		List<Computer> allComputers = new ArrayList<Computer>();
		
		try (Connection connection = dao.getConnection()) {
			ResultSet queryResult = connection.createStatement().executeQuery(GET_ALL);
			while (queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				Optional<LocalDate> introducedDate = stringToLocalDate(queryResult.getString("introduced"));
				Optional<LocalDate> discontinuedDate = stringToLocalDate(queryResult.getString("discontinued"));
				long currentCompanyId = queryResult.getLong("company_id");
				Computer currentComputer = new Computer(currentId, currentName, introducedDate, discontinuedDate, currentCompanyId > 0 ? companyDAO.getById(currentCompanyId) : Optional.empty());
				allComputers.add(currentComputer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		}

		return allComputers;
	}
	
	/**
	 * return the computer chosen by identifier
	 * @param id the identifier of the computer
	 * @return the computer chosen by identifier
	 * @throws DatabaseException 
	 * @throws UnknowComputerException 
	 * @throws UnknowCompanyException 
	 */
	public Optional<Computer> getById(long id) throws DatabaseException, UnknowComputerException, UnknowCompanyException {
		Optional<Computer> computer = Optional.empty();
		
		try (Connection connection = dao.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID);
			preparedStatement.setLong(1, id);
			ResultSet queryResult = preparedStatement.executeQuery();
			if(queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				Optional<LocalDate> introducedDate = stringToLocalDate(queryResult.getString("introduced"));
				Optional<LocalDate> discontinuedDate = stringToLocalDate(queryResult.getString("discontinued"));
				int currentCompanyId = queryResult.getInt("company_id");
				computer = Optional.of(new Computer(currentId, currentName, introducedDate, discontinuedDate, currentCompanyId > 0 ? companyDAO.getById(currentCompanyId) : Optional.empty()));
			}
			else {
				throw new UnknowComputerException();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		}
		
		return computer;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws DatabaseException
	 * @throws UnknowCompanyException 
	 */
	public List<Computer> searchOnName(String name) throws DatabaseException, UnknowCompanyException {
		List<Computer> result = new ArrayList<Computer>();
		try (Connection connection = dao.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_ON_NAME);
			preparedStatement.setString(1, name);
			ResultSet queryResult = preparedStatement.executeQuery();
			while (queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				Optional<LocalDate> introducedDate = stringToLocalDate(queryResult.getString("introduced"));
				Optional<LocalDate> discontinuedDate = stringToLocalDate(queryResult.getString("discontinued"));
				long currentCompanyId = queryResult.getLong("company_id");
				Computer currentComputer = new Computer(currentId, currentName, introducedDate, discontinuedDate, currentCompanyId > 0 ? companyDAO.getById(currentCompanyId) : Optional.empty());
				result.add(currentComputer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}
	
	/**
	 * Adds a computer to the database
	 * @param computer the added computer
	 * @return true if the computer is added and false if not
	 * @throws DatabaseException 
	 */
	public boolean addComputer(Computer computer) throws DatabaseException {
		String name = computer.getName();
		Optional<LocalDate> introducedDate = computer.getIntroducedDate();
		Optional<LocalDate> discontinuedDate = computer.getDiscontinuedDate();
		Optional<Company> company = computer.getCompany();
		
		int queryResult = -1;
		try {
			Connection connection = dao.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(ADD);
			preparedStatement.setString(1, name);
			preparedStatement.setDate(2, introducedDate.isPresent() ? java.sql.Date.valueOf(introducedDate.get()) : null);
			preparedStatement.setDate(3, discontinuedDate.isPresent() ? java.sql.Date.valueOf(discontinuedDate.get()) : null);
			if (company.isPresent()) {
				preparedStatement.setLong(4, company.get().getId());
			}
			else {
				preparedStatement.setNull(4, java.sql.Types.NUMERIC);
			}
			queryResult = preparedStatement.executeUpdate();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		}
		return (queryResult == 1);
	}
	
	/**
	 * Deletes a computer to the database
	 * @param id the identifier of the computer to delete
	 * @return true if the computer is deleted and false if not
	 * @throws DatabaseException 
	 * @throws UnknowComputerException 
	 */
	public boolean deleteComputerById(long id) throws DatabaseException, UnknowComputerException {
		int queryResult = -1;
		try (Connection connection = dao.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
			preparedStatement.setLong(1, id);
			queryResult = preparedStatement.executeUpdate();
			if (queryResult < 1) {
				throw new UnknowComputerException();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (queryResult == 1);
	}
	
	/**
	 * Updates a computer to the database
	 * @param computer the updated computer
	 * @return true if the computer is updated and false if not
	 * @throws DatabaseException 
	 */
	public boolean updateComputerById(Computer computer) throws DatabaseException {
		long id = computer.getId();
		String name = computer.getName();
		Optional<LocalDate> introducedDate = computer.getIntroducedDate();
		Optional<LocalDate> discontinuedDate = computer.getDiscontinuedDate();
		Optional<Company> company = computer.getCompany();
		
		int queryResult = -1;
		try (Connection connection = dao.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
			
			preparedStatement.setString(1, name);
			preparedStatement.setDate(2, introducedDate.isPresent() ? java.sql.Date.valueOf(introducedDate.get()) : null);
			preparedStatement.setDate(3, discontinuedDate.isPresent() ? java.sql.Date.valueOf(discontinuedDate.get()) : null);
			if (company.isPresent()) {
				preparedStatement.setLong(4, company.get().getId());
			}
			else {
				preparedStatement.setNull(4, java.sql.Types.NUMERIC);
			}
			preparedStatement.setLong(5, id);
			queryResult = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		}
		
		return (queryResult == 1);
	}
	
	/**
	 * Converts a string to a LocalDate
	 * @param strDate the string to convert
	 * @return the LocalDate
	 */
	private Optional<LocalDate> stringToLocalDate(String strDate) {
		Optional<LocalDate> localDate = Optional.empty();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
		if (strDate != null) {
			localDate = Optional.of(LocalDate.parse(strDate, formatter));
		}
		return localDate;
	}
	
}