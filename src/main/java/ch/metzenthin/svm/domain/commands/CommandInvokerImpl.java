package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.SvmDbException;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author Hans Stamm
 */
public class CommandInvokerImpl implements CommandInvoker {

    private static final Logger LOGGER = Logger.getLogger(CommandInvokerImpl.class);

    private EntityManagerFactory entityManagerFactory = null;
    private EntityManager entityManager = null;

    @Override
    public Command executeCommand(Command command) {
        command.execute();
        return command;
    }

    @Override
    public GenericDaoCommand executeCommand(GenericDaoCommand genericDaoCommand) throws SvmDbException {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("svm");
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            genericDaoCommand.setEntityManager(entityManager);
            genericDaoCommand.execute();
            tx.commit();
        } catch (RuntimeException e) {
            LOGGER.error("Fehler in executeCommand(GenericDaoCommand)", e);
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new SvmDbException("Fehler beim Ausführen eines DB-Commands", e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
                entityManagerFactory.close();
            }
        }
        return genericDaoCommand;
    }

    @Override
    public void beginTransaction() {
        LOGGER.trace("beginTransaction aufgerufen");
        try {
            if (entityManager == null || !entityManager.isOpen()) {
                entityManagerFactory = Persistence.createEntityManagerFactory("svm");
                entityManager = entityManagerFactory.createEntityManager();
            }
            entityManager.getTransaction().begin();
            LOGGER.trace("beginTransaction durchgeführt");
        } catch (RuntimeException e) {
            LOGGER.error("Fehler in beginTransaction()", e);
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
            LOGGER.error("Fehler in commitTransaction()", e);
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
            LOGGER.error("Fehler beim Rollback", e);
            EntityTransaction tx = entityManager.getTransaction();
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
                entityManager = null;
                entityManagerFactory.close();
                entityManagerFactory = null;
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
            LOGGER.error("Fehler in executeCommandWithinTransaction(GenericDaoCommand)", e);
            rollbackTransaction();
            throw e;
        }
        return genericDaoCommand;
    }

    @Override
    public void openSession() {
        LOGGER.trace("openSession aufgerufen");
        if (entityManager == null || !entityManager.isOpen()) {
            entityManagerFactory = Persistence.createEntityManagerFactory("svm");
            entityManager = entityManagerFactory.createEntityManager();
        }
    }

    @Override
    public void closeSession() {
        LOGGER.trace("closeSession aufgerufen");
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
            entityManager = null;
            entityManagerFactory.close();
            entityManagerFactory = null;
        }
    }

    @Override
    public GenericDaoCommand executeCommandWithinSession(GenericDaoCommand genericDaoCommand) {
        LOGGER.trace("executeCommandWithinSession aufgerufen");
        genericDaoCommand.setEntityManager(entityManager);
        genericDaoCommand.execute();
        LOGGER.trace("executeCommandWithinSession durchgeführt");
        return genericDaoCommand;
    }

}
