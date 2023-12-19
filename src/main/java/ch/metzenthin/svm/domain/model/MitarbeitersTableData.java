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
public record MitarbeitersTableData(List<Mitarbeiter> mitarbeiters) {

    private static final Field[] COLUMNS = {
            Field.SELEKTIERT,
            Field.NACHNAME,
            Field.VORNAME,
            Field.STRASSE_HAUSNUMMER,
            Field.PLZ,
            Field.ORT,
            Field.FESTNETZ,
            Field.NATEL,
            Field.EMAIL,
            Field.GEBURTSDATUM,
            Field.AHV_NUMMER,
            Field.IBAN_NUMMER,
            Field.LEHRKRAFT,
            Field.CODES,
            Field.VERTRETUNGSMOEGLICHKEITEN,
            Field.BEMERKUNGEN,
            Field.AKTIV};

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
            case SELEKTIERT -> value = mitarbeiter.isSelektiert();
            case NACHNAME -> value = mitarbeiter.getNachname();
            case VORNAME -> value = mitarbeiter.getVorname();
            case STRASSE_HAUSNUMMER ->
                    value = (mitarbeiter.getAdresse() == null ? "" : mitarbeiter.getAdresse().getStrHausnummer());
            case PLZ -> value = (mitarbeiter.getAdresse() == null ? "" : mitarbeiter.getAdresse().getPlz());
            case ORT -> value = (mitarbeiter.getAdresse() == null ? "" : mitarbeiter.getAdresse().getOrt());
            case FESTNETZ -> value = (!checkNotEmpty(mitarbeiter.getFestnetz()) ? "" : mitarbeiter.getFestnetz());
            case NATEL -> value = (!checkNotEmpty(mitarbeiter.getNatel()) ? "" : mitarbeiter.getNatel());
            case EMAIL -> value = (!checkNotEmpty(mitarbeiter.getEmail()) ? "" : mitarbeiter.getEmail());
            case GEBURTSDATUM -> value = mitarbeiter.getGeburtsdatum();
            case AHV_NUMMER -> value = (!checkNotEmpty(mitarbeiter.getAhvNummer()) ? "" : mitarbeiter.getAhvNummer());
            case IBAN_NUMMER ->
                    value = (!checkNotEmpty(mitarbeiter.getIbanNummer()) ? "" : mitarbeiter.getIbanNummer());
            case LEHRKRAFT -> value = (mitarbeiter.getLehrkraft() ? "ja" : "nein");
            case AKTIV -> value = (mitarbeiter.getAktiv() ? "ja" : "nein");
            case CODES -> value = mitarbeiter.getMitarbeiterCodesAsStr();
            case VERTRETUNGSMOEGLICHKEITEN ->
                    value = mitarbeiter.getVertretungsmoeglichkeitenLineBreaksReplacedBySemicolonOrPeriod();
            case BEMERKUNGEN -> value = mitarbeiter.getBemerkungenLineBreaksReplacedBySemicolonOrPeriod();
            default -> {
            }
        }
        return value;
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (COLUMNS[columnIndex] == Field.SELEKTIERT) {
            Mitarbeiter mitarbeiter = mitarbeiters.get(rowIndex);
            mitarbeiter.setSelektiert((boolean) value);
            mitarbeiters.set(rowIndex, mitarbeiter);
        }
    }

    public boolean isCellEditable(int columnIndex) {
        return COLUMNS[columnIndex] == Field.SELEKTIERT;
    }

    public Class<?> getColumnClass(int columnIndex) {
        return switch (COLUMNS[columnIndex]) {
            case GEBURTSDATUM -> Calendar.class;
            case SELEKTIERT -> Boolean.class;
            default -> String.class;
        };
    }

    public String getColumnName(int column) {
        return COLUMNS[column].toString();
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
