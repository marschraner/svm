package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Wochentag;
import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.sql.Time;
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
    private final Lehrkraft lehrkraft;  // nullable

    // output
    private Map<Schueler, List<Kurs>> kurseMap = new HashMap<>();

    public FindKurseMapSchuelerSemesterCommand(List<Schueler> schuelerList, Semester semester, Wochentag wochentag, Time zeitBeginn, Lehrkraft lehrkraft) {
        this.schuelerList = schuelerList;
        this.semester = semester;
        this.wochentag = wochentag;
        this.zeitBeginn = zeitBeginn;
        this.lehrkraft = lehrkraft;
    }

    @Override
    public void execute() {
        if (semester == null) {
            return;
        }
        KursDao kursDao = new KursDao(entityManager);
        for (Schueler schueler : schuelerList) {
            kurseMap.put(schueler, kursDao.findKurseSchuelerSemester(schueler, semester, wochentag, zeitBeginn, lehrkraft));
        }
    }

    public Map<Schueler, List<Kurs>> getKurseMap() {
        return kurseMap;
    }
}
