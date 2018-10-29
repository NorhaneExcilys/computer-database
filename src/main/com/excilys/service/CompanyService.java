package com.excilys.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.exception.DatabaseException;
import com.excilys.model.Company;
import com.excilys.persistance.CompanyDAO;

@Service
public class CompanyService {
	
	@Autowired
	private CompanyDAO companyDAO;
	
	public List<Company> getCompanies() throws DatabaseException {
		return companyDAO.getAll();
	}
	
	public Optional<Company> getCompanyById(long id) throws DatabaseException {
		return companyDAO.getById(id);
	}
	
	public boolean deleteCompanyById(long id) throws DatabaseException {
		return companyDAO.deleteById(id);
	}

}