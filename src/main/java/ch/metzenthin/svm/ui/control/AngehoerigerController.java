package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.model.AngehoerigerModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * @author Hans Stamm
 */
public class AngehoerigerController extends PersonController {

    private JTextField txtBeruf;

    private AngehoerigerModel angehoerigerModel;

    public AngehoerigerController(AngehoerigerModel angehoerigerModel) {
        super(angehoerigerModel);
        this.angehoerigerModel = angehoerigerModel;
    }

    public void setTxtBeruf(JTextField txtBeruf) {
        this.txtBeruf = txtBeruf;
        if (this.txtBeruf != null) {
            this.txtBeruf.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onBerufEvent();
                }
            });
            this.txtBeruf.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    onBerufEvent();
                }
            });
        }
    }

    private void onBerufEvent() {
        System.out.println("AngehoerigerController Event Beruf");
        angehoerigerModel.setBeruf(txtBeruf.getText());
    }

}
