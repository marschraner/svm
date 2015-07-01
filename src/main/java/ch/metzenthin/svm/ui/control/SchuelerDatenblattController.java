package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;

import javax.swing.*;

/**
 * @author Hans Stamm
 */
public class SchuelerDatenblattController {
    private final SchuelerDatenblattModel schuelerDatenblattModel;

    public SchuelerDatenblattController(SchuelerDatenblattModel schuelerDatenblattModel) {
        this.schuelerDatenblattModel = schuelerDatenblattModel;
    }

    public void setLabelVornameNachname(JLabel labelVornameNachname) {
        labelVornameNachname.setText(schuelerDatenblattModel.getSchuelerVorname() + " " + schuelerDatenblattModel.getSchuelerNachname());
    }

    public void setLabelSchueler(JLabel labelSchueler) {
        labelSchueler.setText(schuelerDatenblattModel.getLabelSchueler());
    }

    public void setLabelSchuelerValue(JLabel labelSchuelerValue) {
        labelSchuelerValue.setText(schuelerDatenblattModel.getSchuelerAsString());
    }


    public void setLabelMutterValue(JLabel labelMutterValue) {
        labelMutterValue.setText(schuelerDatenblattModel.getMutterAsString());
    }

    public void setLabelVaterValue(JLabel labelVaterValue) {
        labelVaterValue.setText(schuelerDatenblattModel.getVaterAsString());
    }

    public void setLabelRechnungsempfaenger(JLabel labelRechnungsempfaenger) {
        labelRechnungsempfaenger.setText(schuelerDatenblattModel.getLabelRechnungsempfaenger());
    }

    public void setLabelRechnungsempfaengerValue(JLabel labelRechnungsempfaengerValue) {
        labelRechnungsempfaengerValue.setText(schuelerDatenblattModel.getRechnungsempfaengerAsString());
    }

    public void setLabelGeschwisterValue(JLabel labelGeschwisterValue) {
        labelGeschwisterValue.setText(schuelerDatenblattModel.getGeschwisterAsString());
    }

    public void setLabelSchuelerGleicherRechnungsempfaenger1(JLabel labelSchuelerGleicherRechnungsempfaenger1) {
        labelSchuelerGleicherRechnungsempfaenger1.setText(schuelerDatenblattModel.getLabelSchuelerGleicherRechnungsempfaenger1());
    }

    public void setLabelSchuelerGleicherRechnungsempfaenger2(JLabel labelSchuelerGleicherRechnungsempfaenger2) {
        labelSchuelerGleicherRechnungsempfaenger2.setText(schuelerDatenblattModel.getLabelSchuelerGleicherRechnungsempfaenger2());
    }

    public void setLabelSchuelerGleicherRechnungsempfaengerValue(JLabel labelSchuelerGleicherRechnungsempfaengerValue) {
        labelSchuelerGleicherRechnungsempfaengerValue.setText(schuelerDatenblattModel.getSchuelerGleicherRechnungsempfaengerAsString());
    }

    public void setLabelGeburtsdatumValue(JLabel labelGeburtsdatumValue) {
        labelGeburtsdatumValue.setText(schuelerDatenblattModel.getSchuelerGeburtsdatumAsString());
    }

    public void setLabelAnmeldedatumValue(JLabel labelAnmeldungValue) {
        labelAnmeldungValue.setText(schuelerDatenblattModel.getAnmeldedatumAsString());
    }

    public void setLabelAbmeldedatum(JLabel labelAbmeldedatum) {
        if (schuelerDatenblattModel.getAbmeldedatumAsString().isEmpty()) {
            labelAbmeldedatum.setVisible(false);
        }
    }

    public void setLabelAbmeldedatumValue(JLabel labelAbmeldedatumValue) {
        if (schuelerDatenblattModel.getAbmeldedatumAsString().isEmpty()) {
            labelAbmeldedatumValue.setVisible(false);
        }
        labelAbmeldedatumValue.setText(schuelerDatenblattModel.getAbmeldedatumAsString());
    }

    public void setLabelFruehereAnmeldungen(JLabel labelFruehereAnmeldungen) {
        if (schuelerDatenblattModel.getFruehereAnmeldungenAsString().isEmpty()) {
            labelFruehereAnmeldungen.setVisible(false);
        }
    }

    public void setLabelFruehereAnmeldungenValue(JLabel labelFruehereAnmeldungenValue) {
        if (schuelerDatenblattModel.getFruehereAnmeldungenAsString().isEmpty()) {
            labelFruehereAnmeldungenValue.setVisible(false);
        }
        labelFruehereAnmeldungenValue.setText(schuelerDatenblattModel.getFruehereAnmeldungenAsString());
    }
}
