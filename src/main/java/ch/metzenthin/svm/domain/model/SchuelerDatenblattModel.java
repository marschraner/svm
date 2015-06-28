package ch.metzenthin.svm.domain.model;

import java.util.Calendar;

/**
 * @author Hans Stamm
 */
public interface SchuelerDatenblattModel {
    String getNachname();
    String getVorname();
    Calendar getGeburtsdatum();
}
