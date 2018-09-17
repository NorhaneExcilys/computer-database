package service;

import java.util.List;

import model.Company;
import persistance.CompanyDAO;
import persistance.DAO;

public class CompanyService {
	
	public DAO dao;
	public CompanyDAO companyDAO;
	
	private static CompanyService companyService;
	
	public CompanyService () {
		this.dao = DAO.getInstance();
		this.companyDAO = CompanyDAO.getInstance(dao);
	}
	
	public static CompanyService getInstance() {
		if (companyService == null) {
			return new CompanyService();
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
