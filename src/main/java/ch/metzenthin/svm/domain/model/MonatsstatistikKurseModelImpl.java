package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Schuljahre;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CalculateMonatsstatistikKurseCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static ch.metzenthin.svm.common.utils.Converter.getNMonthsAfterNow;

/**
 * @author Martin Schraner
 */
public class MonatsstatistikKurseModelImpl extends AbstractModel implements MonatsstatistikKurseModel {

    private Calendar monatJahr;

    private final CalendarModelAttribute monatJahrModelAttribute = new CalendarModelAttribute(
            this,
            Field.MONAT_JAHR, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1), getNMonthsAfterNow(12),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return monatJahr;
                }

                @Override
                public void setValue(Calendar value) {
                    monatJahr = value;
                }
            }
    );

    @Override
    public Calendar getMonatJahr() {
        return monatJahrModelAttribute.getValue();
    }

    @Override
    public void setMonatJahr(String monatJahr) throws SvmValidationException {
        monatJahrModelAttribute.setNewValue(true, monatJahr, MONAT_JAHR_DATE_FORMAT_STRING, isBulkUpdate());
    }

    @Override
    public int[] calculateMonatsstatistik() {
        CommandInvoker commandInvoker = getCommandInvoker();
        CalculateMonatsstatistikKurseCommand calculateMonatsstatistikKurseCommand = new CalculateMonatsstatistikKurseCommand(monatJahr);
        commandInvoker.executeCommand(calculateMonatsstatistikKurseCommand);
        return new int[]{calculateMonatsstatistikKurseCommand.getAnzahlAnmeldungen(), calculateMonatsstatistikKurseCommand.getAnzahlAbmeldungen(), calculateMonatsstatistikKurseCommand.getAnzahllLektionen()};
    }

    @Override
    public boolean isCompleted() {
        return monatJahr != null;
    }

    @Override
    void doValidate() throws SvmValidationException {
        if (monatJahr == null) {
            throw new SvmValidationException(2000, "Monat/Jahr obligatorisch", Field.MONAT_JAHR);
        }
    }
}
