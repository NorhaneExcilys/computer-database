package service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Computer;

public class ComputerService {

	private static ComputerService _computerService;
	
	public DatabaseService _databaseService;
	
	private ComputerService(DatabaseService databaseService) {
		_databaseService = databaseService;
	}
	
	public static ComputerService getInstance(DatabaseService databaseService) {
		if (_computerService == null) {
			return new ComputerService(databaseService);
		}
		return _computerService;
	}
	
	public List<Computer> getComputers() {
		ResultSet queryResult = _databaseService.executeQuery("SELECT * FROM computer;");
		List<Computer> computerList = new ArrayList<Computer>();
		
		try {
			while (queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				String currentIntroducedDate = queryResult.getString("introduced");
				String currentDiscontinuedDate = queryResult.getString("discontinued");
				int currentCompanyId = queryResult.getInt("company_id");
				Computer currentComputer = new Computer(currentId, currentName, null, null, currentCompanyId);
				//Computer currentComputer = new Computer(currentId, currentName, currentIntroducedDate, currentDiscontinuedDate, currentCompanyId);
				computerList.add(currentComputer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return computerList;
	}
	
	public Computer getComputerById(int id) {
		Computer currentComputer = null;
		ResultSet queryResult = _databaseService.executeQuery("SELECT * FROM computer WHERE id = " + id + ";");
		try {
			if(queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				String currentIntroducedDate = queryResult.getString("introduced");
				String currentDiscontinuedDate = queryResult.getString("discontinued");
				int currentCompanyId = queryResult.getInt("company_id");
				currentComputer = new Computer(currentId, currentName, null, null, currentCompanyId);
				//Computer currentComputer = new Computer(currentId, currentName, currentIntroducedDate, currentDiscontinuedDate, currentCompanyId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return currentComputer;
	}

	public int addComputer(Computer computer) {
		String name = computer.getName();
		int queryResult = _databaseService.executeUpdate("INSERT INTO computer (name, introduced, discontinued, company_id) VALUE (\'" + name + "\', null, null, null);");
		return queryResult;
	}
	
	public int updateComputerById(Computer computer) {
		int computerId = computer.getId();
		String computerName = computer.getName();
		
		// Introduced Date
		Date introducedDate = computer.getIntroducedDate();
		java.sql.Date sqlIntroducedDate = null;
		String computerIntroducedDate = null;
		if (introducedDate != null) {
			sqlIntroducedDate = new java.sql.Date(introducedDate.getTime());
			computerIntroducedDate = "'" + sqlIntroducedDate + "'";
		}
		else {
			computerIntroducedDate = "null";
		}
		
		// Discontinued Date
		Date discontinuedDate = computer.getDiscontinuedDate();
		java.sql.Date sqlDiscontinuedDate = null;
		String computerDiscontinuedDate = null;
		if (discontinuedDate != null) {
			sqlDiscontinuedDate = new java.sql.Date(discontinuedDate.getTime());
			computerDiscontinuedDate = "'" + sqlDiscontinuedDate + "'";
		}
		else {
			computerDiscontinuedDate = "null";
		}
		
		int computerCompanyId = computer.getCompanyId();
		
		System.out.println("UPDATE computer SET name = '" + computerName + "', introduced = " + sqlIntroducedDate + ", discontinued = " + sqlDiscontinuedDate + ", company_id = " + computerCompanyId + " WHERE id =" + computerId + ";");
		
		int queryResult = _databaseService.executeUpdate("UPDATE computer SET name = '" + computerName + "', introduced = " + computerIntroducedDate + ", discontinued = " + computerDiscontinuedDate + ", company_id = " + computerCompanyId + " WHERE id =" + computerId + ";");
		return queryResult;
	}
	
	public int deleteComputerById(int id) {
		int queryResult = _databaseService.executeUpdate("DELETE FROM computer WHERE id=" + id);
		return queryResult;
	}
	
	public int deleteComputerByName(String name) {
		int queryResult = _databaseService.executeUpdate("DELETE FROM computer WHERE name=\'" + name + "\'");
		return queryResult;
	}
	
	public boolean isCorrectId(int id) {
		ResultSet quertyResult = _databaseService.executeQuery("SELECT * FROM computer WHERE id = " + id + ";");
		try {
			if (!quertyResult.next()) {
				return false;
			}
		} catch (SQLException e) {

		}
		return true;
	}
	
}
