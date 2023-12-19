package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Elternmithilfe;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class MaercheneinteilungenTableData {

    private static final Field[] COLUMNS = {
            Field.MAERCHEN,
            Field.GRUPPE,
            Field.ROLLE1,
            Field.BILDER_ROLLE1,
            Field.ROLLE2,
            Field.BILDER_ROLLE2,
            Field.ROLLE3,
            Field.BILDER_ROLLE3,
            Field.ELTERNMITHILFE,
            Field.ELTERNMITHILFE_CODE,
            Field.KUCHEN_VORSTELLUNGEN,
            Field.ZUSATZATTRIBUT_MAERCHEN,
            Field.BEMERKUNGEN};

    private List<Maercheneinteilung> maercheneinteilungen;

    public MaercheneinteilungenTableData(List<Maercheneinteilung> maercheneinteilungen) {
        this.maercheneinteilungen = maercheneinteilungen;
    }

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
            case MAERCHEN -> value = maercheneinteilung.getMaerchen();
            case GRUPPE -> value = maercheneinteilung.getGruppe();
            case ROLLE1 -> value = maercheneinteilung.getRolle1();
            case BILDER_ROLLE1 -> value = maercheneinteilung.getBilderRolle1();
            case ROLLE2 -> value = maercheneinteilung.getRolle2();
            case BILDER_ROLLE2 -> value = maercheneinteilung.getBilderRolle2();
            case ROLLE3 -> value = maercheneinteilung.getRolle3();
            case BILDER_ROLLE3 -> value = maercheneinteilung.getBilderRolle3();
            case ELTERNMITHILFE -> {
                if (maercheneinteilung.getElternmithilfe() == Elternmithilfe.DRITTPERSON) {
                    value = maercheneinteilung.getElternmithilfeDrittperson().toString();
                } else {
                    value = maercheneinteilung.getElternmithilfe();
                }
            }
            case ELTERNMITHILFE_CODE -> value = maercheneinteilung.getElternmithilfeCode();
            case KUCHEN_VORSTELLUNGEN -> value = maercheneinteilung.getKuchenVorstellungenAsString();
            case ZUSATZATTRIBUT_MAERCHEN -> value = maercheneinteilung.getZusatzattribut();
            case BEMERKUNGEN -> value = maercheneinteilung.getBemerkungen();
            default -> {
            }
        }
        return value;
    }

    public Class<?> getColumnClass() {
        return String.class;
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
