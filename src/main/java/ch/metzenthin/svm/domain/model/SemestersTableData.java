package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.Calendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SemestersTableData {

    private List<Semester> semesters;

    public SemestersTableData(List<Semester> semesters) {
        this.semesters = semesters;
    }

    private static final Field[] COLUMNS = {Field.SCHULJAHR, Field.SEMESTERBEZEICHNUNG, Field.SEMESTERBEGINN, Field.SEMESTERENDE, Field.FERIENBEGINN1, Field.FERIENENDE1, Field.FERIENBEGINN2, Field.FERIENENDE2, Field.ANZAHL_SCHULWOCHEN, Field.ANZAHL_KURSE};

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
            case SCHULJAHR -> value = semester.getSchuljahr();
            case SEMESTERBEZEICHNUNG -> value = semester.getSemesterbezeichnung();
            case SEMESTERBEGINN -> value = semester.getSemesterbeginn();
            case SEMESTERENDE -> value = semester.getSemesterende();
            case FERIENBEGINN1 -> value = semester.getFerienbeginn1();
            case FERIENENDE1 -> value = semester.getFerienende1();
            case FERIENBEGINN2 -> value = semester.getFerienbeginn2();
            case FERIENENDE2 -> value = semester.getFerienende2();
            case ANZAHL_SCHULWOCHEN -> value = semester.getAnzahlSchulwochen();
            case ANZAHL_KURSE -> value = semester.getKurse().size();
            default -> {
            }
        }
        return value;
    }

    public Class<?> getColumnClass(int columnIndex) {
        return switch (COLUMNS[columnIndex]) {
            case SEMESTERBEGINN,
                    SEMESTERENDE,
                    FERIENBEGINN1,
                    FERIENENDE1,
                    FERIENBEGINN2,
                    FERIENENDE2 -> Calendar.class;
            case ANZAHL_SCHULWOCHEN,
                    ANZAHL_KURSE -> Integer.class;
            default -> String.class;
        };
    }

    public String getColumnName(int column) {
        return COLUMNS[column].toString();
    }

    public List<Semester> getSemesters() {
        return semesters;
    }

    public void setSemesters(List<Semester> semesters) {
        this.semesters = semesters;
    }
}
