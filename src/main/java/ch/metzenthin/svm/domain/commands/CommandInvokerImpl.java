package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.SvmDbException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 * @author Hans Stamm
 */
public class CommandInvokerImpl implements CommandInvoker {

    private EntityManagerFactory entityManagerFactory;
    EntityManager entityManager = null;

    public CommandInvokerImpl() {
    }

    public CommandInvokerImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Command executeCommand(Command command) {
        command.execute();
        return command;
    }

    @Override
    public GenericDaoCommand executeCommand(GenericDaoCommand genericDaoCommand) throws SvmDbException {
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            genericDaoCommand.setEntityManager(entityManager);
            genericDaoCommand.execute();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new SvmDbException("Fehler beim Ausführen eines DB-Commands", e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return genericDaoCommand;
    }

    public void beginTransaction() {
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
        } catch (RuntimeException e) {
            EntityTransaction tx = entityManager.getTransaction();
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public void commitTransaction() {
        try {
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            EntityTransaction tx = entityManager.getTransaction();
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public void rollbackTransaction() {
        try {
            entityManager.getTransaction().rollback();
        } catch (RuntimeException e) {
            EntityTransaction tx = entityManager.getTransaction();
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public GenericDaoCommand executeCommandWithinTransaction(GenericDaoCommand genericDaoCommand) {
        try {
            genericDaoCommand.setEntityManager(entityManager);
            genericDaoCommand.execute();
        } catch (Throwable e) {
            rollbackTransaction();
            throw e;
        }
        return genericDaoCommand;
    }

}
