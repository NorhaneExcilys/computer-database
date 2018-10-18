package com.excilys.dto;

public class ComputerDTO {

	private String id;
	private String name;
	private String introducedDate;
	private String discontinuedDate;
	private String companyId;
	private String companyName;
	
	private ComputerDTO(ComputerDTOBuilder computerDTOBuilder) {
		this.id = computerDTOBuilder.id;
		this.name = computerDTOBuilder.name;
		this.introducedDate = computerDTOBuilder.introducedDate;
		this.discontinuedDate = computerDTOBuilder.discontinuedDate;
		this.companyId = computerDTOBuilder.companyId;
		this.companyName = computerDTOBuilder.companyName;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getIntroducedDate() {
		return introducedDate;
	}

	public String getDiscontinuedDate() {
		return discontinuedDate;
	}
	
	public String getCompanyId() {
		return companyId;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	public static class ComputerDTOBuilder {
		private String id;
		private String name;
		private String introducedDate;
		private String discontinuedDate;
		private String companyId;
		private String companyName;
		
		public ComputerDTOBuilder (String name) {
    		this.name = name;
    	}
		
	  	public ComputerDTOBuilder id (String id) {
    		this.id = id;
    		return this;
    	}
	  	
	  	public ComputerDTOBuilder name (String name) {
    		this.name = name;
    		return this;
    	}
    	
    	public ComputerDTOBuilder introducedDate (String introducedDate) {
    		this.introducedDate = introducedDate;
    		return this;
    	}
    	
    	public ComputerDTOBuilder discontinuedDate (String discontinuedDate) {
    		this.discontinuedDate = discontinuedDate;
    		return this;
    	}
    	
    	public ComputerDTOBuilder companyId (String companyId) {
    		this.companyId = companyId;
    		return this;
    	}
    	
    	public ComputerDTOBuilder companyName (String companyName) {
    		this.companyName = companyName;
    		return this;
    	}
    	
    	public ComputerDTO build() {
    		return new ComputerDTO(this);
    	}
	}
	
}