package dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import model.Computer;

public class ComputerDTO {

	private String id;
	private String name;
	private String introducedDate;
	private String discontinuedDate;
	private String company;
	
	public ComputerDTO(Computer computer) {
		id = Long.toString(computer.getId());
		name = computer.getName();
		introducedDate = localDateToString(computer.getIntroducedDate());
		discontinuedDate = localDateToString(computer.getDiscontinuedDate());
		company = computer.getCompany().isPresent() ? computer.getCompany().get().getName() : "";
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntroducedDate() {
		return introducedDate;
	}
	public void setIntroducedDate(String introducedDate) {
		this.introducedDate = introducedDate;
	}
	public String getDiscontinuedDate() {
		return discontinuedDate;
	}
	public void setDiscontinuedDate(String discontinuedDate) {
		this.discontinuedDate = discontinuedDate;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	private String localDateToString(Optional<LocalDate> date) {
		String strDate = "";
		if (date.isPresent()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			strDate = date.get().format(formatter);
		}
		return strDate;
	}
	
}
