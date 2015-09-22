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

    private List<Kursanmeldung> kursanmeldungen;

    public KursanmeldungenTableData(List<Kursanmeldung> kursanmeldungen) {
        this.kursanmeldungen = kursanmeldungen;
    }

    private static final Field[] COLUMNS = {Field.SCHULJAHR, Field.SEMESTER, Field.KURSTYP_BEZEICHNUNG, Field.ALTERSBEREICH, Field.STUFE,
            Field.TAG, Field.ZEIT_BEGINN, Field.ZEIT_ENDE, Field.ORT, Field.LEITUNG, Field.ANMELDEDATUM, Field.ABMELDEDATUM, Field.BEMERKUNGEN};

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
            case SCHULJAHR:
                value = kursanmeldung.getKurs().getSemester().getSchuljahr();
                break;
            case SEMESTER:
                value = kursanmeldung.getKurs().getSemester().getSemesterbezeichnung();
                break;
            case KURSTYP_BEZEICHNUNG:
                value = kursanmeldung.getKurs().getKurstyp().getBezeichnung();
                break;
            case ALTERSBEREICH:
                value = kursanmeldung.getKurs().getAltersbereich();
                break;
            case STUFE:
                value = kursanmeldung.getKurs().getStufe();
                break;
            case TAG:
                value = kursanmeldung.getKurs().getWochentag().toString();
                break;
            case ZEIT_BEGINN:
                value = asString(kursanmeldung.getKurs().getZeitBeginn());
                break;
            case ZEIT_ENDE:
                value = asString(kursanmeldung.getKurs().getZeitEnde());
                break;
            case ORT:
                value = kursanmeldung.getKurs().getKursort().getBezeichnung();
                break;
            case LEITUNG:
                StringBuilder leitung = new StringBuilder();
                for (Mitarbeiter mitarbeiter : kursanmeldung.getKurs().getLehrkraefte()) {
                    if (leitung.length() > 0) {
                        leitung.append(" / ");
                    }
                    leitung.append(mitarbeiter.getVorname()).append(" ").append(mitarbeiter.getNachname());
                }
                value = leitung.toString();
                break;
            case ANMELDEDATUM:
                value = (kursanmeldung.getAnmeldedatum() == null ? "" : asString(kursanmeldung.getAnmeldedatum()));
                break;
            case ABMELDEDATUM:
                value = (kursanmeldung.getAbmeldedatum() == null ? "" : asString(kursanmeldung.getAbmeldedatum()));
                break;
            case BEMERKUNGEN:
                value = kursanmeldung.getBemerkungen();
                break;
            default:
                break;
        }
        return value;
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (COLUMNS[columnIndex]) {
            default:
                return String.class;
        }
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
