package ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import model.Company;
import model.Computer;
import persistance.DAO;
import service.CompanyService;
import service.ComputerService;

public class UserInterface {
	
	private DAO dao;
	/**
	 * contains the singleton computerDAO
	 */
	private ComputerService computerService;
	/**
	 * contains the singleton companyDAO
	 */
	private CompanyService companyService;
	/**
	 * contains the scanner
	 */
	private Scanner scanner;
	
	/**
	 * builds a UserInterface and initialize the databaseService, computerService, companyService and scanner
	 */
	public UserInterface() {
		dao = DAO.getInstance();
		companyService = CompanyService.getInstance();
		computerService = ComputerService.getInstance();
		scanner = new Scanner(System.in);
	}
	
	/**
	 * initializes the console interface, give informations to the user and gets his requests
	 */
	public void InitInterface() {
		dao.loadDriver();
		//dao.connectDatabase();
		
		System.out.println("Hi, welcome to your database management application.\n");
		getInstructions();
		
		String str = "";
		
		do {
			System.out.println("\nPlease enter a number between 1 and 7 (Enter help for more informations).");
			str = scanner.nextLine();
			switch (str.trim()) {
	        	case "1":
	        		showComputers();
	                break;
	        	case "2":
	        		showCompanies();
	                break;
	        	case "3":
	        		showComputerById();
	        		break;
	        	case "4":
	        		addComputer();
	        		break;
	        	case "5":
	        		updateComputerInformations();
	        		break;
	        	case "6":
	        		deleteComputer();
	        		break;
	        	case "7":
	        		closeApplication();
	                break;
	        	case "help":
	        		getInstructions();
	        		break;
	        	default:
	        		enterInvalid();
	                break;
			}
        }
        while(!str.trim().equals("7"));
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
		System.out.println("7 - Close application");
	}
	
	/**
	 * shows the informations of a computer
	 */
	public void showComputers() {
		List<Computer> computers = computerService.getComputers();
		System.out.println("\n--- Here is the list of computers ---");
		System.out.println("id name introduced discountinued company_id");
		computers.forEach((v) -> System.out.println(v));
	}
	
	/**
	 * shows all companies
	 */
	public void showCompanies() {
		List<Company> companies = companyService.getCompanies();
		System.out.println("\n--- Here is the list of companies ---");
		System.out.println("id name");
		companies.forEach((v) -> System.out.println(v));
	}
	
	/**
	 * shows the informations of a chosen computer
	 */
	public void showComputerById() {
		System.out.println("Enter the number of the computer.");
		
		int computerId = -1;
		do {
			String str = scanner.nextLine();
			try {
				computerId = Integer.parseInt(str);
				if (computerId < 0) {
					System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the computer.");
				}
			}
			catch (NumberFormatException e) {
				System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the computer.");
			}
		} while (computerId < 0);
		
		Computer computer = computerService.getComputerById(computerId);
		System.out.println("\n--- Here is the information of the computer number " + computerId + " ---");
		System.out.println(computer);
	}
	
	/**
	 * adds a computer
	 */
	public void addComputer() {
		// Name
		System.out.println("Please enter the name of the computer you want to add.");
		String computerName = scanner.nextLine();
		
		// Introduced date
		System.out.println("Please now enter the introduced date with the format dd/MM/yyyy. (If you don't want to enter this date, please enter null)");
		LocalDate introducedDate = null;
		String strIntroducedDate = null;
		do {
			strIntroducedDate = scanner.nextLine();
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				introducedDate = LocalDate.parse(strIntroducedDate, formatter);
			}
			catch (DateTimeParseException e) {
				if (!strIntroducedDate.equals("null")) {
					System.out.println("This is an incorrect date. Please enter the introduced date with the format dd/MM/yyyy. (If you don't want to enter this date, please enter null)");
				}
			}

		} while (!strIntroducedDate.equals("null") && introducedDate == null);
		
		// Discontinued date
		System.out.println("Please now enter the discontinued date with the format dd/MM/yyyy. (If you don't want to enter this date, please enter null)");
		LocalDate discontinuedDate = null;
		String strDiscontinuedDate = null;
		do {
			strDiscontinuedDate = scanner.nextLine();
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				discontinuedDate = LocalDate.parse(strDiscontinuedDate, formatter);
			}
			catch (DateTimeParseException e) {
				if (!strIntroducedDate.equals("null")) {
					System.out.println("This is an incorrect date. Please enter the discontinued date with the format dd/MM/yyyy. (If you don't want to enter this date, please enter null)");
				}
			}
		} while (!strDiscontinuedDate.equals("null") && discontinuedDate == null);

		// Company Id
		System.out.println("Please now enter the company identifiant of your computer. (If you don't want to enter this identifiant, please enter null)");
		int companyId = -1;
		String strCompanyId = null;
		do {
			strCompanyId = scanner.nextLine();
			try {
				companyId = Integer.parseInt(strCompanyId);
				if (companyId < 0) {
					System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the company of your computer. (If you don't want to enter this identifiant, please enter null)");
				}
				// Si companyId est incorrect alors, on recommence
				if (!companyService.isCorrectId(companyId)) {
					companyId = -1;
					System.out.println("This number doesn't correspond to a company. Please, enter the number of the identifiant of the company of your computer. (If you don't want to enter this identifiant, please enter null)");
				}
			}
			catch (NumberFormatException e) {
				System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the computer.");
			}
		} while (!strCompanyId.equals("null") && companyId < 0);
		
		
		Computer c = new Computer(computerName, introducedDate, discontinuedDate, companyService.getCompanyById(companyId));
		
		System.out.println();
		int success = computerService.addComputer(c);
		if (success == 1) {
			System.out.println("Your computer is now added");
		}
		else {
			System.out.println("Your computer is not added");
		}
	}
	
	/**
	 * updates a computer
	 */
	public void updateComputerInformations() {
		System.out.println("Please enter the number of the computer you want to update.");
		
		// On selectionne l'ordinateur en question
		long computerId = -1;
		do {
			String str = scanner.nextLine();
			try {
				computerId = Long.parseLong(str);
				if (computerId < 0) {
					System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the computer you want to update.");
				}
				else {
					// Si computerId est incorrect alors, on recommence
					if (!computerService.isCorrectId(computerId)) {
						computerId = -1;
						System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the computer you want to update.");
					}
				}
			}
			catch (NumberFormatException e) {
				System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the computer.");
			}
		} while (computerId < 0);
		
		Computer computer = computerService.getComputerById(computerId);
		long currentId = computer.getId();
		String currentName = computer.getName();
		LocalDate currentIntroducedDate = computer.getIntroducedDate();
		LocalDate currentDiscontinuedDate = computer.getDiscontinuedDate();
		long currentCompanyId = computer.getCompany().getId();
		
		// Computer name
		System.out.println("The name of the computer is " + computer.getName());
		System.out.println("Do you want to change this name? Enter yes or no.");
		int resultName = -1;
		do {
			String str = scanner.nextLine();
			switch (str.trim()) {
				case "yes":
					resultName = 1;
	                break;
	        	case "no":
	        		resultName = 0;
	        		break;
	        	default:
	        		System.out.println("Sorry, do you want to change this name? Enter yes or no.");
	        		resultName = -1;
	        		break;
			}
		} while(resultName < 0);
		String computerName = null;
		if (resultName == 1) {
			// On lui demande le nouveau nom
        	System.out.println("Please now enter the name of your computer.");
    		do {
    			computerName = scanner.nextLine();
    			if (computerName.trim().equals("")) {
    				System.out.println("Please, enter a correct name");
    			}
    		} while (computerName.trim().equals(""));
    		currentName = computerName.trim();
		}
		
		// Introduced date
		System.out.println("The introduced date of your computer is " + computer.getIntroducedDate());
		System.out.println("Do you want to change this date? Enter yes or no.");
		int result = -1;
		do {
			String str = scanner.nextLine();
			switch (str.trim()) {
				case "yes":
	        		result = 1;
	                break;
	        	case "no":
	        		result = 0;
	        		break;
	        	default:
	        		System.out.println("Sorry, do you want to change this date? Enter yes or no.");
	        		result = -1;
	        		break;
			}
		} while(result < 0);
        if (result == 1) {
        	// On lui demande la nouvelle date
        	System.out.println("Please now enter the introduced date with the format dd/MM/yyyy.");
    		LocalDate introducedDate = null;
    		String strIntroducedDate = null;
    		do {
    			strIntroducedDate = scanner.nextLine();
    			try {
    				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    				introducedDate = LocalDate.parse(strIntroducedDate, formatter);
    			}
    			catch (DateTimeParseException e) {
    				if (!strIntroducedDate.equals("null")) {
    					System.out.println("This is an incorrect date. Please enter the introduced date with the format dd/MM/yyyy.");
    				}
    			}
    		} while (introducedDate == null);
    		currentIntroducedDate = introducedDate;
        }
        
		// Discontinued date
		System.out.println("The discontinued date of your computer is " + computer.getDiscontinuedDate());
		System.out.println("Do you want to change this date? Enter yes or no.");
		int changeDiscontinuedDate = -1;
		do {
			String str = scanner.nextLine();
			switch (str.trim()) {
				case "yes":
					changeDiscontinuedDate = 1;
	                break;
	        	case "no":
	        		changeDiscontinuedDate = 0;
	        		break;
	        	default:
	        		System.out.println("Sorry, do you want to change this date? Enter yes or no.");
	        		changeDiscontinuedDate = -1;
	        		break;
			}
		} while(changeDiscontinuedDate < 0);
        if (changeDiscontinuedDate == 1) {
        	// On lui demande la nouvelle date
        	System.out.println("Please now enter the discontinued date with the format dd/MM/yyyy.");
    		LocalDate discontinuedDate = null;
    		String strDiscontinuedDate = null;
    		do {
    			strDiscontinuedDate = scanner.nextLine();
    			try {
    				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    				discontinuedDate = LocalDate.parse(strDiscontinuedDate, formatter);
    			}
    			catch (DateTimeParseException e) {
    				if (!strDiscontinuedDate.equals("null")) {
    					System.out.println("This is an incorrect date. Please enter the discontinued date with the format dd/MM/yyyy.");
    				}
    			}

    		} while (discontinuedDate == null);
    		currentDiscontinuedDate = discontinuedDate;
        }
        
        // Company id
        System.out.println("The company id of your computer is " + computer.getCompany().getId());
		System.out.println("Do you want to change this number? Enter yes or no.");
		int changeCompanyId = -1;
		do {
			String str = scanner.nextLine();
			switch (str.trim()) {
				case "yes":
					changeCompanyId = 1;
	                break;
	        	case "no":
	        		changeCompanyId = 0;
	        		break;
	        	default:
	        		System.out.println("Sorry, do you want to change the company id? Enter yes or no.");
	        		changeCompanyId = -1;
	        		break;
			}
		} while(changeCompanyId < 0);
		if (changeCompanyId == 1) {
			// On lui demande le nouvelle Id d'entreprise
			System.out.println("Please now enter the company identifiant of your computer.");
			int companyId = -1;
			String strCompanyId = null;
			do {
				strCompanyId = scanner.nextLine();
				try {
					companyId = Integer.parseInt(strCompanyId);
					if (companyId < 0) {
						System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the company of your computer. (If you don't want to enter this identifiant, please enter null)");
					}
					// Si companyId est incorrect alors, on recommence
					if (!companyService.isCorrectId(companyId)) {
						companyId = -1;
						System.out.println("This number doesn't correspond to a company. Please, enter the number of the identifiant of the company of your computer. (If you don't want to enter this identifiant, please enter null)");
					}
				}
				catch (NumberFormatException e) {
					System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the company of the computer.");
				}
			} while (companyId < 0);
			currentCompanyId = companyId;
		}
		
		Computer updatedComputer = new Computer(currentId, currentName, currentIntroducedDate, currentDiscontinuedDate, companyService.getCompanyById(currentCompanyId));
		
		
		int success = computerService.updateComputerById(updatedComputer);
		if (success == 1) {
			System.out.println("Computer number " + currentId + " is now update");
		}
		else {
			System.out.println(currentName);
			System.out.println(currentCompanyId);
			System.out.println("Careful");
		}
	
	}
	
	/**
	 * deletes a computer
	 */
	public void deleteComputer() {
		System.out.println("Please enter the number of the computer you want to delete.");
		
		// On selectionne l'ordinateur en question
		int computerId = -1;
		do {
			String str = scanner.nextLine();
			try {
				computerId = Integer.parseInt(str);
				if (computerId < 0) {
					System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the computer you want to delete.");
				}
				else {
					// Si companyId est incorrect alors, on recommence
					if (!computerService.isCorrectId(computerId)) {
						computerId = -1;
						System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the computer you want to delete.");
					}
				}
			}
			catch (NumberFormatException e) {
				System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the computer.");
			}
		} while (computerId < 0);
		
		System.out.println("You have selected the computer number " + computerId);
		System.out.println("Are you sure you want to delete this computer?");
		int deleteComputer = -1;
		do {
			String str = scanner.nextLine();
			switch (str.trim()) {
				case "yes":
					deleteComputer = 1;
	                break;
	        	case "no":
	        		deleteComputer = 0;
	        		break;
	        	default:
	        		System.out.println("Sorry, do you want to delete the computer? Enter yes or no.");
	        		deleteComputer = -1;
	        		break;
			}
		} while(deleteComputer < 0);
		if (deleteComputer == 1) {
			int success = computerService.deleteComputerById(computerId);
			if (success == 1) {
				System.out.println("Computer number " + computerId + " is now deleted");
			}
			else {
				System.out.println("Careful");
			}
		}
	}
	
	/**
	 * close the application
	 */
	public static void closeApplication() {
		System.out.println("See yaa!");
	}
	
	/**
	 * informs the user that is enter is invalid
	 */
	public static void enterInvalid() {
		System.out.println("Incorrect Enter. Please enter a number between 1 and 7.");
	}
	
}