package ch.metzenthin.svm.persistence;

import ch.metzenthin.svm.common.utils.PersistenceProperties;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Singelton zur Verwaltung der EntityManagerFactory (global) und des EntityManager (Thread-basiert).
 *
 * @author Martin Schraner
 */
public class DBImpl implements DB {

    private static final Logger LOGGER = LogManager.getLogger(DBImpl.class);

    private volatile static DBImpl instance;
    private static EntityManagerFactory entityManagerFactory;

    private final ThreadLocal<ThreadLocalData> threadLocal = new ThreadLocal<>();

    private DBImpl() {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory(
                    "svm", PersistenceProperties.getPersistenceProperties());
        } catch (Exception e) {
            LOGGER.error("", e);
            throw e;
        }
    }

    static DBImpl getInstance() {
        if (instance == null) {
            synchronized (DBImpl.class) {
                if (instance == null) {
                    instance = new DBImpl();
                }
            }
        }
        return instance;
    }

    private void setThreadLocalData(ThreadLocalData threadLocalData) {
        threadLocal.set(threadLocalData);
    }

    private ThreadLocalData getThreadLocalData() {
        ThreadLocalData threadLocalData = threadLocal.get();
        if (threadLocalData == null) {
            threadLocalData = new ThreadLocalData();
            setThreadLocalData(threadLocalData);
        }
        return threadLocalData;
    }

    @Override
    public EntityManager getCurrentEntityManager() {
        return getThreadLocalData().getEntityManager(true);
    }

    @Override
    public void closeSession() {
        EntityManager entityManager = getThreadLocalData().getEntityManager(false);
        if (entityManager != null) {
            entityManager.close();
            LOGGER.trace("Session closed");
        }
        // Von threadLocal entfernen
        threadLocal.remove();
    }

    // Speicherung der Daten pro Thread
    private class ThreadLocalData {

        private EntityManager entityManager;

        private ThreadLocalData() {
            // Verhindern, dass andere Klassen ThreadLocalData-Objekte instanziieren können
        }

        EntityManager getEntityManager(boolean createIfNecessary) {
            if (entityManager == null && createIfNecessary) {
                entityManager = entityManagerFactory.createEntityManager();
                LOGGER.trace("Opened session");
            }
            return entityManager;
        }
    }
}
