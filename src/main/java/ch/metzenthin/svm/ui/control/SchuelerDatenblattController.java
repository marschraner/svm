package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Codetyp;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.ui.componentmodel.*;
import ch.metzenthin.svm.ui.components.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Hans Stamm
 */
public class SchuelerDatenblattController {
    private final SvmContext svmContext;
    private final SchuelerSuchenTableModel schuelerSuchenTableModel;
    private final JTable schuelerSuchenResultTable;
    private int selectedRow;
    private SchuelerDatenblattModel schuelerDatenblattModel;
    private JButton btnErster;
    private JButton btnLetzter;
    private JButton btnNachfolgender;
    private JButton btnVorheriger;
    private JButton btnZurueck;
    private JButton btnAbbrechen;
    private JButton btnEmail;
    private JLabel labelVornameNachname;
    private JLabel labelSchueler;
    private JLabel labelSchuelerValue;
    private JLabel labelMutterValue;
    private JLabel labelVaterValue;
    private JLabel labelRechnungsempfaenger;
    private JLabel labelRechnungsempfaengerValue;
    private JLabel labelGeschwisterValue;
    private JLabel labelSchuelerGleicherRechnungsempfaenger1;
    private JLabel labelSchuelerGleicherRechnungsempfaenger2;
    private JLabel labelSchuelerGleicherRechnungsempfaengerValue;
    private JLabel labelGeburtsdatumValue;
    private JLabel labelAnmeldungValue;
    private JLabel labelAbmeldedatum;
    private JLabel labelAbmeldedatumValue;
    private JLabel labelFruehereAnmeldungen;
    private JLabel labelFruehereAnmeldungenValue;
    private JLabel labelBemerkungenValue;
    private JLabel labelNichtDispensiert;
    private JLabel labelDispensationsdauer;
    private JLabel labelDispensationsdauerValue;
    private JLabel labelDispensationsgrund;
    private JLabel labelDispensationsgrundValue;
    private JLabel labelFruehereDispensationenValue;
    private JLabel labelSchuelerCodesValue;
    private JLabel labelSemesterkurseValue;
    private JLabel labelMaerchenValue;
    private JLabel labelKurseValue;
    private JLabel labelGruppe;
    private JLabel labelGruppeValue;
    private JLabel labelRolle1;
    private JLabel labelRolle1Value;
    private JLabel labelBilderRolle1;
    private JLabel labelBilderRolle1Value;
    private JLabel labelRolle2;
    private JLabel labelRolle2Value;
    private JLabel labelBilderRolle2;
    private JLabel labelBilderRolle2Value;
    private JLabel labelRolle3;
    private JLabel labelRolle3Value;
    private JLabel labelBilderRolle3;
    private JLabel labelBilderRolle3Value;
    private JLabel labelElternmithilfe;
    private JLabel labelElternmithilfeValue;
    private JLabel labelElternmithilfeCode;
    private JLabel labelElternmithilfeCodeValue;
    private JLabel labelVorstellungenKuchen;
    private JLabel labelVorstellungenKuchenValue;
    private JLabel labelZusatzattribut;
    private JLabel labelZusatzattributValue;
    private JLabel labelBemerkungenMaerchen;
    private JLabel labelBemerkungenMaerchenValue;
    private JLabel labelScrollPosition;
    private ActionListener nextPanelListener;
    private ActionListener closeListener;
    private ActionListener zurueckZuSchuelerSuchenListener;
    private boolean isFromSchuelerSuchenResult;

    public SchuelerDatenblattController(SvmContext svmContext, SchuelerSuchenTableModel schuelerSuchenTableModel, JTable schuelerSuchenResultTable, int selectedRow, boolean isFromSchuelerSuchenResult) {
        this.svmContext = svmContext;
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.schuelerSuchenResultTable = schuelerSuchenResultTable;
        this.selectedRow = selectedRow;
        this.isFromSchuelerSuchenResult = isFromSchuelerSuchenResult;
        schuelerDatenblattModel = schuelerSuchenTableModel.getSchuelerDatenblattModel(convertRowIndexToModel());
    }

    private void scroll(int selectedRow) {
        if ((this.selectedRow == selectedRow) || (selectedRow < 0) || (selectedRow >= schuelerSuchenTableModel.getRowCount())) {
            return;
        }
        this.selectedRow = selectedRow;
        setNewModel();
    }

    private void setNewModel() {
        schuelerDatenblattModel = schuelerSuchenTableModel.getSchuelerDatenblattModel(convertRowIndexToModel());
        enableScrollButtons();
        setLabelScrollPosition();
        setLabelVornameNachname();
        setLabelSchueler();
        setLabelSchuelerValue();
        setLabelAnmeldungValue();
        setLabelFruehereAnmeldungen();
        setLabelFruehereAnmeldungenValue();
        setLabelAbmeldedatum();
        setLabelAbmeldedatumValue();
        setLabelBemerkungenValue();
        setLabelGeburtsdatumValue();
        setLabelMutterValue();
        setLabelVaterValue();
        setLabelRechnungsempfaenger();
        setLabelRechnungsemfpaengerValue();
        setLabelSchuelerGleicherRechnungsemfpaengerValue();
        setLabelSchuelerGleicherRechnungsempfaenger1();
        setLabelSchuelerGleicherRechnungsempfaenger2();
        setLabelGeschwisterValue();
        setLabelNichtDispensiert();
        setLabelDispensationsdauer();
        setLabelDispensationsdauerValue();
        setLabelDispensationsgrund();
        setLabelDispensationsgrundValue();
        setLabelSchuelerCodesValue();
        setLabelFruehereDispensationenValue();
        setLabelSemesterKurseValue();
        setLabelKurseValue();
        setLabelMaerchenValue();
        setLabelGruppe();
        setLabelGruppeValue();
        setLabelRolle1();
        setLabelRolle1Value();
        setLabelBilderRolle1();
        setLabelBilderRolle1Value();
        setLabelRolle2();
        setLabelRolle2Value();
        setLabelBilderRolle2();
        setLabelBilderRolle2Value();
        setLabelRolle3();
        setLabelRolle3Value();
        setLabelBilderRolle3();
        setLabelBilderRolle3Value();
        setLabelElternmithilfe();
        setLabelElternmithilfeValue();
        setLabelElternmithilfeCode();
        setLabelElternmithilfeCodeValue();
        setLabelVorstellungenKuchen();
        setLabelVorstellungenKuchenValue();
        setLabelZusatzattribut();
        setLabelZusatzattributValue();
        setLabelBemerkungenMaerchen();
        setLabelBemerkungenMaerchenValue();
        setEmailEnabledDisabled();
    }

    private int convertRowIndexToModel() {
        if (schuelerSuchenResultTable == null) {
            return selectedRow;
        }
        return schuelerSuchenResultTable.convertRowIndexToModel(selectedRow);
    }

    private void enableScrollButtons() {
        enableBtnErster();
        enableBtnLetzter();
        enableBtnVorheriger();
        enableBtnNachfolgender();
    }

    private void enableBtnNachfolgender() {
        btnNachfolgender.setEnabled(selectedRow != (schuelerSuchenTableModel.getRowCount() - 1));
    }

    private void enableBtnVorheriger() {
        btnVorheriger.setEnabled(selectedRow != 0);
    }

    private void enableBtnLetzter() {
        btnLetzter.setEnabled(selectedRow != (schuelerSuchenTableModel.getRowCount() - 1));
    }

    private void enableBtnErster() {
        btnErster.setEnabled(selectedRow != 0);
    }

    public void setLabelVornameNachname(JLabel labelVornameNachname) {
        this.labelVornameNachname = labelVornameNachname;
        setLabelVornameNachname();
    }

    private void setLabelVornameNachname() {
        labelVornameNachname.setText(schuelerDatenblattModel.getSchuelerVorname() + " " + schuelerDatenblattModel.getSchuelerNachname());
    }

    public void setLabelSchueler(JLabel labelSchueler) {
        this.labelSchueler = labelSchueler;
        setLabelSchueler();
    }

    private void setLabelSchueler() {
        labelSchueler.setText(schuelerDatenblattModel.getLabelSchueler());
    }

    public void setLabelSchuelerValue(JLabel labelSchuelerValue) {
        this.labelSchuelerValue = labelSchuelerValue;
        setLabelSchuelerValue();
    }

    private void setLabelSchuelerValue() {
        labelSchuelerValue.setText(schuelerDatenblattModel.getSchuelerAsString());
    }

    public void setLabelMutterValue(JLabel labelMutterValue) {
        this.labelMutterValue = labelMutterValue;
        setLabelMutterValue();
    }

    private void setLabelMutterValue() {
        labelMutterValue.setText(schuelerDatenblattModel.getMutterAsString());
    }

    public void setLabelVaterValue(JLabel labelVaterValue) {
        this.labelVaterValue = labelVaterValue;
        setLabelVaterValue();
    }

    private void setLabelVaterValue() {
        labelVaterValue.setText(schuelerDatenblattModel.getVaterAsString());
    }

    public void setLabelRechnungsempfaenger(JLabel labelRechnungsempfaenger) {
        this.labelRechnungsempfaenger = labelRechnungsempfaenger;
        setLabelRechnungsempfaenger();
    }

    private void setLabelRechnungsempfaenger() {
        labelRechnungsempfaenger.setText(schuelerDatenblattModel.getLabelRechnungsempfaenger());
    }

    public void setLabelRechnungsempfaengerValue(JLabel labelRechnungsempfaengerValue) {
        this.labelRechnungsempfaengerValue = labelRechnungsempfaengerValue;
        setLabelRechnungsemfpaengerValue();
    }

    private void setLabelRechnungsemfpaengerValue() {
        labelRechnungsempfaengerValue.setText(schuelerDatenblattModel.getRechnungsempfaengerAsString());
    }

    public void setLabelGeschwisterValue(JLabel labelGeschwisterValue) {
        this.labelGeschwisterValue = labelGeschwisterValue;
        setLabelGeschwisterValue();
    }

    private void setLabelGeschwisterValue() {
        labelGeschwisterValue.setText(schuelerDatenblattModel.getGeschwisterAsString());
    }

    public void setLabelSchuelerGleicherRechnungsempfaenger1(JLabel labelSchuelerGleicherRechnungsempfaenger1) {
        this.labelSchuelerGleicherRechnungsempfaenger1 = labelSchuelerGleicherRechnungsempfaenger1;
        setLabelSchuelerGleicherRechnungsempfaenger1();
    }

    private void setLabelSchuelerGleicherRechnungsempfaenger1() {
        labelSchuelerGleicherRechnungsempfaenger1.setText(schuelerDatenblattModel.getLabelSchuelerGleicherRechnungsempfaenger1());
    }

    public void setLabelSchuelerGleicherRechnungsempfaenger2(JLabel labelSchuelerGleicherRechnungsempfaenger2) {
        this.labelSchuelerGleicherRechnungsempfaenger2 = labelSchuelerGleicherRechnungsempfaenger2;
        setLabelSchuelerGleicherRechnungsempfaenger2();
    }

    private void setLabelSchuelerGleicherRechnungsempfaenger2() {
        labelSchuelerGleicherRechnungsempfaenger2.setText(schuelerDatenblattModel.getLabelSchuelerGleicherRechnungsempfaenger2());
    }

    public void setLabelSchuelerGleicherRechnungsempfaengerValue(JLabel labelSchuelerGleicherRechnungsempfaengerValue) {
        this.labelSchuelerGleicherRechnungsempfaengerValue = labelSchuelerGleicherRechnungsempfaengerValue;
        setLabelSchuelerGleicherRechnungsemfpaengerValue();
    }

    private void setLabelSchuelerGleicherRechnungsemfpaengerValue() {
        labelSchuelerGleicherRechnungsempfaengerValue.setText(schuelerDatenblattModel.getSchuelerGleicherRechnungsempfaengerAsString());
    }

    public void setLabelGeburtsdatumValue(JLabel labelGeburtsdatumValue) {
        this.labelGeburtsdatumValue = labelGeburtsdatumValue;
        setLabelGeburtsdatumValue();
    }

    private void setLabelGeburtsdatumValue() {
        labelGeburtsdatumValue.setText(schuelerDatenblattModel.getSchuelerGeburtsdatumAsString());
    }

    public void setLabelAnmeldedatumValue(JLabel labelAnmeldungValue) {
        this.labelAnmeldungValue = labelAnmeldungValue;
        setLabelAnmeldungValue();
    }

    private void setLabelAnmeldungValue() {
        labelAnmeldungValue.setText(schuelerDatenblattModel.getAnmeldedatumAsString());
    }

    public void setLabelAbmeldedatum(JLabel labelAbmeldedatum) {
        this.labelAbmeldedatum = labelAbmeldedatum;
        setLabelAbmeldedatum();
    }

    private void setLabelAbmeldedatum() {
        if (schuelerDatenblattModel.getAbmeldedatumAsString().isEmpty()) {
            labelAbmeldedatum.setVisible(false);
        } else {
            labelAbmeldedatum.setVisible(true);
        }
    }

    public void setLabelAbmeldedatumValue(JLabel labelAbmeldedatumValue) {
        this.labelAbmeldedatumValue = labelAbmeldedatumValue;
        setLabelAbmeldedatumValue();
    }

    private void setLabelAbmeldedatumValue() {
        if (schuelerDatenblattModel.getAbmeldedatumAsString().isEmpty()) {
            labelAbmeldedatumValue.setVisible(false);
        } else {
            labelAbmeldedatumValue.setVisible(true);
        }
        labelAbmeldedatumValue.setText(schuelerDatenblattModel.getAbmeldedatumAsString());
    }

    public void setLabelFruehereAnmeldungen(JLabel labelFruehereAnmeldungen) {
        this.labelFruehereAnmeldungen = labelFruehereAnmeldungen;
        setLabelFruehereAnmeldungen();
    }

    private void setLabelFruehereAnmeldungen() {
        if (schuelerDatenblattModel.getFruehereAnmeldungenAsString().isEmpty()) {
            labelFruehereAnmeldungen.setVisible(false);
        } else {
            labelFruehereAnmeldungen.setVisible(true);
        }
    }

    public void setLabelFruehereAnmeldungenValue(JLabel labelFruehereAnmeldungenValue) {
        this.labelFruehereAnmeldungenValue = labelFruehereAnmeldungenValue;
        setLabelFruehereAnmeldungenValue();
    }

    private void setLabelFruehereAnmeldungenValue() {
        if (schuelerDatenblattModel.getFruehereAnmeldungenAsString().isEmpty()) {
            labelFruehereAnmeldungenValue.setVisible(false);
        } else {
            labelFruehereAnmeldungenValue.setVisible(true);
        }
        labelFruehereAnmeldungenValue.setText(schuelerDatenblattModel.getFruehereAnmeldungenAsString());
    }

    public void setLabelBemerkungenValue(JLabel labelBemerkungenValue) {
        this.labelBemerkungenValue = labelBemerkungenValue;
        setLabelBemerkungenValue();
    }

    private void setLabelBemerkungenValue() {
        labelBemerkungenValue.setText(schuelerDatenblattModel.getBemerkungen());
    }

    public void setLabelNichtDispensiert(JLabel labelNichtDispensiert) {
        this.labelNichtDispensiert = labelNichtDispensiert;
        setLabelNichtDispensiert();
    }

    private void setLabelNichtDispensiert() {
        if (!schuelerDatenblattModel.getDispensationsdauerAsString().isEmpty()) {
            labelNichtDispensiert.setVisible(false);
        } else {
            labelNichtDispensiert.setVisible(true);
        }
    }

    public void setLabelDispensationsdauer(JLabel labelDispensationsdauer) {
        this.labelDispensationsdauer = labelDispensationsdauer;
        setLabelDispensationsdauer();
    }

    private void setLabelDispensationsdauer() {
        if (schuelerDatenblattModel.getDispensationsdauerAsString().isEmpty()) {
            labelDispensationsdauer.setVisible(false);
        } else {
            labelDispensationsdauer.setVisible(true);
        }
    }

    public void setLabelDispensationsdauerValue(JLabel labelDispensationsdauerValue) {
        this.labelDispensationsdauerValue = labelDispensationsdauerValue;
        setLabelDispensationsdauerValue();
    }

    private void setLabelDispensationsdauerValue() {
        if (schuelerDatenblattModel.getDispensationsdauerAsString().isEmpty()) {
            labelDispensationsdauerValue.setVisible(false);
        } else {
            labelDispensationsdauerValue.setVisible(true);
            labelDispensationsdauerValue.setText(schuelerDatenblattModel.getDispensationsdauerAsString());
        }
    }

    public void setLabelDispensationsgrund(JLabel labelDispensationsgrund) {
        this.labelDispensationsgrund = labelDispensationsgrund;
        setLabelDispensationsgrund();
    }

    private void setLabelDispensationsgrund() {
        if (schuelerDatenblattModel.getDispensationsgrund().isEmpty()) {
            labelDispensationsgrund.setVisible(false);
        } else {
            labelDispensationsgrund.setVisible(true);
        }
    }

    public void setLabelDispensationsgrundValue(JLabel labelDispensationsgrundValue) {
        this.labelDispensationsgrundValue = labelDispensationsgrundValue;
        setLabelDispensationsgrundValue();
    }

    private void setLabelDispensationsgrundValue() {
        if (schuelerDatenblattModel.getDispensationsgrund().isEmpty()) {
            labelDispensationsgrundValue.setVisible(false);
        } else {
            labelDispensationsgrundValue.setVisible(true);
            labelDispensationsgrundValue.setText(schuelerDatenblattModel.getDispensationsgrund());
        }
    }

    public void setLabelFruehereDispensationenValue(JLabel labelFruehereDispensationenValue) {
        this.labelFruehereDispensationenValue = labelFruehereDispensationenValue;
        setLabelFruehereDispensationenValue();
    }

    private void setLabelFruehereDispensationenValue() {
        labelFruehereDispensationenValue.setText(schuelerDatenblattModel.getFruehereDispensationenAsString());
    }

    public void setLabelSchuelerCodesValue(JLabel labelCodesValue) {
        this.labelSchuelerCodesValue = labelCodesValue;
        setLabelSchuelerCodesValue();
    }

    private void setLabelSchuelerCodesValue() {
        labelSchuelerCodesValue.setText(schuelerDatenblattModel.getSchuelerCodesAsString());
    }

    public void setLabelSemesterKurseValue(JLabel labelSemesterkurseValue) {
        this.labelSemesterkurseValue = labelSemesterkurseValue;
        setLabelSemesterKurseValue();
    }

    private void setLabelSemesterKurseValue() {
        labelSemesterkurseValue.setText(schuelerDatenblattModel.getSemesterKurseAsString(svmContext.getSvmModel()));
    }

    public void setLabelKurseValue(JLabel labelKurseValue) {
        this.labelKurseValue = labelKurseValue;
        setLabelKurseValue();
    }

    private void setLabelKurseValue() {
        labelKurseValue.setText(schuelerDatenblattModel.getKurseAsString(svmContext.getSvmModel()));
    }

    public void setLabelMaerchenValue(JLabel labelMaerchenValue) {
        this.labelMaerchenValue = labelMaerchenValue;
        setLabelMaerchenValue();
    }

    private void setLabelMaerchenValue() {
        labelMaerchenValue.setText(schuelerDatenblattModel.getMaerchen());
    }

    public void setLabelGruppe(JLabel labelGruppe) {
        this.labelGruppe = labelGruppe;
        setLabelGruppe();
    }

    private void setLabelGruppe() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelGruppe.setVisible(false);
        } else {
            labelGruppe.setVisible(true);
        }
    }

    public void setLabelGruppeValue(JLabel labelGruppeValue) {
        this.labelGruppeValue = labelGruppeValue;
        setLabelGruppeValue();
    }

    private void setLabelGruppeValue() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelGruppeValue.setVisible(false);
        } else {
            labelGruppeValue.setVisible(true);
            labelGruppeValue.setText(schuelerDatenblattModel.getGruppe());
        }
    }

    public void setLabelRolle1(JLabel labelRolle1) {
        this.labelRolle1 = labelRolle1;
        setLabelRolle1();
    }

    private void setLabelRolle1() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelRolle1.setVisible(false);
        } else {
            labelRolle1.setVisible(true);
        }
    }

    public void setLabelRolle1Value(JLabel labelRolle1Value) {
        this.labelRolle1Value = labelRolle1Value;
        setLabelRolle1Value();
    }

    private void setLabelRolle1Value() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelRolle1Value.setVisible(false);
        } else {
            labelRolle1Value.setText(schuelerDatenblattModel.getRolle1());
            labelRolle1Value.setVisible(true);
        }
    }

    public void setLabelBilderRolle1(JLabel labelBilderRolle1) {
        this.labelBilderRolle1 = labelBilderRolle1;
        setLabelBilderRolle1();
    }

    private void setLabelBilderRolle1() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelBilderRolle1.setVisible(false);
        } else {
            labelBilderRolle1.setVisible(true);
        }
    }

    public void setLabelBilderRolle1Value(JLabel labelBilderRolle1Value) {
        this.labelBilderRolle1Value = labelBilderRolle1Value;
        setLabelBilderRolle1Value();
    }

    private void setLabelBilderRolle1Value() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelBilderRolle1Value.setVisible(false);
        } else {
            labelBilderRolle1Value.setText(schuelerDatenblattModel.getBilderRolle1());
            labelBilderRolle1Value.setVisible(true);
        }
    }

    public void setLabelRolle2(JLabel labelRolle2) {
        this.labelRolle2 = labelRolle2;
        setLabelRolle2();
    }

    private void setLabelRolle2() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelRolle2.setVisible(false);
        } else {
            labelRolle2.setVisible(true);
        }
    }

    public void setLabelRolle2Value(JLabel labelRolle2Value) {
        this.labelRolle2Value = labelRolle2Value;
        setLabelRolle2Value();
    }

    private void setLabelRolle2Value() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelRolle2Value.setVisible(false);
        } else {
            labelRolle2Value.setText(schuelerDatenblattModel.getRolle2());
            labelRolle2Value.setVisible(true);
        }
    }

    public void setLabelBilderRolle2(JLabel labelBilderRolle2) {
        this.labelBilderRolle2 = labelBilderRolle2;
        setLabelBilderRolle2();
    }

    private void setLabelBilderRolle2() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelBilderRolle2.setVisible(false);
        } else {
            labelBilderRolle2.setVisible(true);
        }
    }

    public void setLabelBilderRolle2Value(JLabel labelBilderRolle2Value) {
        this.labelBilderRolle2Value = labelBilderRolle2Value;
        setLabelBilderRolle2Value();
    }

    private void setLabelBilderRolle2Value() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelBilderRolle2Value.setVisible(false);
        } else {
            labelBilderRolle2Value.setText(schuelerDatenblattModel.getBilderRolle2());
            labelBilderRolle2Value.setVisible(true);
        }
    }

    public void setLabelRolle3(JLabel labelRolle3) {
        this.labelRolle3 = labelRolle3;
        setLabelRolle3();
    }

    private void setLabelRolle3() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelRolle3.setVisible(false);
        } else {
            labelRolle3.setVisible(true);
        }
    }

    public void setLabelRolle3Value(JLabel labelRolle3Value) {
        this.labelRolle3Value = labelRolle3Value;
        setLabelRolle3Value();
    }

    private void setLabelRolle3Value() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelRolle3Value.setVisible(false);
        } else {
            labelRolle3Value.setText(schuelerDatenblattModel.getRolle3());
            labelRolle3Value.setVisible(true);
        }
    }

    public void setLabelBilderRolle3(JLabel labelBilderRolle3) {
        this.labelBilderRolle3 = labelBilderRolle3;
        setLabelBilderRolle3();
    }

    private void setLabelBilderRolle3() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelBilderRolle3.setVisible(false);
        } else {
            labelBilderRolle3.setVisible(true);
        }
    }

    public void setLabelBilderRolle3Value(JLabel labelBilderRolle3Value) {
        this.labelBilderRolle3Value = labelBilderRolle3Value;
        setLabelBilderRolle3Value();
    }

    private void setLabelBilderRolle3Value() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelBilderRolle3Value.setVisible(false);
        } else {
            labelBilderRolle3Value.setText(schuelerDatenblattModel.getBilderRolle3());
            labelBilderRolle3Value.setVisible(true);
        }
    }

    public void setLabelElternmithilfe(JLabel labelElternmithilfe) {
        this.labelElternmithilfe = labelElternmithilfe;
        setLabelElternmithilfe();
    }

    private void setLabelElternmithilfe() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelElternmithilfe.setVisible(false);
        } else {
            labelElternmithilfe.setVisible(true);
        }
    }

    public void setLabelElternmithilfeValue(JLabel labelElternmithilfeValue) {
        this.labelElternmithilfeValue = labelElternmithilfeValue;
        setLabelElternmithilfeValue();
    }

    private void setLabelElternmithilfeValue() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelElternmithilfeValue.setVisible(false);
        } else {
            labelElternmithilfeValue.setText(schuelerDatenblattModel.getElternmithilfe());
            labelElternmithilfeValue.setVisible(true);
        }
    }

    public void setLabelElternmithilfeCode(JLabel labelElternmithilfeCode) {
        this.labelElternmithilfeCode = labelElternmithilfeCode;
        setLabelElternmithilfeCode();
    }

    private void setLabelElternmithilfeCode() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelElternmithilfeCode.setVisible(false);
        } else {
            labelElternmithilfeCode.setVisible(true);
        }
    }

    public void setLabelElternmithilfeCodeValue(JLabel labelElternmithilfeCodeValue) {
        this.labelElternmithilfeCodeValue = labelElternmithilfeCodeValue;
        setLabelElternmithilfeCodeValue();
    }

    private void setLabelElternmithilfeCodeValue() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelElternmithilfeCodeValue.setVisible(false);
        } else {
            labelElternmithilfeCodeValue.setText(schuelerDatenblattModel.getElternmithilfeCode());
            labelElternmithilfeCodeValue.setVisible(true);
        }
    }

    public void setLabelZusatzattribut(JLabel labelZusatzattribut) {
        this.labelZusatzattribut = labelZusatzattribut;
        setLabelZusatzattribut();
    }

    private void setLabelZusatzattribut() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelZusatzattribut.setVisible(false);
        } else {
            labelZusatzattribut.setVisible(true);
        }
    }

    public void setLabelZusatzattributValue(JLabel labelZusatzattributValue) {
        this.labelZusatzattributValue = labelZusatzattributValue;
        setLabelZusatzattributValue();
    }

    private void setLabelZusatzattributValue() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelZusatzattributValue.setVisible(false);
        } else {
            labelZusatzattributValue.setText(schuelerDatenblattModel.getZusatzattribut());
            labelZusatzattributValue.setVisible(true);
        }
    }

    public void setLabelVorstellungenKuchen(JLabel labelVorstellungenKuchen) {
        this.labelVorstellungenKuchen = labelVorstellungenKuchen;
        setLabelVorstellungenKuchen();
    }

    private void setLabelVorstellungenKuchen() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelVorstellungenKuchen.setVisible(false);
        } else {
            labelVorstellungenKuchen.setVisible(true);
        }
    }

    public void setLabelVorstellungenKuchenValue(JLabel labelVorstellungenKuchenValue) {
        this.labelVorstellungenKuchenValue = labelVorstellungenKuchenValue;
        setLabelVorstellungenKuchenValue();
    }

    private void setLabelVorstellungenKuchenValue() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelVorstellungenKuchenValue.setVisible(false);
        } else {
            labelVorstellungenKuchenValue.setText(schuelerDatenblattModel.getKuchenVorstellungenAsString());
            labelVorstellungenKuchenValue.setVisible(true);
        }
    }

    public void setLabelBemerkungenMaerchen(JLabel labelBemerkungenMaerchen) {
        this.labelBemerkungenMaerchen = labelBemerkungenMaerchen;
        setLabelBemerkungenMaerchen();
    }

    private void setLabelBemerkungenMaerchen() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelBemerkungenMaerchen.setVisible(false);
        } else {
            labelBemerkungenMaerchen.setVisible(true);
        }
    }

    public void setLabelBemerkungenMaerchenValue(JLabel labelBemerkungenMaerchenValue) {
        this.labelBemerkungenMaerchenValue = labelBemerkungenMaerchenValue;
        setLabelBemerkungenMaerchenValue();
    }

    private void setLabelBemerkungenMaerchenValue() {
        if (schuelerDatenblattModel.getMaerchen().equals("-")) {
            labelBemerkungenMaerchenValue.setVisible(false);
        } else {
            labelBemerkungenMaerchenValue.setText(schuelerDatenblattModel.getBemerkungenMaerchen());
            labelBemerkungenMaerchenValue.setVisible(true);
        }
    }

    public void setBtnZurueck(JButton btnZurueck) {
        this.btnZurueck = btnZurueck;
        if (!isFromSchuelerSuchenResult) {
            btnZurueck.setVisible(false);
            return;
        }
        btnZurueck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onZurueck();
            }
        });
    }

    private void onZurueck() {
        if (schuelerSuchenTableModel.getRowCount() > 1) {
            schuelerDatenblattModel.refreshSchuelerSuchenTableData(svmContext, schuelerSuchenTableModel);
            SchuelerSuchenResultPanel schuelerSuchenResultPanel = new SchuelerSuchenResultPanel(svmContext, schuelerSuchenTableModel);
            schuelerSuchenResultPanel.addNextPanelListener(nextPanelListener);
            schuelerSuchenResultPanel.addCloseListener(closeListener);
            schuelerSuchenResultPanel.addZurueckListener(zurueckZuSchuelerSuchenListener);
            nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerSuchenResultPanel.$$$getRootComponent$$$(), "Suchresultat"}, ActionEvent.ACTION_PERFORMED, "Suchresultat"));
        } else {
            zurueckZuSchuelerSuchenListener.actionPerformed(new ActionEvent(btnZurueck, ActionEvent.ACTION_PERFORMED, "Zurück zu Schüler suchen"));
        }
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        this.btnAbbrechen = btnAbbrechen;
        if (isFromSchuelerSuchenResult) {
            btnAbbrechen.setVisible(false);
            return;
        }
        btnAbbrechen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        });
    }

    private void onAbbrechen() {
        closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Abbrechen"));
    }

    public void setBtnErster(JButton btnErster) {
        if (!isFromSchuelerSuchenResult) {
            btnErster.setVisible(false);
            return;
        }
        this.btnErster = btnErster;
        btnErster.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onErster();
            }
        });
        enableBtnErster();
    }

    private void onErster() {
        scroll(0);
    }

    public void setBtnLetzter(JButton btnLetzter) {
        if (!isFromSchuelerSuchenResult) {
            btnLetzter.setVisible(false);
            return;
        }
        this.btnLetzter = btnLetzter;
        btnLetzter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLetzter();
            }
        });
        enableBtnLetzter();
    }

    private void onLetzter() {
        scroll(schuelerSuchenTableModel.getRowCount() - 1);
    }

    public void setBtnNachfolgender(JButton btnNachfolgender) {
        if (!isFromSchuelerSuchenResult) {
            btnNachfolgender.setVisible(false);
            return;
        }
        this.btnNachfolgender = btnNachfolgender;
        btnNachfolgender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNachfolgender();
            }
        });
        enableBtnNachfolgender();
    }

    private void onNachfolgender() {
        scroll(selectedRow + 1);
    }

    public void setBtnVorheriger(JButton btnVorheriger) {
        if (!isFromSchuelerSuchenResult) {
            btnVorheriger.setVisible(false);
            return;
        }
        this.btnVorheriger = btnVorheriger;
        btnVorheriger.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onVorheriger();
            }
        });
        enableBtnVorheriger();
    }

    private void onVorheriger() {
        scroll(selectedRow - 1);
    }

    public void setBtnStammdatenBearbeiten(JButton btnStammdatenBearbeiten) {
        btnStammdatenBearbeiten.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStammdatenBearbeiten();
            }
        });
    }

    private void onStammdatenBearbeiten() {
        SchuelerErfassenPanel schuelerErfassenPanel = new SchuelerErfassenPanel(svmContext, schuelerDatenblattModel);
        schuelerErfassenPanel.addCloseListener(closeListener);
        schuelerErfassenPanel.addZurueckZuDatenblattListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSaveSuccessful();
            }
        });
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerErfassenPanel.$$$getRootComponent$$$(), "Stammdaten bearbeiten"}, ActionEvent.ACTION_PERFORMED, "Schueler bearbeiten"));
    }

    public void setBtnEmail(JButton btnEmail) {
        this.btnEmail = btnEmail;
        setEmailEnabledDisabled();
        btnEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEmail();
            }
        });
    }

    public void setEmailEnabledDisabled() {
        if (!schuelerDatenblattModel.checkIfStammdatenMitEmail()) {
            this.btnEmail.setEnabled(false);
            return;
        }
        this.btnEmail.setEnabled(true);
    }

    private void onEmail() {
        btnEmail.setFocusPainted(true);
        EmailDialog emailDialog = new EmailDialog(svmContext, schuelerDatenblattModel);
        emailDialog.pack();
        emailDialog.setVisible(true);
        btnEmail.setFocusPainted(false);
    }

    private void onSaveSuccessful() {
        schuelerSuchenTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
        SchuelerDatenblattPanel schuelerDatenblattPanel = new SchuelerDatenblattPanel(svmContext, schuelerSuchenTableModel, schuelerSuchenResultTable, selectedRow, isFromSchuelerSuchenResult);
        schuelerDatenblattPanel.addCloseListener(closeListener);
        schuelerDatenblattPanel.addNextPanelListener(nextPanelListener);
        schuelerDatenblattPanel.addZurueckZuSchuelerSuchenListener(zurueckZuSchuelerSuchenListener);
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerDatenblattPanel.$$$getRootComponent$$$(), "Datenblatt"}, ActionEvent.ACTION_PERFORMED, "Schüler gespeichert"));
    }

    public void setBtnDispensationenBearbeiten(JButton btnDispensationenBearbeiten) {
        btnDispensationenBearbeiten.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDispensationenBearbeiten();
            }
        });
    }

    private void onDispensationenBearbeiten() {
        DispensationenTableModel dispensationenTableModel = new DispensationenTableModel(schuelerDatenblattModel.getDispensationenTableData());
        DispensationenPanel dispensationenPanel = new DispensationenPanel(svmContext, dispensationenTableModel, schuelerDatenblattModel, schuelerSuchenTableModel, schuelerSuchenResultTable, selectedRow, isFromSchuelerSuchenResult);
        dispensationenPanel.addNextPanelListener(nextPanelListener);
        dispensationenPanel.addCloseListener(closeListener);
        dispensationenPanel.addZurueckZuSchuelerSuchenListener(zurueckZuSchuelerSuchenListener);
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{dispensationenPanel.$$$getRootComponent$$$(), "Dispensationen"}, ActionEvent.ACTION_PERFORMED, "Dispensationen"));
    }

    public void setBtnCodesBearbeiten(JButton btnCodesBearbeiten) {
        btnCodesBearbeiten.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCodesBearbeiten();
            }
        });
    }

    private void onCodesBearbeiten() {
        CodesTableModel codesTableModel = new CodesTableModel(schuelerDatenblattModel.getCodesTableData());
        CodesPanel codesPanel = new CodesPanel(svmContext, codesTableModel, schuelerDatenblattModel, schuelerSuchenTableModel, schuelerSuchenResultTable, selectedRow, true, isFromSchuelerSuchenResult, Codetyp.SCHUELER);
        codesPanel.addNextPanelListener(nextPanelListener);
        codesPanel.addCloseListener(closeListener);
        codesPanel.addZurueckZuSchuelerSuchenListener(zurueckZuSchuelerSuchenListener);
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{codesPanel.$$$getRootComponent$$$(), "Codes"}, ActionEvent.ACTION_PERFORMED, "Codes"));
    }

    public void setBtnKurseBearbeiten(JButton btnKurseBearbeiten) {
        btnKurseBearbeiten.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onKurseBearbeiten();
            }
        });
    }

    private void onKurseBearbeiten() {
        KurseTableModel kurseTableModel = new KurseTableModel(schuelerDatenblattModel.getKurseTableData());
        String titel = "Kurse " + schuelerDatenblattModel.getSchuelerVorname() + " " + schuelerDatenblattModel.getSchuelerNachname();
        KursePanel kursePanel = new KursePanel(svmContext, null, kurseTableModel, schuelerDatenblattModel, schuelerSuchenTableModel, schuelerSuchenResultTable, selectedRow, true, isFromSchuelerSuchenResult, titel);
        kursePanel.addNextPanelListener(nextPanelListener);
        kursePanel.addCloseListener(closeListener);
        kursePanel.addZurueckZuSchuelerSuchenListener(zurueckZuSchuelerSuchenListener);
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{kursePanel.$$$getRootComponent$$$(), "Kurse"}, ActionEvent.ACTION_PERFORMED, "Kurse"));
    }

    public void setBtnMaerchenBearbeiten(JButton btnMaerchenBearbeiten) {
        btnMaerchenBearbeiten.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onMaerchenBearbeiten();
            }
        });
    }

    private void onMaerchenBearbeiten() {
        MaercheneinteilungenTableModel maercheneinteilungenTableModel = new MaercheneinteilungenTableModel(schuelerDatenblattModel.getMaercheneinteilungenTableData());
        String titel = "Märcheneinteilungen " + schuelerDatenblattModel.getSchuelerVorname() + " " + schuelerDatenblattModel.getSchuelerNachname();
        MaercheneinteilungenPanel maercheneinteilungenPanel = new MaercheneinteilungenPanel(svmContext, maercheneinteilungenTableModel, schuelerDatenblattModel, schuelerSuchenTableModel, schuelerSuchenResultTable, selectedRow, isFromSchuelerSuchenResult, titel);
        maercheneinteilungenPanel.addNextPanelListener(nextPanelListener);
        maercheneinteilungenPanel.addCloseListener(closeListener);
        maercheneinteilungenPanel.addZurueckZuSchuelerSuchenListener(zurueckZuSchuelerSuchenListener);
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{maercheneinteilungenPanel.$$$getRootComponent$$$(), "Märcheneinteilungen"}, ActionEvent.ACTION_PERFORMED, "Märcheneinteilungen"));
    }

    public void setLabelScrollPosition(JLabel labelScrollPosition) {
        if (!isFromSchuelerSuchenResult) {
            labelScrollPosition.setVisible(false);
            return;
        }
        this.labelScrollPosition = labelScrollPosition;
        setLabelScrollPosition();
    }

    private void setLabelScrollPosition() {
        labelScrollPosition.setText((selectedRow + 1) + " / " + schuelerSuchenTableModel.getRowCount());
    }

    public void addNextPanelListener(ActionListener nextPanelListener) {
        this.nextPanelListener = nextPanelListener;
    }

    public void addCloseListener(ActionListener closeListener) {
        this.closeListener = closeListener;
    }

    public void addZurueckZuSchuelerSuchenListener(ActionListener zurueckZuSchuelerSuchenListener) {
        this.zurueckZuSchuelerSuchenListener = zurueckZuSchuelerSuchenListener;
    }

}
