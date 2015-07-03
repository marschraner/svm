package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Hans Stamm
 */
public class SchuelerSuchenResult {

    private List<Schueler> schuelerList;

    public SchuelerSuchenResult(List<Schueler> schuelerList) {
        this.schuelerList = schuelerList;
    }

    private static final String[] COLUMNS = {"Vorname", "Nachname", "Geburtsdatum", "Geschlecht", "Strasse/Hausnummer", "PLZ", "Ort", "Festnetz", "Natel", "Email", "Mutter", "Vater", "Rechnungsempfänger"};

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
            case "Nachname" :
                value = schueler.getNachname();
                break;
            case "Vorname" :
                value = schueler.getVorname();
                break;
            case "Geburtsdatum" :
                value = asString(schueler.getGeburtsdatum());
                break;
            case "Geschlecht" :
                value = schueler.getGeschlecht();
                break;
            case "Strasse/Hausnummer" :
                value = schuelerAdresse.getStrasseHausnummer();
               break;
            case "PLZ" :
                value = schuelerAdresse.getPlz();
                break;
            case "Ort" :
                value = schuelerAdresse.getOrt();
                break;
            case "Festnetz" :
                value = schuelerAdresse.getFestnetz();
                break;
            case "Natel" :
                value = schueler.getNatel();
                break;
            case "E-Mail" :
                value = schueler.getEmail();
                break;
            case "Mutter" :
                value = getString(schueler.getMutter());
                break;
            case "Vater" :
                value = getString(schueler.getVater());
                break;
            case "Rechnungsempfänger" :
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
        return COLUMNS[column];
    }

    public SchuelerDatenblattModel getSchuelerDatenblattModel(int rowIndex) {
        return new SchuelerDatenblattModelImpl(schuelerList.get(rowIndex));
    }

}
