package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Angehoeriger;

/**
 * @author Hans Stamm
 */
public interface AngehoerigerModel extends PersonModel {

    Angehoeriger getAngehoeriger();

    boolean isGleicheAdresseWieSchueler();

    void setIsGleicheAdresseWieSchueler(boolean isSelected);

    boolean isRechnungsempfaenger();

    void setIsRechnungsempfaenger(boolean isSelected);
}
