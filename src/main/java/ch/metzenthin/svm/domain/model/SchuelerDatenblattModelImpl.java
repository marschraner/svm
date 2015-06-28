package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.Calendar;

/**
 * @author Hans Stamm
 */
public class SchuelerDatenblattModelImpl implements SchuelerDatenblattModel {

    private final Schueler schueler;

    public SchuelerDatenblattModelImpl(Schueler schueler) {
        this.schueler = schueler;
    }

    @Override
    public String getNachname() {
        return schueler.getNachname();
    }

    @Override
    public String getVorname() {
        return schueler.getVorname();
    }

    @Override
    public Calendar getGeburtsdatum() {
        return schueler.getGeburtsdatum();
    }

}
