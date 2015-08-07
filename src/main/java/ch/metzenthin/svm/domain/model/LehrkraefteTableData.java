package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;

import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Martin Schraner
 */
public class LehrkraefteTableData {

    private List<Lehrkraft> lehrkraefte;

    public LehrkraefteTableData(List<Lehrkraft> lehrkraefte) {
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
        Lehrkraft lehrkraft = lehrkraefte.get(rowIndex);
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case NACHNAME:
                value = lehrkraft.getNachname();
                break;
            case VORNAME:
                value = lehrkraft.getVorname();
                break;
            case STRASSE_HAUSNUMMER:
                value = lehrkraft.getAdresse().getStrasseHausnummer();
                break;
            case PLZ:
                value = lehrkraft.getAdresse().getPlz();
                break;
            case ORT:
                value = lehrkraft.getAdresse().getOrt();
                break;
            case FESTNETZ:
                value = lehrkraft.getFestnetz();
                break;
            case NATEL:
                value = lehrkraft.getNatel();
                break;
            case EMAIL:
                value = lehrkraft.getEmail();
                break;
            case GEBURTSDATUM:
                value = asString(lehrkraft.getGeburtsdatum());
                break;
            case AHV_NUMMER:
                value = lehrkraft.getAhvNummer();
                break;
            case VERTRETUNGSMOEGLICHKEITEN:
                value = lehrkraft.getVertretungsmoeglichkeiten();
                break;
            case AKTIV:
                value = (lehrkraft.isAktiv() ? "ja" : "nein");
                break;
            default:
                break;
        }
        return value;
    }

    public String getColumnName(int column) {
        return COLUMNS[column].toString();
    }

    public List<Lehrkraft> getLehrkraefte() {
        return lehrkraefte;
    }
}
