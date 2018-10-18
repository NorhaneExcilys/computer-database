package com.excilys.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Component;

import com.excilys.exception.IntroducedAfterDiscontinuedException;

import com.excilys.dto.ComputerDTO;
import com.excilys.exception.IncorrectNameException;
import com.excilys.exception.IncorrectDateException;
import com.excilys.exception.IncorrectIdException;

@Component
public class Validator {
	
	public boolean validComputer(ComputerDTO computerDTO, String format) throws IncorrectNameException, IncorrectIdException, IncorrectDateException, IntroducedAfterDiscontinuedException {
		return isValidName(computerDTO.getName()) && introducedIsBeforeDiscontinued(computerDTO.getIntroducedDate(), computerDTO.getDiscontinuedDate(), format) && isValidId(computerDTO.getCompanyId());
	}
	
	protected boolean isValidName(String strName) throws IncorrectNameException {
		if (!strName.trim().equals("")) {
			return true;
		}
		throw new IncorrectNameException();
	}
	
	protected boolean isValidId(String strId) throws IncorrectIdException {
		if (strId == null) {
			return true;
		}
		try {
			long id = Long.parseLong(strId);
			if (id >= 0) {
				return true;
			}
		}
		catch (NumberFormatException e) {
			throw new IncorrectIdException();
		}
		throw new IncorrectIdException();
	}
	
	public boolean isValidDate(String strDate, String format) throws IncorrectDateException {
		if (strDate.equals("")) {
			return true;
		}
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
			LocalDate.parse(strDate, formatter);
			return true;
		}
		catch (DateTimeParseException e) {
			throw new IncorrectDateException();
		}
	}
	
	protected boolean introducedIsBeforeDiscontinued(String strIntroducedDate, String strDiscontinuedDate, String format) throws IncorrectDateException, IntroducedAfterDiscontinuedException {
		if (strIntroducedDate.equals("") || strDiscontinuedDate.equals("")) {
			return true;
		}
		else {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
				LocalDate introducedDate = LocalDate.parse(strIntroducedDate, formatter);
				LocalDate discontinuedDate = LocalDate.parse(strDiscontinuedDate, formatter);
				if (!introducedDate.isAfter(discontinuedDate)){
					return true;
				}
				else {
					throw new IntroducedAfterDiscontinuedException();
				}
			}
			catch (DateTimeParseException e) {
				throw new IncorrectDateException();
			}	
		}
	}
	
}