package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.*;

import java.util.*;

/**
 * @author Martin Schraner
 */
public class MonatsstatistikSchuelerModelImpl extends AbstractModel implements MonatsstatistikSchuelerModel {

    private Calendar monatJahr;
    private AnAbmeldungenDispensationenSelected anAbmeldungenDispensationen;

    public MonatsstatistikSchuelerModelImpl(CommandInvoker commandInvoker) {
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
    public SchuelerSuchenTableData suchen(SvmModel svmModel) {
        MonatsstatistikSchuelerSuchenCommand monatsstatistikSchuelerSuchenCommand = new MonatsstatistikSchuelerSuchenCommand(this);
        CommandInvoker commandInvoker = getCommandInvoker();
        commandInvoker.executeCommand(monatsstatistikSchuelerSuchenCommand);
        List<Schueler> schuelerList = monatsstatistikSchuelerSuchenCommand.getSchuelerFound();
        Semester semesterTableData = determineSemesterTableData(svmModel);
        Map<Schueler, List<Kurs>> kurseMapTableData = determineKurseMapTableData(schuelerList, semesterTableData);
        Maerchen maerchenTableData = determineMaerchenTableData(svmModel, semesterTableData);
        Map<Schueler, Maercheneinteilung> maercheneinteilungenMapTableData = determineMaercheneinteilungenMapTableData(schuelerList, maerchenTableData);
        return new SchuelerSuchenTableData(schuelerList, kurseMapTableData, semesterTableData, null, null, null, maercheneinteilungenMapTableData, maerchenTableData, null, null, false, false);
    }

    private Semester determineSemesterTableData(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        List<Semester> erfassteSemester = svmModel.getSemestersAll();
        Calendar lastDayOfMonth = new GregorianCalendar(monatJahr.get(Calendar.YEAR), monatJahr.get(Calendar.MONTH), monatJahr.getActualMaximum(Calendar.DAY_OF_MONTH));
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(lastDayOfMonth, erfassteSemester);
        commandInvoker.executeCommand(findSemesterForCalendarCommand);
        Semester currentSemester = findSemesterForCalendarCommand.getCurrentSemester();
        Semester nextSemester = findSemesterForCalendarCommand.getNextSemester();
        // Wenn in Ferien zwischen 2 Semestern Folgesemester nehmen
        return  (currentSemester == null ? nextSemester : currentSemester);
    }

    private Map<Schueler, List<Kurs>> determineKurseMapTableData(List<Schueler> schuelerList, Semester semester) {
        CommandInvoker commandInvoker = getCommandInvoker();
        FindKurseMapSchuelerSemesterCommand findKurseMapSchuelerSemesterCommand = new FindKurseMapSchuelerSemesterCommand(schuelerList, semester, null, null, null);
        commandInvoker.executeCommand(findKurseMapSchuelerSemesterCommand);
        return findKurseMapSchuelerSemesterCommand.getKurseMap();
    }

    private Maerchen determineMaerchenTableData(SvmModel svmModel, Semester semesterTableData) {
        // Kein Märchen für 2. Semester
        if (semesterTableData.getSemesterbezeichnung() == Semesterbezeichnung.ZWEITES_SEMESTER) {
            return null;
        }
        List<Maerchen> erfassteMaerchen = svmModel.getMaerchensAll();
        for (Maerchen maerchen : erfassteMaerchen) {
            if (maerchen.getSchuljahr().equals(semesterTableData.getSchuljahr())) {
                return maerchen;
            }
        }
        return null;
    }

    private Map<Schueler,Maercheneinteilung> determineMaercheneinteilungenMapTableData(List<Schueler> schuelerList, Maerchen maerchen) {
        FindMaercheneinteilungenMapSchuelerSemesterCommand findMaercheneinteilungenMapSchuelerSemesterCommand = new FindMaercheneinteilungenMapSchuelerSemesterCommand(schuelerList, maerchen);
        findMaercheneinteilungenMapSchuelerSemesterCommand.execute();
        return findMaercheneinteilungenMapSchuelerSemesterCommand.getMaercheneinteilungenMap();
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
