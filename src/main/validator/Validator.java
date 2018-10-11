package validator;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import exception.IncorrectNameException;
import exception.IncorrectDateException;
import exception.IncorrectIdException;
import mapper.DateMapper;

public class Validator {
	
	private DateMapper dateMapper;
	
	public Validator() {
		dateMapper = DateMapper.getInstance();
	}
	
	public boolean isValidName(String strName) throws IncorrectNameException {
		if (!strName.trim().equals("")) {
			return true;
		}
		throw new IncorrectNameException();
	}
	
	public boolean isValidId(String strId) throws IncorrectIdException {
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
	
	public boolean isValidDate(String strDate) throws IncorrectDateException {
		try {
			Optional<LocalDate> date = dateMapper.stringToLocalDate(strDate, "yyyy-MM-dd");
			if (!date.isPresent()) {
				return true;
			}
			else {
				if (date.get().getYear() > 1970) {
					return true;
				}
			}
		}
		catch (DateTimeParseException e) {
			throw new IncorrectDateException();
		}
		throw new IncorrectDateException();
	}
	
	public boolean compareDate(String strIntroducedDate, String strDiscontinuedDate) throws IncorrectDateException {
		try {
			if (isValidDate(strIntroducedDate) && isValidDate(strDiscontinuedDate)) {
				Optional<LocalDate> introducedDate = dateMapper.stringToLocalDate(strIntroducedDate, "yyyy-MM-dd");
				Optional<LocalDate> discontinuedDate = dateMapper.stringToLocalDate(strDiscontinuedDate, "yyyy-MM-dd");
				if (introducedDate.isPresent() && discontinuedDate.isPresent()) {
					if (introducedDate.get().isAfter(discontinuedDate.get())){
						return true;
					}
				}
				else {
					return true;
				}
			}
		} catch (DateTimeParseException e) {
			throw new IncorrectDateException();
		} catch (IncorrectDateException e) {
			throw new IncorrectDateException();
		}
		throw new IncorrectDateException();
	}
	
}