package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.Listentyp;
import ch.metzenthin.svm.domain.model.searchfields.MitarbeiterSearchFields;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.service.MitarbeiterService;
import ch.metzenthin.svm.service.result.DeleteMitarbeiterResult;
import ch.metzenthin.svm.service.result.ExportListResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import jakarta.persistence.OptimisticLockException;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class MitarbeiterListModel
    extends AbstractListModel<
        MitarbeiterTableData,
        MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert,
        CreateOrUpdateMitarbeiterModel,
        DeleteMitarbeiterResult> {

  private final MitarbeiterService mitarbeiterService;
  private final MitarbeiterSearchFields mitarbeiterSearchFields;

  public MitarbeiterListModel(
      MitarbeiterService mitarbeiterService, MitarbeiterSearchFields mitarbeiterSearchFields) {
    super(createTableModel(mitarbeiterService, mitarbeiterSearchFields));
    this.mitarbeiterService = mitarbeiterService;
    this.mitarbeiterSearchFields = mitarbeiterSearchFields;
  }

  private static TableModel<
          MitarbeiterTableData, MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert>
      createTableModel(
          MitarbeiterService mitarbeiterService, MitarbeiterSearchFields mitarbeiterSearchFields) {

    List<MitarbeiterAndMitarbeiterCodes> mitarbeiterAndMitarbeiterCodesFound =
        mitarbeiterService.findMitarbeiterAndMitarbeiterCodesBySearchFields(
            mitarbeiterSearchFields);
    List<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert>
        mitarbeiterAndMitarbeiterCodesAsStringAndSelektiertList =
            mitarbeiterAndMitarbeiterCodesFound.stream()
                .map(
                    mitarbeiterAndMitarbeiterCodes -> {
                      List<MitarbeiterCode> mitarbeiterCodes =
                          mitarbeiterAndMitarbeiterCodes.mitarbeiterCodes();
                      Collections.sort(mitarbeiterCodes);
                      String mitarbeiterCodesAsString =
                          mitarbeiterCodes.stream()
                              .map(Code::getKuerzel)
                              .collect(Collectors.joining(", "));
                      return new MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert(
                          mitarbeiterAndMitarbeiterCodes.mitarbeiter(),
                          mitarbeiterCodesAsString,
                          true);
                    })
                .toList();

    MitarbeiterTableData mitarbeiterTableData =
        new MitarbeiterTableData(mitarbeiterAndMitarbeiterCodesAsStringAndSelektiertList);
    return new TableModel<>(mitarbeiterTableData, true);
  }

  @Override
  public CreateOrUpdateMitarbeiterModel createCreateOrUpdateModel(SvmContext svmContext) {
    return svmContext.getModelFactory().createCreateOrUpdateMitarbeiterModel(Optional.empty());
  }

  @Override
  public CreateOrUpdateMitarbeiterModel createCreateOrUpdateModel(
      SvmContext svmContext, int indexMitarbeiterToBeUpdated) {
    Mitarbeiter mitarbeiterToBeUpdated = getSelectedRow(indexMitarbeiterToBeUpdated).mitarbeiter();
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateMitarbeiterModel(Optional.of(mitarbeiterToBeUpdated));
  }

  @Override
  public DeleteMitarbeiterResult eintragLoeschen(int indexMitarbeiterToBeDeleted) {
    Mitarbeiter mitarbeiterToBeDeleted = getSelectedRow(indexMitarbeiterToBeDeleted).mitarbeiter();
    DeleteMitarbeiterResult deleteMitarbeiterResult;
    try {
      deleteMitarbeiterResult = mitarbeiterService.deleteMitarbeiter(mitarbeiterToBeDeleted);
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      deleteMitarbeiterResult =
          DeleteMitarbeiterResult.MITARBEITER_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return deleteMitarbeiterResult;
  }

  @Override
  public void reloadData() {
    // TODO
    //    List<Mitarbeiter> mitarbeiterFound =
    //        mitarbeiterService.findMitarbeiterAndMitarbeiterCodesBySearchFields(
    //            mitarbeiterSearchFields);
    //    tableModel.setData(mitarbeiterFound);
  }

  public ExportListResult exportList(Listentyp listentyp, String listenTitel, File outputFile) {
    return null;
  }

  @Override
  public String getListItemName() {
    return "Mitarbeiterliste";
  }
}
