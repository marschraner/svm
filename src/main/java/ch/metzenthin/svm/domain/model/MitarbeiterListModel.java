package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.Listentyp;
import ch.metzenthin.svm.domain.model.searchfields.MitarbeiterSearchFields;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.service.MitarbeiterService;
import ch.metzenthin.svm.service.result.DeleteMitarbeiterResult;
import ch.metzenthin.svm.service.result.ExportListResult;
import ch.metzenthin.svm.ui.componentmodel.MitarbeiterTableModel;
import jakarta.persistence.OptimisticLockException;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
        DeleteMitarbeiterResult,
        MitarbeiterTableModel> {

  private final MitarbeiterService mitarbeiterService;
  private final MitarbeiterSearchFields mitarbeiterSearchFields;

  public MitarbeiterListModel(
      MitarbeiterService mitarbeiterService, MitarbeiterSearchFields mitarbeiterSearchFields) {
    super(createTableModel(mitarbeiterService, mitarbeiterSearchFields));
    this.mitarbeiterService = mitarbeiterService;
    this.mitarbeiterSearchFields = mitarbeiterSearchFields;
  }

  private static MitarbeiterTableModel createTableModel(
      MitarbeiterService mitarbeiterService, MitarbeiterSearchFields mitarbeiterSearchFields) {

    List<MitarbeiterAndMitarbeiterCodes> mitarbeiterAndMitarbeiterCodesList =
        mitarbeiterService.findMitarbeiterAndMitarbeiterCodes(
            mitarbeiterSearchFields.getNachnameAsOptional(),
            mitarbeiterSearchFields.getVornameAsOptional(),
            mitarbeiterSearchFields.isLehrkraftAsOptional(),
            mitarbeiterSearchFields.isAktivAsOptional(),
            mitarbeiterSearchFields.getMitarbeiterCodeAsOptional());

    List<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert>
        mitarbeiterAndMitarbeiterCodesAsStringAndSelektiertList =
            mitarbeiterAndMitarbeiterCodesList.stream()
                .map(
                    mitarbeiterAndMitarbeiterCodes -> {
                      List<MitarbeiterCode> mitarbeiterCodes =
                          mitarbeiterAndMitarbeiterCodes.mitarbeiterCodes();
                      String mitarbeiterCodesAsString =
                          mitarbeiterCodes.stream()
                              .map(MitarbeiterCode::getKuerzel)
                              .collect(Collectors.joining(", "));
                      return new MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert(
                          mitarbeiterAndMitarbeiterCodes.mitarbeiter(),
                          mitarbeiterCodesAsString,
                          new Selection(true));
                    })
                .toList();

    MitarbeiterTableData mitarbeiterTableData =
        new MitarbeiterTableData(mitarbeiterAndMitarbeiterCodesAsStringAndSelektiertList);
    return new MitarbeiterTableModel(mitarbeiterTableData);
  }

  private static String getMitarbeiterCodesAsString(
      MitarbeiterAndMitarbeiterCodes mitarbeiterAndMitarbeiterCodes) {
    List<MitarbeiterCode> mitarbeiterCodes = mitarbeiterAndMitarbeiterCodes.mitarbeiterCodes();
    Collections.sort(mitarbeiterCodes);
    return mitarbeiterCodes.stream()
        .map(MitarbeiterCode::getKuerzel)
        .collect(Collectors.joining(", "));
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
    Map<Integer, Selection> selektiertMap = new HashMap<>();
    Iterator<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert> rowIterator =
        tableModel.getRowIterator();
    while (rowIterator.hasNext()) {
      MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert next = rowIterator.next();
      selektiertMap.put(next.mitarbeiter().getPersonId(), next.selektiert());
    }
    List<MitarbeiterAndMitarbeiterCodes> mitarbeiterAndMitarbeiterCodesList =
        mitarbeiterService.findMitarbeiterAndMitarbeiterCodes(
            mitarbeiterSearchFields.getNachnameAsOptional(),
            mitarbeiterSearchFields.getVornameAsOptional(),
            mitarbeiterSearchFields.isLehrkraftAsOptional(),
            mitarbeiterSearchFields.isAktivAsOptional(),
            mitarbeiterSearchFields.getMitarbeiterCodeAsOptional());
    List<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert>
        mitarbeiterAndMitarbeiterCodesAsStringAndSelektiertList =
            mitarbeiterAndMitarbeiterCodesList.stream()
                .map(
                    mitarbeiterAndMitarbeiterCodes -> {
                      String mitarbeiterCodesAsString =
                          getMitarbeiterCodesAsString(mitarbeiterAndMitarbeiterCodes);
                      Selection selection =
                          selektiertMap.get(
                              mitarbeiterAndMitarbeiterCodes.mitarbeiter().getPersonId());
                      if (selection == null) {
                        selection = new Selection(true);
                      }
                      return new MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert(
                          mitarbeiterAndMitarbeiterCodes.mitarbeiter(),
                          mitarbeiterCodesAsString,
                          selection);
                    })
                .toList();
    tableModel.setData(mitarbeiterAndMitarbeiterCodesAsStringAndSelektiertList);
  }

  public ExportListResult exportList(Listentyp listentyp, String listenTitel, File outputFile) {
    return null; // TODO
  }

  @Override
  public String getListItemName() {
    return "Mitarbeiterliste";
  }
}
