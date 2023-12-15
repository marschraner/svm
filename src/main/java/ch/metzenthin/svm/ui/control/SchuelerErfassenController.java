package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.DeleteSchuelerCommand;
import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.domain.model.*;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Controller, der die Models von Schüler erfassen überwacht.
 *
 * @author Hans Stamm
 */
public class SchuelerErfassenController extends AbstractController {

    private static final Logger LOGGER = LogManager.getLogger(SchuelerErfassenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private JButton btnSpeichern;
    private JButton btnSchuelerLoeschen;
    private JButton btnFruehereAnmeldungenLoeschen;
    private JButton btnAbbrechen;
    private ActionListener closeListener;

    private final SvmContext svmContext;
    private final SchuelerErfassenModel schuelerErfassenModel;
    private final boolean isBearbeiten;
    private final boolean defaultButtonEnabled;
    private ActionListener zurueckZuDatenblattListener;
    private ActionListener nextPanelListener;
    private final boolean modelValidationMode;

    public SchuelerErfassenController(SvmContext svmContext, SchuelerErfassenModel schuelerErfassenModel, boolean isBearbeiten, boolean defaultButtonEnabled) {
        super(schuelerErfassenModel);
        this.svmContext = svmContext;
        this.schuelerErfassenModel = schuelerErfassenModel;
        this.schuelerErfassenModel.addDisableFieldsListener(this);
        this.schuelerErfassenModel.addMakeErrorLabelsInvisibleListener(this);
        this.isBearbeiten = isBearbeiten;
        this.defaultButtonEnabled = defaultButtonEnabled;
        this.schuelerErfassenModel.addCompletedListener(this::onSchuelerErfassenModelCompleted);
        this.modelValidationMode = MODEL_VALIDATION_MODE;
    }

    public void constructionDone() {
        schuelerErfassenModel.initializeCompleted();
    }

    private void onSchuelerErfassenModelCompleted(boolean completed) {
        LOGGER.trace("SchuelerErfassenController completed=" + completed);
        if (completed) {
            btnSpeichern.setToolTipText(null);
            btnSpeichern.setEnabled(true);
        } else {
            btnSpeichern.setToolTipText("Bitte Eingabedaten vervollständigen");
            btnSpeichern.setEnabled(false);
        }
    }

    private AbstractController schuelerController;

    public void setSchuelerPanel(SchuelerPanel schuelerPanel, SchuelerModel schuelerModel) {
        schuelerController = schuelerPanel.setModel(schuelerModel, defaultButtonEnabled);
        schuelerController.setModelValidationMode(MODEL_VALIDATION_MODE);
        // Kein Abmeldedatum sichtbar
        schuelerPanel.getLblAbmeldedatum().setVisible(isBearbeiten);
        schuelerPanel.getTxtAbmeldedatum().setVisible(isBearbeiten);
        schuelerErfassenModel.setSchuelerModel(schuelerModel);
    }

    private AngehoerigerController mutterController;

    public void setMutterPanel(AngehoerigerPanel mutterPanel, AngehoerigerModel mutterModel) {
        mutterController = mutterPanel.setModel(mutterModel, defaultButtonEnabled);
        mutterController.setModelValidationMode(MODEL_VALIDATION_MODE);
        // Rechnungsempfänger-Label überschreiben
        mutterPanel.getLblRechnungsempfaenger().setText("Rechnungsempfängerin");
        // Anrede auf Frau setzen (Anrede anzeigen für Möglichkeit zur Erfassung gleichgeschlechtlicher Paare)
        try {
            mutterModel.setAnrede(Anrede.FRAU);
            // Default Rechungsempfängerin
            mutterModel.setIsRechnungsempfaenger(true);
        } catch (SvmValidationException ignore) {
        }
        // Gleiche Adresse wie Schüler
        if (isBearbeiten) {
            // bearbeiten: false (falls Mutter vorhanden, wird danach dieser Wert genommen)
            mutterModel.setIsGleicheAdresseWieSchueler(false);
        } else {
            // neu: true
            mutterModel.setIsGleicheAdresseWieSchueler(true);
            mutterModel.disableFields(getAdresseFields());
        }
        // Wünscht E-Mails
        // bearbeiten: false (falls Mutter vorhanden, wird danach dieser Wert genommen)
        // neu: true
        mutterModel.setWuenschtEmails(!isBearbeiten);
        schuelerErfassenModel.setMutterModel(mutterModel);
    }

    @SuppressWarnings("DuplicatedCode")
    private static Set<Field> getAdresseFields() {
        Set<Field> adresseFields = new HashSet<>();
        adresseFields.add(Field.STRASSE_HAUSNUMMER);
        adresseFields.add(Field.PLZ);
        adresseFields.add(Field.ORT);
        adresseFields.add(Field.FESTNETZ);
        return adresseFields;
    }

    private AngehoerigerController vaterController;

    public void setVaterPanel(AngehoerigerPanel vaterPanel, AngehoerigerModel vaterModel) {
        vaterController = vaterPanel.setModel(vaterModel, defaultButtonEnabled);
        vaterController.setModelValidationMode(MODEL_VALIDATION_MODE);
        // Anrede auf Herr setzen (Anrede anzeigen für Möglichkeit zur Erfassung gleichgeschlechtlicher Paare)
        try {
            vaterModel.setAnrede(Anrede.HERR);
        } catch (SvmValidationException ignore) {
        }
        // Gleiche Adresse wie Schüler
        if (isBearbeiten) {
            // bearbeiten: false (falls Vater vorhanden, wird danach dieser Wert genommen)
            vaterModel.setIsGleicheAdresseWieSchueler(false);
        } else {
            // neu: true
            vaterModel.setIsGleicheAdresseWieSchueler(true);
            vaterModel.disableFields(getAdresseFields());
        }
        // Wünscht E-Mails: false
        vaterModel.setWuenschtEmails(false);
        schuelerErfassenModel.setVaterModel(vaterModel);
    }

    private AbstractController drittempfaengerController;

    public void setDrittempfaengerPanel(AngehoerigerPanel drittempfaengerPanel, AngehoerigerModel drittempfaengerModel) {
        drittempfaengerController = drittempfaengerPanel.setModel(drittempfaengerModel, defaultButtonEnabled);
        drittempfaengerController.setModelValidationMode(MODEL_VALIDATION_MODE);
        // Keine Adresse Schüler übernehmen-Checkbox anzeigen
        drittempfaengerPanel.getLblGleicheAdresseWieSchueler().setVisible(false);
        drittempfaengerPanel.getCheckBoxGleicheAdresseWieSchueler().setVisible(false);
        // Keine Rechnungsempfänger-Checkbox anzeigen
        drittempfaengerPanel.getLblRechnungsempfaenger().setVisible(false);
        drittempfaengerPanel.getCheckBoxRechnungsempfaenger().setVisible(false);
        // Keine wünscht Emails-Checkbox anzeigen (wird nicht ausgewertet)
        drittempfaengerPanel.getLblWuenschtEmails().setVisible(false);
        drittempfaengerPanel.getCheckBoxWuenschtEmails().setVisible(false);
        // Anrede: Default Frau
        try {
            drittempfaengerModel.setAnrede(Anrede.FRAU);
        } catch (SvmRequiredException ignore) {
        }
        // Default deaktiviert
        drittempfaengerModel.disableFields();
        schuelerErfassenModel.setDrittempfaengerModel(drittempfaengerModel);
    }

    public void setBtnSpeichern(JButton btnSpeichern) {
        this.btnSpeichern = btnSpeichern;
        if (isModelValidationMode()) {
            btnSpeichern.setEnabled(false);
        }
        btnSpeichern.addActionListener(e -> onSpeichern());
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        this.btnAbbrechen = btnAbbrechen;
        btnAbbrechen.addActionListener(e -> onAbbrechen());
    }

    public void setBtnSchuelerLoeschen(JButton btnSchuelerLoeschen) {
        this.btnSchuelerLoeschen = btnSchuelerLoeschen;
        if (!isBearbeiten) {
            btnSchuelerLoeschen.setVisible(false);
        }
        btnSchuelerLoeschen.addActionListener(e -> onSchuelerLoeschen());
    }

    public void setBtnFruehereAnmeldungenLoeschen(JButton btnFruehereAnmeldungenLoeschen) {
        this.btnFruehereAnmeldungenLoeschen = btnFruehereAnmeldungenLoeschen;
        if (!isBearbeiten) {
            btnFruehereAnmeldungenLoeschen.setVisible(false);
        } else if (!schuelerErfassenModel.hasFruehereAnmeldungen()) {
            btnFruehereAnmeldungenLoeschen.setEnabled(false);
        }
        btnFruehereAnmeldungenLoeschen.addActionListener(e -> onFruehereAnmeldungenLoeschen());
    }

    public void addCloseListener(ActionListener closeListener) {
        this.closeListener = closeListener;
    }

    public void addNextPanelListener(ActionListener nextPanelListener) {
        this.nextPanelListener = nextPanelListener;
    }

    public void addZurueckZuDatenblattListener(ActionListener saveSuccessfulListener) {
        this.zurueckZuDatenblattListener = saveSuccessfulListener;
    }

    private void onAbbrechen() {
        LOGGER.trace("SchuelerErfassenPanelAbbrechen gedrückt");
        Object[] options = {"Ja", "Nein"};
        String dialogText;
        if (!isBearbeiten) {
            dialogText = "Durch Drücken des Ja-Buttons wird die Eingabemaske geschlossen. Allfällige Eingaben werden nicht gespeichert. Fortfahren?";
        } else {
            dialogText = "Durch Drücken des Ja-Buttons wird die Eingabemaske geschlossen. Allfällige getätigte Änderungen werden nicht gespeichert. Fortfahren?";
        }
        int n = JOptionPane.showOptionDialog(
                null,
                dialogText,
                "Eingabemaske schliessen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,  //the titles of buttons
                options[0]); //default button title
        if (n == 0) {
            if (!isBearbeiten) {
                closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Abbrechen"));
            } else {
                zurueckZuDatenblattListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Zurück nach Abbrechen"));
            }
        }
    }

    private void onSpeichern() {
        LOGGER.trace("SchuelerErfassenPanel Speichern gedrückt");
        if (!modelValidationMode && !validateOnSpeichern()) {
            btnSpeichern.setFocusPainted(false);
            return;
        }
        SchuelerErfassenDialog dialog;
        SchuelerErfassenSaveResult schuelerErfassenSaveResult = schuelerErfassenModel.validieren(svmContext);
        while (schuelerErfassenSaveResult != null) { // Wenn null: kein weiterer Dialog
            dialog = createDialog(schuelerErfassenSaveResult);
            if (dialog == null) {
                break;
            }
            dialog.pack();
            dialog.setVisible(true);
            schuelerErfassenSaveResult = dialog.getResult();
        }
        // Wenn isAbbrechen() zurück zur Eingabemaske, sonst Listener aufrufen (wenn vorhanden), der ein neues Panel aufruft
        if (schuelerErfassenSaveResult != null && schuelerErfassenSaveResult.getResult() == ValidateSchuelerCommand.Result.SPEICHERUNG_ERFOLGREICH) {
            if (zurueckZuDatenblattListener != null) {
                zurueckZuDatenblattListener.actionPerformed(new ActionEvent(btnSpeichern, ActionEvent.ACTION_PERFORMED, "Speichern erfolgreich"));
            } else {
                SchuelerSuchenTableData schuelerSuchenTableData = schuelerErfassenModel.getSchuelerSuchenTableData();
                SchuelerSuchenTableModel schuelerSuchenTableModel = new SchuelerSuchenTableModel(schuelerSuchenTableData);
                SchuelerDatenblattPanel schuelerDatenblattPanel = new SchuelerDatenblattPanel(svmContext, schuelerSuchenTableModel, null, 0, false);
                schuelerDatenblattPanel.addNextPanelListener(nextPanelListener);
                schuelerDatenblattPanel.addCloseListener(closeListener);
                nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerDatenblattPanel.$$$getRootComponent$$$(), "Datenblatt"}, ActionEvent.ACTION_PERFORMED, "Schüler ausgewählt"));
            }
        }
        btnSpeichern.setFocusPainted(false);
    }

    @Override
    public boolean validateOnSpeichern() {
        // Defaultmässig sind beim Erfassen eines neuen Schülers für Mutter und Vater
        // "gleiche Adresse wie Schüler" selektiert, für Mutter zudem "wünscht E-Mails".
        // Falls die Mutter / der Vater nicht erfasst werden (kein Nachname), sollen diese Felder
        // auf false gesetzt werden. Andernfalls hätte die nicht erfasste Mutter / der nicht
        // erfasste Vater eine Adresse / ein Festnetz bzw. "wünscht E-Mails" selektiert, ohne
        // dass eine E-Mail gesetzt ist, was nicht zulässig ist.
        try {
            schuelerController.validateWithThrowException();
            if (schuelerErfassenModel.isEmptyNachnameMutter()) {
                mutterController.setModelGleicheAdresseWieSchueler(false);
                mutterController.setModelWuenschtEmails(false);
            }
            mutterController.validateWithThrowException();
            if (schuelerErfassenModel.isEmptyNachnameVater()) {
                vaterController.setModelGleicheAdresseWieSchueler(false);
                vaterController.setModelWuenschtEmails(false);
            }
            vaterController.validateWithThrowException();
            drittempfaengerController.validateWithThrowException();
            validateWithThrowException();
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerErfassenPanel validateOnSpeichern Exception: " + e.getMessageLong());
            return false;
        }
        return true;
    }

    @Override
    void validateFields() throws SvmValidationException {
        // Hat keine eigenen Eingabe-Felder
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        schuelerController.showErrMsg(e);
        mutterController.showErrMsg(e);
        vaterController.showErrMsg(e);
        drittempfaengerController.showErrMsg(e);
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        schuelerController.showErrMsgAsToolTip(e);
        mutterController.showErrMsgAsToolTip(e);
        vaterController.showErrMsgAsToolTip(e);
        drittempfaengerController.showErrMsgAsToolTip(e);
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
        schuelerController.disableFields(disable, fields);
        mutterController.disableFields(disable, fields);
        vaterController.disableFields(disable, fields);
        drittempfaengerController.disableFields(disable, fields);
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        schuelerController.makeErrorLabelsInvisible(fields);
        mutterController.makeErrorLabelsInvisible(fields);
        vaterController.makeErrorLabelsInvisible(fields);
        drittempfaengerController.makeErrorLabelsInvisible(fields);
    }

    private void onSchuelerLoeschen() {
        Object[] options = {"Ja", "Nein"};
        int n = JOptionPane.showOptionDialog(
                null,
                "Durch Drücken des Ja-Buttons wird der Schüler unwiderruflich aus der Datenbank gelöscht. Fortfahren?",
                "Schüler löschen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            DeleteSchuelerCommand.Result result = schuelerErfassenModel.schuelerLoeschen();
            switch (result) {
                case SCHUELER_IN_KURSE_EINGESCHRIEBEN -> {
                    JOptionPane.showMessageDialog(null, "Der Schüler ist in mindestens einen Kurs eingeschrieben und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    btnSchuelerLoeschen.setFocusPainted(false);
                }
                case SCHUELER_IN_MAERCHEN_EINGETEILT -> {
                    JOptionPane.showMessageDialog(null, "Der Schüler ist in mindestens einem Märchen eingeteilt und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    btnSchuelerLoeschen.setFocusPainted(false);
                }
                case RECHNUNGSEMPFAENGER_HAT_SEMESTERRECHNUNGEN -> {
                    JOptionPane.showMessageDialog(null, "Der Rechnungsempfänger des Schülers hat mindestens eine Semesterrechnung. Der Schüler kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    btnSchuelerLoeschen.setFocusPainted(false);
                }
                case LOESCHEN_ERFOLGREICH -> {
                    JOptionPane.showMessageDialog(
                            null,
                            "Der Schüler wurde erfolgreich aus der Datenbank gelöscht.",
                            "Löschen erfolgreich",
                            JOptionPane.INFORMATION_MESSAGE);
                    closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Löschen"));
                }
            }
        } else {
            btnSchuelerLoeschen.setFocusPainted(false);
        }
    }

    private void onFruehereAnmeldungenLoeschen() {
        Object[] options = {"Ja", "Nein"};
        int n = JOptionPane.showOptionDialog(
                null,
                "Durch Drücken des Ja-Buttons werden alle früheren Schüler-Anmeldungen, ausser der aktuellsten, gelöscht. \n" +
                        "Kurs-Anmeldungen werden nicht gelöscht. Fortfahren?",
                "Frühere Schüler-Anmeldungen löschen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            schuelerErfassenModel.fruehereAnmeldungenLoeschen();
            zurueckZuDatenblattListener.actionPerformed(new ActionEvent(btnSpeichern, ActionEvent.ACTION_PERFORMED, "Frühere Anmeldungen erfolgreich gelöscht"));
        } else {
            btnFruehereAnmeldungenLoeschen.setFocusPainted(false);
        }
    }

    private SchuelerErfassenDialog createDialog(SchuelerErfassenSaveResult schuelerErfassenSaveResult) {
        final SchuelerErfassenDialog[] dialog = new SchuelerErfassenDialog[1]; // Array, weil Variable final sein muss
        schuelerErfassenSaveResult.accept(new SchuelerErfassenSaveResultVisitor() {
            @Override
            public void visit(ValidateSchuelerSummaryResult validateSchuelerSummaryResult) {
                dialog[0] = new ValidateSchuelerSummaryDialog(validateSchuelerSummaryResult, schuelerErfassenModel);
            }

            @Override
            public void visit(AngehoerigerMehrereEintraegePassenResult angehoerigerMehrereEintraegePassenResult) {
                dialog[0] = new AngehoerigerMehrereEintraegePassenDialog(angehoerigerMehrereEintraegePassenResult, schuelerErfassenModel);
            }

            @Override
            public void visit(AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult) {
                dialog[0] = new AngehoerigerMehrereEintraegeGleicherNameAndereAttributeDialog(angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult, schuelerErfassenModel);
            }

            @Override
            public void visit(AngehoerigerEinEintragGleicherNameAndereAttributeResult angehoerigerEinEintragGleicherNameAndereAttributeResult) {
                dialog[0] = new AngehoerigerEinEintragGleicherNameAndereAttributeDialog(angehoerigerEinEintragGleicherNameAndereAttributeResult, schuelerErfassenModel);
            }

            @Override
            public void visit(AngehoerigerEinEintragPasstResult angehoerigerEinEintragPasstResult) {
                dialog[0] = new AngehoerigerEinEintragPasstDialog(angehoerigerEinEintragPasstResult, schuelerErfassenModel);
            }

            @Override
            public void visit(SchuelerBereitsInDatenbankResult schuelerBereitsInDatenbankResult) {
                JOptionPane.showMessageDialog(
                        null,
                        schuelerBereitsInDatenbankResult.getErrorMessage(),
                        "Fehler",
                        JOptionPane.ERROR_MESSAGE);
                schuelerErfassenModel.abbrechen();
                dialog[0] = null;
            }

            @Override
            public void visit(GeschwisterOhneWuenschtEmailsResult schuelerBereitsInDatenbankResult) {
                JOptionPane.showMessageDialog(
                        null,
                        schuelerBereitsInDatenbankResult.getErrorMessage(),
                        "Wünscht E-Mails nicht selektiert",
                        JOptionPane.ERROR_MESSAGE);
                schuelerErfassenModel.abbrechen();
                dialog[0] = null;
            }

            @Override
            public void visit(SchuelerErfassenSaveOkResult schuelerErfassenSaveOkResult) {
                dialog[0] = null;
            }

            @Override
            public void visit(SchuelerErfassenUnerwarteterFehlerResult schuelerErfassenUnerwarteterFehlerResult) {
                throw new RuntimeException(schuelerErfassenUnerwarteterFehlerResult.getFehler());
            }

            @Override
            public void visit(DrittpersonIdentischMitElternteilResult drittpersonIdentischMitElternteilResult) {
                JOptionPane.showMessageDialog(
                        null,
                        drittpersonIdentischMitElternteilResult.getErrorMessage(),
                        "Fehler",
                        JOptionPane.ERROR_MESSAGE);
                schuelerErfassenModel.abbrechen();
                dialog[0] = null;
            }

            @Override
            public void visit(AbbrechenResult abbrechenResult) {
                schuelerErfassenModel.abbrechen();
                dialog[0] = null;
            }

        });
        return dialog[0];
    }
}
