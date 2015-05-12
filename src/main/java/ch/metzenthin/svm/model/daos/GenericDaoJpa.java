package ch.metzenthin.svm.model.daos;

import javax.persistence.EntityManager;
import java.lang.reflect.ParameterizedType;

/**
 * @author hans
 */
public abstract class GenericDaoJpa<T, ID> implements GenericDao<T, ID> {
    private Class<T> persistentClass;
    protected EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public GenericDaoJpa() {
        persistentClass = (Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        if (entityManager == null) {
            throw new NullPointerException("EntityManager not set");
        }
        return entityManager;
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    @Override
    public T findById(ID id) {
        return entityManager.find(persistentClass, id);
    }

    @Override
    public T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }
}
