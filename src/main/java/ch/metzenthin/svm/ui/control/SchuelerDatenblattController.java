package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;

import javax.swing.*;

/**
 * @author Hans Stamm
 */
public class SchuelerDatenblattController {
    private final SchuelerDatenblattModel schuelerDatenblattModel;
    private JLabel labelSchuelerValue;
    private JLabel labelMutterValue;
    private JLabel labelVaterValue;

    public SchuelerDatenblattController(SchuelerDatenblattModel schuelerDatenblattModel) {
        this.schuelerDatenblattModel = schuelerDatenblattModel;
    }

    public void setLabelSchuelerValue(JLabel labelSchuelerValue) {
        this.labelSchuelerValue = labelSchuelerValue;
        labelSchuelerValue.setText(schuelerDatenblattModel.getNachname() + "," + schuelerDatenblattModel.getVorname());
    }

    public void setLabelMutterValue(JLabel labelMutterValue) {
        this.labelMutterValue = labelMutterValue;
    }

    public void setLabelVaterValue(JLabel labelVaterValue) {
        this.labelVaterValue = labelVaterValue;
    }

}
