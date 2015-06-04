package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Hans Stamm
 */
public interface ValidateSchuelerModel {
    Schueler getSchueler();
    Adresse getAdresseSchueler();
    Angehoeriger getMutter();
    Adresse getAdresseMutter();
    boolean isRechnungsempfaengerMutter();
    Angehoeriger getVater();
    Adresse getAdresseVater();
    boolean isRechnungsempfaengerVater();
    Angehoeriger getRechnungsempfaengerDrittperson();
    Adresse getAdresseRechnungsempfaengerDrittperson();
    boolean isRechnungsempfaengerDrittperson();
}
