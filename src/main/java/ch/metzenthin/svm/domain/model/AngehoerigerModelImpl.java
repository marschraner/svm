package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Person;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

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

    AngehoerigerModelImpl() {
        angehoeriger = new Angehoeriger();
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
    public Boolean getWuenschtEmails() {
        return angehoeriger.getWuenschtEmails();
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
    public void setWuenschtEmails(Boolean isSelected) {
        Boolean oldValue = angehoeriger.getWuenschtEmails();
        angehoeriger.setWuenschtEmails(isSelected);
        firePropertyChange(Field.WUENSCHT_EMAILS, oldValue, isSelected);
    }

    @Override
    public void setIsRechnungsempfaenger(boolean isSelected) {
        boolean oldValue = isRechnungsempfaenger;
        isRechnungsempfaenger = isSelected;
        firePropertyChange(Field.RECHNUNGSEMPFAENGER, oldValue, isRechnungsempfaenger);
    }

    @Override
    public void setAngehoeriger(Angehoeriger angehoeriger, boolean isGleicheAdresseWieSchueler,
            boolean isRechnungsempfaenger) {
        angehoerigerOrigin = angehoeriger;
        isGleicheAdresseWieSchuelerOrigin = isGleicheAdresseWieSchueler;
        isRechnungsempfaengerOrigin = isRechnungsempfaenger;
    }

    @Override
    public boolean isCompleted() {
        if (isSetName() && angehoeriger.getWuenschtEmails() != null
            && angehoeriger.getWuenschtEmails() && !checkNotEmpty(angehoeriger.getEmail())) {
            return false;
        }
        return super.isCompleted();
    }

    @Override
    public void doValidate() throws SvmValidationException {
        if (isSetName() && angehoeriger.getWuenschtEmails() != null
            && angehoeriger.getWuenschtEmails() && !checkNotEmpty(angehoeriger.getEmail())) {
            throw new SvmValidationException(2010,
                    "Wenn \"Wünscht E-Mails\" selektiert ist, darf die E-Mail nicht leer sein",
                    Field.EMAIL);
        }
        super.doValidate();
    }

    @Override
    public void initializeCompleted() {
        if (angehoerigerOrigin != null) {
            setBulkUpdate(true);
            try {
                setAnrede(angehoerigerOrigin.getAnrede());
                setNachname(angehoerigerOrigin.getNachname());
                setVorname(angehoerigerOrigin.getVorname());
                if (angehoerigerOrigin.getAdresse() != null) {
                    setStrasseHausnummer(angehoerigerOrigin.getAdresse().getStrasseHausnummer());
                    setPlz(angehoerigerOrigin.getAdresse().getPlz());
                    setOrt(angehoerigerOrigin.getAdresse().getOrt());
                }
                setFestnetz(angehoerigerOrigin.getFestnetz());
                setNatel(angehoerigerOrigin.getNatel());
                setEmail(angehoerigerOrigin.getEmail());
                setGeburtsdatum(asString(angehoerigerOrigin.getGeburtsdatum()));
                isRechnungsempfaenger = !isRechnungsempfaengerOrigin; // damit PropertyChange ausgelöst wird!
                setIsRechnungsempfaenger(isRechnungsempfaengerOrigin);
                isGleicheAdresseWieSchueler = !isGleicheAdresseWieSchuelerOrigin; // damit PropertyChange ausgelöst wird!
                setIsGleicheAdresseWieSchueler(isGleicheAdresseWieSchuelerOrigin);
                setWuenschtEmails(angehoerigerOrigin.getWuenschtEmails());
            } catch (SvmValidationException ignore) {
            }
            setBulkUpdate(false);
        } else {
            super.initializeCompleted();
        }
    }

    @Override
    public void clear() {
        setBulkUpdate(true);
        try {
            setIsRechnungsempfaenger(false);
            setIsGleicheAdresseWieSchueler(false);
            setWuenschtEmails(null);
            setAnrede(Anrede.FRAU);
            setNachname("");
            setVorname("");
            setStrasseHausnummer("");
            setPlz("");
            setOrt("");
            setFestnetz("");
            setNatel("");
            setEmail("");
            setGeburtsdatum("");
        } catch (SvmValidationException e) {
            throw new RuntimeException("Keine SvmValidationException erwartet: ", e);
        }
        setBulkUpdate(false);
    }

}
