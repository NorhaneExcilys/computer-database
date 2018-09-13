package service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Company;

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
	
	/**
	 * contains the singleton companyService
	 */
	private static CompanyService _companyService;
	
	/**
	 * contains the databaseService
	 */
	public DatabaseService _databaseService;
	
	/**
	 * builds CompanyService defined by databaseService
	 * @param databaseService the databaseService
	 */
	private CompanyService(DatabaseService databaseService) {
		_databaseService = databaseService;
	}

	/**
	 * builds CompanyService if it isn't created or return the actual companyService
	 * @param databaseService the databaseService
	 * @return the actual companyService
	 */
	public static CompanyService getInstance(DatabaseService databaseService) {
		if (_companyService == null) {
			return new CompanyService(databaseService);
		}
		return _companyService;
	}
	
	/**
	 * return the list of all companies in the database company
	 * @return the list of all companies
	 */
	public List<Company> getCompanies() {
		ResultSet quertyResult = _databaseService.executeQuery("SELECT * FROM company;");
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
	 * Return true if the id of the company is correct and false if not
	 * @param id the identifier of the company
	 * @return true if the identifier of the company is correct and false if not
	 */
	public boolean isCorrectId(int id) {
		ResultSet quertyResult = _databaseService.executeQuery("SELECT * FROM company WHERE id = " + id + ";");
		try {
			if (!quertyResult.next()) {
				return false;
			}
		} catch (SQLException e) {

		}
		return true;
	}

}
