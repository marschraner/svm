package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.persistence.entities.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Schraner
 */
public class ImportSemesterrechnungenFromPreviousSemesterCommand extends GenericDaoCommand {

    // input
    private List<Semesterrechnung> semesterrechnungenCurrentSemester;
    private Semester currentSemester;
    private boolean bisherigeUeberschreiben;
    private boolean importRestbetraege;

    public ImportSemesterrechnungenFromPreviousSemesterCommand(List<Semesterrechnung> semesterrechnungenCurrentSemester, Semester currentSemester, boolean bisherigeUeberschreiben, boolean importRestbetraege) {
        this.semesterrechnungenCurrentSemester = semesterrechnungenCurrentSemester;
        this.currentSemester = currentSemester;
        this.bisherigeUeberschreiben = bisherigeUeberschreiben;
        this.importRestbetraege = importRestbetraege;
    }

    @Override
    public void execute() {

        // 1. Vorhergehendes Semester
        FindPreviousSemesterCommand findPreviousSemesterCommand = new FindPreviousSemesterCommand(currentSemester);
        findPreviousSemesterCommand.setEntityManager(entityManager);
        findPreviousSemesterCommand.execute();
        Semester previousSemester = findPreviousSemesterCommand.getPreviousSemester();
        if (previousSemester == null) {
            return;
        }

        // 2. Semesterrechnungen des vorherigen Semesters
        FindSemesterrechnungenSemesterCommand findSemesterrechnungenSemesterCommand = new FindSemesterrechnungenSemesterCommand(previousSemester);
        findSemesterrechnungenSemesterCommand.setEntityManager(entityManager);
        findSemesterrechnungenSemesterCommand.execute();
        List<Semesterrechnung> semesterrechnungenPreviousSemester = findSemesterrechnungenSemesterCommand.getSemesterrechnungenFound();

        // 3. Lektionsgebühren
        FindAllLektionsgebuehrenCommand findAllLektionsgebuehrenCommand = new FindAllLektionsgebuehrenCommand();
        findAllLektionsgebuehrenCommand.setEntityManager(entityManager);
        findAllLektionsgebuehrenCommand.execute();
        Map<Integer, BigDecimal[]> lektionsgebuehrenMap = findAllLektionsgebuehrenCommand.getLektionsgebuehrenAllMap();

        Semesterrechnungen:
        for (Semesterrechnung previousSemesterrechnung : semesterrechnungenPreviousSemester) {

            // 4. Semesterrechnung bereits erfasst?
            Semesterrechnung bereitsErfassteSemesterrechnung = null;
            for (Semesterrechnung semesterrechnungRechnungsempfaenger : previousSemesterrechnung.getRechnungsempfaenger().getSemesterrechnungen()) {
                if (semesterrechnungRechnungsempfaenger.getSemester().getSemesterId().equals(currentSemester.getSemesterId())) {
                    bereitsErfassteSemesterrechnung = semesterrechnungRechnungsempfaenger;
                    // Bereits vorhandene Semesterrechnungen mit Rechnungsdatum sollen nicht ein zweites Mal importiert werden
                    if (!bisherigeUeberschreiben || bereitsErfassteSemesterrechnung.getRechnungsdatumVorrechnung() != null || bereitsErfassteSemesterrechnung.getRechnungsdatumNachrechnung() != null) {
                        continue Semesterrechnungen;
                    }
                }
            }

            // 5. Gibt es offene Rechnung?
            boolean offeneRechnung = false;
            if (importRestbetraege && previousSemesterrechnung.getRestbetrag() != null && previousSemesterrechnung.getRestbetrag().compareTo(BigDecimal.ZERO) != 0) {
                offeneRechnung = true;
            }

            // 6. Gibt es für die vorherige Semesterrechnung Schüler und Kurse ohne Abmeldungen?
            if (!importRestbetraege || !offeneRechnung) {
                boolean kurseOhneAbmeldungFound = false;

                Schueler:
                for (Schueler schueler : previousSemesterrechnung.getRechnungsempfaenger().getSchuelerRechnungsempfaenger()) {
                    // 4.a Nur Schüler ohne Abmeldungen
                    Anmeldung anmeldung = schueler.getAnmeldungen().get(0);
                    if (anmeldung.getAbmeldedatum() != null && !anmeldung.getAbmeldedatum().after(currentSemester.getSemesterbeginn())) {
                        continue;
                    }
                    // 4.b Nur Kurse ohne Abmeldungen
                    for (Kursanmeldung kursanmeldung : schueler.getKursanmeldungen()) {
                        if (kursanmeldung.getKurs().getSemester().getSemesterId().equals(previousSemester.getSemesterId()) && kursanmeldung.getAbmeldedatum() != null) {
                            kurseOhneAbmeldungFound = true;
                            break Schueler;
                        }
                    }
                }
                if (!kurseOhneAbmeldungFound) {
                    continue;
                }
            }

            // 7. Neue Semesterrechnung erzeugen
            Semesterrechnung semesterrechnung = new Semesterrechnung();
            semesterrechnung.setSemester(currentSemester);
            semesterrechnung.setRechnungsempfaenger(previousSemesterrechnung.getRechnungsempfaenger());

            // 7.a Settings von früherer Rechnung übernehmen
            SemesterrechnungCode semesterrechnungCode;
            if (bereitsErfassteSemesterrechnung != null) {
                semesterrechnung.copyAttributesFrom(bereitsErfassteSemesterrechnung);
                semesterrechnungCode = bereitsErfassteSemesterrechnung.getSemesterrechnungCode();
            } else {
                semesterrechnung.setRechnungsempfaenger(previousSemesterrechnung.getRechnungsempfaenger());
                semesterrechnung.setStipendium(previousSemesterrechnung.getStipendium());
                semesterrechnung.setGratiskinder(previousSemesterrechnung.getGratiskinder());
                semesterrechnungCode = previousSemesterrechnung.getSemesterrechnungCode();
            }

            // 7.b Zuschlag / Ermässigung neu setzen
            semesterrechnung.setErmaessigungVorrechnung(BigDecimal.ZERO);
            semesterrechnung.setErmaessigungsgrundVorrechnung(null);
            semesterrechnung.setErmaessigungNachrechnung(BigDecimal.ZERO);
            semesterrechnung.setErmaessigungsgrundNachrechnung(null);
            semesterrechnung.setZuschlagVorrechnung(BigDecimal.ZERO);
            semesterrechnung.setZuschlagsgrundVorrechnung(null);
            semesterrechnung.setZuschlagNachrechnung(BigDecimal.ZERO);
            semesterrechnung.setZuschlagsgrundNachrechnung(null);
            BigDecimal restbetragPreviousSemester = previousSemesterrechnung.getRestbetrag();
            if (importRestbetraege && restbetragPreviousSemester != null) {
                String grund = "Übertrag Restbetrag vorheriges Semester";
                if (restbetragPreviousSemester.compareTo(BigDecimal.ZERO) == -1) {
                    semesterrechnung.setErmaessigungVorrechnung(restbetragPreviousSemester);
                    semesterrechnung.setErmaessigungsgrundVorrechnung(grund);
                    semesterrechnung.setErmaessigungNachrechnung(restbetragPreviousSemester);
                    semesterrechnung.setErmaessigungsgrundNachrechnung(grund);
                } else if (restbetragPreviousSemester.compareTo(BigDecimal.ZERO) == 1) {
                    semesterrechnung.setZuschlagVorrechnung(restbetragPreviousSemester);
                    semesterrechnung.setZuschlagsgrundVorrechnung(grund);
                    semesterrechnung.setZuschlagNachrechnung(restbetragPreviousSemester);
                    semesterrechnung.setZuschlagsgrundNachrechnung(grund);
                }
            }

            // 8.a Berechnung des Wochenbetrags Vorrechnung
            semesterrechnung.setAnzahlWochenVorrechnung(currentSemester.getAnzahlSchulwochen());
            CalculateWochenbetragCommand calculateWochenbetragCommand = new CalculateWochenbetragCommand(semesterrechnung, previousSemester, Rechnungstyp.VORRECHNUNG, lektionsgebuehrenMap);
            calculateWochenbetragCommand.execute();
            if (calculateWochenbetragCommand.getResult() == CalculateWochenbetragCommand.Result.WOCHENBETRAG_ERFOLGREICH_BERECHNET) {
                semesterrechnung.setWochenbetragVorrechnung(calculateWochenbetragCommand.getWochenbetrag());
            }

            // 8.b Berechnung des Wochenbetrags Nachrechnung
            if (bereitsErfassteSemesterrechnung == null) {
                semesterrechnung.setAnzahlWochenNachrechnung(currentSemester.getAnzahlSchulwochen());
                calculateWochenbetragCommand = new CalculateWochenbetragCommand(semesterrechnung, currentSemester, Rechnungstyp.NACHRECHNUNG, lektionsgebuehrenMap);
                calculateWochenbetragCommand.execute();
                if (calculateWochenbetragCommand.getResult() == CalculateWochenbetragCommand.Result.WOCHENBETRAG_ERFOLGREICH_BERECHNET) {
                    semesterrechnung.setWochenbetragNachrechnung(calculateWochenbetragCommand.getWochenbetrag());
                }
            }

            // 7.d Neue Semesterrechnung speichern bzw. alte updaten
            SaveOrUpdateSemesterrechnungCommand saveOrUpdateSemesterrechnungCommand = new SaveOrUpdateSemesterrechnungCommand(semesterrechnung, semesterrechnungCode, bereitsErfassteSemesterrechnung, semesterrechnungenCurrentSemester);
            saveOrUpdateSemesterrechnungCommand.setEntityManager(entityManager);
            saveOrUpdateSemesterrechnungCommand.execute();
        }
    }

}
