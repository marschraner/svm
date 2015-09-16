package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SvmModelImpl implements SvmModel {

    private CommandInvoker commandInvoker;

    public SvmModelImpl(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    @Override
    public List<SchuelerCode> getSchuelerCodesAll() {
        FindAllSchuelerCodesCommand findAllSchuelerCodesCommand = new FindAllSchuelerCodesCommand();
        commandInvoker.executeCommand(findAllSchuelerCodesCommand);
        return findAllSchuelerCodesCommand.getSchuelerCodesAll();
    }

    @Override
    public List<SchuelerCode> getSelektierbareSchuelerCodesAll() {
        List<SchuelerCode> selektierbareSchuelerCodesAll = new ArrayList<>();
        for (SchuelerCode schuelerCode : getSchuelerCodesAll()) {
            if (schuelerCode.getSelektierbar()) {
                selektierbareSchuelerCodesAll.add(schuelerCode);
            }
        }
        return selektierbareSchuelerCodesAll;
    }

    @Override
    public List<ElternmithilfeCode> getElternmithilfeCodesAll() {
        FindAllElternmithilfeCodesCommand findAllElternmithilfeCodesCommand = new FindAllElternmithilfeCodesCommand();
        commandInvoker.executeCommand(findAllElternmithilfeCodesCommand);
        return findAllElternmithilfeCodesCommand.getElternmithilfeCodesAll();
    }

    @Override
    public List<ElternmithilfeCode> getSelektierbareElternmithilfeCodesAll() {
        List<ElternmithilfeCode> selektierbareElternmithilfeCodesAll = new ArrayList<>();
        for (ElternmithilfeCode elternmithilfeCode : getElternmithilfeCodesAll()) {
            if (elternmithilfeCode.getSelektierbar()) {
                selektierbareElternmithilfeCodesAll.add(elternmithilfeCode);
            }
        }
        return selektierbareElternmithilfeCodesAll;
    }

    @Override
    public List<SemesterrechnungCode> getSemesterrechnungCodesAll() {
        FindAllSemesterrechnungCodesCommand findAllSemesterrechnungCodesCommand = new FindAllSemesterrechnungCodesCommand();
        commandInvoker.executeCommand(findAllSemesterrechnungCodesCommand);
        return findAllSemesterrechnungCodesCommand.getSemesterrechnungCodesAll();
    }

    @Override
    public List<SemesterrechnungCode> getSelektierbareSemesterrechnungCodesAll() {
        List<SemesterrechnungCode> selektierbareSemesterrechnungCodesAll = new ArrayList<>();
        for (SemesterrechnungCode semesterrechnungCode : getSemesterrechnungCodesAll()) {
            if (semesterrechnungCode.getSelektierbar()) {
                selektierbareSemesterrechnungCodesAll.add(semesterrechnungCode);
            }
        }
        return selektierbareSemesterrechnungCodesAll;
    }

    @Override
    public List<Mitarbeiter> getMitarbeitersAll() {
        FindAllMitarbeitersCommand findAllMitarbeitersCommand = new FindAllMitarbeitersCommand();
        commandInvoker.executeCommand(findAllMitarbeitersCommand);
        return findAllMitarbeitersCommand.getLehrkraefteAll();
    }

    @Override
    public List<Mitarbeiter> getAktiveLehrkraefteAll() {
        List<Mitarbeiter> aktiveLehrkraefteAll = new ArrayList<>();
        for (Mitarbeiter mitarbeiter : getMitarbeitersAll()) {
            if (mitarbeiter.getAktiv()) {
                aktiveLehrkraefteAll.add(mitarbeiter);
            }
        }
        return aktiveLehrkraefteAll;
    }

    @Override
    public List<Kursort> getKursorteAll() {
        FindAllKursorteCommand findAllKursorteCommand = new FindAllKursorteCommand();
        commandInvoker.executeCommand(findAllKursorteCommand);
        return findAllKursorteCommand.getKursorteAll();
    }

    @Override
    public List<Kursort> getSelektierbareKursorteAll() {
        List<Kursort> selektierbareKursorteAll = new ArrayList<>();
        for (Kursort kursort : getKursorteAll()) {
            if (kursort.getSelektierbar()) {
                selektierbareKursorteAll.add(kursort);
            }
        }
        return selektierbareKursorteAll;
    }

    @Override
    public List<Kurstyp> getKurstypenAll() {
        FindAllKurstypenCommand findAllKurstypenCommand = new FindAllKurstypenCommand();
        commandInvoker.executeCommand(findAllKurstypenCommand);
        return findAllKurstypenCommand.getKurstypenAll();
    }

    @Override
    public List<Kurstyp> getSelektierbareKurstypenAll() {
        List<Kurstyp> selektierbareKurstypenAll = new ArrayList<>();
        for (Kurstyp kurstyp : getKurstypenAll()) {
            if (kurstyp.getSelektierbar()) {
                selektierbareKurstypenAll.add(kurstyp);
            }
        }
        return selektierbareKurstypenAll;
    }

    @Override
    public List<Semester> getSemestersAll() {
        FindAllSemestersCommand findAllSemestersCommand = new FindAllSemestersCommand();
        commandInvoker.executeCommand(findAllSemestersCommand);
        return findAllSemestersCommand.getSemestersAll();
    }

    @Override
    public List<Maerchen> getMaerchensAll() {
        FindAllMaerchensCommand findAllMaerchensCommand = new FindAllMaerchensCommand();
        commandInvoker.executeCommand(findAllMaerchensCommand);
        return findAllMaerchensCommand.getMaerchensAll();
    }

    @Override
    public List<Lektionsgebuehren> getLektionsgebuehrenAllList() {
        FindAllLektionsgebuehrenCommand findAllLektionsgebuehrenCommand = new FindAllLektionsgebuehrenCommand();
        commandInvoker.executeCommand(findAllLektionsgebuehrenCommand);
        return findAllLektionsgebuehrenCommand.getLektionsgebuehrenAllList();
    }
}
