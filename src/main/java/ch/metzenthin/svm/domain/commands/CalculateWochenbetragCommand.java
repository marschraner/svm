package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.persistence.entities.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Schraner
 */
public class CalculateWochenbetragCommand implements Command {

    public enum Result {
        WOCHENBETRAG_ERFOLGREICH_BERECHNET,
        LEKTIONSGEBUEHREN_NICHT_FUER_ALLE_KURSLAENGEN_ERFASST
    }

    // input
    private Semesterrechnung semesterrechnung;
    private Semester relevantesSemester;
    private Rechnungstyp rechnungstyp;
    private Map<Integer, BigDecimal[]> lektionsgebuehrenMap;

    // output
    private Result result;
    private BigDecimal wochenbetrag;

    public CalculateWochenbetragCommand(Semesterrechnung semesterrechnung, Semester relevantesSemester, Rechnungstyp rechnungstyp, Map<Integer, BigDecimal[]> lektionsgebuehrenMap) {
        this.semesterrechnung = semesterrechnung;
        this.relevantesSemester = relevantesSemester;
        this.rechnungstyp = rechnungstyp;
        this.lektionsgebuehrenMap = lektionsgebuehrenMap;
    }

    @Override
    public void execute() {

        // 1. Anzahl Kurse Rechnungsempfänger
        int anzahlKurseRechnungsempfaenger = 0;
        for (Schueler schueler : semesterrechnung.getRechnungsempfaenger().getSchuelerRechnungsempfaenger()) {

            // abgemeldete Schüler nicht berücksichtigen
            Anmeldung anmeldung = schueler.getAnmeldungen().get(0);
            if (anmeldung.getAbmeldedatum() != null && !anmeldung.getAbmeldedatum().after(semesterrechnung.getSemester().getSemesterbeginn())) {
                continue;
            }

            for (Kursanmeldung kursanmeldung : schueler.getKursanmeldungen()) {
                if (kursanmeldung.getKurs().getSemester().getSemesterId().equals(relevantesSemester.getSemesterId()) && (rechnungstyp == Rechnungstyp.NACHRECHNUNG || !kursanmeldung.getAbmeldungPerEndeSemester())) {
                    anzahlKurseRechnungsempfaenger++;
                }
            }
        }
        // Nur Rabatte bis Lektionsgebuehren.MAX_KINDER
        if (anzahlKurseRechnungsempfaenger > Lektionsgebuehren.MAX_KINDER) {
            anzahlKurseRechnungsempfaenger = Lektionsgebuehren.MAX_KINDER;
        }

        // 2. Wochenbetrag berechnen
        wochenbetrag = BigDecimal.ZERO;
        for (Schueler schueler : semesterrechnung.getRechnungsempfaenger().getSchuelerRechnungsempfaenger()) {

            // 2.a abgemeldete Schüler nicht berücksichtigen
            Anmeldung anmeldung = schueler.getAnmeldungen().get(0);
            if (anmeldung.getAbmeldedatum() != null && !anmeldung.getAbmeldedatum().after(semesterrechnung.getSemester().getSemesterbeginn())) {
                continue;
            }

            // 2.b relevante Kurse für einen Schüler
            List<Kurs> relevanteKurseSchueler = new ArrayList<>();
            for (Kursanmeldung kursanmeldung : schueler.getKursanmeldungen()) {
                if (kursanmeldung.getKurs().getSemester().getSemesterId().equals(relevantesSemester.getSemesterId()) && (rechnungstyp == Rechnungstyp.NACHRECHNUNG || !kursanmeldung.getAbmeldungPerEndeSemester())) {
                    relevanteKurseSchueler.add(kursanmeldung.getKurs());
                }
            }

            // 2.c Wochenbetrag Kurse Schüler ohne 6-Jahres-Rabatt
            for (Kurs kurs : relevanteKurseSchueler) {
                int kurslaenge = kurs.getKurslaenge();
                BigDecimal[] lektionsgebuehrenKurs = lektionsgebuehrenMap.get(kurslaenge);
                if (lektionsgebuehrenKurs == null) {
                    result = Result.LEKTIONSGEBUEHREN_NICHT_FUER_ALLE_KURSLAENGEN_ERFASST;
                    return;
                }
                BigDecimal betragKurs = lektionsgebuehrenKurs[anzahlKurseRechnungsempfaenger - 1];
                wochenbetrag = wochenbetrag.add(betragKurs);
            }

            // 2.d Schüler 6-Jahres-Rabatt-berechtigt?
            boolean schuelerHatSechsJahresRabatt = false;
            if (relevanteKurseSchueler.size() >= 2) {
                CheckIfAnmeldungsdauerErlaubtSechsJahresRabattCommand checkIfAnmeldungsdauerErlaubtSechsJahresRabattCommand = new CheckIfAnmeldungsdauerErlaubtSechsJahresRabattCommand(schueler, relevantesSemester.getSemesterbeginn());
                checkIfAnmeldungsdauerErlaubtSechsJahresRabattCommand.execute();
                if (checkIfAnmeldungsdauerErlaubtSechsJahresRabattCommand.isAnmeldungsdauerErlaubtSechsJahresRabatt()) {
                    schuelerHatSechsJahresRabatt = true;
                }
            }

            // 2.e Reduktion 6-Jahres-Rabatt
            if (schuelerHatSechsJahresRabatt) {
                // Kurs mit kürzester Lektionsdauer suchen (= günstigster Kurs)
                int minimaleKurslaenge = Integer.MAX_VALUE;
                for (Kurs kurs : relevanteKurseSchueler) {
                    int kurslaenge = kurs.getKurslaenge();
                    if (kurslaenge < minimaleKurslaenge) {
                        minimaleKurslaenge = kurslaenge;
                    }
                }
                // Bereits erfasster (reduzierter) Preis des günstigsten Kurses wieder subtrahieren
                wochenbetrag = wochenbetrag.subtract(lektionsgebuehrenMap.get(minimaleKurslaenge)[anzahlKurseRechnungsempfaenger - 1]);
                // 0.5*voller Preis des Kurses addieren
                BigDecimal sechsJahresRabatt = lektionsgebuehrenMap.get(minimaleKurslaenge)[0];
                sechsJahresRabatt = sechsJahresRabatt.multiply(new BigDecimal("0.5"));
                wochenbetrag = wochenbetrag.add(sechsJahresRabatt);
            }
        }

        // 3. Runden auf 2 Nachkommastellen
        wochenbetrag = wochenbetrag.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        result = Result.WOCHENBETRAG_ERFOLGREICH_BERECHNET;
    }

    public BigDecimal getWochenbetrag() {
        return wochenbetrag;
    }

    public Result getResult() {
        return result;
    }
}
