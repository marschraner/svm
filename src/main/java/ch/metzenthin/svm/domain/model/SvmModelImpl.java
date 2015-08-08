package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SvmModelImpl implements SvmModel {

    private List<SchuelerCode> schuelerCodesAll;
    private List<ElternmithilfeCode> elternmithilfeCodesAll;
    private List<Lehrkraft> lehrkraefteAll;
    private List<Kursort> kursorteAll;
    private List<Kurstyp> kurstypenAll;
    private List<Semester> semestersAll;
    private List<Maerchen> maerchensAll;
    private CommandInvoker commandInvoker;

    public SvmModelImpl(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
        loadSchuelerCodesAll();
        loadElternmithilfeCodesAll();
        loadLehrkraefteAll();
        loadKursorteAll();
        loadKurstypenAll();
        loadSemestersAll();
        loadMaerchensAll();
    }

    @Override
    public void loadSchuelerCodesAll() {
        FindAllSchuelerCodesCommand findAllSchuelerCodesCommand = new FindAllSchuelerCodesCommand();
        commandInvoker.executeCommand(findAllSchuelerCodesCommand);
        schuelerCodesAll = findAllSchuelerCodesCommand.getSchuelerCodesAll();
    }

    @Override
    public void loadElternmithilfeCodesAll() {
        FindAllElternmithilfeCodesCommand findAllElternmithilfeCodesCommand = new FindAllElternmithilfeCodesCommand();
        commandInvoker.executeCommand(findAllElternmithilfeCodesCommand);
        elternmithilfeCodesAll = findAllElternmithilfeCodesCommand.getElternmithilfeCodesAll();
    }

    @Override
    public void loadLehrkraefteAll() {
        FindAllLehrkraefteCommand findAllLehrkraefteCommand = new FindAllLehrkraefteCommand();
        commandInvoker.executeCommand(findAllLehrkraefteCommand);
        lehrkraefteAll = findAllLehrkraefteCommand.getLehrkraefteAll();
    }

    @Override
    public void loadKursorteAll() {
        FindAllKursorteCommand findAllKursorteCommand = new FindAllKursorteCommand();
        commandInvoker.executeCommand(findAllKursorteCommand);
        kursorteAll = findAllKursorteCommand.getKursorteAll();
    }

    @Override
    public void loadKurstypenAll() {
        FindAllKurstypenCommand findAllKurstypenCommand = new FindAllKurstypenCommand();
        commandInvoker.executeCommand(findAllKurstypenCommand);
        kurstypenAll = findAllKurstypenCommand.getKurstypenAll();
    }

    @Override
    public void loadSemestersAll() {
        FindAllSemestersCommand findAllSemestersCommand = new FindAllSemestersCommand();
        commandInvoker.executeCommand(findAllSemestersCommand);
        semestersAll = findAllSemestersCommand.getSemestersAll();
    }

    @Override
    public void loadMaerchensAll() {
        FindAllMaerchensCommand findAllMaerchensCommand = new FindAllMaerchensCommand();
        commandInvoker.executeCommand(findAllMaerchensCommand);
        maerchensAll = findAllMaerchensCommand.getMaerchensAll();
    }

    @Override
    public List<SchuelerCode> getSchuelerCodesAll() {
        return schuelerCodesAll;
    }

    @Override
    public List<ElternmithilfeCode> getElternmithilfeCodesAll() {
        return elternmithilfeCodesAll;
    }

    @Override
    public List<Lehrkraft> getLehrkraefteAll() {
        return lehrkraefteAll;
    }

    @Override
    public List<Lehrkraft> getAktiveLehrkraefteAll() {
        List<Lehrkraft> lehrkraefteAktiveAll = new ArrayList<>();
        for (Lehrkraft lehrkraft : lehrkraefteAll) {
            if (lehrkraft.isAktiv()) {
                lehrkraefteAktiveAll.add(lehrkraft);
            }
        }
        return lehrkraefteAktiveAll;
    }

    @Override
    public List<Kursort> getKursorteAll() {
        return kursorteAll;
    }

    @Override
    public List<Kurstyp> getKurstypenAll() {
        return kurstypenAll;
    }

    @Override
    public List<Semester> getSemestersAll() {
        return semestersAll;
    }

    @Override
    public List<Maerchen> getMaerchensAll() {
        return maerchensAll;
    }
}
