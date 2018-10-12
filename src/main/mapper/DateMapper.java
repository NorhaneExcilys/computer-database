package mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import exception.IncorrectDateException;
import validator.Validator;

public class DateMapper {
	
	private static DateMapper dateMapper;
	private Validator validator;
	
	public static DateMapper getInstance() {
		if (dateMapper == null) {
			dateMapper = new DateMapper();
		}
		return dateMapper;
	}
	
	public DateMapper() {
		this.validator = Validator.getInstance();
	}
	
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
