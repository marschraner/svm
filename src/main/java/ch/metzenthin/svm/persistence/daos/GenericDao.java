package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import jakarta.persistence.EntityManager;

import java.lang.reflect.ParameterizedType;

/**
 * @author hans
 */
public abstract class GenericDao<T, U> {

    private final Class<T> persistentClass;
    protected final DB db = DBFactory.getInstance();

    @SuppressWarnings("unchecked")
    protected GenericDao() {
        persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public T findById(U id) {
        return db.getCurrentEntityManager().find(persistentClass, id);
    }

    public T save(T entity) {
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.persist(entity);
        entityManager.flush();
        entityManager.refresh(entity);
        return entity;
    }

    public void remove(T entity) {
        db.getCurrentEntityManager().remove(entity);
    }
}
