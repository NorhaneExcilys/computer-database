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
	private int _id;
	/**
	 * contains the name of this computer 
	 */
	private String _name;
	/**
	 * contains the introduced date of this computer 
	 */
	private Date _introducedDate;
	/**
	 * contains the discontinued date of this computer 
	 */
	private Date _discontinuedDate;
	/**
	 * contains the company identifier of this computer 
	 */
	private int _companyId;
	
	/**
	 * builds Computer defined by its name, introducedDate, discontinuedDate, companyId
	 * @param name the name of the computer
	 * @param introducedDate the introduced date of the computer
	 * @param discontinuedDate the discontinued date of the computer
	 * @param companyId the company identifier of this computer
	 */
	public Computer(String name, Date introducedDate, Date discontinuedDate, int companyId) {
		_name = name;
		_introducedDate = introducedDate;
		_discontinuedDate = discontinuedDate;
		_companyId = companyId;
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
		_id = id;
		_name = name;
		_introducedDate = introducedDate;
		_discontinuedDate = discontinuedDate;
		_companyId = companyId;
	}
	
	/**
     * Return the id of this computer.
     * @return The id of this computer.
     */
	public int getId() {
		return _id;
	}

	/**
     * Return the name of this computer.
     * @return The name of this computer.
     */
	public String getName() {
		return _name;
	}
	
	/**
	 * change the actual name of this computer
	 * @param name the new name
	 */
	public void setName(String name) {
		_name = name;
	}
	
	/**
	 * Return the introduced date of this computer.
	 * @return The introduced date of this computer.
	 */
	public Date getIntroducedDate() {
		return _introducedDate;
	}
	
	/**
	 * change the actual introduced date of this computer
	 * @param introducedDate the new introduced date
	 */
	public void setIntroducedDate(Date introducedDate) {
		_introducedDate = introducedDate;
	}
	
	/**
	 * Return the discontinued date of this computer.
	 * @return The discontinued date of this computer.
	 */
	public Date getDiscontinuedDate() {
		return _discontinuedDate;
	}
	
	/**
	 * change the actual discontinued date of this computer
	 * @param discontinuedDate the new discontinued date
	 */
	public void setDiscontinuedDate(Date discontinuedDate) {
		_discontinuedDate = discontinuedDate;
	}
	
	/**
	 * Return the company identifier of this computer.
	 * @return the company identifier of this computer.
	 */
	public int getCompanyId() {
		return _companyId;
	}
	
	/**
	 * change the actual company identifier of this computer
	 * @param companyId the new company identifier
	 */
	public void setCompanyId(int companyId) {
		_companyId = companyId;
	}
	
    /**
     * return a string representation of the computer.
     * @return A string representation of the computer.
     */
	public String toString() {
		return _id + " " + _name + " " + _introducedDate + " " + _discontinuedDate + " " + _companyId;
	}
	
    /**
     * return a detailed string representation of the computer.
     * @return A string representation of the computer.
     */
	public String getDetail() {
		return "Number: " +_id + " Name: " + _name + " Introduced date: " + _introducedDate + " DiscontinuedDate: " + _discontinuedDate + " Company Number: " + _companyId;
	}

}
