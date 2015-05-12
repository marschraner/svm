package ch.metzenthin.svm.commands;

import javax.persistence.EntityManager;

/**
 * @author Hans Stamm
 */
public abstract class GenericDaoCommand implements Command {

    protected EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
