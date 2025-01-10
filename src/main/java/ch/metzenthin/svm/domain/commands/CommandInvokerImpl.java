package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.SvmRuntimeException;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.StaleObjectStateException;

/**
 * @author Hans Stamm
 */
public class CommandInvokerImpl implements CommandInvoker {

    private static final Logger LOGGER = LogManager.getLogger(CommandInvokerImpl.class);

    private final DB db = DBFactory.getInstance();

    @Override
    public Command executeCommand(Command command) {
        LOGGER.trace("executeCommand aufgerufen");
        command.execute();
        LOGGER.trace("executeCommand durchgeführt");
        return command;
    }

    @Override
    public Command executeCommandAsTransaction(Command command) {
        LOGGER.trace("executeCommandAsTransaction aufgerufen");
        EntityManager entityManager = db.getCurrentEntityManager();
        try {
            entityManager.getTransaction().begin();
            command.execute();
            entityManager.getTransaction().commit();
            LOGGER.trace("executeCommandAsTransaction durchgeführt");
        } catch (Exception e) {
            LOGGER.error("Fehler in executeCommandAsTransaction(Command)", e);
            if (entityManager.isOpen() && entityManager.getTransaction().isActive()) {
                LOGGER.trace("Rollback wird durchgeführt executeCommandAsTransaction(Command)", e);
                entityManager.getTransaction().rollback();
                LOGGER.trace("Rollback ist durchgeführt executeCommandAsTransaction(Command)", e);
            }
            if (e instanceof StaleObjectStateException || e instanceof OptimisticLockException) {
                throw new SvmRuntimeException("Speichern / löschen fehlgeschlagen, da das Objekt inzwischen auf der Datenbank verändert wurde.", e);
            }
            throw e;
        }
        return command;
    }
}
