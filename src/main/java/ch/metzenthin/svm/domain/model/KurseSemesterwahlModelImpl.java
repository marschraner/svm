package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DetermineSemesterInitCommand;
import ch.metzenthin.svm.domain.commands.FindKurseSemesterCommand;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class KurseSemesterwahlModelImpl extends AbstractModel implements KurseSemesterwahlModel {

    private Semester semester;

    @Override
    public Semester getSemester() {
        return semester;
    }

    @Override
    public void setSemester(Semester semester) {
        Semester oldValue = this.semester;
        this.semester = semester;
        firePropertyChange(Field.SEMESTER, oldValue, this.semester);
    }

    @Override
    public Semester getInitSemester(SvmModel svmModel) {
        DetermineSemesterInitCommand determineSemesterInitCommand = new DetermineSemesterInitCommand(svmModel, 40);
        determineSemesterInitCommand.execute();
        return determineSemesterInitCommand.getSemesterInit();
    }

    @Override
    public KurseTableData suchen() {
        CommandInvoker commandInvoker = getCommandInvoker();
        FindKurseSemesterCommand findKurseSemesterCommand = new FindKurseSemesterCommand(semester);
        commandInvoker.executeCommand(findKurseSemesterCommand);
        List<Kurs> kurseFound = findKurseSemesterCommand.getKurseFound();
        return new KurseTableData(kurseFound);
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }
}
