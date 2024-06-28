package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
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
    private boolean mutterUndOderVaterSelected;
    private boolean blindkopien;
    private Set<String> fehlendeEmailAdressen;
    private Set<String> ungueltigeEmailAdressen;

    @Override
    public boolean isRechnungsempfaengerSelected() {
        return rechnungsempfaengerSelected;
    }

    @Override
    public boolean isMutterUndOderVaterSelected() {
        return mutterUndOderVaterSelected;
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
    public void setMutterUndOderVaterSelected(boolean isSelected) {
        boolean oldValue = mutterUndOderVaterSelected;
        mutterUndOderVaterSelected = isSelected;
        firePropertyChange(Field.MUTTER_ODER_VATER, oldValue, mutterUndOderVaterSelected);
    }

    @Override
    public void setBlindkopien(boolean isSelected) {
        boolean oldValue = blindkopien;
        blindkopien = isSelected;
        firePropertyChange(Field.BLINDKOPIEN, oldValue, blindkopien);
    }

    @SuppressWarnings("DuplicatedCode")
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

            // Mutter und/oder Vater (ODER Rechnungsempfänger ohne E-Mail -> Mutter und/oder Vater)
            if (mutterUndOderVaterSelected || (rechnungsempfaengerSelected && emailAdressenSemesterrechnung.isEmpty())) {
                Set<Schueler> schuelerRechnungempfaenger = rechnungsempfaenger.getSchuelerRechnungsempfaenger();

                // Mutter und/oder Vater mit wuenschtEmails selektiert
                for (Schueler schueler : schuelerRechnungempfaenger) {
                    if (schueler.getMutter() != null
                            && checkNotEmpty(schueler.getMutter().getEmail())
                            && schueler.getMutter().getWuenschtEmails() != null
                            && schueler.getMutter().getWuenschtEmails()) {
                        emailAdressenSemesterrechnung.add(schueler.getMutter().getEmail());
                    }
                    if (schueler.getVater() != null
                            && checkNotEmpty(schueler.getVater().getEmail())
                            && schueler.getVater().getWuenschtEmails() != null
                            && schueler.getVater().getWuenschtEmails()) {
                        emailAdressenSemesterrechnung.add(schueler.getVater().getEmail());
                    }
                }

                // Falls keine gefunden wuenschtEmails ignorieren (sollte nicht auftreten)
                if (emailAdressenSemesterrechnung.isEmpty()) {
                    for (Schueler schueler : schuelerRechnungempfaenger) {
                        if (schueler.getMutter() != null
                                && checkNotEmpty(schueler.getMutter().getEmail())) {
                            emailAdressenSemesterrechnung.add(schueler.getMutter().getEmail());
                        } else if (schueler.getVater() != null
                                && checkNotEmpty(schueler.getVater().getEmail())) {
                            emailAdressenSemesterrechnung.add(schueler.getVater().getEmail());
                        }
                    }
                }
            }

            // Mutter, Vater und Rechnungsempfänger ohne E-Mail -> Schüler
            if ((rechnungsempfaengerSelected || mutterUndOderVaterSelected)
                    && emailAdressenSemesterrechnung.isEmpty()) {
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
