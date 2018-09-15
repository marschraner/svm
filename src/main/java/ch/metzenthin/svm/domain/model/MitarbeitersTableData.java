package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;

import java.util.ArrayList;
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

    private static final Field[] COLUMNS = {Field.SELEKTIERT, Field.NACHNAME, Field.VORNAME, Field.STRASSE_HAUSNUMMER, Field.PLZ, Field.ORT, Field.FESTNETZ, Field.NATEL, Field.EMAIL, Field.GEBURTSDATUM, Field.AHV_NUMMER, Field.LEHRKRAFT, Field.CODES, Field.VERTRETUNGSMOEGLICHKEITEN, Field.BEMERKUNGEN, Field.AKTIV};

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return mitarbeiters.size();
    }

    public int getAnzSelektiert() {
        int anzSelektiert = 0;
        for (Mitarbeiter mitarbeiter : mitarbeiters) {
            if (mitarbeiter.isSelektiert()) {
                anzSelektiert++;
            }
        }
        return anzSelektiert;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Mitarbeiter mitarbeiter = mitarbeiters.get(rowIndex);
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case SELEKTIERT:
                value = mitarbeiter.isSelektiert();
                break;
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
                value = mitarbeiter.getVertretungsmoeglichkeitenLineBreaksReplacedBySemicolonOrPeriod();
                break;
            case BEMERKUNGEN:
                value = mitarbeiter.getBemerkungenLineBreaksReplacedBySemicolonOrPeriod();
                break;
            default:
                break;
        }
        return value;
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        switch (COLUMNS[columnIndex]) {
            case SELEKTIERT:
                Mitarbeiter mitarbeiter = mitarbeiters.get(rowIndex);
                mitarbeiter.setSelektiert((boolean) value);
                mitarbeiters.set(rowIndex, mitarbeiter);
                break;
            default:
        }
    }

    public boolean isCellEditable(int columnIndex) {
        switch (COLUMNS[columnIndex]) {
            case SELEKTIERT:
                return true;
            default:
                return false;
        }
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (COLUMNS[columnIndex]) {
            case GEBURTSDATUM:
                return Calendar.class;
            case SELEKTIERT:
                return Boolean.class;
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

    public List<Mitarbeiter> getSelektierteMitarbeiters() {
        List<Mitarbeiter> selektierteMitarbeiters = new ArrayList<>();
        for (Mitarbeiter mitarbeiter : mitarbeiters) {
            if (mitarbeiter.isSelektiert()) {
                selektierteMitarbeiters.add(mitarbeiter);
            }
        }
        return selektierteMitarbeiters;
    }

    public boolean isAlleSelektiert() {
        for (Mitarbeiter mitarbeiter : mitarbeiters) {
            if (!mitarbeiter.isSelektiert()) {
                return false;
            }
        }
        return true;
    }

    public void alleMitarbeiterSelektieren() {
        for (Mitarbeiter mitarbeiter : mitarbeiters) {
            mitarbeiter.setSelektiert(true);
        }
    }

    public void alleMitarbeiterDeselektieren() {
        for (Mitarbeiter mitarbeiter : mitarbeiters) {
            mitarbeiter.setSelektiert(false);
        }
    }
}
