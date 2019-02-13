package ch.metzenthin.svm.persistence;

/**
 * @author Martin Schraner
 */
public class DBFactory {

    public static DB getInstance() {
        return DBImpl.getInstance();
    }
}
