package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.common.dataTypes.Gruppe;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.domain.model.SchuelerSuchenTableData;
import ch.metzenthin.svm.persistence.entities.*;

import javax.swing.table.AbstractTableModel;
import java.sql.Time;
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

    public Semester getSemester() {
        return schuelerSuchenTableData.getSemester();
    }

    public Wochentag getWochentag() {
        return schuelerSuchenTableData.getWochentag();
    }

    public Time getZeitBeginn() {
        return schuelerSuchenTableData.getZeitBeginn();
    }

    public Lehrkraft getLehrkraft() {
        return schuelerSuchenTableData.getLehrkraft();
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

    public Gruppe getGruppe() {
        return schuelerSuchenTableData.getGruppe();
    }

    public ElternmithilfeCode getElternmithilfeCode() {
        return schuelerSuchenTableData.getElternmithilfeCode();
    }

    public boolean isMaerchenFuerSucheBeruecksichtigen() {
        return schuelerSuchenTableData.isMaerchenFuerSucheBeruecksichtigen();
    }

    public boolean isNachRollenGesucht() {
        return schuelerSuchenTableData.isNachRollenGesucht();
    }
}
