package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Martin Schraner.
 */
public class CheckGeschwisterSchuelerRechnungsempfaengerCommandTest {

    private final CommandInvoker commandInvoker = new CommandInvokerImpl();

    // 1.
    @Test
    public void testExecute() {

        Angehoeriger angehoeriger1 = new Angehoeriger(Anrede.FRAU, "Eva", "Juchli", null, null, null, true);
        Angehoeriger angehoeriger2 = new Angehoeriger(Anrede.HERR, "Kurt", "Juchli", null, null, null, true);
        Angehoeriger angehoeriger3 = new Angehoeriger(Anrede.FRAU, "Käthi", "Schraner", null, null, null, true);
        Angehoeriger angehoeriger4 = new Angehoeriger(Anrede.FRAU, "Regula", "Rösle", null, null, null, false);
        Angehoeriger angehoeriger5 = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null, false);

        Adresse adresse1 = new Adresse("Forchstrasse", "232", "8032", "Zürich");
        Adresse adresse2 = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");

        // Erstes Kind mit Grossmutter als Rechnungsempfängerin erfassen
        Schueler schueler1 = new Schueler("Lilly", "Juchli", new GregorianCalendar(2008, Calendar.JANUARY, 13), null, null, null, Geschlecht.W, null);
        schueler1.setAdresse(adresse1);
        schueler1.setMutter(angehoeriger1);
        schueler1.setVater(angehoeriger2);
        schueler1.setRechnungsempfaenger(angehoeriger3);
        schueler1.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
        CheckGeschwisterSchuelerRechnungempfaengerCommand checkGeschwisterSchuelerRechnungempfaengerCommand = new CheckGeschwisterSchuelerRechnungempfaengerCommand(schueler1, true);
        commandInvoker.executeCommand(checkGeschwisterSchuelerRechnungempfaengerCommand);
        List<Schueler> geschwisterList = checkGeschwisterSchuelerRechnungempfaengerCommand.getGeschwisterList();
        List<Schueler> angemeldeteGeschwisterList = checkGeschwisterSchuelerRechnungempfaengerCommand.getAngemeldeteGeschwisterList();
        List<Schueler> andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList = checkGeschwisterSchuelerRechnungempfaengerCommand.getAndereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList();
        assertTrue(geschwisterList.isEmpty());
        assertTrue(angemeldeteGeschwisterList.isEmpty());
        assertTrue(andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList.isEmpty());
        printGeschwisterUndSchuelerRechnungsempfaenger(schueler1, angemeldeteGeschwisterList, andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList);

        // Geschwister von Schüler 1 mit Vater als Rechnungsempfänger erfassen
        Schueler schueler2 = new Schueler("Anna", "Juchli", new GregorianCalendar(2010, Calendar.MARCH, 5), null, null, null, Geschlecht.W, null);
        schueler2.setAdresse(adresse1);
        schueler2.setMutter(angehoeriger1);
        schueler2.setVater(angehoeriger2);
        schueler2.setRechnungsempfaenger(angehoeriger2);
        schueler2.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
        checkGeschwisterSchuelerRechnungempfaengerCommand = new CheckGeschwisterSchuelerRechnungempfaengerCommand(schueler2);
        commandInvoker.executeCommand(checkGeschwisterSchuelerRechnungempfaengerCommand);
        geschwisterList = checkGeschwisterSchuelerRechnungempfaengerCommand.getGeschwisterList();
        angemeldeteGeschwisterList = checkGeschwisterSchuelerRechnungempfaengerCommand.getAngemeldeteGeschwisterList();
        andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList = checkGeschwisterSchuelerRechnungempfaengerCommand.getAndereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList();
        assertEquals(1, geschwisterList.size());
        assertEquals(schueler1, geschwisterList.get(0));
        assertEquals(1, angemeldeteGeschwisterList.size());
        assertEquals(schueler1, angemeldeteGeschwisterList.get(0));
        assertTrue(andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList.isEmpty());
        printGeschwisterUndSchuelerRechnungsempfaenger(schueler2, angemeldeteGeschwisterList, andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList);

        // Anderer Schueler mit gleicher Grossmutter wie bei Schüler 1 als Rechnungsempfängerin erfassen
        Schueler schueler3 = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), null, null, null, Geschlecht.W, null);
        schueler3.setAdresse(adresse2);
        schueler3.setMutter(angehoeriger4);
        schueler3.setVater(angehoeriger5);
        schueler3.setRechnungsempfaenger(angehoeriger3);
        schueler3.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
        checkGeschwisterSchuelerRechnungempfaengerCommand = new CheckGeschwisterSchuelerRechnungempfaengerCommand(schueler3, true);
        commandInvoker.executeCommand(checkGeschwisterSchuelerRechnungempfaengerCommand);
        geschwisterList = checkGeschwisterSchuelerRechnungempfaengerCommand.getGeschwisterList();
        angemeldeteGeschwisterList = checkGeschwisterSchuelerRechnungempfaengerCommand.getAngemeldeteGeschwisterList();
        andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList = checkGeschwisterSchuelerRechnungempfaengerCommand.getAndereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList();
        assertTrue(geschwisterList.isEmpty());
        assertTrue(angemeldeteGeschwisterList.isEmpty());
        assertEquals(1, andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList.size());
        assertEquals(schueler1, andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList.get(0));
        printGeschwisterUndSchuelerRechnungsempfaenger(schueler3, angemeldeteGeschwisterList, andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList);

        // Weiteres Geschwister von Schueler 3 mit Mutter als Rechnungsempfängerin erfassen
        Schueler schueler4 = new Schueler("Valentin Dan", "Rösle", new GregorianCalendar(2014, Calendar.SEPTEMBER, 15), null, null, null, Geschlecht.W, null);
        schueler4.setAdresse(adresse2);
        schueler4.setMutter(angehoeriger4);
        schueler4.setVater(angehoeriger5);
        schueler4.setRechnungsempfaenger(angehoeriger4);
        schueler4.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
        checkGeschwisterSchuelerRechnungempfaengerCommand = new CheckGeschwisterSchuelerRechnungempfaengerCommand(schueler4);
        commandInvoker.executeCommand(checkGeschwisterSchuelerRechnungempfaengerCommand);
        geschwisterList = checkGeschwisterSchuelerRechnungempfaengerCommand.getGeschwisterList();
        angemeldeteGeschwisterList = checkGeschwisterSchuelerRechnungempfaengerCommand.getAngemeldeteGeschwisterList();
        andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList = checkGeschwisterSchuelerRechnungempfaengerCommand.getAndereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList();
        assertEquals(1, geschwisterList.size());
        assertEquals(schueler3, geschwisterList.get(0));
        assertEquals(1, angemeldeteGeschwisterList.size());
        assertEquals(schueler3, angemeldeteGeschwisterList.get(0));
        assertTrue(andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList.isEmpty());
        printGeschwisterUndSchuelerRechnungsempfaenger(schueler4, angemeldeteGeschwisterList, andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList);

        // Weiteres Geschwister von Schueler 1 und 2 mit Mutter von Schüler 3 und 4 als Rechnungsempfänger erfassen
        Schueler schueler5 = new Schueler("Felicitas", "Juchli", new GregorianCalendar(2012, Calendar.MAY, 5), null, null, null, Geschlecht.W, null);
        schueler5.setAdresse(adresse1);
        schueler5.setMutter(angehoeriger1);
        schueler5.setVater(angehoeriger2);
        schueler5.setRechnungsempfaenger(angehoeriger4);
        schueler5.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
        checkGeschwisterSchuelerRechnungempfaengerCommand = new CheckGeschwisterSchuelerRechnungempfaengerCommand(schueler5, true);
        commandInvoker.executeCommand(checkGeschwisterSchuelerRechnungempfaengerCommand);
        geschwisterList = checkGeschwisterSchuelerRechnungempfaengerCommand.getGeschwisterList();
        angemeldeteGeschwisterList = checkGeschwisterSchuelerRechnungempfaengerCommand.getAngemeldeteGeschwisterList();
        andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList = checkGeschwisterSchuelerRechnungempfaengerCommand.getAndereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList();
        assertEquals(2, geschwisterList.size());
        assertTrue(geschwisterList.contains(schueler1));
        assertTrue(geschwisterList.contains(schueler2));
        assertEquals(2, angemeldeteGeschwisterList.size());
        assertTrue(angemeldeteGeschwisterList.contains(schueler1));
        assertTrue(angemeldeteGeschwisterList.contains(schueler2));
        assertEquals(1, andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList.size());
        assertEquals(schueler4, andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList.get(0));
        printGeschwisterUndSchuelerRechnungsempfaenger(schueler5, angemeldeteGeschwisterList, andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList);

        // Schüler 2 und Schüler 4 melden sich ab
        schueler2.getAnmeldungen().get(0).setAbmeldedatum(new GregorianCalendar(2015, Calendar.JUNE, 1));
        schueler4.getAnmeldungen().get(0).setAbmeldedatum(new GregorianCalendar(2015, Calendar.JUNE, 1));

        // Nochmals Abfrage für Schüler 5
        checkGeschwisterSchuelerRechnungempfaengerCommand = new CheckGeschwisterSchuelerRechnungempfaengerCommand(schueler5);
        commandInvoker.executeCommand(checkGeschwisterSchuelerRechnungempfaengerCommand);
        geschwisterList = checkGeschwisterSchuelerRechnungempfaengerCommand.getGeschwisterList();
        angemeldeteGeschwisterList = checkGeschwisterSchuelerRechnungempfaengerCommand.getAngemeldeteGeschwisterList();
        andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList = checkGeschwisterSchuelerRechnungempfaengerCommand.getAndereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList();
        assertEquals(2, geschwisterList.size());
        assertTrue(geschwisterList.contains(schueler1));
        assertTrue(geschwisterList.contains(schueler2));
        assertEquals(1, angemeldeteGeschwisterList.size());
        assertTrue(angemeldeteGeschwisterList.contains(schueler1));
        assertTrue(andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList.isEmpty());
        printGeschwisterUndSchuelerRechnungsempfaenger(schueler5, angemeldeteGeschwisterList, andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList);

    }

    private void printGeschwisterUndSchuelerRechnungsempfaenger(Schueler schueler, List<Schueler> geschwister, List<Schueler> andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaenger) {

        System.out.println("***");
        System.out.println("Neu angemeldeter Schüler:");
        System.out.println(schueler);
        System.out.println("Mutter: " + schueler.getMutter());
        System.out.println("Vater: " + schueler.getVater());
        System.out.println("Rechnungsempfänger: " + schueler.getRechnungsempfaenger());
        if (!geschwister.isEmpty()) {
            System.out.println("Angemeldete Geschwister von " + schueler.getVorname() + " " + schueler.getNachname() + ":");
            for (Schueler schueler1 : geschwister) {
                System.out.println(schueler1);
            }
        } else {
            System.out.println(schueler.getVorname() + " " + schueler.getNachname() + " hat keine angemeldeten Geschwister.");
        }

        if (!andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaenger.isEmpty()) {
            System.out.println("Andere Schüler, welche den Vater, die Mutter oder den Rechnungsempfänger von " + schueler.getVorname() + " " + schueler.getNachname() + " als Rechnungsempfänger haben:");
            for (Schueler schueler1 : andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaenger) {
                System.out.println(schueler1);
            }
        }

    }

}