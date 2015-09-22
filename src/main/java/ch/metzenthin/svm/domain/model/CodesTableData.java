package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Code;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class CodesTableData {

    private List<? extends Code> codes;
    private boolean isCodesSpecific;
    private List<Field> columns = new ArrayList<>();

    public CodesTableData(List<? extends Code> codes, boolean isCodesSpecific) {
        this.codes = codes;
        this.isCodesSpecific = isCodesSpecific;
        initColumns();
    }

    private void initColumns() {
        columns.add(Field.KUERZEL);
        columns.add(Field.BESCHREIBUNG);
        if (!isCodesSpecific) {
            columns.add(Field.SELEKTIERBAR);
        }
    }

    public int getColumnCount() {
        return columns.size();
    }

    public int size() {
        return codes.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Code code = codes.get(rowIndex);
        Object value = null;
        switch (columns.get(columnIndex)) {
            case KUERZEL:
                value = code.getKuerzel();
                break;
            case BESCHREIBUNG:
                value = code.getBeschreibung();
                break;
            case SELEKTIERBAR:
                value = (code.getSelektierbar() ? "ja" : "nein");
                break;
            default:
                break;
        }
        return value;
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (columns.get(columnIndex)) {
            default:
                return String.class;
        }
    }

    public void setCodes(List<? extends Code> codes) {
        this.codes = codes;
    }

    public String getColumnName(int column) {
        return columns.get(column).toString();
    }

    public Code getCodeAt(int rowIndex) {
        return codes.get(rowIndex);
    }

}
