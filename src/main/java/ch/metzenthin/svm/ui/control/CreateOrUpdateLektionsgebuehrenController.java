package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.utils.Converter;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CreateOrUpdateLektionsgebuehrenModel;
import ch.metzenthin.svm.service.result.SaveLektionsgebuehrenResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateLektionsgebuehrenView;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateLektionsgebuehrenController
    extends SpeichernAbbrechenDialogController<
        CreateOrUpdateLektionsgebuehrenModel,
        CreateOrUpdateLektionsgebuehrenView,
        SaveLektionsgebuehrenResult> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(CreateOrUpdateLektionsgebuehrenController.class);

  public CreateOrUpdateLektionsgebuehrenController(
      CreateOrUpdateLektionsgebuehrenModel model, boolean isBearbeiten, String title) {
    super(model, new CreateOrUpdateLektionsgebuehrenView(title), isBearbeiten);
    configTxtLektionslaenge();
    configTxtBetrag1Kind();
    configTxtBetrag2Kinder();
    configTxtBetrag3Kinder();
    configTxtBetrag4Kinder();
    configTxtBetrag5Kinder();
    configTxtBetrag6Kinder();
  }

  private ModelAndViewAccessor<String> lektionslaengeModelAndViewAccessor;

  private void configTxtLektionslaenge() {
    lektionslaengeModelAndViewAccessor =
        new ModelAndViewAccessor<>(
            Field.LEKTIONSLAENGE,
            () -> Integer.toString(model.getLektionslaenge()),
            model::setLektionslaenge,
            view::getTxtLektionslaengeText,
            view::setTxtLektionslaengeToolTipText);
    view.addTxtLektionslaengeActionListener(e -> onLektionslaengeEvent(true));
    view.addTxtLektionslaengeFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onLektionslaengeEvent(false);
          }
        });
  }

  private void onLektionslaengeEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Lektionslaenge");
    setModelValueFromViewWithViewValueChangedCheck(
        lektionslaengeModelAndViewAccessor, showRequiredErrMsg);
  }

  private ModelAndViewAccessor<String> betrag1KindModelAndViewAccessor;

  private void configTxtBetrag1Kind() {
    betrag1KindModelAndViewAccessor =
        new ModelAndViewAccessor<>(
            Field.BETRAG_1_KIND,
            () -> Converter.asStringNullSafe(model.getBetrag1Kind()),
            model::setBetrag1Kind,
            view::getTxtBetrag1KindText,
            view::setTxtBetrag1KindToolTipText);
    view.addTxtBetrag1KindActionListener(e -> onBetrag1KindEvent(true));
    view.addTxtBetrag1KindFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetrag1KindEvent(false);
          }
        });
  }

  private void onBetrag1KindEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Betrag1Kind");
    setModelValueFromViewWithViewValueChangedCheck(
        betrag1KindModelAndViewAccessor, showRequiredErrMsg);
  }

  private ModelAndViewAccessor<String> betrag2KinderModelAndViewAccessor;

  private void configTxtBetrag2Kinder() {
    betrag2KinderModelAndViewAccessor =
        new ModelAndViewAccessor<>(
            Field.BETRAG_2_KINDER,
            () -> Converter.asStringNullSafe(model.getBetrag2Kinder()),
            model::setBetrag2Kinder,
            view::getTxtBetrag2KinderText,
            view::setTxtBetrag2KinderToolTipText);
    view.addTxtBetrag2KinderActionListener(e -> onBetrag2KinderEvent(true));
    view.addTxtBetrag2KinderFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetrag2KinderEvent(false);
          }
        });
  }

  private void onBetrag2KinderEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Betrag2Kinder");
    setModelValueFromViewWithViewValueChangedCheck(
        betrag2KinderModelAndViewAccessor, showRequiredErrMsg);
  }

  private ModelAndViewAccessor<String> betrag3KinderModelAndViewAccessor;

  private void configTxtBetrag3Kinder() {
    betrag3KinderModelAndViewAccessor =
        new ModelAndViewAccessor<>(
            Field.BETRAG_3_KINDER,
            () -> Converter.asStringNullSafe(model.getBetrag3Kinder()),
            model::setBetrag3Kinder,
            view::getTxtBetrag3KinderText,
            view::setTxtBetrag3KinderToolTipText);
    view.addTxtBetrag3KinderActionListener(e -> onBetrag3KinderEvent(true));
    view.addTxtBetrag3KinderFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetrag3KinderEvent(false);
          }
        });
  }

  private void onBetrag3KinderEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Betrag3Kinder");
    setModelValueFromViewWithViewValueChangedCheck(
        betrag3KinderModelAndViewAccessor, showRequiredErrMsg);
  }

  private ModelAndViewAccessor<String> betrag4KinderModelAndViewAccessor;

  private void configTxtBetrag4Kinder() {
    betrag4KinderModelAndViewAccessor =
        new ModelAndViewAccessor<>(
            Field.BETRAG_4_KINDER,
            () -> Converter.asStringNullSafe(model.getBetrag4Kinder()),
            model::setBetrag4Kinder,
            view::getTxtBetrag4KinderText,
            view::setTxtBetrag4KinderToolTipText);
    view.addTxtBetrag4KinderActionListener(e -> onBetrag4KinderEvent(true));
    view.addTxtBetrag4KinderFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetrag4KinderEvent(false);
          }
        });
  }

  private void onBetrag4KinderEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Betrag4Kinder");
    setModelValueFromViewWithViewValueChangedCheck(
        betrag4KinderModelAndViewAccessor, showRequiredErrMsg);
  }

  private ModelAndViewAccessor<String> betrag5KinderModelAndViewAccessor;

  private void configTxtBetrag5Kinder() {
    betrag5KinderModelAndViewAccessor =
        new ModelAndViewAccessor<>(
            Field.BETRAG_5_KINDER,
            () -> Converter.asStringNullSafe(model.getBetrag5Kinder()),
            model::setBetrag5Kinder,
            view::getTxtBetrag5KinderText,
            view::setTxtBetrag5KinderToolTipText);
    view.addTxtBetrag5KinderActionListener(e -> onBetrag5KinderEvent(true));
    view.addTxtBetrag5KinderFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetrag5KinderEvent(false);
          }
        });
  }

  private void onBetrag5KinderEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Betrag5Kinder");
    setModelValueFromViewWithViewValueChangedCheck(
        betrag5KinderModelAndViewAccessor, showRequiredErrMsg);
  }

  private ModelAndViewAccessor<String> betrag6KinderModelAndViewAccessor;

  private void configTxtBetrag6Kinder() {
    betrag6KinderModelAndViewAccessor =
        new ModelAndViewAccessor<>(
            Field.BETRAG_6_KINDER,
            () -> Converter.asStringNullSafe(model.getBetrag6Kinder()),
            model::setBetrag6Kinder,
            view::getTxtBetrag6KinderText,
            view::setTxtBetrag6KinderToolTipText);
    view.addTxtBetrag6KinderActionListener(e -> onBetrag6KinderEvent(true));
    view.addTxtBetrag6KinderFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetrag6KinderEvent(false);
          }
        });
  }

  private void onBetrag6KinderEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Betrag6Kinder");
    setModelValueFromViewWithViewValueChangedCheck(
        betrag6KinderModelAndViewAccessor, showRequiredErrMsg);
  }

  @SuppressWarnings("java:S3776")
  @Override
  void doPropertyChange(PropertyChangeEvent evt) {
    super.doPropertyChange(evt);
    if (checkIsFieldChange(Field.LEKTIONSLAENGE, evt)) {
      view.setTxtLektionslaengeText(Integer.toString(model.getLektionslaenge()));
    } else if (checkIsFieldChange(Field.BETRAG_1_KIND, evt)) {
      view.setTxtBetrag1KindText(Converter.asStringNullSafe(model.getBetrag1Kind()));
    } else if (checkIsFieldChange(Field.BETRAG_2_KINDER, evt)) {
      view.setTxtBetrag2KinderText(Converter.asStringNullSafe(model.getBetrag2Kinder()));
    } else if (checkIsFieldChange(Field.BETRAG_3_KINDER, evt)) {
      view.setTxtBetrag3KinderText(Converter.asStringNullSafe(model.getBetrag3Kinder()));
    } else if (checkIsFieldChange(Field.BETRAG_4_KINDER, evt)) {
      view.setTxtBetrag4KinderText(Converter.asStringNullSafe(model.getBetrag4Kinder()));
    } else if (checkIsFieldChange(Field.BETRAG_5_KINDER, evt)) {
      view.setTxtBetrag5KinderText(Converter.asStringNullSafe(model.getBetrag5Kinder()));
    } else if (checkIsFieldChange(Field.BETRAG_6_KINDER, evt)) {
      view.setTxtBetrag6KinderText(Converter.asStringNullSafe(model.getBetrag6Kinder()));
    }
  }

  @Override
  void validateFields() throws SvmValidationException {
    if (view.isTxtLektionslaengeEnabled()) {
      LOGGER.trace("Validate field Lektionslaenge");
      setModelValueFromView(lektionslaengeModelAndViewAccessor, true);
    }
    if (view.isTxtBetrag1KindEnabled()) {
      LOGGER.trace("Validate field Betrag 1 Kind");
      setModelValueFromView(betrag1KindModelAndViewAccessor, true);
    }
    if (view.isTxtBetrag2KinderEnabled()) {
      LOGGER.trace("Validate field Betrag 2 Kinder");
      setModelValueFromView(betrag2KinderModelAndViewAccessor, true);
    }
    if (view.isTxtBetrag3KinderEnabled()) {
      LOGGER.trace("Validate field Betrag 3 Kinder");
      setModelValueFromView(betrag3KinderModelAndViewAccessor, true);
    }
    if (view.isTxtBetrag4KinderEnabled()) {
      LOGGER.trace("Validate field Betrag 4 Kinder");
      setModelValueFromView(betrag4KinderModelAndViewAccessor, true);
    }
    if (view.isTxtBetrag5KinderEnabled()) {
      LOGGER.trace("Validate field Betrag 5 Kinder");
      setModelValueFromView(betrag5KinderModelAndViewAccessor, true);
    }
    if (view.isTxtBetrag6KinderEnabled()) {
      LOGGER.trace("Validate field Betrag 6 Kinder");
      setModelValueFromView(betrag6KinderModelAndViewAccessor, true);
    }
  }

  @Override
  void showErrMsg(SvmValidationException e) {
    if (e.getAffectedFields().contains(Field.LEKTIONSLAENGE)) {
      view.setErrorLabelLektionslaengeVisible(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_1_KIND)) {
      view.setErrorLabelBetrag1KindVisible(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_2_KINDER)) {
      view.setErrorLabelBetrag2KinderVisible(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_3_KINDER)) {
      view.setErrorLabelBetrag3KinderVisible(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_4_KINDER)) {
      view.setErrorLabelBetrag4KinderVisible(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_5_KINDER)) {
      view.setErrorLabelBetrag5KinderVisible(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_6_KINDER)) {
      view.setErrorLabelBetrag6KinderVisible(e.getMessage());
    }
  }

  @Override
  void showErrMsgAsToolTip(SvmValidationException e) {
    if (e.getAffectedFields().contains(Field.LEKTIONSLAENGE)) {
      view.setTxtLektionslaengeToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_1_KIND)) {
      view.setTxtBetrag1KindToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_2_KINDER)) {
      view.setTxtBetrag2KinderToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_3_KINDER)) {
      view.setTxtBetrag3KinderToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_4_KINDER)) {
      view.setTxtBetrag4KinderToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_5_KINDER)) {
      view.setTxtBetrag5KinderToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_6_KINDER)) {
      view.setTxtBetrag6KinderToolTipText(e.getMessage());
    }
  }

  @Override
  public void makeErrorLabelsInvisible(Set<Field> fields) {
    if (fields.contains(Field.ALLE) || fields.contains(Field.LEKTIONSLAENGE)) {
      view.setErrorLabelLektionslaengeInvisible();
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_1_KIND)) {
      view.setErrorLabelBetrag1KindInvisible();
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_2_KINDER)) {
      view.setErrorLabelBetrag2KinderInvisible();
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_3_KINDER)) {
      view.setErrorLabelBetrag3KinderInvisible();
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_4_KINDER)) {
      view.setErrorLabelBetrag4KinderInvisible();
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_5_KINDER)) {
      view.setErrorLabelBetrag5KinderInvisible();
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_6_KINDER)) {
      view.setErrorLabelBetrag6KinderInvisible();
    }
  }

  @Override
  public void disableFields(boolean disable, Set<Field> fields) {
    // Keine Felder, die inaktiviert werden müssen
  }
}
