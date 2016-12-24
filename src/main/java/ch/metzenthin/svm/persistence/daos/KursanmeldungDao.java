package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.persistence.entities.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Time;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class KursanmeldungDao extends GenericDao<Kursanmeldung, KursanmeldungId> {

    public KursanmeldungDao(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Kursanmeldung save(Kursanmeldung kursanmeldung) {
        kursanmeldung.getSchueler().getKursanmeldungen().add(kursanmeldung);
        kursanmeldung.getKurs().getKursanmeldungen().add(kursanmeldung);
        entityManager.persist(kursanmeldung);
        entityManager.flush();
        entityManager.refresh(kursanmeldung);
        return kursanmeldung;
    }

    @Override
    public void remove(Kursanmeldung kursanmeldung) {
        kursanmeldung.getSchueler().getKursanmeldungen().remove(kursanmeldung);
        kursanmeldung.getKurs().getKursanmeldungen().remove(kursanmeldung);
        entityManager.remove(kursanmeldung);
    }

    public List<Kursanmeldung> findKursanmeldungenSchueler(Schueler schueler) {
        TypedQuery<Kursanmeldung> typedQuery = entityManager.createQuery("select k from Kursanmeldung k where k.schueler.personId = :personId", Kursanmeldung.class);
        typedQuery.setParameter("personId", schueler.getPersonId());
        List<Kursanmeldung> kurseinteilungenFound = typedQuery.getResultList();
        // Sortieren gemäss compareTo in Kursanmeldungen
        Collections.sort(kurseinteilungenFound);
        return kurseinteilungenFound;
    }

    public List<Kursanmeldung> findKursanmeldungen(Schueler schueler, Semester semester, Wochentag wochentag, Time zeitBeginn, Mitarbeiter mitarbeiter, Calendar anmeldemonat, Calendar abmeldemonat, boolean keineAbgemeldetenKurseAnzeigen, Calendar stichtagSchuelerSuchen) {

        StringBuilder selectStatementSb = new StringBuilder("select k from Kursanmeldung k");
        if (mitarbeiter != null) {
            selectStatementSb.append(" join k.kurs.lehrkraefte lk");
        }
        selectStatementSb.append(" where");
        if (schueler != null) {
            selectStatementSb.append(" k.schueler.personId = :schuelerPersonId and");
        }
        if (semester != null) {
            selectStatementSb.append(" k.kurs.semester.semesterId = :semesterId and");
        }
        if (wochentag != null) {
            selectStatementSb.append(" k.kurs.wochentag = :wochentag and");
        }
        if (zeitBeginn != null) {
            selectStatementSb.append(" k.kurs.zeitBeginn = :zeitBeginn and");
        }
        if (mitarbeiter != null) {
            selectStatementSb.append(" lk.personId = :lehrkraftPersonId and");
        }
        if (anmeldemonat != null) {
            selectStatementSb.append(" k.anmeldedatum >= :anmeldemonatBeginn and k.anmeldedatum <= :anmeldemonatEnde and");
        }
        if (abmeldemonat != null) {
            selectStatementSb.append(" k.abmeldedatum >= :abmeldemonatBeginn and k.abmeldedatum <= :abmeldemonatEnde and");
        }
        if (keineAbgemeldetenKurseAnzeigen && stichtagSchuelerSuchen != null) {
            selectStatementSb.append(" (k.abmeldedatum is null or k.abmeldedatum > :stichtagSchuelerSuchen) and");
        }
        // Letztes " and" löschen
        if (selectStatementSb.substring(selectStatementSb.length() - 4).equals(" and")) {
            selectStatementSb.setLength(selectStatementSb.length() - 4);
        }
        // Query
        TypedQuery<Kursanmeldung> typedQuery = entityManager.createQuery(selectStatementSb.toString(), Kursanmeldung.class);
        if (schueler != null) {
            typedQuery.setParameter("schuelerPersonId", schueler.getPersonId());
        }
        if (semester != null) {
            typedQuery.setParameter("semesterId", semester.getSemesterId());
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
        if (anmeldemonat != null) {
            typedQuery.setParameter("anmeldemonatBeginn", getMonatBeginn(anmeldemonat));
            typedQuery.setParameter("anmeldemonatEnde", getMonatEnde(anmeldemonat));
        }
        if (abmeldemonat != null) {
            typedQuery.setParameter("abmeldemonatBeginn", getMonatBeginn(abmeldemonat));
            typedQuery.setParameter("abmeldemonatEnde", getMonatEnde(abmeldemonat));
        }
        if (keineAbgemeldetenKurseAnzeigen && stichtagSchuelerSuchen != null) {
            typedQuery.setParameter("stichtagSchuelerSuchen", stichtagSchuelerSuchen);
        }
        List<Kursanmeldung> kurseinteilungenFound = typedQuery.getResultList();
        // Sortieren gemäss compareTo in Kurseinteilung
        Collections.sort(kurseinteilungenFound);
        return kurseinteilungenFound;
    }

    private Calendar getMonatBeginn(Calendar monatJahr) {
        return new GregorianCalendar(monatJahr.get(Calendar.YEAR), monatJahr.get(Calendar.MONTH), 1);
    }

    private Calendar getMonatEnde(Calendar monatJahr) {
        Calendar statistikMonatEnde;
        if (monatJahr.get(Calendar.MONTH) == Calendar.DECEMBER) {
            statistikMonatEnde = new GregorianCalendar(monatJahr.get(Calendar.YEAR) + 1, Calendar.JANUARY, 1);
        } else {
            statistikMonatEnde = new GregorianCalendar(monatJahr.get(Calendar.YEAR), monatJahr.get(Calendar.MONTH) + 1, 1);
        }
        statistikMonatEnde.add(Calendar.DAY_OF_YEAR, -1);
        return statistikMonatEnde;
    }
}

