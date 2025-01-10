package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.persistence.entities.*;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class KursDao extends GenericDao<Kurs, Integer> {

    private static final String SEMESTER_ID = "semesterId";

    @Override
    public void remove(Kurs kurs) {

        // Entferne Kurs von Semester, Kurstyp und Kursort
        Semester semester = kurs.getSemester();
        semester.getKurse().remove(kurs);

        Kurstyp kurstyp = kurs.getKurstyp();
        kurstyp.getKurse().remove(kurs);

        Kursort kursort = kurs.getKursort();
        kursort.getKurse().remove(kurs);

        // Entferne zugewiesene Lehrkräfte
        for (Mitarbeiter mitarbeiter : new ArrayList<>(kurs.getLehrkraefte())) {
            kurs.deleteLehrkraft(mitarbeiter);
        }

        // Lösche Kurs aus DB
        db.getCurrentEntityManager().remove(kurs);
    }

    public List<Kurs> findKurseSemester(Semester semester) {
        TypedQuery<Kurs> typedQuery = db.getCurrentEntityManager().createQuery(
                "select k from Kurs k where k.semester.semesterId = :semesterId", Kurs.class);
        typedQuery.setParameter(SEMESTER_ID, semester.getSemesterId());
        List<Kurs> kurseFound = typedQuery.getResultList();
        // Sortieren gemäss compareTo in Kurs
        Collections.sort(kurseFound);
        return kurseFound;
    }

    @SuppressWarnings("DuplicatedCode")
    public List<Kurs> findKurse(Semester semester, Wochentag wochentag, Time zeitBeginn, Mitarbeiter mitarbeiter) {
        StringBuilder selectStatementSb = new StringBuilder("select k from Kurs k");
        if (mitarbeiter != null) {
            selectStatementSb.append(" join k.lehrkraefte lk");
        }
        selectStatementSb.append(" where");
        if (semester != null) {
            selectStatementSb.append(" k.semester.semesterId = :semesterId and");
        }
        if (wochentag != null) {
            selectStatementSb.append(" k.wochentag = :wochentag and");
        }
        if (zeitBeginn != null) {
            selectStatementSb.append(" k.zeitBeginn = :zeitBeginn and");
        }
        if (mitarbeiter != null) {
            selectStatementSb.append(" lk.personId = :lehrkraftPersonId and");
        }
        // Letztes " and" löschen
        if (selectStatementSb.substring(selectStatementSb.length() - 4).equals(" and")) {
            selectStatementSb.setLength(selectStatementSb.length() - 4);
        }
        // "where" löschen, falls dieses am Schluss steht
        if (selectStatementSb.substring(selectStatementSb.length() - 5).equals("where")) {
            selectStatementSb.setLength(selectStatementSb.length() - 5);
        }
        // Query
        TypedQuery<Kurs> typedQuery = db.getCurrentEntityManager().createQuery(
                selectStatementSb.toString(), Kurs.class);
        if (semester != null) {
            typedQuery.setParameter(SEMESTER_ID, semester.getSemesterId());
        }
        if (wochentag != null) {
            typedQuery.setParameter("wochentag", wochentag);
        }
        if (zeitBeginn != null) {
            typedQuery.setParameter("zeitBeginn", zeitBeginn);
        }
        if (mitarbeiter != null) {
            typedQuery.setParameter("lehrkraftPersonId", mitarbeiter.getPersonId());
        }
        List<Kurs> kurseFound = typedQuery.getResultList();
        // Sortieren gemäss compareTo in Kurs
        Collections.sort(kurseFound);
        return kurseFound;
    }

    public Kurs findKurs(Semester semester, Wochentag wochentag, Time zeitBeginn, Mitarbeiter mitarbeiter) {
        Kurs kursFound;
        try {
            TypedQuery<Kurs> typedQuery = db.getCurrentEntityManager().createQuery(
                    "select k from Kurs k join k.lehrkraefte lk " +
                    " where k.semester.semesterId = :semesterId and k.wochentag = :wochentag and k.zeitBeginn = :zeitBeginn " +
                    " and lk.personId = :lehrkraftPersonId", Kurs.class);
            typedQuery.setParameter(SEMESTER_ID, semester.getSemesterId());
            typedQuery.setParameter("wochentag", wochentag);
            typedQuery.setParameter("zeitBeginn", zeitBeginn);
            typedQuery.setParameter("lehrkraftPersonId", mitarbeiter.getPersonId());
            kursFound = typedQuery.getSingleResult();
        } catch (NoResultException e) {
            kursFound = null;
        }
        return kursFound;
    }

}
