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

    public void setLabelSchuelerValue(JLabel labelSchuelerValue) {
        labelSchuelerValue.setText(schuelerDatenblattModel.getSchuelerAsString());
    }

    public void setLabelMutterValue(JLabel labelMutterValue) {
        labelMutterValue.setText(schuelerDatenblattModel.getMutterAsString());
    }

    public void setLabelVaterValue(JLabel labelVaterValue) {
        labelVaterValue.setText(schuelerDatenblattModel.getVaterAsString());
    }

    public void setLabelRechnungsempfaengerValue(JLabel labelRechnungsempfaengerValue) {
        labelRechnungsempfaengerValue.setText(schuelerDatenblattModel.getRechnungsempfaengerAsString());
    }

    public void setLabelGeschwisterValue(JLabel labelGeschwisterValue) {
        labelGeschwisterValue.setText(schuelerDatenblattModel.getGeschwisterAsString());
    }

    public void setLabelSchuelerGleicherRechnungsempfaengerValue(JLabel labelSchuelerGleicherRechnungsempfaengerValue) {
        labelSchuelerGleicherRechnungsempfaengerValue.setText(schuelerDatenblattModel.getSchuelerGleicherRechnungsempfaengerAsString());
    }

    public void setLabelAnmeldungValue(JLabel labelAnmeldungValue) {
        labelAnmeldungValue.setText(schuelerDatenblattModel.getAnmeldungenAsString());
    }

}
