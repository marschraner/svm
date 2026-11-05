package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.components.SchuelerDatenblattPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * @author Hans Stamm
 */
public class SchuelerDatenblattView extends AbstractView<SchuelerDatenblattPanel> {

  // Stammdaten
  private final JLabel lblSchueler;
  private final JLabel lblSchuelerValue;
  private final JLabel lblMutterValue;
  private final JLabel lblVaterValue;
  private final JLabel lblRechnungsempfaenger;
  private final JLabel lblRechnungsempfaengerValue;
  private final JLabel lblGeschwisterValue;
  private final JLabel lblSchuelerGleicherRechnungsempfaenger1;
  private final JLabel lblSchuelerGleicherRechnungsempfaengerValue;
  private final JLabel lblSchuelerGleicherRechnungsempfaenger2;
  private final JLabel lblGeburtsdatumValue;
  private final JLabel lblAnmeldedatumValue;
  private final JLabel lblAbmeldedatum;
  private final JLabel lblAbmeldedatumValue;
  private final JLabel lblFruehereAnmeldungen;
  private final JLabel lblFruehereAnmeldungenValue;
  private final JLabel lblBemerkungenValue;
  private final JButton btnStammdatenBearbeiten;

  // Dispensationen
  private final JLabel lblNichtDispensiert;
  private final JLabel lblDispensationsdauer;
  private final JLabel lblDispensationsdauerValue;
  private final JLabel lblDispensationsgrund;
  private final JLabel lblDispensationsgrundValue;
  private final JLabel lblFruehereDispensationenValue;
  private final JButton btnDispensationenBearbeiten;

  // Schülercodes
  private final JLabel lblSchuelerCodesValue;
  private final JButton btnCodesBearbeiten;

  // Kurse
  private final JLabel lblSemesterKurseValue;
  private final JLabel lblKurseValue;
  private final JButton btnKurseBearbeiten;

  // Märchen
  private final JLabel lblMaerchenValue;
  private final JLabel lblRolle1;
  private final JLabel lblRolle1Value;
  private final JLabel lblRolle2;
  private final JLabel lblRolle2Value;
  private final JLabel lblRolle3;
  private final JLabel lblRolle3Value;
  private final JLabel lblElternmithilfe;
  private final JLabel lblElternmithilfeValue;
  private final JLabel lblVorstellungenKuchen;
  private final JLabel lblVorstellungenKuchenValue;
  private final JLabel lblBemerkungenMaerchen;
  private final JLabel lblBemerkungenMaerchenValue;
  private final JLabel lblGruppe;
  private final JLabel lblGruppeValue;
  private final JLabel lblBilderRolle1;
  private final JLabel lblBilderRolle1Value;
  private final JLabel lblBilderRolle2;
  private final JLabel lblBilderRolle2Value;
  private final JLabel lblBilderRolle3;
  private final JLabel lblBilderRolle3Value;
  private final JLabel lblElternmithilfeCode;
  private final JLabel lblElternmithilfeCodeValue;
  private final JLabel lblZusatzattribut;
  private final JLabel lblZusatzattributValue;
  private final JButton btnMaerchenBearbeiten;

  // Navigation
  private final JLabel lblScrollPosition;
  private final JButton btnErster;
  private final JButton btnVorheriger;
  private final JButton btnNachfolgender;
  private final JButton btnLetzter;
  private final JButton btnZurueck;
  private final JButton btnAbbrechen;

  public SchuelerDatenblattView() {
    super(new SchuelerDatenblattPanel());
    this.lblSchueler = component.getLblSchueler();
    this.lblSchuelerValue = component.getLblSchuelerValue();
    this.lblMutterValue = component.getLblMutterValue();
    this.lblVaterValue = component.getLblVaterValue();
    this.lblRechnungsempfaenger = component.getLblRechnungsempfaenger();
    this.lblRechnungsempfaengerValue = component.getLblRechnungsempfaengerValue();
    this.lblGeschwisterValue = component.getLblGeschwisterValue();
    this.lblSchuelerGleicherRechnungsempfaenger1 =
        component.getLblSchuelerGleicherRechnungsempfaenger1();
    this.lblSchuelerGleicherRechnungsempfaengerValue =
        component.getLblSchuelerGleicherRechnungsempfaengerValue();
    this.lblSchuelerGleicherRechnungsempfaenger2 =
        component.getLblSchuelerGleicherRechnungsempfaenger2();
    this.lblGeburtsdatumValue = component.getLblGeburtsdatumValue();
    this.lblAnmeldedatumValue = component.getLblAnmeldedatumValue();
    this.lblAbmeldedatum = component.getLblAbmeldedatum();
    this.lblAbmeldedatumValue = component.getLblAbmeldedatumValue();
    this.lblFruehereAnmeldungen = component.getLblFruehereAnmeldungen();
    this.lblFruehereAnmeldungenValue = component.getLblFruehereAnmeldungenValue();
    this.lblBemerkungenValue = component.getLblBemerkungenValue();
    this.btnStammdatenBearbeiten = component.getBtnStammdatenBearbeiten();
    this.lblNichtDispensiert = component.getLblNichtDispensiert();
    this.lblDispensationsdauer = component.getLblDispensationsdauer();
    this.lblDispensationsdauerValue = component.getLblDispensationsdauerValue();
    this.lblDispensationsgrund = component.getLblDispensationsgrund();
    this.lblDispensationsgrundValue = component.getLblDispensationsgrundValue();
    this.lblFruehereDispensationenValue = component.getLblFruehereDispensationenValue();
    this.btnDispensationenBearbeiten = component.getBtnDispensationenBearbeiten();
    this.lblSchuelerCodesValue = component.getLblSchuelerCodesValue();
    this.btnCodesBearbeiten = component.getBtnCodesBearbeiten();
    this.lblSemesterKurseValue = component.getLblSemesterKurseValue();
    this.lblKurseValue = component.getLblKurseValue();
    this.btnKurseBearbeiten = component.getBtnKurseBearbeiten();
    this.lblMaerchenValue = component.getLblMaerchenValue();
    this.lblRolle1 = component.getLblRolle1();
    this.lblRolle1Value = component.getLblRolle1Value();
    this.lblRolle2 = component.getLblRolle2();
    this.lblRolle2Value = component.getLblRolle2Value();
    this.lblRolle3 = component.getLblRolle3();
    this.lblRolle3Value = component.getLblRolle3Value();
    this.lblElternmithilfe = component.getLblElternmithilfe();
    this.lblElternmithilfeValue = component.getLblElternmithilfeValue();
    this.lblVorstellungenKuchen = component.getLblVorstellungenKuchen();
    this.lblVorstellungenKuchenValue = component.getLblVorstellungenKuchenValue();
    this.lblBemerkungenMaerchen = component.getLblBemerkungenMaerchen();
    this.lblBemerkungenMaerchenValue = component.getLblBemerkungenMaerchenValue();
    this.lblGruppe = component.getLblGruppe();
    this.lblGruppeValue = component.getLblGruppeValue();
    this.lblBilderRolle1 = component.getLblBilderRolle1();
    this.lblBilderRolle1Value = component.getLblBilderRolle1Value();
    this.lblBilderRolle2 = component.getLblBilderRolle2();
    this.lblBilderRolle2Value = component.getLblBilderRolle2Value();
    this.lblBilderRolle3 = component.getLblBilderRolle3();
    this.lblBilderRolle3Value = component.getLblBilderRolle3Value();
    this.lblElternmithilfeCode = component.getLblElternmithilfeCode();
    this.lblElternmithilfeCodeValue = component.getLblElternmithilfeCodeValue();
    this.lblZusatzattribut = component.getLblZusatzattribut();
    this.lblZusatzattributValue = component.getLblZusatzattributValue();
    this.btnMaerchenBearbeiten = component.getBtnMaerchenBearbeiten();
    this.lblScrollPosition = component.getLblScrollPosition();
    this.btnErster = component.getBtnErster();
    this.btnVorheriger = component.getBtnVorheriger();
    this.btnNachfolgender = component.getBtnNachfolgender();
    this.btnLetzter = component.getBtnLetzter();
    this.btnZurueck = component.getBtnZurueck();
    this.btnAbbrechen = component.getBtnAbbrechen();
  }
}
