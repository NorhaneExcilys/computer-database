package com.excilys.model;

public class Paging {

	private int computersPerPage;
	private int currentPage;
	
	public Paging(int computersPerPage, int currentPage) {
		this.computersPerPage = computersPerPage;
		this.currentPage = currentPage;
	}
	
	public int getComputersPerPage() {
		return computersPerPage;
	}
	
	public void setComputersPerPage(int computersPerPage) {
		this.computersPerPage = computersPerPage;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
}
