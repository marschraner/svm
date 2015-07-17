package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;
import ch.metzenthin.svm.ui.componentmodel.DispensationenTableModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
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
    private int selectedRow;
    private SchuelerDatenblattModel schuelerDatenblattModel;
    private JButton btnErster;
    private JButton btnLetzter;
    private JButton btnNachfolgender;
    private JButton btnVorheriger;
    private JButton btnZurueck;
    private JButton btnAbbrechen;
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
    private JLabel labelCodesValue;
    private JLabel labelScrollPosition;
    private ActionListener nextPanelListener;
    private ActionListener closeListener;
    private ActionListener zurueckZuSchuelerSuchenListener;
    private boolean isFromSchuelerSuchenResult;

    public SchuelerDatenblattController(SvmContext svmContext, SchuelerSuchenTableModel schuelerSuchenTableModel, int selectedRow, boolean isFromSchuelerSuchenResult) {
        this.svmContext = svmContext;
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.selectedRow = selectedRow;
        this.isFromSchuelerSuchenResult = isFromSchuelerSuchenResult;
        schuelerDatenblattModel = schuelerSuchenTableModel.getSchuelerDatenblattModel(selectedRow);
    }

    private void scroll(int selectedRow) {
        if ((this.selectedRow == selectedRow) || (selectedRow < 0) || (selectedRow >= schuelerSuchenTableModel.getRowCount())) {
            return;
        }
        this.selectedRow = selectedRow;
        setNewModel();
    }

    private void setNewModel() {
        schuelerDatenblattModel = schuelerSuchenTableModel.getSchuelerDatenblattModel(selectedRow);
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
        setLabelCodesValue();
        setLabelFruehereDispensationenValue();
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

    public void setLabelCodesValue(JLabel labelCodesValue) {
        this.labelCodesValue = labelCodesValue;
        setLabelCodesValue();
    }

    private void setLabelCodesValue() {
        labelCodesValue.setText(schuelerDatenblattModel.getCodesAsString());
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

    private void onSaveSuccessful() {
        schuelerSuchenTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
        SchuelerDatenblattPanel schuelerDatenblattPanel = new SchuelerDatenblattPanel(svmContext, schuelerSuchenTableModel, selectedRow, isFromSchuelerSuchenResult);
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
        DispensationenPanel dispensationenPanel = new DispensationenPanel(svmContext, dispensationenTableModel, schuelerDatenblattModel, schuelerSuchenTableModel, selectedRow, isFromSchuelerSuchenResult);
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
        CodesPanel codesPanel = new CodesPanel(svmContext, codesTableModel, schuelerDatenblattModel, schuelerSuchenTableModel, selectedRow, true, isFromSchuelerSuchenResult);
        codesPanel.addNextPanelListener(nextPanelListener);
        codesPanel.addCloseListener(closeListener);
        codesPanel.addZurueckZuSchuelerSuchenListener(zurueckZuSchuelerSuchenListener);
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{codesPanel.$$$getRootComponent$$$(), "Codes"}, ActionEvent.ACTION_PERFORMED, "Codes"));
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
