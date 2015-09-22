package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.Calendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Martin Schraner
 */
public class SemestersTableData {

    private List<Semester> semesters;

    public SemestersTableData(List<Semester> semesters) {
        this.semesters = semesters;
    }

    private static final Field[] COLUMNS = {Field.SCHULJAHR, Field.SEMESTERBEZEICHNUNG, Field.SEMESTERBEGINN, Field.SEMESTERENDE, Field.ANZAHL_SCHULWOCHEN, Field.ANZAHL_KURSE};

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return semesters.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Semester semester = semesters.get(rowIndex);
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case SCHULJAHR:
                value = semester.getSchuljahr();
                break;
            case SEMESTERBEZEICHNUNG:
                value = semester.getSemesterbezeichnung();
                break;
            case SEMESTERBEGINN:
                value = semester.getSemesterbeginn();
                break;
            case SEMESTERENDE:
                value = semester.getSemesterende();
                break;
            case ANZAHL_SCHULWOCHEN:
                value = semester.getAnzahlSchulwochen();
                break;
            case ANZAHL_KURSE:
                value = semester.getKurse().size();
                break;
            default:
                break;
        }
        return value;
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (COLUMNS[columnIndex]) {
            case SEMESTERBEGINN:
                return Calendar.class;
            case SEMESTERENDE:
                return Calendar.class;
            case ANZAHL_SCHULWOCHEN:
                return Integer.class;
            case ANZAHL_KURSE:
                return Integer.class;
            default:
                return String.class;
        }
    }

    public String getColumnName(int column) {
        return COLUMNS[column].toString();
    }

    public void setSemesters(List<Semester> semesters) {
        this.semesters = semesters;
    }
}
