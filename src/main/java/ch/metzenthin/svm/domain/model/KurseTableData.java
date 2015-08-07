package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;

import java.util.ArrayList;
import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Martin Schraner
 */
public class KurseTableData {

    private List<Kurs> kurse;
    private List<Field> columns = new ArrayList<>();

    public KurseTableData(List<Kurs> kurse, boolean isKurseSchueler) {
        this.kurse = kurse;
        if (isKurseSchueler) {
            columns.add(Field.SCHULJAHR);
            columns.add(Field.SEMESTERBEZEICHNUNG);
        }
        columns.add(Field.KURSTYP_BEZEICHNUNG);
        columns.add(Field.ALTERSBEREICH);
        columns.add(Field.STUFE);
        columns.add(Field.TAG);
        columns.add(Field.ZEIT_BEGINN);
        columns.add(Field.ZEIT_ENDE);
        columns.add(Field.ORT);
        columns.add(Field.LEITUNG);
        columns.add(Field.BEMERKUNGEN);
        if (!isKurseSchueler) {
            columns.add(Field.ANZAHL_SCHUELER);
        }
    }

    public int getColumnCount() {
        return columns.size();
    }

    public int size() {
        return kurse.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Kurs kurs = kurse.get(rowIndex);
        Object value = null;
        switch (columns.get(columnIndex)) {
            case SCHULJAHR:
                value = kurs.getSemester().getSchuljahr();
                break;
            case SEMESTERBEZEICHNUNG:
                value = kurs.getSemester().getSemesterbezeichnung();
                break;
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
                StringBuilder leitung = new StringBuilder();
                for (Lehrkraft lehrkraft : kurs.getLehrkraefte()) {
                    if (leitung.length() > 0) {
                        leitung.append(" / ");
                    }
                    leitung.append(lehrkraft.getVorname()).append(" ").append(lehrkraft.getNachname());
                }
                value = leitung.toString();
                break;
            case BEMERKUNGEN:
                value = kurs.getBemerkungen();
                break;
            case ANZAHL_SCHUELER:
                value = kurs.getSchueler().size();
                break;
            default:
                break;
        }
        return value;
    }

    public void setKurse(List<Kurs> kurse) {
        this.kurse = kurse;
    }

    public String getColumnName(int column) {
        return columns.get(column).toString();
    }

    public List<Kurs> getKurse() {
        return kurse;
    }

    public Kurs getKursSelected(int rowIndex) {
        return kurse.get(rowIndex);
    }

    public int getAnzahlSchueler() {
        int anzahlSchueler = 0;
        for (Kurs kurs : kurse) {
            anzahlSchueler += kurs.getSchueler().size();
        }
        return anzahlSchueler;
    }

    public Kurs getKursAt(int rowIndex) {
        return kurse.get(rowIndex);
    }
}
