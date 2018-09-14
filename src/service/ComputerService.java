package service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	 * contains the databaseService
	 */
	public DatabaseService databaseService;
	
	/**
	 * builds ComputerService defined by databaseService
	 * @param databaseService the databaseService
	 * @param databaseService the databaseService
	 */
	private ComputerService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
	
	/**
	 * builds ComputerService if it isn't created or return the actual computerService
	 * @param databaseService the databaseService
	 * @return the actual computerService
	 */
	public static ComputerService getInstance(DatabaseService databaseService) {
		if (computerService == null) {
			return new ComputerService(databaseService);
		}
		return computerService;
	}
	
	/**
	 * return the list of all computers in the database computer
	 * @return the list of all computers
	 */
	public List<Computer> getComputers() {
		ResultSet queryResult = databaseService.executeQuery("SELECT * FROM computer;");
		List<Computer> computerList = new ArrayList<Computer>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			while (queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				String currentIntroducedDate = queryResult.getString("introduced");
				String currentDiscontinuedDate = queryResult.getString("discontinued");
				Date introducedDate = null;
				Date discontinuedDate = null;
				try {
					if (currentIntroducedDate != null) {
						introducedDate = simpleDateFormat.parse(currentIntroducedDate);
					}
					if (currentDiscontinuedDate != null) {
						discontinuedDate = simpleDateFormat.parse(currentDiscontinuedDate);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				int currentCompanyId = queryResult.getInt("company_id");
				Computer currentComputer = new Computer(currentId, currentName, introducedDate, discontinuedDate, currentCompanyId);
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
		ResultSet queryResult = databaseService.executeQuery("SELECT * FROM computer WHERE id = " + id + ";");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			if(queryResult.next()) {
				int currentId = queryResult.getInt("id");
				String currentName = queryResult.getString("name");
				String currentIntroducedDate = queryResult.getString("introduced");
				String currentDiscontinuedDate = queryResult.getString("discontinued");
				Date introducedDate = null;
				Date discontinuedDate = null;
				try {
					if (currentIntroducedDate != null) {
						introducedDate = simpleDateFormat.parse(currentIntroducedDate);
					}
					if (currentDiscontinuedDate != null) {
						discontinuedDate = simpleDateFormat.parse(currentDiscontinuedDate);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				int currentCompanyId = queryResult.getInt("company_id");
				currentComputer = new Computer(currentId, currentName, introducedDate, discontinuedDate, currentCompanyId);
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
		long companyId = computer.getCompanyId();
		String strCompanyId = companyId == -1 ? null : Long.toString(companyId);
		
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
		
		long computerCompanyId = computer.getCompanyId();
		
		System.out.println("UPDATE computer SET name = '" + computerName + "', introduced = " + sqlIntroducedDate + ", discontinued = " + sqlDiscontinuedDate + ", company_id = " + computerCompanyId + " WHERE id =" + computerId + ";");
		
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
		ResultSet quertyResult = databaseService.executeQuery("SELECT * FROM computer WHERE id = " + id + ";");
		try {
			if (!quertyResult.next()) {
				return false;
			}
		} catch (SQLException e) {

		}
		return true;
	}
	
}
