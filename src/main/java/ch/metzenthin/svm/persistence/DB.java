package ch.metzenthin.svm.persistence;

import javax.persistence.EntityManager;

/**
 * @author Martin Schraner
 */
public interface DB {

    EntityManager getCurrentEntityManager();

    void closeSession();
}
