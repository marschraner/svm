package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.FieldName;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.domain.commands.ValidateSchuelerModel;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.ui.control.CompletedListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import static ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand.Entry.NEU_ERFASSTEN_SCHUELER_VALIDIEREN;

/**
 * Dieses Model ist eigentlich Model und Controller (verwaltet die Submodels Schüler, Mutter, Vater, Rechnungsempfänger).
 *
 * @author Hans Stamm
 */
public class SchuelerErfassenModelImpl extends AbstractModel implements SchuelerErfassenModel, ValidateSchuelerModel {

    private SchuelerModel schuelerModel;
    private AngehoerigerModel mutterModel;
    private AngehoerigerModel vaterModel;
    private AngehoerigerModel drittempfaengerModel;

    private boolean isSchuelerModelCompleted;
    private boolean isMutterModelCompleted;
    private boolean isVaterModelCompleted;
    private boolean isDrittempfaengerModelCompleted;

    SchuelerErfassenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

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
        System.out.println("SchuelerErfassenModel isCompleted=" + completed);
        return completed;
    }

    private boolean isSubModelsCompleted() {
        return isSchuelerModelCompleted && isMutterModelCompleted && isVaterModelCompleted && isDrittempfaengerModelCompleted;
    }

    /**
     * Überschrieben, damit Observer dieses Modells nicht informiert wird. Er muss die Exception fangen und entsprechend handeln.
     *
     * @throws SvmValidationException
     */
    @Override
    public void validate() throws SvmValidationException {
        doValidate();
    }

    @Override
    public void doValidate() throws SvmValidationException {
        if (isSubModelsCompleted()) {
            if (!isSetRechnungsempfaenger()) {
                throw new SvmValidationException(3000, "Rechnungsempfänger ist nicht gesetzt", "Rechnungsempfaenger");
            }
        }
    }

    private boolean isSetRechnungsempfaenger() {
        return mutterModel.isRechnungsempfaenger() || vaterModel.isRechnungsempfaenger() || drittempfaengerModel.isRechnungsempfaenger();
        //return isMutterRechnungsempfaenger || isVaterRechnungsempfaenger || isDrittempfaengerRechnungsempfaenger;
    }

    @Override
    public void setSchuelerModel(SchuelerModel schuelerModel) {
        this.schuelerModel = schuelerModel;
        this.schuelerModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onSchuelerModelCompleted(completed);
            }
        });
        this.schuelerModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                onSchuelerModelPropertyChange(evt);
            }
        });
    }

    @Override
    public void setMutterModel(AngehoerigerModel mutterModel) {
        this.mutterModel = mutterModel;
        this.mutterModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onMutterModelCompleted(completed);
            }
        });
        this.mutterModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                onMutterModelPropertyChange(evt);
            }
        });
    }

    @Override
    public void setVaterModel(AngehoerigerModel vaterModel) {
        this.vaterModel = vaterModel;
        this.vaterModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onVaterModelCompleted(completed);
            }
        });
        this.vaterModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                onVaterModelPropertyChange(evt);
            }
        });
    }

    @Override
    public void setDrittempfaengerModel(AngehoerigerModel drittempfaengerModel) {
        this.drittempfaengerModel = drittempfaengerModel;
        this.drittempfaengerModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onDrittempfaengerModelCompleted(completed);
            }
        });
        this.drittempfaengerModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                onDrittempfaengerModelPropertyChange(evt);
            }
        });
    }

    // todo schöner programmieren (nächste vier methoden)
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

    private void onMutterModelPropertyChange(PropertyChangeEvent evt) {
        if (isRechnungsempfaengerPropertyChange(evt)) {
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                uncheckRechnungsempfaenger(vaterModel, drittempfaengerModel);
                drittempfaengerModel.disableFields();
            } else {
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
            } else {
                invalidateAdresse(mutterModel);
                mutterModel.enableFields(getAdresseFields());
            }
        }
        fireCompletedChange();
    }

    private void onVaterModelPropertyChange(PropertyChangeEvent evt) {
        if (isRechnungsempfaengerPropertyChange(evt)) {
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                uncheckRechnungsempfaenger(mutterModel, drittempfaengerModel);
                drittempfaengerModel.disableFields();
            } else {
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
            } else {
                invalidateAdresse(vaterModel);
                vaterModel.enableFields(getAdresseFields());
            }
        }
        fireCompletedChange();
    }

    private void onDrittempfaengerModelPropertyChange(PropertyChangeEvent evt) {
        if (isRechnungsempfaengerPropertyChange(evt)) {
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                uncheckRechnungsempfaenger(mutterModel, vaterModel);
            }
        }
        fireCompletedChange();
    }

    private boolean isStrasseHausnummerPropertyChange(PropertyChangeEvent evt) {
        return "StrasseHausnummer".equals(evt.getPropertyName());
    }

    private boolean isPlzPropertyChange(PropertyChangeEvent evt) {
        return "Plz".equals(evt.getPropertyName());
    }

    private boolean isOrtPropertyChange(PropertyChangeEvent evt) {
        return "Ort".equals(evt.getPropertyName());
    }

    private boolean isFestnetzPropertyChange(PropertyChangeEvent evt) {
        return "Festnetz".equals(evt.getPropertyName());
    }

    private boolean isRechnungsempfaengerPropertyChange(PropertyChangeEvent evt) {
        return "Rechnungsempfaenger".equals(evt.getPropertyName());
    }

    private boolean isGleicheAdresseWieSchuelerPropertyChange(PropertyChangeEvent evt) {
        return "GleicheAdresseWieSchueler".equals(evt.getPropertyName());
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
        replaceByStrasseHausnummerSchueler(angehoerigerModel);
        replaceByPlzSchueler(angehoerigerModel);
        replaceByOrtSchueler(angehoerigerModel);
        replaceByFestnetzSchueler(angehoerigerModel);
    }

    private void invalidateAdresse(AngehoerigerModel angehoerigerModel) {
        invalidateStrasseHausnummer(angehoerigerModel);
        invalidatePlz(angehoerigerModel);
        invalidateOrt(angehoerigerModel);
        invalidateFestnetz(angehoerigerModel);
    }

    private Set<FieldName> getAdresseFields() {
        Set<FieldName> adresseFields = new HashSet<>();
        adresseFields.add(FieldName.STRASSE_HAUSNUMMER);
        adresseFields.add(FieldName.PLZ);
        adresseFields.add(FieldName.ORT);
        adresseFields.add(FieldName.FESTNETZ);
        return adresseFields;
    }

    private void replaceByStrasseHausnummerSchueler(AngehoerigerModel angehoerigerModel) {
        try {
            angehoerigerModel.setStrasseHausnummer(schuelerModel.getStrasseHausnummer());
        } catch (SvmRequiredException e) {
            System.out.println("SchuelerErfassenController replaceByStrasseHausnummerSchueler RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
        } catch (SvmValidationException e) {
            // Tritt nie ein, da Validierung bereits beim Schüler
        }
    }

    private void replaceByPlzSchueler(AngehoerigerModel angehoerigerModel) {
        try {
            angehoerigerModel.setPlz(schuelerModel.getPlz());
        } catch (SvmRequiredException e) {
            System.out.println("SchuelerErfassenController replaceByPlzSchueler RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
        } catch (SvmValidationException e) {
            // Tritt nie ein, da Validierung bereits beim Schüler
        }
    }

    private void replaceByOrtSchueler(AngehoerigerModel angehoerigerModel) {
        try {
            angehoerigerModel.setOrt(schuelerModel.getOrt());
        } catch (SvmRequiredException e) {
            System.out.println("SchuelerErfassenController replaceByOrtSchueler RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
        } catch (SvmValidationException e) {
            // Tritt nie ein, da Validierung bereits beim Schüler
        }
    }

    private void replaceByFestnetzSchueler(AngehoerigerModel angehoerigerModel) {
        try {
            angehoerigerModel.setFestnetz(schuelerModel.getFestnetz());
        } catch (SvmRequiredException e) {
            System.out.println("SchuelerErfassenController replaceByFestnetzSchueler RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
        } catch (SvmValidationException e) {
            // Tritt nie ein, da Validierung bereits beim Schüler
        }
    }

    private void invalidateStrasseHausnummer(AngehoerigerModel angehoerigerModel) {
        try {
            angehoerigerModel.setStrasseHausnummer(null);
        } catch (SvmValidationException e) {
            // Nicht weiter behandeln
        }
    }

    private void invalidatePlz(AngehoerigerModel angehoerigerModel) {
        try {
            angehoerigerModel.setPlz(null);
        } catch (SvmValidationException e) {
            // Nicht weiter behandeln
        }
    }

    private void invalidateOrt(AngehoerigerModel angehoerigerModel) {
        try {
            angehoerigerModel.setOrt(null);
        } catch (SvmValidationException e) {
            // Nicht weiter behandeln
        }
    }

    private void invalidateFestnetz(AngehoerigerModel angehoerigerModel) {
        try {
            angehoerigerModel.setFestnetz(null);
        } catch (SvmValidationException e) {
            // Nicht weiter behandeln
        }
    }

    private void onSchuelerModelCompleted(boolean completed) {
        System.out.println("SchuelerModel completed=" + completed);
        isSchuelerModelCompleted = completed;
        fireCompletedChange();
    }

    private void onMutterModelCompleted(boolean completed) {
        System.out.println("MutterModel completed=" + completed);
        isMutterModelCompleted = completed;
        fireCompletedChange();
    }

    private void onVaterModelCompleted(boolean completed) {
        System.out.println("VaterModel completed=" + completed);
        isVaterModelCompleted = completed;
        fireCompletedChange();
    }

    private void onDrittempfaengerModelCompleted(boolean completed) {
        System.out.println("DrittempfaengerModel completed=" + completed);
        isDrittempfaengerModelCompleted = completed;
        fireCompletedChange();
    }

    private void fireCompletedChange() {
        fireCompleted(isCompleted());
    }

    private ValidateSchuelerCommand validateSchuelerCommand;

    @Override
    public SchuelerErfassenSaveResult validieren() {
        System.out.println("SchuelerErfassenModel validieren");

        validateSchuelerCommand = new ValidateSchuelerCommand(this);
        validateSchuelerCommand.setEntry(NEU_ERFASSTEN_SCHUELER_VALIDIEREN);
        CommandInvoker commandInvoker = getCommandInvoker();
        try {
            commandInvoker.beginTransaction();
            commandInvoker.executeCommandWithinTransaction(validateSchuelerCommand);
        } catch (Throwable e) {
            commandInvoker.rollbackTransaction();
            return new SchuelerErfassenUnerwarteterFehlerResult(ValidateSchuelerCommand.Result.UNERWARTETER_FEHLER, e);
        }
        return validateSchuelerCommand.getResult();
    }

    @Override
    public SchuelerErfassenSaveResult proceedUebernehmen(SchuelerErfassenSaveResult schuelerErfassenSaveResult) {
        // Eigentlich müsste man das Model durch das neue Objekt, das übernommen wird, ersetzen. Darauf verzichten
        // wir im Moment.
        validateSchuelerCommand.setEntry(schuelerErfassenSaveResult.getResult().proceedUebernehmen());
        return executeCommandWithinTransaction();
    }

    @Override
    public SchuelerErfassenSaveResult proceedWeiterfahren(SchuelerErfassenSaveResult schuelerErfassenSaveResult) {
        validateSchuelerCommand.setEntry(schuelerErfassenSaveResult.getResult().proceedWeiterfahren());
        return executeCommandWithinTransaction();
    }

    private SchuelerErfassenSaveResult executeCommandWithinTransaction() {
        try {
            getCommandInvoker().executeCommandWithinTransaction(validateSchuelerCommand);
        } catch (Throwable e) {
            getCommandInvoker().rollbackTransaction();
            return new SchuelerErfassenUnerwarteterFehlerResult(ValidateSchuelerCommand.Result.UNERWARTETER_FEHLER, e);
        }
        return validateSchuelerCommand.getResult();
    }

    @Override
    public SchuelerErfassenSaveResult speichern(SchuelerErfassenSaveResult schuelerErfassenSaveResult) {
        validateSchuelerCommand.setEntry(ValidateSchuelerCommand.Entry.SUMMARY_BESTAETIGT);
        CommandInvoker commandInvoker = getCommandInvoker();
        try {
            commandInvoker.executeCommandWithinTransaction(validateSchuelerCommand);
            commandInvoker.commitTransaction();
            return validateSchuelerCommand.getResult();
        } catch (Throwable e) {
            commandInvoker.rollbackTransaction();
            return new SchuelerErfassenUnerwarteterFehlerResult(ValidateSchuelerCommand.Result.UNERWARTETER_FEHLER, e);
        } finally {
            validateSchuelerCommand = null;
        }
    }

    @Override
    public void abbrechen() {
        CommandInvoker commandInvoker = getCommandInvoker();
        commandInvoker.rollbackTransaction();
        validateSchuelerCommand = null;
    }

    @Override
    public Schueler getSchueler() {
        return schuelerModel.getSchueler();
    }

    @Override
    public Adresse getAdresseSchueler() {
        return schuelerModel.getAdresse();
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
}
