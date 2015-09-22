package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Gruppe;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.persistence.entities.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author Hans Stamm
 */
public class SchuelerSuchenTableData {

    private List<Schueler> schuelerList;
    private Map<Schueler, List<Kurs>> kurse;
    private final Semester semester;
    private final Wochentag wochentag;
    private final Time zeitBeginn;
    private final Mitarbeiter mitarbeiter;
    private Map<Schueler, Maercheneinteilung> maercheneinteilungen;
    private Maerchen maerchen;
    private final Gruppe gruppe;
    private final ElternmithilfeCode elternmithilfeCode;
    private boolean maerchenFuerSucheBeruecksichtigen;
    private boolean nachRollenGesucht;
    private List<Field> columns = new ArrayList<>();

    public SchuelerSuchenTableData(List<Schueler> schuelerList, Map<Schueler, List<Kurs>> kurse, Semester semester, Wochentag wochentag, Time zeitBeginn, Mitarbeiter mitarbeiter, Map<Schueler, Maercheneinteilung> maercheneinteilungen, Maerchen maerchen, Gruppe gruppe, ElternmithilfeCode elternmithilfeCode, boolean maerchenFuerSucheBeruecksichtigen, boolean nachRollenGesucht) {
        this.schuelerList = schuelerList;
        this.kurse = kurse;
        this.semester = semester;
        this.wochentag = wochentag;
        this.zeitBeginn = zeitBeginn;
        this.mitarbeiter = mitarbeiter;
        this.maercheneinteilungen = maercheneinteilungen;
        this.maerchen = maerchen;
        this.gruppe = gruppe;
        this.elternmithilfeCode = elternmithilfeCode;
        this.maerchenFuerSucheBeruecksichtigen = maerchenFuerSucheBeruecksichtigen;
        this.nachRollenGesucht = nachRollenGesucht;
        initColumns();
    }

    private void initColumns() {
        columns.add(Field.NACHNAME);
        columns.add(Field.VORNAME);
        columns.add(Field.STRASSE_HAUSNUMMER);
        columns.add(Field.PLZ);
        columns.add(Field.ORT);
        columns.add(Field.GEBURTSDATUM_SHORT);
        columns.add(Field.MUTTER);
        columns.add(Field.VATER);
        columns.add(Field.RECHNUNGSEMPFAENGER);
        columns.add(Field.KURS1);
        columns.add(Field.ANZAHL_KURSE);
        if (semester != null && semester.getSemesterbezeichnung() == Semesterbezeichnung.ERSTES_SEMESTER) {
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

    public Object getValueAt(int rowIndex, int columnIndex) {
        Schueler schueler = schuelerList.get(rowIndex);
        List<Kurs> kurseOfSchueler = kurse.get(schueler);
        Adresse schuelerAdresse = schueler.getAdresse();
        Object value = null;
        switch (columns.get(columnIndex)) {
            case NACHNAME:
                value = schueler.getNachname();
                break;
            case VORNAME:
                value = schueler.getVorname();
                break;
            case STRASSE_HAUSNUMMER:
                value = schuelerAdresse.getStrHausnummer();
               break;
            case PLZ:
                value = schuelerAdresse.getPlz();
                break;
            case ORT:
                value = schuelerAdresse.getOrt();
                break;
            case GEBURTSDATUM_SHORT:
                value = schueler.getGeburtsdatum();
                break;
            case MUTTER:
                value = getString(schueler.getMutter());
                break;
            case VATER:
                value = getString(schueler.getVater());
                break;
            case RECHNUNGSEMPFAENGER:
                String rechnungsempfaenger;
                if (schueler.getMutter() != null && schueler.getMutter().isIdenticalWith(schueler.getRechnungsempfaenger())) {
                    rechnungsempfaenger = "Mutter";
                } else if (schueler.getVater() != null && schueler.getVater().isIdenticalWith(schueler.getRechnungsempfaenger())) {
                    rechnungsempfaenger = "Vater";
                } else {
                    rechnungsempfaenger = getString(schueler.getRechnungsempfaenger());
                }
                value = rechnungsempfaenger;
                break;
            case KURS1:
                if (kurseOfSchueler != null && !kurseOfSchueler.isEmpty()) {
                    value = kurseOfSchueler.get(0).toStringShort();
                }
                break;
            case ANZAHL_KURSE:
                value = (kurseOfSchueler == null ? 0 : kurseOfSchueler.size());
                break;
            case GRUPPE:
                if (maercheneinteilungen.get(schueler) != null) {
                    value = maercheneinteilungen.get(schueler).getGruppe().toString();
                }
                break;
            default:
                break;
        }
        return value;
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (columns.get(columnIndex)) {
            case GEBURTSDATUM_SHORT:
                return Calendar.class;
            case ANZAHL_KURSE:
                return Integer.class;
            default:
                return String.class;
        }
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

    public List<Schueler> getSchuelerList() {
        return schuelerList;
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

    public void setMaercheneinteilungen(Map<Schueler, Maercheneinteilung> maercheneinteilungen) {
        this.maercheneinteilungen = maercheneinteilungen;
    }

    public Gruppe getGruppe() {
        return gruppe;
    }

    public ElternmithilfeCode getElternmithilfeCode() {
        return elternmithilfeCode;
    }

    public boolean isMaerchenFuerSucheBeruecksichtigen() {
        return maerchenFuerSucheBeruecksichtigen;
    }

    public boolean isNachRollenGesucht() {
        return nachRollenGesucht;
    }
}
