package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.*;
import ch.metzenthin.svm.service.ElternmithilfeCodeService;
import ch.metzenthin.svm.service.MitarbeiterCodeService;
import ch.metzenthin.svm.service.SchuelerCodeService;
import ch.metzenthin.svm.service.SemesterService;
import ch.metzenthin.svm.service.SemesterrechnungCodeService;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SvmModelImpl implements SvmModel {

  private final CommandInvoker commandInvoker = new CommandInvokerImpl();

  private final SchuelerCodeService schuelerCodeService;
  private final MitarbeiterCodeService mitarbeiterCodeService;
  private final ElternmithilfeCodeService elternmithilfeCodeService;
  private final SemesterrechnungCodeService semesterrechnungCodeService;
  private final SemesterService semesterService;

  @SuppressWarnings("java:S107")
  public SvmModelImpl(
      SchuelerCodeService schuelerCodeService,
      MitarbeiterCodeService mitarbeiterCodeService,
      ElternmithilfeCodeService elternmithilfeCodeService,
      SemesterrechnungCodeService semesterrechnungCodeService,
      SemesterService semesterService) {
    this.schuelerCodeService = schuelerCodeService;
    this.mitarbeiterCodeService = mitarbeiterCodeService;
    this.elternmithilfeCodeService = elternmithilfeCodeService;
    this.semesterrechnungCodeService = semesterrechnungCodeService;
    this.semesterService = semesterService;
  }

  @Override
  public List<SchuelerCode> getSchuelerCodesAll() {
    return schuelerCodeService.findAllCodes();
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
    return mitarbeiterCodeService.findAllCodes();
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
    return elternmithilfeCodeService.findAllCodes();
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
    return semesterrechnungCodeService.findAllCodes();
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
  public List<Semester> getSemestersAll() {
    return semesterService.findAllSemesters();
  }

  @Override
  public List<Maerchen> getMaerchensAll() {
    FindAllMaerchensCommand findAllMaerchensCommand = new FindAllMaerchensCommand();
    commandInvoker.executeCommand(findAllMaerchensCommand);
    return findAllMaerchensCommand.getMaerchensAll();
  }
}
