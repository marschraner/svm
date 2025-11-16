package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.service.result.DeleteCodeResult;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;

/**
 * @author Martin Schraner
 */
public interface CodesModel {

  DeleteCodeResult eintragLoeschenSchuelerCodesVerwalten(
      CodesTableModel codesTableModel, int indexCodeToBeRemoved);

  DeleteCodeResult eintragLoeschenMitarbeiterCodesVerwalten(
      CodesTableModel codesTableModel, int indexCodeToBeRemoved);

  DeleteCodeResult eintragLoeschenElternmithilfeCodesVerwalten(
      CodesTableModel codesTableModel, int indexCodeToBeRemoved);

  DeleteCodeResult eintragLoeschenSemesterrechnungCodesVerwalten(
      CodesTableModel codesTableModel, int indexCodeToBeRemoved);

  void eintragLoeschenSchuelerCodesSchueler(
      CodesTableModel codesTableModel,
      SchuelerCode schuelerCodeToBeRemoved,
      SchuelerDatenblattModel schuelerDatenblattModel);

  void eintragLoeschenMitarbeiterCodesMitarbeiter(
      CodesTableModel codesTableModel,
      MitarbeiterCode mitarbeiterCodeToBeRemoved,
      MitarbeiterErfassenModel mitarbeiterErfassenModel);

  CreateOrUpdateSchuelerCodeModel createCreateOrUpdateSchuelerCodeModel(
      SvmContext svmContext, CodesTableModel codesTableModel);

  CreateOrUpdateSchuelerCodeModel createCreateOrUpdateSchuelerCodeModel(
      SvmContext svmContext, CodesTableModel codesTableModel, int indexSchuelerCodeToBeModified);

  CreateOrUpdateMitarbeiterCodeModel createCreateOrUpdateMitarbeiterCodeModel(
      SvmContext svmContext, CodesTableModel codesTableModel);

  CreateOrUpdateMitarbeiterCodeModel createCreateOrUpdateMitarbeiterCodeModel(
      SvmContext svmContext, CodesTableModel codesTableModel, int indexMitarbeiterCodeToBeModified);

  CreateOrUpdateElternmithilfeCodeModel createCreateOrUpdateElternmithilfeCodeModel(
      SvmContext svmContext, CodesTableModel codesTableModel);

  CreateOrUpdateElternmithilfeCodeModel createCreateOrUpdateElternmithilfeCodeModel(
      SvmContext svmContext,
      CodesTableModel codesTableModel,
      int indexElternmithilfeCodeToBeModified);

  CreateOrUpdateSemesterrechnungCodeModel createCreateOrUpdateSemesterrechnungCodeModel(
      SvmContext svmContext, CodesTableModel codesTableModel);

  CreateOrUpdateSemesterrechnungCodeModel createCreateOrUpdateSemesterrechnungCodeModel(
      SvmContext svmContext,
      CodesTableModel codesTableModel,
      int indexSemesterrechnungCodeToBeModified);

  SchuelerCode[] getSelectableSchuelerCodes(
      SvmModel svmModel, SchuelerDatenblattModel schuelerDatenblattModel);

  MitarbeiterCode[] getSelectableMitarbeiterCodes(
      SvmModel svmModel, MitarbeiterErfassenModel mitarbeiterErfassenModel);
}
