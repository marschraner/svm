package ch.metzenthin.svm.persistence;

import jakarta.persistence.EntityManager;

/**
 * @author Martin Schraner
 */
public interface DB {

    EntityManager getCurrentEntityManager();

    void closeSession();
}
