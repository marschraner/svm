package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Semester;

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

    private static final Field[] COLUMNS = {Field.SCHULJAHR, Field.SEMESTERBEZEICHNUNG, Field.SEMESTERBEGINN, Field.SEMESTERENDE, Field.ANZAHL_SCHULWOCHEN};

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
                value = asString(semester.getSemesterbeginn());
                break;
            case SEMESTERENDE:
                value = asString(semester.getSemesterende());
                break;
            case ANZAHL_SCHULWOCHEN:
                value = semester.getAnzahlSchulwochen();
                break;
            default:
                break;
        }
        return value;
    }

    public String getColumnName(int column) {
        return COLUMNS[column].toString();
    }

}
