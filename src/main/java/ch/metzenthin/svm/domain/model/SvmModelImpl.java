package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.*;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class SvmModelImpl implements SvmModel {

    private List<Code> codesAll;
    private List<Lehrkraft> lehrkraefteAll;
    private List<Kursort> kursorteAll;
    private List<Kurstyp> kurstypenAll;
    private List<Semester> semestersAll;
    private CommandInvoker commandInvoker;

    public SvmModelImpl(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
        loadCodesAll();
        loadLehrkraefteAll();
        loadKursorteAll();
        loadKurstypenAll();
        loadSemestersAll();
    }

    @Override
    public void loadCodesAll() {
        FindAllCodesCommand findAllCodesCommand = new FindAllCodesCommand();
        commandInvoker.executeCommand(findAllCodesCommand);
        codesAll = findAllCodesCommand.getCodesAll();
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
        semestersAll = findAllSemestersCommand.getSemesterAll();
    }

    @Override
    public List<Code> getCodesAll() {
        return codesAll;
    }

    @Override
    public List<Lehrkraft> getLehrkraefteAll() {
        return lehrkraefteAll;
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
}
