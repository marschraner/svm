package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.persistence.daos.KursanmeldungDao;
import ch.metzenthin.svm.persistence.entities.*;

import java.sql.Time;
import java.util.*;

/**
 * @author Martin Schraner
 */
public class FindKurseMapSchuelerSemesterCommand implements Command {

    private final KursanmeldungDao kursanmeldungDao = new KursanmeldungDao();

    // input
    private final List<Schueler> schuelerList;
    private final Semester semester;
    private final Wochentag wochentag;  // nullable
    private final Time zeitBeginn;      // nullable
    private final Mitarbeiter mitarbeiter;  // nullable
    private final Calendar stichtagSchuelerSuchen;  // nullable
    private final boolean keineAbgemeldetenKurseAnzeigen;
    private final Calendar anmeldemonat;  // nullable
    private final Calendar abmeldemonat;  // nullable

    // output
    private final Map<Schueler, List<Kurs>> kurseMap = new HashMap<>();

    @SuppressWarnings("java:S107")
    public FindKurseMapSchuelerSemesterCommand(List<Schueler> schuelerList, Semester semester, Wochentag wochentag, Time zeitBeginn, Mitarbeiter mitarbeiter, Calendar stichtagSchuelerSuchen, boolean keineAbgemeldetenKurseAnzeigen, Calendar anmeldemonat, Calendar abmeldemonat) {
        this.schuelerList = schuelerList;
        this.semester = semester;
        this.wochentag = wochentag;
        this.zeitBeginn = zeitBeginn;
        this.mitarbeiter = mitarbeiter;
        this.stichtagSchuelerSuchen = stichtagSchuelerSuchen;
        this.keineAbgemeldetenKurseAnzeigen = keineAbgemeldetenKurseAnzeigen;
        this.anmeldemonat = anmeldemonat;
        this.abmeldemonat = abmeldemonat;
    }

    @Override
    public void execute() {
        if (semester == null) {
            return;
        }
        for (Schueler schueler : schuelerList) {
            List<Kursanmeldung> kursanmeldungenFound = kursanmeldungDao.findKursanmeldungen(schueler, semester, wochentag, zeitBeginn, mitarbeiter, anmeldemonat, abmeldemonat, keineAbgemeldetenKurseAnzeigen, stichtagSchuelerSuchen);
            List<Kurs> kurse = new ArrayList<>();
            for (Kursanmeldung kursanmeldung : kursanmeldungenFound) {
                kurse.add(kursanmeldung.getKurs());
            }
            kurseMap.put(schueler, kurse);
        }
    }

    public Map<Schueler, List<Kurs>> getKurseMap() {
        return kurseMap;
    }
}
