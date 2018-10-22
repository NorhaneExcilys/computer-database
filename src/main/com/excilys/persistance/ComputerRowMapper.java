package com.excilys.persistance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.exception.DatabaseException;
import com.excilys.exception.UnknowCompanyException;
import com.excilys.model.Company;
import com.excilys.model.Computer;

@Component
public class ComputerRowMapper implements RowMapper<Computer> {

	@Autowired
	private CompanyDAO companyDAO;
	
	@Override
	public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
		int currentId = rs.getInt("id");
		String currentName = rs.getString("name");
		Optional<LocalDate> introducedDate = sqlDateToLocalDate(rs, "introduced");
		Optional<LocalDate> discontinuedDate = sqlDateToLocalDate(rs, "discontinued");
		int currentCompanyId = rs.getInt("company_id");
		Optional<Company> company = Optional.empty();
		try {
			company = currentCompanyId > 0 ? companyDAO.getById(currentCompanyId) : Optional.empty();
		} catch (DatabaseException e) {
			throw new SQLException();
		} catch (UnknowCompanyException e) {
			throw new SQLException();
		}
		Computer computer = new Computer.ComputerBuilder(currentName).id(currentId).introducedDate(introducedDate).discontinuedDate(discontinuedDate).company(company).build();
		
		return computer;
	}
	
	private Optional<LocalDate> sqlDateToLocalDate(ResultSet queryResult, String field) throws SQLException {
		return queryResult.getDate(field) == null ? Optional.empty() : Optional.of(queryResult.getDate(field).toLocalDate());
	}

}
