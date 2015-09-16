package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.persistence.daos.KursanmeldungDao;
import ch.metzenthin.svm.persistence.entities.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Schraner
 */
public class FindKurseMapSchuelerSemesterCommand extends GenericDaoCommand {

    // input
    private List<Schueler> schuelerList;
    private final Semester semester;
    private final Wochentag wochentag;  // nullable
    private final Time zeitBeginn;      // nullable
    private final Mitarbeiter mitarbeiter;  // nullable

    // output
    private Map<Schueler, List<Kurs>> kurseMap = new HashMap<>();

    public FindKurseMapSchuelerSemesterCommand(List<Schueler> schuelerList, Semester semester, Wochentag wochentag, Time zeitBeginn, Mitarbeiter mitarbeiter) {
        this.schuelerList = schuelerList;
        this.semester = semester;
        this.wochentag = wochentag;
        this.zeitBeginn = zeitBeginn;
        this.mitarbeiter = mitarbeiter;
    }

    @Override
    public void execute() {
        if (semester == null) {
            return;
        }
        KursanmeldungDao kursanmeldungDao = new KursanmeldungDao(entityManager);
        for (Schueler schueler : schuelerList) {
            List<Kursanmeldung> kurseinteilungenFound = kursanmeldungDao.findKursanmeldungen(schueler, semester, wochentag, zeitBeginn, mitarbeiter);
            List<Kurs> kurse = new ArrayList<>();
            for (Kursanmeldung kursanmeldung : kurseinteilungenFound) {
                kurse.add(kursanmeldung.getKurs());
            }
            kurseMap.put(schueler, kurse);
        }
    }

    public Map<Schueler, List<Kurs>> getKurseMap() {
        return kurseMap;
    }
}
