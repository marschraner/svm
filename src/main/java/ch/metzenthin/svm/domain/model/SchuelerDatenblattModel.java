package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;

/**
 * @author Hans Stamm
 */
public interface SchuelerDatenblattModel {

  String getSchuelerNachname();

  String getSchuelerVorname();

  String getLabelSchueler();

  String getSchuelerAsString();

  String getMutterAsString();

  String getVaterAsString();

  String getLabelRechnungsempfaenger();

  String getRechnungsempfaengerAsString();

  String getGeschwisterAsString();

  String getLabelSchuelerGleicherRechnungsempfaenger1();

  String getLabelSchuelerGleicherRechnungsempfaenger2();

  String getSchuelerGleicherRechnungsempfaengerAsString();

  String getSchuelerGeburtsdatumAsString();

  String getAnmeldedatumAsString();

  String getAbmeldedatumAsString();

  String getFruehereAnmeldungenAsString();

  String getBemerkungen();

  String getDispensationsdauerAsString();

  String getDispensationsgrund();

  String getFruehereDispensationenAsString();

  String getSchuelerCodesAsString();

  String getSemesterKurseAsString(SvmModel svmModel);

  String getKurseAsString(SvmModel svmModel);

  String getMaerchen();

  String getGruppe();

  String getRolle1();

  String getBilderRolle1();

  String getRolle2();

  String getBilderRolle2();

  String getRolle3();

  String getBilderRolle3();

  String getElternmithilfe();

  String getElternmithilfeCode();

  String getKuchenVorstellungenAsString();

  String getZusatzattribut();

  String getBemerkungenMaerchen();

  DispensationenTableData getDispensationenTableData();

  CodesTableData getCodesTableData();

  KursanmeldungenTableData getKurseinteilungenTableData();

  MaercheneinteilungenTableData getMaercheneinteilungenTableData();

  SchuelerModel getSchuelerModel(SvmContext svmContext);

  AngehoerigerModel getMutterModel(SvmContext svmContext);

  AngehoerigerModel getVaterModel(SvmContext svmContext);

  AngehoerigerModel getRechnungsempfaengerModel(SvmContext svmContext);

  Schueler getSchueler();

  void refreshSchuelerSuchenTableData(
      SvmContext svmContext, SchuelerSuchenTableModel schuelerSuchenTableModel);

  boolean checkIfStammdatenMitEmail();
}
