package dto;

import mapper.DateMapper;
import model.Computer;

public class ComputerDTO {
	
	private DateMapper dateMapper;

	private String id;
	private String name;
	private String introducedDate;
	private String discontinuedDate;
	private String company;
	
	public ComputerDTO(Computer computer) {
		dateMapper = DateMapper.getInstance();
		
		id = Long.toString(computer.getId());
		name = computer.getName();
		introducedDate = dateMapper.localDateToString(computer.getIntroducedDate(), "yyyy-MM-dd");
		discontinuedDate = dateMapper.localDateToString(computer.getDiscontinuedDate(), "yyyy-MM-dd");
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
	
}