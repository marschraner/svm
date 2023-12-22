package ch.metzenthin.svm.persistence.entities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class MaercheneinteilungTest {

    @Test
    public void getRolle1WithoutSorterCharacters() {
        Maercheneinteilung maercheneinteilung = new Maercheneinteilung();

        maercheneinteilung.setRolle1("Hund 2");
        assertEquals("Hund 2", maercheneinteilung.getRolle1WithoutSorterCharacters());

        maercheneinteilung.setRolle1("y Hund 2");
        assertEquals("Hund 2", maercheneinteilung.getRolle1WithoutSorterCharacters());

        maercheneinteilung.setRolle1("Y Hund 2");
        assertEquals("Hund 2", maercheneinteilung.getRolle1WithoutSorterCharacters());

        maercheneinteilung.setRolle1("y 1 Hund 2");
        assertEquals("Hund 2", maercheneinteilung.getRolle1WithoutSorterCharacters());

        maercheneinteilung.setRolle1("Y 1 Hund 2");
        assertEquals("Hund 2", maercheneinteilung.getRolle1WithoutSorterCharacters());

        maercheneinteilung.setRolle1("y 11 Hund 2");
        assertEquals("Hund 2", maercheneinteilung.getRolle1WithoutSorterCharacters());

        maercheneinteilung.setRolle1("Y 11 Hund 2");
        assertEquals("Hund 2", maercheneinteilung.getRolle1WithoutSorterCharacters());

        maercheneinteilung.setRolle1("yy Hund 2");
        assertEquals("Hund 2", maercheneinteilung.getRolle1WithoutSorterCharacters());

        maercheneinteilung.setRolle1("YY Hund 2");
        assertEquals("Hund 2", maercheneinteilung.getRolle1WithoutSorterCharacters());

        maercheneinteilung.setRolle1("yy 11 Hund 2");
        assertEquals("Hund 2", maercheneinteilung.getRolle1WithoutSorterCharacters());

        maercheneinteilung.setRolle1("YY 11 Hund 2");
        assertEquals("Hund 2", maercheneinteilung.getRolle1WithoutSorterCharacters());

        maercheneinteilung.setRolle1("yyy 11 Hund 2");
        assertEquals("yyy 11 Hund 2", maercheneinteilung.getRolle1WithoutSorterCharacters());

        maercheneinteilung.setRolle1("YYY 11 Hund 2");
        assertEquals("YYY 11 Hund 2", maercheneinteilung.getRolle1WithoutSorterCharacters());

        // Gross- und Kleinschreibung gemischt als Sorter nicht ersetzen
        maercheneinteilung.setRolle1("Yy Hund 2");
        assertEquals("Yy Hund 2", maercheneinteilung.getRolle1WithoutSorterCharacters());

        maercheneinteilung.setRolle1("Yy 11 Hund 2");
        assertEquals("Yy 11 Hund 2", maercheneinteilung.getRolle1WithoutSorterCharacters());

        maercheneinteilung.setRolle1("yY Hund 2");
        assertEquals("yY Hund 2", maercheneinteilung.getRolle1WithoutSorterCharacters());

        maercheneinteilung.setRolle1("yY 11 Hund 2");
        assertEquals("yY 11 Hund 2", maercheneinteilung.getRolle1WithoutSorterCharacters());
    }

}