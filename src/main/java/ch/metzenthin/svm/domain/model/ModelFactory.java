package ch.metzenthin.svm.domain.model;

/**
 * @author Hans Stamm
 */
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
    KursortErfassenModel createKursortErfassenModel();
    KurstypenModel createKurstypenModel();
    KurstypErfassenModel createKurstypErfassenModel();
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
