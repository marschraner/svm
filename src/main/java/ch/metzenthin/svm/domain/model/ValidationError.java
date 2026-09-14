package ch.metzenthin.svm.domain.model;

import lombok.Getter;

/**
 * @author Hans Stamm
 */
@Getter
public class ValidationError {

  private String errorMessage;

  public void setErrorLabelVisible(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public void setErrorLabelInvisible() {
    this.errorMessage = null;
  }

  public boolean isValid() {
    return errorMessage == null;
  }
}
