package ch.metzenthin.svm.domain.model;

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
}
