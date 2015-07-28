package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CheckSemesterBereitsErfasstCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.FindKurseSemesterCommand;
import ch.metzenthin.svm.domain.commands.FindSemesterForSchuljahrSemesterbezeichnungCommand;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class KurseSemesterwahlModelImpl extends AbstractModel implements KurseSemesterwahlModel {

    private String schuljahr;
    private Semesterbezeichnung semesterbezeichnung;

    public KurseSemesterwahlModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    private final StringModelAttribute schuljahrModelAttribute = new StringModelAttribute(
            this,
            Field.SCHULJAHR, 9, 9,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return schuljahr;
                }

                @Override
                public void setValue(String value) {
                    schuljahr = value;
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

    @Override
    public Semesterbezeichnung getSemesterbezeichnung() {
        return semesterbezeichnung;
    }

    @Override
    public void setSemesterbezeichnung(Semesterbezeichnung semesterbezeichnung) {
        Semesterbezeichnung oldValue = this.semesterbezeichnung;
        this.semesterbezeichnung = semesterbezeichnung;
        firePropertyChange(Field.SEMESTERBEZEICHNUNG, oldValue, this.semesterbezeichnung);
    }

    @Override
    public boolean checkSemesterBereitsErfasst(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        CheckSemesterBereitsErfasstCommand checkSemesterBereitsErfasstCommand = new CheckSemesterBereitsErfasstCommand(new Semester(schuljahr, semesterbezeichnung, null, null, 0), null, svmModel.getSemestersAll());
        commandInvoker.executeCommand(checkSemesterBereitsErfasstCommand);
        return checkSemesterBereitsErfasstCommand.isBereitsErfasst();
    }

    @Override
    public KurseTableData suchen() {
        CommandInvoker commandInvoker = getCommandInvoker();
        FindKurseSemesterCommand findKurseSemesterCommand = new FindKurseSemesterCommand(new Semester(schuljahr, semesterbezeichnung, null, null, 0));
        commandInvoker.executeCommand(findKurseSemesterCommand);
        List<Kurs> kurseFound = findKurseSemesterCommand.getKurseFound();
        return new KurseTableData(kurseFound, false);
    }

    @Override
    public Semester getSemester(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        FindSemesterForSchuljahrSemesterbezeichnungCommand findSemesterForSchuljahrSemesterbezeichnungCommand = new FindSemesterForSchuljahrSemesterbezeichnungCommand(schuljahr, semesterbezeichnung, svmModel.getSemestersAll());
        commandInvoker.executeCommand(findSemesterForSchuljahrSemesterbezeichnungCommand);
        return findSemesterForSchuljahrSemesterbezeichnungCommand.getSemesterFound();
    }

    @Override
    void doValidate() throws SvmValidationException {}

    @Override
    public boolean isCompleted() {
        return true;
    }
}
