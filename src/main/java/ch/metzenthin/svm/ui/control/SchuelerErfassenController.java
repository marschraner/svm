package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.*;
import ch.metzenthin.svm.ui.components.*;

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
        try {
            mutterModel.setAnrede(Anrede.FRAU);
        } catch (SvmValidationException ignore) {
        }
        schuelerErfassenModel.setMutterModel(mutterModel);
    }

    public void setVaterPanel(AngehoerigerPanel vaterPanel, AngehoerigerModel vaterModel) {
        vaterPanel.setModel(vaterModel);
        try {
            vaterModel.setAnrede(Anrede.HERR);
        } catch (SvmValidationException ignore) {
        }
        schuelerErfassenModel.setVaterModel(vaterModel);
    }

    public void setDrittempfaengerPanel(AngehoerigerPanel drittempfaengerPanel, AngehoerigerModel drittempfaengerModel) {
        drittempfaengerPanel.setModel(drittempfaengerModel);
        try {
            drittempfaengerModel.setAnrede(Anrede.FRAU);
        } catch (SvmValidationException ignore) {
        }
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
        } catch (Throwable e) {
            // todo Dialog "nicht erfolgreich gespeichert"
            e.printStackTrace();
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
        });
        return dialog[0];
    }

}
