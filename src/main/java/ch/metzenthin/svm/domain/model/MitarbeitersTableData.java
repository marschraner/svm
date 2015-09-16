package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;

import java.util.Calendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class MitarbeitersTableData {

    private List<Mitarbeiter> lehrkraefte;

    public MitarbeitersTableData(List<Mitarbeiter> lehrkraefte) {
        this.lehrkraefte = lehrkraefte;
    }

    private static final Field[] COLUMNS = {Field.NACHNAME, Field.VORNAME, Field.STRASSE_HAUSNUMMER, Field.PLZ, Field.ORT, Field.FESTNETZ, Field.NATEL, Field.EMAIL, Field.GEBURTSDATUM, Field.AHV_NUMMER, Field.VERTRETUNGSMOEGLICHKEITEN, Field.AKTIV};

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return lehrkraefte.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Mitarbeiter mitarbeiter = lehrkraefte.get(rowIndex);
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case NACHNAME:
                value = mitarbeiter.getNachname();
                break;
            case VORNAME:
                value = mitarbeiter.getVorname();
                break;
            case STRASSE_HAUSNUMMER:
                value = mitarbeiter.getAdresse().getStrasseHausnummer();
                break;
            case PLZ:
                value = mitarbeiter.getAdresse().getPlz();
                break;
            case ORT:
                value = mitarbeiter.getAdresse().getOrt();
                break;
            case FESTNETZ:
                value = mitarbeiter.getFestnetz();
                break;
            case NATEL:
                value = mitarbeiter.getNatel();
                break;
            case EMAIL:
                value = mitarbeiter.getEmail();
                break;
            case GEBURTSDATUM:
                value = mitarbeiter.getGeburtsdatum();
                break;
            case AHV_NUMMER:
                value = mitarbeiter.getAhvNummer();
                break;
            case VERTRETUNGSMOEGLICHKEITEN:
                value = mitarbeiter.getVertretungsmoeglichkeiten();
                break;
            case AKTIV:
                value = (mitarbeiter.getAktiv() ? "ja" : "nein");
                break;
            default:
                break;
        }
        return value;
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (COLUMNS[columnIndex]) {
            case GEBURTSDATUM:
                return Calendar.class;
            default:
                return String.class;
        }
    }

    public String getColumnName(int column) {
        return COLUMNS[column].toString();
    }

    public List<Mitarbeiter> getLehrkraefte() {
        return lehrkraefte;
    }

    public void setMitarbeiters(List<Mitarbeiter> lehrkraefte) {
        this.lehrkraefte = lehrkraefte;
    }
}
