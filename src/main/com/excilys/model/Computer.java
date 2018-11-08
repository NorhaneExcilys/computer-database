package com.excilys.model;

import java.time.LocalDate;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

@Entity(name="computer")
public class Computer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "introduced")
	private LocalDate introducedDate;
	
	@Column(name = "discontinued")
	private LocalDate discontinuedDate;
	
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
	
	public Computer() {
		
	}
	
	/**
	 * builds Computer defined by a computerBuilder
	 * @param computerBuilder the companyBuilder
	 */
	private Computer(ComputerBuilder computerBuilder) {
		this.id = computerBuilder.id;
		this.name = computerBuilder.name;
		this.introducedDate = computerBuilder.introducedDate.isPresent() ? computerBuilder.introducedDate.get() : null;
		this.discontinuedDate = computerBuilder.discontinuedDate.isPresent() ? computerBuilder.discontinuedDate.get() : null;
		this.company = computerBuilder.company.isPresent() ? computerBuilder.company.get() : null;
	}
	
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public Optional<LocalDate> getIntroducedDate() {
		return Optional.ofNullable(introducedDate);
	}
	
	public Optional<LocalDate> getDiscontinuedDate() {
		return Optional.ofNullable(discontinuedDate);
	}
	
	public Optional<Company> getCompany() {
		return Optional.ofNullable(company);
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