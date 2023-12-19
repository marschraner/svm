package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.ui.componentmodel.KurstypenTableModel;

/**
 * @author Martin Schraner
 */
public interface KurstypErfassenModel extends Model {

    void setKurstypOrigin(Kurstyp kurstypOrigin);

    String getBezeichnung();

    Boolean isSelektierbar();

    Kurstyp getKurstyp();

    void setBezeichnung(String bezeichnung) throws SvmValidationException;

    void setSelektierbar(Boolean isSelected);

    boolean checkKurstypBezeichnungBereitsInVerwendung(SvmModel svmModel);

    void speichern(SvmModel svmModel, KurstypenTableModel kurstypenTableModel);
}
