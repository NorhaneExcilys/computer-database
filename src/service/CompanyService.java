package service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Company;

public class CompanyService {
	
	private static CompanyService _companyService;
	
	public DatabaseService _databaseService;
	
	private CompanyService(DatabaseService databaseService) {
		_databaseService = databaseService;
	}

	public static CompanyService getInstance(DatabaseService databaseService) {
		if (_companyService == null) {
			return new CompanyService(databaseService);
		}
		return _companyService;
	}
	
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
