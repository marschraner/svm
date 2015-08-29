package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Schraner
 */
public class SvmModelImpl implements SvmModel {

    private List<SchuelerCode> schuelerCodesAll;
    private List<ElternmithilfeCode> elternmithilfeCodesAll;
    private List<SemesterrechnungCode> semesterrechnungCodesAll;
    private List<Lehrkraft> lehrkraefteAll;
    private List<Kursort> kursorteAll;
    private List<Kurstyp> kurstypenAll;
    private List<Semester> semestersAll;
    private List<Maerchen> maerchensAll;
    private List<Lektionsgebuehren> lektionsgebuehrenAllList;
    private Map<Integer, BigDecimal[]> lektionsgebuehrenAllMap;
    private CommandInvoker commandInvoker;

    public SvmModelImpl(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
        loadAll();
    }

    @Override
    public void loadAll() {
        loadSchuelerCodesAll();
        loadElternmithilfeCodesAll();
        loadSemesterrechnungCodesAll();
        loadLehrkraefteAll();
        loadKursorteAll();
        loadKurstypenAll();
        loadSemestersAll();
        loadMaerchensAll();
        loadLektionsgebuehrenAll();
    }

    @Override
    public void loadSchuelerCodesAll() {
        FindAllSchuelerCodesCommand findAllSchuelerCodesCommand = new FindAllSchuelerCodesCommand();
        commandInvoker.executeCommand(findAllSchuelerCodesCommand);
        schuelerCodesAll = findAllSchuelerCodesCommand.getSchuelerCodesAll();
    }

    @Override
    public List<SchuelerCode> getSelektierbareSchuelerCodesAll() {
        List<SchuelerCode> selektierbareSchuelerCodesAll = new ArrayList<>();
        for (SchuelerCode schuelerCode : schuelerCodesAll) {
            if (schuelerCode.getSelektierbar()) {
                selektierbareSchuelerCodesAll.add(schuelerCode);
            }
        }
        return selektierbareSchuelerCodesAll;
    }

    @Override
    public void loadElternmithilfeCodesAll() {
        FindAllElternmithilfeCodesCommand findAllElternmithilfeCodesCommand = new FindAllElternmithilfeCodesCommand();
        commandInvoker.executeCommand(findAllElternmithilfeCodesCommand);
        elternmithilfeCodesAll = findAllElternmithilfeCodesCommand.getElternmithilfeCodesAll();
    }

    @Override
    public List<ElternmithilfeCode> getSelektierbareElternmithilfeCodesAll() {
        List<ElternmithilfeCode> selektierbareElternmithilfeCodesAll = new ArrayList<>();
        for (ElternmithilfeCode elternmithilfeCode : elternmithilfeCodesAll) {
            if (elternmithilfeCode.getSelektierbar()) {
                selektierbareElternmithilfeCodesAll.add(elternmithilfeCode);
            }
        }
        return selektierbareElternmithilfeCodesAll;
    }

    @Override
    public void loadSemesterrechnungCodesAll() {
        FindAllSemesterrechnungCodesCommand findAllSemesterrechnungCodesCommand = new FindAllSemesterrechnungCodesCommand();
        commandInvoker.executeCommand(findAllSemesterrechnungCodesCommand);
        semesterrechnungCodesAll = findAllSemesterrechnungCodesCommand.getSemesterrechnungCodesAll();
    }

    @Override
    public List<SemesterrechnungCode> getSelektierbareSemesterrechnungCodesAll() {
        List<SemesterrechnungCode> selektierbareSemesterrechnungCodesAll = new ArrayList<>();
        for (SemesterrechnungCode semesterrechnungCode : semesterrechnungCodesAll) {
            if (semesterrechnungCode.getSelektierbar()) {
                selektierbareSemesterrechnungCodesAll.add(semesterrechnungCode);
            }
        }
        return selektierbareSemesterrechnungCodesAll;
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
    public void loadLektionsgebuehrenAll() {
        FindAllLektionsgebuehrenCommand findAllLektionsgebuehrenCommand = new FindAllLektionsgebuehrenCommand();
        commandInvoker.executeCommand(findAllLektionsgebuehrenCommand);
        lektionsgebuehrenAllList = findAllLektionsgebuehrenCommand.getLektionsgebuehrenAllList();
        lektionsgebuehrenAllMap = findAllLektionsgebuehrenCommand.getLektionsgebuehrenAllMap();
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
    public List<SemesterrechnungCode> getSemesterrechnungCodesAll() {
        return semesterrechnungCodesAll;
    }

    @Override
    public List<Lehrkraft> getLehrkraefteAll() {
        return lehrkraefteAll;
    }

    @Override
    public List<Lehrkraft> getAktiveLehrkraefteAll() {
        List<Lehrkraft> aktiveLehrkraefteAll = new ArrayList<>();
        for (Lehrkraft lehrkraft : lehrkraefteAll) {
            if (lehrkraft.getAktiv()) {
                aktiveLehrkraefteAll.add(lehrkraft);
            }
        }
        return aktiveLehrkraefteAll;
    }

    @Override
    public List<Kursort> getKursorteAll() {
        return kursorteAll;
    }

    @Override
    public List<Kursort> getSelektierbareKursorteAll() {
        List<Kursort> selektierbareKursorteAll = new ArrayList<>();
        for (Kursort kursort : kursorteAll) {
            if (kursort.getSelektierbar()) {
                selektierbareKursorteAll.add(kursort);
            }
        }
        return selektierbareKursorteAll;
    }

    @Override
    public List<Kurstyp> getKurstypenAll() {
        return kurstypenAll;
    }

    @Override
    public List<Kurstyp> getSelektierbareKurstypenAll() {
        List<Kurstyp> selektierbareKurstypenAll = new ArrayList<>();
        for (Kurstyp kurstyp : kurstypenAll) {
            if (kurstyp.getSelektierbar()) {
                selektierbareKurstypenAll.add(kurstyp);
            }
        }
        return selektierbareKurstypenAll;
    }

    @Override
    public List<Semester> getSemestersAll() {
        return semestersAll;
    }

    @Override
    public List<Maerchen> getMaerchensAll() {
        return maerchensAll;
    }

    @Override
    public List<Lektionsgebuehren> getLektionsgebuehrenAllList() {
        return lektionsgebuehrenAllList;
    }

    @Override
    public Map<Integer, BigDecimal[]> getLektionsgebuehrenAllMap() {
        return lektionsgebuehrenAllMap;
    }
}
