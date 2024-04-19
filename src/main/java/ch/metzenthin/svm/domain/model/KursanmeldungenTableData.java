package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;

import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Martin Schraner
 */
public class KursanmeldungenTableData {

    private static final Field[] COLUMNS = {
            Field.SCHULJAHR,
            Field.SEMESTER,
            Field.KURSTYP_BEZEICHNUNG,
            Field.ALTERSBEREICH,
            Field.STUFE,
            Field.TAG,
            Field.ZEIT_BEGINN,
            Field.ZEIT_ENDE,
            Field.ORT,
            Field.LEITUNG,
            Field.ANMELDEDATUM,
            Field.ABMELDEDATUM,
            Field.BEMERKUNGEN};

    private List<Kursanmeldung> kursanmeldungen;

    public KursanmeldungenTableData(List<Kursanmeldung> kursanmeldungen) {
        this.kursanmeldungen = kursanmeldungen;
    }

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return kursanmeldungen.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Kursanmeldung kursanmeldung = kursanmeldungen.get(rowIndex);
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case SCHULJAHR -> value = kursanmeldung.getKurs().getSemester().getSchuljahr();
            case SEMESTER -> value = kursanmeldung.getKurs().getSemester().getSemesterbezeichnung();
            case KURSTYP_BEZEICHNUNG -> value = kursanmeldung.getKurs().getKurstyp().getBezeichnung();
            case ALTERSBEREICH -> value = kursanmeldung.getKurs().getAltersbereich();
            case STUFE -> value = kursanmeldung.getKurs().getStufe();
            case TAG -> value = kursanmeldung.getKurs().getWochentag().toString();
            case ZEIT_BEGINN -> value = asString(kursanmeldung.getKurs().getZeitBeginn());
            case ZEIT_ENDE -> value = asString(kursanmeldung.getKurs().getZeitEnde());
            case ORT -> value = kursanmeldung.getKurs().getKursort().getBezeichnung();
            case LEITUNG -> {
                StringBuilder leitung = new StringBuilder();
                for (Mitarbeiter mitarbeiter : kursanmeldung.getKurs().getLehrkraefte()) {
                    if (!leitung.isEmpty()) {
                        leitung.append(" / ");
                    }
                    leitung.append(mitarbeiter.getVorname()).append(" ").append(mitarbeiter.getNachname());
                }
                value = leitung.toString();
            }
            case ANMELDEDATUM ->
                    value = (kursanmeldung.getAnmeldedatum() == null ? "" : asString(kursanmeldung.getAnmeldedatum()));
            case ABMELDEDATUM ->
                    value = (kursanmeldung.getAbmeldedatum() == null ? "" : asString(kursanmeldung.getAbmeldedatum()));
            case BEMERKUNGEN -> value = kursanmeldung.getBemerkungen();
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

    public Kursanmeldung getKursanmeldungSelected(int rowIndex) {
        return kursanmeldungen.get(rowIndex);
    }

    public List<Kursanmeldung> getKursanmeldungen() {
        return kursanmeldungen;
    }

    public void setKursanmeldungen(List<Kursanmeldung> kursanmeldungen) {
        this.kursanmeldungen = kursanmeldungen;
    }
}
