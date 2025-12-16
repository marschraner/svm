package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import java.util.Calendar;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Martin Schraner
 */
@Setter
@Getter
public class SemestersTableData {

  private List<SemesterAndNumberOfKurse> semestersAndNumberOfKurses;

  public SemestersTableData(List<SemesterAndNumberOfKurse> semestersAndNumberOfKurses) {
    this.semestersAndNumberOfKurses = semestersAndNumberOfKurses;
  }

  private static final Field[] COLUMNS = {
    Field.SCHULJAHR,
    Field.SEMESTERBEZEICHNUNG,
    Field.SEMESTERBEGINN,
    Field.SEMESTERENDE,
    Field.FERIENBEGINN1,
    Field.FERIENENDE1,
    Field.FERIENBEGINN2,
    Field.FERIENENDE2,
    Field.ANZAHL_SCHULWOCHEN,
    Field.ANZAHL_KURSE
  };

  public int getColumnCount() {
    return COLUMNS.length;
  }

  public int size() {
    return semestersAndNumberOfKurses.size();
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    SemesterAndNumberOfKurse semesterAndNumberOfKurse = semestersAndNumberOfKurses.get(rowIndex);
    Object value = null;
    switch (COLUMNS[columnIndex]) {
      case SCHULJAHR -> value = semesterAndNumberOfKurse.semester().getSchuljahr();
      case SEMESTERBEZEICHNUNG ->
          value = semesterAndNumberOfKurse.semester().getSemesterbezeichnung();
      case SEMESTERBEGINN -> value = semesterAndNumberOfKurse.semester().getSemesterbeginn();
      case SEMESTERENDE -> value = semesterAndNumberOfKurse.semester().getSemesterende();
      case FERIENBEGINN1 -> value = semesterAndNumberOfKurse.semester().getFerienbeginn1();
      case FERIENENDE1 -> value = semesterAndNumberOfKurse.semester().getFerienende1();
      case FERIENBEGINN2 -> value = semesterAndNumberOfKurse.semester().getFerienbeginn2();
      case FERIENENDE2 -> value = semesterAndNumberOfKurse.semester().getFerienende2();
      case ANZAHL_SCHULWOCHEN -> value = semesterAndNumberOfKurse.semester().getAnzahlSchulwochen();
      case ANZAHL_KURSE -> value = semesterAndNumberOfKurse.numberOfKurse();
      default -> {
        // Nothing to do
      }
    }
    return value;
  }

  public Class<?> getColumnClass(int columnIndex) {
    return switch (COLUMNS[columnIndex]) {
      case SEMESTERBEGINN, SEMESTERENDE, FERIENBEGINN1, FERIENENDE1, FERIENBEGINN2, FERIENENDE2 ->
          Calendar.class;
      case ANZAHL_SCHULWOCHEN, ANZAHL_KURSE -> Integer.class;
      default -> String.class;
    };
  }

  public String getColumnName(int column) {
    return COLUMNS[column].toString();
  }
}
