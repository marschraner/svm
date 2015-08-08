package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Maerchen;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class MaerchensTableData {

    private List<Maerchen> maerchens;

    public MaerchensTableData(List<Maerchen> maerchens) {
        this.maerchens = maerchens;
    }

    private static final Field[] COLUMNS = {Field.SCHULJAHR, Field.BEZEICHNUNG, Field.ANZAHL_SCHUELER};

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return maerchens.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Maerchen maerchen = maerchens.get(rowIndex);
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case SCHULJAHR:
                value = maerchen.getSchuljahr();
                break;
            case BEZEICHNUNG:
                value = maerchen.getBezeichnung();
                break;
            case ANZAHL_SCHUELER:
                value = 0;  //TODO
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
