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

  SchuelerCodeErfassenModel createSchuelerCodeErfassenModel(
      SvmContext svmContext, CodesTableModel codesTableModel);

  SchuelerCodeErfassenModel createSchuelerCodeErfassenModel(
      SvmContext svmContext, CodesTableModel codesTableModel, int indexSchuelerCodeToBeModified);

  MitarbeiterCodeErfassenModel createMitarbeiterCodeErfassenModel(
      SvmContext svmContext, CodesTableModel codesTableModel);

  MitarbeiterCodeErfassenModel createMitarbeiterCodeErfassenModel(
      SvmContext svmContext, CodesTableModel codesTableModel, int indexMitarbeiterCodeToBeModified);

  ElternmithilfeCodeErfassenModel createElternmithilfeCodeErfassenModel(
      SvmContext svmContext, CodesTableModel codesTableModel);

  ElternmithilfeCodeErfassenModel createElternmithilfeCodeErfassenModel(
      SvmContext svmContext,
      CodesTableModel codesTableModel,
      int indexElternmithilfeCodeToBeModified);

  SemesterrechnungCodeErfassenModel createSemesterrechnungCodeErfassenModel(
      SvmContext svmContext, CodesTableModel codesTableModel);

  SemesterrechnungCodeErfassenModel createSemesterrechnungCodeErfassenModel(
      SvmContext svmContext,
      CodesTableModel codesTableModel,
      int indexSemesterrechnungCodeToBeModified);

  SchuelerCode[] getSelectableSchuelerCodes(
      SvmModel svmModel, SchuelerDatenblattModel schuelerDatenblattModel);

  MitarbeiterCode[] getSelectableMitarbeiterCodes(
      SvmModel svmModel, MitarbeiterErfassenModel mitarbeiterErfassenModel);
}
