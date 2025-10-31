package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MaercheneinteilungDao;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindMaercheneinteilungenSchuelerCommand implements Command {

  private final MaercheneinteilungDao maercheneinteilungDao = new MaercheneinteilungDao();

  // input
  private final Schueler schueler;

  // output
  private List<Maercheneinteilung> maercheneinteilungenFound;

  FindMaercheneinteilungenSchuelerCommand(Schueler schueler) {
    this.schueler = schueler;
  }

  @Override
  public void execute() {
    maercheneinteilungenFound = maercheneinteilungDao.findMaercheneinteilungenSchueler(schueler);
  }

  List<Maercheneinteilung> getMaercheneinteilungenFound() {
    return maercheneinteilungenFound;
  }
}
