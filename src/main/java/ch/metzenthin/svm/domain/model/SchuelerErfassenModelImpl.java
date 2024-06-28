package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;
import static ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand.Entry.NEU_ERFASSTEN_SCHUELER_VALIDIEREN;

/**
 * Dieses Model ist eigentlich Model und Controller (verwaltet die Submodels Schüler, Mutter, Vater,
 * Rechnungsempfänger).
 *
 * @author Hans Stamm
 */
public class SchuelerErfassenModelImpl extends AbstractModel implements SchuelerErfassenModel, ValidateSchuelerModel {

    private static final Logger LOGGER = LogManager.getLogger(SchuelerErfassenModelImpl.class);

    private SchuelerModel schuelerModel;
    private AngehoerigerModel mutterModel;
    private AngehoerigerModel vaterModel;
    private AngehoerigerModel drittempfaengerModel;

    private boolean isSchuelerModelCompleted;
    private boolean isMutterModelCompleted;
    private boolean isVaterModelCompleted;
    private boolean isDrittempfaengerModelCompleted;

    @Override
    public void initializeCompleted() {
        schuelerModel.initializeCompleted();
        mutterModel.initializeCompleted();
        vaterModel.initializeCompleted();
        drittempfaengerModel.initializeCompleted();
    }

    @Override
    public boolean isCompleted() {
        boolean completed = isSubModelsCompleted();
        LOGGER.trace("SchuelerErfassenModel isCompleted={}", completed);
        return completed;
    }

    private boolean isSubModelsCompleted() {
        return isSchuelerModelCompleted && isMutterModelCompleted && isVaterModelCompleted && isDrittempfaengerModelCompleted;
    }

    /**
     * Überschrieben, damit Observer dieses Modells nicht informiert wird. Er muss die Exception fangen und entsprechend handeln.
     */
    @Override
    public void validate() throws SvmValidationException {
        doValidate();
    }

    @Override
    public void doValidate() throws SvmValidationException {
        if (isSubModelsCompleted()) {
            if (!isSetRechnungsempfaenger()) {
                throw new SvmValidationException(3000, "Rechnungsempfänger ist nicht gesetzt",
                        Field.RECHNUNGSEMPFAENGER);
            }
            if (isSetEmails() && !isSetWuenschtEmails()) {
                throw new SvmValidationException(3001,
                        "Für mindestens eine Elternperson muss \"Wünscht E-Mails\" selektiert sein",
                        Field.WUENSCHT_EMAILS);
            }
        }
    }

    private boolean isSetRechnungsempfaenger() {
        return mutterModel.isRechnungsempfaenger() || vaterModel.isRechnungsempfaenger()
                || drittempfaengerModel.isRechnungsempfaenger();
    }

    private boolean isSetEmails() {
        return checkNotEmpty(mutterModel.getEmail()) || checkNotEmpty(vaterModel.getEmail());
    }

    private boolean isSetWuenschtEmails() {
        return (mutterModel.getWuenschtEmails() != null && mutterModel.getWuenschtEmails())
                || (vaterModel.getWuenschtEmails() != null && vaterModel.getWuenschtEmails());
    }

    @Override
    public void setSchuelerModel(SchuelerModel schuelerModel) {
        this.schuelerModel = schuelerModel;
        this.schuelerModel.addCompletedListener(this::onSchuelerModelCompleted);
        this.schuelerModel.addPropertyChangeListener(this::onSchuelerModelPropertyChange);
    }

    @Override
    public void setMutterModel(AngehoerigerModel mutterModel) {
        this.mutterModel = mutterModel;
        this.mutterModel.addCompletedListener(this::onMutterModelCompleted);
        this.mutterModel.addPropertyChangeListener(this::onMutterModelPropertyChange);
    }

    @Override
    public void setVaterModel(AngehoerigerModel vaterModel) {
        this.vaterModel = vaterModel;
        this.vaterModel.addCompletedListener(this::onVaterModelCompleted);
        this.vaterModel.addPropertyChangeListener(this::onVaterModelPropertyChange);
    }

    @Override
    public void setDrittempfaengerModel(AngehoerigerModel drittempfaengerModel) {
        this.drittempfaengerModel = drittempfaengerModel;
        this.drittempfaengerModel.addCompletedListener(this::onDrittempfaengerModelCompleted);
        this.drittempfaengerModel.addPropertyChangeListener(this::onDrittempfaengerModelPropertyChange);
    }

    @Override
    public boolean isEmptyNachnameMutter() {
        return mutterModel.getAngehoeriger() == null
                || !checkNotEmpty(mutterModel.getAngehoeriger().getNachname());
    }

    @Override
    public boolean isEmptyNachnameVater() {
        return vaterModel.getAngehoeriger() == null
                || !checkNotEmpty(vaterModel.getAngehoeriger().getNachname());
    }

    private void onSchuelerModelPropertyChange(PropertyChangeEvent evt) {
        if (isStrasseHausnummerPropertyChange(evt)) {
            if (mutterModel.isGleicheAdresseWieSchueler()) {
                replaceByStrasseHausnummerSchueler(mutterModel);
            }
            if (vaterModel.isGleicheAdresseWieSchueler()) {
                replaceByStrasseHausnummerSchueler(vaterModel);
            }
        }
        if (isPlzPropertyChange(evt)) {
            if (mutterModel.isGleicheAdresseWieSchueler()) {
                replaceByPlzSchueler(mutterModel);
            }
            if (vaterModel.isGleicheAdresseWieSchueler()) {
                replaceByPlzSchueler(vaterModel);
            }
        }
        if (isOrtPropertyChange(evt)) {
            if (mutterModel.isGleicheAdresseWieSchueler()) {
                replaceByOrtSchueler(mutterModel);
            }
            if (vaterModel.isGleicheAdresseWieSchueler()) {
                replaceByOrtSchueler(vaterModel);
            }
        }
        if (isFestnetzPropertyChange(evt)) {
            if (mutterModel.isGleicheAdresseWieSchueler()) {
                replaceByFestnetzSchueler(mutterModel);
            }
            if (vaterModel.isGleicheAdresseWieSchueler()) {
                replaceByFestnetzSchueler(vaterModel);
            }
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private void onMutterModelPropertyChange(PropertyChangeEvent evt) {
        if (isRechnungsempfaengerPropertyChange(evt)) {
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                uncheckRechnungsempfaenger(vaterModel, drittempfaengerModel);
                drittempfaengerModel.disableFields();
                drittempfaengerModel.makeErrorLabelsInvisible(getFieldAlleInSet());
            } else {
                mutterModel.makeErrorLabelsInvisible(getFieldAlleInSet());
                if (!vaterModel.isRechnungsempfaenger()) {
                    drittempfaengerModel.setIsRechnungsempfaenger(true);
                    drittempfaengerModel.enableFields();
                }
            }
        } else if (isGleicheAdresseWieSchuelerPropertyChange(evt)) {
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                replaceByAdresseSchueler(mutterModel);
                mutterModel.disableFields(getAdresseFields());
                mutterModel.makeErrorLabelsInvisible(getAdresseFields());
            } else {
                invalidateAdresse(mutterModel);
                mutterModel.enableFields(getAdresseFields());
            }
        } else {
            makeWuenschtEmailsErrorLabelsInvisibleIfRequired(evt);
        }
        fireCompletedChange();
    }

    private void makeWuenschtEmailsErrorLabelsInvisibleIfRequired(PropertyChangeEvent evt) {
        if (isWuenschtEmailsPropertyChange(evt)) {
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                Set<Field> fields = new HashSet<>();
                fields.add(Field.WUENSCHT_EMAILS);
                makeErrorLabelsInvisible(fields);
            }
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private void onVaterModelPropertyChange(PropertyChangeEvent evt) {
        if (isRechnungsempfaengerPropertyChange(evt)) {
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                uncheckRechnungsempfaenger(mutterModel, drittempfaengerModel);
                drittempfaengerModel.disableFields();
                drittempfaengerModel.makeErrorLabelsInvisible(getFieldAlleInSet());
            } else {
                vaterModel.makeErrorLabelsInvisible(getFieldAlleInSet());
                if (!mutterModel.isRechnungsempfaenger()) {
                    drittempfaengerModel.setIsRechnungsempfaenger(true);
                    drittempfaengerModel.enableFields();
                }
            }
        } else if (isGleicheAdresseWieSchuelerPropertyChange(evt)) {
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                replaceByAdresseSchueler(vaterModel);
                vaterModel.disableFields(getAdresseFields());
                vaterModel.makeErrorLabelsInvisible(getAdresseFields());
            } else {
                invalidateAdresse(vaterModel);
                vaterModel.enableFields(getAdresseFields());
            }
        } else {
            makeWuenschtEmailsErrorLabelsInvisibleIfRequired(evt);
        }
        fireCompletedChange();
    }

    private void onDrittempfaengerModelPropertyChange(PropertyChangeEvent evt) {
        if (isRechnungsempfaengerPropertyChange(evt)) {
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                uncheckRechnungsempfaenger(mutterModel, vaterModel);
            } else {
                drittempfaengerModel.clear();
            }
        }
        fireCompletedChange();
    }

    private boolean isStrasseHausnummerPropertyChange(PropertyChangeEvent evt) {
        return checkIsFieldChange(Field.STRASSE_HAUSNUMMER, evt);
    }

    private boolean isPlzPropertyChange(PropertyChangeEvent evt) {
        return checkIsFieldChange(Field.PLZ, evt);
    }

    private boolean isOrtPropertyChange(PropertyChangeEvent evt) {
        return checkIsFieldChange(Field.ORT, evt);
    }

    private boolean isFestnetzPropertyChange(PropertyChangeEvent evt) {
        return checkIsFieldChange(Field.FESTNETZ, evt);
    }

    private boolean isRechnungsempfaengerPropertyChange(PropertyChangeEvent evt) {
        return checkIsFieldChange(Field.RECHNUNGSEMPFAENGER, evt);
    }

    private boolean isGleicheAdresseWieSchuelerPropertyChange(PropertyChangeEvent evt) {
        return checkIsFieldChange(Field.GLEICHE_ADRESSE_WIE_SCHUELER, evt);
    }

    private boolean isWuenschtEmailsPropertyChange(PropertyChangeEvent evt) {
        return checkIsFieldChange(Field.WUENSCHT_EMAILS, evt);
    }

    private boolean isBooleanNewValuePropertyChecked(Boolean newValue) {
        return (newValue != null) && newValue;
    }

    private void uncheckRechnungsempfaenger(AngehoerigerModel... angehoerigerModels) {
        for (AngehoerigerModel angehoerigerModel : angehoerigerModels) {
            angehoerigerModel.setIsRechnungsempfaenger(false);
        }
    }

    private void replaceByAdresseSchueler(AngehoerigerModel angehoerigerModel) {
        angehoerigerModel.initAdresse(schuelerModel);
    }

    private void invalidateAdresse(AngehoerigerModel angehoerigerModel) {
        angehoerigerModel.initAdresse(null);
    }

    @SuppressWarnings("DuplicatedCode")
    private static Set<Field> getAdresseFields() {
        Set<Field> adresseFields = new HashSet<>();
        adresseFields.add(Field.STRASSE_HAUSNUMMER);
        adresseFields.add(Field.PLZ);
        adresseFields.add(Field.ORT);
        adresseFields.add(Field.FESTNETZ);
        return adresseFields;
    }

    private Set<Field> getFieldAlleInSet() {
        return new HashSet<>(Collections.singletonList(Field.ALLE));
    }

    private void replaceByStrasseHausnummerSchueler(AngehoerigerModel angehoerigerModel) {
        try {
            angehoerigerModel.setStrasseHausnummer(schuelerModel.getStrasseHausnummer());
        } catch (SvmRequiredException e) {
            LOGGER.trace("SchuelerErfassenController replaceByStrasseHausnummerSchueler RequiredException={}", e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
        } catch (SvmValidationException e) {
            // Tritt nie ein, da Validierung bereits beim Schüler
        }
    }

    private void replaceByPlzSchueler(AngehoerigerModel angehoerigerModel) {
        try {
            angehoerigerModel.setPlz(schuelerModel.getPlz());
        } catch (SvmRequiredException e) {
            LOGGER.trace("SchuelerErfassenController replaceByPlzSchueler RequiredException={}", e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
        } catch (SvmValidationException e) {
            // Tritt nie ein, da Validierung bereits beim Schüler
        }
    }

    private void replaceByOrtSchueler(AngehoerigerModel angehoerigerModel) {
        try {
            angehoerigerModel.setOrt(schuelerModel.getOrt());
        } catch (SvmRequiredException e) {
            LOGGER.trace("SchuelerErfassenController replaceByOrtSchueler RequiredException={}", e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
        } catch (SvmValidationException e) {
            // Tritt nie ein, da Validierung bereits beim Schüler
        }
    }

    private void replaceByFestnetzSchueler(AngehoerigerModel angehoerigerModel) {
        try {
            angehoerigerModel.setFestnetz(schuelerModel.getFestnetz());
        } catch (SvmRequiredException e) {
            LOGGER.trace("SchuelerErfassenController replaceByFestnetzSchueler RequiredException={}", e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
        } catch (SvmValidationException e) {
            // Tritt nie ein, da Validierung bereits beim Schüler
        }
    }

    private void onSchuelerModelCompleted(boolean completed) {
        LOGGER.trace("SchuelerModel completed={}", completed);
        isSchuelerModelCompleted = completed;
        fireCompletedChange();
    }

    private void onMutterModelCompleted(boolean completed) {
        LOGGER.trace("MutterModel completed={}", completed);
        isMutterModelCompleted = completed;
        fireCompletedChange();
    }

    private void onVaterModelCompleted(boolean completed) {
        LOGGER.trace("VaterModel completed={}", completed);
        isVaterModelCompleted = completed;
        fireCompletedChange();
    }

    private void onDrittempfaengerModelCompleted(boolean completed) {
        LOGGER.trace("DrittempfaengerModel completed={}", completed);
        isDrittempfaengerModelCompleted = completed;
        fireCompletedChange();
    }

    private void fireCompletedChange() {
        fireCompleted(isCompleted());
    }

    private ValidateSchuelerCommand validateSchuelerCommand;

    @Override
    public SchuelerErfassenSaveResult validieren(SvmContext svmContext) {
        LOGGER.trace("SchuelerErfassenModel validieren");

        validateSchuelerCommand = new ValidateSchuelerCommand(this);
        validateSchuelerCommand.setEntry(NEU_ERFASSTEN_SCHUELER_VALIDIEREN);
        return executeCommandWithinSession();
    }

    @Override
    public SchuelerErfassenSaveResult proceedUebernehmen(SchuelerErfassenSaveResult schuelerErfassenSaveResult) {
        // Eigentlich müsste man das Model durch das neue Objekt, das übernommen wird, ersetzen. Darauf verzichten
        // wir im Moment.
        validateSchuelerCommand.setEntry(schuelerErfassenSaveResult.getResult().proceedUebernehmen());
        return executeCommandWithinSession();
    }

    @Override
    public SchuelerErfassenSaveResult proceedWeiterfahren(SchuelerErfassenSaveResult schuelerErfassenSaveResult) {
        validateSchuelerCommand.setEntry(schuelerErfassenSaveResult.getResult().proceedWeiterfahren());
        return executeCommandWithinSession();
    }

    private SchuelerErfassenSaveResult executeCommandWithinSession() {
        getCommandInvoker().executeCommand(validateSchuelerCommand);
        return validateSchuelerCommand.getResult();
    }

    @Override
    public SchuelerErfassenSaveResult speichern(SchuelerErfassenSaveResult schuelerErfassenSaveResult) {
        validateSchuelerCommand.setEntry(ValidateSchuelerCommand.Entry.SUMMARY_BESTAETIGT);
        CommandInvoker commandInvoker = getCommandInvoker();
        try {
            commandInvoker.executeCommandAsTransaction(validateSchuelerCommand);
            return validateSchuelerCommand.getResult();
        } catch (Throwable e) {
            // Rollback wurde bereits in CommandInvoker durchgeführt
            return new SchuelerErfassenUnerwarteterFehlerResult(ValidateSchuelerCommand.Result.UNERWARTETER_FEHLER, e);
        } finally {
            validateSchuelerCommand = null;
        }
    }

    @Override
    public DeleteSchuelerCommand.Result schuelerLoeschen() {
        DeleteSchuelerCommand deleteSchuelerCommand = new DeleteSchuelerCommand(getSchuelerOrigin());
        CommandInvoker commandInvoker = getCommandInvoker();
        try {
            commandInvoker.executeCommandAsTransaction(deleteSchuelerCommand);
            return deleteSchuelerCommand.getResult();
        } catch (Throwable e) {
            // Rollback wurde bereits in CommandInvoker durchgeführt
            throw new RuntimeException(e);
        }
    }

    @Override
    public void fruehereAnmeldungenLoeschen() {
        RemoveFruehereAnmeldungenFromSchuelerCommand removeFruehereAnmeldungenFromSchuelerCommand = new RemoveFruehereAnmeldungenFromSchuelerCommand(getSchuelerOrigin());
        getCommandInvoker().executeCommandAsTransaction(removeFruehereAnmeldungenFromSchuelerCommand);
    }

    @Override
    public boolean hasFruehereAnmeldungen() {
        return getSchuelerOrigin().getAnmeldungen().size() > 1;
    }

    @Override
    public void abbrechen() {
        validateSchuelerCommand = null;
    }

    @Override
    public Schueler getSchueler() {
        return schuelerModel.getSchueler();
    }

    @Override
    public Schueler getSchuelerOrigin() {
        return schuelerModel.getSchuelerOrigin();
    }

    @Override
    public Adresse getAdresseSchueler() {
        return schuelerModel.getAdresse();
    }

    @Override
    public Anmeldung getAnmeldung() {
        return schuelerModel.getAnmeldung();
    }

    @Override
    public Angehoeriger getMutter() {
        if (mutterModel.isEmpty()) {
            return null;
        }
        return mutterModel.getAngehoeriger();
    }

    @Override
    public Adresse getAdresseMutter() {
        return mutterModel.getAdresse();
    }

    @Override
    public boolean isRechnungsempfaengerMutter() {
        return mutterModel.isRechnungsempfaenger();
    }

    @Override
    public Angehoeriger getVater() {
        if (vaterModel.isEmpty()) {
            return null;
        }
        return vaterModel.getAngehoeriger();
    }

    @Override
    public Adresse getAdresseVater() {
        return vaterModel.getAdresse();
    }

    @Override
    public boolean isRechnungsempfaengerVater() {
        return vaterModel.isRechnungsempfaenger();
    }

    @Override
    public Angehoeriger getRechnungsempfaengerDrittperson() {
        if (drittempfaengerModel.isEmpty()) {
            return null;
        }
        return drittempfaengerModel.getAngehoeriger();
    }

    @Override
    public Adresse getAdresseRechnungsempfaengerDrittperson() {
        return drittempfaengerModel.getAdresse();
    }

    @Override
    public boolean isRechnungsempfaengerDrittperson() {
        return drittempfaengerModel.isRechnungsempfaenger();
    }

    @Override
    public SchuelerSuchenTableData getSchuelerSuchenTableData() {
        return new SchuelerSuchenTableData(Collections.singletonList(getSchueler()), new HashMap<>(),
                null, null, null, null, null, null, new HashMap<>(), null, null, null, false, false, false, null, false);
    }

}
