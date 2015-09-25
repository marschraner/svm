package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;

import java.util.Calendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class MitarbeitersTableData {

    private List<Mitarbeiter> mitarbeiters;

    public MitarbeitersTableData(List<Mitarbeiter> mitarbeiters) {
        this.mitarbeiters = mitarbeiters;
    }

    private static final Field[] COLUMNS = {Field.NACHNAME, Field.VORNAME, Field.STRASSE_HAUSNUMMER, Field.PLZ, Field.ORT, Field.FESTNETZ, Field.NATEL, Field.EMAIL, Field.GEBURTSDATUM, Field.AHV_NUMMER, Field.LEHRKRAFT, Field.CODES, Field.VERTRETUNGSMOEGLICHKEITEN, Field.BEMERKUNGEN, Field.AKTIV};

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return mitarbeiters.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Mitarbeiter mitarbeiter = mitarbeiters.get(rowIndex);
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case NACHNAME:
                value = mitarbeiter.getNachname();
                break;
            case VORNAME:
                value = mitarbeiter.getVorname();
                break;
            case STRASSE_HAUSNUMMER:
                value = (mitarbeiter.getAdresse() == null ? "" : mitarbeiter.getAdresse().getStrHausnummer());
                break;
            case PLZ:
                value = (mitarbeiter.getAdresse() == null ? "" : mitarbeiter.getAdresse().getPlz());
                break;
            case ORT:
                value = (mitarbeiter.getAdresse() == null ? "" : mitarbeiter.getAdresse().getOrt());
                break;
            case FESTNETZ:
                value = (!checkNotEmpty(mitarbeiter.getFestnetz()) ? "" : mitarbeiter.getFestnetz());
                break;
            case NATEL:
                value = (!checkNotEmpty(mitarbeiter.getNatel()) ? "" : mitarbeiter.getNatel());
                break;
            case EMAIL:
                value = (!checkNotEmpty(mitarbeiter.getEmail()) ? "" : mitarbeiter.getEmail());
                break;
            case GEBURTSDATUM:
                value = mitarbeiter.getGeburtsdatum();
                break;
            case AHV_NUMMER:
                value = (!checkNotEmpty(mitarbeiter.getAhvNummer()) ? "" : mitarbeiter.getAhvNummer());
                break;
            case LEHRKRAFT:
                value = (mitarbeiter.getLehrkraft() ? "ja" : "nein");
                break;
            case AKTIV:
                value = (mitarbeiter.getAktiv() ? "ja" : "nein");
                break;
            case CODES:
                value = mitarbeiter.getMitarbeiterCodesAsStr();
                break;
            case VERTRETUNGSMOEGLICHKEITEN:
                value = mitarbeiter.getVertretungsmoeglichkeiten();
                break;
            case BEMERKUNGEN:
                value = mitarbeiter.getBemerkungen();
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

    public List<Mitarbeiter> getMitarbeiters() {
        return mitarbeiters;
    }
}
