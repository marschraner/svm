package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
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

  CodeErfassenModel createCodeErfassenModel();

  CodeSpecificHinzufuegenModel createCodeSchuelerHinzufuegenModel();

  MitarbeitersModel createLehrkraefteModel();

  MitarbeiterErfassenModel createMitarbeiterErfassenModel();

  KursorteModel createKursorteModel();

  CreateOrUpdateKursortModel createCreateOrUpdateKursortModel(
      Optional<Kursort> kursortToBeModifiedOptional);

  KurstypenModel createKurstypenModel();

  KurstypErfassenModel createKurstypErfassenModel(Optional<Kurstyp> kurstypToBeModifiedOptional);

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
