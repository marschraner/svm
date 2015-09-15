package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semester;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindRechnungsempfaengerOhneSemesterrechnungenCommand extends GenericDaoCommand {

    // input
    private Semester semester;

    // output
    private List<Angehoeriger> rechnungsempfaengersFound = new ArrayList<>();

    public FindRechnungsempfaengerOhneSemesterrechnungenCommand(Semester semester) {
        this.semester = semester;
    }

    @Override
    public void execute() {

        if (semester == null) {
            return;
        }

        TypedQuery<Angehoeriger>typedQuery = entityManager.createQuery("select distinct rech from Angehoeriger rech" +
                " join rech.schuelerRechnungsempfaenger sch" +
                " join sch.kursanmeldungen kursanm" +
                " where kursanm.kurs.semester.semesterId = :semesterId and " +
                " not exists (select semre.rechnungsempfaenger from Semesterrechnung semre where semre.rechnungsempfaenger.personId = rech.personId and semre.semester.semesterId = :semesterId)", Angehoeriger.class);

        typedQuery.setParameter("semesterId", semester.getSemesterId());

        rechnungsempfaengersFound = typedQuery.getResultList();
    }

    public List<Angehoeriger> getRechnungsempfaengersFound() {
        return rechnungsempfaengersFound;
    }
}
