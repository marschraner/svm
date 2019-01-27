package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.SvmRuntimeException;
import ch.metzenthin.svm.common.utils.PersistenceProperties;
import org.apache.log4j.Logger;
import org.hibernate.StaleObjectStateException;

import javax.persistence.*;

/**
 * @author Hans Stamm
 */
public class CommandInvokerImpl implements CommandInvoker {

    private static final Logger LOGGER = Logger.getLogger(CommandInvokerImpl.class);

    private EntityManagerFactory entityManagerFactory = createEntityManagerFactory();
    private EntityManager entityManager = null;

    private static EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("svm", PersistenceProperties.getPersistenceProperties());
    }

    @Override
    public Command executeCommand(Command command) {
        command.execute();
        return command;
    }

    @Override
    public GenericDaoCommand executeCommand(GenericDaoCommand genericDaoCommand) {
        openSession();
        execute(entityManager, genericDaoCommand);
        return genericDaoCommand;
    }

    private void execute(EntityManager entityManager, GenericDaoCommand genericDaoCommand) {
        LOGGER.trace("executeCommand aufgerufen");
        genericDaoCommand.setEntityManager(entityManager);
        genericDaoCommand.execute();
        LOGGER.trace("executeCommand durchgeführt");
    }

    @Override
    public GenericDaoCommand executeCommandAsTransactionWithOpenAndClose(GenericDaoCommand genericDaoCommand) {
        LOGGER.trace("executeCommandAsTransactionWithOpenAndClose aufgerufen");
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            execute(entityManager, genericDaoCommand);
            tx.commit();
            LOGGER.trace("executeCommandAsTransactionWithOpenAndClose durchgeführt");
        } catch (RuntimeException e) {
            LOGGER.error("Fehler in executeCommandAsTransactionWithOpenAndClose(GenericDaoCommand)", e);
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return genericDaoCommand;
    }

    @Override
    public GenericDaoCommand executeCommandAsTransaction(GenericDaoCommand genericDaoCommand) {
        LOGGER.trace("executeCommandAsTransaction aufgerufen");
        try {
            openSession();
            entityManager.getTransaction().begin();
            execute(entityManager, genericDaoCommand);
            entityManager.getTransaction().commit();
            LOGGER.trace("executeCommandAsTransaction durchgeführt");
        }
        catch (Throwable e) {
            LOGGER.error("Fehler in executeCommandAsTransaction(GenericDaoCommand)", e);
            if ((entityManager != null) && entityManager.isOpen() && entityManager.getTransaction().isActive()) {
                LOGGER.trace("Rollback wird durchgeführt executeCommandAsTransaction(GenericDaoCommand)", e);
                entityManager.getTransaction().rollback();
                LOGGER.trace("Rollback ist durchgeführt executeCommandAsTransaction(GenericDaoCommand)", e);
            }
            if (e instanceof StaleObjectStateException || e instanceof OptimisticLockException) {
                throw new SvmRuntimeException("Speichern / löschen fehlgeschlagen, da das Objekt inzwischen auf der Datenbank verändert wurde.", e);
            }
            throw e;
        }
        return genericDaoCommand;
    }

    @Override
    public void openSession() {
        LOGGER.trace("openSession aufgerufen");
        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = entityManagerFactory.createEntityManager();
            LOGGER.trace("Session opened");
        }
    }

    @Override
    public void closeSession() {
        LOGGER.trace("closeSession aufgerufen");
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
            entityManager = null;
            LOGGER.trace("Session closed");
        }
    }

    @Override
    public void closeSessionAndEntityManagerFactory() {
        LOGGER.trace("closeSessionAndEntityManagerFactory aufgerufen");
        closeSession();
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
            entityManagerFactory = null;
        }
        LOGGER.trace("Session and entity manager factory closed");
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
