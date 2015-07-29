package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CheckKursBereitsErfasstCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SaveOrUpdateKursCommand;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;

import java.sql.Time;
import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.isTimePeriodValid;

/**
 * @author Martin Schraner
 */
public class KursErfassenModelImpl extends AbstractModel implements KursErfassenModel {

    private static Lehrkraft LEHRKRAFT_KEINE = new Lehrkraft();

    private Kurs kurs = new Kurs();
    private Kurs kursOrigin;
    private Kurstyp kurstyp = new Kurstyp();
    private Kursort kursort = new Kursort();
    private Lehrkraft lehrkraft1 = new Lehrkraft();
    private Lehrkraft lehrkraft2 = new Lehrkraft();

    public KursErfassenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public Kurs getKurs() {
        return kurs;
    }

    @Override
    public void setKursOrigin(Kurs kursOrigin) {
        this.kursOrigin = kursOrigin;
    }

    @Override
    public Kurstyp getKurstyp() {
        return kurstyp;
    }

    @Override
    public void setKurstyp(Kurstyp kurstyp) throws SvmRequiredException {
        Kurstyp oldValue = this.kurstyp;
        this.kurstyp = kurstyp;
        firePropertyChange(Field.KURSTYP, oldValue, this.kurstyp);
        if (kurstyp == null) {
            invalidate();
            throw new SvmRequiredException(Field.KURSTYP);
        }
    }

    private final StringModelAttribute altersbereichModelAttribute = new StringModelAttribute(
            this,
            Field.ALTERSBEREICH, 2, 20,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return kurs.getAltersbereich();
                }

                @Override
                public void setValue(String value) {
                    kurs.setAltersbereich(value);
                }
            }
    );

    @Override
    public String getAltersbereich() {
        return altersbereichModelAttribute.getValue();
    }

    @Override
    public void setAltersbereich(String altersbereich) throws SvmValidationException {
        altersbereichModelAttribute.setNewValue(true, altersbereich, isBulkUpdate());
    }

    private final StringModelAttribute stufeModelAttribute = new StringModelAttribute(
            this,
            Field.STUFE, 2, 30,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return kurs.getStufe();
                }

                @Override
                public void setValue(String value) {
                    kurs.setStufe(value);
                }
            }
    );

    @Override
    public String getStufe() {
        return stufeModelAttribute.getValue();
    }

    @Override
    public void setStufe(String stufe) throws SvmValidationException {
        stufeModelAttribute.setNewValue(true, stufe, isBulkUpdate());
    }

    private final TimeModelAttribute zeitBeginnModelAttribute = new TimeModelAttribute(
            this,
            Field.ZEIT_BEGINN,
            new AttributeAccessor<Time>() {
                @Override
                public Time getValue() {
                    return kurs.getZeitBeginn();
                }

                @Override
                public void setValue(Time value) {
                    kurs.setZeitBeginn(value);
                }
            }
    );

    @Override
    public Time getZeitBeginn() {
        return zeitBeginnModelAttribute.getValue();
    }

    @Override
    public void setZeitBeginn(String zeitBeginn) throws SvmValidationException {
        zeitBeginnModelAttribute.setNewValue(true, zeitBeginn, isBulkUpdate());
        if (!isBulkUpdate() && kurs.getZeitBeginn() != null && kurs.getZeitEnde() != null && !isTimePeriodValid(kurs.getZeitBeginn(), kurs.getZeitEnde())) {
            kurs.setZeitBeginn(null);
            invalidate();
            throw new SvmValidationException(2042, "Keine gültige Zeitperiode", Field.ZEIT_BEGINN);
        }
    }

    private final TimeModelAttribute zeitEndeModelAttribute = new TimeModelAttribute(
            this,
            Field.ZEIT_ENDE,
            new AttributeAccessor<Time>() {
                @Override
                public Time getValue() {
                    return kurs.getZeitEnde();
                }

                @Override
                public void setValue(Time value) {
                    kurs.setZeitEnde(value);
                }
            }
    );

    @Override
    public Time getZeitEnde() {
        return zeitEndeModelAttribute.getValue();
    }

    @Override
    public void setZeitEnde(String zeitEnde) throws SvmValidationException {
        zeitEndeModelAttribute.setNewValue(true, zeitEnde, isBulkUpdate());
        if (!isBulkUpdate() && kurs.getZeitBeginn() != null && kurs.getZeitEnde() != null && !isTimePeriodValid(kurs.getZeitBeginn(), kurs.getZeitEnde())) {
            kurs.setZeitEnde(null);
            invalidate();
            throw new SvmValidationException(2043, "Keine gültige Zeitperiode", Field.ZEIT_ENDE);
        }
    }

    @Override
    public Wochentag getWochentag() {
        return kurs.getWochentag();
    }

    @Override
    public void setWochentag(Wochentag wochentag) throws SvmRequiredException {
        Wochentag oldValue = kurs.getWochentag();
        kurs.setWochentag(wochentag);
        firePropertyChange(Field.WOCHENTAG, oldValue, kurs.getWochentag());
        if (wochentag == null) {
            invalidate();
            throw new SvmRequiredException(Field.WOCHENTAG);
        }
    }

    @Override
    public Kursort getKursort() {
        return kursort;
    }

    @Override
    public void setKursort(Kursort kursort) throws SvmRequiredException {
        Kursort oldValue = this.kursort;
        this.kursort = kursort;
        firePropertyChange(Field.KURSORT, oldValue, this.kursort);
        if (kursort == null) {
            invalidate();
            throw new SvmRequiredException(Field.KURSORT);
        }
    }

    @Override
    public Lehrkraft[] getSelectableLehrkraefte1(SvmModel svmModel) {
        List<Lehrkraft> lehrkraefteList = svmModel.getAktiveLehrkraefteAll();
        addLehrkraefteOrigin(lehrkraefteList);
        return lehrkraefteList.toArray(new Lehrkraft[lehrkraefteList.size()]);
    }

    private void addLehrkraefteOrigin(List<Lehrkraft> lehrkraefteList) {
        if (kursOrigin != null) {
            // Lehrkraefte in kursOrigin müssen auch angezeigt werden, wenn die Lehrkraft nicht (mehr) aktiv ist
            for (Lehrkraft lehrkraft : kursOrigin.getLehrkraefte()) {
                if (!lehrkraefteList.contains(lehrkraft)) {
                    lehrkraefteList.add(lehrkraft);
                }
            }
        }
    }

    @Override
    public Lehrkraft getLehrkraft1() {
        return lehrkraft1;
    }

    @Override
    public void setLehrkraft1(Lehrkraft lehrkraft1) throws SvmRequiredException {
        Lehrkraft oldValue = this.lehrkraft1;
        this.lehrkraft1 = lehrkraft1;
        firePropertyChange(Field.LEHRKRAFT1, oldValue, this.lehrkraft1);
        if (lehrkraft1 == null) {
            invalidate();
            throw new SvmRequiredException(Field.LEHRKRAFT1);
        }
    }

    @Override
    public Lehrkraft[] getSelectableLehrkraefte2(SvmModel svmModel) {
        List<Lehrkraft> lehrkraefteList = svmModel.getAktiveLehrkraefteAll();
        // Lehrkraft2 kann auch leer sein
        lehrkraefteList.add(0, LEHRKRAFT_KEINE);
        addLehrkraefteOrigin(lehrkraefteList);
        return lehrkraefteList.toArray(new Lehrkraft[lehrkraefteList.size()]);
    }

    @Override
    public Lehrkraft getLehrkraft2() {
        return lehrkraft2;
    }

    @Override
    public void setLehrkraft2(Lehrkraft lehrkraft2) {
        if (lehrkraft2 == LEHRKRAFT_KEINE) {
            lehrkraft2 = null;
        }
        Lehrkraft oldValue = this.lehrkraft2;
        this.lehrkraft2 = lehrkraft2;
        firePropertyChange(Field.LEHRKRAFT2, oldValue, this.lehrkraft2);
    }

    private final StringModelAttribute bemerkungenModelAttribute = new StringModelAttribute(
            this,
            Field.BEMERKUNGEN, 2, 100,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return kurs.getBemerkungen();
                }

                @Override
                public void setValue(String value) {
                    kurs.setBemerkungen(value);
                }
            }
    );

    @Override
    public String getBemerkungen() {
        return bemerkungenModelAttribute.getValue();
    }

    @Override
    public void setBemerkungen(String bemerkungen) throws SvmValidationException {
        bemerkungenModelAttribute.setNewValue(false, bemerkungen, isBulkUpdate());
    }

    @Override
    public boolean checkKursBereitsErfasst(KurseTableModel kurseTableModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        CheckKursBereitsErfasstCommand checkKursBereitsErfasstCommand = new CheckKursBereitsErfasstCommand(kurs, kursOrigin, kurseTableModel.getKurse());
        commandInvoker.executeCommand(checkKursBereitsErfasstCommand);
        return checkKursBereitsErfasstCommand.isBereitsErfasst();
    }

    @Override
    public void speichern(SvmModel svmModel, KurseSemesterwahlModel kurseSemesterwahlModel, KurseTableModel kurseTableModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        SaveOrUpdateKursCommand saveOrUpdateKursCommand = new SaveOrUpdateKursCommand(kurs, kurseSemesterwahlModel.getSemester(svmModel), kurstyp, kursort, lehrkraft1, lehrkraft2, kursOrigin, kurseTableModel.getKurse());
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursCommand);
    }

    @Override
    public void initializeCompleted() {
        if (kursOrigin != null) {
            setBulkUpdate(true);
            try {
                setKurstyp(kursOrigin.getKurstyp());
                setAltersbereich(kursOrigin.getAltersbereich());
                setStufe(kursOrigin.getStufe());
                setWochentag(kursOrigin.getWochentag());
                setZeitBeginn(asString(kursOrigin.getZeitBeginn()));
                setZeitEnde(asString(kursOrigin.getZeitEnde()));
                setKursort(kursOrigin.getKursort());
                setLehrkraft1(kursOrigin.getLehrkraefte().get(0));
                if (kursOrigin.getLehrkraefte().size() > 1) {
                    setLehrkraft2(kursOrigin.getLehrkraefte().get(1));
                }
                setBemerkungen(kursOrigin.getBemerkungen());
            } catch (SvmValidationException ignore) {
                ignore.printStackTrace();
            }
            setBulkUpdate(false);
        } else {
            super.initializeCompleted();
        }
    }

    @Override
    public boolean isCompleted() {
        return !lehrkraft1.isIdenticalWith(lehrkraft2);
    }

    @Override
    void doValidate() throws SvmValidationException {
        if (!isBulkUpdate() && lehrkraft1.isIdenticalWith(lehrkraft2)) {
            throw new SvmValidationException(2033, "Lehrkräfte 1 und 2 dürfen nicht identisch sein", Field.LEHRKRAFT2);
        }
    }
}
