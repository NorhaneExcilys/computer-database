package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import exception.DatabaseException;
import exception.UnknowCompanyException;
import model.Company;

/**
 * <b>CompanyDAO is the class that enables to performs action on the database computer.</b>
 * A CompanyDAO is characterized by the following informations:
 * <ul>
 * <li> A companyDAO</li>
 * <li> A dao</li>
 * </ul>
 * @author elgharbi
 *
 */
public class CompanyDAO {

	private final static String GET_ALL = "SELECT id, name FROM company;";
	private final static String GET_BY_ID = "SELECT id, name FROM company WHERE id = ?;";
	
	private static CompanyDAO companyDAO;
	private DAO dao;
	
	/**
	 * builds CompanyDAO
	 */
	private CompanyDAO() {
		this.dao = DAO.getInstance();
	}

	/**
	 * builds companyDAO if it isn't created or return the actual companyDAO
	 * @return the actual companyDAO
	 */
	public static CompanyDAO getInstance() {
		if (companyDAO == null) {
			companyDAO = new CompanyDAO();
		}
		return companyDAO;
	}
	
	/**
	 * return the list of all companies in the database company
	 * @return the list of all companies
	 * @throws DatabaseException 
	 */
	public List<Company> getAll() throws DatabaseException {
		List<Company> allCompanies = new ArrayList<Company>();
		
		try (Connection connection = dao.getConnection()) {
			ResultSet queryResult = connection.createStatement().executeQuery(GET_ALL);
			while (queryResult.next()) {
				long currentId = queryResult.getLong("id");
				String currentName = queryResult.getString("name");
				Company currentCompany = new Company(currentId, currentName);
				allCompanies.add(currentCompany);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		}
		
		return allCompanies;
	}
	
	/**
	 * return the company chosen by identifier
	 * @param id the identifier of the company
	 * @return the company chosen by identifier
	 * @throws DatabaseException 
	 * @throws UnknowCompanyException 
	 */
	public Optional<Company> getById(long id) throws DatabaseException, UnknowCompanyException {
		Optional<Company> company = Optional.empty();
		
		try (Connection connection = dao.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID);
			preparedStatement.setLong(1, id); 
			ResultSet queryResult = preparedStatement.executeQuery();
			if(queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				company = Optional.of(new Company(currentId, currentName));
			}
			else {
				throw new UnknowCompanyException();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		}

		return company;
	}
	
}
