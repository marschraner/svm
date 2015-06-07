package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.domain.commands.ValidateSchuelerModel;
import ch.metzenthin.svm.persistence.SvmDbException;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.ui.control.CompletedListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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

    private boolean isMutterRechnungsempfaenger;
    private boolean isVaterRechnungsempfaenger;
    private boolean isDrittempfaengerRechnungsempfaenger;

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
        return isMutterRechnungsempfaenger || isVaterRechnungsempfaenger || isDrittempfaengerRechnungsempfaenger;
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

    // todo schöner programmieren (nächste drei methoden)
    private void onMutterModelPropertyChange(PropertyChangeEvent evt) {
        if (isRechnungsempfaengerPropertyChange(evt)) {
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                uncheckRechnungsempfaenger(vaterModel, drittempfaengerModel);
            }
            isMutterRechnungsempfaenger = newValue;
        }
        fireCompletedChange();
    }

    private void onVaterModelPropertyChange(PropertyChangeEvent evt) {
        if (isRechnungsempfaengerPropertyChange(evt)) {
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                uncheckRechnungsempfaenger(mutterModel, drittempfaengerModel);
            }
            isVaterRechnungsempfaenger = newValue;
        }
        fireCompletedChange();
    }

    private void onDrittempfaengerModelPropertyChange(PropertyChangeEvent evt) {
        if (isRechnungsempfaengerPropertyChange(evt)) {
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                uncheckRechnungsempfaenger(mutterModel, vaterModel);
            }
            isDrittempfaengerRechnungsempfaenger = newValue;
        }
        fireCompletedChange();
    }

    private boolean isRechnungsempfaengerPropertyChange(PropertyChangeEvent evt) {
        return "Rechnungsempfaenger".equals(evt.getPropertyName());
    }

    private boolean isBooleanNewValuePropertyChecked(Boolean newValue) {
        return (newValue != null) && newValue;
    }

    private void uncheckRechnungsempfaenger(AngehoerigerModel... angehoerigerModels) {
        for (AngehoerigerModel angehoerigerModel : angehoerigerModels) {
            angehoerigerModel.setIsRechnungsempfaenger(false);
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

        CommandInvoker commandInvoker = getCommandInvoker();
        commandInvoker.beginTransaction();
        validateSchuelerCommand = new ValidateSchuelerCommand(this);
        validateSchuelerCommand.setEntry(NEU_ERFASSTEN_SCHUELER_VALIDIEREN);
        try {
            commandInvoker.executeCommandWithinTransaction(validateSchuelerCommand);
        } catch (SvmDbException e) {
            e.printStackTrace(); // todo $$$
        }
        return validateSchuelerCommand.getResult();
    }

    @Override
    public SchuelerErfassenSaveResult proceedUebernehmen(SchuelerErfassenSaveResult schuelerErfassenSaveResult) {
        validateSchuelerCommand.setEntry(schuelerErfassenSaveResult.getResult().proceedUebernehmen());
        try {
            getCommandInvoker().executeCommandWithinTransaction(validateSchuelerCommand);
        } catch (SvmDbException e) {
            e.printStackTrace(); // todo $$$
        }
        return validateSchuelerCommand.getResult();
    }

    @Override
    public SchuelerErfassenSaveResult proceedWeiterfahren(SchuelerErfassenSaveResult schuelerErfassenSaveResult) {
        validateSchuelerCommand.setEntry(schuelerErfassenSaveResult.getResult().proceedWeiterfahren());
        try {
            getCommandInvoker().executeCommandWithinTransaction(validateSchuelerCommand);
        } catch (SvmDbException e) {
            e.printStackTrace(); // todo $$$
        }
        return validateSchuelerCommand.getResult();
    }

    @Override
    public SchuelerErfassenSaveResult speichern(SchuelerErfassenSaveResult schuelerErfassenSaveResult) { // todo Summary entfernen
        //validateSchuelerCommand.setEntry(schuelerErfassenSaveResult.getResult().proceedWeiterfahren());
        validateSchuelerCommand.setEntry(ValidateSchuelerCommand.Entry.SUMMARY_BESTAETIGT);
        CommandInvoker commandInvoker = getCommandInvoker();
        try {
            commandInvoker.executeCommandWithinTransaction(validateSchuelerCommand);
        } catch (SvmDbException e) {
            e.printStackTrace(); // todo $$$
        }
        commandInvoker.commitTransaction();
        SchuelerErfassenSaveResult result = validateSchuelerCommand.getResult();
        validateSchuelerCommand = null;
        return result;
    }

    @Override
    public void abbrechen() {
        CommandInvoker commandInvoker = getCommandInvoker();
        commandInvoker.rollbackTransaction();
        validateSchuelerCommand = null;
    }

    private AngehoerigerModel getRechnungsempfaengerModel() {
        if (mutterModel.isRechnungsempfaenger()) {
            return mutterModel;
        } else if (vaterModel.isRechnungsempfaenger()) {
            return vaterModel;
        } else if (drittempfaengerModel.isRechnungsempfaenger()) {
            return drittempfaengerModel;
        }
        return null;
    }

    // todo null wenn nicht gefüllt (Mutter, Vater, Rechnungempfaenger)

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
        return null; /*vaterModel.getAngehoeriger();*/ // todo
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
        return null; /*drittempfaengerModel.getAngehoeriger();*/ // todo
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
