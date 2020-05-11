package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.CallDefaultEmailClientCommand;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;

import java.util.Set;

/**
 * @author Martin Schraner
 */
public interface EmailSemesterrechnungenModel extends Model {

    boolean isRechnungsempfaengerSelected();
    boolean isMutterOderVaterSelected();
    boolean isBlindkopien();

    void setRechnungsempfaengerSelected(boolean selected);
    void setMutterOderVaterSelected(boolean selected);
    void setBlindkopien(boolean selected);

    CallDefaultEmailClientCommand.Result callEmailClient(SemesterrechnungenTableModel semesterrechnungenTableModel);
    Set<String> getFehlendeEmailAdressen();
    Set<String> getUngueltigeEmailAdressen();
}
