package com.excilys.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.exception.IncorrectDateException;
import com.excilys.validator.Validator;

@Component
public class DateMapper {
	
	@Autowired
	private Validator validator;
	
	public String localDateToString(Optional<LocalDate> date, String format) {
		String strDate = "";
		if (date.isPresent()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
			strDate = date.get().format(formatter);
		}
		return strDate;
	}
	
	public Optional<LocalDate> stringToLocalDate(String strDate, String format) throws IncorrectDateException {
		if (validator.isValidDate(strDate, format)) {
			Optional<LocalDate> date = Optional.empty();
			if (!strDate.equals("")) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
				date = Optional.of(LocalDate.parse(strDate, formatter));
			}
			return date;
		}
		throw new IncorrectDateException();
	}
	
}
