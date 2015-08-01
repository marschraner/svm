package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.*;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author Hans Stamm
 */
public class SchuelerSuchenTableData {

    private List<Schueler> schuelerList;
    private Map<Schueler, List<Kurs>> kurseMap;
    private Semester semester;

    public SchuelerSuchenTableData(List<Schueler> schuelerList, Map<Schueler, List<Kurs>> kurseMap, Semester semester) {
        this.schuelerList = schuelerList;
        this.kurseMap = kurseMap;
        this.semester = semester;
    }

    private static final Field[] COLUMNS = {Field.NACHNAME, Field.VORNAME, Field.STRASSE_HAUSNUMMER, Field.PLZ, Field.ORT, Field.FESTNETZ, Field.NATEL, Field.EMAIL, Field.GEBURTSDATUM, Field.MUTTER, Field.VATER, Field.RECHNUNGSEMPFAENGER, Field.KURS1, Field.ANZAHL_KURSE};

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return schuelerList.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Schueler schueler = schuelerList.get(rowIndex);
        List<Kurs> kurseOfSchueler = kurseMap.get(schueler);
        Adresse schuelerAdresse = schueler.getAdresse();
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case NACHNAME:
                value = schueler.getNachname();
                break;
            case VORNAME:
                value = schueler.getVorname();
                break;
            case STRASSE_HAUSNUMMER:
                value = schuelerAdresse.getStrasseHausnummer();
               break;
            case PLZ:
                value = schuelerAdresse.getPlz();
                break;
            case ORT:
                value = schuelerAdresse.getOrt();
                break;
            case FESTNETZ:
                value = schueler.getFestnetz();
                break;
            case NATEL:
                value = schueler.getNatel();
                break;
            case EMAIL:
                value = schueler.getEmail();
                break;
            case GEBURTSDATUM:
                value = schueler.getGeburtsdatum();
                break;
            case MUTTER:
                value = getString(schueler.getMutter());
                break;
            case VATER:
                value = getString(schueler.getVater());
                break;
            case RECHNUNGSEMPFAENGER:
                value = getString(schueler.getRechnungsempfaenger());
                break;
            case KURS1:
                if (kurseOfSchueler != null && !kurseOfSchueler.isEmpty()) {
                    value = kurseOfSchueler.get(0).toStringShort();
                }
                break;
            case ANZAHL_KURSE:
                value = (kurseOfSchueler == null ? 0 : kurseOfSchueler.size());
                break;
            default:
                break;
        }
        return value;
    }

    private String getString(Angehoeriger angehoeriger) {
        String value = null;
        if (angehoeriger != null) {
            value = angehoeriger.getVorname() + " " + angehoeriger.getNachname();
        }
        return value;
    }

    public String getColumnName(int column) {
        return COLUMNS[column].toString();
    }

    public Class<?> getColumnClass(int column) {
        if (COLUMNS[column] == Field.GEBURTSDATUM) {
            return Calendar.class;
        } else {
            return String.class;
        }
    }

    public SchuelerDatenblattModel getSchuelerDatenblattModel(int rowIndex) {
        return new SchuelerDatenblattModelImpl(schuelerList.get(rowIndex));
    }

    public int getAnzahlLektionen() {
        int anzahlKurse = 0;
        for (Schueler schueler : schuelerList) {
            anzahlKurse += kurseMap.get(schueler).size();
        }
        return anzahlKurse;
    }

    public Semester getSemester() {
        return semester;
    }

    public List<Schueler> getSchuelerList() {
        return schuelerList;
    }

    public void setKurseMap(Map<Schueler, List<Kurs>> kurseMap) {
        this.kurseMap = kurseMap;
    }

}
