package model;

/**
 * <b>Company is the class that represent a company.</b>
 * A company is characterized by the following informations:
 * <ul>
 * <li> A unique identifier assigned automatically.</li>
 * <li> A name.</li>
 * </ul>
 * @author elgharbi
 *
 */

public class Company {

	/**
	 * contains the id of this company 
	 */
	private long id;
	/**
	 * contains the name of this company 
	 */
	private String name;
	
	/** builds Company defined by its id and name
	 * @param id the id of the company
	 * @param name the name of the company
	 */
	public Company(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	/**
     * Return the id of this company.
     * @return The id of this company.
     */
	public long getId() {
		return id;
	}
	
	/**
     * Return the name of this company.
     * @return The name of this company.
     */
	public String getName() {
		return name;
	}
	
	/**
	 * change the actual name of this company
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

    /**
     * return a string representation of the company.
     * @return A string representation of the company.
     */
	public String toString() {
		return id + " " + name;
	}
	
}
