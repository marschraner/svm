package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.*;
import ch.metzenthin.svm.service.ElternmithilfeCodeService;
import ch.metzenthin.svm.service.MitarbeiterCodeService;
import ch.metzenthin.svm.service.SchuelerCodeService;
import ch.metzenthin.svm.service.SemesterrechnungCodeService;
import ch.metzenthin.svm.service.result.DeleteCodeResult;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;
import jakarta.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class CodesModelImpl extends AbstractModel implements CodesModel {

  private final SchuelerCodeService schuelerCodeService;
  private final MitarbeiterCodeService mitarbeiterCodeService;
  private final ElternmithilfeCodeService elternmithilfeCodeService;
  private final SemesterrechnungCodeService semesterrechnungCodeService;

  public CodesModelImpl(
      SchuelerCodeService schuelerCodeService,
      MitarbeiterCodeService mitarbeiterCodeService,
      ElternmithilfeCodeService elternmithilfeCodeService,
      SemesterrechnungCodeService semesterrechnungCodeService) {
    this.schuelerCodeService = schuelerCodeService;
    this.mitarbeiterCodeService = mitarbeiterCodeService;
    this.elternmithilfeCodeService = elternmithilfeCodeService;
    this.semesterrechnungCodeService = semesterrechnungCodeService;
  }

  @SuppressWarnings("DuplicatedCode")
  @Override
  public DeleteCodeResult eintragLoeschenSchuelerCodesVerwalten(
      CodesTableModel codesTableModel, int indexCodeToBeRemoved) {
    SchuelerCode schuelerCodeToBeDeleted =
        getSelectedSchuelerCode(codesTableModel, indexCodeToBeRemoved);
    DeleteCodeResult deleteCodeResult;
    try {
      schuelerCodeService.deleteCode(schuelerCodeToBeDeleted);
      deleteCodeResult = DeleteCodeResult.LOESCHEN_ERFOLGREICH;
    } catch (EntityStillReferencedException e) {
      deleteCodeResult = DeleteCodeResult.CODE_REFERENZIERT;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      deleteCodeResult = DeleteCodeResult.CODE_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return deleteCodeResult;
  }

  @SuppressWarnings("DuplicatedCode")
  @Override
  public DeleteCodeResult eintragLoeschenMitarbeiterCodesVerwalten(
      CodesTableModel codesTableModel, int indexCodeToBeRemoved) {
    MitarbeiterCode mitarbeiterCodeToBeDeleted =
        getSelectedMitarbeiterCode(codesTableModel, indexCodeToBeRemoved);
    DeleteCodeResult deleteCodeResult;
    try {
      mitarbeiterCodeService.deleteCode(mitarbeiterCodeToBeDeleted);
      deleteCodeResult = DeleteCodeResult.LOESCHEN_ERFOLGREICH;
    } catch (EntityStillReferencedException e) {
      deleteCodeResult = DeleteCodeResult.CODE_REFERENZIERT;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      deleteCodeResult = DeleteCodeResult.CODE_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return deleteCodeResult;
  }

  @SuppressWarnings("DuplicatedCode")
  @Override
  public DeleteCodeResult eintragLoeschenElternmithilfeCodesVerwalten(
      CodesTableModel codesTableModel, int indexCodeToBeRemoved) {
    ElternmithilfeCode elternmithilfeCodeToBeDeleted =
        getSelectedElternmithilfeCode(codesTableModel, indexCodeToBeRemoved);
    DeleteCodeResult deleteCodeResult;
    try {
      elternmithilfeCodeService.deleteCode(elternmithilfeCodeToBeDeleted);
      deleteCodeResult = DeleteCodeResult.LOESCHEN_ERFOLGREICH;
    } catch (EntityStillReferencedException e) {
      deleteCodeResult = DeleteCodeResult.CODE_REFERENZIERT;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      deleteCodeResult = DeleteCodeResult.CODE_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return deleteCodeResult;
  }

  @SuppressWarnings("DuplicatedCode")
  @Override
  public DeleteCodeResult eintragLoeschenSemesterrechnungCodesVerwalten(
      CodesTableModel codesTableModel, int indexCodeToBeRemoved) {
    SemesterrechnungCode semesterrechnungCodeToBeDeleted =
        getSelectedSemesterrechnungCode(codesTableModel, indexCodeToBeRemoved);
    DeleteCodeResult deleteCodeResult;
    try {
      semesterrechnungCodeService.deleteCode(semesterrechnungCodeToBeDeleted);
      deleteCodeResult = DeleteCodeResult.LOESCHEN_ERFOLGREICH;
    } catch (EntityStillReferencedException e) {
      deleteCodeResult = DeleteCodeResult.CODE_REFERENZIERT;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      deleteCodeResult = DeleteCodeResult.CODE_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return deleteCodeResult;
  }

  @Override
  public void eintragLoeschenSchuelerCodesSchueler(
      CodesTableModel codesTableModel,
      SchuelerCode schuelerCodeToBeRemoved,
      SchuelerDatenblattModel schuelerDatenblattModel) {
    CommandInvoker commandInvoker = getCommandInvoker();
    RemoveSchuelerCodeFromSchuelerCommand removeSchuelerCodeFromSchuelerCommand =
        new RemoveSchuelerCodeFromSchuelerCommand(
            schuelerCodeToBeRemoved, schuelerDatenblattModel.getSchueler());
    commandInvoker.executeCommandAsTransaction(removeSchuelerCodeFromSchuelerCommand);
    Schueler schuelerUpdated = removeSchuelerCodeFromSchuelerCommand.getSchuelerUpdated();
    // TableData mit von der Datenbank upgedateten SchülerCodes updaten
    codesTableModel.getCodesTableData().setCodes(schuelerUpdated.getSortedSchuelerCodes());
  }

  @Override
  public void eintragLoeschenMitarbeiterCodesMitarbeiter(
      CodesTableModel codesTableModel,
      MitarbeiterCode mitarbeiterCodeToBeRemoved,
      MitarbeiterErfassenModel mitarbeiterErfassenModel) {
    mitarbeiterErfassenModel.getMitarbeiterCodes().remove(mitarbeiterCodeToBeRemoved);
    // TableData updaten
    codesTableModel
        .getCodesTableData()
        .setCodes(mitarbeiterErfassenModel.getMitarbeiterCodesAsList());
  }

  @Override
  public CreateOrUpdateSchuelerCodeModel createCreateOrUpdateSchuelerCodeModel(
      SvmContext svmContext, CodesTableModel codesTableModel) {
    return svmContext.getModelFactory().createCreateOrUpdateSchuelerCodeModel(Optional.empty());
  }

  @Override
  public CreateOrUpdateSchuelerCodeModel createCreateOrUpdateSchuelerCodeModel(
      SvmContext svmContext, CodesTableModel codesTableModel, int indexSchuelerCodeToBeModified) {
    SchuelerCode schuelerCodeToBeModified =
        getSelectedSchuelerCode(codesTableModel, indexSchuelerCodeToBeModified);
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateSchuelerCodeModel(Optional.of(schuelerCodeToBeModified));
  }

  private SchuelerCode getSelectedSchuelerCode(
      CodesTableModel codesTableModel, int indexSchuelerCodeToBeModified) {
    return (SchuelerCode) codesTableModel.getCodeAt(indexSchuelerCodeToBeModified);
  }

  @Override
  public CreateOrUpdateMitarbeiterCodeModel createCreateOrUpdateMitarbeiterCodeModel(
      SvmContext svmContext, CodesTableModel codesTableModel) {
    return svmContext.getModelFactory().createCreateOrUpdateMitarbeiterCodeModel(Optional.empty());
  }

  @Override
  public CreateOrUpdateMitarbeiterCodeModel createCreateOrUpdateMitarbeiterCodeModel(
      SvmContext svmContext,
      CodesTableModel codesTableModel,
      int indexMitarbeiterCodeToBeModified) {
    MitarbeiterCode mitarbeiterCodeToBeModified =
        getSelectedMitarbeiterCode(codesTableModel, indexMitarbeiterCodeToBeModified);
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateMitarbeiterCodeModel(Optional.of(mitarbeiterCodeToBeModified));
  }

  private MitarbeiterCode getSelectedMitarbeiterCode(
      CodesTableModel codesTableModel, int indexMitarbeiterCodeToBeModified) {
    return (MitarbeiterCode) codesTableModel.getCodeAt(indexMitarbeiterCodeToBeModified);
  }

  @Override
  public CreateOrUpdateElternmithilfeCodeModel createCreateOrUpdateElternmithilfeCodeModel(
      SvmContext svmContext, CodesTableModel codesTableModel) {
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateElternmithilfeCodeModel(Optional.empty());
  }

  @Override
  public CreateOrUpdateElternmithilfeCodeModel createCreateOrUpdateElternmithilfeCodeModel(
      SvmContext svmContext,
      CodesTableModel codesTableModel,
      int indexElternmithilfeCodeToBeModified) {
    ElternmithilfeCode elternmithilfeCodeToBeModified =
        getSelectedElternmithilfeCode(codesTableModel, indexElternmithilfeCodeToBeModified);
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateElternmithilfeCodeModel(Optional.of(elternmithilfeCodeToBeModified));
  }

  private ElternmithilfeCode getSelectedElternmithilfeCode(
      CodesTableModel codesTableModel, int indexElternmithilfeCodeToBeModified) {
    return (ElternmithilfeCode) codesTableModel.getCodeAt(indexElternmithilfeCodeToBeModified);
  }

  @Override
  public CreateOrUpdateSemesterrechnungCodeModel createCreateOrUpdateSemesterrechnungCodeModel(
      SvmContext svmContext, CodesTableModel codesTableModel) {
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateSemesterrechnungCodeModel(Optional.empty());
  }

  @Override
  public CreateOrUpdateSemesterrechnungCodeModel createCreateOrUpdateSemesterrechnungCodeModel(
      SvmContext svmContext,
      CodesTableModel codesTableModel,
      int indexSemesterrechnungCodeToBeModified) {
    SemesterrechnungCode semesterrechnungCodeToBeModified =
        getSelectedSemesterrechnungCode(codesTableModel, indexSemesterrechnungCodeToBeModified);
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateSemesterrechnungCodeModel(
            Optional.of(semesterrechnungCodeToBeModified));
  }

  private SemesterrechnungCode getSelectedSemesterrechnungCode(
      CodesTableModel codesTableModel, int indexSemesterrechnungCodeToBeModified) {
    return (SemesterrechnungCode) codesTableModel.getCodeAt(indexSemesterrechnungCodeToBeModified);
  }

  @Override
  public SchuelerCode[] getSelectableSchuelerCodes(
      SvmModel svmModel, SchuelerDatenblattModel schuelerDatenblattModel) {
    List<SchuelerCode> selectableSchuelerCodes =
        new ArrayList<>(svmModel.getSelektierbareSchuelerCodesAll());
    List<SchuelerCode> codesToBeRemoved = new ArrayList<>();
    for (SchuelerCode schuelerCode : selectableSchuelerCodes) {
      for (SchuelerCode schuelerCodeSchueler :
          schuelerDatenblattModel.getSchueler().getSchuelerCodes()) {
        // Nur noch nicht zugewiesene Codes sollen selektierbar sein
        if (schuelerCodeSchueler.isIdenticalWith(schuelerCode)) {
          codesToBeRemoved.add(schuelerCode);
        }
      }
    }
    selectableSchuelerCodes.removeAll(codesToBeRemoved);
    return selectableSchuelerCodes.toArray(new SchuelerCode[0]);
  }

  @Override
  public MitarbeiterCode[] getSelectableMitarbeiterCodes(
      SvmModel svmModel, MitarbeiterErfassenModel mitarbeiterErfassenModel) {
    List<MitarbeiterCode> selectableMitarbeiterCodes =
        new ArrayList<>(svmModel.getSelektierbareMitarbeiterCodesAll());
    List<MitarbeiterCode> codesToBeRemoved = new ArrayList<>();
    for (MitarbeiterCode mitarbeiterCode : selectableMitarbeiterCodes) {
      for (MitarbeiterCode mitarbeiterCodeMitarbeiter :
          mitarbeiterErfassenModel.getMitarbeiterCodes()) {
        // Nur noch nicht zugewiesene Codes sollen selektierbar sein
        if (mitarbeiterCodeMitarbeiter.isIdenticalWith(mitarbeiterCode)) {
          codesToBeRemoved.add(mitarbeiterCode);
        }
      }
    }
    selectableMitarbeiterCodes.removeAll(codesToBeRemoved);
    return selectableMitarbeiterCodes.toArray(new MitarbeiterCode[0]);
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
