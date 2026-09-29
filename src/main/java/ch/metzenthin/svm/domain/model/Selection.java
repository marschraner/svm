package ch.metzenthin.svm.domain.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Hans Stamm
 */
@Getter
@Setter
public class Selection {

  boolean selected;

  public Selection(boolean selected) {
    this.selected = selected;
  }
}
