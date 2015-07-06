package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.domain.model.SchuelerSuchenModel;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.PersonSuchen;
import ch.metzenthin.svm.persistence.entities.Schueler;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class SchuelerSuchenCommand extends GenericDaoCommand {

    private static final Logger LOGGER = Logger.getLogger(SchuelerSuchenCommand.class);

    // input
    private PersonSuchen person;
    private Adresse adresse;
    private SchuelerSuchenModel.RolleSelected rolle;
    private SchuelerSuchenModel.AnmeldestatusSelected anmeldestatus;
    private SchuelerSuchenModel.DispensationSelected dispensation;
    private SchuelerSuchenModel.GeschlechtSelected geschlecht;
    private Calendar geburtsdatumSuchperiodeBeginn;
    private Calendar geburtsdatumSuchperiodeEnde;
    private String geburtsdatumSuchperiodeDateFormatString;
    private Calendar stichtag;
    private StringBuilder selectStatementSb;
    private TypedQuery<Schueler> typedQuery;

    // output
    private List<Schueler> schuelerFound;

    public SchuelerSuchenCommand(SchuelerSuchenModel schuelerSuchenModel) {
        this.adresse = schuelerSuchenModel.getAdresse();
        this.person = schuelerSuchenModel.getPerson();
        this.rolle = schuelerSuchenModel.getRolle();
        this.anmeldestatus = schuelerSuchenModel.getAnmeldestatus();
        this.dispensation = schuelerSuchenModel.getDispensation();
        this.geschlecht = schuelerSuchenModel.getGeschlecht();
        this.geburtsdatumSuchperiodeBeginn = schuelerSuchenModel.getGeburtsdatumSuchperiodeBeginn();
        this.geburtsdatumSuchperiodeEnde = schuelerSuchenModel.getGeburtsdatumSuchperiodeEnde();
        this.geburtsdatumSuchperiodeDateFormatString = schuelerSuchenModel.getGeburtsdatumSuchperiodeDateFormatString();
        this.stichtag = schuelerSuchenModel.getStichtag();
    }

    @Override
    public void execute() {

        selectStatementSb = new StringBuilder("select s from Schueler s where");

        // Query erzeugen
        createQueryStammdatenOhneGeburtsdatumSuchperiode();
        createQueryGeburtsdatumSuchperiode();
        createQueryAnmeldestatus();
        createQueryDispensation();
        createQueryGeschlecht();

        // Letztes " and" löschen
        if (selectStatementSb.substring(selectStatementSb.length() - 4).equals(" and")) {
            selectStatementSb.setLength(selectStatementSb.length() - 4);
        }

        // "where" löschen, falls dieses am Schluss steht
        if (selectStatementSb.substring(selectStatementSb.length() - 5).equals("where")) {
            selectStatementSb.setLength(selectStatementSb.length() - 5);
        }

        // Sortierung
        selectStatementSb.append(" order by s.nachname, s.vorname, s.adresse.ort, s.adresse.strasse");

        LOGGER.trace("JPQL Select-Statement: " + selectStatementSb.toString());

        typedQuery = entityManager.createQuery(selectStatementSb.toString(), Schueler.class);

        // Suchparameter setzen
        setParameterStammdatenOhneGeburtsdatumSuchperiode();
        setParameterGeburtsdatumSuchperiode();
        setParameterStichtag();

        schuelerFound = typedQuery.getResultList();
    }

    // Einfache or-Abfrage für Eltern / alle (where s.vorname = :vorname or s.mutter.vorname = :vorname or s.vater.vorname = :vorname)
    // schlägt fehl, wenn mutter oder vater nicht existiert, auch wenn ein anderes or-Element, z.B. s.vorname = :vorname erfüllt wäre. Bug?
    // Abhilfe: Subselects.
    private void createQueryStammdatenOhneGeburtsdatumSuchperiode() {
        if (person != null && checkNotEmpty(person.getVorname())) {
            String selectSchueler = " lower(s.vorname) = :vorname";
            String selectEltern = "(exists (select s1 from Schueler s1 where lower(s1.mutter.vorname) = :vorname and s1.personId = s.personId) or exists (select s2 from Schueler s2 where lower(s2.vater.vorname) = :vorname and s2.personId = s.personId)";
            if (rolle == SchuelerSuchenModel.RolleSelected.SCHUELER) {
                selectStatementSb.append(selectSchueler).append(" and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ELTERN) {
                selectStatementSb.append(selectEltern).append(") and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER) {
                selectStatementSb.append(" lower(s.rechnungsempfaenger.vorname) = :vorname and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ALLE) {
                selectStatementSb.append(selectSchueler).append(" or ").append(selectEltern).append(" or exists (select s3 from Schueler s3 where lower(s3.rechnungsempfaenger.vorname) = :vorname and s3.personId = s.personId)) and");
            }
        }
        if (person != null && checkNotEmpty(person.getNachname())) {
            String selectSchueler = " lower(s.nachname) = :nachname";
            String selectEltern = "(exists (select s1 from Schueler s1 where lower(s1.mutter.nachname) = :nachname and s1.personId = s.personId) or exists (select s2 from Schueler s2 where lower(s2.vater.nachname) = :nachname and s2.personId = s.personId)";
            if (rolle == SchuelerSuchenModel.RolleSelected.SCHUELER) {
                selectStatementSb.append(selectSchueler).append(" and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ELTERN) {
                selectStatementSb.append(selectEltern).append(") and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER) {
                selectStatementSb.append(" lower(s.rechnungsempfaenger.nachname) = :nachname and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ALLE) {
                selectStatementSb.append(selectSchueler).append(" or ").append(selectEltern).append(" or exists (select s3 from Schueler s3 where lower(s3.rechnungsempfaenger.nachname) = :nachname and s3.personId = s.personId)) and");
            }
        }
        if (adresse != null && checkNotEmpty(adresse.getStrasse())) {
            String selectSchueler = " lower(s.adresse.strasse) = :strasse";
            String selectEltern = "(lower(s.adresse.strasse) = :strasse or exists (select s1 from Schueler s1 where lower(s1.mutter.adresse.strasse) = :strasse and s1.personId = s.personId) or exists (select s2 from Schueler s2 where lower(s2.vater.adresse.strasse) = :strasse and s2.personId = s.personId)";
            if (rolle == SchuelerSuchenModel.RolleSelected.SCHUELER) {
                selectStatementSb.append(selectSchueler).append(" and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ELTERN) {
                selectStatementSb.append(selectEltern).append(") and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER) {
                selectStatementSb.append(" lower(s.rechnungsempfaenger.adresse.strasse) = :strasse and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ALLE) {
                selectStatementSb.append(selectSchueler).append(" or ").append(selectEltern).append(" or exists (select s3 from Schueler s3 where lower(s3.rechnungsempfaenger.adresse.strasse) = :strasse and s3.personId = s.personId)) and");
            }
        }
        if (adresse != null && checkNotEmpty(adresse.getHausnummer())) {
            String selectSchueler = " lower(s.adresse.hausnummer) = :hausnummer";
            String selectEltern = "(lower(s.adresse.hausnummer) = :hausnummer or exists (select s1 from Schueler s1 where lower(s1.mutter.adresse.hausnummer) = :hausnummer and s1.personId = s.personId) or exists (select s2 from Schueler s2 where lower(s2.vater.adresse.hausnummer) = :hausnummer and s2.personId = s.personId)";
            if (rolle == SchuelerSuchenModel.RolleSelected.SCHUELER) {
                selectStatementSb.append(selectSchueler).append(" and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ELTERN) {
                selectStatementSb.append(selectEltern).append(") and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER) {
                selectStatementSb.append(" lower(s.rechnungsempfaenger.adresse.hausnummer) = :hausnummer and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ALLE) {
                selectStatementSb.append(selectSchueler).append(" or ").append(selectEltern).append(" or exists (select s3 from Schueler s3 where lower(s3.rechnungsempfaenger.adresse.hausnummer) = :hausnummer and s3.personId = s.personId)) and");
            }
        }
        if (adresse != null && checkNotEmpty(adresse.getPlz())) {
            String selectSchueler = "  s.adresse.plz = :plz";
            String selectEltern = "(s.adresse.plz = :plz or exists (select s1 from Schueler s1 where s1.mutter.adresse.plz = :plz and s1.personId = s.personId) or exists (select s2 from Schueler s2 where s2.vater.adresse.plz = :plz and s2.personId = s.personId)";
            if (rolle == SchuelerSuchenModel.RolleSelected.SCHUELER) {
                selectStatementSb.append(selectSchueler).append(" and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ELTERN) {
                selectStatementSb.append(selectEltern).append(") and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER) {
                selectStatementSb.append(" s.rechnungsempfaenger.adresse.plz = :plz and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ALLE) {
                selectStatementSb.append(selectSchueler).append(" or ").append(selectEltern).append(" or exists (select s3 from Schueler s3 where s3.rechnungsempfaenger.adresse.plz = :plz and s3.personId = s.personId)) and");
            }
        }
        if (adresse != null && checkNotEmpty(adresse.getOrt())) {
            String selectSchueler = " lower(s.adresse.ort) = :ort";
            String selectEltern = "(lower(s.adresse.ort) = :ort or exists (select s1 from Schueler s1 where lower(s1.mutter.adresse.ort) = :ort and s1.personId = s.personId) or exists (select s2 from Schueler s2 where lower(s2.vater.adresse.ort) = :ort and s2.personId = s.personId)";
            if (rolle == SchuelerSuchenModel.RolleSelected.SCHUELER) {
                selectStatementSb.append(selectSchueler).append(" and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ELTERN) {
                selectStatementSb.append(selectEltern).append(") and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER) {
                selectStatementSb.append(" lower(s.rechnungsempfaenger.adresse.ort) = :ort and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ALLE) {
                selectStatementSb.append(selectSchueler).append(" or ").append(selectEltern).append(" or exists (select s3 from Schueler s3 where lower(s3.rechnungsempfaenger.adresse.ort) = :ort and s3.personId = s.personId)) and");
            }
        }
        if (adresse != null && checkNotEmpty(adresse.getFestnetz())) {
            String selectSchueler = " replace(s.adresse.festnetz, ' ', '') = :festnetz";
            String selectEltern = "(replace(s.adresse.festnetz, ' ', '') = :festnetz or exists (select s1 from Schueler s1 where replace(s1.mutter.adresse.festnetz, ' ', '') = :festnetz and s1.personId = s.personId) or exists (select s2 from Schueler s2 where replace(s2.vater.adresse.festnetz, ' ', '') = :festnetz and s2.personId = s.personId)";
            if (rolle == SchuelerSuchenModel.RolleSelected.SCHUELER) {
                selectStatementSb.append(selectSchueler).append(" and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ELTERN) {
                selectStatementSb.append(selectEltern).append(") and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER) {
                selectStatementSb.append(" replace(s.rechnungsempfaenger.adresse.festnetz, ' ', '') = :festnetz and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ALLE) {
                selectStatementSb.append(selectSchueler).append(" or ").append(selectEltern).append(" or exists (select s3 from Schueler s3 where replace(s3.rechnungsempfaenger.adresse.festnetz, ' ', '') = :festnetz and s3.personId = s.personId)) and");
            }
        }
        if (person != null && checkNotEmpty(person.getNatel())) {
            String selectSchueler = " replace(s.natel, ' ', '') = :natel";
            String selectEltern = "(replace(s.natel, ' ', '') = :natel or exists (select s1 from Schueler s1 where replace(s1.mutter.natel, ' ', '') = :natel and s1.personId = s.personId) or exists (select s2 from Schueler s2 where replace(s2.vater.natel, ' ', '') = :natel and s2.personId = s.personId)";
            if (rolle == SchuelerSuchenModel.RolleSelected.SCHUELER) {
                selectStatementSb.append(selectSchueler).append(" and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ELTERN) {
                selectStatementSb.append(selectEltern).append(") and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER) {
                selectStatementSb.append(" replace(s.rechnungsempfaenger.natel, ' ', '') = :natel and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ALLE) {
                selectStatementSb.append(selectSchueler).append(" or ").append(selectEltern).append(" or exists (select s3 from Schueler s3 where replace(s3.rechnungsempfaenger.natel, ' ', '') = :natel and s3.personId = s.personId)) and");
            }
        }
        if (person != null && checkNotEmpty(person.getEmail())) {
            String selectSchueler = " lower(s.email) = :email";
            String selectEltern = "(lower(s.email) = :email or exists (select s1 from Schueler s1 where lower(s1.mutter.email) = :email and s1.personId = s.personId) or exists (select s2 from Schueler s2 where lower(s2.vater.email) = :email and s2.personId = s.personId)";
            if (rolle == SchuelerSuchenModel.RolleSelected.SCHUELER) {
                selectStatementSb.append(selectSchueler).append(" and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ELTERN) {
                selectStatementSb.append(selectEltern).append(") and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER) {
                selectStatementSb.append(" lower(s.rechnungsempfaenger.email) = :email and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ALLE) {
                selectStatementSb.append(selectSchueler).append(" or ").append(selectEltern).append(" or exists (select s3 from Schueler s3 where lower(s3.rechnungsempfaenger.email) = :email and s3.personId = s.personId)) and");
            }
        }
    }

    private void createQueryGeburtsdatumSuchperiode() {
        if (geburtsdatumSuchperiodeBeginn == null && geburtsdatumSuchperiodeEnde == null) {
            return;
        }
        if (geburtsdatumSuchperiodeDateFormatString.equals("dd.MM.")) {
            selectStatementSb.append(" function('DAY', s.geburtsdatum) = function('DAY', :geburtsdatumSuchperiodeBeginn) and function('MONTH', s.geburtsdatum) = function('MONTH', :geburtsdatumSuchperiodeBeginn) and");
        }
        if (geburtsdatumSuchperiodeDateFormatString.equals("MM.yyyy")) {
            if (geburtsdatumSuchperiodeBeginn != null && geburtsdatumSuchperiodeEnde != null) {
                selectStatementSb.append(" s.geburtsdatum >= :geburtsdatumSuchperiodeBegMmYyyy and s.geburtsdatum < :geburtsdatumSuchperiodeEndMmYyyy and");
            } else if (geburtsdatumSuchperiodeBeginn != null) {
                selectStatementSb.append(" function('MONTH', s.geburtsdatum) = function('MONTH', :geburtsdatumSuchperiodeBeginn) and function('YEAR', s.geburtsdatum) = function('YEAR', :geburtsdatumSuchperiodeBeginn) and");
            }
        }
        if (geburtsdatumSuchperiodeDateFormatString.equals("dd.MM.yyyy")) {
            if (geburtsdatumSuchperiodeBeginn != null && geburtsdatumSuchperiodeEnde != null) {
                selectStatementSb.append(" s.geburtsdatum >= :geburtsdatumSuchperiodeBeginn and s.geburtsdatum <= :geburtsdatumSuchperiodeEnde and");
            } else if (geburtsdatumSuchperiodeBeginn != null) {
                selectStatementSb.append(" s.geburtsdatum = :geburtsdatumSuchperiodeBeginn and");
            }
        }
        if (geburtsdatumSuchperiodeDateFormatString.equals("yyyy")) {
            if (geburtsdatumSuchperiodeBeginn != null && geburtsdatumSuchperiodeEnde != null) {
                selectStatementSb.append(" function('YEAR', s.geburtsdatum) >= function('YEAR', :geburtsdatumSuchperiodeBeginn) and function('YEAR', s.geburtsdatum) <= function('YEAR', :geburtsdatumSuchperiodeEnde) and");
            } else if (geburtsdatumSuchperiodeBeginn != null) {
                selectStatementSb.append(" function('YEAR', s.geburtsdatum) = function('YEAR', :geburtsdatumSuchperiodeBeginn) and");
            }
        }
    }

    private void createQueryDispensation() {
        if (dispensation == SchuelerSuchenModel.DispensationSelected.DISPENSIERT) {
            selectStatementSb.append(" exists (select dis from Dispensation dis join dis.schueler sch where dis.dispensationsbeginn <= :stichtag and (dis.dispensationsende is null or dis.dispensationsende > :stichtag) and s.personId = sch.personId) and");
        }
        if (dispensation == SchuelerSuchenModel.DispensationSelected.NICHT_DISPENSIERT) {
            selectStatementSb.append(" not exists (select dis from Dispensation dis join dis.schueler sch where dis.dispensationsende is null and s.personId = sch.personId) and :stichtag >= all (select dis.dispensationsende from Dispensation dis join dis.schueler sch where dis.dispensationsende is not null and s.personId = sch.personId) and");
        }
    }

    private void createQueryAnmeldestatus() {
        if (anmeldestatus == SchuelerSuchenModel.AnmeldestatusSelected.ANGEMELDET) {
            selectStatementSb.append(" exists (select anm from Anmeldung anm join anm.schueler sch where anm.anmeldedatum <= :stichtag and (anm.abmeldedatum is null or anm.abmeldedatum > :stichtag) and s.personId = sch.personId) and");
        }
        if (anmeldestatus == SchuelerSuchenModel.AnmeldestatusSelected.ABGEMELDET) {
            selectStatementSb.append(" not exists (select anm from Anmeldung anm join anm.schueler sch where anm.abmeldedatum is null and s.personId = sch.personId) and :stichtag >= all (select anm.abmeldedatum from Anmeldung anm join anm.schueler sch where anm.abmeldedatum is not null and s.personId = sch.personId) and");
        }
    }

    private void createQueryGeschlecht() {
        if (geschlecht == SchuelerSuchenModel.GeschlechtSelected.WEIBLICH) {
            selectStatementSb.append(" s.geschlecht = 'W' and");
        }
        if (geschlecht == SchuelerSuchenModel.GeschlechtSelected.MAENNLICH) {
            selectStatementSb.append(" s.geschlecht = 'M' and");
        }
    }

    private void setParameterStammdatenOhneGeburtsdatumSuchperiode() {
        if (selectStatementSb.toString().contains(":vorname")) {
            typedQuery.setParameter("vorname", person.getVorname().toLowerCase());
        }
        if (selectStatementSb.toString().contains(":nachname")) {
            typedQuery.setParameter("nachname", person.getNachname().toLowerCase());
        }
        if (selectStatementSb.toString().contains(":strasse")) {
            typedQuery.setParameter("strasse", adresse.getStrasse().toLowerCase());
        }
        if (selectStatementSb.toString().contains(":hausnummer")) {
            typedQuery.setParameter("hausnummer", adresse.getHausnummer().toLowerCase());
        }
        if (selectStatementSb.toString().contains(":plz")) {
            typedQuery.setParameter("plz", adresse.getPlz());
        }
        if (selectStatementSb.toString().contains(":ort")) {
            typedQuery.setParameter("ort", adresse.getOrt().toLowerCase());
        }
        if (selectStatementSb.toString().contains(":festnetz")) {
            typedQuery.setParameter("festnetz", adresse.getFestnetz().replaceAll("\\s+", ""));
        }
        if (selectStatementSb.toString().contains(":natel")) {
            typedQuery.setParameter("natel", person.getNatel().replaceAll("\\s+", ""));
        }
        if (selectStatementSb.toString().contains(":email")) {
            typedQuery.setParameter("email", person.getEmail().toLowerCase());
        }
    }

    private void setParameterGeburtsdatumSuchperiode() {
        if (selectStatementSb.toString().contains(":geburtsdatumSuchperiodeBeginn")) {
            typedQuery.setParameter("geburtsdatumSuchperiodeBeginn", geburtsdatumSuchperiodeBeginn);
        }
        if (selectStatementSb.toString().contains(":geburtsdatumSuchperiodeEnde")) {
            typedQuery.setParameter("geburtsdatumSuchperiodeEnde", geburtsdatumSuchperiodeEnde);
        }
        if (selectStatementSb.toString().contains(":geburtsdatumSuchperiodeBegMmYyyy")) {
            Calendar geburtsdatumSuchperiodeBeginnMmYyyy = new GregorianCalendar(geburtsdatumSuchperiodeBeginn.get(Calendar.YEAR), geburtsdatumSuchperiodeBeginn.get(Calendar.MONTH), 1);
            typedQuery.setParameter("geburtsdatumSuchperiodeBegMmYyyy", geburtsdatumSuchperiodeBeginnMmYyyy);
        }
        if (selectStatementSb.toString().contains(":geburtsdatumSuchperiodeEndMmYyyy")) {
            Calendar geburtsdatumSuchperiodeEndeMmYyyy;
            if (geburtsdatumSuchperiodeEnde.get(Calendar.MONTH) == Calendar.DECEMBER) {
                geburtsdatumSuchperiodeEndeMmYyyy = new GregorianCalendar(geburtsdatumSuchperiodeEnde.get(Calendar.YEAR) + 1, Calendar.JANUARY, 1);
            } else {
                geburtsdatumSuchperiodeEndeMmYyyy = new GregorianCalendar(geburtsdatumSuchperiodeEnde.get(Calendar.YEAR), geburtsdatumSuchperiodeEnde.get(Calendar.MONTH) + 1, 1);
            }
            typedQuery.setParameter("geburtsdatumSuchperiodeEndMmYyyy", geburtsdatumSuchperiodeEndeMmYyyy);
        }
    }

    private void setParameterStichtag() {
        if (selectStatementSb.toString().contains(":stichtag")) {
            typedQuery.setParameter("stichtag", stichtag);
        }
    }

    public List<Schueler> getSchuelerFound() {
        return schuelerFound;
    }
}
