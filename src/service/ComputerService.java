package service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Company;
import model.Computer;

/**
 * <b>ComputerService is the class that enables to performs action on the database computer.</b>
 * A ComputerService is characterized by the following informations:
 * <ul>
 * <li> A computerService</li>
 * <li> A databaseService</li>
 * </ul>
 * @author elgharbi
 *
 */


public class ComputerService {

	/**
	 * contains the singleton computerService
	 */
	private static ComputerService computerService;
	
	/**
	 * contains the companyService
	 */
	public CompanyService companyService;
	
	/**
	 * contains the databaseService
	 */
	public DatabaseService databaseService;
	
	/**
	 * builds ComputerService defined by databaseService
	 * @param databaseService the databaseService
	 * @param databaseService the databaseService
	 */
	private ComputerService(DatabaseService databaseService, CompanyService companyService) {
		this.databaseService = databaseService;
		this.companyService = companyService;
	}
	
	/**
	 * builds ComputerService if it isn't created or return the actual computerService
	 * @param databaseService the databaseService
	 * @return the actual computerService
	 */
	public static ComputerService getInstance(DatabaseService databaseService, CompanyService companyService) {
		if (computerService == null) {
			return new ComputerService(databaseService, companyService);
		}
		return computerService;
	}
	
	/**
	 * return the list of all computers in the database computer
	 * @return the list of all computers
	 */
	public List<Computer> getComputers() {
		ResultSet queryResult = databaseService.executeQuery("SELECT id, name, introduced, discontinued, company_id  FROM computer;");
		List<Computer> computerList = new ArrayList<Computer>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
		
		try {
			while (queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				String currentIntroducedDate = queryResult.getString("introduced");
				String currentDiscontinuedDate = queryResult.getString("discontinued");
				LocalDate introducedDate = null;
				LocalDate discontinuedDate = null;
				if (currentIntroducedDate != null) {
					try {
						introducedDate = LocalDate.parse(currentIntroducedDate, formatter);
					}
					catch (DateTimeParseException ex) {
						ex.printStackTrace();
					}
				}
				if (currentDiscontinuedDate != null) {
					try {
						discontinuedDate = LocalDate.parse(currentDiscontinuedDate, formatter);
					}
					catch (DateTimeParseException ex) {
						ex.printStackTrace();
					}
				}
				long currentCompanyId = queryResult.getLong("company_id");
				Computer currentComputer = new Computer(currentId, currentName, introducedDate, discontinuedDate, companyService.getCompanyById(currentCompanyId));
				computerList.add(currentComputer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return computerList;
	}
	
	/**
	 * return the computer chosen by identifier
	 * @param id the identifier of the computer
	 * @return the computer chosen by identifier
	 */
	public Computer getComputerById(long id) {
		Computer currentComputer = null;
		ResultSet queryResult = databaseService.executeQuery("SELECT id, name, introduced, discontinued, company_id FROM computer WHERE id = " + id + ";");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
		
		try {
			if(queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				String currentIntroducedDate = queryResult.getString("introduced");
				String currentDiscontinuedDate = queryResult.getString("discontinued");
				LocalDate introducedDate = null;
				LocalDate discontinuedDate = null;
				if (currentIntroducedDate != null) {
					try {
						introducedDate = LocalDate.parse(currentIntroducedDate, formatter);
					}
					catch (DateTimeParseException ex) {
						ex.printStackTrace();
					}
				}
				if (currentDiscontinuedDate != null) {
					try {
						discontinuedDate = LocalDate.parse(currentDiscontinuedDate, formatter);
					}
					catch (DateTimeParseException ex) {
						ex.printStackTrace();
					}
				}
				int currentCompanyId = queryResult.getInt("company_id");
				currentComputer = new Computer(currentId, currentName, introducedDate, discontinuedDate, companyService.getCompanyById(currentCompanyId));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return currentComputer;
	}

	/**
	 * Adds a computer to the database
	 * @param computer the added computer
	 * @return 1 if the computer is added and 0 if not
	 */
	public int addComputer(Computer computer) {
		String name = computer.getName();
		Company company = computer.getCompany();
		String strCompanyId = company.getId() == -1 ? null : Long.toString(company.getId());
		
		// Introduced Date
		LocalDate introducedDate = computer.getIntroducedDate();
		java.sql.Date sqlIntroducedDate = null;
		String computerIntroducedDate = null;
		if (introducedDate != null) {
			sqlIntroducedDate = java.sql.Date.valueOf(introducedDate);
			computerIntroducedDate = "'" + sqlIntroducedDate + "'";
		}
		else {
			computerIntroducedDate = "null";
		}
		
		// Discontinued Date
		LocalDate discontinuedDate = computer.getDiscontinuedDate();
		java.sql.Date sqlDiscontinuedDate = null;
		String computerDiscontinuedDate = null;
		if (discontinuedDate != null) {
			sqlDiscontinuedDate = java.sql.Date.valueOf(discontinuedDate);
			computerDiscontinuedDate = "'" + sqlDiscontinuedDate + "'";
		}
		else {
			computerDiscontinuedDate = "null";
		}

		int queryResult = databaseService.executeUpdate("INSERT INTO computer (name, introduced, discontinued, company_id) VALUE (\'" + name + "\', " + computerIntroducedDate + ", " + computerDiscontinuedDate + ", " + strCompanyId + ");");
		return queryResult;
	}
	
	/**
	 * Updates a computer to the database
	 * @param computer the updated computer
	 * @return 1 if the computer is updated and 0 if not
	 */
	public int updateComputerById(Computer computer) {
		long computerId = computer.getId();
		String computerName = computer.getName();
		
		// Introduced Date
		LocalDate introducedDate = computer.getIntroducedDate();
		java.sql.Date sqlIntroducedDate = null;
		String computerIntroducedDate = null;
		if (introducedDate != null) {
			sqlIntroducedDate = java.sql.Date.valueOf(introducedDate);
			computerIntroducedDate = "'" + sqlIntroducedDate + "'";
		}
		else {
			computerIntroducedDate = "null";
		}
		
		// Discontinued Date
		LocalDate discontinuedDate = computer.getDiscontinuedDate();
		java.sql.Date sqlDiscontinuedDate = null;
		String computerDiscontinuedDate = null;
		if (discontinuedDate != null) {
			sqlDiscontinuedDate = java.sql.Date.valueOf(discontinuedDate);
			computerDiscontinuedDate = "'" + sqlDiscontinuedDate + "'";
		}
		else {
			computerDiscontinuedDate = "null";
		}
		
		long computerCompanyId = computer.getCompany().getId();
		
		int queryResult = databaseService.executeUpdate("UPDATE computer SET name = '" + computerName + "', introduced = " + computerIntroducedDate + ", discontinued = " + computerDiscontinuedDate + ", company_id = " + computerCompanyId + " WHERE id =" + computerId + ";");
		return queryResult;
	}
	
	/**
	 * Deletes a computer to the database
	 * @param id the id of the computer to delete
	 * @return 1 if the computer is deleted and 0 if not
	 */
	public int deleteComputerById(int id) {
		int queryResult = databaseService.executeUpdate("DELETE FROM computer WHERE id=" + id);
		return queryResult;
	}
	
	/**
	 * Return true if the id of the computer is correct and false if not
	 * @param id the identifier of the computer
	 * @return true if the identifier of the computer is correct and false if not
	 */
	public boolean isCorrectId(long id) {
		ResultSet quertyResult = databaseService.executeQuery("SELECT id, name, introduced, discontinued, company_id FROM computer WHERE id = " + id + ";");
		try {
			if (!quertyResult.next()) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
}
