package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.DeleteSchuelerCommand;
import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.domain.model.*;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.*;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller, der die Models von Schüler erfassen überwacht.
 *
 * Nicht abgeleitet von AbstractController, da dieser Controller keine eigenen Eingabe-Felder hat.
 *
 * @author Hans Stamm
 */
public class SchuelerErfassenController {

    private static final Logger LOGGER = Logger.getLogger(SchuelerErfassenController.class);

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
    private boolean defaultButtonEnabled;
    private ActionListener zurueckZuDatenblattListener;
    private ActionListener nextPanelListener;
    private boolean modelValidationMode;

    public SchuelerErfassenController(SvmContext svmContext, SchuelerErfassenModel schuelerErfassenModel, boolean isBearbeiten, boolean defaultButtonEnabled) {
        this.svmContext = svmContext;
        this.schuelerErfassenModel = schuelerErfassenModel;
        this.isBearbeiten = isBearbeiten;
        this.defaultButtonEnabled = defaultButtonEnabled;
        this.schuelerErfassenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onSchuelerErfassenModelCompleted(completed);
            }
        });
        this.modelValidationMode = MODEL_VALIDATION_MODE;
    }

    public void constructionDone() {
        schuelerErfassenModel.initializeCompleted();
    }

    private void onSchuelerErfassenModelCompleted(boolean completed) {
        LOGGER.trace("SchuelerErfassenController completed=" + completed);
        if (completed) {
            try {
                schuelerErfassenModel.validate();
                btnSpeichern.setToolTipText(null);
                btnSpeichern.setEnabled(true);
            } catch (SvmValidationException e) {
                LOGGER.trace("SchuelerErfassenController Exception=" + e.getMessage());
                btnSpeichern.setToolTipText(e.getMessage());
                btnSpeichern.setEnabled(false);
            }
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

    private AbstractController mutterController;

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
        schuelerErfassenModel.setMutterModel(mutterModel);
    }

    private AbstractController vaterController;

    public void setVaterPanel(AngehoerigerPanel vaterPanel, AngehoerigerModel vaterModel) {
        vaterController = vaterPanel.setModel(vaterModel, defaultButtonEnabled);
        vaterController.setModelValidationMode(MODEL_VALIDATION_MODE);
        // Anrede auf Herr setzen (Anrede anzeigen für Möglichkeit zur Erfassung gleichgeschlechtlicher Paare)
        try {
            vaterModel.setAnrede(Anrede.HERR);
        } catch (SvmValidationException ignore) {
        }
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
        // Default deaktiviert
        drittempfaengerModel.disableFields();
        schuelerErfassenModel.setDrittempfaengerModel(drittempfaengerModel);
    }

    public void setBtnSpeichern(JButton btnSpeichern) {
        this.btnSpeichern = btnSpeichern;
        btnSpeichern.setEnabled(false);
        btnSpeichern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSpeichern();
            }
        });
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        this.btnAbbrechen = btnAbbrechen;
        btnAbbrechen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        });
    }

    public void setBtnSchuelerLoeschen(JButton btnSchuelerLoeschen) {
        this.btnSchuelerLoeschen = btnSchuelerLoeschen;
        if (!isBearbeiten) {
            btnSchuelerLoeschen.setVisible(false);
        }
        btnSchuelerLoeschen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSchuelerLoeschen();
            }
        });
    }

    public void setBtnFruehereAnmeldungenLoeschen(JButton btnFruehereAnmeldungenLoeschen) {
        this.btnFruehereAnmeldungenLoeschen = btnFruehereAnmeldungenLoeschen;
        if (!isBearbeiten) {
            btnFruehereAnmeldungenLoeschen.setVisible(false);
        } else if (!schuelerErfassenModel.hasFruehereAnmeldungen()) {
            btnFruehereAnmeldungenLoeschen.setEnabled(false);
        }
        btnFruehereAnmeldungenLoeschen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onFruehereAnmeldungenLoeschen();
            }
        });
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
                svmContext.getDialogIcons().getQuestionIcon(),
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

    private boolean validateOnSpeichern() {
        try {
            schuelerController.validateWithThrowException();
            mutterController.validateWithThrowException();
            vaterController.validateWithThrowException();
            drittempfaengerController.validateWithThrowException();
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerErfassenPanel validateOnSpeichern Exception: " + e.getMessageLong());
            return false;
        }
        return true;
    }

    private void onSchuelerLoeschen() {
        Object[] options = {"Ja", "Nein"};
        int n = JOptionPane.showOptionDialog(
                null,
                "Durch Drücken des Ja-Buttons wird der Schüler unwiderruflich aus der Datenbank gelöscht. Fortfahren?",
                "Schüler löschen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                svmContext.getDialogIcons().getWarningIcon(),     //do not use a custom Icon
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            DeleteSchuelerCommand.Result result = schuelerErfassenModel.schuelerLoeschen();
            switch (result) {
                case SCHUELER_IN_KURSE_EINGESCHRIEBEN:
                    JOptionPane.showMessageDialog(null, "Der Schüler ist in mindestens einen Kurs eingeschrieben und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
                    btnSchuelerLoeschen.setFocusPainted(false);
                    break;
                case SCHUELER_IN_MAERCHEN_EINGETEILT:
                    JOptionPane.showMessageDialog(null, "Der Schüler ist in mindestens einem Märchen eingeteilt und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
                    btnSchuelerLoeschen.setFocusPainted(false);
                    break;
                case RECHNUNGSEMPFAENGER_HAT_SEMESTERRECHNUNGEN:
                    JOptionPane.showMessageDialog(null, "Der Rechnungsempfänger des Schülers hat mindestens eine Semesterrechnung. Der Schüler kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
                    btnSchuelerLoeschen.setFocusPainted(false);
                    break;
                case LOESCHEN_ERFOLGREICH:
                    JOptionPane.showMessageDialog(
                            null,
                            "Der Schüler wurde erfolgreich aus der Datenbank gelöscht.",
                            "Löschen erfolgreich",
                            JOptionPane.INFORMATION_MESSAGE,
                            svmContext.getDialogIcons().getInformationIcon());
                    closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Löschen"));
                    break;
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
                svmContext.getDialogIcons().getWarningIcon(),
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
                        JOptionPane.ERROR_MESSAGE,
                        svmContext.getDialogIcons().getErrorIcon());
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
                        JOptionPane.ERROR_MESSAGE,
                        svmContext.getDialogIcons().getErrorIcon());
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
