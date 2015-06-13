package ch.metzenthin.svm.domain;

/**
 * @author Hans Stamm
 */
public class SvmException extends Exception {

    private int errorId;

    public SvmException(int errorId, String errorMsg) {
        super(errorMsg);
        this.errorId = errorId;
    }

    public int getErrorId() {
        return errorId;
    }

}
