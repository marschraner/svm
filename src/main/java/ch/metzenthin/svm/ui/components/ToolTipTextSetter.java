package ch.metzenthin.svm.ui.components;

import java.util.function.Consumer;

/**
 * @author Hans Stamm
 */
public interface ToolTipTextSetter extends Consumer<String> {

  default void setToolTipText(String toolTipText) {
    this.accept(toolTipText);
  }
}
