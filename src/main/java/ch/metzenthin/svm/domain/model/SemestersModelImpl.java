package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DeleteSemesterCommand;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class SemestersModelImpl extends AbstractModel implements SemestersModel {

    public SemestersModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public SemesterErfassenModel getSemesterErfassenModel(SvmContext svmContext, int indexSemesterToBeModified) {
        SemesterErfassenModel semesterErfassenModel = svmContext.getModelFactory().createSemesterErfassenModel();
        List<Semester> semesters = svmContext.getSvmModel().getSemestersAll();
        semesterErfassenModel.setSemesterOrigin(semesters.get(indexSemesterToBeModified));
        return semesterErfassenModel;
    }

    @Override
    public DeleteSemesterCommand.Result semesterLoeschen(SvmContext svmContext, int indexSemesterToBeRemoved) {
        List<Semester> semesters = svmContext.getSvmModel().getSemestersAll();
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteSemesterCommand deleteSemesterCommand = new DeleteSemesterCommand(semesters, indexSemesterToBeRemoved);
        commandInvoker.executeCommandAsTransaction(deleteSemesterCommand);
        return deleteSemesterCommand.getResult();
    }

    @Override
    void doValidate() throws SvmValidationException {}

    @Override
    public boolean isCompleted() {
        return false;
    }
}
