package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	 * builds CompanyDAO defined by dao
	 * @param dao the dao
	 */
	private CompanyDAO() {
		this.dao = DAO.getInstance();
	}

	/**
	 * builds companyDAO if it isn't created or return the actual companyDAO
	 * @param dao the dao
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
	 */
	public List<Company> getAll() {
		
		// TODO Return optional
		
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
		}
		
		return allCompanies;
	}
	
	/**
	 * return the company chosen by identifier
	 * @param id the identifier of the company
	 * @return the company chosen by identifier
	 */
	public Company getById(long id) {
		
		// TODO Return optional
		
		Company company = null;
		
		try (Connection connection = dao.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID);
			preparedStatement.setLong(1, id); 
			ResultSet queryResult = preparedStatement.executeQuery();
			if(queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				company = new Company(currentId, currentName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return company;
	}
	
	/**
	 * Return true if the id of the company is correct and false if not
	 * @param id the identifier of the company
	 * @return true if the identifier of the company is correct and false if not
	 */
	public boolean isCorrectId(long id) {
		
		// TODO Return optional
		
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
