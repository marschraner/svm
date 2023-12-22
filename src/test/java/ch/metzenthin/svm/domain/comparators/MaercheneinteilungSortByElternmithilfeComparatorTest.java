package ch.metzenthin.svm.domain.comparators;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Elternmithilfe;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.common.dataTypes.Gruppe;
import ch.metzenthin.svm.persistence.entities.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class MaercheneinteilungSortByElternmithilfeComparatorTest {

    @Test
    public void testCompare() {
        Schueler schueler1 = new Schueler("Jana", "Huber", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
        Adresse adresse1 = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
        schueler1.setAdresse(adresse1);
        schueler1.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
        Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Moser", "044 491 69 33", null, null, false);
        vater.setAdresse(adresse1);
        schueler1.setVater(vater);
        schueler1.setRechnungsempfaenger(vater);

        Schueler schueler2 = new Schueler("Hanna", "Zanetti", new GregorianCalendar(2010, Calendar.JULY, 24), "044 422 69 33", null, null, Geschlecht.W, null);
        Adresse adresse2 = new Adresse("Hohenklingenstrasse", "22", "8049", "Zürich");
        schueler2.setAdresse(adresse2);
        schueler2.addAnmeldung(new Anmeldung(new GregorianCalendar(2013, Calendar.JANUARY, 1), null));
        Angehoeriger mutter2 = new Angehoeriger(Anrede.FRAU, "Adriana", "Meier", "044 422 69 33", null, null, true);
        mutter2.setAdresse(adresse2);
        schueler2.setMutter(mutter2);
        schueler2.setRechnungsempfaenger(mutter2);

        Schueler schueler3 = new Schueler("Alma", "Zanetti", new GregorianCalendar(2010, Calendar.JULY, 24), "044 422 69 33", null, null, Geschlecht.W, null);
        schueler3.setAdresse(adresse2);
        schueler3.addAnmeldung(new Anmeldung(new GregorianCalendar(2013, Calendar.JANUARY, 1), null));
        schueler3.setMutter(mutter2);
        schueler3.setRechnungsempfaenger(mutter2);

        Maerchen maerchen = new Maerchen("1911/1912", "Scheewittchen", 7);
        Maercheneinteilung maercheneinteilung1 = new Maercheneinteilung(schueler1, maerchen, Gruppe.A, "Komödiant 1", "1, 2", "Hase 1", "2, 3", "Frosch 1", "3, 4", Elternmithilfe.VATER,
                true, true, true, false, false, false, false, false, false, null, null);
        Maercheneinteilung maercheneinteilung2 = new Maercheneinteilung(schueler2, maerchen, Gruppe.A, "Komödiant 2", "1, 3", "Hase 2", "2, 3", "Frosch 2", "3, 4", Elternmithilfe.MUTTER,
                true, true, true, false, false, false, false, false, false, null, null);
        Maercheneinteilung maercheneinteilung3 = new Maercheneinteilung(schueler3, maerchen, Gruppe.B, "Komödiant 3", "1, 3", "Hase 2", "2, 3", "Frosch 2", "3, 4", Elternmithilfe.MUTTER,
                true, true, true, false, false, false, false, false, false, null, null);

        List<Maercheneinteilung> maercheneinteilungList = new ArrayList<>();
        maercheneinteilungList.add(maercheneinteilung1);
        maercheneinteilungList.add(maercheneinteilung2);
        maercheneinteilungList.add(maercheneinteilung3);

        maercheneinteilungList.sort(new MaercheneinteilungSortByElternmithilfeComparator());

        assertEquals(maercheneinteilung2, maercheneinteilungList.get(0));
        assertEquals(maercheneinteilung3, maercheneinteilungList.get(1));
        assertEquals(maercheneinteilung1, maercheneinteilungList.get(2));
    }
}