package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.utils.Converter;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.ui.control.CompletedListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;

/**
 * @author Hans Stamm
 */
public class SchuelerErfassenModelImpl extends AbstractModel implements SchuelerErfassenModel {

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
    public void checkCompleted() {
        schuelerModel.checkCompleted();
        mutterModel.checkCompleted();
        vaterModel.checkCompleted();
        drittempfaengerModel.checkCompleted();
    }

    @Override
    public boolean isCompleted() {
        boolean completed = isSetRechnungsempfaenger() && isSchuelerModelCompleted && isMutterModelCompleted && isVaterModelCompleted && isDrittempfaengerModelCompleted;
        System.out.println("SchuelerErfassenModel completed=" + completed);
        return completed;
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
    }

    private void onVaterModelPropertyChange(PropertyChangeEvent evt) {
        if (isRechnungsempfaengerPropertyChange(evt)) {
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                uncheckRechnungsempfaenger(mutterModel, drittempfaengerModel);
            }
            isVaterRechnungsempfaenger = newValue;
        }
    }

    private void onDrittempfaengerModelPropertyChange(PropertyChangeEvent evt) {
        if (isRechnungsempfaengerPropertyChange(evt)) {
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                uncheckRechnungsempfaenger(mutterModel, vaterModel);
            }
            isDrittempfaengerRechnungsempfaenger = newValue;
        }
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

    @Override
    public void save() {
        System.out.println("SchuelerErfassenModel save");

        // todo $$$ anfang hack
        Calendar calendar = Converter.toCalendarIgnoreException("01.01.1965");
        vaterModel.getAngehoeriger().setGeburtsdatum(calendar);
        mutterModel.getAngehoeriger().setGeburtsdatum(calendar);
        schuelerModel.getSchueler().setNewAdresse(schuelerModel.getAdresse());
        vaterModel.getAngehoeriger().setNewAdresse(vaterModel.getAdresse());
        mutterModel.getAngehoeriger().setNewAdresse(mutterModel.getAdresse());
        // todo $$$ ende hack
        AngehoerigerModel rechnungsempfaenger = getRechnungsempfaengerModel();
        ValidateSchuelerCommand validateSchuelerCommand = new ValidateSchuelerCommand(
                schuelerModel.getSchueler(),
                (mutterModel.isCompleted()) ? mutterModel.getAngehoeriger() : null, true, // todo $$$ hack
                (vaterModel.isCompleted()) ? vaterModel.getAngehoeriger() : null, false, // todo $$$ hack
                (rechnungsempfaenger != null) ? rechnungsempfaenger.getAngehoeriger() : null
        );
//        // todo muss das eine Transaktion sein, oder einfach validateSchuelerCommand.execute() ?
//        getCommandInvoker().executeCommand(validateSchuelerCommand);
//        System.out.println("Info AbweichendeAdressen=" + validateSchuelerCommand.getInfoAbweichendeAdressen());
//        System.out.println("Info BereitsInDb=" + validateSchuelerCommand.getInfoBereitsInDb());
//        System.out.println("Info IdentischeAdressen=" + validateSchuelerCommand.getInfoIdentischeAdressen());
//        System.out.println("Info NeuErfasst=" + validateSchuelerCommand.getInfoNeuErfasst());
//        System.out.println("Info Rechnungsempfaenger=" + validateSchuelerCommand.getInfoRechnungsempfaenger());
//
//        // todo $$$ aufteilen in validate und save Methoden aufgerufen von Controller
//        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(validateSchuelerCommand.getSchueler());
//        getCommandInvoker().executeCommand(saveSchuelerCommand);
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

}
