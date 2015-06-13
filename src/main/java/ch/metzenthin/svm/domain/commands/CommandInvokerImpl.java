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
        System.out.println("commitTransaction aufgerufen");
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
        System.out.println("commitTransaction aufgerufen");
        try {
            entityManager.getTransaction().commit();
            System.out.println("commitTransaction durchgeführt");
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
        System.out.println("rollbackTransaction aufgerufen");
        try {
            entityManager.getTransaction().rollback();
            System.out.println("rollbackTransaction durchgeführt");
        } catch (RuntimeException e) {
            EntityTransaction tx = entityManager.getTransaction();
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            if ((entityManager != null) && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    public GenericDaoCommand executeCommandWithinTransaction(GenericDaoCommand genericDaoCommand) {
        System.out.println("executeCommandWithinTransaction aufgerufen");
        try {
            genericDaoCommand.setEntityManager(entityManager);
            genericDaoCommand.execute();
            System.out.println("executeCommandWithinTransaction durchgeführt");
        } catch (Throwable e) {
            rollbackTransaction();
            throw e;
        }
        return genericDaoCommand;
    }

}
