package com.excilys.persistance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.excilys.exception.DatabaseException;
import com.excilys.exception.UnknowCompanyException;
import com.excilys.model.Company;

/**
 * <b>CompanyDAO is the class that enables to performs action on the database computer.</b>
 * A CompanyDAO is characterized by the following informations:
 * <ul>
 * <li> A companyDAO</li>
 * <li> A connectionDAO</li>
 * </ul>
 * @author elgharbi
 *
 */
@Repository
public class CompanyDAO {

	private final static String GET_ALL = "SELECT id, name FROM company;";
	private final static String GET_BY_ID = "SELECT id, name FROM company WHERE id = ?;";
	private final static String DELETE_BY_ID = "DELETE FROM company WHERE id = ?;";
	private final static String DELETE_BY_COMPANY_ID = "DELETE FROM computer WHERE company_id = ?";
	
	@Autowired
	private CompanyRowMapper companyRowMapper;
	
    private JdbcTemplate jdbcTemplate;
    
    private TransactionTemplate transactionTemplate;
    
    private Logger logger = LoggerFactory.getLogger("AddComputer");
    
    @Autowired
    public CompanyDAO(DataSource dataSource, PlatformTransactionManager transactionManager) {
    	transactionTemplate = new TransactionTemplate(transactionManager);
        jdbcTemplate = new JdbcTemplate(dataSource);
        logger.debug(transactionTemplate.toString());
    }
    
	/**
	 * return the list of all companies in the database company
	 * @return the list of all companies
	 * @throws DatabaseException 
	 */
	public List<Company> getAll() throws DatabaseException {
		try {
			return jdbcTemplate.query(GET_ALL, companyRowMapper);
		} catch (DataAccessException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/**
	 * return the company chosen by identifier
	 * @param id the identifier of the company
	 * @return the company chosen by identifier
	 * @throws DatabaseException 
	 */
	public Optional<Company> getById(long id) throws DatabaseException {
		List<Company> companies = new ArrayList<Company>();
		try {
			companies = jdbcTemplate.query(GET_BY_ID, companyRowMapper, id);
		} catch (DataAccessException e) {
			throw new DatabaseException(e.getMessage());
		}
		
		if (companies.size() == 0) {
			return Optional.empty();
		}
		
		return Optional.of(companies.get(0));
	}
	
	/**
	 * delete a company by id and all the computer associate by this company
	 * @param id the identifier of the company to delete
	 * @return true if the company is well deleted, false otherwise
	 * @throws DatabaseException
	 */
	public boolean deleteById(long id) throws DatabaseException {
		try {
			int queryResult = transactionTemplate.execute(new TransactionCallback<Integer>() {
				@Override
				public Integer doInTransaction(TransactionStatus status) {
					jdbcTemplate.update(DELETE_BY_COMPANY_ID, id);
					int queryResult = jdbcTemplate.update(DELETE_BY_ID, id);
					if (queryResult < 1) {
						status.setRollbackOnly();
					}
					return queryResult;
				}});
			return (queryResult == 1);
		} catch (DataAccessException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
}