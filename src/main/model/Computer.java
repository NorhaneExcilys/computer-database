package model;

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
 * <li> A company identifier.</li>
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
	 * builds Computer defined by its name, introducedDate, discontinuedDate, companyId
	 * @param name the name of the computer
	 * @param introducedDate the introduced date of the computer
	 * @param discontinuedDate the discontinued date of the computer
	 * @param company the company of the computer
	 */
	private Computer(ComputerBuilder computerBuilder) {
		this.id = computerBuilder.id;
		this.name = computerBuilder.name;
		this.introducedDate = computerBuilder.introducedDate;
		this.discontinuedDate = computerBuilder.discontinuedDate;
		this.company = computerBuilder.company;
	}
	
	/**
	 * builds Computer defined by its id, name, introducedDate, discontinuedDate, companyId
	 * @param id the identifier of the computer
	 * @param name the name of the computer
	 * @param introducedDate the introduced date of the computer
	 * @param discontinuedDate the discontinued date of the computer
	 * @param company the company of the computer
	 */
	/*public Computer(long id, String name, Optional<LocalDate> introducedDate, Optional<LocalDate> discontinuedDate, Optional<Company> company) {
		this.id = id;
		this.name = name;
		this.introducedDate = introducedDate;
		this.discontinuedDate = discontinuedDate;
		this.company = company;
	}*/
	
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	/*public void setName(String name) {
		this.name = name;
	}*/
	
	public Optional<LocalDate> getIntroducedDate() {
		return introducedDate;
	}
	
	/*public void setIntroducedDate(Optional<LocalDate> introducedDate) {
		this.introducedDate = introducedDate;
	}*/
	
	public Optional<LocalDate> getDiscontinuedDate() {
		return discontinuedDate;
	}
	
	/*public void setDiscontinuedDate(Optional<LocalDate> discontinuedDate) {
		this.discontinuedDate = discontinuedDate;
	}*/
	
	public Optional<Company> getCompany() {
		return company;
	}

	/*public void setCompany(Optional<Company> company) {
		this.company = company;
	}*/
	
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
