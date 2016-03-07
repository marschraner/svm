package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Semester;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

/**
 * @author Martin Schraner
 */
public class CalculateMonatsstatistikKurseCommand extends GenericDaoCommand {

    // input
    private Calendar monatJahr;

    // output
    private int anzahlAnmeldungen;
    private int anzahlAbmeldungen;
    private int anzahllLektionen;

    Calendar statistikMonatBeginn;
    Calendar statistikMonatEnde;
    private List<Semester> semestersAll;
    private Set<Integer> schuelerIdsAll = new HashSet<>();
    private Map<Integer, Integer> schuelerIdsAnzahlAnmeldungen = new HashMap<>();
    private Map<Integer, Integer> schuelerIdsAnzahlAbmeldungen = new HashMap<>();

    public CalculateMonatsstatistikKurseCommand(Calendar monatJahr) {
        this.monatJahr = monatJahr;
    }

    @Override
    public void execute() {
        determineStatistikMonatBeginn();
        determineStatistikMonatEnde();
        determineSemestersAll();
        calculateAnzahlLektionen();
        calculateAnzahlAnmeldungenSchueler();
        calculateAnzahlAbmeldungenSchueler();
        Semester previousSemesterBeforeSemesterbeginn = checkIfMonatContainsSemesterbeginnAndGetPreviousSemester();
        if (previousSemesterBeforeSemesterbeginn != null && !schuelerIdsAnzahlAnmeldungen.isEmpty()) {
            // Implizite Abmeldungen nur berücksichtigen, falls bereits Schüler fürs nächste Semester erfasst wurden
            calculateAnzahlImpliziteAbmeldungenVorhergehendesSemesterSchueler(previousSemesterBeforeSemesterbeginn);
        }
        calculateAnzahlAnmeldungenAbmeldungenTotal();
    }

    private void calculateAnzahlLektionen() {
        Semester aktuellesSemester = getAktuellesSemester(semestersAll);
        if (aktuellesSemester == null) {
            anzahllLektionen = 0;
            return;
        }
        // Eine Anmeldung dauert bis und mit Abmeldedatum
        // Eine Kursanmeldung des Vorsemesters darf bis zu 31 Tagen ins neue Semester hineinreichen (für Schüler,
        // die sich im laufenden Semester innerhalb 31 Tagen wieder abmelden und daher keine Rechnung erhalten sollen)
        Semester nachfolgendesSemester = getNachfolgendesSemester(aktuellesSemester);
        Query query;
        if (nachfolgendesSemester != null) {
            // Eine Kursanmeldung des Vorsemesters darf bis zu 90 Tagen ins alte Semester hineinreichen (für Schüler,
            // die am Ende des vorhergehenden Semesters begonnen haben und keine Rechnung mehr fürs alte Semester erhalten sollen)
            query = entityManager.createQuery("select count(k) from Kursanmeldung k where" +
                    " k.anmeldedatum <= :statistikMonatEnde and" +
                    " (k.abmeldedatum >= :statistikMonatEnde or" +
                    " (k.abmeldedatum is null and (k.kurs.semester.semesterId = :semesterId or k.kurs.semester.semesterId = :semesterIdNachfolgendesSemester)))");
            query.setParameter("semesterIdNachfolgendesSemester", nachfolgendesSemester.getSemesterId());
        } else {
            query = entityManager.createQuery("select count(k) from Kursanmeldung k where" +
                    " k.anmeldedatum <= :statistikMonatEnde and" +
                    " (k.abmeldedatum >= :statistikMonatEnde or" +
                    " (k.abmeldedatum is null and k.kurs.semester.semesterId = :semesterId))");
        }
        query.setParameter("semesterId", aktuellesSemester.getSemesterId());
        query.setParameter("statistikMonatEnde", statistikMonatEnde);
        anzahllLektionen = (int) (long) query.getSingleResult();
    }

    private void calculateAnzahlAnmeldungenSchueler() {
        TypedQuery<Object[]> typedQuery = entityManager.createQuery("select k.schueler.personId, count(k) from Kursanmeldung k where" +
                " (k.anmeldedatum >= :statistikMonatBeginn and k.anmeldedatum <= :statistikMonatEnde) group by k.schueler.personId", Object[].class);
        typedQuery.setParameter("statistikMonatBeginn", statistikMonatBeginn);
        typedQuery.setParameter("statistikMonatEnde", statistikMonatEnde);
        for (Object[] result : typedQuery.getResultList()) {
            Integer schuelerId = (Integer) result[0];
            schuelerIdsAnzahlAnmeldungen.put(schuelerId, (int) (long) result[1]);
            schuelerIdsAll.add(schuelerId);
        }
    }

    private void calculateAnzahlAbmeldungenSchueler() {
        TypedQuery<Object[]> typedQuery = entityManager.createQuery("select k.schueler.personId, count(k) from Kursanmeldung k where" +
                " (k.abmeldedatum >= :statistikMonatBeginn and k.abmeldedatum <= :statistikMonatEnde) group by k.schueler.personId", Object[].class);
        typedQuery.setParameter("statistikMonatBeginn", statistikMonatBeginn);
        typedQuery.setParameter("statistikMonatEnde", statistikMonatEnde);
        for (Object[] result : typedQuery.getResultList()) {
            Integer schuelerId = (Integer) result[0];
            schuelerIdsAnzahlAbmeldungen.put(schuelerId, (int) (long) result[1]);
            schuelerIdsAll.add(schuelerId);
        }
    }

    private void calculateAnzahlImpliziteAbmeldungenVorhergehendesSemesterSchueler(Semester previousSemesterBeforeSemesterbeginn) {
        TypedQuery<Object[]> typedQuery = entityManager.createQuery("select k.schueler.personId, count(k) from Kursanmeldung k where" +
                " k.kurs.semester.semesterId = :semesterIdPreviousSemester and k.abmeldedatum is null group by k.schueler.personId", Object[].class);
        typedQuery.setParameter("semesterIdPreviousSemester", previousSemesterBeforeSemesterbeginn.getSemesterId());
        for (Object[] result : typedQuery.getResultList()) {
            Integer schuelerId = (Integer) result[0];
            int anzahlAbmeldungenSchueler = (int) (long) result[1];
            if (schuelerIdsAnzahlAbmeldungen.containsKey(schuelerId)) {
                anzahlAbmeldungenSchueler += schuelerIdsAnzahlAbmeldungen.get(schuelerId);
            }
            schuelerIdsAnzahlAbmeldungen.put(schuelerId, anzahlAbmeldungenSchueler);
            schuelerIdsAll.add(schuelerId);
        }
    }

    private void calculateAnzahlAnmeldungenAbmeldungenTotal() {
        for (Integer schuelerId : schuelerIdsAll) {
            if (schuelerIdsAnzahlAnmeldungen.containsKey(schuelerId) && schuelerIdsAnzahlAbmeldungen.containsKey(schuelerId)) {
                int netto = schuelerIdsAnzahlAnmeldungen.get(schuelerId) - schuelerIdsAnzahlAbmeldungen.get(schuelerId);
                if (netto > 0) {
                    anzahlAnmeldungen += netto;
                } else if (netto < 0) {
                    anzahlAbmeldungen += -netto;
                }
            } else if (schuelerIdsAnzahlAnmeldungen.containsKey(schuelerId)) {
                anzahlAnmeldungen += schuelerIdsAnzahlAnmeldungen.get(schuelerId);
            } else if (schuelerIdsAnzahlAbmeldungen.containsKey(schuelerId)) {
                anzahlAbmeldungen += schuelerIdsAnzahlAbmeldungen.get(schuelerId);
            }
        }
    }

    private void determineStatistikMonatBeginn() {
        statistikMonatBeginn = new GregorianCalendar(monatJahr.get(Calendar.YEAR), monatJahr.get(Calendar.MONTH), 1);
    }

    private void determineStatistikMonatEnde() {
        if (monatJahr.get(Calendar.MONTH) == Calendar.DECEMBER) {
            statistikMonatEnde = new GregorianCalendar(monatJahr.get(Calendar.YEAR) + 1, Calendar.JANUARY, 1);
        } else {
            statistikMonatEnde = new GregorianCalendar(monatJahr.get(Calendar.YEAR), monatJahr.get(Calendar.MONTH) + 1, 1);
        }
        statistikMonatEnde.add(Calendar.DAY_OF_YEAR, -1);
    }

    private Semester getAktuellesSemester(List<Semester> semestersAll) {
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(statistikMonatEnde, semestersAll);
        findSemesterForCalendarCommand.execute();
        Semester currentSemester = findSemesterForCalendarCommand.getCurrentSemester();
        Semester previousSemester = findSemesterForCalendarCommand.getPreviousSemester();
        if (currentSemester != null) {
            // Innerhalb Semester
            return currentSemester;
        }
        else {
            // Semesterferien (-> vorhergehendes Semester)
            return previousSemester;
        }
    }

    private Semester getNachfolgendesSemester(Semester aktuellesSemester) {
        FindNextSemesterCommand findNextSemesterCommand = new FindNextSemesterCommand(aktuellesSemester);
        findNextSemesterCommand.setEntityManager(entityManager);
        findNextSemesterCommand.execute();
        return findNextSemesterCommand.getNextSemester();
    }

    private void determineSemestersAll() {
        FindAllSemestersCommand findAllSemestersCommand = new FindAllSemestersCommand();
        findAllSemestersCommand.setEntityManager(entityManager);
        findAllSemestersCommand.execute();
        semestersAll = findAllSemestersCommand.getSemestersAll();
    }

    private Semester checkIfMonatContainsSemesterbeginnAndGetPreviousSemester() {
        Collections.sort(semestersAll);
        for (int i = 0; i < semestersAll.size(); i++) {
            if (!semestersAll.get(i).getSemesterbeginn().before(statistikMonatBeginn) && !semestersAll.get(i).getSemesterbeginn().after(statistikMonatEnde)) {
                if (i < semestersAll.size() - 1) {
                    return semestersAll.get(i+1);  // vorhergehendes Semester
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public int getAnzahlAnmeldungen() {
        return anzahlAnmeldungen;
    }

    public int getAnzahlAbmeldungen() {
        return anzahlAbmeldungen;
    }

    public int getAnzahllLektionen() {
        return anzahllLektionen;
    }

}
