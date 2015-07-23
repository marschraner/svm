package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class KurseTableData {

    private List<Kurs> kurse;

    public KurseTableData(List<Kurs> kurse) {
        this.kurse = kurse;
    }

    private static final Field[] COLUMNS = {Field.KURSTYP_BEZEICHNUNG, Field.ALTERSBEREICH, Field.STUFE, Field.TAG, Field.ZEIT_BEGINN, Field.ZEIT_ENDE, Field.ORT, Field.LEITUNG};

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return kurse.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Kurs kurs = kurse.get(rowIndex);
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case KURSTYP_BEZEICHNUNG:
                value = kurs.getKurstyp().getBezeichnung();
                break;
            case ALTERSBEREICH:
                value = kurs.getAltersbereich();
                break;
            case STUFE:
                value = kurs.getStufe();
                break;
            case TAG:
                value = kurs.getWochentag();
                break;
            case ZEIT_BEGINN:
                value = kurs.getZeitBeginn();
                break;
            case ZEIT_ENDE:
                value = kurs.getZeitEnde();
                break;
            case ORT:
                value = kurs.getKursort().getBezeichnung();
                break;
            case LEITUNG:
                String leitung = "";
                for (Lehrkraft lehrkraft : kurs.getLehrkraefte()) {
                    if (leitung.length() > 0) {
                        leitung = leitung + "/";
                    }
                    leitung = leitung + lehrkraft.getVorname() + " " + lehrkraft.getVorname();
                }
                value = leitung;
                break;
            default:
                break;
        }
        return value;
    }

    public String getColumnName(int column) {
        return COLUMNS[column].toString();
    }

    public Kurs getKursSelected(int rowIndex) {
        return kurse.get(rowIndex);
    }

}
