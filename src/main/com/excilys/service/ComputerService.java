package com.excilys.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.exception.UnknowComputerException;
import com.excilys.model.Computer;
import com.excilys.model.Paging;

import com.excilys.exception.DatabaseException;
import com.excilys.exception.UnknowCompanyException;
import com.excilys.persistance.ComputerDAO;

@Service
public class ComputerService {

	@Autowired
	private ComputerDAO computerDAO;
	
	public int getCount(Optional<String> word) throws DatabaseException {
		return word.isPresent() ? computerDAO.getCountBySearchedWord(word.get()) : computerDAO.getCount();
	}
	
	public List<Computer> getByPage(Paging paging) throws DatabaseException, UnknowCompanyException {
		return computerDAO.getByPage(paging);
	}
	
	public Optional<Computer> getById(long id) throws DatabaseException, UnknowComputerException, UnknowCompanyException {
		return computerDAO.getById(id);
	}
	
	public List<Computer> getBySearchedWord(String word) throws DatabaseException, UnknowCompanyException {
		return computerDAO.getBySearchedWord(word);
	}
	
	public boolean updateComputerById(Computer computer) throws DatabaseException, UnknowComputerException {
		return computerDAO.updateComputerById(computer);
	}
	
	public boolean deleteComputerByList(String idList) throws DatabaseException, UnknowComputerException {
		return computerDAO.deleteComputerByList(idList);
	}
	
	public boolean addComputer(Computer computer) throws DatabaseException {
		return computerDAO.addComputer(computer);
	}
	
}