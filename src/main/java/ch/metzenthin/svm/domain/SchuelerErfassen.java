package ch.metzenthin.svm.domain;

import ch.metzenthin.svm.model.Adresse;
import ch.metzenthin.svm.model.Angehoeriger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 *
 * @author Martin Schraner
 */
public class SchuelerErfassen {

    private EntityManagerFactory entityManagerFactory;

    public SchuelerErfassen(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void saveAdresse(Adresse adresse) {

        EntityManager entityManager = null;
        EntityTransaction transaction = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            entityManager.persist(adresse);

            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }

        finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

    }

    public void saveAngehoeriger(Angehoeriger angehoeriger, Adresse adresse) {

        EntityManager entityManager = null;
        EntityTransaction transaction = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            angehoeriger.setAdresse(adresse);
            entityManager.persist(angehoeriger);

            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }

        finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

    }
}
