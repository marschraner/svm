package ch.metzenthin.svm.service;

import ch.metzenthin.svm.common.datatypes.Listentyp;
import ch.metzenthin.svm.domain.model.KursAndLehrkraefteAndNumberOfKursanmeldungen;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.service.result.DeleteKursResult;
import ch.metzenthin.svm.service.result.ExportListResult;
import ch.metzenthin.svm.service.result.SaveKursResult;
import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * @author Martin Schraner
 */
public interface KursService {

  boolean existsKursByKursortId(int kursortId);

  boolean existsKursByKurstypId(int kurstypId);

  boolean existsKursByLektionslaenge(int lektionslaenge);

  boolean existsKursBySemesterId(int semesterId);

  int countKursanmeldungenByKursId(int kursId);

  List<KursAndLehrkraefteAndNumberOfKursanmeldungen>
      findAllKurseAndLehrkraefteAndNumberOfKursanmeldungenForSemester(int semesterId);

  SaveKursResult saveKurs(Kurs kurs, Mitarbeiter lehrkraft1, Mitarbeiter lehrkraft2);

  DeleteKursResult deleteKurs(int kursId);

  ExportListResult exportList(
      Listentyp listentyp,
      String listenTitel,
      File outputFile,
      Iterator<KursAndLehrkraefteAndNumberOfKursanmeldungen> rows,
      Semester semester);
}
