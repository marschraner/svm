package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.MonatsstatistikSchuelerSuchenCommand;
import ch.metzenthin.svm.persistence.SvmDbException;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class MonatsstatistikModelImpl extends AbstractModel implements MonatsstatistikModel {

    private static Calendar MONAT_JAHR_INIT;
    private static final AnAbmeldungenDispensationenSelected AN_ABMELDUNGEN_DISPENSATIONEN_SELECTED_INIT = AnAbmeldungenDispensationenSelected.ANMELDUNGEN;

    private Calendar monatJahr = MONAT_JAHR_INIT;
    private AnAbmeldungenDispensationenSelected anAbmeldungenDispensationen = AN_ABMELDUNGEN_DISPENSATIONEN_SELECTED_INIT;

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

    static {
        MONAT_JAHR_INIT = new GregorianCalendar();
        MONAT_JAHR_INIT.add(Calendar.MONTH, -1);
    }

    @Override
    public Calendar getMonatJahr() {
        return monatJahrModelAttribute.getValue();
    }

    @Override
    public Calendar getMonatJahrInit() {
        return MONAT_JAHR_INIT;
    }

    @Override
    public void setMonatJahr(String monatJahr) throws SvmValidationException {
        monatJahrModelAttribute.setNewValue(true, monatJahr, MONAT_JAHR_DATE_FORMAT_STRING);
    }

    @Override
    public AnAbmeldungenDispensationenSelected getAnAbmeldungenDispensationen() {
        return anAbmeldungenDispensationen;
    }

    @Override
    public AnAbmeldungenDispensationenSelected getAnAbmeldungenDispensationenInit() {
        return AN_ABMELDUNGEN_DISPENSATIONEN_SELECTED_INIT;
    }

    @Override
    public void setAnAbmeldungenDispensationen(AnAbmeldungenDispensationenSelected anAbmeldungenDispensationen) {
        AnAbmeldungenDispensationenSelected oldValue = this.anAbmeldungenDispensationen;
        this.anAbmeldungenDispensationen = anAbmeldungenDispensationen;
        firePropertyChange(Field.AN_ABMELDUNGEN_DISPENSATIONEN, oldValue, this.anAbmeldungenDispensationen);
    }

    @Override
    public SchuelerSuchenResult suchen() throws SvmDbException {
        MonatsstatistikSchuelerSuchenCommand monatsstatistikSchuelerSuchenCommand = new MonatsstatistikSchuelerSuchenCommand(this);
        CommandInvoker commandInvoker = getCommandInvoker();
        try {
            commandInvoker.openSession();
            commandInvoker.executeCommandWithinSession(monatsstatistikSchuelerSuchenCommand);
        } catch (Throwable e) {
            throw new SvmDbException("Fehler beim Ausführen eines DB-Commands", e);
        }
        List<Schueler> schuelerList = monatsstatistikSchuelerSuchenCommand.getSchuelerFound();
        return new SchuelerSuchenResult(schuelerList);
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
