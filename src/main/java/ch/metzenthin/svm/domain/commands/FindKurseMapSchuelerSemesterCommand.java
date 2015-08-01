package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Schraner
 */
public class FindKurseMapSchuelerSemesterCommand extends GenericDaoCommand {

    // input
    private List<Schueler> schuelerList;
    private Semester semester;

    // output
    private Map<Schueler, List<Kurs>> kurseMap = new HashMap<>();

    public FindKurseMapSchuelerSemesterCommand(List<Schueler> schuelerList, Semester semester) {
        this.schuelerList = schuelerList;
        this.semester = semester;
    }

    @Override
    public void execute() {
        if (semester == null) {
            return;
        }
        KursDao kursDao = new KursDao(entityManager);
        for (Schueler schueler : schuelerList) {
            kurseMap.put(schueler, kursDao.findKurseSchuelerSemester(schueler, semester));
        }
    }

    public Map<Schueler, List<Kurs>> getKurseMap() {
        return kurseMap;
    }
}
