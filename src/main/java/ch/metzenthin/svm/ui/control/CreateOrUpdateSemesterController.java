package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmRuntimeException;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.model.CreateOrUpdateSemesterModel;
import ch.metzenthin.svm.domain.model.entityfields.SemesterFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateSemesterView;
import java.awt.event.*;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateSemesterController
    extends AbstractCreateOrUpdateController<CreateOrUpdateSemesterView> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(CreateOrUpdateSemesterController.class);

  private final CreateOrUpdateSemesterModel model;

  public CreateOrUpdateSemesterController(
      CreateOrUpdateSemesterModel createOrUpdateSemesterModel, String title) {
    super(createView(title));
    model = createOrUpdateSemesterModel;
    configSpinnerSchuljahre();
    configComboBoxSemesterbezeichnung();
    configTxtSemesterbeginn();
    configTxtSemesterende();
    configTxtFerienbeginn1();
    configTxtFerienende1();
    configTxtFerienbeginn2();
    configTxtFerienende2();
    initialiseViewFields();
  }

  private static CreateOrUpdateSemesterView createView(String title) {
    return new CreateOrUpdateSemesterView(title);
  }

  private void configSpinnerSchuljahre() {
    view.addSpinnerSchuljahreChangeListener(e -> onSchuljahrSelected());
  }

  private void onSchuljahrSelected() {
    LOGGER.trace(
        "CreateOrUpdateSemesterController Event Schuljahre selected ={}",
        view.getSpinnerSchuljahreValue());
    ValidationResult validationResult = model.validateSchuljahr(view.getSpinnerSchuljahreValue());
    if (!validationResult.isValid()) {
      view.showErrorMessageDialog(validationResult.errorMessage(), "Fehler");
    }
  }

  private void configComboBoxSemesterbezeichnung() {
    view.setComboBoxSemesterbezeichnungValues(Semesterbezeichnung.values());
    view.addComboBoxSemesterbezeichnungActionListener(e -> onSemesterbezeichnungSelected());
  }

  private void onSemesterbezeichnungSelected() {
    LOGGER.trace(
        "CreateOrUpdateSemesterController Event Semesterbezeichnung selected={}",
        view.getComboBoxSemesterbezeichnungSelectedItem());
    ValidationResult validationResult =
        model.validateSemesterbezeichnung(view.getComboBoxSemesterbezeichnungSelectedItem());
    if (!validationResult.isValid()) {
      view.showErrorMessageDialog(validationResult.errorMessage(), "Fehler");
    }
  }

  private void configTxtSemesterbeginn() {
    view.addTxtSemesterbeginnActionListener(e -> onSemesterbeginnEvent());
    view.addTxtSemesterbeginnFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onSemesterbeginnEvent();
          }
        });
  }

  private void onSemesterbeginnEvent() {
    LOGGER.trace("CreateOrUpdateSemesterController Event Semesterbeginn");
    formatConvertAndValidateDate(
        view.getTxtSemesterbeginnText(),
        model::validateSemesterbeginn,
        view::setTxtSemesterbeginnText,
        view::setErrorLabelSemesterbeginnVisible,
        view::setErrorLabelSemesterbeginnInvisible);
  }

  private void configTxtSemesterende() {
    view.addTxtSemesterendeActionListener(e -> onSemesterendeEvent());
    view.addTxtSemesterendeFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onSemesterendeEvent();
          }
        });
  }

  private void onSemesterendeEvent() {
    LOGGER.trace("CreateOrUpdateSemesterController Event Semesterende");
    formatConvertAndValidateDate(
        view.getTxtSemesterendeText(),
        model::validateSemesterende,
        view::setTxtSemesterendeText,
        view::setErrorLabelSemesterendeVisible,
        view::setErrorLabelSemesterendeInvisible);
  }

  private void configTxtFerienbeginn1() {
    view.addTxtFerienbeginn1ActionListener(e -> onFerienbeginn1Event());
    view.addTxtFerienbeginn1FocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onFerienbeginn1Event();
          }
        });
  }

  private void onFerienbeginn1Event() {
    LOGGER.trace("CreateOrUpdateSemesterController Event Ferienbeginn1");
    formatConvertAndValidateDate(
        view.getTxtFerienbeginn1Text(),
        model::validateFerienbeginn1,
        view::setTxtFerienbeginn1Text,
        view::setErrorLabelFerienbeginn1Visible,
        view::setErrorLabelFerienbeginn1Invisible);
  }

  private void configTxtFerienende1() {
    view.addTxtFerienende1ActionListener(e -> onFerienende1Event());
    view.addTxtFerienende1FocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onFerienende1Event();
          }
        });
  }

  private void onFerienende1Event() {
    LOGGER.trace("CreateOrUpdateSemesterController Event Ferienende1");
    formatConvertAndValidateDate(
        view.getTxtFerienende1Text(),
        model::validateFerienende1,
        view::setTxtFerienende1Text,
        view::setErrorLabelFerienende1Visible,
        view::setErrorLabelFerienende1Invisible);
  }

  private void configTxtFerienbeginn2() {
    view.addTxtFerienbeginn2ActionListener(e -> onFerienbeginn2Event());
    view.addTxtFerienbeginn2FocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onFerienbeginn2Event();
          }
        });
  }

  private void onFerienbeginn2Event() {
    LOGGER.trace("CreateOrUpdateSemesterController Event Ferienbeginn2");
    formatConvertAndValidateDate(
        view.getTxtFerienbeginn2Text(),
        model::validateFerienbeginn2,
        view::setTxtFerienbeginn2Text,
        view::setErrorLabelFerienbeginn2Visible,
        view::setErrorLabelFerienbeginn2Invisible);
  }

  private void configTxtFerienende2() {
    view.addTxtFerienende2ActionListener(e -> onFerienende2Event());
    view.addTxtFerienende2FocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onFerienende2Event();
          }
        });
  }

  private void onFerienende2Event() {
    LOGGER.trace("CreateOrUpdateSemesterController Event Ferienende2");
    formatConvertAndValidateDate(
        view.getTxtFerienende2Text(),
        model::validateFerienende2,
        view::setTxtFerienende2Text,
        view::setErrorLabelFerienende2Visible,
        view::setErrorLabelFerienende2Invisible);
  }

  private void initialiseViewFields() {
    if (!model.isNeu()) {
      SemesterFields semesterFields = model.getSemesterFields();
      view.setSpinnerSchuljahreValue(semesterFields.schuljahr());
      view.setComboBoxSemesterbezeichnungSelectedItem(semesterFields.semesterbezeichnung());
      view.setTxtSemesterbeginnText(semesterFields.semesterbeginn());
      view.setTxtSemesterendeText(semesterFields.semesterende());
      view.setTxtFerienbeginn1Text(semesterFields.ferienbeginn1());
      view.setTxtFerienende1Text(semesterFields.ferienende1());
      view.setTxtFerienbeginn2Text(semesterFields.ferienbeginn2());
      view.setTxtFerienende2Text(semesterFields.ferienende2());
    } else {
      SemesterFields naechstesSemester = model.getNaechstesNochNichtErfasstesSemester();
      view.setSpinnerSchuljahreValue(naechstesSemester.schuljahr());
      view.setComboBoxSemesterbezeichnungSelectedItem(naechstesSemester.semesterbezeichnung());
    }
  }

  @Override
  protected ValidationResultsAndSaveResult speichern() {
    boolean affectsSemesterrechnungen =
        model.checkIfUpdateAffectsSemesterrechnungen(
            view.getTxtSemesterbeginnText(),
            view.getTxtSemesterendeText(),
            view.getTxtFerienbeginn1Text(),
            view.getTxtFerienende1Text(),
            view.getTxtFerienbeginn2Text(),
            view.getTxtFerienende2Text());
    ValidationResultsAndSaveResult validationResultsAndSaveResult;
    if (affectsSemesterrechnungen) {
      int n =
          view.showYesNoDialog(
              "Soll die Anzahl Semesterwochen auch bei den Semesterrechnungen geändert werden? \n"
                  + "(Semesterrechnungen mit gesetztem Rechnungsdatum werden nicht verändert.)",
              "Anzahl Semesterwochen auch bei Semesterrechnungen ändern?");
      if (n == 0) {
        JDialog semesterSpeichernBusyDialog =
            view.createBusyDialog(
                "Das Semester und die betroffenen Semesterrechnungen werden gespeichert.");
        SwingWorker<ValidationResultsAndSaveResult, Void> semesterSpeichernSwingWorker =
            executeSpeichernOnSwingWorker(semesterSpeichernBusyDialog);
        semesterSpeichernBusyDialog.setVisible(true);
        // Der nachfolgende Code ist blockiert, bis der Worker beendet ist und den BusyDialog
        // schliesst
        validationResultsAndSaveResult =
            getValidationResultAndSaveResult(semesterSpeichernSwingWorker);
      } else {
        validationResultsAndSaveResult = model.speichern(createSemesterFieldsFromView(), false);
      }
    } else {
      validationResultsAndSaveResult = model.speichern(createSemesterFieldsFromView(), false);
    }

    return validationResultsAndSaveResult;
  }

  private SemesterFields createSemesterFieldsFromView() {
    return new SemesterFields(
        view.getSpinnerSchuljahreValue(),
        view.getComboBoxSemesterbezeichnungSelectedItem(),
        view.getTxtSemesterbeginnText(),
        view.getTxtSemesterendeText(),
        view.getTxtFerienbeginn1Text(),
        view.getTxtFerienende1Text(),
        view.getTxtFerienbeginn2Text(),
        view.getTxtFerienende2Text());
  }

  private SwingWorker<ValidationResultsAndSaveResult, Void> executeSpeichernOnSwingWorker(
      JDialog busyDialog) {
    SwingWorker<ValidationResultsAndSaveResult, Void> worker =
        new SwingWorker<>() {
          @Override
          protected ValidationResultsAndSaveResult doInBackground() {
            return model.speichern(createSemesterFieldsFromView(), true);
          }

          @Override
          protected void done() {
            busyDialog.dispose();
          }
        };

    // Worker muss ausgeführt werden bevor der Dialog visible wird, da mit setVisible der
    // nachfolgende Code blockiert ist
    worker.execute();
    return worker;
  }

  private static ValidationResultsAndSaveResult getValidationResultAndSaveResult(
      SwingWorker<ValidationResultsAndSaveResult, Void> semesterSpeichernSwingWorker) {
    ValidationResultsAndSaveResult validationResultsAndSaveResult;
    try {
      validationResultsAndSaveResult = semesterSpeichernSwingWorker.get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new SvmRuntimeException(e.getMessage(), e);
    } catch (ExecutionException e) {
      throw new SvmRuntimeException(e.getMessage(), e);
    }
    return validationResultsAndSaveResult;
  }

  @Override
  protected void setErrorLabelVisible(ValidationResult validationResult, Field field) {
    switch (field) {
      case SEMESTERBEGINN ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelSemesterbeginnVisible);
      case SEMESTERENDE ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelSemesterendeVisible);
      case FERIENBEGINN1 ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelFerienbeginn1Visible);
      case FERIENENDE1 ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelFerienende1Visible);
      case FERIENBEGINN2 ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelFerienbeginn2Visible);
      case FERIENENDE2 ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelFerienende2Visible);
      default -> throw new IllegalStateException("Unexpected value: " + field);
    }
  }

  @Override
  protected void setAllErrorLabelsInvisible() {
    view.setErrorLabelSemesterbeginnInvisible();
    view.setErrorLabelSemesterendeInvisible();
    view.setErrorLabelFerienbeginn1Invisible();
    view.setErrorLabelFerienende1Invisible();
    view.setErrorLabelFerienbeginn2Invisible();
    view.setErrorLabelFerienende2Invisible();
  }
}
