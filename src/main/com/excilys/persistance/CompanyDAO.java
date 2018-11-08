package com.excilys.persistance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.exception.DatabaseException;
import com.excilys.model.Company;
import com.excilys.model.QCompany;
import com.querydsl.jpa.impl.JPAQueryFactory;

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
@Transactional
public class CompanyDAO {
	
	private final static String GET_ALL = "SELECT id, name FROM company;";
	private final static String GET_BY_ID = "SELECT id, name FROM company WHERE id = ?;";
	private final static String DELETE_BY_ID = "DELETE FROM company WHERE id = ?;";
	private final static String DELETE_BY_COMPANY_ID = "DELETE FROM computer WHERE company_id = ?";
	
	@Autowired
	private CompanyRowMapper companyRowMapper;
	
	private SessionFactory sessionFactory;
	
    private JdbcTemplate jdbcTemplate;
    
    private QCompany qCompany;
    
    @Autowired
    private HibernateTransactionManager tx;

    @Autowired
    public CompanyDAO(DataSource dataSource, SessionFactory sessionFactory) {
    	jdbcTemplate = new JdbcTemplate(dataSource);
        this.sessionFactory = sessionFactory;
        qCompany = QCompany.company;
    }
    
	/**
	 * return the list of all companies in the database company
	 * @return the list of all companies
	 * @throws DatabaseException 
	 */
	public List<Company> getAll() throws DatabaseException {
		Session session = sessionFactory.getCurrentSession();
		JPAQueryFactory query = new JPAQueryFactory(session);
		return query.selectFrom(qCompany).fetch();
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
		/*try {
			int queryResult = tx.execute(new TransactionCallback<Integer>() {
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
		}*/
		return true;
	}
	
}