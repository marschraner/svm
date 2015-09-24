package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.MitarbeiterSuchenCommand;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
final class MitarbeiterSuchenModelImpl extends AbstractModel implements MitarbeiterSuchenModel {

    private String nachname;
    private String vorname;
    private MitarbeiterCode mitarbeiterCode;
    private LehrkraftJaNeinSelected lehrkraftJaNeinSelected;
    private AktivJaNeinSelected aktivJaNeinSelected;
    
    MitarbeiterSuchenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }
    
    private final StringModelAttribute nachnameModelAttribute = new StringModelAttribute(
            this,
            Field.NACHNAME, 1, 50,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return nachname;
                }

                @Override
                public void setValue(String value) {
                    nachname = value;
                }
            }
    );

    @Override
    public String getNachname() {
        return nachnameModelAttribute.getValue();
    }

    @Override
    public void setNachname(String nachname) throws SvmValidationException {
        nachnameModelAttribute.setNewValue(false, nachname, isBulkUpdate());
    }

    private final StringModelAttribute vornameModelAttribute = new StringModelAttribute(
            this,
            Field.VORNAME, 1, 50,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return vorname;
                }

                @Override
                public void setValue(String value) {
                    vorname = value;
                }
            }
    );

    @Override
    public String getVorname() {
        return vornameModelAttribute.getValue();
    }

    @Override
    public void setVorname(String vorname) throws SvmValidationException {
        vornameModelAttribute.setNewValue(false, vorname, isBulkUpdate());
    }

    @Override
    public MitarbeiterCode getMitarbeiterCode() {
        return mitarbeiterCode;
    }

    @Override
    public void setMitarbeiterCode(MitarbeiterCode mitarbeiterCode) {
        MitarbeiterCode oldValue = this.mitarbeiterCode;
        this.mitarbeiterCode = mitarbeiterCode;
        firePropertyChange(Field.MITARBEITER_CODE, oldValue, this.mitarbeiterCode);
    }

    @Override
    public LehrkraftJaNeinSelected getLehrkraftJaNeinSelected() {
        return lehrkraftJaNeinSelected;
    }

    @Override
    public void setLehrkraftJaNeinSelected(LehrkraftJaNeinSelected lehrkraftJaNeinSelected) {
        LehrkraftJaNeinSelected oldValue = this.lehrkraftJaNeinSelected;
        this.lehrkraftJaNeinSelected = lehrkraftJaNeinSelected;
        firePropertyChange(Field.LEHRKRAFT_JA_NEIN, oldValue, this.lehrkraftJaNeinSelected);
    }

    @Override
    public AktivJaNeinSelected getAktivJaNeinSelected() {
        return aktivJaNeinSelected;
    }

    @Override
    public void setAktivJaNeinSelected(AktivJaNeinSelected aktivJaNeinSelected) {
        AktivJaNeinSelected oldValue = this.aktivJaNeinSelected;
        this.aktivJaNeinSelected = aktivJaNeinSelected;
        firePropertyChange(Field.AKTIV_JA_NEIN, oldValue, this.aktivJaNeinSelected);
    }

    @Override
    public MitarbeiterCode[] getSelectableMitarbeiterCodes(SvmModel svmModel) {
        List<MitarbeiterCode> codesList = svmModel.getSelektierbareMitarbeiterCodesAll();
        // MitarbeiterCode alle auch erlaubt
        codesList.add(0, MITARBEITER_CODE_ALLE);
        return codesList.toArray(new MitarbeiterCode[codesList.size()]);
    }

    @Override
    public MitarbeitersTableData suchen() {
        if (mitarbeiterCode == MITARBEITER_CODE_ALLE) {
            mitarbeiterCode = null;
        }
        MitarbeiterSuchenCommand mitarbeiterSuchenCommand = new MitarbeiterSuchenCommand(this);
        getCommandInvoker().executeCommand(mitarbeiterSuchenCommand);
        return new MitarbeitersTableData(mitarbeiterSuchenCommand.getMitarbeitersFound());
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
