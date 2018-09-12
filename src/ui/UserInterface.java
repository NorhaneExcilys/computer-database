package ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.Company;
import model.Computer;
import service.CompanyService;
import service.ComputerService;
import service.DatabaseService;

public class UserInterface {
	
	private DatabaseService _db;
	private ComputerService _computerService;
	private CompanyService _companyService;
	private Scanner _scanner;
	
	public UserInterface() {
		_db = DatabaseService.getInstance();
		_computerService = ComputerService.getInstance(_db);
		_companyService = CompanyService.getInstance(_db);
		_scanner = new Scanner(System.in);
	}
	
	public void InitInterface() {
		_db.loadDriver();
		_db.connectDatabase();
		
		System.out.println("Hi, welcome to your database management application.\n");
		getInstructions();
		
		String str = "";
		
		do {
			System.out.println("\nPlease enter a number between 1 and 7 (Enter help for more informations).");
			str = _scanner.nextLine();
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
	
	public void getInstructions() {
		System.out.println("1 - Get the list of computers");
		System.out.println("2 - Get the list of companies");
		System.out.println("3 - Show the detailed information of a computer");
		System.out.println("4 - Add a new computer");
		System.out.println("5 - Update the information of a computer");
		System.out.println("6 - Delete a computer from the database");
		System.out.println("7 - Close application");
	}
	
	public void showComputers() {
		List<Computer> computers = _computerService.getComputers();
		System.out.println("\n--- Here is the list of computers ---");
		System.out.println("id name introduced discountinued company_id");
		computers.forEach((v) -> System.out.println(v));
	}
	
	public void showCompanies() {
		List<Company> companies = _companyService.getCompanies();
		System.out.println("\n--- Here is the list of companies ---");
		System.out.println("id name");
		companies.forEach((v) -> System.out.println(v));
	}
	
	public void showComputerById() {
		System.out.println("Enter the number of the computer.");
		
		int computerId = -1;
		do {
			String str = _scanner.nextLine();
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
		
		Computer computer = _computerService.getComputerById(computerId);
		System.out.println("\n--- Here is the information of the computer number " + computerId + " ---");
		System.out.println(computer.getDetail());
	}
	
	public void addComputer() {
		// Name
		System.out.println("Please enter the name of the computer you want to add.");
		String computerName = _scanner.nextLine();
		
		// Introduced date
		System.out.println("Please now enter the introduced date with the format dd/MM/yyyy. (If you don't want to enter this date, please enter null)");
		Date introducedDate = null;
		String strIntroducedDate = null;
		do {
			strIntroducedDate = _scanner.nextLine();
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
				introducedDate = simpleDateFormat.parse(strIntroducedDate);
			}
			catch (ParseException e) {
				if (!strIntroducedDate.equals("null")) {
					System.out.println("This is an incorrect date. Please enter the introduced date with the format dd/MM/yyyy. (If you don't want to enter this date, please enter null)");
				}
			}
		} while (!strIntroducedDate.equals("null") && introducedDate == null);
		
		// Discontinued date
		System.out.println("Please now enter the discontinued date with the format dd/MM/yyyy. (If you don't want to enter this date, please enter null)");
		Date discontinuedDate = null;
		String strDiscontinuedDate = null;
		do {
			strDiscontinuedDate = _scanner.nextLine();
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
				discontinuedDate = simpleDateFormat.parse(strDiscontinuedDate);
			}
			catch (ParseException e) {
				if (!strDiscontinuedDate.equals("null")) {
					System.out.println("This is an incorrect date. Please enter the discontinued date with the format dd/MM/yyyy. (If you don't want to enter this date, please enter null)");
				}
			}
		} while (!strDiscontinuedDate.equals("null") && discontinuedDate == null);

		// Company Id
		System.out.println("Please now enter the company identifiant of your computer. (If you don't want to enter this identifiant, please enter null)");
		int companyId = -1;
		String strCompanyId = null;
		do {
			strCompanyId = _scanner.nextLine();
			try {
				companyId = Integer.parseInt(strCompanyId);
				if (companyId < 0) {
					System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the company of your computer. (If you don't want to enter this identifiant, please enter null)");
				}
				// Si companyId est incorrect alors, on recommence
				if (!_companyService.isCorrectId(companyId)) {
					companyId = -1;
					System.out.println("This number doesn't correspond to a company. Please, enter the number of the identifiant of the company of your computer. (If you don't want to enter this identifiant, please enter null)");
				}
			}
			catch (NumberFormatException e) {
				System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the computer.");
			}
		} while (!strCompanyId.equals("null") && companyId < 0);
		
		Computer c = new Computer(computerName, introducedDate, discontinuedDate, companyId);
		int success = _computerService.addComputer(c);
		if (success == 1) {
			System.out.println("Your computer is now added");
		}
		else {
			System.out.println("Your computer is not added");
		}
	}
	
	public void updateComputerInformations() {
		System.out.println("Please enter the number of the computer you want to update.");
		
		// On selectionne l'ordinateur en question
		int computerId = -1;
		do {
			String str = _scanner.nextLine();
			try {
				computerId = Integer.parseInt(str);
				if (computerId < 0) {
					System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the computer you want to update.");
				}
				else {
					// Si companyId est incorrect alors, on recommence
					if (!_computerService.isCorrectId(computerId)) {
						computerId = -1;
						System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the computer you want to update.");
					}
				}
			}
			catch (NumberFormatException e) {
				System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the computer.");
			}
		} while (computerId < 0);
		
		Computer computer = _computerService.getComputerById(computerId);
		int currentId = computer.getId();
		String currentName = computer.getName();
		Date currentIntroducedDate = computer.getIntroducedDate();
		Date currentDiscontinuedDate = computer.getDiscontinuedDate();
		int currentCompanyId = computer.getCompanyId();
		
		// Computer name
		System.out.println("The name of the computer is " + computer.getName());
		System.out.println("Do you want to change this name? Enter yes or no.");
		int resultName = -1;
		do {
			String str = _scanner.nextLine();
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
    		Date introducedDate = null;
    		do {
    			computerName = _scanner.nextLine();
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
			String str = _scanner.nextLine();
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
    		Date introducedDate = null;
    		String strIntroducedDate = null;
    		do {
    			strIntroducedDate = _scanner.nextLine();
    			try {
    				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    				introducedDate = simpleDateFormat.parse(strIntroducedDate);
    			}
    			catch (ParseException e) {
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
			String str = _scanner.nextLine();
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
    		Date discontinuedDate = null;
    		String strDiscontinuedDate = null;
    		do {
    			strDiscontinuedDate = _scanner.nextLine();
    			try {
    				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    				discontinuedDate = simpleDateFormat.parse(strDiscontinuedDate);
    			}
    			catch (ParseException e) {
    				if (!strDiscontinuedDate.equals("null")) {
    					System.out.println("This is an incorrect date. Please enter the discontinued date with the format dd/MM/yyyy.");
    				}
    			}
    		} while (discontinuedDate == null);
    		currentDiscontinuedDate = discontinuedDate;
        }
        
        // Company id
        System.out.println("The company id of your computer is " + computer.getCompanyId());
		System.out.println("Do you want to change this number? Enter yes or no.");
		int changeCompanyId = -1;
		do {
			String str = _scanner.nextLine();
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
				strCompanyId = _scanner.nextLine();
				try {
					companyId = Integer.parseInt(strCompanyId);
					if (companyId < 0) {
						System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the company of your computer. (If you don't want to enter this identifiant, please enter null)");
					}
					// Si companyId est incorrect alors, on recommence
					if (!_companyService.isCorrectId(companyId)) {
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
		
		Computer updatedComputer = new Computer(currentId, currentName, currentIntroducedDate, currentDiscontinuedDate, currentCompanyId);
		
		
		int success = _computerService.updateComputerById(updatedComputer);
		if (success == 1) {
			System.out.println("Computer number " + currentId + " is now update");
		}
		else {
			System.out.println(currentName);
			System.out.println(currentCompanyId);
			System.out.println("Careful");
		}
	
	}
	
	public void deleteComputer() {
		System.out.println("Please enter the number of the computer you want to delete.");
		
		// On selectionne l'ordinateur en question
		int computerId = -1;
		do {
			String str = _scanner.nextLine();
			try {
				computerId = Integer.parseInt(str);
				if (computerId < 0) {
					System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the computer you want to delete.");
				}
				else {
					// Si companyId est incorrect alors, on recommence
					if (!_computerService.isCorrectId(computerId)) {
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
			String str = _scanner.nextLine();
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
			int success = _computerService.deleteComputerById(computerId);
			if (success == 1) {
				System.out.println("Computer number " + computerId + " is now deleted");
			}
			else {
				System.out.println("Careful");
			}
		}
	}
	
	public static void closeApplication() {
		System.out.println("See yaa!");
	}
	
	public static void enterInvalid() {
		System.out.println("Incorrect Enter. Please enter a number between 1 and 7.");
	}
	
}