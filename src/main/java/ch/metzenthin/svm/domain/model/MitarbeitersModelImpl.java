package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CallDefaultEmailClientCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DeleteMitarbeiterCommand;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class MitarbeitersModelImpl extends AbstractModel implements MitarbeitersModel {

    private Set<String> fehlendeEmailAdressen;
    private Set<String> ungueltigeEmailAdressen;

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

        Set<String> emailAdressen = new LinkedHashSet<>();
        fehlendeEmailAdressen = new LinkedHashSet<>();

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
    public Set<String> getFehlendeEmailAdressen() {
        return fehlendeEmailAdressen;
    }

    @Override
    public Set<String> getUngueltigeEmailAdressen() {
        return ungueltigeEmailAdressen;
    }

    @Override
    void doValidate() throws SvmValidationException {}

    @Override
    public boolean isCompleted() {
        return false;
    }
}
