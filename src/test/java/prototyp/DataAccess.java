package prototyp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class DataAccess {

	public DataAccess() throws Exception {
		setUp();
	}

	private EntityManagerFactory entityManagerFactory;

	protected void setUp() throws Exception {
		// like discussed with regards to SessionFactory, an EntityManagerFactory is set up once for an application
		// 		IMPORTANT: notice how the name here matches the name we gave the persistence-unit in persistence.xml!
		entityManagerFactory = Persistence.createEntityManagerFactory( "prototyp" );
	}

	protected void tearDown() throws Exception {
		if ( entityManagerFactory != null ) {
			entityManagerFactory.close();
		}
	}

	@SuppressWarnings({ "unchecked" })
	public List<Person> query() throws Exception {
		// lets pull persons from the database and list them
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
        List<Person> result = entityManager.createQuery( "from Person" ).getResultList();
		for ( Person person : (List<Person>) result ) {
			System.out.println( "Person (" + person.toString() + ")" );
		}
		entityManager.getTransaction().commit();
		entityManager.close();
        return result;
	}

	public void persist(Person person) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(person);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
