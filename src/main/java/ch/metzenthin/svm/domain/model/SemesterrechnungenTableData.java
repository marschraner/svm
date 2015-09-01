package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SemesterrechnungenTableData {

    private List<Semesterrechnung> semesterrechnungen;

    public SemesterrechnungenTableData(List<Semesterrechnung> semesterrechnungen) {
        this.semesterrechnungen = semesterrechnungen;
    }

    private static final Field[] COLUMNS = {Field.RECHNUNGSEMPFAENGER, Field.RECHNUNGSDATUM_VORRECHNUNG, Field.ERMAESSIGUNG_VORRECHNUNG,
            Field.ZUSCHLAG_VORRECHNUNG, Field.ANZAHL_WOCHEN_VORRECHNUNG, Field.WOCHENBETRAG_VORRECHNUNG, Field.SCHULGELD_VORRECHNUNG,
            Field.RECHNUNGSDATUM_NACHRECHNUNG, Field.ERMAESSIGUNG_NACHRECHNUNG,
            Field.ZUSCHLAG_NACHRECHNUNG, Field.ANZAHL_WOCHEN_NACHRECHNUNG, Field.WOCHENBETRAG_NACHRECHNUNG, Field.SCHULGELD_NACHRECHNUNG, Field.SCHULGELD_DIFFERENZ_NACHRECHNUNG_VORRECHNUNG, Field.RESTBETRAG};

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return semesterrechnungen.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Semesterrechnung semesterrechnung = semesterrechnungen.get(rowIndex);
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case RECHNUNGSEMPFAENGER:
                value = semesterrechnung.getRechnungsempfaenger().getNachname() + " " + semesterrechnung.getRechnungsempfaenger().getVorname();
                break;
            case RECHNUNGSDATUM_VORRECHNUNG:
                value = semesterrechnung.getRechnungsdatumVorrechnung();
                break;
            case ERMAESSIGUNG_VORRECHNUNG:
                value = semesterrechnung.getErmaessigungVorrechnung();
                break;
            case ZUSCHLAG_VORRECHNUNG:
                value = semesterrechnung.getZuschlagVorrechnung();
                break;
            case ANZAHL_WOCHEN_VORRECHNUNG:
                value = semesterrechnung.getAnzahlWochenVorrechnung();
                break;
            case WOCHENBETRAG_VORRECHNUNG:
                value = semesterrechnung.getWochenbetragVorrechnung();
                break;
            case SCHULGELD_VORRECHNUNG:
                value = semesterrechnung.getSchulgeldVorrechnung();
                break;
            case RECHNUNGSDATUM_NACHRECHNUNG:
                value = semesterrechnung.getRechnungsdatumNachrechnung();
                break;
            case ERMAESSIGUNG_NACHRECHNUNG:
                value = semesterrechnung.getErmaessigungNachrechnung();
                break;
            case ZUSCHLAG_NACHRECHNUNG:
                value = semesterrechnung.getZuschlagNachrechnung();
                break;
            case ANZAHL_WOCHEN_NACHRECHNUNG:
                value = semesterrechnung.getAnzahlWochenNachrechnung();
                break;
            case WOCHENBETRAG_NACHRECHNUNG:
                value = semesterrechnung.getWochenbetragNachrechnung();
                break;
            case SCHULGELD_NACHRECHNUNG:
                value = semesterrechnung.getSchulgeldNachrechnung();
                break;
            case SCHULGELD_DIFFERENZ_NACHRECHNUNG_VORRECHNUNG:
                value = semesterrechnung.getSchulgeldDifferenzNachrechnungVorrechnung();
                break;
            case RESTBETRAG:
                value = semesterrechnung.getRestbetrag();
                break;
            default:
                break;
        }
        return value;
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (COLUMNS[columnIndex]) {
            case RECHNUNGSDATUM_VORRECHNUNG:
                return Calendar.class;
            case ERMAESSIGUNG_VORRECHNUNG:
                return BigDecimal.class;
            case ZUSCHLAG_VORRECHNUNG:
                return BigDecimal.class;
            case ANZAHL_WOCHEN_VORRECHNUNG:
                return Integer.class;
            case WOCHENBETRAG_VORRECHNUNG:
                return BigDecimal.class;
            case SCHULGELD_VORRECHNUNG:
                return BigDecimal.class;
            case RECHNUNGSDATUM_NACHRECHNUNG:
                return Calendar.class;
            case ERMAESSIGUNG_NACHRECHNUNG:
                return BigDecimal.class;
            case ZUSCHLAG_NACHRECHNUNG:
                return BigDecimal.class;
            case ANZAHL_WOCHEN_NACHRECHNUNG:
                return Integer.class;
            case WOCHENBETRAG_NACHRECHNUNG:
                return BigDecimal.class;
            case SCHULGELD_NACHRECHNUNG:
                return BigDecimal.class;
            case SCHULGELD_DIFFERENZ_NACHRECHNUNG_VORRECHNUNG:
                return BigDecimal.class;
            case RESTBETRAG:
                return BigDecimal.class;
            default:
                return String.class;
        }
    }

    public String getColumnName(int column) {
        return COLUMNS[column].toString();
    }

    public Semester getSememester() {
        if (semesterrechnungen.isEmpty()) {
            return null;
        }
        // Alle Semesterrechnungen in der Liste haben dasselbe Semester
        return semesterrechnungen.get(0).getSemester();
    }

}
