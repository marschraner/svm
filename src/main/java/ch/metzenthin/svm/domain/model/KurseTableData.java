package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;

import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Martin Schraner
 */
public class KurseTableData {

    private List<Kurs> kurse;

    public KurseTableData(List<Kurs> kurse) {
        this.kurse = kurse;
    }

    private static final Field[] COLUMNS = {Field.KURSTYP_BEZEICHNUNG, Field.ALTERSBEREICH, Field.STUFE, Field.TAG, Field.ZEIT_BEGINN, Field.ZEIT_ENDE, Field.ORT, Field.LEITUNG, Field.ANZAHL_SCHUELER};

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
                value = asString(kurs.getZeitBeginn());
                break;
            case ZEIT_ENDE:
                value = asString(kurs.getZeitEnde());
                break;
            case ORT:
                value = kurs.getKursort().getBezeichnung();
                break;
            case LEITUNG:
                String leitung = "";
                for (Lehrkraft lehrkraft : kurs.getLehrkraefte()) {
                    if (leitung.length() > 0) {
                        leitung = leitung + " / ";
                    }
                    leitung = leitung + lehrkraft.getVorname() + " " + lehrkraft.getNachname();
                }
                value = leitung;
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

    public List<Kurs> getKurse() {
        return kurse;
    }

    public Kurs getKursSelected(int rowIndex) {
        return kurse.get(rowIndex);
    }

    public int getAnzahlSchueler() {
        int anzahlSchueler = 0;
        //TODO
//        for (Kurs kurs : kurse) {
//            anzahlSchueler += kurs.getSchueler().size();
//        }
        return anzahlSchueler;
    }
}
