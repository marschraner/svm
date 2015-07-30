package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.Calendar;
import java.util.List;

/**
 * @author Hans Stamm
 */
public class SchuelerSuchenTableData {

    private List<Schueler> schuelerList;

    public SchuelerSuchenTableData(List<Schueler> schuelerList) {
        this.schuelerList = schuelerList;
    }

    private static final Field[] COLUMNS = {Field.NACHNAME, Field.VORNAME, Field.STRASSE_HAUSNUMMER, Field.PLZ, Field.ORT, Field.FESTNETZ, Field.NATEL, Field.EMAIL, Field.GEBURTSDATUM, Field.MUTTER, Field.VATER, Field.RECHNUNGSEMPFAENGER};

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return schuelerList.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Schueler schueler = schuelerList.get(rowIndex);
        Adresse schuelerAdresse = schueler.getAdresse();
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case NACHNAME:
                value = schueler.getNachname();
                break;
            case VORNAME:
                value = schueler.getVorname();
                break;
            case STRASSE_HAUSNUMMER:
                value = schuelerAdresse.getStrasseHausnummer();
               break;
            case PLZ:
                value = schuelerAdresse.getPlz();
                break;
            case ORT:
                value = schuelerAdresse.getOrt();
                break;
            case FESTNETZ:
                value = schueler.getFestnetz();
                break;
            case NATEL:
                value = schueler.getNatel();
                break;
            case EMAIL:
                value = schueler.getEmail();
                break;
            case GEBURTSDATUM:
                value = schueler.getGeburtsdatum();
                break;
            case MUTTER:
                value = getString(schueler.getMutter());
                break;
            case VATER:
                value = getString(schueler.getVater());
                break;
            case RECHNUNGSEMPFAENGER:
                value = getString(schueler.getRechnungsempfaenger());
                break;
            default:
                break;
        }
        return value;
    }

    private String getString(Angehoeriger angehoeriger) {
        String value = null;
        if (angehoeriger != null) {
            value = angehoeriger.getVorname() + " " + angehoeriger.getNachname();
        }
        return value;
    }

    public String getColumnName(int column) {
        return COLUMNS[column].toString();
    }

    public Class<?> getColumnClass(int column) {
        if (COLUMNS[column] == Field.GEBURTSDATUM) {
            return Calendar.class;
        } else {
            return String.class;
        }
    }

    public SchuelerDatenblattModel getSchuelerDatenblattModel(int rowIndex) {
        return new SchuelerDatenblattModelImpl(schuelerList.get(rowIndex));
    }

}
