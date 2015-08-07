package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Code;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CodesTableData {

    private List<Code> codes;

    public CodesTableData(List<Code> codes) {
        this.codes = codes;
    }

    private static final Field[] COLUMNS = {Field.KUERZEL, Field.BESCHREIBUNG};

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return codes.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Code code = codes.get(rowIndex);
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case KUERZEL:
                value = code.getKuerzel();
                break;
            case BESCHREIBUNG:
                value = code.getBeschreibung();
                break;
            default:
                break;
        }
        return value;
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
    }

    public String getColumnName(int column) {
        return COLUMNS[column].toString();
    }

    public Code getCodeAt(int rowIndex) {
        return codes.get(rowIndex);
    }

}
