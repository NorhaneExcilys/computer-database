package service;

import java.util.List;

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
	
	public List<Company> getCompanies() {
		return companyDAO.getCompanies();
	}
	
	public boolean isCorrectId(long id) {
		return companyDAO.isCorrectId(id);
	}
	
	public Company getCompanyById(long id) {
		return companyDAO.getCompanyById(id);
	}
}
