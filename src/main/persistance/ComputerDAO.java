package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import exception.DatabaseException;
import exception.UnknowCompanyException;
import exception.UnknowComputerException;
import model.Company;
import model.Computer;
import model.Paging;

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
	private final static String GET_COUNT_BY_SEARCHED_WORD = "SELECT COUNT(id) AS count FROM computer WHERE name LIKE ?;";	
	//private final static String GET_ALL = "SELECT id, name, introduced, discontinued, company_id  FROM computer;";
	private final static String GET_BY_PAGE = "SELECT id, name, introduced, discontinued, company_id FROM computer LIMIT ? OFFSET ?;";
	private final static String GET_BY_ID = "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE id = ?;";
	private final static String GET_BY_SEARCHED_WORD = "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE name LIKE ?;";
	private final static String ADD = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUE (?, ?, ?, ?);";
	private final static String UPDATE = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;";
	private final static String DELETE_BY_LIST = "DELETE FROM computer WHERE id IN (%s)";
	private final static String DELETE_BY_COMPANY_ID = "DELETE FROM computer WHERE company_id = ?";
	
	private static ComputerDAO computerDAO;
	private CompanyDAO companyDAO;
	private ConnectionDAO connectionDAO;
	
	private ComputerDAO() {
		this.connectionDAO = ConnectionDAO.getInstance();
		this.companyDAO = CompanyDAO.getInstance();
	}
	
	/**
	 * builds ComputerDAO if it isn't created or return the actual computerDAO
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
		try (Connection connection = connectionDAO.getConnection()) {
			ResultSet queryResult = connection.createStatement().executeQuery(GET_COUNT);
			while (queryResult.next()) {
				computerNumber = queryResult.getInt("count");
			}
		} catch (SQLException e) {
			throw new DatabaseException("Impossible to count the number of computer" + e.getMessage());
		}
		return computerNumber;
	}
	
	/**
	 * return the number of computer in the database computer filter by name
	 * @param word the filter of name
	 * @return the number of computer in the database computer filter by name
	 * @throws DatabaseException
	 */
	public int getCountBySearchedWord(String word) throws DatabaseException {
		int computerNumber = -1;
		try (Connection connection = connectionDAO.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(GET_COUNT_BY_SEARCHED_WORD);
			preparedStatement.setString(1, "%" + word + "%");
			ResultSet queryResult = preparedStatement.executeQuery();
			while (queryResult.next()) {
				computerNumber = queryResult.getInt("count");
			}
		} catch (SQLException e) {
			throw new DatabaseException("Impossible to count the number of computer by filter name" + e.getMessage());
		}
		return computerNumber;
	}
	
	/**
	 * return the list of computer for a given page
	 * @param paging the actual paging
	 * @return the list of the computer for a given page
	 * @throws DatabaseException
	 * @throws UnknowCompanyException
	 */
	public List<Computer> getByPage(Paging paging) throws DatabaseException, UnknowCompanyException {
		List<Computer> computers = new ArrayList<Computer>();
		try (Connection connection = connectionDAO.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_PAGE);
			preparedStatement.setLong(1, paging.getComputersPerPage());
			preparedStatement.setInt(2, paging.getComputersPerPage() * (paging.getCurrentPage()-1));
			ResultSet queryResult = preparedStatement.executeQuery();
			while (queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				Optional<LocalDate> introducedDate = sqlDateToLocalDate(queryResult, "introduced");
				Optional<LocalDate> discontinuedDate = sqlDateToLocalDate(queryResult, "discontinued");
				long currentCompanyId = queryResult.getLong("company_id");
				Computer currentComputer = new Computer.ComputerBuilder(currentName).id(currentId).introducedDate(introducedDate).discontinuedDate(discontinuedDate).company(currentCompanyId > 0 ? companyDAO.getById(currentCompanyId) : Optional.empty()).build();
				computers.add(currentComputer);
			}
		} catch (SQLException e) {
			throw new DatabaseException("Impossible to get computers by page" + e.getMessage());
		}
		return computers;
	}
	
	/**
	 * return the list of all computers in the database computer
	 * @return the list of all computers
	 * @throws DatabaseException 
	 * @throws UnknowCompanyException 
	 */
	/*public List<Computer> getAll() throws DatabaseException, UnknowCompanyException {
		List<Computer> allComputers = new ArrayList<Computer>();
		
		try (Connection connection = connectionDAO.getConnection()) {
			ResultSet queryResult = connection.createStatement().executeQuery(GET_ALL);
			while (queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				Optional<LocalDate> introducedDate = Optional.ofNullable(queryResult.getDate("introduced").toLocalDate());
				Optional<LocalDate> discontinuedDate = Optional.ofNullable(queryResult.getDate("discontinued").toLocalDate());
				long currentCompanyId = queryResult.getLong("company_id");
				Computer currentComputer = new Computer.ComputerBuilder(currentName).id(currentId).introducedDate(introducedDate).discontinuedDate(discontinuedDate).company(currentCompanyId > 0 ? companyDAO.getById(currentCompanyId) : Optional.empty()).build();
				allComputers.add(currentComputer);
			}
		} catch (SQLException e) {
			throw new DatabaseException("Impossible to get computers" + e.getMessage());
		}

		return allComputers;
	}*/
	
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
		
		try (Connection connection = connectionDAO.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID);
			preparedStatement.setLong(1, id);
			ResultSet queryResult = preparedStatement.executeQuery();
			if(queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				Optional<LocalDate> introducedDate = sqlDateToLocalDate(queryResult, "introduced");
				Optional<LocalDate> discontinuedDate = sqlDateToLocalDate(queryResult, "discontinued");
				int currentCompanyId = queryResult.getInt("company_id");
				computer = Optional.of(new Computer.ComputerBuilder(currentName).id(currentId).introducedDate(introducedDate).discontinuedDate(discontinuedDate).company(currentCompanyId > 0 ? companyDAO.getById(currentCompanyId) : Optional.empty()).build());
			}
			else {
				throw new UnknowComputerException();
			}
		} catch (SQLException e) {
			throw new DatabaseException("Impossible to get computer by id" + e.getMessage());
		}
		
		return computer;
	}
	
	/**
	 * return the list of computer filter by name
	 * @param word the filter of name
	 * @return the list of computer filter by name
	 * @throws DatabaseException
	 * @throws UnknowCompanyException
	 */
	public List<Computer> getBySearchedWord(String word) throws DatabaseException, UnknowCompanyException {
		List<Computer> searchedComputers = new ArrayList<Computer>();
		
		try (Connection connection = connectionDAO.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_SEARCHED_WORD);
			preparedStatement.setString(1, "%" + word + "%");
			ResultSet queryResult = preparedStatement.executeQuery();
			while (queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				Optional<LocalDate> introducedDate = sqlDateToLocalDate(queryResult, "introduced");
				Optional<LocalDate> discontinuedDate = sqlDateToLocalDate(queryResult, "discontinued");
				int currentCompanyId = queryResult.getInt("company_id");
				searchedComputers.add(new Computer.ComputerBuilder(currentName).id(currentId).introducedDate(introducedDate).discontinuedDate(discontinuedDate).company(currentCompanyId > 0 ? companyDAO.getById(currentCompanyId) : Optional.empty()).build());
			}
		} catch (SQLException e) {
			throw new DatabaseException("Impossible to get computers by searchedWord" + e.getMessage());
		}
		
		return searchedComputers;
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
			Connection connection = connectionDAO.getConnection();
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
			throw new DatabaseException("Impossible to add computer" + e.getMessage());
		}
		return (queryResult == 1);
	}
	
	/**
	 * Deletes computers to the database
	 * @param idList the list of the id of the computers to delete separated by ","
	 * @return true if the computers are deleted and false if not
	 * @throws DatabaseException 
	 * @throws UnknowComputerException 
	 */
	public boolean deleteComputerByList(String idList) throws DatabaseException, UnknowComputerException {
		int queryResult = -1;
		try (Connection connection = connectionDAO.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(String.format(DELETE_BY_LIST, idList));
			queryResult = preparedStatement.executeUpdate();
			if (queryResult < 1) {
				throw new UnknowComputerException();
			}
		} catch (SQLException e) {
			throw new DatabaseException("Impossible to delete computers" + e.getMessage());
		}
		return (queryResult == 1);
	}
	
	public void deleteComputerByCompanyId(Connection connection, long idCompany) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_COMPANY_ID);
		preparedStatement.setLong(1, idCompany);
		preparedStatement.executeUpdate();
	}
	
	/**
	 * Updates a computer to the database
	 * @param computer the updated computer
	 * @return true if the computer is updated and false if not
	 * @throws DatabaseException 
	 */
	public boolean updateComputerById(Computer computer) throws DatabaseException, UnknowComputerException {
		long id = computer.getId();
		String name = computer.getName();
		Optional<LocalDate> introducedDate = computer.getIntroducedDate();
		Optional<LocalDate> discontinuedDate = computer.getDiscontinuedDate();
		Optional<Company> company = computer.getCompany();
		int queryResult = -1;
		try (Connection connection = connectionDAO.getConnection()) {
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
			if (queryResult < 1) {
				throw new UnknowComputerException();
			}
		} catch (SQLException e) {
			throw new DatabaseException("Impossible to update computer" + e.getMessage());
		}
		return (queryResult == 1);
	}
	
	/**
	 * convert a sqlDate to an Optional<LocalDate> from a queryResult
	 * @param queryResult the queryResult
	 * @param field the field which contains the sqlDate
	 * @return the sqlDate converts
	 * @throws DatabaseException 
	 */
	private Optional<LocalDate> sqlDateToLocalDate(ResultSet queryResult, String field) throws DatabaseException {
		try {
			return queryResult.getDate(field) == null ? Optional.empty() : Optional.of(queryResult.getDate(field).toLocalDate());
		} catch (SQLException e) {
			throw new DatabaseException("\"Impossible to convert the sqlDate to LocalDate" + e.getMessage());
		}
	}
	
}