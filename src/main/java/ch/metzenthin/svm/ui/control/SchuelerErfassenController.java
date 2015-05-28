package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.model.AngehoerigerModel;
import ch.metzenthin.svm.domain.model.SchuelerErfassenModel;
import ch.metzenthin.svm.domain.model.SchuelerModel;
import ch.metzenthin.svm.ui.components.AngehoerigerPanel;
import ch.metzenthin.svm.ui.components.SchuelerPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
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
        schuelerErfassenModel.checkCompleted();
    }

    private void onSchuelerErfassenModelCompleted(boolean completed) {
        btnSpeichern.setEnabled(completed);
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
            schuelerErfassenModel.save();
        } catch (Throwable e) {
            // todo Dialog "nicht erfolgreich gespeichert"
            e.printStackTrace();
        }
        // todo Dialog "erfolgreich gespeichert"
        closeListener.actionPerformed(new ActionEvent(btnSpeichern, ActionEvent.ACTION_PERFORMED, "Close nach Speichern"));
    }

}
