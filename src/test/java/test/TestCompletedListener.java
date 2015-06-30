package test;

import ch.metzenthin.svm.domain.model.CompletedListener;

/**
 * @author Hans Stamm
 */
public class TestCompletedListener implements CompletedListener {
    private int counter;
    private boolean isCompleted;
    @Override
    public void completed(boolean completed) {
        counter++;
        isCompleted = completed;
    }
    public int getCounter() {
        return counter;
    }
    public boolean isCompleted() {
        return isCompleted;
    }
}