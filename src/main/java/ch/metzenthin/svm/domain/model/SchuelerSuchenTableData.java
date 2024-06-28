package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Gruppe;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.common.utils.StringNumber;
import ch.metzenthin.svm.persistence.entities.*;

import java.sql.Time;
import java.util.*;

/**
 * @author Hans Stamm
 */
public class SchuelerSuchenTableData {

    private final List<Schueler> schuelerList;
    private Map<Schueler, List<Kurs>> kurse;
    private final Semester semester;
    private final Wochentag wochentag;
    private final Time zeitBeginn;
    private final Mitarbeiter mitarbeiter;
    private final Calendar anmeldemonat;
    private final Calendar abmeldemonat;
    private Map<Schueler, Maercheneinteilung> maercheneinteilungen;
    private final Maerchen maerchen;
    private final Gruppe gruppe;
    private final ElternmithilfeCode elternmithilfeCode;
    private final boolean kursFuerSucheBeruecksichtigen;
    private final boolean maerchenFuerSucheBeruecksichtigen;
    private final boolean nachRollenGesucht;
    private final Calendar stichtag;
    private final boolean keineAbgemeldetenKurseAnzeigen;
    private final List<Field> columns = new ArrayList<>();

    public SchuelerSuchenTableData(List<Schueler> schuelerList, Map<Schueler, List<Kurs>> kurse, Semester semester, Wochentag wochentag, Time zeitBeginn, Mitarbeiter mitarbeiter, Calendar anmeldemonat, Calendar abmeldemonat, Map<Schueler, Maercheneinteilung> maercheneinteilungen, Maerchen maerchen, Gruppe gruppe, ElternmithilfeCode elternmithilfeCode, boolean kursFuerSucheBeruecksichtigen, boolean maerchenFuerSucheBeruecksichtigen, boolean nachRollenGesucht, Calendar stichtag, boolean keineAbgemeldetenKurseAnzeigen) {
        this.schuelerList = schuelerList;
        this.kurse = kurse;
        this.semester = semester;
        this.wochentag = wochentag;
        this.zeitBeginn = zeitBeginn;
        this.mitarbeiter = mitarbeiter;
        this.anmeldemonat = anmeldemonat;
        this.abmeldemonat = abmeldemonat;
        this.maercheneinteilungen = maercheneinteilungen;
        this.maerchen = maerchen;
        this.gruppe = gruppe;
        this.elternmithilfeCode = elternmithilfeCode;
        this.kursFuerSucheBeruecksichtigen = kursFuerSucheBeruecksichtigen;
        this.maerchenFuerSucheBeruecksichtigen = maerchenFuerSucheBeruecksichtigen;
        this.nachRollenGesucht = nachRollenGesucht;
        this.stichtag = stichtag;
        this.keineAbgemeldetenKurseAnzeigen = keineAbgemeldetenKurseAnzeigen;
        initColumns();
    }

    private void initColumns() {
        columns.add(Field.SELEKTIERT);
        columns.add(Field.NACHNAME);
        columns.add(Field.VORNAME);
        columns.add(Field.STRASSE_HAUSNUMMER);
        columns.add(Field.PLZ);
        columns.add(Field.ORT);
        columns.add(Field.GEBURTSDATUM_SHORT);
        columns.add(Field.MUTTER);
        columns.add(Field.VATER);
        columns.add(Field.RECHNUNGSEMPFAENGER);
        if (maerchenFuerSucheBeruecksichtigen && !kursFuerSucheBeruecksichtigen) {
            columns.add(Field.ROLLE1);
        } else {
            columns.add(Field.KURS1);
            columns.add(Field.ANZAHL_KURSE);
        }
        if ((semester != null && semester.getSemesterbezeichnung() == Semesterbezeichnung.ERSTES_SEMESTER) || (maerchenFuerSucheBeruecksichtigen && !kursFuerSucheBeruecksichtigen)) {
            // Märchen nur im 1. Semester anzeigen
            columns.add(Field.GRUPPE);
        }
    }

    public int getColumnCount() {
        return columns.size();
    }

    public int size() {
        return schuelerList.size();
    }

    public int getAnzSelektiert() {
        int anzSelektiert = 0;
        for (Schueler schueler : schuelerList) {
            if (schueler.isSelektiert()) {
                anzSelektiert++;
            }
        }
        return anzSelektiert;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Schueler schueler = schuelerList.get(rowIndex);
        List<Kurs> kurseOfSchueler = kurse.get(schueler);
        Adresse schuelerAdresse = schueler.getAdresse();
        Object value = null;
        switch (columns.get(columnIndex)) {
            case SELEKTIERT -> value = schueler.isSelektiert();
            case NACHNAME -> value = schueler.getNachname();
            case VORNAME -> value = schueler.getVorname();
            case STRASSE_HAUSNUMMER -> value = schuelerAdresse.getStrHausnummer();
            case PLZ -> value = schuelerAdresse.getPlz();
            case ORT -> value = schuelerAdresse.getOrt();
            case GEBURTSDATUM_SHORT -> value = schueler.getGeburtsdatum();
            case MUTTER -> value = getString(schueler.getMutter());
            case VATER -> value = getString(schueler.getVater());
            case RECHNUNGSEMPFAENGER -> {
                String rechnungsempfaenger;
                if (schueler.getMutter() != null && schueler.getMutter().isIdenticalWith(schueler.getRechnungsempfaenger())) {
                    rechnungsempfaenger = "Mutter";
                } else if (schueler.getVater() != null && schueler.getVater().isIdenticalWith(schueler.getRechnungsempfaenger())) {
                    rechnungsempfaenger = "Vater";
                } else {
                    rechnungsempfaenger = getString(schueler.getRechnungsempfaenger());
                }
                value = rechnungsempfaenger;
            }
            case KURS1 -> {
                if (kurseOfSchueler != null && !kurseOfSchueler.isEmpty()) {
                    value = kurseOfSchueler.get(0).toStringShort();
                }
            }
            case ANZAHL_KURSE -> value = (kurseOfSchueler == null ? 0 : kurseOfSchueler.size());
            case ROLLE1 -> {
                if (maercheneinteilungen.get(schueler) != null) {
                    // Korrekte Sortierung
                    value = new StringNumber(maercheneinteilungen.get(schueler).getRolle1());
                }
            }
            case GRUPPE -> {
                if (maercheneinteilungen.get(schueler) != null) {
                    value = maercheneinteilungen.get(schueler).getGruppe().toString();
                }
            }
            default -> {
            }
        }
        return value;
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (Objects.requireNonNull(columns.get(columnIndex)) == Field.SELEKTIERT) {
            Schueler schueler = schuelerList.get(rowIndex);
            schueler.setSelektiert((boolean) value);
            schuelerList.set(rowIndex, schueler);
        }
    }

    public boolean isCellEditable(int columnIndex) {
        return Objects.requireNonNull(columns.get(columnIndex)) == Field.SELEKTIERT;
    }

    public Class<?> getColumnClass(int columnIndex) {
        return switch (columns.get(columnIndex)) {
            case GEBURTSDATUM_SHORT -> Calendar.class;
            case ANZAHL_KURSE -> Integer.class;
            case SELEKTIERT -> Boolean.class;
            case ROLLE1 -> StringNumber.class;
            default -> String.class;
        };
    }

    private String getString(Angehoeriger angehoeriger) {
        String value = null;
        if (angehoeriger != null) {
            value = angehoeriger.getNachname() + " " + angehoeriger.getVorname();
        }
        return value;
    }

    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).toString();
    }

    public SchuelerDatenblattModel getSchuelerDatenblattModel(int rowIndex) {
        return new SchuelerDatenblattModelImpl(schuelerList.get(rowIndex));
    }

    public int getAnzahlLektionen() {
        int anzahlKurse = 0;
        for (Schueler schueler : schuelerList) {
            if (kurse.get(schueler) != null) {
                anzahlKurse += kurse.get(schueler).size();
            }
        }
        return anzahlKurse;
    }

    public int getAnzahlMaercheneinteilungen() {
        int anzahlMaercheneinteilungen = 0;
        for (Schueler schueler : schuelerList) {
            if (maercheneinteilungen.get(schueler) != null) {
                anzahlMaercheneinteilungen++;
            }
        }
        return anzahlMaercheneinteilungen;
    }

    public Semester getSemester() {
        return semester;
    }

    public Wochentag getWochentag() {
        return wochentag;
    }

    public Time getZeitBeginn() {
        return zeitBeginn;
    }

    public Mitarbeiter getMitarbeiter() {
        return mitarbeiter;
    }

    public Calendar getAnmeldemonat() {
        return anmeldemonat;
    }

    public Calendar getAbmeldemonat() {
        return abmeldemonat;
    }

    public List<Schueler> getSchuelerList() {
        return schuelerList;
    }

    public List<Schueler> getSelektierteSchuelerList() {
        List<Schueler> selektierteSchuelerList = new ArrayList<>();
        for (Schueler schueler : schuelerList) {
            if (schueler.isSelektiert()) {
                selektierteSchuelerList.add(schueler);
            }
        }
        return selektierteSchuelerList;
    }

    public boolean isAlleSelektiert() {
        for (Schueler schueler : schuelerList) {
            if (!schueler.isSelektiert()) {
                return false;
            }
        }
        return true;
    }

    public void alleSchuelerSelektieren() {
        for (Schueler schueler : schuelerList) {
            schueler.setSelektiert(true);
        }
    }

    public void alleSchuelerDeselektieren() {
        for (Schueler schueler : schuelerList) {
            schueler.setSelektiert(false);
        }
    }

    public void setKurse(Map<Schueler, List<Kurs>> kurse) {
        this.kurse = kurse;
    }

    public Maerchen getMaerchen() {
        return maerchen;
    }

    public Map<Schueler, List<Kurs>> getKurse() {
        return kurse;
    }

    public Map<Schueler, Maercheneinteilung> getMaercheneinteilungen() {
        return maercheneinteilungen;
    }

    public int getAnzZuExportierendeMaercheneinteilungen() {
        int anzZuExportierendeMaercheneinteilungen = 0;
        for (Schueler schueler : maercheneinteilungen.keySet()) {
            if (schueler.isSelektiert() && maercheneinteilungen.get(schueler) != null) {
                anzZuExportierendeMaercheneinteilungen++;
            }
        }
        return anzZuExportierendeMaercheneinteilungen;
    }

    public void setMaercheneinteilungen(Map<Schueler, Maercheneinteilung> maercheneinteilungen) {
        this.maercheneinteilungen = maercheneinteilungen;
    }

    public Gruppe getGruppe() {
        return gruppe;
    }

    public ElternmithilfeCode getElternmithilfeCode() {
        return elternmithilfeCode;
    }

    public boolean isKursFuerSucheBeruecksichtigen() {
        return kursFuerSucheBeruecksichtigen;
    }

    public boolean isMaerchenFuerSucheBeruecksichtigen() {
        return maerchenFuerSucheBeruecksichtigen;
    }

    public boolean isNachRollenGesucht() {
        return nachRollenGesucht;
    }

    public Calendar getStichtag() {
        return stichtag;
    }

    public boolean isKeineAbgemeldetenKurseAnzeigen() {
        return keineAbgemeldetenKurseAnzeigen;
    }
}
