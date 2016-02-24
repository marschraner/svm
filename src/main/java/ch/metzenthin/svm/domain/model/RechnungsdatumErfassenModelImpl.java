package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.ReplaceRechnungsdatumAndUpdateSemesterrechnungenCommand;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.util.Calendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.getNMonthsAfterNow;
import static ch.metzenthin.svm.common.utils.Converter.getNYearsBeforeNow;

/**
 * @author Martin Schraner
 */
public class RechnungsdatumErfassenModelImpl extends AbstractModel implements RechnungsdatumErfassenModel {

    private Calendar rechnungsdatum;

    public RechnungsdatumErfassenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    private final CalendarModelAttribute rechnungsdatumModelAttribute = new CalendarModelAttribute(
            this,
            Field.RECHNUNGSDATUM, getNYearsBeforeNow(10), getNMonthsAfterNow(2),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return rechnungsdatum;
                }

                @Override
                public void setValue(Calendar value) {
                    rechnungsdatum = value;
                }
            }
    );

    @Override
    public Calendar getRechnungsdatum() {
        return rechnungsdatumModelAttribute.getValue();
    }

    @Override
    public void setRechnungsdatum(String rechnungsdatum) throws SvmValidationException {
        rechnungsdatumModelAttribute.setNewValue(false, rechnungsdatum, isBulkUpdate());
    }

    @Override
    public void replaceRechnungsdatumAndUpdateSemesterrechnung(List<Semesterrechnung> semesterrechnungen, Rechnungstyp rechnungstyp) {
        CommandInvoker commandInvoker = getCommandInvoker();
        ReplaceRechnungsdatumAndUpdateSemesterrechnungenCommand replaceRechnungsdatumAndUpdateSemesterrechnungenCommand = new ReplaceRechnungsdatumAndUpdateSemesterrechnungenCommand(semesterrechnungen, rechnungstyp, rechnungsdatum);
        commandInvoker.executeCommandAsTransaction(replaceRechnungsdatumAndUpdateSemesterrechnungenCommand);
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    void doValidate() throws SvmValidationException {}
}
