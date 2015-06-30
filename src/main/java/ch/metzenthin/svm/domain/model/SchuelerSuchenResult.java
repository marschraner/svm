package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.List;

/**
 * @author Hans Stamm
 */
public class SchuelerSuchenResult {

    private List<Schueler> schuelerList;

    public SchuelerSuchenResult(List<Schueler> schuelerList) {
        this.schuelerList = schuelerList;
    }

    private static final String[] COLUMNS = {"Nachname", "Vorname", "Geburtsdatum"};

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return schuelerList.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Schueler schueler = schuelerList.get(rowIndex);
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case "Nachname" :
                value = schueler.getNachname();
                break;
            case "Vorname" :
                value = schueler.getVorname();
                break;
            case "Geburtsdatum" :
                value = schueler.getGeburtsdatum();
                break;
            default:
                break;
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
