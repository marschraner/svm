package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class MaercheneinteilungenTableData {

    private List<Maercheneinteilung> maercheneinteilungen;

    public MaercheneinteilungenTableData(List<Maercheneinteilung> maercheneinteilungen) {
        this.maercheneinteilungen = maercheneinteilungen;
    }

    private static final Field[] COLUMNS = {Field.MAERCHEN, Field.GRUPPE, Field.ROLLE1, Field.BILDER_ROLLE1, Field.ROLLE2,
            Field.BILDER_ROLLE2, Field.ROLLE3, Field.BILDER_ROLLE3, Field.ELTERNMITHILFE, Field.ELTERNMITHILFE_CODE,
            Field.KUCHEN_VORSTELLUNGEN, Field.ZUSATZATTRIBUT, Field.BEMERKUNGEN};

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return maercheneinteilungen.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Maercheneinteilung maercheneinteilung = maercheneinteilungen.get(rowIndex);
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case MAERCHEN:
                value = maercheneinteilung.getMaerchen();
                break;
            case GRUPPE:
                value = maercheneinteilung.getGruppe();
                break;
            case ROLLE1:
                value = maercheneinteilung.getRolle1();
                break;
            case BILDER_ROLLE1:
                value = maercheneinteilung.getBilderRolle1();
                break;
            case ROLLE2:
                value = maercheneinteilung.getRolle2();
                break;
            case BILDER_ROLLE2:
                value = maercheneinteilung.getBilderRolle2();
                break;
            case ROLLE3:
                value = maercheneinteilung.getRolle3();
                break;
            case BILDER_ROLLE3:
                value = maercheneinteilung.getBilderRolle3();
                break;
            case ELTERNMITHILFE:
                value = maercheneinteilung.getElternmithilfe();
                break;
            case ELTERNMITHILFE_CODE:
                value = maercheneinteilung.getElternmithilfeCode();
                break;
            case KUCHEN_VORSTELLUNGEN:
                value = maercheneinteilung.getKuchenVorstellungenAsString();
                break;
            case ZUSATZATTRIBUT:
                value = maercheneinteilung.getZusatzattribut();
                break;
            case BEMERKUNGEN:
                value = maercheneinteilung.getBemerkungen();
                break;
            default:
                break;
        }
        return value;
    }

    public String getColumnName(int column) {
        return COLUMNS[column].toString();
    }

    public Maercheneinteilung getMaercheneinteilungSelected(int rowIndex) {
        return maercheneinteilungen.get(rowIndex);
    }

    public List<Maercheneinteilung> getMaercheneinteilungen() {
        return maercheneinteilungen;
    }

    public void setMaercheneinteilungen(List<Maercheneinteilung> maercheneinteilungen) {
        this.maercheneinteilungen = maercheneinteilungen;
    }
}
