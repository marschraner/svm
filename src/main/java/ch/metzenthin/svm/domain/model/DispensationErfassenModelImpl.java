package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.AddDispensationToSchuelerAndSaveCommand;
import ch.metzenthin.svm.domain.commands.CheckDispensationUeberlapptAndereDispensationenCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Dispensation;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.ui.componentmodel.DispensationenTableModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.Converter.*;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class DispensationErfassenModelImpl extends AbstractModel implements DispensationErfassenModel {

    private static final Logger LOGGER = LogManager.getLogger(DispensationErfassenModelImpl.class);

    private final Dispensation dispensation = new Dispensation();
    private Dispensation dispensationOrigin;

    @Override
    public Dispensation getDispensation() {
        return dispensation;
    }

    @Override
    public void setDispensationOrigin(Dispensation dispensationOrigin) {
        this.dispensationOrigin = dispensationOrigin;
    }

    private final CalendarModelAttribute dispensationsbeginnModelAttribute = new CalendarModelAttribute(
            this,
            Field.DISPENSATIONSBEGINN, getNYearsBeforeNow(5), getNYearsAfterNow(5),
            new AttributeAccessor<>() {
                @Override
                public Calendar getValue() {
                    return dispensation.getDispensationsbeginn();
                }

                @Override
                public void setValue(Calendar value) {
                    dispensation.setDispensationsbeginn(value);
                }
            }
    );

    @Override
    public Calendar getDispensationsbeginn() {
        return dispensationsbeginnModelAttribute.getValue();
    }

    @Override
    public void setDispensationsbeginn(String dispensationsbeginn) throws SvmValidationException {
        dispensationsbeginnModelAttribute.setNewValue(true, dispensationsbeginn, isBulkUpdate());
        if (!isBulkUpdate() && dispensation.getDispensationsbeginn() != null && dispensation.getDispensationsende() != null && dispensation.getDispensationsbeginn().after(dispensation.getDispensationsende())) {
            dispensation.setDispensationsbeginn(null);
            invalidate();
            throw new SvmValidationException(2012, "Keine gültige Periode", Field.DISPENSATIONSBEGINN);
        }
    }

    private final CalendarModelAttribute dispensationsendeModelAttribute = new CalendarModelAttribute(
            this,
            Field.DISPENSATIONSENDE, getNYearsBeforeNow(5), getNYearsAfterNow(5),
            new AttributeAccessor<>() {
                @Override
                public Calendar getValue() {
                    return dispensation.getDispensationsende();
                }

                @Override
                public void setValue(Calendar value) {
                    dispensation.setDispensationsende(value);
                }
            }
    );

    @Override
    public Calendar getDispensationsende() {
        return dispensationsendeModelAttribute.getValue();
    }

    @Override
    public void setDispensationsende(String dispensationsende) throws SvmValidationException {
        dispensationsendeModelAttribute.setNewValue(false, dispensationsende, isBulkUpdate());
        if (!isBulkUpdate() && dispensation.getDispensationsbeginn() != null && dispensation.getDispensationsende() != null && dispensation.getDispensationsbeginn().after(dispensation.getDispensationsende())) {
            dispensation.setDispensationsende(null);
            invalidate();
            throw new SvmValidationException(2013, "Keine gültige Periode", Field.DISPENSATIONSENDE);
        }
    }

    private final StringModelAttribute voraussichtlicheDauerModelAttribute = new StringModelAttribute(
            this,
            Field.VORAUSSICHTLICHE_DAUER, 5, 20,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return dispensation.getVoraussichtlicheDauer();
                }

                @Override
                public void setValue(String value) {
                    dispensation.setVoraussichtlicheDauer(value);
                }
            }
    );

    @Override
    public String getVoraussichtlicheDauer() {
        return voraussichtlicheDauerModelAttribute.getValue();
    }

    @Override
    public void setVoraussichtlicheDauer(String voraussichtlicheDauer) throws SvmValidationException {
        voraussichtlicheDauerModelAttribute.setNewValue(false, voraussichtlicheDauer, isBulkUpdate());
    }

    private final StringModelAttribute grundModelAttribute = new StringModelAttribute(
            this,
            Field.GRUND, 5, 30,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return dispensation.getGrund();
                }

                @Override
                public void setValue(String value) {
                    dispensation.setGrund(value);
                }
            }
    );

    @Override
    public String getGrund() {
        return grundModelAttribute.getValue();
    }

    @Override
    public void setGrund(String grund) throws SvmValidationException {
        grundModelAttribute.setNewValue(true, grund, isBulkUpdate());
    }

    @Override
    public boolean checkDispensationUeberlapptAndereDispensationen(SchuelerDatenblattModel schuelerDatenblattModel) {
        CheckDispensationUeberlapptAndereDispensationenCommand checkDispensationUeberlapptAndereDispensationenCommand = new CheckDispensationUeberlapptAndereDispensationenCommand(dispensation, dispensationOrigin, schuelerDatenblattModel.getSchueler().getDispensationen());
        CommandInvoker commandInvoker = getCommandInvoker();
        commandInvoker.executeCommand(checkDispensationUeberlapptAndereDispensationenCommand);
        return checkDispensationUeberlapptAndereDispensationenCommand.isUeberlappt();
    }

    @Override
    public void speichern(DispensationenTableModel dispensationenTableModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        AddDispensationToSchuelerAndSaveCommand addDispensationToSchuelerAndSaveCommand = new AddDispensationToSchuelerAndSaveCommand(this.getDispensation(), dispensationOrigin, schuelerDatenblattModel.getSchueler());
        commandInvoker.executeCommandAsTransaction(addDispensationToSchuelerAndSaveCommand);
        Schueler schuelerUpdated = addDispensationToSchuelerAndSaveCommand.getSchuelerUpdated();
        // TableData mit von der Datenbank upgedatetem Schüler updaten
        if (schuelerUpdated != null) {
            dispensationenTableModel.getDispensationenTableData().setDispensationen(schuelerUpdated.getDispensationen());
        }
    }

    @Override
    public void initializeCompleted() {
        if (dispensationOrigin != null) {
            setBulkUpdate(true);
            try {
                setDispensationsbeginn(asString(dispensationOrigin.getDispensationsbeginn()));
                setDispensationsende(asString(dispensationOrigin.getDispensationsende()));
                setVoraussichtlicheDauer(dispensationOrigin.getVoraussichtlicheDauer());
                setGrund(dispensationOrigin.getGrund());
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
        return dispensation.getDispensationsbeginn() != null && checkNotEmpty(dispensation.getGrund());
    }

    @Override
    void doValidate() throws SvmValidationException {
        if (dispensation.getDispensationsbeginn() == null) {
            throw new SvmValidationException(2010, "Dispensationsbeginn obligatorisch", Field.DISPENSATIONSBEGINN);
        }
        if (dispensation.getGrund() == null) {
            throw new SvmValidationException(2011, "Grund obligatorisch", Field.GRUND);
        }
    }
}
