package mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class DateMapper {
	
	private static DateMapper dateMapper;

	public static DateMapper getInstance() {
		if (dateMapper == null) {
			dateMapper = new DateMapper();
		}
		return dateMapper;
	}
	
	public String localDateToString(Optional<LocalDate> date, String format) {
		String strDate = "";
		if (date.isPresent()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
			strDate = date.get().format(formatter);
		}
		return strDate;
	}
	
	public Optional<LocalDate> stringToLocalDate(String strDate, String format) throws DateTimeParseException {
		Optional<LocalDate> date = Optional.empty();
		if (!strDate.equals("")) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
			date = Optional.of(LocalDate.parse(strDate, formatter));
		}
		return date;
	}
	
}
