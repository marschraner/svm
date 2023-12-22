package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Gruppe;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.model.SchuelerSuchenModel;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.*;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Time;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class SchuelerSuchenCommand implements Command {

    public static final int MAX_RESULTS = 5000;

    private static final Logger LOGGER = LogManager.getLogger(SchuelerSuchenCommand.class);

    private final DB db = DBFactory.getInstance();

    // input
    private final PersonSuchen person;
    private final Adresse adresse;
    private final SchuelerSuchenModel.RolleSelected rolle;
    private final SchuelerSuchenModel.AnmeldestatusSelected anmeldestatus;
    private final SchuelerSuchenModel.DispensationSelected dispensation;
    private final SchuelerSuchenModel.GeschlechtSelected geschlecht;
    private final Calendar geburtsdatumSuchperiodeBeginn;
    private final Calendar geburtsdatumSuchperiodeEnde;
    private final String geburtsdatumSuchperiodeDateFormatString;
    private final Calendar stichtag;
    private final SchuelerCode schuelerCode;
    private final Semester semesterKurs;
    private final Wochentag wochentag;
    private final Time zeitBeginn;
    private final Mitarbeiter mitarbeiter;
    private final boolean kursFuerSucheBeruecksichtigen;
    private final Maerchen maerchen;
    private final Gruppe gruppe;
    private final String rollen;
    private final ElternmithilfeCode elternmithilfeCode;
    private final Integer kuchenVorstellung;
    private final String zusatzattributMaerchen;
    private final boolean maerchenFuerSucheBeruecksichtigen;
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
        this.schuelerCode = schuelerSuchenModel.getSchuelerCode();
        this.semesterKurs = schuelerSuchenModel.getSemesterKurs();
        this.wochentag = schuelerSuchenModel.getWochentag();
        this.zeitBeginn = schuelerSuchenModel.getZeitBeginn();
        this.mitarbeiter = schuelerSuchenModel.getMitarbeiter();
        this.kursFuerSucheBeruecksichtigen = schuelerSuchenModel.isKursFuerSucheBeruecksichtigen();
        this.maerchen = schuelerSuchenModel.getMaerchen();
        this.gruppe = schuelerSuchenModel.getGruppe();
        this.rollen = schuelerSuchenModel.getRollen();
        this.elternmithilfeCode = schuelerSuchenModel.getElternmithilfeCode();
        this.kuchenVorstellung = schuelerSuchenModel.getKuchenVorstellung();
        this.zusatzattributMaerchen = schuelerSuchenModel.getZusatzattributMaerchen();
        this.maerchenFuerSucheBeruecksichtigen = schuelerSuchenModel.isMaerchenFuerSucheBeruecksichtigen();
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void execute() {

        selectStatementSb = new StringBuilder("select distinct s from Schueler s");

        // Inner-Joins erzeugen
        createJoinSchuelerCode();
        createJoinKurs();
        createJoinMaerchen();

        // Where-Selektionen erzeugen
        selectStatementSb.append(" where");
        createWhereSelectionsStammdatenOhneGeburtsdatumSuchperiode();
        createWhereSelectionsGeburtsdatumSuchperiode();
        createWhereSelectionsAnmeldestatus();
        createWhereSelectionsDispensation();
        createWhereSelectionsGeschlecht();
        createWhereSelectionsSchuelerCode();
        createWhereSelectionsKurs();
        createWhereSelectionsMaerchen();

        // Letztes " and" löschen
        if (selectStatementSb.substring(selectStatementSb.length() - 4).equals(" and")) {
            selectStatementSb.setLength(selectStatementSb.length() - 4);
        }

        // "where" löschen, falls dieses am Schluss steht
        if (selectStatementSb.substring(selectStatementSb.length() - 5).equals("where")) {
            selectStatementSb.setLength(selectStatementSb.length() - 5);
        }

        LOGGER.trace("JPQL Select-Statement: " + selectStatementSb.toString());

        typedQuery = db.getCurrentEntityManager().createQuery(
                selectStatementSb.toString(), Schueler.class);

        // Maximale Anzahl zurückzuliefernde Werte
        typedQuery.setMaxResults(MAX_RESULTS);

        // Suchparameter setzen
        setParameterStammdatenOhneGeburtsdatumSuchperiode();
        setParameterGeburtsdatumSuchperiode();
        setParameterStichtag();
        setParameterSchuelerCodeId();
        setParameterKurs();
        setParameterMaerchen();

        schuelerFound = typedQuery.getResultList();

        // Sortierung in Java (weil SELECT DISTINCT ... ORDER BY ... s.adresse.ort, s.adresse.strasse nicht mehr erlaubt ab MySQL 5.7,
        // vgl. http://www.programmerinterview.com/index.php/database-sql/sql-select-distinct-and-order-by/)
        Collections.sort(schuelerFound);
    }

    private void createJoinSchuelerCode() {
        if (schuelerCode != SchuelerSuchenModel.SCHUELER_CODE_ALLE) {
            selectStatementSb.append(" join s.schuelerCodes cod");
        }
    }

    private void createJoinKurs() {
        if (kursFuerSucheBeruecksichtigen) {
            selectStatementSb.append(" join s.kursanmeldungen kursanm");
            if (mitarbeiter != SchuelerSuchenModel.MITARBEITER_ALLE) {
                selectStatementSb.append(" join kursanm.kurs.lehrkraefte lkr");
            }
        }
    }

    private void createJoinMaerchen() {
        if (maerchenFuerSucheBeruecksichtigen) {
            selectStatementSb.append(" join s.maercheneinteilungen mae");
        }
    }

    private void createWhereSelectionsStammdatenOhneGeburtsdatumSuchperiode() {
        if (person == null ||
                (!checkNotEmpty(person.getVorname()) &&
                        !checkNotEmpty(person.getNachname()) &&
                        !checkNotEmpty(adresse.getStrasse()) &&
                        !checkNotEmpty(adresse.getHausnummer()) &&
                        !checkNotEmpty(adresse.getPlz()) &&
                        !checkNotEmpty(adresse.getOrt()) &&
                        !checkNotEmpty(person.getFestnetz()) &&
                        !checkNotEmpty(person.getNatel()) &&
                        !checkNotEmpty(person.getEmail()))) {
            return;
        }
        if (rolle == SchuelerSuchenModel.RolleSelected.SCHUELER) {
            createWhereSelectionsStammdatenSchuelerOhneGeburtsdatumSuchperiode();
            selectStatementSb.append(" and");
        } else if (rolle == SchuelerSuchenModel.RolleSelected.ELTERN) {
            selectStatementSb.append(" ((");
            createWhereSelectionsStammdatenMutter();
            selectStatementSb.append(") or (");
            createWhereSelectionsStammdatenVater();
            selectStatementSb.append(")) and");
        } else if (rolle == SchuelerSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER) {
            createWhereSelectionsStammdatenRechnungsempfaenger();
            selectStatementSb.append(" and");
        } else if (rolle == SchuelerSuchenModel.RolleSelected.ALLE) {
            selectStatementSb.append(" ((");
            createWhereSelectionsStammdatenSchuelerOhneGeburtsdatumSuchperiode();
            selectStatementSb.append(") or (");
            createWhereSelectionsStammdatenMutter();
            selectStatementSb.append(") or (");
            createWhereSelectionsStammdatenVater();
            selectStatementSb.append(") or (");
            createWhereSelectionsStammdatenRechnungsempfaenger();
            selectStatementSb.append(")) and");
        }
    }

    private void createWhereSelectionsStammdatenSchuelerOhneGeburtsdatumSuchperiode() {
        if (person != null && checkNotEmpty(person.getVorname())) {
            selectStatementSb.append(" lower(s.vorname) like :vorname and");
        }
        if (person != null && checkNotEmpty(person.getNachname())) {
            selectStatementSb.append(" lower(s.nachname) like :nachname and");
        }
        if (adresse != null && checkNotEmpty(adresse.getStrasse())) {
            selectStatementSb.append(" lower(s.adresse.strasse) like :strasse and");
        }
        if (adresse != null && checkNotEmpty(adresse.getHausnummer())) {
            selectStatementSb.append(" lower(s.adresse.hausnummer) = :hausnummer and");
        }
        if (adresse != null && checkNotEmpty(adresse.getPlz())) {
            selectStatementSb.append(" s.adresse.plz = :plz and");
        }
        if (adresse != null && checkNotEmpty(adresse.getOrt())) {
            selectStatementSb.append(" lower(s.adresse.ort) like :ort and");
        }
        if (person != null && checkNotEmpty(person.getFestnetz())) {
            selectStatementSb.append(" replace(s.festnetz, ' ', '') = :festnetz and");
        }
        if (person != null && checkNotEmpty(person.getNatel())) {
            selectStatementSb.append(" replace(s.natel, ' ', '') = :natel and");
        }
        if (person != null && checkNotEmpty(person.getEmail())) {
            selectStatementSb.append(" lower(s.email) like :email and");
        }
        selectStatementSb.setLength(selectStatementSb.length() - 4);  // letztes ' and' loeschen
    }

    private void createWhereSelectionsStammdatenMutter() {
        // Einfache or-Abfrage für Eltern / alle (where s.vorname = :vorname or s.mutter.vorname = :vorname or s.vater.vorname = :vorname)
        // schlägt fehl, wenn mutter oder vater nicht existiert, auch wenn ein anderes or-Element, z.B. s.vorname = :vorname erfüllt wäre. Bug?
        // Abhilfe: Subselects.
        if (person != null && checkNotEmpty(person.getVorname())) {
            selectStatementSb.append(" exists (select s1 from Schueler s1 where lower(s1.mutter.vorname) like :vorname and s1.personId = s.personId) and");
        }
        if (person != null && checkNotEmpty(person.getNachname())) {
            selectStatementSb.append(" exists (select s1 from Schueler s1 where lower(s1.mutter.nachname) like :nachname and s1.personId = s.personId) and");
        }
        if (adresse != null && checkNotEmpty(adresse.getStrasse())) {
            selectStatementSb.append(" (lower(s.adresse.strasse) like :strasse or exists (select s1 from Schueler s1 where lower(s1.mutter.adresse.strasse) like :strasse and s1.personId = s.personId)) and");
        }
        if (adresse != null && checkNotEmpty(adresse.getHausnummer())) {
            selectStatementSb.append(" (lower(s.adresse.hausnummer) = :hausnummer or exists (select s1 from Schueler s1 where lower(s1.mutter.adresse.hausnummer) = :hausnummer and s1.personId = s.personId)) and");
        }
        if (adresse != null && checkNotEmpty(adresse.getPlz())) {
            selectStatementSb.append(" (lower(s.adresse.plz) like :plz or exists (select s1 from Schueler s1 where lower(s1.mutter.adresse.plz) like :plz and s1.personId = s.personId)) and");
        }
        if (adresse != null && checkNotEmpty(adresse.getOrt())) {
            selectStatementSb.append(" (lower(s.adresse.ort) like :ort or exists (select s1 from Schueler s1 where lower(s1.mutter.adresse.ort) like :ort and s1.personId = s.personId)) and");
        }
        if (person != null && checkNotEmpty(person.getFestnetz())) {
            selectStatementSb.append(" (replace(s.festnetz, ' ', '') = :festnetz or exists (select s1 from Schueler s1 where replace(s1.mutter.festnetz, ' ', '') = :festnetz and s1.personId = s.personId)) and");
        }
        if (person != null && checkNotEmpty(person.getNatel())) {
            selectStatementSb.append(" (replace(s.natel, ' ', '') = :natel or exists (select s1 from Schueler s1 where replace(s1.mutter.natel, ' ', '') = :natel and s1.personId = s.personId)) and");
        }
        if (person != null && checkNotEmpty(person.getEmail())) {
            selectStatementSb.append(" (lower(s.email) like :email or exists (select s1 from Schueler s1 where lower(s1.mutter.email) like :email and s1.personId = s.personId)) and");
        }
        selectStatementSb.setLength(selectStatementSb.length() - 4);  // letztes ' and' loeschen
    }

    private void createWhereSelectionsStammdatenVater() {
        if (person != null && checkNotEmpty(person.getVorname())) {
            selectStatementSb.append(" exists (select s1 from Schueler s1 where lower(s1.vater.vorname) like :vorname and s1.personId = s.personId) and");
        }
        if (person != null && checkNotEmpty(person.getNachname())) {
            selectStatementSb.append(" exists (select s1 from Schueler s1 where lower(s1.vater.nachname) like :nachname and s1.personId = s.personId) and");
        }
        if (adresse != null && checkNotEmpty(adresse.getStrasse())) {
            selectStatementSb.append(" (lower(s.adresse.strasse) like :strasse or exists (select s1 from Schueler s1 where lower(s1.vater.adresse.strasse) like :strasse and s1.personId = s.personId)) and");
        }
        if (adresse != null && checkNotEmpty(adresse.getHausnummer())) {
            selectStatementSb.append(" (lower(s.adresse.hausnummer) = :hausnummer or exists (select s1 from Schueler s1 where lower(s1.vater.adresse.hausnummer) = :hausnummer and s1.personId = s.personId)) and");
        }
        if (adresse != null && checkNotEmpty(adresse.getPlz())) {
            selectStatementSb.append(" (lower(s.adresse.plz) like :plz or exists (select s1 from Schueler s1 where lower(s1.vater.adresse.plz) like :plz and s1.personId = s.personId)) and");
        }
        if (adresse != null && checkNotEmpty(adresse.getOrt())) {
            selectStatementSb.append(" (lower(s.adresse.ort) like :ort or exists (select s1 from Schueler s1 where lower(s1.vater.adresse.ort) like :ort and s1.personId = s.personId)) and");
        }
        if (person != null && checkNotEmpty(person.getFestnetz())) {
            selectStatementSb.append(" (replace(s.festnetz, ' ', '') = :festnetz or exists (select s1 from Schueler s1 where replace(s1.vater.festnetz, ' ', '') = :festnetz and s1.personId = s.personId)) and");
        }
        if (person != null && checkNotEmpty(person.getNatel())) {
            selectStatementSb.append(" (replace(s.natel, ' ', '') = :natel or exists (select s1 from Schueler s1 where replace(s1.vater.natel, ' ', '') = :natel and s1.personId = s.personId)) and");
        }
        if (person != null && checkNotEmpty(person.getEmail())) {
            selectStatementSb.append(" (lower(s.email) like :email or exists (select s1 from Schueler s1 where lower(s1.vater.email) like :email and s1.personId = s.personId)) and");
        }
        selectStatementSb.setLength(selectStatementSb.length() - 4);  // letztes ' and' loeschen
    }

    private void createWhereSelectionsStammdatenRechnungsempfaenger() {
        if (person != null && checkNotEmpty(person.getVorname())) {
            selectStatementSb.append(" lower(s.rechnungsempfaenger.vorname) like :vorname and");
        }
        if (person != null && checkNotEmpty(person.getNachname())) {
            selectStatementSb.append(" lower(s.rechnungsempfaenger.nachname) like :nachname and");
        }
        if (adresse != null && checkNotEmpty(adresse.getStrasse())) {
            selectStatementSb.append(" lower(s.rechnungsempfaenger.adresse.strasse) like :strasse and");
        }
        if (adresse != null && checkNotEmpty(adresse.getHausnummer())) {
            selectStatementSb.append(" lower(s.rechnungsempfaenger.adresse.hausnummer) = :hausnummer and");
        }
        if (adresse != null && checkNotEmpty(adresse.getPlz())) {
            selectStatementSb.append(" s.rechnungsempfaenger.adresse.plz = :plz and");
        }
        if (adresse != null && checkNotEmpty(adresse.getOrt())) {
            selectStatementSb.append(" lower(s.rechnungsempfaenger.adresse.ort) like :ort and");
        }
        if (person != null && checkNotEmpty(person.getFestnetz())) {
            selectStatementSb.append(" replace(s.rechnungsempfaenger.festnetz, ' ', '') = :festnetz and");
        }
        if (person != null && checkNotEmpty(person.getNatel())) {
            selectStatementSb.append(" replace(s.rechnungsempfaenger.natel, ' ', '') = :natel and");
        }
        if (person != null && checkNotEmpty(person.getEmail())) {
            selectStatementSb.append(" lower(s.rechnungsempfaenger.email) like :email and");
        }
        selectStatementSb.setLength(selectStatementSb.length() - 4);  // letztes ' and' loeschen
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
        // Eine Dispensation dauert bis und mit Dispensationsende
        if (dispensation == SchuelerSuchenModel.DispensationSelected.DISPENSIERT) {
            selectStatementSb.append(" exists (select dis from Dispensation dis join dis.schueler sch where dis.dispensationsbeginn <= :stichtag and (dis.dispensationsende is null or dis.dispensationsende >= :stichtag) and s.personId = sch.personId) and");
        }
        if (dispensation == SchuelerSuchenModel.DispensationSelected.NICHT_DISPENSIERT) {
            selectStatementSb.append(" not exists (select dis from Dispensation dis join dis.schueler sch where dis.dispensationsbeginn <= :stichtag and (dis.dispensationsende is null or dis.dispensationsende >= :stichtag) and s.personId = sch.personId) and");
        }
    }

    private void createWhereSelectionsAnmeldestatus() {
        // Eine Anmeldung dauert bis und mit Abmeldedatum
        if (anmeldestatus == SchuelerSuchenModel.AnmeldestatusSelected.ANGEMELDET) {
            selectStatementSb.append(" exists (select anm from Anmeldung anm join anm.schueler sch where anm.anmeldedatum <= :stichtag and (anm.abmeldedatum is null or anm.abmeldedatum >= :stichtag) and s.personId = sch.personId) and");
        }
        if (anmeldestatus == SchuelerSuchenModel.AnmeldestatusSelected.ABGEMELDET) {
            selectStatementSb.append(" not exists (select anm from Anmeldung anm join anm.schueler sch where anm.anmeldedatum <= :stichtag and (anm.abmeldedatum is null or anm.abmeldedatum >= :stichtag) and s.personId = sch.personId) and");
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

    private void createWhereSelectionsSchuelerCode() {
        if (schuelerCode != SchuelerSuchenModel.SCHUELER_CODE_ALLE) {
            selectStatementSb.append(" cod.codeId = :schuelerCodeId and");
        }
    }

    private void createWhereSelectionsKurs() {
        if (kursFuerSucheBeruecksichtigen) {
            selectStatementSb.append(" kursanm.kurs.semester.semesterId = :semesterKursId and");
            if (wochentag != Wochentag.ALLE) {
                selectStatementSb.append(" kursanm.kurs.wochentag = :wochentag and");
            }
            if (zeitBeginn != null) {
                selectStatementSb.append(" kursanm.kurs.zeitBeginn = :zeitBeginn and");
            }
            if (mitarbeiter != SchuelerSuchenModel.MITARBEITER_ALLE) {
                selectStatementSb.append(" lkr.personId = :lehrkraftPersonId and");
            }
        }
    }

    private void createWhereSelectionsMaerchen() {
        if (maerchenFuerSucheBeruecksichtigen) {
            selectStatementSb.append(" mae.maerchen.maerchenId = :maerchenId and");
            if (gruppe != Gruppe.ALLE) {
                selectStatementSb.append(" mae.gruppe = :gruppe and");
            }
            if (checkNotEmpty(rollen)) {
                selectStatementSb.append(" (");
                for (int i = 0; i < rollen.split("[,;]").length; i++) {
                    selectStatementSb.append(" lower(trim(mae.rolle1)) = :maerchenrolleEq").append(i).append(" or");
                    selectStatementSb.append(" lower(trim(mae.rolle1)) like :maerchenrolleL1").append(i).append(" or");
                    selectStatementSb.append(" lower(trim(mae.rolle1)) like :maerchenrolleL2").append(i).append(" or");
                    selectStatementSb.append(" lower(trim(mae.rolle1)) like :maerchenrolleL3").append(i).append(" or");
                    selectStatementSb.append(" lower(trim(mae.rolle1)) like :maerchenrolleL4").append(i).append(" or");
                    selectStatementSb.append(" lower(trim(mae.rolle1)) like :maerchenrolleL5").append(i).append(" or");
                    selectStatementSb.append(" lower(trim(mae.rolle2)) = :maerchenrolleEq").append(i).append(" or");
                    selectStatementSb.append(" lower(trim(mae.rolle2)) like :maerchenrolleL1").append(i).append(" or");
                    selectStatementSb.append(" lower(trim(mae.rolle2)) like :maerchenrolleL2").append(i).append(" or");
                    selectStatementSb.append(" lower(trim(mae.rolle2)) like :maerchenrolleL3").append(i).append(" or");
                    selectStatementSb.append(" lower(trim(mae.rolle2)) like :maerchenrolleL4").append(i).append(" or");
                    selectStatementSb.append(" lower(trim(mae.rolle2)) like :maerchenrolleL5").append(i).append(" or");
                    selectStatementSb.append(" lower(trim(mae.rolle3)) = :maerchenrolleEq").append(i).append(" or");
                    selectStatementSb.append(" lower(trim(mae.rolle3)) like :maerchenrolleL1").append(i).append(" or");
                    selectStatementSb.append(" lower(trim(mae.rolle3)) like :maerchenrolleL2").append(i).append(" or");
                    selectStatementSb.append(" lower(trim(mae.rolle3)) like :maerchenrolleL3").append(i).append(" or");
                    selectStatementSb.append(" lower(trim(mae.rolle3)) like :maerchenrolleL4").append(i).append(" or");
                    selectStatementSb.append(" lower(trim(mae.rolle3)) like :maerchenrolleL5").append(i).append(" or");
                }
                // letztes or löschen
                selectStatementSb.setLength(selectStatementSb.length() - 3);
                selectStatementSb.append(") and");
            }
            if (elternmithilfeCode != SchuelerSuchenModel.ELTERNMITHILFE_CODE_ALLE) {
                selectStatementSb.append(" mae.elternmithilfeCode.codeId = :elternmithilfeCodeId and");
            }
            if (kuchenVorstellung != null) {
                switch (kuchenVorstellung) {
                    case (1) -> selectStatementSb.append(" mae.kuchenVorstellung1 = true and");
                    case (2) -> selectStatementSb.append(" mae.kuchenVorstellung2 = true and");
                    case (3) -> selectStatementSb.append(" mae.kuchenVorstellung3 = true and");
                    case (4) -> selectStatementSb.append(" mae.kuchenVorstellung4 = true and");
                    case (5) -> selectStatementSb.append(" mae.kuchenVorstellung5 = true and");
                    case (6) -> selectStatementSb.append(" mae.kuchenVorstellung6 = true and");
                    case (7) -> selectStatementSb.append(" mae.kuchenVorstellung7 = true and");
                    case (8) -> selectStatementSb.append(" mae.kuchenVorstellung8 = true and");
                    case (9) -> selectStatementSb.append(" mae.kuchenVorstellung9 = true and");
                    default -> {
                    }
                }
            }
            if (checkNotEmpty(zusatzattributMaerchen)) {
                selectStatementSb.append(" mae.zusatzattribut = :zusatzattributMaerchen and");
            }
        }
    }

    private void setParameterStammdatenOhneGeburtsdatumSuchperiode() {
        if (selectStatementSb.toString().contains(":vorname")) {
            typedQuery.setParameter("vorname", person.getVorname().toLowerCase() + "%");
        }
        if (selectStatementSb.toString().contains(":nachname")) {
            typedQuery.setParameter("nachname", person.getNachname().toLowerCase() + "%");
        }
        if (selectStatementSb.toString().contains(":strasse")) {
            typedQuery.setParameter("strasse", adresse.getStrasse().toLowerCase() + "%");
        }
        if (selectStatementSb.toString().contains(":hausnummer")) {
            typedQuery.setParameter("hausnummer", adresse.getHausnummer().toLowerCase());
        }
        if (selectStatementSb.toString().contains(":plz")) {
            typedQuery.setParameter("plz", adresse.getPlz());
        }
        if (selectStatementSb.toString().contains(":ort")) {
            typedQuery.setParameter("ort", adresse.getOrt().toLowerCase() + "%");
        }
        if (selectStatementSb.toString().contains(":festnetz")) {
            typedQuery.setParameter("festnetz", person.getFestnetz().replaceAll("\\s+", ""));
        }
        if (selectStatementSb.toString().contains(":natel")) {
            typedQuery.setParameter("natel", person.getNatel().replaceAll("\\s+", ""));
        }
        if (selectStatementSb.toString().contains(":email")) {
            typedQuery.setParameter("email", person.getEmail().toLowerCase() + "%");
        }
    }

    @SuppressWarnings("DuplicatedCode")
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
                //noinspection MagicConstant
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

    private void setParameterSchuelerCodeId() {
        if (selectStatementSb.toString().contains(":schuelerCodeId")) {
            typedQuery.setParameter("schuelerCodeId", schuelerCode.getCodeId());
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private void setParameterKurs() {
        if (selectStatementSb.toString().contains(":semesterKursId")) {
            typedQuery.setParameter("semesterKursId", semesterKurs.getSemesterId());
        }
        if (selectStatementSb.toString().contains(":wochentag")) {
            typedQuery.setParameter("wochentag", wochentag);
        }
        if (selectStatementSb.toString().contains(":zeitBeginn")) {
            typedQuery.setParameter("zeitBeginn", zeitBeginn);
        }
        if (selectStatementSb.toString().contains(":lehrkraftPersonId")) {
            typedQuery.setParameter("lehrkraftPersonId", mitarbeiter.getPersonId());
        }
    }

    private void setParameterMaerchen() {
        if (selectStatementSb.toString().contains(":maerchenId")) {
            typedQuery.setParameter("maerchenId", maerchen.getMaerchenId());
        }
        if (selectStatementSb.toString().contains(":gruppe")) {
            typedQuery.setParameter("gruppe", gruppe);
        }
        if (selectStatementSb.toString().contains(":maerchenrolle")) {
            String[] rollenSplitted = rollen.split("[,;]");
            for (int i = 0; i < rollenSplitted.length; i++) {
                String rolle = rollenSplitted[i].trim().toLowerCase();
                typedQuery.setParameter("maerchenrolleEq" + i, rolle);
                // Like soll nur eigenständige Wörter finden
                typedQuery.setParameter("maerchenrolleL1" + i, rolle + " %");
                typedQuery.setParameter("maerchenrolleL2" + i, "% " + rolle);
                typedQuery.setParameter("maerchenrolleL3" + i, "% " + rolle + " %");
                typedQuery.setParameter("maerchenrolleL4" + i, rolle + ": %");
                typedQuery.setParameter("maerchenrolleL5" + i, "% " + rolle + ": %");
            }
        }
        if (selectStatementSb.toString().contains(":elternmithilfeCodeId")) {
            typedQuery.setParameter("elternmithilfeCodeId", elternmithilfeCode.getCodeId());
        }
        if (selectStatementSb.toString().contains(":zusatzattributMaerchen")) {
            typedQuery.setParameter("zusatzattributMaerchen", zusatzattributMaerchen);
        }
    }

    public List<Schueler> getSchuelerFound() {
        return schuelerFound;
    }
}
