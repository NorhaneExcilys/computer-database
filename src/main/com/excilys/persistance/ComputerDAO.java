package com.excilys.persistance;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.exception.UnknowComputerException;
import com.excilys.model.Computer;
import com.excilys.model.Paging;
import com.excilys.model.QComputer;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.excilys.exception.DatabaseException;
import com.excilys.exception.UnknowCompanyException;

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

	private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager tx;
    
	private QComputer qComputer;

    @Autowired
    public ComputerDAO(EntityManagerFactory entityManagerFactory) {
    	qComputer = QComputer.computer;	
    	this.entityManager = entityManagerFactory.createEntityManager();
    }
 
	/**
	 * return the number of computer in the database computer
	 * @return the number of computer in the database computer
	 * @throws DatabaseException
	 */
	public long getCount() throws DatabaseException {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		return query.selectFrom(qComputer).fetchCount();
	}
	
	/**
	 * return the number of computer in the database computer filter by name
	 * @param word the filter of name
	 * @return the number of computer in the database computer filter by name
	 * @throws DatabaseException
	 */
	public long getCountBySearchedWord(String word, Paging paging) throws DatabaseException {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		return query
				.selectFrom(qComputer)
				.where(qComputer.name.like("%" + word + "%").or(qComputer.company.name.like("%" + word + "%")))
				.limit(paging.getComputersPerPage())
				.offset(paging.getComputersPerPage() * (paging.getCurrentPage()-1))
				.fetchCount();
	}
	
	/**
	 * return the list of computer for a given page
	 * @param paging the actual paging
	 * @return the list of the computer for a given page
	 * @throws DatabaseException
	 * @throws UnknowCompanyException
	 */
	public List<Computer> getByPage(Paging paging) throws DatabaseException {		
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		return query
				.selectFrom(qComputer)
				.limit(paging.getComputersPerPage())
				.offset(paging.getComputersPerPage() * (paging.getCurrentPage()-1))
				.fetch();
	}

	/**
	 * return the computer chosen by identifier
	 * @param id the identifier of the computer
	 * @return the computer chosen by identifier
	 * @throws DatabaseException
	 */
	public Optional<Computer> getById(long id) throws DatabaseException {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		return Optional.of(query
				.selectFrom(qComputer)
				.where(qComputer.id.eq(id))
				.fetchOne());
	}
	
	/**
	 * return the list of computer filter by name
	 * @param word the filter of name
	 * @return the list of computer filter by name
	 * @throws DatabaseException
	 */
	public List<Computer> getBySearchedWord(String word, Paging paging) throws DatabaseException {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		return query
				.selectFrom(qComputer)
				.where(qComputer.name.like("%" + word + "%").or(qComputer.company.name.like("%" + word + "%")))
				.limit(paging.getComputersPerPage())
				.offset(paging.getComputersPerPage() * (paging.getCurrentPage()-1))
				.fetch();	
	}
	
	/**
	 * Adds a computer to the database
	 * @param computer the added computer
	 * @return true if the computer is added and false if not
	 * @throws DatabaseException 
	 */
	public boolean addComputer(Computer computer) throws DatabaseException {
		entityManager.getTransaction().begin();
		entityManager.persist(computer);
		entityManager.getTransaction().commit();
		
		return true;
	}
	
	/**
	 * Deletes computers to the database
	 * @param idList the list of the id of the computers to delete separated by ","
	 * @return true if the computers are deleted and false if not
	 * @throws DatabaseException 
	 * @throws UnknowComputerException 
	 */
	public boolean deleteComputerByList(String strIdList) throws DatabaseException, UnknowComputerException {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		String[] idList = strIdList.split(",");
		BooleanBuilder predicate = new BooleanBuilder();
	    for (String strId : idList){
	    	predicate.or(qComputer.id.eq(Long.valueOf(strId)));
	    }
	    entityManager.getTransaction().begin();
		boolean queryResult = query
		    .delete(qComputer)
		    .where(predicate)
			.execute() == idList.length;
		entityManager.getTransaction().commit();
		return queryResult;
	}

	/**
	 * Updates a computer to the database
	 * @param computer the updated computer
	 * @return true if the computer is updated and false if not
	 * @throws DatabaseException 
	 */
	public boolean updateComputerById(Computer computer) throws DatabaseException {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		entityManager.getTransaction().begin();
		boolean queryResult = query
		    .update(qComputer)
			.where(qComputer.id.eq(computer.getId()))
			.set(qComputer.name, computer.getName())
			.set(qComputer.introducedDate, computer.getIntroducedDate().isPresent() ? computer.getIntroducedDate().get() : null)
			.set(qComputer.discontinuedDate, computer.getDiscontinuedDate().isPresent() ? computer.getDiscontinuedDate().get() : null)
			.set(qComputer.company, computer.getCompany().isPresent() ? computer.getCompany().get() : null)
			.execute() == 1;
		entityManager.getTransaction().commit();
		return queryResult;
	}
	
	
}