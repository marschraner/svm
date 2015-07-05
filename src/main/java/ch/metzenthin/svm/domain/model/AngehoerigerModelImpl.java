package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Person;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Hans Stamm
 */
public class AngehoerigerModelImpl extends PersonModelImpl implements AngehoerigerModel {

    private final Angehoeriger angehoeriger;
    private Angehoeriger angehoerigerOrigin;
    private boolean isRechnungsempfaengerOrigin;
    private boolean isGleicheAdresseWieSchuelerOrigin;

    private boolean isGleicheAdresseWieSchueler;
    private boolean isRechnungsempfaenger;

    public AngehoerigerModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
        angehoeriger = new Angehoeriger();
    }

    @Override
    public boolean isCompleted() {
        return super.isCompleted();
    }

    @Override
    public void doValidate() throws SvmValidationException {
        super.doValidate();
    }

    @Override
    Person getPerson() {
        return angehoeriger;
    }

    @Override
    public Angehoeriger getAngehoeriger() {
        return angehoeriger;
    }

    @Override
    public boolean isGleicheAdresseWieSchueler() {
        return isGleicheAdresseWieSchueler;
    }

    @Override
    public boolean isRechnungsempfaenger() {
        return isRechnungsempfaenger;
    }

    /**
     * Für Angehörige ist die Adresse obligatorisch, falls er Rechnungsempfänger ist.
     * @return true, falls der Angehörige Rechnungsempfänger ist
     */
    @Override
    public boolean isAdresseRequired() {
        return isRechnungsempfaenger;
    }

    @Override
    public void setIsGleicheAdresseWieSchueler(boolean isSelected) {
        boolean oldValue = isGleicheAdresseWieSchueler;
        isGleicheAdresseWieSchueler = isSelected;
        firePropertyChange(Field.GLEICHE_ADRESSE_WIE_SCHUELER, oldValue, isGleicheAdresseWieSchueler);
    }

    @Override
    public void setIsRechnungsempfaenger(boolean isSelected) {
        boolean oldValue = isRechnungsempfaenger;
        isRechnungsempfaenger = isSelected;
        firePropertyChange(Field.RECHNUNGSEMPFAENGER, oldValue, isRechnungsempfaenger);
    }

    @Override
    public void setAngehoeriger(Angehoeriger angehoeriger, boolean isGleicheAdresseWieSchueler, boolean isRechnungsempfaenger) {
        angehoerigerOrigin = angehoeriger;
        isGleicheAdresseWieSchuelerOrigin = isGleicheAdresseWieSchueler;
        isRechnungsempfaengerOrigin = isRechnungsempfaenger;
    }


    @Override
    public void initializeCompleted() {
        if (angehoerigerOrigin != null) {
            try {
                setAnrede(angehoerigerOrigin.getAnrede());
                setNachname(angehoerigerOrigin.getNachname());
                setVorname(angehoerigerOrigin.getVorname());
                if (angehoerigerOrigin.getAdresse() != null) {
                    setStrasseHausnummer(angehoerigerOrigin.getAdresse().getStrasseHausnummer());
                    setPlz(angehoerigerOrigin.getAdresse().getPlz());
                    setOrt(angehoerigerOrigin.getAdresse().getOrt());
                    setFestnetz(angehoerigerOrigin.getAdresse().getFestnetz());
                }
                setNatel(angehoerigerOrigin.getNatel());
                setEmail(angehoerigerOrigin.getEmail());
                setGeburtsdatum(asString(angehoerigerOrigin.getGeburtsdatum()));
                isRechnungsempfaenger = !isRechnungsempfaengerOrigin; // damit PropertyChange ausgelöst wird!
                setIsRechnungsempfaenger(isRechnungsempfaengerOrigin);
                isGleicheAdresseWieSchueler = !isGleicheAdresseWieSchuelerOrigin; // damit PropertyChange ausgelöst wird!
                setIsGleicheAdresseWieSchueler(isGleicheAdresseWieSchuelerOrigin);
            } catch (SvmValidationException ignore) {
            }
        }
        super.initializeCompleted();
    }

}
