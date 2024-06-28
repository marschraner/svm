package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.common.utils.StringNumber;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;

import java.util.ArrayList;
import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Martin Schraner
 */
public class KurseTableData {

    private List<Kurs> kurse;
    private final List<Field> columns = new ArrayList<>();

    KurseTableData(List<Kurs> kurse) {
        this.kurse = kurse;
        columns.add(Field.KURSTYP_BEZEICHNUNG);
        columns.add(Field.ALTERSBEREICH);
        columns.add(Field.STUFE);
        columns.add(Field.TAG);
        columns.add(Field.ZEIT_BEGINN);
        columns.add(Field.ZEIT_ENDE);
        columns.add(Field.ORT);
        columns.add(Field.LEITUNG);
        columns.add(Field.BEMERKUNGEN);
        columns.add(Field.ANZAHL_SCHUELER);
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
            case SCHULJAHR -> value = kurs.getSemester().getSchuljahr();
            case SEMESTERBEZEICHNUNG -> value = kurs.getSemester().getSemesterbezeichnung();
            case KURSTYP_BEZEICHNUNG -> value = kurs.getKurstyp().getBezeichnung();
            case ALTERSBEREICH ->
                // Korrekte Sortierung
                    value = new StringNumber(kurs.getAltersbereich());
            case STUFE ->
                // Korrekte Sortierung
                    value = new StringNumber(kurs.getStufe());
            case TAG -> value = kurs.getWochentag();
            case ZEIT_BEGINN -> value = asString(kurs.getZeitBeginn());
            case ZEIT_ENDE -> value = asString(kurs.getZeitEnde());
            case ORT -> value = kurs.getKursort().getBezeichnung();
            case LEITUNG -> {
                StringBuilder leitung = new StringBuilder();
                for (Mitarbeiter mitarbeiter : kurs.getLehrkraefte()) {
                    if (!leitung.isEmpty()) {
                        leitung.append(" / ");
                    }
                    leitung.append(mitarbeiter.getVorname()).append(" ").append(mitarbeiter.getNachname());
                }
                value = leitung.toString();
            }
            case BEMERKUNGEN -> value = kurs.getBemerkungen();
            case ANZAHL_SCHUELER -> value = kurs.getKursanmeldungen().size();
            default -> {
            }
        }
        return value;
    }

    public Class<?> getColumnClass(int columnIndex) {
        return switch (columns.get(columnIndex)) {
            case TAG -> Wochentag.class;
            case ANZAHL_SCHUELER -> Integer.class;
            case ALTERSBEREICH, STUFE -> StringNumber.class;
            default -> String.class;
        };
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
            anzahlSchueler += kurs.getKursanmeldungen().size();
        }
        return anzahlSchueler;
    }
}
