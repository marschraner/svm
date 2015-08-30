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

    private static final Field[] COLUMNS = {Field.RECHNUNGSEMPFAENGER, Field.RECHNUNGSDATUM, Field.WOCHENBETRAG, Field.SCHULGELD, Field.RESTBETRAG};

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
            case RECHNUNGSDATUM:
                value = semesterrechnung.getRechnungsdatum();
                break;
            case WOCHENBETRAG:
                value = semesterrechnung.getWochenbetrag();
                break;
            case SCHULGELD:
                value = semesterrechnung.getSchulgeld();
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
            case RECHNUNGSDATUM:
                return Calendar.class;
            case WOCHENBETRAG:
                return BigDecimal.class;
            case SCHULGELD:
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
