package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CheckKursortBezeichnungBereitsInVerwendungCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SaveOrUpdateKursortCommand;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.ui.componentmodel.KursorteTableModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Martin Schraner
 */
public class KursortErfassenModelImpl extends AbstractModel implements KursortErfassenModel {

    private static final Logger LOGGER = LogManager.getLogger(KursortErfassenModelImpl.class);

    private final Kursort kursort = new Kursort();
    private Kursort kursortOrigin;

    @Override
    public Kursort getKursort() {
        return kursort;
    }

    @Override
    public void setKursortOrigin(Kursort kursortOrigin) {
        this.kursortOrigin = kursortOrigin;
    }

    private final StringModelAttribute bezeichnungModelAttribute = new StringModelAttribute(
            this,
            Field.BEZEICHNUNG, 2, 50,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return kursort.getBezeichnung();
                }

                @Override
                public void setValue(String value) {
                    kursort.setBezeichnung(value);
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
        Boolean oldValue = kursort.getSelektierbar();
        kursort.setSelektierbar(isSelected);
        firePropertyChange(Field.SELEKTIERBAR, oldValue, isSelected);
    }

    @Override
    public Boolean isSelektierbar() {
        return kursort.getSelektierbar();
    }

    @Override
    public boolean checkKursortBezeichnungBereitsInVerwendung(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        CheckKursortBezeichnungBereitsInVerwendungCommand checkKursortBezeichnungBereitsInVerwendungCommand = new CheckKursortBezeichnungBereitsInVerwendungCommand(kursort, kursortOrigin, svmModel.getKursorteAll());
        commandInvoker.executeCommand(checkKursortBezeichnungBereitsInVerwendungCommand);
        return checkKursortBezeichnungBereitsInVerwendungCommand.isBereitsInVerwendung();
    }

    @Override
    public void speichern(SvmModel svmModel, KursorteTableModel kursorteTableModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        SaveOrUpdateKursortCommand saveOrUpdateKursortCommand = new SaveOrUpdateKursortCommand(kursort, kursortOrigin, svmModel.getKursorteAll());
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursortCommand);
        // TableData mit von der Datenbank upgedateten Kursorten updaten
        kursorteTableModel.getKursorteTableData().setKursorte(svmModel.getKursorteAll());
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void initializeCompleted() {
        if (kursortOrigin != null) {
            setBulkUpdate(true);
            try {
                setBezeichnung(kursortOrigin.getBezeichnung());
                setSelektierbar(!kursortOrigin.getSelektierbar()); // damit PropertyChange ausgelöst wird!
                setSelektierbar(kursortOrigin.getSelektierbar());
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
