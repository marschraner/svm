package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.persistence.daos.SemesterrechnungDao;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Martin Schraner
 */
public class UpdateWochenbetragCommand extends GenericDaoCommand {

    // input
    private Angehoeriger rechnungsempfaenger;
    private Semester currentSemester;

    // output
    private CalculateWochenbetragCommand.Result result = CalculateWochenbetragCommand.Result.WOCHENBETRAG_ERFOLGREICH_BERECHNET;

    public UpdateWochenbetragCommand(Angehoeriger rechnungsempfaenger, Semester currentSemester) {
        this.rechnungsempfaenger = rechnungsempfaenger;
        this.currentSemester = currentSemester;
    }

    @Override
    public void execute() {

        SemesterrechnungDao semesterrechnungDao = new SemesterrechnungDao(entityManager);

        // 1. Nachfolgendes Semester
        FindNextSemesterCommand findNextSemesterCommand = new FindNextSemesterCommand(currentSemester);
        findNextSemesterCommand.setEntityManager(entityManager);
        findNextSemesterCommand.execute();
        Semester nextSemester = findNextSemesterCommand.getNextSemester();

        // 2. Semesterrechnung des jetzigen und nachfolgenden Semesters
        Semesterrechnung semesterrechnungCurrentSemester = null;
        Semesterrechnung semesterrechnungNextSemester = null;
        for (Semesterrechnung semesterrechnung1 : rechnungsempfaenger.getSemesterrechnungen()) {
            if (semesterrechnung1.getSemester().getSemesterId().equals(currentSemester.getSemesterId())) {
                semesterrechnungCurrentSemester = semesterrechnung1;
            }
            if (nextSemester != null && semesterrechnung1.getSemester().getSemesterId().equals(nextSemester.getSemesterId())) {
                semesterrechnungNextSemester = semesterrechnung1;
            }
        }

        // 3. Lektionsgebühren
        FindAllLektionsgebuehrenCommand findAllLektionsgebuehrenCommand = new FindAllLektionsgebuehrenCommand();
        findAllLektionsgebuehrenCommand.setEntityManager(entityManager);
        findAllLektionsgebuehrenCommand.execute();
        Map<Integer, BigDecimal[]> lektionsgebuehrenMap = findAllLektionsgebuehrenCommand.getLektionsgebuehrenAllMap();

        // 4.a Jetziges Semesters: Berechnung des Wochenbetrags Nachrechnung und Update
        if (semesterrechnungCurrentSemester != null && semesterrechnungCurrentSemester.getRechnungsdatumNachrechnung() == null) {
            CalculateWochenbetragCommand calculateWochenbetragCommand = new CalculateWochenbetragCommand(semesterrechnungCurrentSemester, currentSemester, Rechnungstyp.NACHRECHNUNG, lektionsgebuehrenMap);
            calculateWochenbetragCommand.execute();
            if (calculateWochenbetragCommand.getResult() == CalculateWochenbetragCommand.Result.WOCHENBETRAG_ERFOLGREICH_BERECHNET) {
                semesterrechnungCurrentSemester.setWochenbetragNachrechnung(calculateWochenbetragCommand.getWochenbetrag());
            } else {  // sollte nie eintreten
                result = calculateWochenbetragCommand.getResult();
                semesterrechnungCurrentSemester.setWochenbetragNachrechnung(new BigDecimal("-99999.99"));
            }
            semesterrechnungDao.save(semesterrechnungCurrentSemester);
        }

        // 4.b Nachfolgendes Semesters: Berechnung des Wochenbetrags Vorrechnung und Update
        if (semesterrechnungNextSemester != null && semesterrechnungNextSemester.getRechnungsdatumVorrechnung() == null) {
            CalculateWochenbetragCommand calculateWochenbetragCommand = new CalculateWochenbetragCommand(semesterrechnungNextSemester, currentSemester, Rechnungstyp.VORRECHNUNG, lektionsgebuehrenMap);
            calculateWochenbetragCommand.execute();
            if (calculateWochenbetragCommand.getResult() == CalculateWochenbetragCommand.Result.WOCHENBETRAG_ERFOLGREICH_BERECHNET) {
                semesterrechnungNextSemester.setWochenbetragVorrechnung(calculateWochenbetragCommand.getWochenbetrag());
            } else {  // sollte nie eintreten
                result = calculateWochenbetragCommand.getResult();
                semesterrechnungNextSemester.setWochenbetragVorrechnung(new BigDecimal("-99999.99"));
            }
            semesterrechnungDao.save(semesterrechnungNextSemester);
        }
    }

    public CalculateWochenbetragCommand.Result getResult() {
        return result;
    }
}
