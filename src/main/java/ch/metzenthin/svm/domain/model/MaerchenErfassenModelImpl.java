package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CheckMaerchenBereitsErfasstCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DetermineNaechstesNochNichtErfasstesSchuljahrMaerchenCommand;
import ch.metzenthin.svm.domain.commands.SaveOrUpdateMaerchenCommand;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import ch.metzenthin.svm.ui.componentmodel.MaerchensTableModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Martin Schraner
 */
public class MaerchenErfassenModelImpl extends AbstractModel implements MaerchenErfassenModel {

    private static final Logger LOGGER = LogManager.getLogger(MaerchenErfassenModelImpl.class);

    private final Maerchen maerchen = new Maerchen();
    private Maerchen maerchenOrigin;

    @Override
    public Maerchen getMaerchen() {
        return maerchen;
    }

    @Override
    public void setMaerchenOrigin(Maerchen maerchenOrigin) {
        this.maerchenOrigin = maerchenOrigin;
    }

    private final StringModelAttribute schuljahrModelAttribute = new StringModelAttribute(
            this,
            Field.SCHULJAHR, 9, 9,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return maerchen.getSchuljahr();
                }

                @Override
                public void setValue(String value) {
                    maerchen.setSchuljahr(value);
                }
            }
    );

    @Override
    public String getSchuljahr() {
        return schuljahrModelAttribute.getValue();
    }

    @Override
    public void setSchuljahr(String schuljahr) throws SvmValidationException {
        schuljahrModelAttribute.setNewValue(true, schuljahr, isBulkUpdate());
    }

    private final StringModelAttribute bezeichnungModelAttribute = new StringModelAttribute(
            this,
            Field.BEZEICHNUNG, 2, 30,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return maerchen.getBezeichnung();
                }

                @Override
                public void setValue(String value) {
                    maerchen.setBezeichnung(value);
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

    private final IntegerModelAttribute anzahlVorstellungenModelAttribute = new IntegerModelAttribute(
            this,
            Field.ANZAHL_VORSTELLUNGEN, 1, 9,
            new AttributeAccessor<>() {
                @Override
                public Integer getValue() {
                    return maerchen.getAnzahlVorstellungen();
                }

                @Override
                public void setValue(Integer value) {
                    maerchen.setAnzahlVorstellungen(value);
                }
            }
    );

    @Override
    public Integer getAnzahlVorstellungen() {
        return anzahlVorstellungenModelAttribute.getValue();
    }

    @Override
    public void setAnzahlVorstellungen(String anzahlVorstellungen) throws SvmValidationException {
        anzahlVorstellungenModelAttribute.setNewValue(true, anzahlVorstellungen, isBulkUpdate());
    }

    @Override
    public void initializeCompleted() {
        if (maerchenOrigin != null) {
            setBulkUpdate(true);
            try {
                setSchuljahr(maerchenOrigin.getSchuljahr());
                setBezeichnung(maerchenOrigin.getBezeichnung());
                setAnzahlVorstellungen(Integer.toString(maerchenOrigin.getAnzahlVorstellungen()));

            } catch (SvmValidationException e) {
                LOGGER.error(e.getMessage());
            }
            setBulkUpdate(false);
        } else {
            super.initializeCompleted();
        }
    }

    @Override
    public boolean checkMaerchenBereitsErfasst(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        CheckMaerchenBereitsErfasstCommand checkMaerchenBereitsErfasstCommand = new CheckMaerchenBereitsErfasstCommand(maerchen, maerchenOrigin, svmModel.getMaerchensAll());
        commandInvoker.executeCommand(checkMaerchenBereitsErfasstCommand);
        return checkMaerchenBereitsErfasstCommand.isBereitsErfasst();
    }

    @Override
    public String getNaechstesNochNichtErfasstesSchuljahrMaerchen(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        DetermineNaechstesNochNichtErfasstesSchuljahrMaerchenCommand determineNaechstesNochNichtErfasstesSchuljahrMaerchenCommand = new DetermineNaechstesNochNichtErfasstesSchuljahrMaerchenCommand(svmModel.getMaerchensAll());
        commandInvoker.executeCommand(determineNaechstesNochNichtErfasstesSchuljahrMaerchenCommand);
        return determineNaechstesNochNichtErfasstesSchuljahrMaerchenCommand.getNaechstesNochNichtErfasstesSchuljahrMaerchen();
    }

    @Override
    public boolean checkIfMaerchenIsInPast() {
        Calendar today = new GregorianCalendar();
        int schuljahr1;
        if (today.get(Calendar.MONTH) <= Calendar.JUNE) {
            schuljahr1 = today.get(Calendar.YEAR) - 1;
        } else {
            schuljahr1 = today.get(Calendar.YEAR);
        }
        int schuljahr1Maerchen = Integer.parseInt(maerchen.getSchuljahr().substring(0, 4));
        return schuljahr1Maerchen < schuljahr1;
    }

    @Override
    public void speichern(SvmModel svmModel, MaerchensTableModel maerchensTableModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        SaveOrUpdateMaerchenCommand saveOrUpdateMaerchenCommand = new SaveOrUpdateMaerchenCommand(maerchen, maerchenOrigin, svmModel.getMaerchensAll());
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCommand);
        // TableData mit von der Datenbank upgedateten Märchens updaten
        maerchensTableModel.getMaerchensTableData().setMaerchens(svmModel.getMaerchensAll());
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    void doValidate() throws SvmValidationException {
        // Keine feldübergreifende Validierung notwendig
    }

}
