package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;

import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface AnmeldungenStatistikModel extends Model {

    enum AnAbmeldungenSelected {
        ANMELDUNGEN,
        ABMELDUNGEN
    }

    Calendar getAnAbmeldemonat();
    AnAbmeldungenSelected getAnAbmeldungen();
    SchuelerSuchenResult suchen();

    void setAnAbmeldemonat(String anAbmeldemonat) throws SvmValidationException;
    void setAnAbmeldungen(AnAbmeldungenSelected anAbmeldungen);
}
