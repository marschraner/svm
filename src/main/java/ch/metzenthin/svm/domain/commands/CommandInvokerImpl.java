package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.SvmRuntimeException;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import org.apache.log4j.Logger;
import org.hibernate.StaleObjectStateException;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;

/**
 * @author Hans Stamm
 */
public class CommandInvokerImpl implements CommandInvoker {

    private static final Logger LOGGER = Logger.getLogger(CommandInvokerImpl.class);

    private final DB db = DBFactory.getInstance();

    @Override
    public Command executeCommand(Command command) {
        command.execute();
        return command;
    }

    @Override
    public GenericDaoCommand executeCommand(GenericDaoCommand genericDaoCommand) {
        LOGGER.trace("executeCommand aufgerufen");
        genericDaoCommand.execute();
        LOGGER.trace("executeCommand durchgeführt");
        return genericDaoCommand;
    }

    @Override
    public GenericDaoCommand executeCommandAsTransaction(GenericDaoCommand genericDaoCommand) {
        LOGGER.trace("executeCommandAsTransaction aufgerufen");
        EntityManager entityManager = db.getCurrentEntityManager();
        try {
            entityManager.getTransaction().begin();
            genericDaoCommand.execute();
            entityManager.getTransaction().commit();
            LOGGER.trace("executeCommandAsTransaction durchgeführt");
        } catch (Throwable e) {
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
}
