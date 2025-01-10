package ch.metzenthin.svm.persistence;

/**
 * @author Martin Schraner
 */
public class DBFactory {

    private DBFactory() {
    }

    public static DB getInstance() {
        return DBImpl.getInstance();
    }
}
