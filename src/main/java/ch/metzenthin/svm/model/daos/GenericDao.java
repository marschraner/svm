package ch.metzenthin.svm.model.daos;

/**
 * @author hans
 */
public interface GenericDao<T, ID> {
    T findById(ID id);
    T save(T entity);
}
