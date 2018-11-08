package com.excilys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

@Entity(name="company")
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "name")
	private String name;
	
	public Company() {
		
	}
	
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
