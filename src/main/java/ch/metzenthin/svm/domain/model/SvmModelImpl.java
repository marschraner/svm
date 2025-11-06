package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.*;
import ch.metzenthin.svm.service.KursortService;
import ch.metzenthin.svm.service.KurstypService;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SvmModelImpl implements SvmModel {

  private final CommandInvoker commandInvoker = new CommandInvokerImpl();

  private final KursortService kursortService;
  private final KurstypService kurstypService;

  public SvmModelImpl(KursortService kursortService, KurstypService kurstypService) {
    this.kursortService = kursortService;
    this.kurstypService = kurstypService;
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
      if (schuelerCode.isSelektierbar()) {
        selektierbareSchuelerCodesAll.add(schuelerCode);
      }
    }
    return selektierbareSchuelerCodesAll;
  }

  @Override
  public List<MitarbeiterCode> getMitarbeiterCodesAll() {
    FindAllMitarbeiterCodesCommand findAllMitarbeiterCodesCommand =
        new FindAllMitarbeiterCodesCommand();
    commandInvoker.executeCommand(findAllMitarbeiterCodesCommand);
    return findAllMitarbeiterCodesCommand.getMitarbeiterCodesAll();
  }

  @Override
  public List<MitarbeiterCode> getSelektierbareMitarbeiterCodesAll() {
    List<MitarbeiterCode> selektierbareMitarbeiterCodesAll = new ArrayList<>();
    for (MitarbeiterCode mitarbeiterCode : getMitarbeiterCodesAll()) {
      if (mitarbeiterCode.isSelektierbar()) {
        selektierbareMitarbeiterCodesAll.add(mitarbeiterCode);
      }
    }
    return selektierbareMitarbeiterCodesAll;
  }

  @Override
  public List<ElternmithilfeCode> getElternmithilfeCodesAll() {
    FindAllElternmithilfeCodesCommand findAllElternmithilfeCodesCommand =
        new FindAllElternmithilfeCodesCommand();
    commandInvoker.executeCommand(findAllElternmithilfeCodesCommand);
    return findAllElternmithilfeCodesCommand.getElternmithilfeCodesAll();
  }

  @Override
  public List<ElternmithilfeCode> getSelektierbareElternmithilfeCodesAll() {
    List<ElternmithilfeCode> selektierbareElternmithilfeCodesAll = new ArrayList<>();
    for (ElternmithilfeCode elternmithilfeCode : getElternmithilfeCodesAll()) {
      if (elternmithilfeCode.isSelektierbar()) {
        selektierbareElternmithilfeCodesAll.add(elternmithilfeCode);
      }
    }
    return selektierbareElternmithilfeCodesAll;
  }

  @Override
  public List<SemesterrechnungCode> getSemesterrechnungCodesAll() {
    FindAllSemesterrechnungCodesCommand findAllSemesterrechnungCodesCommand =
        new FindAllSemesterrechnungCodesCommand();
    commandInvoker.executeCommand(findAllSemesterrechnungCodesCommand);
    return findAllSemesterrechnungCodesCommand.getSemesterrechnungCodesAll();
  }

  @Override
  public List<SemesterrechnungCode> getSelektierbareSemesterrechnungCodesAll() {
    List<SemesterrechnungCode> selektierbareSemesterrechnungCodesAll = new ArrayList<>();
    for (SemesterrechnungCode semesterrechnungCode : getSemesterrechnungCodesAll()) {
      if (semesterrechnungCode.isSelektierbar()) {
        selektierbareSemesterrechnungCodesAll.add(semesterrechnungCode);
      }
    }
    return selektierbareSemesterrechnungCodesAll;
  }

  @Override
  public List<Mitarbeiter> getMitarbeitersAll() {
    FindAllMitarbeitersCommand findAllMitarbeitersCommand = new FindAllMitarbeitersCommand();
    commandInvoker.executeCommand(findAllMitarbeitersCommand);
    return findAllMitarbeitersCommand.getMitarbeitersAll();
  }

  @Override
  public List<Mitarbeiter> getAktiveLehrkraefteAll() {
    List<Mitarbeiter> aktiveLehrkraefteAll = new ArrayList<>();
    for (Mitarbeiter mitarbeiter : getMitarbeitersAll()) {
      if (mitarbeiter.isAktiv() && mitarbeiter.isLehrkraft()) {
        aktiveLehrkraefteAll.add(mitarbeiter);
      }
    }
    return aktiveLehrkraefteAll;
  }

  @Override
  public List<Kursort> getKursorteAll() {
    return kursortService.findAllKursorte();
  }

  @Override
  public List<Kursort> getSelektierbareKursorteAll() {
    List<Kursort> selektierbareKursorteAll = new ArrayList<>();
    for (Kursort kursort : getKursorteAll()) {
      if (kursort.isSelektierbar()) {
        selektierbareKursorteAll.add(kursort);
      }
    }
    return selektierbareKursorteAll;
  }

  @Override
  public List<Kurstyp> getKurstypenAll() {
    return kurstypService.findAllKurstypen();
  }

  @Override
  public List<Kurstyp> getSelektierbareKurstypenAll() {
    List<Kurstyp> selektierbareKurstypenAll = new ArrayList<>();
    for (Kurstyp kurstyp : getKurstypenAll()) {
      if (kurstyp.isSelektierbar()) {
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
    FindAllLektionsgebuehrenCommand findAllLektionsgebuehrenCommand =
        new FindAllLektionsgebuehrenCommand();
    commandInvoker.executeCommand(findAllLektionsgebuehrenCommand);
    return findAllLektionsgebuehrenCommand.getLektionsgebuehrenAllList();
  }
}
