package ch.metzenthin.svm.domain;

/**
 * @author Hans Stamm
 */
public class SvmException extends Exception {
    private int errorId;
    private String errorMsg;

    public SvmException(int errorId, String errorMsg) {
        super(errorMsg);
        this.errorId = errorId;
        this.errorMsg = errorMsg;
    }

}
