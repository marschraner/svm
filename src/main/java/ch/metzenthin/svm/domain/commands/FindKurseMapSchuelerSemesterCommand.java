package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.persistence.daos.KursanmeldungDao;
import ch.metzenthin.svm.persistence.entities.*;

import java.sql.Time;
import java.util.*;

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
    private final Calendar stichtagSchuelerSuchen;  // nullable
    private final boolean nurKursanmeldungenOhneVorzeitigeAbmeldung;
    private final Calendar anmeldemonat;  // nullable
    private final Calendar abmeldemonat;  // nullable

    // output
    private Map<Schueler, List<Kurs>> kurseMap = new HashMap<>();

    public FindKurseMapSchuelerSemesterCommand(List<Schueler> schuelerList, Semester semester, Wochentag wochentag, Time zeitBeginn, Mitarbeiter mitarbeiter, Calendar stichtagSchuelerSuchen, boolean nurKursanmeldungenOhneVorzeitigeAbmeldung, Calendar anmeldemonat, Calendar abmeldemonat) {
        this.schuelerList = schuelerList;
        this.semester = semester;
        this.wochentag = wochentag;
        this.zeitBeginn = zeitBeginn;
        this.mitarbeiter = mitarbeiter;
        this.stichtagSchuelerSuchen = stichtagSchuelerSuchen;
        this.nurKursanmeldungenOhneVorzeitigeAbmeldung = nurKursanmeldungenOhneVorzeitigeAbmeldung;
        this.anmeldemonat = anmeldemonat;
        this.abmeldemonat = abmeldemonat;
    }

    @Override
    public void execute() {

        if (semester == null) {
            return;
        }

        KursanmeldungDao kursanmeldungDao = new KursanmeldungDao(entityManager);

        Calendar stichdatumKursabmeldung = null;
        if (nurKursanmeldungenOhneVorzeitigeAbmeldung) {
            stichdatumKursabmeldung = semester.getStichdatumVorzeitigeAbmeldung();
            // Kursabmeldungen später als der bei der Schülersuche verwendete Stichtag sollen alle angezeigt werden
            // -> Falls der Stichtag für Schülersuche früher ist als das Stichdatum für vorzeitige Abmeldung ersteres verwenden
            if (stichtagSchuelerSuchen != null && stichtagSchuelerSuchen.before(stichdatumKursabmeldung)) {
                stichdatumKursabmeldung = stichtagSchuelerSuchen;
            }
        }

        for (Schueler schueler : schuelerList) {
            List<Kursanmeldung> kursanmeldungenFound = kursanmeldungDao.findKursanmeldungen(schueler, semester, wochentag, zeitBeginn, mitarbeiter, anmeldemonat, abmeldemonat, stichdatumKursabmeldung);
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
