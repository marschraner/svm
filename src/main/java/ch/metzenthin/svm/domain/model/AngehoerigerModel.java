package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Angehoeriger;

/**
 * @author Hans Stamm
 */
public interface AngehoerigerModel extends PersonModel {

    Angehoeriger getAngehoeriger();

    boolean isGleicheAdresseWieSchueler();

    Boolean getWuenschtEmails();

    boolean isRechnungsempfaenger();

    void setIsGleicheAdresseWieSchueler(boolean isSelected);

    void setWuenschtEmails(Boolean isSelected);

    void setIsRechnungsempfaenger(boolean isSelected);

    void setAngehoeriger(Angehoeriger angehoeriger, boolean isGleicheAdresseWieSchueler,
                         boolean isRechnungsempfaenger);

    void clear();
}
