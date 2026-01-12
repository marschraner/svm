package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.ModelValueGetter;
import ch.metzenthin.svm.domain.model.ModelValueSetter;
import ch.metzenthin.svm.ui.components.ToolTipTextSetter;
import ch.metzenthin.svm.ui.components.ViewValueGetter;
import lombok.Getter;

/**
 * @param <T> ModelAndViewAccessor-Typ, z.B. String
 * @author Hans Stamm
 */
public class ModelAndViewAccessor<T> {

  @Getter private final Field field;
  private final ModelValueGetter<T> modelValueGetter;
  private final ModelValueSetter<T> modelValueSetter;
  private final ViewValueGetter<T> viewValueGetter;
  private final ToolTipTextSetter toolTipTextSetter;

  public ModelAndViewAccessor(
      Field field,
      ModelValueGetter<T> modelValueGetter,
      ModelValueSetter<T> modelValueSetter,
      ViewValueGetter<T> viewValueGetter,
      ToolTipTextSetter toolTipTextSetter) {
    this.field = field;
    this.modelValueGetter = modelValueGetter;
    this.modelValueSetter = modelValueSetter;
    this.viewValueGetter = viewValueGetter;
    this.toolTipTextSetter = toolTipTextSetter;
  }

  T getModelValue() {
    return modelValueGetter.getModelValue();
  }

  void setModelValue(T modelValue) throws SvmValidationException {
    modelValueSetter.setModelValue(modelValue);
  }

  T getViewValue() {
    return viewValueGetter.getViewValue();
  }

  void setToolTipText(String message) {
    toolTipTextSetter.setToolTipText(message);
  }
}
