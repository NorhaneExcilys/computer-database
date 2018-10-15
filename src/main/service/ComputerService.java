package service;

import java.util.List;
import java.util.Optional;

import exception.DatabaseException;
import exception.UnknowCompanyException;
import exception.UnknowComputerException;
import model.Computer;
import model.Paging;
import persistance.ComputerDAO;

public class ComputerService {

	private ComputerDAO computerDAO;
	
	private static ComputerService computerService;
	
	public ComputerService () {
		this.computerDAO = ComputerDAO.getInstance();
	}
	
	public static ComputerService getInstance() {
		if (computerService == null) {
			computerService = new ComputerService();
		}
		return computerService;
	}
	
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