package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public interface ModelFactory {

  SvmModel createSvmModel();

  SchuelerModel createSchuelerModel();

  AngehoerigerModel createAngehoerigerModel();

  SchuelerErfassenModel createSchuelerErfassenModel();

  SchuelerSuchenModel createSchuelerSuchenModel();

  MonatsstatistikSchuelerModel createMonatsstatistikSchuelerModel();

  MonatsstatistikKurseModel createMonatsstatistikKurseModel();

  DispensationenModel createDispensationenModel();

  DispensationErfassenModel createDispensationErfassenModel();

  CodesModel createCodesModel();

  CreateOrUpdateSchuelerCodeModel createCreateOrUpdateSchuelerCodeModel(
      Optional<SchuelerCode> schuelerCodeToBeModifiedOptional);

  CreateOrUpdateMitarbeiterCodeModel createCreateOrUpdateMitarbeiterCodeModel(
      Optional<MitarbeiterCode> mitarbeiterCodeToBeModifiedOptional);

  CreateOrUpdateElternmithilfeCodeModel createCreateOrUpdateElternmithilfeCodeModel(
      Optional<ElternmithilfeCode> elternmithilfeCodeToBeModifiedOptional);

  CreateOrUpdateSemesterrechnungCodeModel createCreateOrUpdateSemesterrechnungCodeModel(
      Optional<SemesterrechnungCode> semesterrechnungCodeToBeModifiedOptional);

  CodeSpecificHinzufuegenModel createCodeSchuelerHinzufuegenModel();

  MitarbeitersModel createLehrkraefteModel();

  MitarbeiterErfassenModel createMitarbeiterErfassenModel();

  KursorteModel createKursorteModel();

  CreateOrUpdateKursortModel createCreateOrUpdateKursortModel(
      Optional<Kursort> kursortToBeModifiedOptional);

  KurstypenModel createKurstypenModel();

  CreateOrUpdateKurstypModel createCreateOrUpdateKurstypModel(
      Optional<Kurstyp> kurstypToBeModifiedOptional);

  SemestersModel createSemestersModel();

  SemesterErfassenModel createSemesterErfassenModel();

  KurseSemesterwahlModel createKurseSemesterwahlModel();

  KurseModel createKurseModel();

  KursErfassenModel createKursErfassenModel();

  KursanmeldungenModel createKursanmeldungenModel();

  KursanmeldungErfassenModel createKursanmeldungErfassenModel();

  ListenExportModel createListenExportModel();

  MaerchensModel createMaerchensModel();

  MaerchenErfassenModel createMaerchenErfassenModel();

  MaercheneinteilungenModel createMaercheneinteilungenModel();

  MaercheneinteilungErfassenModel createMaercheneinteilungErfassenModel();

  EmailModel createEmailModel();

  LektionsgebuehrenModel createLektionsgebuehrenModel();

  LektionsgebuehrenErfassenModel createLektionsgebuehrenErfassenModel();

  SemesterrechnungenSuchenModel createSemesterrechnungenSuchenModel();

  SemesterrechnungenModel createSemesterrechnungenModel();

  SemesterrechnungBearbeitenModel createSemesterrechnungBearbeitenModel();

  RechnungsdatumErfassenModel createRechnungsdatumErfassenModel();

  MitarbeiterSuchenModel createMitarbeitersSuchenModel();

  EmailSchuelerListeModel createEmailSchuelerListeModel();

  EmailSemesterrechnungenModel createEmailSemesterrechnungenModel();
}
