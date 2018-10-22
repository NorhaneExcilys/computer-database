package com.excilys.persistance;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.model.Company;

@Component
public class CompanyRowMapper implements RowMapper<Company>  {

	@Override
	public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
		int currentId = rs.getInt("id");
		String currentName = rs.getString("name");
		return new Company(currentId, currentName);
	}

}