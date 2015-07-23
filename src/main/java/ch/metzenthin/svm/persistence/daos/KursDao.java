package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class KursDao extends GenericDao<Kurs, Integer> {

    public KursDao(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void remove(Kurs kurs) {

        // Entferne Kurs von Semester, Kurstyp und Kursort
        Semester semester = kurs.getSemester();
        semester.getKurse().remove(kurs);

        Kurstyp kurstyp = kurs.getKurstyp();
        kurstyp.getKurse().remove(kurs);

        Kursort kursort = kurs.getKursort();
        kursort.getKurse().remove(kurs);

        // Lösche zugewiesene Lehrkräfte
        for (Lehrkraft lehrkraft : new ArrayList<>(kurs.getLehrkraefte())) {
            kurs.deleteLehrkraft(lehrkraft);
        }

        // Lösche Kurs aus DB
        entityManager.remove(kurs);
    }

    
    public List<Kurs> findKurseSemester(Semester semester) {
        TypedQuery<Kurs> typedQuery = entityManager.createQuery("select k from Kurs k where k.semester.schuljahr = :schuljahr and k.semester.semesterbezeichnung = :semesterbezeichnung order by k.semester, k.kurstyp, k.stufe, k.wochentag, k.zeitBeginn, k.zeitEnde, k.kursort", Kurs.class);
        typedQuery.setParameter("schuljahr", semester.getSchuljahr());
        typedQuery.setParameter("semesterbezeichnung", semester.getSemesterbezeichnung());
        return typedQuery.getResultList();
    }
    
    

}
