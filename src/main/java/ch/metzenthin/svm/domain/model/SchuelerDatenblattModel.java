package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.persistence.entities.Dispensation;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.List;

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
    String getDispensationsdauerAsString();
    String getDispensationsgrund();
    String getFruehereDispensationenAsString();
    DispensationenTableData getDispensationenTableData();
    SchuelerModel getSchuelerModel(SvmContext svmContext);
    AngehoerigerModel getMutterModel(SvmContext svmContext);
    AngehoerigerModel getVaterModel(SvmContext svmContext);
    AngehoerigerModel getRechnungsempfaengerModel(SvmContext svmContext);
    Schueler getSchueler();
}
