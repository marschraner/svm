package ch.metzenthin.svm.common.utils;

import javax.swing.*;
import java.net.URL;

/**
 * @author Martin Schraner
 */
public class DialogIcons {

    private ImageIcon questionIcon = null;
    private ImageIcon informationIcon = null;
    private ImageIcon warningIcon = null;
    private ImageIcon errorIcon = null;

    public void createDialogIcons() {

        URL questionIconURL = getClass().getResource("/images/dialog-question.png");
        if (questionIconURL != null) {
            questionIcon = new ImageIcon(questionIconURL);
        }
        URL informationIconURL = getClass().getResource("/images/dialog-information.png");
        if (informationIconURL != null) {
            informationIcon = new ImageIcon(informationIconURL);
        }
        URL warningIconURL = getClass().getResource("/images/dialog-warning.png");
        if (warningIconURL != null) {
            warningIcon = new ImageIcon(warningIconURL);
        }
        URL errorIconURL = getClass().getResource("/images/dialog-error.png");
        if (errorIconURL != null) {
            errorIcon = new ImageIcon(errorIconURL);
        }

    }

    public ImageIcon getQuestionIcon() {
        return questionIcon;
    }

    public ImageIcon getInformationIcon() {
        return informationIcon;
    }

    public ImageIcon getWarningIcon() {
        return warningIcon;
    }

    public ImageIcon getErrorIcon() {
        return errorIcon;
    }
}
