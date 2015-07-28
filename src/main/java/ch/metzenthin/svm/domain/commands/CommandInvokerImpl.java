package ch.metzenthin.svm.domain.commands;

import org.apache.log4j.Logger;
import org.hibernate.StaleObjectStateException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.swing.*;

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
    public GenericDaoCommand executeCommandAsTransactionWithOpenAndClose(GenericDaoCommand genericDaoCommand) {
        LOGGER.trace("executeCommandAsTransactionWithOpenAndClose aufgerufen");
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
                entityManagerFactory.close();
            }
        }
        return genericDaoCommand;
    }

    @Override
    public GenericDaoCommand executeCommandAsTransaction(GenericDaoCommand genericDaoCommand) {
        LOGGER.trace("executeCommandAsTransaction aufgerufen");
        try {
            entityManager.getTransaction().begin();
            genericDaoCommand.setEntityManager(entityManager);
            genericDaoCommand.execute();
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
            if (e instanceof StaleObjectStateException) {
                JOptionPane.showMessageDialog(null, "Speichern / löschen fehlgeschlagen, da das Objekt inzwischen auf der Datenbank verändert wurde. Die Applikation muss neu gestartet werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
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
    public GenericDaoCommand executeCommand(GenericDaoCommand genericDaoCommand) {
        LOGGER.trace("executeCommand aufgerufen");
        genericDaoCommand.setEntityManager(entityManager);
        genericDaoCommand.execute();
        LOGGER.trace("executeCommand durchgeführt");
        return genericDaoCommand;
    }

    @Override
    public void clear() {
        LOGGER.trace("clear aufgerufen");
        closeSession();
        openSession();
        entityManager.clear();
//        entityManagerFactory.getCache().evictAll();
    }

}
