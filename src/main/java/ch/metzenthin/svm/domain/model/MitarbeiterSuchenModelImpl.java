package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.MitarbeiterSuchenCommand;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

import java.util.List;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
final class MitarbeiterSuchenModelImpl extends AbstractModel implements MitarbeiterSuchenModel {

    private String nachname;
    private String vorname;
    private MitarbeiterCode mitarbeiterCode;
    private LehrkraftJaNeinSelected lehrkraftJaNeinSelected;
    private StatusSelected statusSelected;

    private final StringModelAttribute nachnameModelAttribute = new StringModelAttribute(
            this,
            Field.NACHNAME, 1, 50,
            new AttributeAccessor<>() {
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
            new AttributeAccessor<>() {
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
    public StatusSelected getStatusSelected() {
        return statusSelected;
    }

    @Override
    public void setStatusSelected(StatusSelected statusSelected) {
        StatusSelected oldValue = this.statusSelected;
        this.statusSelected = statusSelected;
        firePropertyChange(Field.STATUS, oldValue, this.statusSelected);
    }

    @Override
    public MitarbeiterCode[] getSelectableMitarbeiterCodes(SvmModel svmModel) {
        List<MitarbeiterCode> codesList = svmModel.getSelektierbareMitarbeiterCodesAll();
        // MitarbeiterCode alle auch erlaubt
        codesList.add(0, MITARBEITER_CODE_ALLE);
        return codesList.toArray(new MitarbeiterCode[0]);
    }

    @Override
    public boolean isSuchkriterienSelected() {
        return checkNotEmpty(nachname)
                || checkNotEmpty(vorname)
                || (mitarbeiterCode != null && mitarbeiterCode != MITARBEITER_CODE_ALLE)
                || (lehrkraftJaNeinSelected != LehrkraftJaNeinSelected.ALLE)
                || (statusSelected != StatusSelected.AKTIV);
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
        // Keine feldübergreifende Validierung notwendig
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
