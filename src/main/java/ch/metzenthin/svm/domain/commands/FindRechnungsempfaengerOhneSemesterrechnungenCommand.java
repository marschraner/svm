package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semester;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindRechnungsempfaengerOhneSemesterrechnungenCommand implements Command {

    private final DB db = DBFactory.getInstance();

    // input
    private Semester currentSemester;

    // output
    private List<Angehoeriger> rechnungsempfaengersFound = new ArrayList<>();

    public FindRechnungsempfaengerOhneSemesterrechnungenCommand(Semester currentSemester) {
        this.currentSemester = currentSemester;
    }

    @Override
    public void execute() {

        if (currentSemester == null) {
            return;
        }

        // 1. Vorhergehendes Semester
        FindPreviousSemesterCommand findPreviousSemesterCommand = new FindPreviousSemesterCommand(currentSemester);
        findPreviousSemesterCommand.execute();
        Semester previousSemester = findPreviousSemesterCommand.getPreviousSemester();

        TypedQuery<Angehoeriger> typedQuery;
        EntityManager entityManager = db.getCurrentEntityManager();
        if (previousSemester != null) {
            // Suche nach aktuellen Kursen und Kursen des Vorsemesters ohne Abmeldung
            typedQuery = entityManager.createQuery("select distinct rech from Angehoeriger rech" +
                    " join rech.schuelerRechnungsempfaenger sch" +
                    " join sch.kursanmeldungen kursanm" +
                    " where (kursanm.kurs.semester.semesterId = :currentSemesterId or" +
                    " (kursanm.kurs.semester.semesterId = :previousSemesterId and kursanm.abmeldedatum is null)) and" +
                    " not exists (select semre.rechnungsempfaenger from Semesterrechnung semre where semre.rechnungsempfaenger.personId = rech.personId and semre.semester.semesterId = :currentSemesterId)", Angehoeriger.class);

            typedQuery.setParameter("previousSemesterId", previousSemester.getSemesterId());
        } else {
            // Nur Suche nach aktuellen Kursen, da Vorsemester nicht erfasst
            typedQuery = entityManager.createQuery("select distinct rech from Angehoeriger rech" +
                    " join rech.schuelerRechnungsempfaenger sch" +
                    " join sch.kursanmeldungen kursanm" +
                    " where kursanm.kurs.semester.semesterId = :currentSemesterId and" +
                    " not exists (select semre.rechnungsempfaenger from Semesterrechnung semre where semre.rechnungsempfaenger.personId = rech.personId and semre.semester.semesterId = :currentSemesterId)", Angehoeriger.class);
        }

        typedQuery.setParameter("currentSemesterId", currentSemester.getSemesterId());

        rechnungsempfaengersFound = typedQuery.getResultList();
    }

    List<Angehoeriger> getRechnungsempfaengersFound() {
        return rechnungsempfaengersFound;
    }
}
