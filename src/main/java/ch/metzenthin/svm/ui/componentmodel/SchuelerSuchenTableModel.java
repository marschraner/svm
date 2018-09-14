package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.common.dataTypes.Gruppe;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.domain.model.SchuelerSuchenTableData;
import ch.metzenthin.svm.persistence.entities.*;

import javax.swing.table.AbstractTableModel;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


public class SchuelerSuchenTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private SchuelerSuchenTableData schuelerSuchenTableData;

    public SchuelerSuchenTableModel(SchuelerSuchenTableData schuelerSuchenTableData) {
        super();
        this.schuelerSuchenTableData = schuelerSuchenTableData;
    }

    @Override
    public int getRowCount() {
        return schuelerSuchenTableData.size();
    }

    @Override
    public int getColumnCount() {
        return schuelerSuchenTableData.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return schuelerSuchenTableData.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        schuelerSuchenTableData.setValueAt(value, rowIndex, columnIndex);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return schuelerSuchenTableData.isCellEditable(columnIndex);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return schuelerSuchenTableData.getColumnName(columnIndex);
    }

    public SchuelerDatenblattModel getSchuelerDatenblattModel(int rowIndex) {
        return schuelerSuchenTableData.getSchuelerDatenblattModel(rowIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return schuelerSuchenTableData.getColumnClass(columnIndex);
    }

    public int getAnzahlLektionen() {
        return schuelerSuchenTableData.getAnzahlLektionen();
    }

    public int getAnzahlMaercheneinteilungen() {
        return schuelerSuchenTableData.getAnzahlMaercheneinteilungen();
    }

    public List<Schueler> getSchuelerList() {
        return getSchuelerSuchenTableData().getSchuelerList();
    }

    public int getAnzExport() {
        return schuelerSuchenTableData.getAnzExport();
    }

    public List<Schueler> getZuExportierendeSchuelerList() {
        return schuelerSuchenTableData.getZuExportierendeSchuelerList();
    }

    public boolean isAlleSelektiert() {
        return schuelerSuchenTableData.isAlleSelektiert();
    }

    public void alleSchuelerSelektieren() {
        schuelerSuchenTableData.alleSchuelerSelektieren();
    }

    public void alleSchuelerDeselektieren() {
        schuelerSuchenTableData.alleSchuelerDeselektieren();
    }

    public Semester getSemester() {
        return schuelerSuchenTableData.getSemester();
    }

    public Wochentag getWochentag() {
        return schuelerSuchenTableData.getWochentag();
    }

    public Time getZeitBeginn() {
        return schuelerSuchenTableData.getZeitBeginn();
    }

    public Mitarbeiter getLehrkraft() {
        return schuelerSuchenTableData.getMitarbeiter();
    }

    public Calendar getAnmledemonat() {
        return schuelerSuchenTableData.getAnmeldemonat();
    }

    public Calendar getAbmledemonat() {
        return schuelerSuchenTableData.getAbmeldemonat();
    }

    public SchuelerSuchenTableData getSchuelerSuchenTableData() {
        return schuelerSuchenTableData;
    }

    public Map<Schueler, List<Kurs>> getKurse() {
        return schuelerSuchenTableData.getKurse();
    }

    public Maerchen getMaerchen() {
        return schuelerSuchenTableData.getMaerchen();
    }

    public Map<Schueler, Maercheneinteilung> getMaercheneinteilungen() {
        return schuelerSuchenTableData.getMaercheneinteilungen();
    }

    public int getAnzZuExportierendeMaercheneinteilungen() {
        return schuelerSuchenTableData.getAnzZuExportierendeMaercheneinteilungen();
    }

    public Gruppe getGruppe() {
        return schuelerSuchenTableData.getGruppe();
    }

    public ElternmithilfeCode getElternmithilfeCode() {
        return schuelerSuchenTableData.getElternmithilfeCode();
    }

    public boolean isKursFuerSucheBeruecksichtigen() {
        return schuelerSuchenTableData.isKursFuerSucheBeruecksichtigen();
    }

    public boolean isMaerchenFuerSucheBeruecksichtigen() {
        return schuelerSuchenTableData.isMaerchenFuerSucheBeruecksichtigen();
    }

    public boolean isNachRollenGesucht() {
        return schuelerSuchenTableData.isNachRollenGesucht();
    }

    public Calendar getStichtag() {
        return schuelerSuchenTableData.getStichtag();
    }

    public boolean isKeineAbgemeldetenKurseAnzeigen() {
        return schuelerSuchenTableData.isKeineAbgemeldetenKurseAnzeigen();
    }
}
