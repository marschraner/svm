package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SemesterrechnungenTableData {

    private List<Semesterrechnung> semesterrechnungen;
    private Semester semester;

    public SemesterrechnungenTableData(List<Semesterrechnung> semesterrechnungen, Semester semester) {
        this.semesterrechnungen = semesterrechnungen;
        this.semester = semester;
    }

    private static final Field[] COLUMNS = {Field.RECHNUNGSEMPFAENGER, Field.SCHUELER, Field.RECHNUNGSDATUM_VORRECHNUNG, Field.ERMAESSIGUNG_VORRECHNUNG,
            Field.ZUSCHLAG_VORRECHNUNG, Field.ANZAHL_WOCHEN_VORRECHNUNG, Field.WOCHENBETRAG_VORRECHNUNG, Field.RECHNUNGSBETRAG_VORRECHNUNG,
            Field.RECHNUNGSDATUM_NACHRECHNUNG, Field.ERMAESSIGUNG_NACHRECHNUNG,
            Field.ZUSCHLAG_NACHRECHNUNG, Field.ANZAHL_WOCHEN_NACHRECHNUNG, Field.WOCHENBETRAG_NACHRECHNUNG, Field.RECHNUNGSBETRAG_NACHRECHNUNG, Field.DIFFERENZ_RECHNUNGSBETRAG,
            Field.BETRAG_ZAHLUNG_1_NACHRECHNUNG, Field.BETRAG_ZAHLUNG_2_NACHRECHNUNG, Field.BETRAG_ZAHLUNG_3_NACHRECHNUNG, Field.RESTBETRAG_NACHRECHNUNG};

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return semesterrechnungen.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Semesterrechnung semesterrechnung = semesterrechnungen.get(rowIndex);
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case RECHNUNGSEMPFAENGER:
                value = semesterrechnung.getRechnungsempfaenger().getNachname() + " " + semesterrechnung.getRechnungsempfaenger().getVorname();
                break;
            case SCHUELER:
                value = semesterrechnung.getRechnungsempfaenger().getSchuelerRechnungsempfaengerAsStr();
                break;
            case RECHNUNGSDATUM_VORRECHNUNG:
                value = semesterrechnung.getRechnungsdatumVorrechnung();
                break;
            case ERMAESSIGUNG_VORRECHNUNG:
                value = semesterrechnung.getErmaessigungVorrechnung();
                break;
            case ZUSCHLAG_VORRECHNUNG:
                value = semesterrechnung.getZuschlagVorrechnung();
                break;
            case ANZAHL_WOCHEN_VORRECHNUNG:
                value = semesterrechnung.getAnzahlWochenVorrechnung();
                break;
            case WOCHENBETRAG_VORRECHNUNG:
                value = semesterrechnung.getWochenbetragVorrechnung();
                break;
            case RECHNUNGSBETRAG_VORRECHNUNG:
                value = semesterrechnung.getRechnungsbetragVorrechnung();
                break;
            case RECHNUNGSDATUM_NACHRECHNUNG:
                value = semesterrechnung.getRechnungsdatumNachrechnung();
                break;
            case ERMAESSIGUNG_NACHRECHNUNG:
                value = semesterrechnung.getErmaessigungNachrechnung();
                break;
            case ZUSCHLAG_NACHRECHNUNG:
                value = semesterrechnung.getZuschlagNachrechnung();
                break;
            case ANZAHL_WOCHEN_NACHRECHNUNG:
                value = semesterrechnung.getAnzahlWochenNachrechnung();
                break;
            case WOCHENBETRAG_NACHRECHNUNG:
                value = semesterrechnung.getWochenbetragNachrechnung();
                break;
            case RECHNUNGSBETRAG_NACHRECHNUNG:
                value = semesterrechnung.getRechnungsbetragNachrechnung();
                break;
            case DIFFERENZ_RECHNUNGSBETRAG:
                value = semesterrechnung.getDifferenzRechnungsbetrag();
                break;
            case BETRAG_ZAHLUNG_1_NACHRECHNUNG:
                value = semesterrechnung.getBetragZahlung1Nachrechnung();
                break;
            case BETRAG_ZAHLUNG_2_NACHRECHNUNG:
                value = semesterrechnung.getBetragZahlung2Nachrechnung();
                break;
            case BETRAG_ZAHLUNG_3_NACHRECHNUNG:
                value = semesterrechnung.getBetragZahlung3Nachrechnung();
                break;
            case RESTBETRAG_NACHRECHNUNG:
                value = semesterrechnung.getRestbetragNachrechnung();
                break;
            default:
                break;
        }
        return value;
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (COLUMNS[columnIndex]) {
            case RECHNUNGSDATUM_VORRECHNUNG:
                return Calendar.class;
            case ERMAESSIGUNG_VORRECHNUNG:
                return BigDecimal.class;
            case ZUSCHLAG_VORRECHNUNG:
                return BigDecimal.class;
            case ANZAHL_WOCHEN_VORRECHNUNG:
                return Integer.class;
            case WOCHENBETRAG_VORRECHNUNG:
                return BigDecimal.class;
            case RECHNUNGSBETRAG_VORRECHNUNG:
                return BigDecimal.class;
            case RECHNUNGSDATUM_NACHRECHNUNG:
                return Calendar.class;
            case ERMAESSIGUNG_NACHRECHNUNG:
                return BigDecimal.class;
            case ZUSCHLAG_NACHRECHNUNG:
                return BigDecimal.class;
            case ANZAHL_WOCHEN_NACHRECHNUNG:
                return Integer.class;
            case WOCHENBETRAG_NACHRECHNUNG:
                return BigDecimal.class;
            case RECHNUNGSBETRAG_NACHRECHNUNG:
                return BigDecimal.class;
            case DIFFERENZ_RECHNUNGSBETRAG:
                return BigDecimal.class;
            case BETRAG_ZAHLUNG_1_NACHRECHNUNG:
                return BigDecimal.class;
            case BETRAG_ZAHLUNG_2_NACHRECHNUNG:
                return BigDecimal.class;
            case BETRAG_ZAHLUNG_3_NACHRECHNUNG:
                return BigDecimal.class;
            case RESTBETRAG_NACHRECHNUNG:
                return BigDecimal.class;
            default:
                return String.class;
        }
    }

    public String getColumnName(int column) {
        return COLUMNS[column].toString();
    }

    public Semester getSememester() {
        return semester;
    }

    public Semesterrechnung getSemesterrechnungSelected(int rowIndex) {
        return semesterrechnungen.get(rowIndex);
    }

    public List<Semesterrechnung> getSemesterrechnungen() {
        return semesterrechnungen;
    }

    public void setSemesterrechnungen(List<Semesterrechnung> semesterrechnungen) {
        this.semesterrechnungen = semesterrechnungen;
    }
}
