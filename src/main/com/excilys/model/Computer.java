package com.excilys.model;

import java.time.LocalDate;
import java.util.Optional;

/**
 * <b>Computer is the class that represent a computer.</b>
 * A computer is characterized by the following informations:
 * <ul>
 * <li> A unique identifier assigned automatically.</li>
 * <li> A name.</li>
 * <li> An introduced date.</li>
 * <li> A discontinued date.</li>
 * <li> A company.</li>
 * </ul>
 * @author elgharbi
 *
 */

public class Computer {

	private long id;
	private String name;
	private Optional<LocalDate> introducedDate;
	private Optional<LocalDate> discontinuedDate;
	private Optional<Company> company;
	
	/**
	 * builds Computer defined by a computerBuilder
	 * @param computerBuilder the companyBuilder
	 */
	private Computer(ComputerBuilder computerBuilder) {
		this.id = computerBuilder.id;
		this.name = computerBuilder.name;
		this.introducedDate = computerBuilder.introducedDate;
		this.discontinuedDate = computerBuilder.discontinuedDate;
		this.company = computerBuilder.company;
	}
	
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public Optional<LocalDate> getIntroducedDate() {
		return introducedDate;
	}
	
	public Optional<LocalDate> getDiscontinuedDate() {
		return discontinuedDate;
	}
	
	public Optional<Company> getCompany() {
		return company;
	}
	
    @Override
	public String toString() {
		return "Computer [id=" + id + ", name=" + name + ", introducedDate=" + introducedDate + ", discontinuedDate="
				+ discontinuedDate + ", company=" + company + "]";
	}
    
    public static class ComputerBuilder {
    	private long id;
    	private String name;
    	private Optional<LocalDate> introducedDate;
    	private Optional<LocalDate> discontinuedDate;
    	private Optional<Company> company;
    	
    	public ComputerBuilder (String name) {
    		this.name = name;
    	}
    	
    	public ComputerBuilder id (long id) {
    		this.id = id;
    		return this;
    	}
    	
    	public ComputerBuilder name (String name) {
    		this.name = name;
    		return this;
    	}
    	
    	public ComputerBuilder introducedDate (Optional<LocalDate> introducedDate) {
    		this.introducedDate = introducedDate;
    		return this;
    	}
    	
    	public ComputerBuilder discontinuedDate (Optional<LocalDate> discontinuedDate) {
    		this.discontinuedDate = discontinuedDate;
    		return this;
    	}
    	
    	public ComputerBuilder company (Optional<Company> company) {
    		this.company = company;
    		return this;
    	}
    	
    	public Computer build() {
    		return new Computer(this);
    	}
    }

}