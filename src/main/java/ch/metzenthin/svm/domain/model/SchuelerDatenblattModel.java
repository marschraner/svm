package ch.metzenthin.svm.domain.model;

import java.util.Calendar;

/**
 * @author Hans Stamm
 */
public interface SchuelerDatenblattModel {
    String getSchuelerNachname();
    String getSchuelerVorname();
    String getSchuelerAsString();
    String getMutterAsString();
    String getVaterAsString();
    String getRechnungsempfaengerAsString();
    String getGeschwisterAsString();
    String getSchuelerGleicherRechnungsempfaengerAsString();
    String getAnmeldungenAsString();
}
