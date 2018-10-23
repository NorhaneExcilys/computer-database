package com.excilys.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.excilys.exception.UnknowYesOrNoException;

import com.excilys.exception.IncorrectNameException;

@Component
public class InputManager {
	
	private Scanner scanner;
	
	public InputManager() {
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
	
	
	public Optional<Long> askCompanyId() {
		Optional<Long> companyId = Optional.empty();
		boolean correctCompanyId = false;
		do {
			String strCompanyId = scanner.nextLine();
			if (strCompanyId.equals("")) {
				companyId = Optional.empty();
				correctCompanyId = true;
			}
			else {
				try {
					companyId = Optional.of(Long.parseLong(strCompanyId));
					if (companyId.get() < 0) {
						throw new NumberFormatException();
					}
					else {
						correctCompanyId = true;
					}
				}
				catch (NumberFormatException e) {
					System.out.println("This is an incorrect number. Please, enter the number of the identifier of the company.");
				}
			}
		} while (!correctCompanyId);
		return companyId;
	}
	
	public long askComputerId() {
		long computerId = -1;
		boolean correctComputerId = false;
		do {
			String input = scanner.nextLine();
			try {
				computerId = Long.parseLong(input);
				if (computerId < 0) {
					throw new NumberFormatException();
				}
				else {
					correctComputerId = true;
				}
			}
			catch (NumberFormatException e) {
				System.out.println("This is an incorrect number. Please, enter the number of the identifiant of the computer.");
			}
		} while (!correctComputerId);
		return computerId;
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