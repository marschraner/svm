package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterDao;
import ch.metzenthin.svm.persistence.daos.SemesterrechnungCodeDao;
import ch.metzenthin.svm.persistence.daos.SemesterrechnungDao;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungId;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateSemesterrechnungCommand implements Command {

    private final SemesterrechnungDao semesterrechnungDao = new SemesterrechnungDao();
    private final SemesterrechnungCodeDao semesterrechnungCodeDao = new SemesterrechnungCodeDao();
    private final SemesterDao semesterDao = new SemesterDao();

    // input
    private Semesterrechnung semesterrechnung;
    private final SemesterrechnungCode semesterrechnungCode;
    private Semesterrechnung semesterrechnungOrigin;
    private List<Semesterrechnung> bereitsErfassteSemesterrechnungen;


    public SaveOrUpdateSemesterrechnungCommand(Semesterrechnung semesterrechnung, SemesterrechnungCode semesterrechnungCode, Semesterrechnung semesterrechnungOrigin, List<Semesterrechnung> bereitsErfassteSemesterrechnungen) {
        this.semesterrechnung = semesterrechnung;
        this.semesterrechnungCode = semesterrechnungCode;
        this.semesterrechnungOrigin = semesterrechnungOrigin;
        this.bereitsErfassteSemesterrechnungen = bereitsErfassteSemesterrechnungen;
    }

    @Override
    public void execute() {
        // Reload zur Verhinderung von Lazy Loading-Problem
        SemesterrechnungCode semesterrechnungCodeReloaded = null;
        if (semesterrechnungCode != null) {
            semesterrechnungCodeReloaded = semesterrechnungCodeDao.findById(semesterrechnungCode.getCodeId());
        }
        if (semesterrechnungOrigin != null) {
            // Update von semesterrechnungOrigin mit Werten von semesterrechnung
            semesterrechnungOrigin.copyAttributesFrom(semesterrechnung);
            semesterrechnungOrigin.setSemesterrechnungCode(semesterrechnungCodeReloaded);
            // Reload zur Verhinderung von Lazy Loading-Problem
            Semesterrechnung semesterrechnungOriginReloaded = semesterrechnungDao.findById(new SemesterrechnungId(semesterrechnungOrigin.getSemester().getSemesterId(), semesterrechnungOrigin.getRechnungsempfaenger().getPersonId()));
            semesterrechnungDao.save(semesterrechnungOriginReloaded);
        } else {
            semesterrechnung.setSemesterrechnungCode(semesterrechnungCodeReloaded);
            // Reload zur Verhinderung von Lazy Loading-Problem
            Semester semesterReloaded = semesterDao.findById(semesterrechnung.getSemester().getSemesterId());
            semesterrechnung.setSemester(semesterReloaded);
            Semesterrechnung semesterrechnungSaved = semesterrechnungDao.save(semesterrechnung);
            bereitsErfassteSemesterrechnungen.add(semesterrechnungSaved);
            Collections.sort(bereitsErfassteSemesterrechnungen);
        }
    }

}
