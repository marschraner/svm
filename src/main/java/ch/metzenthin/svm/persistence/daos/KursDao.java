package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.dataTypes.Wochentag;
import ch.metzenthin.svm.persistence.entities.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.sql.Time;
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

        // Entferne zugewiesene Lehrkräfte
        for (Lehrkraft lehrkraft : new ArrayList<>(kurs.getLehrkraefte())) {
            kurs.deleteLehrkraft(lehrkraft);
        }

        // Lösche Kurs aus DB
        entityManager.remove(kurs);
    }

    public List<Kurs> findKurseSemester(Semester semester) {
        TypedQuery<Kurs> typedQuery = entityManager.createQuery("select k from Kurs k where k.semester.schuljahr = :schuljahr and k.semester.semesterbezeichnung = :semesterbezeichnung order by k.kurstyp.bezeichnung, k.stufe, k.wochentag, k.zeitBeginn, k.zeitEnde, k.kursort.bezeichnung", Kurs.class);
        typedQuery.setParameter("schuljahr", semester.getSchuljahr());
        typedQuery.setParameter("semesterbezeichnung", semester.getSemesterbezeichnung());
        return typedQuery.getResultList();
    }

    public List<Kurs> findKurseSchuelerSemester(Schueler schueler, Semester semester, Wochentag wochentag, Time zeitBeginn, Lehrkraft lehrkraft) {
        StringBuilder selectStatementSb = new StringBuilder("select k from Kurs k join k.schueler s");
        if (lehrkraft != null) {
            selectStatementSb.append(" join k.lehrkraefte lk");
        }
        selectStatementSb.append(" where k.semester.schuljahr = :schuljahr and k.semester.semesterbezeichnung = :semesterbezeichnung and s.nachname = :schuelerNachname and s.vorname = :schuelerVorname and s.geburtsdatum =:schuelerGeburtsdatum and s.adresse.strasse = :schuelerStrasse and");
        if (wochentag != null) {
            selectStatementSb.append(" k.wochentag = :wochentag and");
        }
        if (zeitBeginn != null) {
            selectStatementSb.append(" k.zeitBeginn = :zeitBeginn and");
        }
        if (lehrkraft != null) {
            selectStatementSb.append(" lk.nachname = :lehrkraftNachname and lk.vorname = :lehrkraftVorname and lk.geburtsdatum = :lehrkraftGeburtsdatum and");
        }
        // Letztes " and" löschen
        if (selectStatementSb.substring(selectStatementSb.length() - 4).equals(" and")) {
            selectStatementSb.setLength(selectStatementSb.length() - 4);
        }
        // Sortierung
        selectStatementSb.append(" order by k.kurstyp.bezeichnung, k.stufe, k.wochentag, k.zeitBeginn, k.zeitEnde, k.kursort.bezeichnung");
        // Query
        TypedQuery<Kurs> typedQuery = entityManager.createQuery(selectStatementSb.toString(), Kurs.class);
        typedQuery.setParameter("schuljahr", semester.getSchuljahr());
        typedQuery.setParameter("semesterbezeichnung", semester.getSemesterbezeichnung());
        typedQuery.setParameter("schuelerNachname", schueler.getNachname());
        typedQuery.setParameter("schuelerVorname", schueler.getVorname());
        typedQuery.setParameter("schuelerGeburtsdatum", schueler.getGeburtsdatum());
        typedQuery.setParameter("schuelerStrasse", schueler.getAdresse().getStrasse());
        if (wochentag != null) {
            typedQuery.setParameter("wochentag", wochentag);
        }
        if (zeitBeginn != null) {
            typedQuery.setParameter("zeitBeginn", zeitBeginn);
        }
        if (lehrkraft != null) {
            typedQuery.setParameter("lehrkraftNachname", lehrkraft.getNachname());
            typedQuery.setParameter("lehrkraftVorname", lehrkraft.getVorname());
            typedQuery.setParameter("lehrkraftGeburtsdatum", lehrkraft.getGeburtsdatum());
        }
        return typedQuery.getResultList();
    }

    public Kurs findKurs(Semester semester, Wochentag wochentag, Time zeitBeginn, Lehrkraft lehrkraft) {
        Kurs kursFound;
        try {
            TypedQuery<Kurs> typedQuery = entityManager.createQuery("select k from Kurs k join k.lehrkraefte lk " +
                    " where k.semester.schuljahr = :schuljahr and k.semester.semesterbezeichnung = :semesterbezeichnung and k.wochentag = :wochentag and k.zeitBeginn = :zeitBeginn " +
                    " and lk.nachname = :lehrkraftNachname and lk.vorname = :lehrkraftVorname and lk.geburtsdatum = :lehrkraftGeburtsdatum", Kurs.class);
            typedQuery.setParameter("schuljahr", semester.getSchuljahr());
            typedQuery.setParameter("semesterbezeichnung", semester.getSemesterbezeichnung());
            typedQuery.setParameter("wochentag", wochentag);
            typedQuery.setParameter("zeitBeginn", zeitBeginn);
            typedQuery.setParameter("lehrkraftNachname", lehrkraft.getNachname());
            typedQuery.setParameter("lehrkraftVorname", lehrkraft.getVorname());
            typedQuery.setParameter("lehrkraftGeburtsdatum", lehrkraft.getGeburtsdatum());
            kursFound = typedQuery.getSingleResult();
        } catch (NoResultException e) {
            kursFound = null;
        }
        return kursFound;
    }

    public Schueler addToSchuelerAndSave(Kurs kurs, Schueler schueler) {
        schueler.addKurs(kurs);
        entityManager.persist(schueler);
        return schueler;
    }

    public Schueler removeFromSchuelerAndUpdate(Kurs kurs, Schueler schueler) {
        schueler.deleteKurs(kurs);
        entityManager.persist(schueler);
        entityManager.flush();
        entityManager.refresh(kurs);
        return schueler;
    }

}
