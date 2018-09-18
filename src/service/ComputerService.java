package service;

import java.util.List;

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
	
	public List<Computer> getComputers() {
		return computerDAO.getAll();
	}
	
	public Computer getComputerById(long id) {
		return computerDAO.getById(id);
	}

	public boolean isCorrectId(long id) {
		return computerDAO.isCorrectId(id);
	}
	
	public int updateComputerById(Computer computer) {
		return computerDAO.updateComputerById(computer);
	}
	
	public int deleteComputerById(int id) {
		return computerDAO.deleteComputerById(id);
	}
	
	public int addComputer(Computer computer) {
		return computerDAO.addComputer(computer);
	}
	
}
