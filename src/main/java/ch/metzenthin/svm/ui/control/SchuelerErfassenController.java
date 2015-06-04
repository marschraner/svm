package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.*;
import ch.metzenthin.svm.ui.components.AngehoerigerPanel;
import ch.metzenthin.svm.ui.components.SchuelerBereitsInDatenbankDialog;
import ch.metzenthin.svm.ui.components.SchuelerPanel;

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

    private JButton btnSpeichern;
    private JButton btnAbbrechen;
    private ActionListener closeListener;

    private SchuelerErfassenModel schuelerErfassenModel;

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
        System.out.println("SchuelerErfassenController completed=" + completed);
        if (completed) {
            try {
                schuelerErfassenModel.validate();
                btnSpeichern.setEnabled(true);
            } catch (SvmValidationException e) {
                System.out.println("SchuelerErfassenController Exception=" + e.getMessage());
                btnSpeichern.setEnabled(false);
            }
        } else {
            btnSpeichern.setEnabled(false);
        }
    }

    public void setSchuelerPanel(SchuelerPanel schuelerPanel, SchuelerModel schuelerModel) {
        schuelerPanel.setModel(schuelerModel);
        schuelerErfassenModel.setSchuelerModel(schuelerModel);
    }

    public void setMutterPanel(AngehoerigerPanel mutterPanel, AngehoerigerModel mutterModel) {
        mutterPanel.setModel(mutterModel);
        schuelerErfassenModel.setMutterModel(mutterModel);
    }

    public void setVaterPanel(AngehoerigerPanel vaterPanel, AngehoerigerModel vaterModel) {
        vaterPanel.setModel(vaterModel);
        schuelerErfassenModel.setVaterModel(vaterModel);
    }

    public void setDrittempfaengerPanel(AngehoerigerPanel drittempfaengerPanel, AngehoerigerModel drittempfaengerModel) {
        drittempfaengerPanel.setModel(drittempfaengerModel);
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
        System.out.println("SchuelerErfassenPanel Abbrechen gedrückt");
        closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Abbrechen"));
        // todo Dialog, ob wirklich abgebrochen werden soll
    }

    private void onSpeichern() {
        System.out.println("SchuelerErfassenPanel Speichern gedrückt");
        try {
            SchuelerErfassenSaveResult schuelerErfassenSaveResult = schuelerErfassenModel.validieren();
            if (schuelerErfassenSaveResult instanceof SchuelerBereitsInDatenbankResult) {
                System.out.println("SchuelerErfassenController SchuelerBereitsInDatenbankResult");
                SchuelerBereitsInDatenbankDialog dialog = new SchuelerBereitsInDatenbankDialog((SchuelerBereitsInDatenbankResult) schuelerErfassenSaveResult, schuelerErfassenModel);
                dialog.pack();
                dialog.setVisible(true);
            } else if (schuelerErfassenSaveResult instanceof AngehoerigerEinEintragPasstResult) {
                System.out.println("SchuelerErfassenController AngehoerigerEinEintragPasstResult");
                SchuelerErfassenSaveResult schuelerErfassenSaveResult2 = schuelerErfassenModel.proceedUebernehmen(schuelerErfassenSaveResult); // todo dialog
                if (schuelerErfassenSaveResult2 instanceof ValidateSchuelerSummaryResult) {
                    System.out.println("SchuelerErfassenController ValidateSchuelerSummaryResult");
                    SchuelerErfassenSaveResult schuelerErfassenSaveResult3 = schuelerErfassenModel.proceedWeiterfahren(schuelerErfassenSaveResult2); // todo dialog
                    schuelerErfassenModel.speichern(schuelerErfassenSaveResult3);
                }
            } else {
                if (schuelerErfassenSaveResult instanceof ValidateSchuelerSummaryResult) {
                    System.out.println("SchuelerErfassenController ValidateSchuelerSummaryResult");
                    SchuelerErfassenSaveResult schuelerErfassenSaveResult3 = schuelerErfassenModel.proceedWeiterfahren(schuelerErfassenSaveResult); // todo dialog
                    schuelerErfassenModel.speichern(schuelerErfassenSaveResult3);
                }
            }
        } catch (Throwable e) {
            // todo Dialog "nicht erfolgreich gespeichert"
            e.printStackTrace();
        }
        // todo Dialog "erfolgreich gespeichert"
        // closeListener.actionPerformed(new ActionEvent(btnSpeichern, ActionEvent.ACTION_PERFORMED, "Close nach Speichern"));
    }

}
