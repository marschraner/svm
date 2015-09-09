package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Semester;

import javax.persistence.Query;
import java.util.Calendar;
import java.util.GregorianCalendar;

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

    public CalculateMonatsstatistikKurseCommand(Calendar monatJahr) {
        this.monatJahr = monatJahr;
    }

    @Override
    public void execute() {

        calculateAnzahlAnmeldungen();
        calculateAnzahlAbmeldungen();
        calculateAnzahlLektionen();
    }

    private void calculateAnzahlAnmeldungen() {
        Query query = entityManager.createQuery("select count(k) from Kursanmeldung k where k.anmeldedatum >= :statistikMonatBeginn and k.anmeldedatum <= :statistikMonatEnde");
        query.setParameter("statistikMonatBeginn", getStatistikMonatBeginn());
        query.setParameter("statistikMonatEnde", getStatistikMonatEnde());
        anzahlAnmeldungen = (int) (long) query.getSingleResult();
    }

    private void calculateAnzahlAbmeldungen() {
        Query query = entityManager.createQuery("select count(k) from Kursanmeldung k where k.abmeldedatum >= :statistikMonatBeginn and k.abmeldedatum <= :statistikMonatEnde");
        query.setParameter("statistikMonatBeginn", getStatistikMonatBeginn());
        query.setParameter("statistikMonatEnde", getStatistikMonatEnde());
        anzahlAbmeldungen = (int) (long) query.getSingleResult();
    }

    private void calculateAnzahlLektionen() {
        Semester relevantesSemester = getRelevantesSemester();
        if (relevantesSemester == null) {
            anzahllLektionen = 0;
            return;
        }
        Query query = entityManager.createQuery("select count(k) from Kursanmeldung k where k.kurs.semester.semesterId = :semesterId and" +
                " (k.anmeldedatum is null or k.anmeldedatum <= :statistikMonatEnde) and" +
                " (k.abmeldedatum is null or k.abmeldedatum > :statistikMonatEnde)");
        query.setParameter("semesterId", getRelevantesSemester().getSemesterId());
        query.setParameter("statistikMonatEnde", getStatistikMonatEnde());
        anzahllLektionen = (int) (long) query.getSingleResult();
    }

    private Calendar getStatistikMonatBeginn() {
        return new GregorianCalendar(monatJahr.get(Calendar.YEAR), monatJahr.get(Calendar.MONTH), 1);
    }

    private Calendar getStatistikMonatEnde() {
        Calendar statistikMonatEnde;
        if (monatJahr.get(Calendar.MONTH) == Calendar.DECEMBER) {
            statistikMonatEnde = new GregorianCalendar(monatJahr.get(Calendar.YEAR) + 1, Calendar.JANUARY, 1);
        } else {
            statistikMonatEnde = new GregorianCalendar(monatJahr.get(Calendar.YEAR), monatJahr.get(Calendar.MONTH) + 1, 1);
        }
        statistikMonatEnde.add(Calendar.DAY_OF_YEAR, -1);
        return statistikMonatEnde;
    }

    private Semester getRelevantesSemester() {
        FindAllSemestersCommand findAllSemestersCommand = new FindAllSemestersCommand();
        findAllSemestersCommand.setEntityManager(entityManager);
        findAllSemestersCommand.execute();
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(getStatistikMonatEnde(), findAllSemestersCommand.getSemestersAll());
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
