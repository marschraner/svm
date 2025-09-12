package ch.metzenthin.svm.common.utils;

import static org.junit.Assert.assertEquals;

import ch.metzenthin.svm.common.datatypes.Geschlecht;
import ch.metzenthin.svm.common.datatypes.Gruppe;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import java.util.*;
import org.junit.Test;

/**
 * @author Martin Schraner
 */
public class MaercheneinteilungenSorterTest {

  private final MaercheneinteilungenSorter maercheneinteilungenSorter =
      new MaercheneinteilungenSorter();

  @Test
  public void testSortMaercheneinteilungenByGruppeAndRolle() {

    String vorname1 = "Jana";
    String vorname2 = "Valentin";
    String vorname3 = "Leandra";
    String vorname4 = "Hugo";
    Schueler schueler1 =
        new Schueler(
            vorname1,
            "Rösle",
            new GregorianCalendar(2012, Calendar.JULY, 24),
            null,
            null,
            null,
            Geschlecht.W,
            "Schwester von Valentin");
    Schueler schueler2 =
        new Schueler(
            vorname2,
            "Rösle",
            new GregorianCalendar(2014, Calendar.SEPTEMBER, 10),
            null,
            null,
            null,
            Geschlecht.M,
            null);
    Schueler schueler3 =
        new Schueler(
            vorname3,
            "Moser",
            new GregorianCalendar(2011, Calendar.JULY, 24),
            null,
            null,
            null,
            Geschlecht.W,
            null);
    Schueler schueler4 =
        new Schueler(
            vorname4,
            "Müller",
            new GregorianCalendar(2010, Calendar.JULY, 24),
            null,
            null,
            null,
            Geschlecht.M,
            null);

    Map<Schueler, Maercheneinteilung> schuelerMaercheneinteilungMap = new HashMap<>();

    Maercheneinteilung maercheneinteilung1 = new Maercheneinteilung();
    maercheneinteilung1.setSchueler(schueler1);
    maercheneinteilung1.setRolle1("Perle 2");
    maercheneinteilung1.setGruppe(Gruppe.A);
    String bemerkungen1 = "Bemerkungen 1";
    maercheneinteilung1.setBemerkungen(bemerkungen1);
    schuelerMaercheneinteilungMap.put(schueler1, maercheneinteilung1);

    Maercheneinteilung maercheneinteilung2 = new Maercheneinteilung();
    maercheneinteilung2.setSchueler(schueler2);
    maercheneinteilung2.setRolle1("Perle 2");
    maercheneinteilung2.setGruppe(Gruppe.B);
    String bemerkungen2 = "Bemerkungen 2";
    maercheneinteilung2.setBemerkungen(bemerkungen2);
    schuelerMaercheneinteilungMap.put(schueler2, maercheneinteilung2);

    Maercheneinteilung maercheneinteilung3 = new Maercheneinteilung();
    maercheneinteilung3.setSchueler(schueler3);
    maercheneinteilung3.setRolle1("Perle 1");
    maercheneinteilung3.setGruppe(Gruppe.B);
    String bemerkungen3 = "Bemerkungen 3";
    maercheneinteilung3.setBemerkungen(bemerkungen3);
    schuelerMaercheneinteilungMap.put(schueler3, maercheneinteilung3);

    Maercheneinteilung maercheneinteilung4 = new Maercheneinteilung();
    maercheneinteilung4.setSchueler(schueler4);
    maercheneinteilung4.setRolle1("Perle 1");
    maercheneinteilung4.setGruppe(Gruppe.A);
    String bemerkungen4 = "Bemerkungen 4";
    maercheneinteilung4.setBemerkungen(bemerkungen4);
    schuelerMaercheneinteilungMap.put(schueler4, maercheneinteilung4);

    Map<Schueler, Maercheneinteilung> sortedSchuelerMaercheneinteilungMap =
        maercheneinteilungenSorter.sortMaercheneinteilungenByGruppeAndRolle(
            schuelerMaercheneinteilungMap);

    assertEquals(4, sortedSchuelerMaercheneinteilungMap.size());

    Set<Schueler> schuelers = sortedSchuelerMaercheneinteilungMap.keySet();
    List<Maercheneinteilung> sortedMaercheneinteilungen = new ArrayList<>();
    for (Schueler schueler : schuelers) {
      sortedMaercheneinteilungen.add(sortedSchuelerMaercheneinteilungMap.get(schueler));
    }

    assertEquals(Gruppe.A, sortedMaercheneinteilungen.get(0).getGruppe());
    assertEquals("Perle 1", sortedMaercheneinteilungen.get(0).getRolle1());
    assertEquals(vorname4, sortedMaercheneinteilungen.get(0).getSchueler().getVorname());
    assertEquals(bemerkungen4, sortedMaercheneinteilungen.get(0).getBemerkungen());

    assertEquals(Gruppe.A, sortedMaercheneinteilungen.get(1).getGruppe());
    assertEquals("Perle 2", sortedMaercheneinteilungen.get(1).getRolle1());
    assertEquals(vorname1, sortedMaercheneinteilungen.get(1).getSchueler().getVorname());
    assertEquals(bemerkungen1, sortedMaercheneinteilungen.get(1).getBemerkungen());

    assertEquals(Gruppe.B, sortedMaercheneinteilungen.get(2).getGruppe());
    assertEquals("Perle 1", sortedMaercheneinteilungen.get(2).getRolle1());
    assertEquals(vorname3, sortedMaercheneinteilungen.get(2).getSchueler().getVorname());
    assertEquals(bemerkungen3, sortedMaercheneinteilungen.get(2).getBemerkungen());

    assertEquals(Gruppe.B, sortedMaercheneinteilungen.get(3).getGruppe());
    assertEquals("Perle 2", sortedMaercheneinteilungen.get(3).getRolle1());
    assertEquals(vorname2, sortedMaercheneinteilungen.get(3).getSchueler().getVorname());
    assertEquals(bemerkungen2, sortedMaercheneinteilungen.get(3).getBemerkungen());
  }
}
