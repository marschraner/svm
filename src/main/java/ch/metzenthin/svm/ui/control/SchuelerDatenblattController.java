package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Hans Stamm
 */
public class SchuelerDatenblattController {
    private final SchuelerSuchenTableModel schuelerSuchenTableModel;
    private int selectedRow;
    private SchuelerDatenblattModel schuelerDatenblattModel;
    private JButton btnAbbrechen;
    private JButton btnErster;
    private JButton btnLetzter;
    private JButton btnNachfolgender;
    private JButton btnVorheriger;
    private JButton btnStammdatenBearbeiten;
    private JButton btnDispensationBearbeiten;
    private JButton btnCodesBearbeiten;
    private JLabel labelVornameNachname;
    private JLabel labelSchueler;
    private JLabel labelSchuelerValue;
    private JLabel labelMutterValue;
    private JLabel labelVaterValue;
    private JLabel labelRechnungsempfaenger;
    private JLabel labelRechnungsempfaengerValue;
    private JLabel labelGeschwisterValue;
    private JLabel labelSchuelerGleicherRechnungsempfaenger1;
    private JLabel labelSchuelerGleicherRechnungsempfaenger2;
    private JLabel labelSchuelerGleicherRechnungsempfaengerValue;
    private JLabel labelGeburtsdatumValue;
    private JLabel labelAnmeldungValue;
    private JLabel labelAbmeldedatum;
    private JLabel labelAbmeldedatumValue;
    private JLabel labelFruehereAnmeldungen;
    private JLabel labelFruehereAnmeldungenValue;
    private JLabel labelScrollPosition;

    public SchuelerDatenblattController(SchuelerSuchenTableModel schuelerSuchenTableModel, int selectedRow) {
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.selectedRow = selectedRow;
        schuelerDatenblattModel = schuelerSuchenTableModel.getSchuelerDatenblattModel(selectedRow);
    }

    private void scroll(int selectedRow) {
        if ((this.selectedRow == selectedRow) || (selectedRow < 0) || (selectedRow >= schuelerSuchenTableModel.getRowCount())) {
            return;
        }
        this.selectedRow = selectedRow;
        schuelerDatenblattModel = schuelerSuchenTableModel.getSchuelerDatenblattModel(selectedRow);
        enableScrollButtons();
        setLabelScrollPosition();
        setLabelVornameNachname();
        setLabelSchueler();
        setLabelSchuelerValue();
        setLabelAnmeldungValue();
        setLabelFruehereAnmeldungen();
        setLabelFruehereAnmeldungenValue();
        setLabelAbmeldedatum();
        setLabelAbmeldedatumValue();
        setLabelGeburtsdatumValue();
        setLabelMutterValue();
        setLabelVaterValue();
        setLabelRechnungsempfaenger();
        setLabelRechnungsemfpaengerValue();
        setLabelSchuelerGleicherRechnungsemfpaengerValue();
        setLabelSchuelerGleicherRechnungsempfaenger1();
        setLabelSchuelerGleicherRechnungsempfaenger2();
        setLabelGeschwisterValue();
    }

    private void enableScrollButtons() {
        enableBtnErster();
        enableBtnLetzter();
        enableBtnVorheriger();
        enableBtnNachfolgender();
    }

    private void enableBtnNachfolgender() {
        btnNachfolgender.setEnabled(selectedRow != (schuelerSuchenTableModel.getRowCount() - 1));
    }

    private void enableBtnVorheriger() {
        btnVorheriger.setEnabled(selectedRow != 0);
    }

    private void enableBtnLetzter() {
        btnLetzter.setEnabled(selectedRow != (schuelerSuchenTableModel.getRowCount() - 1));
    }

    private void enableBtnErster() {
        btnErster.setEnabled(selectedRow != 0);
    }

    public void setLabelVornameNachname(JLabel labelVornameNachname) {
        this.labelVornameNachname = labelVornameNachname;
        setLabelVornameNachname();
    }

    private void setLabelVornameNachname() {
        labelVornameNachname.setText(schuelerDatenblattModel.getSchuelerVorname() + " " + schuelerDatenblattModel.getSchuelerNachname());
    }

    public void setLabelSchueler(JLabel labelSchueler) {
        this.labelSchueler = labelSchueler;
        setLabelSchueler();
    }

    private void setLabelSchueler() {
        labelSchueler.setText(schuelerDatenblattModel.getLabelSchueler());
    }

    public void setLabelSchuelerValue(JLabel labelSchuelerValue) {
        this.labelSchuelerValue = labelSchuelerValue;
        setLabelSchuelerValue();
    }

    private void setLabelSchuelerValue() {
        labelSchuelerValue.setText(schuelerDatenblattModel.getSchuelerAsString());
    }

    public void setLabelMutterValue(JLabel labelMutterValue) {
        this.labelMutterValue = labelMutterValue;
        setLabelMutterValue();
    }

    private void setLabelMutterValue() {
        labelMutterValue.setText(schuelerDatenblattModel.getMutterAsString());
    }

    public void setLabelVaterValue(JLabel labelVaterValue) {
        this.labelVaterValue = labelVaterValue;
        setLabelVaterValue();
    }

    private void setLabelVaterValue() {
        labelVaterValue.setText(schuelerDatenblattModel.getVaterAsString());
    }

    public void setLabelRechnungsempfaenger(JLabel labelRechnungsempfaenger) {
        this.labelRechnungsempfaenger = labelRechnungsempfaenger;
        setLabelRechnungsempfaenger();
    }

    private void setLabelRechnungsempfaenger() {
        labelRechnungsempfaenger.setText(schuelerDatenblattModel.getLabelRechnungsempfaenger());
    }

    public void setLabelRechnungsempfaengerValue(JLabel labelRechnungsempfaengerValue) {
        this.labelRechnungsempfaengerValue = labelRechnungsempfaengerValue;
        setLabelRechnungsemfpaengerValue();
    }

    private void setLabelRechnungsemfpaengerValue() {
        labelRechnungsempfaengerValue.setText(schuelerDatenblattModel.getRechnungsempfaengerAsString());
    }

    public void setLabelGeschwisterValue(JLabel labelGeschwisterValue) {
        this.labelGeschwisterValue = labelGeschwisterValue;
        setLabelGeschwisterValue();
    }

    private void setLabelGeschwisterValue() {
        labelGeschwisterValue.setText(schuelerDatenblattModel.getGeschwisterAsString());
    }

    public void setLabelSchuelerGleicherRechnungsempfaenger1(JLabel labelSchuelerGleicherRechnungsempfaenger1) {
        this.labelSchuelerGleicherRechnungsempfaenger1 = labelSchuelerGleicherRechnungsempfaenger1;
        setLabelSchuelerGleicherRechnungsempfaenger1();
    }

    private void setLabelSchuelerGleicherRechnungsempfaenger1() {
        labelSchuelerGleicherRechnungsempfaenger1.setText(schuelerDatenblattModel.getLabelSchuelerGleicherRechnungsempfaenger1());
    }

    public void setLabelSchuelerGleicherRechnungsempfaenger2(JLabel labelSchuelerGleicherRechnungsempfaenger2) {
        this.labelSchuelerGleicherRechnungsempfaenger2 = labelSchuelerGleicherRechnungsempfaenger2;
        setLabelSchuelerGleicherRechnungsempfaenger2();
    }

    private void setLabelSchuelerGleicherRechnungsempfaenger2() {
        labelSchuelerGleicherRechnungsempfaenger2.setText(schuelerDatenblattModel.getLabelSchuelerGleicherRechnungsempfaenger2());
    }

    public void setLabelSchuelerGleicherRechnungsempfaengerValue(JLabel labelSchuelerGleicherRechnungsempfaengerValue) {
        this.labelSchuelerGleicherRechnungsempfaengerValue = labelSchuelerGleicherRechnungsempfaengerValue;
        setLabelSchuelerGleicherRechnungsemfpaengerValue();
    }

    private void setLabelSchuelerGleicherRechnungsemfpaengerValue() {
        labelSchuelerGleicherRechnungsempfaengerValue.setText(schuelerDatenblattModel.getSchuelerGleicherRechnungsempfaengerAsString());
    }

    public void setLabelGeburtsdatumValue(JLabel labelGeburtsdatumValue) {
        this.labelGeburtsdatumValue = labelGeburtsdatumValue;
        setLabelGeburtsdatumValue();
    }

    private void setLabelGeburtsdatumValue() {
        labelGeburtsdatumValue.setText(schuelerDatenblattModel.getSchuelerGeburtsdatumAsString());
    }

    public void setLabelAnmeldedatumValue(JLabel labelAnmeldungValue) {
        this.labelAnmeldungValue = labelAnmeldungValue;
        setLabelAnmeldungValue();
    }

    private void setLabelAnmeldungValue() {
        labelAnmeldungValue.setText(schuelerDatenblattModel.getAnmeldedatumAsString());
    }

    public void setLabelAbmeldedatum(JLabel labelAbmeldedatum) {
        this.labelAbmeldedatum = labelAbmeldedatum;
        setLabelAbmeldedatum();
    }

    private void setLabelAbmeldedatum() {
        if (schuelerDatenblattModel.getAbmeldedatumAsString().isEmpty()) {
            labelAbmeldedatum.setVisible(false);
        }
    }

    public void setLabelAbmeldedatumValue(JLabel labelAbmeldedatumValue) {
        this.labelAbmeldedatumValue = labelAbmeldedatumValue;
        setLabelAbmeldedatumValue();
    }

    private void setLabelAbmeldedatumValue() {
        if (schuelerDatenblattModel.getAbmeldedatumAsString().isEmpty()) {
            labelAbmeldedatumValue.setVisible(false);
        }
        labelAbmeldedatumValue.setText(schuelerDatenblattModel.getAbmeldedatumAsString());
    }

    public void setLabelFruehereAnmeldungen(JLabel labelFruehereAnmeldungen) {
        this.labelFruehereAnmeldungen = labelFruehereAnmeldungen;
        setLabelFruehereAnmeldungen();
    }

    private void setLabelFruehereAnmeldungen() {
        if (schuelerDatenblattModel.getFruehereAnmeldungenAsString().isEmpty()) {
            labelFruehereAnmeldungen.setVisible(false);
        }
    }

    public void setLabelFruehereAnmeldungenValue(JLabel labelFruehereAnmeldungenValue) {
        this.labelFruehereAnmeldungenValue = labelFruehereAnmeldungenValue;
        setLabelFruehereAnmeldungenValue();
    }

    private void setLabelFruehereAnmeldungenValue() {
        if (schuelerDatenblattModel.getFruehereAnmeldungenAsString().isEmpty()) {
            labelFruehereAnmeldungenValue.setVisible(false);
        }
        labelFruehereAnmeldungenValue.setText(schuelerDatenblattModel.getFruehereAnmeldungenAsString());
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

    private void onAbbrechen() {

    }

    public void setBtnErster(JButton btnErster) {
        this.btnErster = btnErster;
        btnErster.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onErster();
            }
        });
        enableBtnErster();
    }

    private void onErster() {
        scroll(0);
    }

    public void setBtnLetzter(JButton btnLetzter) {
        this.btnLetzter = btnLetzter;
        btnLetzter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLetzter();
            }
        });
        enableBtnLetzter();
    }

    private void onLetzter() {
        scroll(schuelerSuchenTableModel.getRowCount() - 1);
    }

    public void setBtnNachfolgender(JButton btnNachfolgender) {
        this.btnNachfolgender = btnNachfolgender;
        btnNachfolgender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNachfolgender();
            }
        });
        enableBtnNachfolgender();
    }

    private void onNachfolgender() {
        scroll(selectedRow + 1);
    }

    public void setBtnVorheriger(JButton btnVorheriger) {
        this.btnVorheriger = btnVorheriger;
        btnVorheriger.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onVorheriger();
            }
        });
        enableBtnVorheriger();
    }

    private void onVorheriger() {
        scroll(selectedRow - 1);
    }

    public void setBtnStammdatenBearbeiten(JButton btnStammdatenBearbeiten) {
        this.btnStammdatenBearbeiten = btnStammdatenBearbeiten;
        btnStammdatenBearbeiten.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStammdatenBearbeiten();
            }
        });
    }

    private void onStammdatenBearbeiten() {

    }

    public void setBtnDispensationBearbeiten(JButton btnDispensationBearbeiten) {
        this.btnDispensationBearbeiten = btnDispensationBearbeiten;
        btnDispensationBearbeiten.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDispensationBearbeiten();
            }
        });
    }

    private void onDispensationBearbeiten() {

    }

    public void setBtnCodesBearbeiten(JButton btnCodesBearbeiten) {
        this.btnCodesBearbeiten = btnCodesBearbeiten;
        btnCodesBearbeiten.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCodesBearbeiten();
            }
        });
    }

    private void onCodesBearbeiten() {

    }

    public void setLabelScrollPosition(JLabel labelScrollPosition) {
        this.labelScrollPosition = labelScrollPosition;
        setLabelScrollPosition();
    }

    private void setLabelScrollPosition() {
        labelScrollPosition.setText((selectedRow + 1) + " / " + schuelerSuchenTableModel.getRowCount());
    }

}
