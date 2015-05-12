package ch.metzenthin.svm.domain;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Elternrolle;
import ch.metzenthin.svm.model.entities.Adresse;
import ch.metzenthin.svm.model.entities.Angehoeriger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * @author Martin Schraner
 */
public class SchuelerErfassenTest {

    private final String strasse = "Im Isisbüel";
    private final int hausnummer = 20;
    private final int plz = 8720;
    private final String ort = "Räterschen";
    private final String festnetz = "052 712 20 22";

    private EntityManagerFactory entityManagerFactory;
    private SchuelerErfassen schuelerErfassen;

    public SchuelerErfassenTest() {
    }

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
        schuelerErfassen = new SchuelerErfassen(entityManagerFactory);
    }

    @After
    public void tearDown() throws Exception {
        entityManagerFactory.close();
    }

    @Test
    public void  testSaveAdresse() {

        Adresse adresse = new Adresse(strasse, hausnummer, plz, ort, festnetz);

        schuelerErfassen.saveAdresse(adresse);
    }

    @Test
    public void testSaveAngehoeriger() {

        Anrede anrede = Anrede.HERR;
        String vorname = "Urs";
        String nachname = "Müller";
        Calendar geburstdatum = new GregorianCalendar(1960, Calendar.APRIL, 2);
        String email = "urs.mueller@bluewin.ch";
        Elternrolle elternrolle = Elternrolle.KEINE;
        String beruf = "Gärtner";

        Angehoeriger angehoeriger = new Angehoeriger(anrede, vorname, nachname, geburstdatum, null, email, elternrolle, true, beruf);

        Adresse adresse = new Adresse(strasse, hausnummer, plz, ort, festnetz);

        schuelerErfassen.saveAngehoeriger(angehoeriger, adresse);

    }
}