package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.SvmDbException;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 * @author Hans Stamm
 */
public class CommandInvokerImpl implements CommandInvoker {

    private static final Logger LOGGER = Logger.getLogger(CommandInvokerImpl.class);

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager = null;

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

    @Override
    public void beginTransaction() {
        LOGGER.trace("beginTransaction aufgerufen");
        try {
            if (entityManager == null) {
                entityManager = entityManagerFactory.createEntityManager();
            }
            entityManager.getTransaction().begin();
            LOGGER.trace("beginTransaction durchgeführt");
        } catch (RuntimeException e) {
            EntityTransaction tx = null;
            if (entityManager != null) {
                tx = entityManager.getTransaction();
            }
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void commitTransaction() {
        LOGGER.trace("commitTransaction aufgerufen");
        try {
            entityManager.getTransaction().commit();
            LOGGER.trace("commitTransaction durchgeführt");
        } catch (RuntimeException e) {
            EntityTransaction tx = entityManager.getTransaction();
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void rollbackTransaction() {
        LOGGER.trace("rollbackTransaction aufgerufen");
        try {
            entityManager.getTransaction().rollback();
            LOGGER.trace("rollbackTransaction durchgeführt");
        } catch (RuntimeException e) {
            EntityTransaction tx = entityManager.getTransaction();
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    @Override
    public GenericDaoCommand executeCommandWithinTransaction(GenericDaoCommand genericDaoCommand) {
        LOGGER.trace("executeCommandWithinTransaction aufgerufen");
        try {
            genericDaoCommand.setEntityManager(entityManager);
            genericDaoCommand.execute();
            LOGGER.trace("executeCommandWithinTransaction durchgeführt");
        } catch (Throwable e) {
            rollbackTransaction();
            throw e;
        }
        return genericDaoCommand;
    }

    @Override
    public void openSession() {
        LOGGER.trace("openSession aufgerufen");
        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = entityManagerFactory.createEntityManager();
        }
    }

    @Override
    public void closeSession() {
        LOGGER.trace("closeSession aufgerufen");
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }

    @Override
    public GenericDaoCommand executeCommandWithinSession(GenericDaoCommand genericDaoCommand) {
        LOGGER.trace("executeCommandWithinSession aufgerufen");
        genericDaoCommand.setEntityManager(entityManager);
        genericDaoCommand.execute();
        LOGGER.trace("executeCommandWithinTransaction durchgeführt");
        return genericDaoCommand;
    }

}
