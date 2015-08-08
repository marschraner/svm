package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CodesTableData {

    private List<SchuelerCode> schuelerCodes;

    public CodesTableData(List<SchuelerCode> schuelerCodes) {
        this.schuelerCodes = schuelerCodes;
    }

    private static final Field[] COLUMNS = {Field.KUERZEL, Field.BESCHREIBUNG};

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return schuelerCodes.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        SchuelerCode schuelerCode = schuelerCodes.get(rowIndex);
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case KUERZEL:
                value = schuelerCode.getKuerzel();
                break;
            case BESCHREIBUNG:
                value = schuelerCode.getBeschreibung();
                break;
            default:
                break;
        }
        return value;
    }

    public void setSchuelerCodes(List<SchuelerCode> schuelerCodes) {
        this.schuelerCodes = schuelerCodes;
    }

    public String getColumnName(int column) {
        return COLUMNS[column].toString();
    }

    public SchuelerCode getCodeAt(int rowIndex) {
        return schuelerCodes.get(rowIndex);
    }

}
