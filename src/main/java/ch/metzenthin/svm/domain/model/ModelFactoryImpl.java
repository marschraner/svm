package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.service.KursortService;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * @author Hans Stamm
 */
@SuppressWarnings("java:S6539")
@Component
public class ModelFactoryImpl implements ModelFactory {

  private final KursortService kursortService;

  public ModelFactoryImpl(KursortService kursortService) {
    this.kursortService = kursortService;
  }

  @Override
  public SvmModel createSvmModel() {
    return new SvmModelImpl(kursortService);
  }

  @Override
  public SchuelerModel createSchuelerModel() {
    return new SchuelerModelImpl();
  }

  @Override
  public AngehoerigerModel createAngehoerigerModel() {
    return new AngehoerigerModelImpl();
  }

  @Override
  public SchuelerErfassenModel createSchuelerErfassenModel() {
    return new SchuelerErfassenModelImpl();
  }

  @Override
  public SchuelerSuchenModel createSchuelerSuchenModel() {
    return new SchuelerSuchenModelImpl();
  }

  @Override
  public MonatsstatistikSchuelerModel createMonatsstatistikSchuelerModel() {
    return new MonatsstatistikSchuelerModelImpl();
  }

  @Override
  public MonatsstatistikKurseModel createMonatsstatistikKurseModel() {
    return new MonatsstatistikKurseModelImpl();
  }

  @Override
  public DispensationenModel createDispensationenModel() {
    return new DispensationenModelImpl();
  }

  @Override
  public DispensationErfassenModel createDispensationErfassenModel() {
    return new DispensationErfassenModelImpl();
  }

  @Override
  public CodesModel createCodesModel() {
    return new CodesModelImpl();
  }

  @Override
  public CodeErfassenModel createCodeErfassenModel() {
    return new CodeErfassenModelImpl();
  }

  @Override
  public CodeSpecificHinzufuegenModel createCodeSchuelerHinzufuegenModel() {
    return new CodeSpecificHinzufuegenModelImpl();
  }

  @Override
  public MitarbeitersModel createLehrkraefteModel() {
    return new MitarbeitersModelImpl();
  }

  @Override
  public MitarbeiterErfassenModel createMitarbeiterErfassenModel() {
    return new MitarbeiterErfassenModelImpl();
  }

  @Override
  public KursorteModel createKursorteModel() {
    return new KursorteModelImpl(kursortService);
  }

  @Override
  public KursortErfassenModel createKursortErfassenModel(
      Optional<Kursort> kursortToBeModifiedOptional) {
    return new KursortErfassenModelImpl(kursortToBeModifiedOptional, kursortService);
  }

  @Override
  public KurstypenModel createKurstypenModel() {
    return new KurstypenModelImpl();
  }

  @Override
  public KurstypErfassenModel createKurstypErfassenModel() {
    return new KurstypErfassenModelImpl();
  }

  @Override
  public SemestersModel createSemestersModel() {
    return new SemestersModelImpl();
  }

  @Override
  public SemesterErfassenModel createSemesterErfassenModel() {
    return new SemesterErfassenModelImpl();
  }

  @Override
  public KurseSemesterwahlModel createKurseSemesterwahlModel() {
    return new KurseSemesterwahlModelImpl();
  }

  @Override
  public KurseModel createKurseModel() {
    return new KurseModelImpl();
  }

  @Override
  public KursErfassenModel createKursErfassenModel() {
    return new KursErfassenModelImpl();
  }

  @Override
  public KursanmeldungenModel createKursanmeldungenModel() {
    return new KursanmeldungenModelImpl();
  }

  @Override
  public KursanmeldungErfassenModel createKursanmeldungErfassenModel() {
    return new KursanmeldungErfassenModelImpl();
  }

  @Override
  public ListenExportModel createListenExportModel() {
    return new ListenExportModelImpl();
  }

  @Override
  public MaerchensModel createMaerchensModel() {
    return new MaerchensModelImpl();
  }

  @Override
  public MaerchenErfassenModel createMaerchenErfassenModel() {
    return new MaerchenErfassenModelImpl();
  }

  @Override
  public MaercheneinteilungenModel createMaercheneinteilungenModel() {
    return new MaercheneinteilungenModelImpl();
  }

  @Override
  public MaercheneinteilungErfassenModel createMaercheneinteilungErfassenModel() {
    return new MaercheneinteilungErfassenModelImpl();
  }

  @Override
  public EmailModel createEmailModel() {
    return new EmailModelImpl();
  }

  @Override
  public LektionsgebuehrenModel createLektionsgebuehrenModel() {
    return new LektionsgebuehrenModelImpl();
  }

  @Override
  public LektionsgebuehrenErfassenModel createLektionsgebuehrenErfassenModel() {
    return new LektionsgebuehrenErfassenModelImpl();
  }

  @Override
  public SemesterrechnungenSuchenModel createSemesterrechnungenSuchenModel() {
    return new SemesterrechnungenSuchenModelImpl();
  }

  @Override
  public SemesterrechnungenModel createSemesterrechnungenModel() {
    return new SemesterrechnungenModelImpl();
  }

  @Override
  public SemesterrechnungBearbeitenModel createSemesterrechnungBearbeitenModel() {
    return new SemesterrechnungBearbeitenModelImpl();
  }

  @Override
  public RechnungsdatumErfassenModel createRechnungsdatumErfassenModel() {
    return new RechnungsdatumErfassenModelImpl();
  }

  @Override
  public MitarbeiterSuchenModel createMitarbeitersSuchenModel() {
    return new MitarbeiterSuchenModelImpl();
  }

  @Override
  public EmailSchuelerListeModel createEmailSchuelerListeModel() {
    return new EmailSchuelerListeModelImpl();
  }

  @Override
  public EmailSemesterrechnungenModel createEmailSemesterrechnungenModel() {
    return new EmailSemesterrechnungenModelImpl();
  }
}
