package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Codetyp;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Martin Schraner
 */
public class CodeErfassenModelImpl extends AbstractModel implements CodeErfassenModel {

    private static final Logger LOGGER = LogManager.getLogger(CodeErfassenModelImpl.class);

    private String kuerzel;
    private String beschreibung;
    private Boolean selektierbar;
    private SchuelerCode schuelerCodeOrigin;
    private MitarbeiterCode mitarbeiterCodeOrigin;
    private ElternmithilfeCode elternmithilfeCodeOrigin;
    private SemesterrechnungCode semesterrechnungCodeOrigin;

    @Override
    public void setSchuelerCodeOrigin(SchuelerCode schuelerCodeOrigin) {
        this.schuelerCodeOrigin = schuelerCodeOrigin;
    }

    @Override
    public void setMitarbeiterCodeOrigin(MitarbeiterCode mitarbeiterCodeOrigin) {
        this.mitarbeiterCodeOrigin = mitarbeiterCodeOrigin;
    }

    @Override
    public void setElternmithilfeCodeOrigin(ElternmithilfeCode elternmithilfeCodeOrigin) {
        this.elternmithilfeCodeOrigin = elternmithilfeCodeOrigin;
    }

    @Override
    public void setSemesterrechnungCodeOrigin(SemesterrechnungCode semesterrechnungCodeOrigin) {
        this.semesterrechnungCodeOrigin = semesterrechnungCodeOrigin;
    }

    private final StringModelAttribute kuerzelModelAttribute = new StringModelAttribute(
            this,
            Field.KUERZEL, 1, 3,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return kuerzel;
                }

                @Override
                public void setValue(String value) {
                    kuerzel = value;
                }
            }
    );

    @Override
    public String getKuerzel() {
        return kuerzelModelAttribute.getValue();
    }

    @Override
    public void setKuerzel(String kuerzel) throws SvmValidationException {
        kuerzelModelAttribute.setNewValue(true, kuerzel, isBulkUpdate());
    }

    private final StringModelAttribute beschreibungModelAttribute = new StringModelAttribute(
            this,
            Field.BESCHREIBUNG, 2, 50,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return beschreibung;
                }

                @Override
                public void setValue(String value) {
                    beschreibung = value;
                }
            }
    );

    @Override
    public String getBeschreibung() {
        return beschreibungModelAttribute.getValue();
    }

    @Override
    public void setBeschreibung(String beschreibung) throws SvmValidationException {
        beschreibungModelAttribute.setNewValue(true, beschreibung, isBulkUpdate());
    }

    @Override
    public void setSelektierbar(Boolean isSelected) {
        Boolean oldValue = selektierbar;
        selektierbar = isSelected;
        firePropertyChange(Field.SELEKTIERBAR, oldValue, isSelected);
    }

    @Override
    public Boolean isSelektierbar() {
        return selektierbar;
    }

    @Override
    public boolean checkCodeKuerzelBereitsInVerwendung(SvmModel svmModel, Codetyp codetyp) {
        CommandInvoker commandInvoker = getCommandInvoker();
        CheckCodeKuerzelBereitsInVerwendungCommand checkCodeKuerzelBereitsInVerwendungCommand = switch (codetyp) {
            case SCHUELER ->
                    new CheckCodeKuerzelBereitsInVerwendungCommand(kuerzel, schuelerCodeOrigin, svmModel.getSchuelerCodesAll());
            case MITARBEITER ->
                    new CheckCodeKuerzelBereitsInVerwendungCommand(kuerzel, mitarbeiterCodeOrigin, svmModel.getMitarbeiterCodesAll());
            case ELTERNMITHILFE ->
                    new CheckCodeKuerzelBereitsInVerwendungCommand(kuerzel, elternmithilfeCodeOrigin, svmModel.getElternmithilfeCodesAll());
            case SEMESTERRECHNUNG ->
                    new CheckCodeKuerzelBereitsInVerwendungCommand(kuerzel, semesterrechnungCodeOrigin, svmModel.getSemesterrechnungCodesAll());
        };
        commandInvoker.executeCommand(checkCodeKuerzelBereitsInVerwendungCommand);
        return checkCodeKuerzelBereitsInVerwendungCommand.isBereitsInVerwendung();
    }

    @Override
    public void speichern(SvmModel svmModel, CodesTableModel codesTableModel, Codetyp codetyp) {
        CommandInvoker commandInvoker = getCommandInvoker();
        switch (codetyp) {
            case SCHUELER -> {
                SchuelerCode schuelerCode = new SchuelerCode(kuerzel, beschreibung, selektierbar);
                SaveOrUpdateSchuelerCodeCommand saveOrUpdateSchuelerCodeCommand = new SaveOrUpdateSchuelerCodeCommand(schuelerCode, schuelerCodeOrigin, svmModel.getSchuelerCodesAll());
                commandInvoker.executeCommandAsTransaction(saveOrUpdateSchuelerCodeCommand);
                // TableData mit von der Datenbank upgedateten SchülerCodes updaten
                codesTableModel.getCodesTableData().setCodes(svmModel.getSchuelerCodesAll());
            }
            case MITARBEITER -> {
                MitarbeiterCode mitarbeiterCode = new MitarbeiterCode(kuerzel, beschreibung, selektierbar);
                SaveOrUpdateMitarbeiterCodeCommand saveOrUpdateMitarbeiterCodeCommand = new SaveOrUpdateMitarbeiterCodeCommand(mitarbeiterCode, mitarbeiterCodeOrigin, svmModel.getMitarbeiterCodesAll());
                commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCodeCommand);
                // TableData mit von der Datenbank upgedateten MitarbeiterCodes updaten
                codesTableModel.getCodesTableData().setCodes(svmModel.getMitarbeiterCodesAll());
            }
            case ELTERNMITHILFE -> {
                ElternmithilfeCode elternmithilfeCode = new ElternmithilfeCode(kuerzel, beschreibung, selektierbar);
                SaveOrUpdateElternmithilfeCodeCommand saveOrUpdateElternmithilfeCodeCommand = new SaveOrUpdateElternmithilfeCodeCommand(elternmithilfeCode, elternmithilfeCodeOrigin, svmModel.getElternmithilfeCodesAll());
                commandInvoker.executeCommandAsTransaction(saveOrUpdateElternmithilfeCodeCommand);
                // TableData mit von der Datenbank upgedateten ElternmithilfeCodes updaten
                codesTableModel.getCodesTableData().setCodes(svmModel.getElternmithilfeCodesAll());
            }
            case SEMESTERRECHNUNG -> {
                SemesterrechnungCode semesterrechnungCode = new SemesterrechnungCode(kuerzel, beschreibung, selektierbar);
                SaveOrUpdateSemesterrechnungCodeCommand saveOrUpdateSemesterrechnungCodeCommand = new SaveOrUpdateSemesterrechnungCodeCommand(semesterrechnungCode, semesterrechnungCodeOrigin, svmModel.getSemesterrechnungCodesAll());
                commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterrechnungCodeCommand);
                // TableData mit von der Datenbank upgedateten SemesterrrechnungCodes updaten
                codesTableModel.getCodesTableData().setCodes(svmModel.getSemesterrechnungCodesAll());
            }
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void initializeCompleted() {
        if (schuelerCodeOrigin != null) {
            setBulkUpdate(true);
            try {
                setKuerzel(schuelerCodeOrigin.getKuerzel());
                setBeschreibung(schuelerCodeOrigin.getBeschreibung());
                setSelektierbar(!schuelerCodeOrigin.getSelektierbar()); // damit PropertyChange ausgelöst wird!
                setSelektierbar(schuelerCodeOrigin.getSelektierbar());
            } catch (SvmValidationException e) {
                LOGGER.error(e.getMessage());
            }
            setBulkUpdate(false);
        } else if (mitarbeiterCodeOrigin != null) {
            setBulkUpdate(true);
            try {
                setKuerzel(mitarbeiterCodeOrigin.getKuerzel());
                setBeschreibung(mitarbeiterCodeOrigin.getBeschreibung());
                setSelektierbar(!mitarbeiterCodeOrigin.getSelektierbar()); // damit PropertyChange ausgelöst wird!
                setSelektierbar(mitarbeiterCodeOrigin.getSelektierbar());
            } catch (SvmValidationException e) {
                LOGGER.error(e.getMessage());
            }
            setBulkUpdate(false);
        } else if (elternmithilfeCodeOrigin != null) {
            setBulkUpdate(true);
            try {
                setKuerzel(elternmithilfeCodeOrigin.getKuerzel());
                setBeschreibung(elternmithilfeCodeOrigin.getBeschreibung());
                setSelektierbar(!elternmithilfeCodeOrigin.getSelektierbar()); // damit PropertyChange ausgelöst wird!
                setSelektierbar(elternmithilfeCodeOrigin.getSelektierbar());
            } catch (SvmValidationException e) {
                LOGGER.error(e.getMessage());
            }
            setBulkUpdate(false);
        } else if (semesterrechnungCodeOrigin != null) {
            setBulkUpdate(true);
            try {
                setKuerzel(semesterrechnungCodeOrigin.getKuerzel());
                setBeschreibung(semesterrechnungCodeOrigin.getBeschreibung());
                setSelektierbar(!semesterrechnungCodeOrigin.getSelektierbar()); // damit PropertyChange ausgelöst wird!
                setSelektierbar(semesterrechnungCodeOrigin.getSelektierbar());
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
