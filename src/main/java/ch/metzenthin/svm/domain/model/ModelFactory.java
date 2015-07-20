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
    CodeSchuelerHinzufuegenModel createCodeSchuelerHinzufuegenModel();
    LehrkraefteModel createLehrkraefteModel();
    LehrkraftErfassenModel createLehrkraftErfassenModel();
    KursorteModel createKursorteModel();
    KursortErfassenModel createKursortErfassenModel();
    KurstypenModel createKurstypenModel();
    KurstypErfassenModel createKurstypErfassenModel();
}
