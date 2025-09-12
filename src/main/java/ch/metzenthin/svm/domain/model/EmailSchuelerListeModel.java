package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.EmailSchuelerListeEmpfaengerGruppe;
import ch.metzenthin.svm.domain.commands.CallDefaultEmailClientCommand;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import java.util.Set;

/**
 * @author Martin Schraner
 */
public interface EmailSchuelerListeModel extends Model {

  EmailSchuelerListeEmpfaengerGruppe getEmailSchuelerListeEmpfaengerGruppe();

  boolean isBlindkopien();

  void setEmailSchuelerListeEmpfaengerGruppe(
      EmailSchuelerListeEmpfaengerGruppe emailSchuelerListeEmpfaengerGruppe);

  void setBlindkopien(boolean selected);

  EmailSchuelerListeEmpfaengerGruppe[] getSelectableEmailSchuelerListeEmpfaengerGruppen(
      SchuelerSuchenTableModel schuelerSuchenTableModel);

  CallDefaultEmailClientCommand.Result callEmailClient(
      SchuelerSuchenTableModel schuelerSuchenTableModel);

  Set<String> getFehlendeEmailAdressen();

  Set<String> getUngueltigeEmailAdressen();
}
