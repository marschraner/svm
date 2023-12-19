package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CheckKurstypBezeichnungBereitsInVerwendungCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SaveOrUpdateKurstypCommand;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.ui.componentmodel.KurstypenTableModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Martin Schraner
 */
public class KurstypErfassenModelImpl extends AbstractModel implements KurstypErfassenModel {

    private static final Logger LOGGER = LogManager.getLogger(KurstypErfassenModelImpl.class);

    private final Kurstyp kurstyp = new Kurstyp();
    private Kurstyp kurstypOrigin;

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
            new AttributeAccessor<>() {
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
    public void setSelektierbar(Boolean isSelected) {
        Boolean oldValue = kurstyp.getSelektierbar();
        kurstyp.setSelektierbar(isSelected);
        firePropertyChange(Field.SELEKTIERBAR, oldValue, isSelected);
    }

    @Override
    public Boolean isSelektierbar() {
        return kurstyp.getSelektierbar();
    }

    @Override
    public boolean checkKurstypBezeichnungBereitsInVerwendung(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        CheckKurstypBezeichnungBereitsInVerwendungCommand checkKurstypBezeichnungBereitsInVerwendungCommand = new CheckKurstypBezeichnungBereitsInVerwendungCommand(kurstyp, kurstypOrigin, svmModel.getKurstypenAll());
        commandInvoker.executeCommand(checkKurstypBezeichnungBereitsInVerwendungCommand);
        return checkKurstypBezeichnungBereitsInVerwendungCommand.isBereitsInVerwendung();
    }

    @Override
    public void speichern(SvmModel svmModel, KurstypenTableModel kurstypenTableModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        SaveOrUpdateKurstypCommand saveOrUpdateKurstypCommand = new SaveOrUpdateKurstypCommand(kurstyp, kurstypOrigin, svmModel.getKurstypenAll());
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKurstypCommand);
        // TableData mit von der Datenbank upgedateten Kurstypen updaten
        kurstypenTableModel.getKurstypenTableData().setKurstypen(svmModel.getKurstypenAll());
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void initializeCompleted() {
        if (kurstypOrigin != null) {
            setBulkUpdate(true);
            try {
                setBezeichnung(kurstypOrigin.getBezeichnung());
                setSelektierbar(!kurstypOrigin.getSelektierbar()); // damit PropertyChange ausgelöst wird!
                setSelektierbar(kurstypOrigin.getSelektierbar());
            } catch (SvmValidationException e) {
                LOGGER.error(e.getMessage());
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
    void doValidate() throws SvmValidationException {
    }
}
