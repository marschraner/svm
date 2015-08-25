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
    MonatsstatistikModel createMonatsstatistikModel();
    DispensationenModel createDispensationenModel();
    DispensationErfassenModel createDispensationErfassenModel();
    CodesModel createCodesModel();
    CodeErfassenModel createCodeErfassenModel();
    SchuelerCodeSchuelerHinzufuegenModel createCodeSchuelerHinzufuegenModel();
    LehrkraefteModel createLehrkraefteModel();
    LehrkraftErfassenModel createLehrkraftErfassenModel();
    KursorteModel createKursorteModel();
    KursortErfassenModel createKursortErfassenModel();
    KurstypenModel createKurstypenModel();
    KurstypErfassenModel createKurstypErfassenModel();
    SemestersModel createSemestersModel();
    SemesterErfassenModel createSemesterErfassenModel();
    KurseSemesterwahlModel createKurseSemesterwahlModel();
    KurseModel createKurseModel();
    KursErfassenModel createKursErfassenModel();
    KursSchuelerHinzufuegenModel createKursSchuelerHinzufuegenModel();
    ListenExportModel createListenExportModel();
    MaerchensModel createMaerchensModel();
    MaerchenErfassenModel createMaerchenErfassenModel();
    MaercheneinteilungenModel createMaercheneinteilungenModel();
    MaercheneinteilungErfassenModel createMaercheneinteilungErfassenModel();
    EmailModel createEmailModel();
    LektionsgebuehrenModel createLektionsgebuehrenModel();
    LektionsgebuehrenErfassenModel createLektionsgebuehrenErfassenModel();
}
