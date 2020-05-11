package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CallDefaultEmailClientCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;

import java.util.LinkedHashSet;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class EmailSemesterrechnungenModelImpl extends AbstractModel implements EmailSemesterrechnungenModel {

    private boolean rechnungsempfaengerSelected;
    private boolean mutterOderVaterSelected;
    private boolean blindkopien;
    private Set<String> fehlendeEmailAdressen;
    private Set<String> ungueltigeEmailAdressen;

    @Override
    public boolean isRechnungsempfaengerSelected() {
        return rechnungsempfaengerSelected;
    }

    @Override
    public boolean isMutterOderVaterSelected() {
        return mutterOderVaterSelected;
    }

    @Override
    public boolean isBlindkopien() {
        return blindkopien;
    }

    @Override
    public void setRechnungsempfaengerSelected(boolean isSelected) {
        boolean oldValue = rechnungsempfaengerSelected;
        rechnungsempfaengerSelected = isSelected;
        firePropertyChange(Field.RECHNUNGSEMPFAENGER, oldValue, rechnungsempfaengerSelected);
    }

    @Override
    public void setMutterOderVaterSelected(boolean isSelected) {
        boolean oldValue = mutterOderVaterSelected;
        mutterOderVaterSelected = isSelected;
        firePropertyChange(Field.MUTTER_ODER_VATER, oldValue, mutterOderVaterSelected);
    }

    @Override
    public void setBlindkopien(boolean isSelected) {
        boolean oldValue = blindkopien;
        blindkopien = isSelected;
        firePropertyChange(Field.BLINDKOPIEN, oldValue, blindkopien);
    }

    @Override
    public CallDefaultEmailClientCommand.Result callEmailClient(SemesterrechnungenTableModel semesterrechnungenTableModel) {

        Set<String> emailAdressen = new LinkedHashSet<>();
        fehlendeEmailAdressen = new LinkedHashSet<>();

        for (Semesterrechnung semesterrechnung : semesterrechnungenTableModel.getSelektierteSemesterrechnungen()) {
            Angehoeriger rechnungsempfaenger = semesterrechnung.getRechnungsempfaenger();
            Set<String> emailAdressenSemesterrechnung = new LinkedHashSet<>();

            // Rechnungsempfänger
            if (rechnungsempfaengerSelected && checkNotEmpty(rechnungsempfaenger.getEmail())) {
                emailAdressenSemesterrechnung.add(rechnungsempfaenger.getEmail());
            }

            // Mutter oder Vater (ODER Rechnungsempfänger ohne Email -> Mutter oder Vater)
            if (mutterOderVaterSelected || (rechnungsempfaengerSelected && emailAdressenSemesterrechnung.isEmpty())) {
                Set<Schueler> schuelerRechnungempfaenger = rechnungsempfaenger.getSchuelerRechnungsempfaenger();
                for (Schueler schueler : schuelerRechnungempfaenger) {
                    if (schueler.getMutter() != null && checkNotEmpty(schueler.getMutter().getEmail())) {
                        emailAdressenSemesterrechnung.add(schueler.getMutter().getEmail());
                    } else if (schueler.getVater() != null && checkNotEmpty(schueler.getVater().getEmail())) {
                        emailAdressenSemesterrechnung.add(schueler.getVater().getEmail());
                    }
                }
            }

            // Mutter und Vater ohne Email -> Rechnungsempfänger
            if (mutterOderVaterSelected && emailAdressenSemesterrechnung.isEmpty()) {
                emailAdressenSemesterrechnung.add(rechnungsempfaenger.getEmail());
            }

            // Mutter, Vater und Rechnungsempfänger ohne Email -> Schüler
            if ((rechnungsempfaengerSelected || mutterOderVaterSelected) && emailAdressenSemesterrechnung.isEmpty()) {
                Set<Schueler> schuelerRechnungempfaenger = rechnungsempfaenger.getSchuelerRechnungsempfaenger();
                for (Schueler schueler : schuelerRechnungempfaenger) {
                    if (checkNotEmpty(schueler.getEmail())) {
                        emailAdressenSemesterrechnung.add(schueler.getEmail());
                    }
                }
            }

            if (!emailAdressenSemesterrechnung.isEmpty()) {
                emailAdressen.addAll(emailAdressenSemesterrechnung);
            } else {
                fehlendeEmailAdressen.add(rechnungsempfaenger.getNachname() + " " + rechnungsempfaenger.getVorname());
            }
        }

        CommandInvoker commandInvoker = getCommandInvoker();
        CallDefaultEmailClientCommand callDefaultEmailClientCommand = new CallDefaultEmailClientCommand(emailAdressen, blindkopien);
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
    public void initializeCompleted() {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    void doValidate() throws SvmValidationException {
    }
}
