package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Elternmithilfe;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.common.dataTypes.Gruppe;
import ch.metzenthin.svm.persistence.entities.*;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class CheckElternmithilfeBereitsBeiGeschwisterErfasstCommandTest {

    @Test
    public void testExecute() throws Exception {

        // Geschwister 1, Märchen, ElternmithilfeCode erzeugen
        Schueler geschwister1 = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
        Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
        geschwister1.setAdresse(adresse);
        geschwister1.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
        Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null, false);
        vater.setAdresse(adresse);
        geschwister1.setVater(vater);
        geschwister1.setRechnungsempfaenger(vater);
        Maerchen maerchen = new Maerchen("2011/2012", "Schneewittchen", 7);
        ElternmithilfeCode elternmithilfeCode = new ElternmithilfeCode("f", "Frisieren", true);
        Maercheneinteilung maercheneinteilung1 = new Maercheneinteilung(geschwister1, maerchen, Gruppe.A, "Komödiant 1", "1, 2", "Hase 2", "2, 3", "Frosch 3", "3, 4", Elternmithilfe.VATER,
                true, true, true, false, false, false, false, false, false, null, null);
        maercheneinteilung1.setElternmithilfeCode(elternmithilfeCode);
        maercheneinteilung1.getSchueler().getMaercheneinteilungen().add(maercheneinteilung1);
        CheckElternmithilfeBereitsBeiGeschwisterErfasstCommand checkElternmithilfeBereitsBeiGeschwisterErfasstCommand = new CheckElternmithilfeBereitsBeiGeschwisterErfasstCommand(geschwister1, maerchen);
        checkElternmithilfeBereitsBeiGeschwisterErfasstCommand.execute();
        List<Maercheneinteilung> maercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe
                = checkElternmithilfeBereitsBeiGeschwisterErfasstCommand
                .getMaercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe();
        assertTrue(maercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe.isEmpty());

        // 2. Geschwister
        Schueler geschwister2 = new Schueler("Valentin Dan", "Rösle", new GregorianCalendar(2014, Calendar.SEPTEMBER, 25), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
        geschwister2.setAdresse(adresse);
        geschwister2.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
        geschwister2.setVater(vater);
        geschwister2.setRechnungsempfaenger(vater);
        checkElternmithilfeBereitsBeiGeschwisterErfasstCommand = new CheckElternmithilfeBereitsBeiGeschwisterErfasstCommand(geschwister2, maerchen);
        checkElternmithilfeBereitsBeiGeschwisterErfasstCommand.execute();
        maercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe
                = checkElternmithilfeBereitsBeiGeschwisterErfasstCommand
                .getMaercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe();
        assertEquals(1, maercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe.size());
        assertEquals("Jana",
                maercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe.get(0).getSchueler().getVorname());
    }
}