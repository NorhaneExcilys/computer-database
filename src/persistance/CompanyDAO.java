package persistance;

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
	
	
	/**
	 * contains the singleton companyDAO
	 */
	private static CompanyDAO companyDAO;
	
	/**
	 * contains the dao
	 */
	public DAO dao;
	
	/**
	 * builds CompanyDAO defined by dao
	 * @param dao the dao
	 */
	private CompanyDAO(DAO dao) {
		this.dao = dao;
	}

	/**
	 * builds companyDAO if it isn't created or return the actual companyDAO
	 * @param dao the dao
	 * @return the actual companyDAO
	 */
	public static CompanyDAO getInstance(DAO dao) {
		if (companyDAO == null) {
			return new CompanyDAO(dao);
		}
		return companyDAO;
	}
	
	/**
	 * return the list of all companies in the database company
	 * @return the list of all companies
	 */
	public List<Company> getCompanies() {
		ResultSet quertyResult = dao.executeQuery(GET_ALL);
		List<Company> companyList = new ArrayList<Company>();
		
		try {
			while (quertyResult.next()) {
				int currentId = quertyResult.getInt("id");
				String currentName = quertyResult.getString("name");
			    Company currentCompany = new Company(currentId, currentName);
			    companyList.add(currentCompany);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return companyList;
	}
	
	/**
	 * return the company chosen by identifier
	 * @param id the identifier of the company
	 * @return the company chosen by identifier
	 */
	public Company getCompanyById(long id) {
		Company currentCompany = null;
		try {
			PreparedStatement preparedStatement = dao.getConnection().prepareStatement(GET_BY_ID);
			preparedStatement.setLong(1, id); 
			ResultSet queryResult = preparedStatement.executeQuery();
			if(queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				currentCompany = new Company(currentId, currentName);
			}
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return currentCompany;
	}
	
	/**
	 * Return true if the id of the company is correct and false if not
	 * @param id the identifier of the company
	 * @return true if the identifier of the company is correct and false if not
	 */
	public boolean isCorrectId(long id) {
		PreparedStatement preparedStatement;
		try {
			preparedStatement = dao.getConnection().prepareStatement(GET_BY_ID);
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
