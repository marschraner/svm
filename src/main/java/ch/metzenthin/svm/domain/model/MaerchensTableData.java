package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.persistence.entities.Maerchen;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class MaerchensTableData {

    private static final Field[] COLUMNS = {
            Field.SCHULJAHR,
            Field.BEZEICHNUNG,
            Field.ANZAHL_VORSTELLUNGEN,
            Field.ANZAHL_KINDER};

    private List<Maerchen> maerchens;

    public MaerchensTableData(List<Maerchen> maerchens) {
        this.maerchens = maerchens;
    }

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
            case SCHULJAHR -> value = maerchen.getSchuljahr();
            case BEZEICHNUNG -> value = maerchen.getBezeichnung();
            case ANZAHL_VORSTELLUNGEN -> value = maerchen.getAnzahlVorstellungen();
            case ANZAHL_KINDER -> value = maerchen.getMaercheneinteilungen().size();
            default -> {
                // Nothing to do
            }
        }
        return value;
    }

    public Class<?> getColumnClass(int columnIndex) {
        return switch (COLUMNS[columnIndex]) {
            case ANZAHL_VORSTELLUNGEN, ANZAHL_KINDER -> Integer.class;
            default -> String.class;
        };
    }

    public String getColumnName(int column) {
        return COLUMNS[column].toString();
    }

    public void setMaerchens(List<Maerchen> maerchens) {
        this.maerchens = maerchens;
    }
}
