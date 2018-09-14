package model;

import java.util.Date;

/**
 * <b>Computer is the class that represent a computer.</b>
 * A computer is characterized by the following informations:
 * <ul>
 * <li> A unique identifier assigned automatically.</li>
 * <li> A name.</li>
 * <li> An introduced date.</li>
 * <li> A discontinued date.</li>
 * <li> A company identifiant.</li>
 * </ul>
 * @author elgharbi
 *
 */

public class Computer {

	/**
	 * contains the id of this computer 
	 */
	private int id;
	/**
	 * contains the name of this computer 
	 */
	private String name;
	/**
	 * contains the introduced date of this computer 
	 */
	private Date introducedDate;
	/**
	 * contains the discontinued date of this computer 
	 */
	private Date discontinuedDate;
	/**
	 * contains the company identifier of this computer 
	 */
	private int companyId;
	
	/**
	 * builds Computer defined by its name, introducedDate, discontinuedDate, companyId
	 * @param name the name of the computer
	 * @param introducedDate the introduced date of the computer
	 * @param discontinuedDate the discontinued date of the computer
	 * @param companyId the company identifier of this computer
	 */
	public Computer(String name, Date introducedDate, Date discontinuedDate, int companyId) {
		this.name = name;
		this.introducedDate = introducedDate;
		this.discontinuedDate = discontinuedDate;
		this.companyId = companyId;
	}
	
	/**
	 * builds Computer defined by its id, name, introducedDate, discontinuedDate, companyId
	 * @param id the identifier of the computer
	 * @param name the name of the computer
	 * @param introducedDate the introduced date of the computer
	 * @param discontinuedDate the discontinued date of the computer
	 * @param companyId the company identifier of this computer
	 */
	public Computer(int id, String name, Date introducedDate, Date discontinuedDate, int companyId) {
		this.id = id;
		this.name = name;
		this.introducedDate = introducedDate;
		this.discontinuedDate = discontinuedDate;
		this.companyId = companyId;
	}
	
	/**
     * Return the id of this computer.
     * @return The id of this computer.
     */
	public int getId() {
		return id;
	}

	/**
     * Return the name of this computer.
     * @return The name of this computer.
     */
	public String getName() {
		return name;
	}
	
	/**
	 * change the actual name of this computer
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Return the introduced date of this computer.
	 * @return The introduced date of this computer.
	 */
	public Date getIntroducedDate() {
		return introducedDate;
	}
	
	/**
	 * change the actual introduced date of this computer
	 * @param introducedDate the new introduced date
	 */
	public void setIntroducedDate(Date introducedDate) {
		this.introducedDate = introducedDate;
	}
	
	/**
	 * Return the discontinued date of this computer.
	 * @return The discontinued date of this computer.
	 */
	public Date getDiscontinuedDate() {
		return discontinuedDate;
	}
	
	/**
	 * change the actual discontinued date of this computer
	 * @param discontinuedDate the new discontinued date
	 */
	public void setDiscontinuedDate(Date discontinuedDate) {
		this.discontinuedDate = discontinuedDate;
	}
	
	/**
	 * Return the company identifier of this computer.
	 * @return the company identifier of this computer.
	 */
	public int getCompanyId() {
		return companyId;
	}
	
	/**
	 * change the actual company identifier of this computer
	 * @param companyId the new company identifier
	 */
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	
    /**
     * return a string representation of the computer.
     * @return A string representation of the computer.
     */
	public String toString() {
		return id + " " + name + " " + introducedDate + " " + discontinuedDate + " " + companyId;
	}
	
    /**
     * return a detailed string representation of the computer.
     * @return A string representation of the computer.
     */
	public String getDetail() {
		return "Number: " + id + " Name: " + name + " Introduced date: " + introducedDate + " DiscontinuedDate: " + discontinuedDate + " Company Number: " + companyId;
	}

}
