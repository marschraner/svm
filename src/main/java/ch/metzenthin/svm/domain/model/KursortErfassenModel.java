package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Kursort;

/**
 * @author Martin Schraner
 */
public interface KursortErfassenModel extends Model {
    void setKursortOrigin(Kursort kursortOrigin);

    String getBezeichnung();
    Boolean isSelektierbar();
    Kursort getKursort();

    void setBezeichnung(String bezeichnung) throws SvmValidationException;
    void setSelektierbar(Boolean isSelected);

    boolean checkKursortBezeichnungBereitsInVerwendung(SvmModel svmModel);
    void speichern(SvmModel svmModel);
}
