package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CallDefaultEmailClientCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DeleteMitarbeiterCommand;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;

import java.util.ArrayList;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class MitarbeitersModelImpl extends AbstractModel implements MitarbeitersModel {

    private List<String> fehlendeEmailAdressen;
    private List<String> ungueltigeEmailAdressen;

    MitarbeitersModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public MitarbeiterErfassenModel getMitarbeiterErfassenModel(SvmContext svmContext, MitarbeitersTableModel mitarbeitersTableModel, int indexMitarbeiterToBeModified) {
        MitarbeiterErfassenModel mitarbeiterErfassenModel = svmContext.getModelFactory().createMitarbeiterErfassenModel();
        List<Mitarbeiter> mitarbeiters = mitarbeitersTableModel.getMitarbeitersTableData().getMitarbeiters();
        mitarbeiterErfassenModel.setMitarbeiterOrigin(mitarbeiters.get(indexMitarbeiterToBeModified));
        return mitarbeiterErfassenModel;
    }

    @Override
    public DeleteMitarbeiterCommand.Result mitarbeiterLoeschen(MitarbeitersTableModel mitarbeitersTableModel, int indexMitarbeiterToBeRemoved) {
        List<Mitarbeiter> mitarbeiters = mitarbeitersTableModel.getMitarbeiters();
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteMitarbeiterCommand deleteMitarbeiterCommand = new DeleteMitarbeiterCommand(mitarbeiters, indexMitarbeiterToBeRemoved);
        commandInvoker.executeCommandAsTransaction(deleteMitarbeiterCommand);
        return deleteMitarbeiterCommand.getResult();
    }

    @Override
    public CallDefaultEmailClientCommand.Result callEmailClient(MitarbeitersTableModel mitarbeitersTableModel) {

        List<String> emailAdressen = new ArrayList<>();
        fehlendeEmailAdressen = new ArrayList<>();

        for (Mitarbeiter mitarbeiter : mitarbeitersTableModel.getMitarbeiters()) {
            if (!mitarbeiter.isSelektiert()) {
                continue;
            }
            if (checkNotEmpty(mitarbeiter.getEmail())) {
                emailAdressen.add(mitarbeiter.getEmail());
            } else {
                fehlendeEmailAdressen.add(mitarbeiter.getNachname() + " " + mitarbeiter.getVorname());
            }
        }

        CommandInvoker commandInvoker = getCommandInvoker();
        CallDefaultEmailClientCommand callDefaultEmailClientCommand = new CallDefaultEmailClientCommand(emailAdressen, false);
        commandInvoker.executeCommand(callDefaultEmailClientCommand);
        ungueltigeEmailAdressen = callDefaultEmailClientCommand.getUngueltigeEmailAdressen();

        return callDefaultEmailClientCommand.getResult();
    }

    @Override
    public List<String> getFehlendeEmailAdressen() {
        return fehlendeEmailAdressen;
    }

    @Override
    public List<String> getUngueltigeEmailAdressen() {
        return ungueltigeEmailAdressen;
    }

    @Override
    void doValidate() throws SvmValidationException {}

    @Override
    public boolean isCompleted() {
        return false;
    }
}
