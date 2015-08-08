package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.model.SchuelerSuchenModel;
import ch.metzenthin.svm.persistence.entities.*;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.sql.Time;
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
    private String schuljahrKurs;
    private Semesterbezeichnung semesterbezeichnung;
    private Wochentag wochentag;
    private Time zeitBeginn;
    private Lehrkraft lehrkraft;
    private boolean kursFuerSucheBeruecksichtigen;
    private SchuelerCode schuelerCode;
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
        this.schuljahrKurs = schuelerSuchenModel.getSchuljahrKurs();
        this.semesterbezeichnung = schuelerSuchenModel.getSemesterbezeichnung();
        this.wochentag = schuelerSuchenModel.getWochentag();
        this.zeitBeginn = schuelerSuchenModel.getZeitBeginn();
        this.lehrkraft = schuelerSuchenModel.getLehrkraft();
        this.kursFuerSucheBeruecksichtigen = schuelerSuchenModel.isKursFuerSucheBeruecksichtigen();
        this.schuelerCode = schuelerSuchenModel.getSchuelerCode();
    }

    @Override
    public void execute() {

        selectStatementSb = new StringBuilder("select distinct s from Schueler s");
        
        // Inner-Joins erzeugen
        createJoinKurs();
        createJoinSchuelerCode();

        // Where-Selektionen erzeugen
        selectStatementSb.append(" where");
        createWhereSelectionsStammdatenOhneGeburtsdatumSuchperiode();
        createWhereSelectionsGeburtsdatumSuchperiode();
        createWhereSelectionsAnmeldestatus();
        createWhereSelectionsDispensation();
        createWhereSelectionsGeschlecht();
        createWhereSelectionsKurs();
        createWhereSelectionsSchuelerCode();

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
        setParameterKurs();
        setParameterCodeKuerzel();

        schuelerFound = typedQuery.getResultList();
    }

    private void createJoinKurs() {
        if (kursFuerSucheBeruecksichtigen) {
            selectStatementSb.append(" join s.kurse kurs");
            if (lehrkraft != SchuelerSuchenModel.LEHRKRAFT_ALLE) {
                selectStatementSb.append(" join kurs.lehrkraefte lkr");
            }
        }
    }

    private void createJoinSchuelerCode() {
        if (schuelerCode != SchuelerSuchenModel.SCHUELER_CODE_ALLE) {
            selectStatementSb.append(" join s.schuelerCodes cod");
        }
    }

    // Einfache or-Abfrage für Eltern / alle (where s.vorname = :vorname or s.mutter.vorname = :vorname or s.vater.vorname = :vorname)
    // schlägt fehl, wenn mutter oder vater nicht existiert, auch wenn ein anderes or-Element, z.B. s.vorname = :vorname erfüllt wäre. Bug?
    // Abhilfe: Subselects.
    private void createWhereSelectionsStammdatenOhneGeburtsdatumSuchperiode() {
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
        if (person != null && checkNotEmpty(person.getFestnetz())) {
            String selectSchueler = " replace(s.festnetz, ' ', '') = :festnetz";
            String selectEltern = "(replace(s.festnetz, ' ', '') = :festnetz or exists (select s1 from Schueler s1 where replace(s1.mutter.festnetz, ' ', '') = :festnetz and s1.personId = s.personId) or exists (select s2 from Schueler s2 where replace(s2.vater.festnetz, ' ', '') = :festnetz and s2.personId = s.personId)";
            if (rolle == SchuelerSuchenModel.RolleSelected.SCHUELER) {
                selectStatementSb.append(selectSchueler).append(" and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ELTERN) {
                selectStatementSb.append(selectEltern).append(") and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER) {
                selectStatementSb.append(" replace(s.rechnungsempfaenger.festnetz, ' ', '') = :festnetz and");
            } else if (rolle == SchuelerSuchenModel.RolleSelected.ALLE) {
                selectStatementSb.append(selectSchueler).append(" or ").append(selectEltern).append(" or exists (select s3 from Schueler s3 where replace(s3.rechnungsempfaenger.festnetz, ' ', '') = :festnetz and s3.personId = s.personId)) and");
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

    private void createWhereSelectionsGeburtsdatumSuchperiode() {
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

    private void createWhereSelectionsDispensation() {
        if (dispensation == SchuelerSuchenModel.DispensationSelected.DISPENSIERT) {
            selectStatementSb.append(" exists (select dis from Dispensation dis join dis.schueler sch where dis.dispensationsbeginn <= :stichtag and (dis.dispensationsende is null or dis.dispensationsende > :stichtag) and s.personId = sch.personId) and");
        }
        if (dispensation == SchuelerSuchenModel.DispensationSelected.NICHT_DISPENSIERT) {
            selectStatementSb.append(" not exists (select dis from Dispensation dis join dis.schueler sch where dis.dispensationsende is null and s.personId = sch.personId) and :stichtag >= all (select dis.dispensationsende from Dispensation dis join dis.schueler sch where dis.dispensationsende is not null and s.personId = sch.personId) and");
        }
    }

    private void createWhereSelectionsAnmeldestatus() {
        if (anmeldestatus == SchuelerSuchenModel.AnmeldestatusSelected.ANGEMELDET) {
            selectStatementSb.append(" exists (select anm from Anmeldung anm join anm.schueler sch where anm.anmeldedatum <= :stichtag and (anm.abmeldedatum is null or anm.abmeldedatum > :stichtag) and s.personId = sch.personId) and");
        }
        if (anmeldestatus == SchuelerSuchenModel.AnmeldestatusSelected.ABGEMELDET) {
            selectStatementSb.append(" not exists (select anm from Anmeldung anm join anm.schueler sch where anm.abmeldedatum is null and s.personId = sch.personId) and :stichtag >= all (select anm.abmeldedatum from Anmeldung anm join anm.schueler sch where anm.abmeldedatum is not null and s.personId = sch.personId) and");
        }
    }

    private void createWhereSelectionsGeschlecht() {
        if (geschlecht == SchuelerSuchenModel.GeschlechtSelected.WEIBLICH) {
            selectStatementSb.append(" s.geschlecht = 'W' and");
        }
        if (geschlecht == SchuelerSuchenModel.GeschlechtSelected.MAENNLICH) {
            selectStatementSb.append(" s.geschlecht = 'M' and");
        }
    }

    private void createWhereSelectionsKurs() {
        if (kursFuerSucheBeruecksichtigen) {
            selectStatementSb.append(" kurs.semester.schuljahr = :schuljahrKurs and");
            selectStatementSb.append(" kurs.semester.semesterbezeichnung = :semesterbezeichnung and");
            if (wochentag != Wochentag.ALLE) {
                selectStatementSb.append(" kurs.wochentag = :wochentag and");
            }
            if (zeitBeginn != null) {
                selectStatementSb.append(" kurs.zeitBeginn = :zeitBeginn and");
            }
            if (lehrkraft != SchuelerSuchenModel.LEHRKRAFT_ALLE) {
                selectStatementSb.append(" lkr.vorname = :lehrkraftVorname and lkr.nachname = :lehrkraftNachname and lkr.geburtsdatum = :lehrkraftGeburtsdatum and");
            }
        }
    }

    private void createWhereSelectionsSchuelerCode() {
        if (schuelerCode != SchuelerSuchenModel.SCHUELER_CODE_ALLE) {
            selectStatementSb.append(" cod.kuerzel = :codeKuerzel and");
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
            typedQuery.setParameter("festnetz", person.getFestnetz().replaceAll("\\s+", ""));
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

    private void setParameterKurs() {
        if (selectStatementSb.toString().contains(":schuljahrKurs")) {
            typedQuery.setParameter("schuljahrKurs", schuljahrKurs);
        }
        if (selectStatementSb.toString().contains(":semesterbezeichnung")) {
            typedQuery.setParameter("semesterbezeichnung", semesterbezeichnung);
        }
        if (selectStatementSb.toString().contains(":wochentag")) {
            typedQuery.setParameter("wochentag", wochentag);
        }
        if (selectStatementSb.toString().contains(":zeitBeginn")) {
            typedQuery.setParameter("zeitBeginn", zeitBeginn);
        }
        if (selectStatementSb.toString().contains(":lehrkraftVorname")) {
            typedQuery.setParameter("lehrkraftVorname", lehrkraft.getVorname());
        }
        if (selectStatementSb.toString().contains(":lehrkraftNachname")) {
            typedQuery.setParameter("lehrkraftNachname", lehrkraft.getNachname());
        }
        if (selectStatementSb.toString().contains(":lehrkraftGeburtsdatum")) {
            typedQuery.setParameter("lehrkraftGeburtsdatum", lehrkraft.getGeburtsdatum());
        }
    }

    private void setParameterCodeKuerzel() {
        if (selectStatementSb.toString().contains(":codeKuerzel")) {
            typedQuery.setParameter("codeKuerzel", schuelerCode.getKuerzel());
        }
    }

    public List<Schueler> getSchuelerFound() {
        return schuelerFound;
    }
}
