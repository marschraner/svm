package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.MonatsstatistikSchuelerSuchenCommand;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class MonatsstatistikModelImpl extends AbstractModel implements MonatsstatistikModel {

    private Calendar monatJahr;
    private AnAbmeldungenDispensationenSelected anAbmeldungenDispensationen;

    public MonatsstatistikModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    private final CalendarModelAttribute monatJahrModelAttribute = new CalendarModelAttribute(
            this,
            Field.MONAT_JAHR, new GregorianCalendar(2000, Calendar.JANUARY, 1), new GregorianCalendar(),
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
    public AnAbmeldungenDispensationenSelected getAnAbmeldungenDispensationen() {
        return anAbmeldungenDispensationen;
    }

    @Override
    public void setAnAbmeldungenDispensationen(AnAbmeldungenDispensationenSelected anAbmeldungenDispensationen) {
        AnAbmeldungenDispensationenSelected oldValue = this.anAbmeldungenDispensationen;
        this.anAbmeldungenDispensationen = anAbmeldungenDispensationen;
        firePropertyChange(Field.AN_ABMELDUNGEN_DISPENSATIONEN, oldValue, this.anAbmeldungenDispensationen);
    }

    @Override
    public SchuelerSuchenTableData suchen() {
        MonatsstatistikSchuelerSuchenCommand monatsstatistikSchuelerSuchenCommand = new MonatsstatistikSchuelerSuchenCommand(this);
        CommandInvoker commandInvoker = getCommandInvoker();
        commandInvoker.executeCommand(monatsstatistikSchuelerSuchenCommand);
        List<Schueler> schuelerList = monatsstatistikSchuelerSuchenCommand.getSchuelerFound();
        return new SchuelerSuchenTableData(schuelerList);
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
