package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DeleteKursanmeldungCommand;
import ch.metzenthin.svm.domain.commands.SaveSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.componentmodel.KursanmeldungenTableModel;

import java.util.*;

/**
 * @author Martin Schraner
 */
class KursanmeldungenModelImpl extends AbstractModel implements KursanmeldungenModel {

    @Override
    public KursanmeldungErfassenModel getKursanmeldungErfassenModel(SvmContext svmContext, KursanmeldungenTableModel kursanmeldungenTableModel, int rowSelected) {
        KursanmeldungErfassenModel kursanmeldungErfassenModel = svmContext.getModelFactory().createKursanmeldungErfassenModel();
        Kursanmeldung kursanmeldungSelected = kursanmeldungenTableModel.getKursanmeldungSelected(rowSelected);
        kursanmeldungErfassenModel.setKursanmeldungOrigin(kursanmeldungSelected);
        return kursanmeldungErfassenModel;
    }

    @Override
    public void kursanmeldungLoeschen(KursanmeldungenTableModel kursanmeldungenTableModel, SchuelerDatenblattModel schuelerDatenblattModel, int rowSelected) {
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteKursanmeldungCommand deleteKursanmeldungCommand
                = new DeleteKursanmeldungCommand(
                        schuelerDatenblattModel.getSchueler().getSortedKursanmeldungen(),
                rowSelected);
        commandInvoker.executeCommandAsTransaction(deleteKursanmeldungCommand);
        // TableData mit von der Datenbank upgedateter Kursanmeldung updaten
        kursanmeldungenTableModel.getKursanmeldungenTableData().setKursanmeldungen(
                schuelerDatenblattModel.getSchueler().getSortedKursanmeldungen());
    }

    /*
        @return spätestes Abmeldedatum oder null, falls es nicht abgemeldete Kurse gibt
     */
    @Override
    public Calendar getSpaetestesAbmeldedatumKurseNeustesSemester(SchuelerDatenblattModel schuelerDatenblattModel) {
        List<Kursanmeldung> kursanmeldungen
                = schuelerDatenblattModel.getSchueler().getKursanmeldungen();
        Semester neustesSemester = getNeustesSemester(kursanmeldungen);
        List<Kursanmeldung> kursanmeldungenNeustesSemester = getKursanmeldungenNeustesSemester(kursanmeldungen, neustesSemester);
        if (kursanmeldungenNeustesSemester.isEmpty()) {
            return null;
        }
        Calendar spaetestesAbmeldedatum = neustesSemester.getSemesterbeginn();
        for (Kursanmeldung kursanmeldung : kursanmeldungenNeustesSemester) {
            if (kursanmeldung.getAbmeldedatum() == null) {
                return null;
            } else if (kursanmeldung.getAbmeldedatum().after(spaetestesAbmeldedatum)) {
                spaetestesAbmeldedatum = kursanmeldung.getAbmeldedatum();
            }
        }
        return spaetestesAbmeldedatum;
    }

    private List<Kursanmeldung> getKursanmeldungenNeustesSemester(Collection<Kursanmeldung> kursanmeldungen, Semester neustesSemester) {
        List<Kursanmeldung> kursanmeldungenNeustesSemester = new ArrayList<>();
        for (Kursanmeldung kursanmeldung : kursanmeldungen) {
            if (kursanmeldung.getKurs().getSemester().getSemesterId().equals(neustesSemester.getSemesterId())) {
                kursanmeldungenNeustesSemester.add(kursanmeldung);
            }
        }
        return kursanmeldungenNeustesSemester;
    }

    private Semester getNeustesSemester(Collection<Kursanmeldung> kursanmeldungen) {
        List<Semester> semesters = new ArrayList<>();
        for (Kursanmeldung kursanmeldung : kursanmeldungen) {
            semesters.add(kursanmeldung.getKurs().getSemester());
        }
        Collections.sort(semesters);
        return semesters.get(0);
    }

    @Override
    public void schuelerVomKinderUndJugendtheaterAbmelden(SchuelerDatenblattModel schuelerDatenblattModel, Calendar abmeldedatum) {
        Schueler schueler = schuelerDatenblattModel.getSchueler();
        schueler.getAnmeldungen().get(0).setAbmeldedatum(abmeldedatum);
        CommandInvoker commandInvoker = getCommandInvoker();
        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler);
        commandInvoker.executeCommandAsTransaction(saveSchuelerCommand);
    }

    @Override
    public boolean isSchuelerAbgemeldet(SchuelerDatenblattModel schuelerDatenblattModel) {
        return schuelerDatenblattModel.getSchueler().isAbgemeldet();
    }

    @Override
    void doValidate() throws SvmValidationException {
        // Keine feldübergreifende Validierung notwendig
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

}
