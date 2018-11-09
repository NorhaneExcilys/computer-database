package com.excilys.persistance;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
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
	
	private EntityManager entityManager;
	
    @Autowired
    private PlatformTransactionManager tx;
    
    private QCompany qCompany;

    @Autowired
    public CompanyDAO(EntityManagerFactory entityManagerFactory) {
        qCompany = QCompany.company;
        entityManager = entityManagerFactory.createEntityManager();
    }
    
	/**
	 * return the list of all companies in the database company
	 * @return the list of all companies
	 * @throws DatabaseException 
	 */
	public List<Company> getAll() throws DatabaseException {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		return query.selectFrom(qCompany).fetch();
	}
	
	/**
	 * return the company chosen by identifier
	 * @param id the identifier of the company
	 * @return the company chosen by identifier
	 * @throws DatabaseException 
	 */
	public Optional<Company> getById(long id) throws DatabaseException {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		return Optional.of(query.selectFrom(qCompany).where(qCompany.id.eq(id)).fetchOne());
	}
	
	/**
	 * delete a company by id and all the computer associate by this company
	 * @param id the identifier of the company to delete
	 * @return true if the company is well deleted, false otherwise
	 * @throws DatabaseException
	 */
	public boolean deleteById(long id) throws DatabaseException {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		entityManager.getTransaction().begin();
		boolean queryResult = query
		    .delete(qCompany)
		    .where(qCompany.id.eq(id))
			.execute() == 1;
		entityManager.getTransaction().commit();
		return queryResult;
	}
	
}