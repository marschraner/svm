package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class SvmModelImpl implements SvmModel {

    private List<Code> codesAll;
    private List<Lehrkraft> lehrkraefteAll;
    private List<Kursort> kursorteAll;
    private List<Kurstyp> kurstypenAll;
    private CommandInvoker commandInvoker;

    public SvmModelImpl(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
        reloadCodesAll();
        reloadLehrkraefteAll();
    }

    @Override
    public void reloadCodesAll() {
        FindAllCodesCommand findAllCodesCommand = new FindAllCodesCommand();
        commandInvoker.executeCommand(findAllCodesCommand);
        codesAll = findAllCodesCommand.getCodesAll();
    }

    @Override
    public void reloadLehrkraefteAll() {
        FindAllLehrkraefteCommand findAllLehrkraefteCommand = new FindAllLehrkraefteCommand();
        commandInvoker.executeCommand(findAllLehrkraefteCommand);
        lehrkraefteAll = findAllLehrkraefteCommand.getLehrkraefteAll();
    }

    @Override
    public void reloadKursorteAll() {
        FindAllKursorteCommand findAllKursorteCommand = new FindAllKursorteCommand();
        commandInvoker.executeCommand(findAllKursorteCommand);
        kursorteAll = findAllKursorteCommand.getKursorteAll();
    }

    @Override
    public void reloadKurstypenAll() {
        FindAllKurstypenCommand findAllKurstypenCommand = new FindAllKurstypenCommand();
        commandInvoker.executeCommand(findAllKurstypenCommand);
        kurstypenAll = findAllKurstypenCommand.getKurstypenAll();
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
}
