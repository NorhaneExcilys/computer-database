package service;

import java.util.List;
import java.util.Optional;

import exception.DatabaseException;
import exception.UnknowCompanyException;
import exception.UnknowComputerException;
import model.Computer;
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
	
	public int getCount() throws DatabaseException {
		return computerDAO.getCount();
	}
	
	public List<Computer> getComputers() throws DatabaseException, UnknowCompanyException {
		return computerDAO.getAll();
	}
	
	public List<Computer> searchOnName(String name) throws DatabaseException, UnknowCompanyException {
		return computerDAO.searchOnName(name);
	}
	
	public List<Computer> getByPage(int computerNumber, int pageNumber) throws DatabaseException, UnknowCompanyException {
		return computerDAO.getByPage(computerNumber, pageNumber);
	}
	
	public Optional<Computer> getById(long id) throws DatabaseException, UnknowComputerException, UnknowCompanyException {
		return computerDAO.getById(id);
	}
	
	public boolean updateComputerById(Computer computer) throws DatabaseException {
		return computerDAO.updateComputerById(computer);
	}
	
	public boolean deleteComputerById(int id) throws DatabaseException, UnknowComputerException {
		return computerDAO.deleteComputerById(id);
	}
	
	public boolean addComputer(Computer computer) throws DatabaseException {
		return computerDAO.addComputer(computer);
	}
	
}
