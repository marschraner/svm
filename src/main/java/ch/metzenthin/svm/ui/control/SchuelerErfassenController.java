package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.*;
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

    private JButton btnSpeichern;
    private JButton btnAbbrechen;
    private ActionListener closeListener;

    private final SchuelerErfassenModel schuelerErfassenModel;

    public SchuelerErfassenController(SchuelerErfassenModel schuelerErfassenModel) {
        this.schuelerErfassenModel = schuelerErfassenModel;
        this.schuelerErfassenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onSchuelerErfassenModelCompleted(completed);
            }
        });
    }

    public void constructionDone() {
        schuelerErfassenModel.initializeCompleted(); // todo $$$ besser init-Methode in Controller?
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

    public void setSchuelerPanel(SchuelerPanel schuelerPanel, SchuelerModel schuelerModel) {
        schuelerPanel.setModel(schuelerModel);
        // Kein Abmeldedatum sichtbar
        schuelerPanel.getLblAbmeldedatum().setVisible(false);
        schuelerPanel.getTxtAbmeldedatum().setVisible(false);
        schuelerPanel.getErrLblAbmeldedatum().setVisible(false);
        // Geschlecht-Voreinstellung
        schuelerModel.setGeschlecht(Geschlecht.W);
        schuelerErfassenModel.setSchuelerModel(schuelerModel);
    }

    public void setMutterPanel(AngehoerigerPanel mutterPanel, AngehoerigerModel mutterModel) {
        mutterPanel.setModel(mutterModel);
        // Rechnungsempfänger-Label überschreiben
        mutterPanel.getLblRechnungsempfaenger().setText("Rechnungsempfängerin");
        // Keine Anrede anzeigen
        mutterPanel.getLblAnrede().setVisible(false);
        mutterPanel.getComboBoxAnrede().setVisible(false);
        // Anrede immer auf Frau setzen
        try {
            mutterModel.setAnrede(Anrede.FRAU);
            // Default Rechungsempfängerin
            mutterModel.setIsRechnungsempfaenger(true);
        } catch (SvmValidationException ignore) {
        }
        schuelerErfassenModel.setMutterModel(mutterModel);
    }

    public void setVaterPanel(AngehoerigerPanel vaterPanel, AngehoerigerModel vaterModel) {
        vaterPanel.setModel(vaterModel);
        // Keine Anrede anzeigen
        vaterPanel.getLblAnrede().setVisible(false);
        vaterPanel.getComboBoxAnrede().setVisible(false);
        // Anrede immer auf Herr setzen
        try {
            vaterModel.setAnrede(Anrede.HERR);
        } catch (SvmValidationException ignore) {
        }
        schuelerErfassenModel.setVaterModel(vaterModel);
    }

    public void setDrittempfaengerPanel(AngehoerigerPanel drittempfaengerPanel, AngehoerigerModel drittempfaengerModel) {
        drittempfaengerPanel.setModel(drittempfaengerModel);
        // Anrede: KEINE nicht anzeigen:
        drittempfaengerPanel.getComboBoxAnrede().removeItem(Anrede.KEINE);
        // Keine Adresse Schüler übernehmen-Checkbox anzeigen
        drittempfaengerPanel.getLblGleicheAdresseWieSchueler().setVisible(false);
        drittempfaengerPanel.getCheckBoxGleicheAdresseWieSchueler().setVisible(false);
        // Keine Rechnungsempfänger-Checkbox anzeigen
        drittempfaengerPanel.getLblRechnungsempfaenger().setVisible(false);
        drittempfaengerPanel.getCheckBoxRechnungsempfaenger().setVisible(false);
        try {
            drittempfaengerModel.setAnrede(Anrede.FRAU);
        } catch (SvmValidationException ignore) {
        }
        // Default deaktiviert
        drittempfaengerModel.disableFields();
        schuelerErfassenModel.setDrittempfaengerModel(drittempfaengerModel);
    }

    public void setBtnSpeichern(JButton btnSpeichern) {
        this.btnSpeichern = btnSpeichern;
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

    public void addCloseListener(ActionListener closeListener) {
        this.closeListener = closeListener;
    }

    private void onAbbrechen() {
        LOGGER.trace("SchuelerErfassenPanel Abbrechen gedrückt");
        Object[] options = {"Ja", "Nein"};
        int n = JOptionPane.showOptionDialog(
                btnAbbrechen.getParent().getParent(),
                "Durch Drücken des Ja-Buttons wird die Eingabemaske geschlossen. Allfällig erfasste Daten gehen verloren.",
                "Soll die Eingabemaske geschlossen werden?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title
        if (n == 0) {
            closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Abbrechen"));
        }
    }

    private void onSpeichern() {
        LOGGER.trace("SchuelerErfassenPanel Speichern gedrückt");
        SchuelerErfassenDialog dialog = null;
        SchuelerErfassenSaveResult schuelerErfassenSaveResult = schuelerErfassenModel.validieren();
        while (schuelerErfassenSaveResult != null) { // Wenn null: kein weiterer Dialog
            dialog = createDialog(schuelerErfassenSaveResult);
            dialog.pack();
            dialog.setVisible(true);
            schuelerErfassenSaveResult = dialog.getResult();
        }
        // Wenn isAbbrechen() zurück zur Eingabemaske, sonst Eingabemaske schliessen
        if ((dialog == null) || !dialog.isAbbrechen()) {
            closeListener.actionPerformed(new ActionEvent(btnSpeichern, ActionEvent.ACTION_PERFORMED, "Close nach Speichern"));
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
                dialog[0] = new SchuelerBereitsInDatenbankDialog(schuelerBereitsInDatenbankResult, schuelerErfassenModel);
            }

            @Override
            public void visit(SchuelerErfassenSaveOkResult schuelerErfassenSaveOkResult) {
                dialog[0] = new SchuelerErfassenSaveOkDialog(schuelerErfassenSaveOkResult);
            }

            @Override
            public void visit(SchuelerErfassenUnerwarteterFehlerResult schuelerErfassenUnerwarteterFehlerResult) {
                dialog[0] = new SchuelerErfassenUnerwarteterFehlerDialog(schuelerErfassenUnerwarteterFehlerResult);
            }

            @Override
            public void visit(DrittpersonIdentischMitElternteilResult drittpersonIdentischMitElternteilResult) {
                dialog[0] = new DrittpersonIdentischMitElternteilDialog(drittpersonIdentischMitElternteilResult, schuelerErfassenModel);
            }
        });
        return dialog[0];
    }

}
