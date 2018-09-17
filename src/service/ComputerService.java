package service;

import java.util.List;

import model.Computer;
import persistance.CompanyDAO;
import persistance.ComputerDAO;
import persistance.DAO;

public class ComputerService {

	public DAO dao;
	public CompanyDAO companyDAO;
	public ComputerDAO computerDAO;
	
	private static ComputerService computerService;
	
	public ComputerService () {
		this.dao = DAO.getInstance();
		this.companyDAO = CompanyDAO.getInstance(dao);
		this.computerDAO = ComputerDAO.getInstance(dao, companyDAO);
	}
	
	public static ComputerService getInstance() {
		if (computerService == null) {
			return new ComputerService();
		}
		return computerService;
	}
	
	public List<Computer> getComputers() {
		return computerDAO.getComputers();
	}
	
	public Computer getComputerById(long id) {
		return computerDAO.getComputerById(id);
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
