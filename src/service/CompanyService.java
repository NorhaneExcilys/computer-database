package service;

import java.util.List;
import java.util.Optional;

import exception.DatabaseException;
import exception.UnknowCompanyException;
import model.Company;
import persistance.CompanyDAO;

public class CompanyService {
	
	private CompanyDAO companyDAO;
	
	private static CompanyService companyService;
	
	public CompanyService () {
		this.companyDAO = CompanyDAO.getInstance();
	}
	
	public static CompanyService getInstance() {
		if (companyService == null) {
			companyService = new CompanyService();
		}
		return companyService;
	}
	
	public List<Company> getCompanies() throws DatabaseException {
		return companyDAO.getAll();
	}
	
	public Optional<Company> getCompanyById(long id) throws DatabaseException, UnknowCompanyException {
		return companyDAO.getById(id);
	}
}
