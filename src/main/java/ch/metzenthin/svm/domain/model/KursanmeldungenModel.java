package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.ui.componentmodel.KursanmeldungenTableModel;

import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface KursanmeldungenModel extends Model {

    KursanmeldungErfassenModel getKursanmeldungErfassenModel(SvmContext svmContext, KursanmeldungenTableModel kursanmeldungenTableModel, int rowSelected);

    void kursanmeldungLoeschen(KursanmeldungenTableModel kursanmeldungenTableModel, SchuelerDatenblattModel schuelerDatenblattModel, int rowSelected);

    Calendar getSpaetestesAbmeldedatumKurseNeustesSemester(SchuelerDatenblattModel schuelerDatenblattModel);

    void schuelerVomKinderUndJugendtheaterAbmelden(SchuelerDatenblattModel schuelerDatenblattModel, Calendar abmeldedatum);

    boolean isSchuelerAbgemeldet(SchuelerDatenblattModel schuelerDatenblattModel);
}
