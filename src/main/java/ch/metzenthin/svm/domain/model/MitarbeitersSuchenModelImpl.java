package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.MitarbeitersSuchenCommand;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
final class MitarbeitersSuchenModelImpl extends AbstractModel implements MitarbeitersSuchenModel {

    private String nachname;
    private String vorname;
    private String mitarbeiterCodes;
    private LehrkraftJaNeinSelected lehrkraftJaNeinSelected;
    private AktivJaNeinSelected aktivJaNeinSelected;
    
    MitarbeitersSuchenModelImpl(CommandInvoker commandInvoker) {
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

    private final StringModelAttribute mitarbeiterCodesModelAttribute = new StringModelAttribute(
            this,
            Field.MITARBEITER_CODES, 1, 50,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return mitarbeiterCodes;
                }

                @Override
                public void setValue(String value) {
                    mitarbeiterCodes = value;
                }
            }
    );

    @Override
    public String getMitarbeiterCodes() {
        return mitarbeiterCodesModelAttribute.getValue();
    }

    @Override
    public void setMitarbeiterCodes(String mitarbeiterCodes) throws SvmValidationException {
        mitarbeiterCodesModelAttribute.setNewValue(false, mitarbeiterCodes, isBulkUpdate());
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
    public String checkIfCodeKuerzelsExist(SvmModel svmModel) {
        if (mitarbeiterCodes == null) {
            return "";
        }
        List<String> codesKuerzel = new ArrayList<>();
        StringBuilder erfassteCodes = new StringBuilder();
        for (MitarbeiterCode mitarbeiterCode : svmModel.getMitarbeiterCodesAll()) {
            codesKuerzel.add(mitarbeiterCode.getKuerzel());
            erfassteCodes.append(mitarbeiterCode.toString()).append(",\n");
        }
        // letztes ", " entfernen
        erfassteCodes.setLength(erfassteCodes.length() - 2);
        String[] mitarbeiterCodesSplitted = mitarbeiterCodes.split("[,;]");
        for (String mitarbeiterCodeSuchabfrage : mitarbeiterCodesSplitted) {
            if (!codesKuerzel.contains(mitarbeiterCodeSuchabfrage.trim())) {
                return "'" + mitarbeiterCodeSuchabfrage + "' ist kein erfasster Mitarbeiter-Code. Erfasste Codes sind:\n" + erfassteCodes.toString() + ".";
            }
        }
        return "";
    }

    @Override
    public MitarbeitersTableData suchen() {
        MitarbeitersSuchenCommand mitarbeitersSuchenCommand = new MitarbeitersSuchenCommand(this);
        getCommandInvoker().executeCommand(mitarbeitersSuchenCommand);
        return new MitarbeitersTableData(mitarbeitersSuchenCommand.getMitarbeitersFound());
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
