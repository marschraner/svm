package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Dispensation;

import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Hans Stamm
 */
public class DispensationenTableData {

    private List<Dispensation> dispensationen;

    public DispensationenTableData(List<Dispensation> dispensationen) {
        this.dispensationen = dispensationen;
    }

    private static final String[] COLUMNS = {"Dispensationsbeginn", "Dispensationsende", "voraussichtliche Dauer", "Grund"};

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return dispensationen.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Dispensation dispensation = dispensationen.get(rowIndex);
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case "Dispensationsbeginn" :
                value = asString(dispensation.getDispensationsbeginn());
                break;
            case "Dispensationsende" :
                value = asString(dispensation.getDispensationsende());
                break;
            case "voraussichtliche Dauer" :
                value = dispensation.getVoraussichtlicheDauer();
                break;
            case "Grund" :
                value = dispensation.getGrund();
                break;
            default:
                break;
        }
        return value;
    }

    public String getColumnName(int column) {
        return COLUMNS[column];
    }

}
