package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.datatypes.Rechnungstyp;
import ch.metzenthin.svm.persistence.daos.SemesterDao;
import ch.metzenthin.svm.persistence.daos.SemesterrechnungDao;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Schraner
 */
public class CreateSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand implements Command {

    private final SemesterDao semesterDao = new SemesterDao();
    private final SemesterrechnungDao semesterrechnungDao = new SemesterrechnungDao();

    // input
    private final List<Angehoeriger> rechnungsempfaengers;
    private final Semester currentSemester;

    public CreateSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand(List<Angehoeriger> rechnungsempfaengers, Semester currentSemester) {
        this.rechnungsempfaengers = rechnungsempfaengers;
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

        // 2. Lektionsgebühren
        FindAllLektionsgebuehrenCommand findAllLektionsgebuehrenCommand = new FindAllLektionsgebuehrenCommand();
        findAllLektionsgebuehrenCommand.execute();
        Map<Integer, BigDecimal[]> lektionsgebuehrenMap = findAllLektionsgebuehrenCommand.getLektionsgebuehrenAllMap();

        // Reload zur Verhinderung von Lazy Loading-Problem
        Semester currentSemesterReloaded = semesterDao.findById(currentSemester.getSemesterId());

        for (Angehoeriger rechnungsempfaenger : rechnungsempfaengers) {

            // 3. Neue Semesterrechnung erzeugen
            Semesterrechnung semesterrechnung = new Semesterrechnung();
            semesterrechnung.setSemester(currentSemesterReloaded);
            semesterrechnung.setRechnungsempfaenger(rechnungsempfaenger);
            semesterrechnung.setGratiskinder(false);
            semesterrechnung.setDeleted(false);

            // 4. SemesterrechnungCode, Stipendium und Gratiskind von früher
            if (!rechnungsempfaenger.getSemesterrechnungen().isEmpty()) {
                Semesterrechnung previousSemesterrechnung
                        = rechnungsempfaenger.getSortedSemesterrechnungen().get(0);
                semesterrechnung.setStipendium(previousSemesterrechnung.getStipendium());
                semesterrechnung.setGratiskinder(previousSemesterrechnung.getGratiskinder());
                semesterrechnung.setSemesterrechnungCode(previousSemesterrechnung.getSemesterrechnungCode());
            }

            // 5. Berechnung der Anzahl Wochen
            CalculateAnzWochenCommand calculateAnzWochenCommand = new CalculateAnzWochenCommand(rechnungsempfaenger.getSchuelerRechnungsempfaenger(), currentSemester);
            calculateAnzWochenCommand.execute();

            // 6.a Berechnung des Wochenbetrags Vorrechnung
            CalculateWochenbetragCommand calculateWochenbetragCommand;
            semesterrechnung.setAnzahlWochenVorrechnung(calculateAnzWochenCommand.getAnzahlWochen());
            if (previousSemester != null) {
                calculateWochenbetragCommand = new CalculateWochenbetragCommand(semesterrechnung, previousSemester, Rechnungstyp.VORRECHNUNG, lektionsgebuehrenMap);
                calculateWochenbetragCommand.execute();
                if (calculateWochenbetragCommand.getResult() == CalculateWochenbetragCommand.Result.WOCHENBETRAG_ERFOLGREICH_BERECHNET) {
                    semesterrechnung.setWochenbetragVorrechnung(calculateWochenbetragCommand.getWochenbetrag());
                } else {   // sollte nie eintreten
                    semesterrechnung.setWochenbetragVorrechnung(new BigDecimal("-99999.99"));
                }
            } else {
                semesterrechnung.setWochenbetragVorrechnung(BigDecimal.ZERO);
            }

            // 6.b Berechnung des Wochenbetrags Nachrechnung
            semesterrechnung.setAnzahlWochenNachrechnung(calculateAnzWochenCommand.getAnzahlWochen());
            calculateWochenbetragCommand = new CalculateWochenbetragCommand(semesterrechnung, currentSemester, Rechnungstyp.NACHRECHNUNG, lektionsgebuehrenMap);
            calculateWochenbetragCommand.execute();
            if (calculateWochenbetragCommand.getResult() == CalculateWochenbetragCommand.Result.WOCHENBETRAG_ERFOLGREICH_BERECHNET) {
                semesterrechnung.setWochenbetragNachrechnung(calculateWochenbetragCommand.getWochenbetrag());
            } else {   // sollte nie eintreten
                semesterrechnung.setWochenbetragVorrechnung(new BigDecimal("-99999.99"));
            }

            // 5. Ermässigung und Zuschlag
            semesterrechnung.setErmaessigungVorrechnung(BigDecimal.ZERO);
            semesterrechnung.setZuschlagVorrechnung(BigDecimal.ZERO);
            semesterrechnung.setErmaessigungNachrechnung(BigDecimal.ZERO);
            semesterrechnung.setZuschlagNachrechnung(BigDecimal.ZERO);

            // 6. Speichern
            semesterrechnungDao.save(semesterrechnung);
        }
    }

}
