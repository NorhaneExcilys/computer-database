package com.excilys.persistance;

import java.time.LocalDate;
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

import com.excilys.exception.UnknowComputerException;
import com.excilys.model.Computer;
import com.excilys.model.Paging;
import com.excilys.model.QComputer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.excilys.exception.DatabaseException;
import com.excilys.exception.UnknowCompanyException;
import com.excilys.model.Company;

/**
 * <b>DAOComputer is the class that enables to performs action on the database computer.</b>
 * A DAOComputer is characterized by the following informations:
 * <ul>
 * <li> A daoCompany</li>
 * <li> A daoComputer</li>
 * <li> A dao</li>
 * </ul>
 * @author elgharbi
 *
 */
@Repository
@Transactional
public class ComputerDAO {
	
	private final static String GET_COUNT = "SELECT COUNT(id) AS count FROM computer";
	private final static String GET_COUNT_BY_SEARCHED_WORD = "SELECT COUNT(id) AS count FROM computer WHERE name LIKE ?;";	
	private final static String GET_BY_PAGE = "SELECT id, name, introduced, discontinued, company_id FROM computer LIMIT ? OFFSET ?;";
	private final static String GET_BY_ID = "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE id = ?;";
	private final static String GET_BY_SEARCHED_WORD = "SELECT id, name, introduced, discontinued, company_id FROM computer WHERE name LIKE ? LIMIT ? OFFSET ?;";
	private final static String ADD = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUE (?, ?, ?, ?);";
	private final static String UPDATE = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;";
	private final static String DELETE_BY_LIST = "DELETE FROM computer WHERE id IN (%s)";

	@Autowired
	private ComputerRowMapper computerRowMapper;
	
    private JdbcTemplate jdbcTemplate;
    
    private SessionFactory sessionFactory;
    
    @Autowired
    private HibernateTransactionManager tx;
    
	private QComputer qComputer;


    @Autowired
    public ComputerDAO(DataSource dataSource, SessionFactory sessionFactory) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.sessionFactory = sessionFactory;
    	qComputer = QComputer.computer;	
    }
 
	/**
	 * return the number of computer in the database computer
	 * @return the number of computer in the database computer
	 * @throws DatabaseException
	 */
	public int getCount() throws DatabaseException {
		Session session = sessionFactory.getCurrentSession();
		JPAQueryFactory query = new JPAQueryFactory(session);
		QComputer computer = QComputer.computer;
		return (int) query.selectFrom(computer).fetchCount();
	}
	
	/**
	 * return the number of computer in the database computer filter by name
	 * @param word the filter of name
	 * @return the number of computer in the database computer filter by name
	 * @throws DatabaseException
	 */
	public int getCountBySearchedWord(String word) throws DatabaseException {
		try {
			return jdbcTemplate.queryForObject(GET_COUNT_BY_SEARCHED_WORD, Integer.class, "%" + word + "%");
		}
		catch (DataAccessException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/**
	 * return the list of computer for a given page
	 * @param paging the actual paging
	 * @return the list of the computer for a given page
	 * @throws DatabaseException
	 * @throws UnknowCompanyException
	 */
	public List<Computer> getByPage(Paging paging) throws DatabaseException {		
		Session session = sessionFactory.getCurrentSession();
		JPAQueryFactory query = new JPAQueryFactory(session);
		return query.selectFrom(qComputer).limit(paging.getComputersPerPage()).offset(paging.getComputersPerPage() * (paging.getCurrentPage()-1)).fetch();
	}

	/**
	 * return the computer chosen by identifier
	 * @param id the identifier of the computer
	 * @return the computer chosen by identifier
	 * @throws DatabaseException
	 */
	public Optional<Computer> getById(long id) throws DatabaseException {
		Session session = sessionFactory.getCurrentSession();
		JPAQueryFactory query = new JPAQueryFactory(session);
		return Optional.of(query.selectFrom(qComputer).where(qComputer.id.eq(id)).fetchOne());
	}
	
	/**
	 * return the list of computer filter by name
	 * @param word the filter of name
	 * @return the list of computer filter by name
	 * @throws DatabaseException
	 */
	public List<Computer> getBySearchedWord(String word, Paging paging) throws DatabaseException {
		Object[] sqlParameter = {
				"%" + word + "%",
				paging.getComputersPerPage(),
				paging.getComputersPerPage() * (paging.getCurrentPage()-1)
		};
		
		try {	
			return jdbcTemplate.query(GET_BY_SEARCHED_WORD, sqlParameter, computerRowMapper);
		} catch (DataAccessException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/**
	 * Adds a computer to the database
	 * @param computer the added computer
	 * @return true if the computer is added and false if not
	 * @throws DatabaseException 
	 */
	public boolean addComputer(Computer computer) throws DatabaseException {
		String name = computer.getName();
		Optional<LocalDate> introducedDate = computer.getIntroducedDate();
		Optional<LocalDate> discontinuedDate = computer.getDiscontinuedDate();
		Optional<Company> company = computer.getCompany();
		
		Object[] sqlParameter = {
				name,
				introducedDate.isPresent() ? java.sql.Date.valueOf(introducedDate.get()) : null,
				discontinuedDate.isPresent() ? java.sql.Date.valueOf(discontinuedDate.get()) : null,
				company.isPresent() ? company.get().getId() : null
		};
		
		try {	
			int queryResult = jdbcTemplate.update(ADD, sqlParameter);
			return (queryResult == 1);
		} catch (DataAccessException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/**
	 * Deletes computers to the database
	 * @param idList the list of the id of the computers to delete separated by ","
	 * @return true if the computers are deleted and false if not
	 * @throws DatabaseException 
	 * @throws UnknowComputerException 
	 */
	public boolean deleteComputerByList(String idList) throws DatabaseException, UnknowComputerException {
		Session session = sessionFactory.getCurrentSession();
		JPAQueryFactory query = new JPAQueryFactory(session);
		query
		    .delete(qComputer)
		    .where(qComputer.id.eq(1L))
			.execute();
		
		return true;
	}

	/**
	 * Updates a computer to the database
	 * @param computer the updated computer
	 * @return true if the computer is updated and false if not
	 * @throws DatabaseException 
	 */
	public boolean updateComputerById(Computer computer) throws DatabaseException {
		Session session = sessionFactory.getCurrentSession();
		JPAQueryFactory query = new JPAQueryFactory(session);
		return query
		    .update(qComputer)
			.where(qComputer.id.eq(computer.getId()))
			.set(qComputer.name, computer.getName())
			.set(qComputer.introducedDate, computer.getIntroducedDate().isPresent() ? computer.getIntroducedDate().get() : null)
			.set(qComputer.discontinuedDate, computer.getDiscontinuedDate().isPresent() ? computer.getDiscontinuedDate().get() : null)
			.set(qComputer.company, computer.getCompany().isPresent() ? computer.getCompany().get() : null)
			.execute() == 1;
	}
	
	
}