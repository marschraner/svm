package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;

import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.Converter.getNMonthsAfterNow;
import static ch.metzenthin.svm.common.utils.Converter.getNMonthsBeforeNow;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class DispensationErfassenModelImpl extends AbstractModel implements DispensationErfassenModel {

    private Calendar dispensationsbeginn;
    private Calendar dispensationsende;
    private String voraussichtlicheDauer;
    private String grund;

    public DispensationErfassenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    private final CalendarModelAttribute dispensationsbeginnModelAttribute = new CalendarModelAttribute(
            this,
            Field.DISPENSATIONSBEGINN, getNMonthsBeforeNow(3), getNMonthsAfterNow(1),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return dispensationsbeginn;
                }

                @Override
                public void setValue(Calendar value) {
                    dispensationsbeginn = value;
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
        if (!isBulkUpdate() && this.dispensationsbeginn != null && dispensationsende != null && this.dispensationsbeginn.after(dispensationsende)) {
            this.dispensationsbeginn = null;
            invalidate();
            throw new SvmValidationException(2012, "Keine gültige Periode", Field.DISPENSATIONSBEGINN);
        }
    }

    private final CalendarModelAttribute dispensationsendeModelAttribute = new CalendarModelAttribute(
            this,
            Field.DISPENSATIONSENDE, getNMonthsBeforeNow(3), getNMonthsAfterNow(36),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return dispensationsende;
                }

                @Override
                public void setValue(Calendar value) {
                    dispensationsende = value;
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
        if (!isBulkUpdate() && dispensationsbeginn != null && this.dispensationsende != null && dispensationsbeginn.after(this.dispensationsende)) {
            this.dispensationsende = null;
            invalidate();
            throw new SvmValidationException(2013, "Keine gültige Periode", Field.DISPENSATIONSENDE);
        }
    }

    private final StringModelAttribute voraussichtlicheDauerModelAttribute = new StringModelAttribute(
            this,
            Field.VORAUSSICHTLICHE_DAUER, 5, 20,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return voraussichtlicheDauer;
                }

                @Override
                public void setValue(String value) {
                    voraussichtlicheDauer = value;
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
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return grund;
                }

                @Override
                public void setValue(String value) {
                    grund = value;
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
    public boolean isCompleted() {
        return dispensationsbeginn != null && checkNotEmpty(grund);
    }

    @Override
    void doValidate() throws SvmValidationException {
        if (dispensationsbeginn == null) {
            throw new SvmValidationException(2010, "Dispensationsbeginn obligatorisch", Field.DISPENSATIONSBEGINN);
        }
        if (grund == null) {
            throw new SvmValidationException(2011, "Grund obligatorisch", Field.GRUND);
        }
    }
}
