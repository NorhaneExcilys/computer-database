package service;

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
 * <b>CompanyService is the class that enables to performs action on the database computer.</b>
 * A CompanyService is characterized by the following informations:
 * <ul>
 * <li> A companyService</li>
 * <li> A databaseService</li>
 * </ul>
 * @author elgharbi
 *
 */


public class CompanyService {
	
	private final static String GET_ALL = "SELECT id, name FROM company;";
	private final static String GET_BY_ID = "SELECT id, name FROM company WHERE id = ?;";
	
	
	/**
	 * contains the singleton companyService
	 */
	private static CompanyService companyService;
	
	/**
	 * contains the databaseService
	 */
	public DatabaseService databaseService;
	
	/**
	 * builds CompanyService defined by databaseService
	 * @param databaseService the databaseService
	 */
	private CompanyService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	/**
	 * builds CompanyService if it isn't created or return the actual companyService
	 * @param databaseService the databaseService
	 * @return the actual companyService
	 */
	public static CompanyService getInstance(DatabaseService databaseService) {
		if (companyService == null) {
			return new CompanyService(databaseService);
		}
		return companyService;
	}
	
	/**
	 * return the list of all companies in the database company
	 * @return the list of all companies
	 */
	public List<Company> getCompanies() {
		ResultSet quertyResult = databaseService.executeQuery(GET_ALL);
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
			PreparedStatement preparedStatement = databaseService.getConnection().prepareStatement(GET_BY_ID);
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
			preparedStatement = databaseService.getConnection().prepareStatement(GET_BY_ID);
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
