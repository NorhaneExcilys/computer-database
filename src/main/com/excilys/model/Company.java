package com.excilys.model;

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

	private long id;
	private String name;
	
	/** builds Company defined by its id and name
	 * @param id the id of the company
	 * @param name the name of the company
	 */
	public Company(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + "]";
	}
	
}
