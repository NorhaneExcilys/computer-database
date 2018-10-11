package ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.Scanner;

import exception.DatabaseException;
import exception.IncorrectNameException;
import exception.UnknowCompanyException;
import exception.UnknowComputerException;
import exception.UnknowYesOrNoException;
import model.Company;
import model.Computer;
import service.CompanyService;
import service.ComputerService;

public class InputManager {
	
	private ComputerService computerService;
	private CompanyService companyService;
	private Scanner scanner;
	
	public InputManager() {
		companyService = CompanyService.getInstance();
		computerService = ComputerService.getInstance();
		scanner = new Scanner(System.in);
	}
	
	public String askComputerName() {
		boolean correctName = false;
		String computerName = "";
		do {
			try {
				computerName = scanner.nextLine().trim();
				if (computerName.equals("")) {
					throw new IncorrectNameException();
				}
				correctName = true;
			} catch (IncorrectNameException e) {
				System.out.println(e.getMessage());
			}
		}
		while (!correctName);
		return computerName;
	}
	
	public Optional<Company> askCompany() {
		int companyId = -1;
		Optional<Company> company = Optional.empty();
		boolean correctCompanyId = false;
		do {
			String strCompanyId = scanner.nextLine();
			if (strCompanyId.equals("")) {
				company = Optional.empty();
				correctCompanyId = true;
			}
			else {
				try {
					companyId = Integer.parseInt(strCompanyId);
					if (companyId < 0) {
						throw new NumberFormatException();
					}
					else {
						company = companyService.getCompanyById(companyId);
						if (!company.isPresent()) {
							throw new UnknowCompanyException();
						}
						else {
							correctCompanyId = true;
						}
					}
				}
				catch (NumberFormatException e) {
					System.out.println("This is an incorrect number. Please, enter the number of the identifier of the company.");
				}
				catch (UnknowCompanyException e) {
					System.out.println(e.getMessage());
				}
				catch (DatabaseException e) {
					System.out.println(e.getMessage());
				}
			}
		} while (!correctCompanyId);
		return company;
	}
	
	public Optional<LocalDate> askDate() {
		boolean correctDate = false;
		Optional<LocalDate> date = Optional.empty();
		do {
			String strDate = scanner.nextLine();
			if (strDate.equals("")) {
				correctDate = true;
			}
			else {
				try {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					date = Optional.of(LocalDate.parse(strDate, formatter));
					correctDate = true;
				}
				catch (DateTimeParseException e) {
					System.out.println("This is an incorrect format date.\n"
							+ "Please enter the date with the format dd/MM/yyyy.\n"
							+ "If you don't want to enter this date, please press button ENTER.");
				}
			}
		} while (!correctDate);
		return date;
	}
	
	public Optional<Computer> askComputer() {
		Optional<Computer> computer = Optional.empty();
		do {
			String input = scanner.nextLine();
			try {
				long computerId = Long.parseLong(input);
				if (computerId < 0) {
					throw new NumberFormatException();
				}
				else {
					computer = computerService.getById(computerId);
					if (!computer.isPresent()) {
						throw new UnknowComputerException();
					}
				}
			}
			catch (NumberFormatException e) {
				System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the computer.");
			} catch (DatabaseException e) {
				System.out.println(e.getMessage());
			} catch (UnknowComputerException e) {
				System.out.println(e.getMessage());
			} catch (UnknowCompanyException e) {
				System.out.println(e.getMessage());
			}
		} while (!computer.isPresent());
		return computer;
	}
	
	public YesOrNoEnum getYesOrNo (String question) {
		System.out.println(question);
		Optional<YesOrNoEnum> answer = Optional.empty();
		do {
			String str = scanner.nextLine();
			try {
				answer = Optional.of(YesOrNoEnum.stringToYesOrNoEnum(str));
			}
			catch (IllegalArgumentException e) {
				System.out.println("Incorrect input. Please enter yes or no.");
			}
			catch (UnknowYesOrNoException e) {
				System.out.println(e.getMessage());
			}
		}
		while (!answer.isPresent());
		return answer.get();
	}

}