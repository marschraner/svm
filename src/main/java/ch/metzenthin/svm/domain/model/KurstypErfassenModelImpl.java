package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CheckKurstypBezeichnungBereitsInVerwendungCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SaveOrUpdateKurstypCommand;
import ch.metzenthin.svm.persistence.entities.Kurstyp;

/**
 * @author Martin Schraner
 */
public class KurstypErfassenModelImpl extends AbstractModel implements KurstypErfassenModel {

    private Kurstyp kurstyp = new Kurstyp();
    private Kurstyp kurstypOrigin;

    public KurstypErfassenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public Kurstyp getKurstyp() {
        return kurstyp;
    }

    @Override
    public void setKurstypOrigin(Kurstyp kurstypOrigin) {
        this.kurstypOrigin = kurstypOrigin;
    }

    private final StringModelAttribute bezeichnungModelAttribute = new StringModelAttribute(
            this,
            Field.BEZEICHNUNG, 2, 50,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return kurstyp.getBezeichnung();
                }

                @Override
                public void setValue(String value) {
                    kurstyp.setBezeichnung(value);
                }
            }
    );

    @Override
    public String getBezeichnung() {
        return bezeichnungModelAttribute.getValue();
    }

    @Override
    public void setBezeichnung(String bezeichnung) throws SvmValidationException {
        bezeichnungModelAttribute.setNewValue(true, bezeichnung, isBulkUpdate());
    }

    @Override
    public boolean checkKurstypBezeichnungBereitsInVerwendung(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        CheckKurstypBezeichnungBereitsInVerwendungCommand checkKurstypBezeichnungBereitsInVerwendungCommand = new CheckKurstypBezeichnungBereitsInVerwendungCommand(kurstyp, kurstypOrigin, svmModel.getKurstypenAll());
        commandInvoker.executeCommand(checkKurstypBezeichnungBereitsInVerwendungCommand);
        return checkKurstypBezeichnungBereitsInVerwendungCommand.isBereitsInVerwendung();
    }

    @Override
    public void speichern(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        SaveOrUpdateKurstypCommand saveOrUpdateKurstypCommand = new SaveOrUpdateKurstypCommand(kurstyp, kurstypOrigin, svmModel.getKurstypenAll());
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKurstypCommand);
    }

    @Override
    public void initializeCompleted() {
        if (kurstypOrigin != null) {
            setBulkUpdate(true);
            try {
                setBezeichnung(kurstypOrigin.getBezeichnung());
            } catch (SvmValidationException ignore) {
                ignore.printStackTrace();
            }
            setBulkUpdate(false);
        } else {
            super.initializeCompleted();
        }
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    void doValidate() throws SvmValidationException {}
}
