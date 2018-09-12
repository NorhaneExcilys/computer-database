package model;

import java.util.Date;

public class Computer {

	private int _id;
	private String _name;
	private Date _introducedDate;
	private Date _discontinuedDate;
	private int _companyId;
	
	public Computer(String name, Date introducedDate, Date discontinuedDate, int companyId) {
		_name = name;
		_introducedDate = introducedDate;
		_discontinuedDate = discontinuedDate;
		_companyId = companyId;
	}
	
	public Computer(int id, String name, Date introducedDate, Date discontinuedDate, int companyId) {
		_id = id;
		_name = name;
		_introducedDate = introducedDate;
		_discontinuedDate = discontinuedDate;
		_companyId = companyId;
	}
	
	public int getId() {
		return _id;
	}
	
	public void setId(int id) {
		_id = id;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public Date getIntroducedDate() {
		return _introducedDate;
	}
	
	public void setIntroducedDate(Date introducedDate) {
		_introducedDate = introducedDate;
	}
	
	public Date getDiscontinuedDate() {
		return _discontinuedDate;
	}
	
	public void setDiscontinuedDate(Date discontinuedDate) {
		_discontinuedDate = discontinuedDate;
	}
	
	public int getCompanyId() {
		return _companyId;
	}
	
	public void setCompanyId(int companyId) {
		_companyId = companyId;
	}
	
	public String toString() {
		return _id + " " + _name + " " + _introducedDate + " " + _discontinuedDate + " " + _companyId;
	}
	
	public String getDetail() {
		return "Number: " +_id + " Name: " + _name + " Introduced date: " + _introducedDate + " DiscontinuedDate: " + _discontinuedDate + " Company Number: " + _companyId;
	}

}
