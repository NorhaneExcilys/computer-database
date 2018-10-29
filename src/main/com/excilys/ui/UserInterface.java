package com.excilys.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.exception.UnknowComputerException;
import com.excilys.exception.UnknowMenuException;
import com.excilys.model.Computer;
import com.excilys.model.Paging;

import com.excilys.exception.DatabaseException;
import com.excilys.exception.UnknowCompanyException;
import com.excilys.model.Company;
import com.excilys.service.CompanyService;
import com.excilys.service.ComputerService;

@Component
public class UserInterface {
	
	@Autowired
	private ComputerService computerService;
	
	@Autowired
	private CompanyService companyService;
	
	private InputManager inputManager;

	private Scanner scanner;
	
	private final static int DEFAULT_COMPUTERS_PER_PAGE = 50;
	private final static int DEFAULT_CURRENT_PAGE = 1;
	
	/**
	 * builds a UserInterface and initialize the databaseService, computerService, companyService and scanner
	 */
	public UserInterface() {
		scanner = new Scanner(System.in);
		inputManager = new InputManager();
	}
	
	/**
	 * initializes the console interface, give informations to the user and gets his requests
	 */
	public void InitInterface() {
		System.out.println("Hi, welcome to your database management application.");
		getInstructions();
		
		Optional<MenuEnum> userInput = Optional.empty();
		do {
			System.out.println("\nPlease enter a number between 1 and 7 (Enter help for more informations).");
			String str = scanner.nextLine();
			try {
				userInput = Optional.of(MenuEnum.stringToMenuEnum(str));
				switch (userInput.get()) {
		        	case GET_COMPUTERS:
		        		showComputers();
		                break;
		        	case GET_COMPANIES:
		        		showCompanies();
		                break;
		        	case GET_COMPUTER_BY_ID:
		        		showComputerById();
		        		break;
		        	case ADD_COMPUTER:
		        		addComputer();
		        		break;
		        	case UPDATE_COMPUTER:
		        		updateComputerInformations();
		        		break;
		        	case DELETE_COMPUTER:
		        		deleteComputer();
		        		break;
		        	case DELETE_COMPANY:
		        		deleteCompany();
		        		break;
		        	case QUIT:
		        		closeApplication();
		                break;
		        	case HELP:
		        		getInstructions();
		        		break;
				}
			}
			catch (UnknowMenuException e) {
				System.out.println(e.getMessage());
			}
        }
        while(userInput.get() != MenuEnum.QUIT);
	}
	
	/**
	 * gives instructions to a user 
	 */
	public void getInstructions() {
		System.out.println("1 - Get the list of computers");
		System.out.println("2 - Get the list of companies");
		System.out.println("3 - Show the detailed information of a computer");
		System.out.println("4 - Add a new computer");
		System.out.println("5 - Update the information of a computer");
		System.out.println("6 - Delete a computer from the database");
		System.out.println("7 - Delete a company from the database");
		System.out.println("8 - Close application");
	}
	
	/**
	 * shows the informations of a computer
	 */
	public void showComputers() {
		List<Computer> computers = new ArrayList<Computer>();
		try {
			
			computers = computerService.getByPage(new Paging(DEFAULT_COMPUTERS_PER_PAGE, DEFAULT_CURRENT_PAGE));
			if (computers.size() > 0) {
				computers.forEach((v) -> System.out.println(v));
			}
			else {
				System.out.println("Sorry, there is no computer in the database");
			}
		} catch (DatabaseException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (UnknowCompanyException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * shows all companies
	 */
	public void showCompanies() {
		List<Company> companies = new ArrayList<Company>();
		
		try {
			companies = companyService.getCompanies();
			if (companies.size() > 0) {
				companies.forEach((v) -> System.out.println(v));
			}
			else {
				System.out.println("Sorry, there is no company in the database");
			}
		} catch (DatabaseException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * shows the informations of a chosen computer
	 */
	public void showComputerById() {
		System.out.println("Enter the number of the computer you want to show.");
		
		int computerId = -1;
		Optional<Computer> computer = Optional.empty();
		
		do {
			String input = scanner.nextLine();	
			try {
				computerId = Integer.parseInt(input);
				if (computerId < 0) {
					throw new NumberFormatException();
				}
				computer = computerService.getById(computerId);
			} catch (NumberFormatException e) {
				System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the computer.");
			} catch (DatabaseException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		} while (!computer.isPresent());
		
		System.out.println("Here is the information of the computer number " + computerId);
		System.out.println(computer.get());
	}
	
	/**
	 * deletes a computer
	 */
	public void deleteComputer() {
		System.out.println("Please enter the number of the computer you want to delete.");
		
		int computerId = -1;
		boolean success = false;
		
		do {
			String input = scanner.nextLine();
			try {
				computerId = Integer.parseInt(input);
				if (computerId < 0) {
					throw new NumberFormatException();
				}
				success = computerService.deleteComputerByList(input);
			}
			catch (NumberFormatException e) {
				System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the computer.");
			} catch (DatabaseException e) {
				e.printStackTrace();
			} catch (UnknowComputerException e) {
				System.out.println(e.getMessage());
			}
		} while (!success);
		
		System.out.println("Computer number " + computerId + " is now deleted");
	}
	
	public void deleteCompany() {
		System.out.println("Please enter the number of the company you want to delete.");
		
		int companyId = -1;
		boolean success = false;
		
		do {
			String input = scanner.nextLine();
			try {
				companyId = Integer.parseInt(input);
				if (companyId < 0) {
					throw new NumberFormatException();
				}
				success = companyService.deleteCompanyById(companyId);
			}
			catch (NumberFormatException e) {
				System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the company.");
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		} while (!success);
		
		System.out.println("Company number " + companyId + " is now deleted");
	}
	/**
	 * adds a computer
	 */
	public void addComputer() {
		// Name
		System.out.println("Please enter the name of the computer you want to add.");
		String computerName = inputManager.askComputerName();
		
		// Introduced date
		System.out.println("Please enter the introduced date with the format dd/MM/yyyy.\n"
				+ "If you don\'t want to enter this date, please press button ENTER.");
		Optional<LocalDate> introducedDate = inputManager.askDate();
		
		// Discontinued date
		System.out.println("Please enter the discontinued date with the format dd/MM/yyyy.\n"
				+ "If you don\'t want to enter this date, please press button ENTER.");
		Optional<LocalDate> discontinuedDate = inputManager.askDate();

		// Company Id
		System.out.println("Please enter the company identifier of your computer.\n"
				+ "If you don't want to enter this identifier, please press button ENTER.");
		
		Optional<Company> company = Optional.empty();
		boolean correctCompanyId = false;
		do {
			Optional<Long> companyId = inputManager.askCompanyId();
			if (companyId.isPresent()) {
				try {
					company = companyService.getCompanyById(companyId.get());
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
				if (company.isPresent()) {
					correctCompanyId = true;
				}
			}
			else {
				correctCompanyId = true;
			}
		} while (!correctCompanyId);
		
		Computer computer;
		boolean success = false;
		try {
			computer = new Computer.ComputerBuilder(computerName).introducedDate(introducedDate).discontinuedDate(discontinuedDate).company(company).build();
			success = computerService.addComputer(computer);
		} catch (DatabaseException e) {
			System.out.print(e.getMessage());
		}
		
		if (success) {
			System.out.println("Your computer is added");
		}
	}
	
	/**
	 * updates a computer
	 */
	public void updateComputerInformations() {
		System.out.println("Please enter the number of the computer you want to update.");
		
		
		Optional<Computer> computer = Optional.empty();
		do {
			long computerId = inputManager.askComputerId();
			try {
				computer = computerService.getById(computerId);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			if (!computer.isPresent()) {
				System.out.println("This identifier is unknow, please, enter a correct identifier of a computer.");
			}
		} while (!computer.isPresent());
		
		Computer currentComputer = computer.get();
		long currentId = currentComputer.getId();
		String currentName = currentComputer.getName();
		Optional<LocalDate> currentIntroducedDate = currentComputer.getIntroducedDate();
		Optional<LocalDate> currentDiscontinuedDate = currentComputer.getDiscontinuedDate();
		Optional<Company> currentCompany = currentComputer.getCompany();
		
		// Computer name
		System.out.println("The name of the computer is " + currentName);
		YesOrNoEnum changeName = inputManager.getYesOrNo("Do you want to change this name? Enter yes or no.");
		if (changeName == YesOrNoEnum.YES) {
			System.out.println("Please enter the new name of the computer.");
			currentName = inputManager.askComputerName();
		}
		
		// Introduced date
		System.out.println("The introduced date of the computer is " + currentIntroducedDate);
		YesOrNoEnum changeIntroducedDate = inputManager.getYesOrNo("Do you want to change this introduced date? Enter yes or no.");
		if (changeIntroducedDate == YesOrNoEnum.YES) {
			System.out.println("Please enter the new introduced date of the computer.");
			currentIntroducedDate = inputManager.askDate();
		}
		
		// Discontinued date
		System.out.println("The discontinued date of the computer is " + currentDiscontinuedDate);
		YesOrNoEnum changeDiscontinuedDate = inputManager.getYesOrNo("Do you want to change this discontinued date? Enter yes or no.");
		if (changeDiscontinuedDate == YesOrNoEnum.YES) {
			System.out.println("Please enter the new discontinued date of the computer.");
			currentDiscontinuedDate = inputManager.askDate();
		}
		
		// Company
		System.out.println("The company of the company is " + currentCompany);
		YesOrNoEnum changeCompanyId = inputManager.getYesOrNo("Do you want to change this company? Enter yes or no.");
		if (changeCompanyId == YesOrNoEnum.YES) {
			System.out.println("Please enter the new company identifier of the computer.");
			currentCompany = Optional.empty();
			boolean correctCompanyId = false;
			do {
				Optional<Long> companyId = inputManager.askCompanyId();
				if (companyId.isPresent()) {
					try {
						currentCompany = companyService.getCompanyById(companyId.get());
					} catch (DatabaseException e) {
						e.printStackTrace();
					}
					if (currentCompany.isPresent()) {
						correctCompanyId = true;
					}
				}
				else {
					correctCompanyId = true;
				}
			} while (!correctCompanyId);
		}
		Computer updatedComputer = new Computer.ComputerBuilder(currentName).id(currentId).introducedDate(currentIntroducedDate).discontinuedDate(currentDiscontinuedDate).company(currentCompany).build();
		boolean success;
		try {
			success = computerService.updateComputerById(updatedComputer);
			if (success) {
				System.out.println("Computer number " + currentId + " is now update");
			}
			else {
				System.out.println("Careful");
			}
		} catch (DatabaseException e) {
			System.out.println(e.getMessage());
		} catch (UnknowComputerException e) {
			System.out.println(e.getMessage());
		}
		
	}

	/**
	 * close the application
	 */
	public static void closeApplication() {
		System.out.println("See yaa!");
	}

}