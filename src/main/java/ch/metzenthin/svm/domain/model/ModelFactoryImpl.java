package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import ch.metzenthin.svm.service.ElternmithilfeCodeService;
import ch.metzenthin.svm.service.KursLehrkraftService;
import ch.metzenthin.svm.service.KursService;
import ch.metzenthin.svm.service.KursortService;
import ch.metzenthin.svm.service.KurstypService;
import ch.metzenthin.svm.service.LektionsgebuehrenService;
import ch.metzenthin.svm.service.MaerchenService;
import ch.metzenthin.svm.service.MitarbeiterCodeService;
import ch.metzenthin.svm.service.MitarbeiterService;
import ch.metzenthin.svm.service.SchuelerCodeService;
import ch.metzenthin.svm.service.SemesterService;
import ch.metzenthin.svm.service.SemesterrechnungCodeService;
import ch.metzenthin.svm.service.SemesterrechnungService;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * @author Hans Stamm
 */
@SuppressWarnings("java:S6539")
@Component
public class ModelFactoryImpl implements ModelFactory {

  private final KursService kursService;
  private final KursortService kursortService;
  private final KurstypService kurstypService;
  private final MaerchenService maerchenService;
  private final SchuelerCodeService schuelerCodeService;
  private final MitarbeiterCodeService mitarbeiterCodeService;
  private final ElternmithilfeCodeService elternmithilfeCodeService;
  private final SemesterrechnungCodeService semesterrechnungCodeService;
  private final LektionsgebuehrenService lektionsgebuehrenService;
  private final SemesterService semesterService;
  private final SemesterrechnungService semesterrechnungService;
  private final MitarbeiterService mitarbeiterService;
  private final KursLehrkraftService kursLehrkraftService;

  public ModelFactoryImpl(
      KursService kursService,
      KursortService kursortService,
      KurstypService kurstypService,
      MaerchenService maerchenService,
      SchuelerCodeService schuelerCodeService,
      MitarbeiterCodeService mitarbeiterCodeService,
      ElternmithilfeCodeService elternmithilfeCodeService,
      SemesterrechnungCodeService semesterrechnungCodeService,
      LektionsgebuehrenService lektionsgebuehrenService,
      SemesterService semesterService,
      SemesterrechnungService semesterrechnungService,
      MitarbeiterService mitarbeiterService,
      KursLehrkraftService kursLehrkraftService) {
    this.kursService = kursService;
    this.kursortService = kursortService;
    this.kurstypService = kurstypService;
    this.maerchenService = maerchenService;
    this.schuelerCodeService = schuelerCodeService;
    this.mitarbeiterCodeService = mitarbeiterCodeService;
    this.elternmithilfeCodeService = elternmithilfeCodeService;
    this.semesterrechnungCodeService = semesterrechnungCodeService;
    this.lektionsgebuehrenService = lektionsgebuehrenService;
    this.semesterService = semesterService;
    this.semesterrechnungService = semesterrechnungService;
    this.mitarbeiterService = mitarbeiterService;
    this.kursLehrkraftService = kursLehrkraftService;
  }

  @Override
  public SvmModel createSvmModel() {
    return new SvmModelImpl(
        kursortService,
        kurstypService,
        schuelerCodeService,
        mitarbeiterCodeService,
        elternmithilfeCodeService,
        semesterrechnungCodeService,
        lektionsgebuehrenService,
        semesterService);
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
  public ElternmithilfeCodeListModel createElternmithilfeCodeListModel() {
    return new ElternmithilfeCodeListModel(elternmithilfeCodeService);
  }

  @Override
  public MitarbeiterCodeListModel createMitarbeiterCodeListModel() {
    return new MitarbeiterCodeListModel(mitarbeiterCodeService);
  }

  @Override
  public SchuelerCodeListModel createSchuelerCodeListModel() {
    return new SchuelerCodeListModel(schuelerCodeService);
  }

  @Override
  public SemesterrechnungCodeListModel createSemesterrechnungCodeListModel() {
    return new SemesterrechnungCodeListModel(semesterrechnungCodeService);
  }

  @Override
  public CodesModel createCodesModel() {
    return new CodesModelImpl(
        schuelerCodeService,
        mitarbeiterCodeService,
        elternmithilfeCodeService,
        semesterrechnungCodeService);
  }

  @Override
  public CreateOrUpdateSchuelerCodeModel createCreateOrUpdateSchuelerCodeModel(
      Optional<SchuelerCode> schuelerCodeToBeModifiedOptional) {
    return new CreateOrUpdateSchuelerCodeModelImpl(
        schuelerCodeToBeModifiedOptional, schuelerCodeService);
  }

  @Override
  public CreateOrUpdateMitarbeiterCodeModel createCreateOrUpdateMitarbeiterCodeModel(
      Optional<MitarbeiterCode> mitarbeiterCodeToBeModifiedOptional) {
    return new CreateOrUpdateMitarbeiterCodeModelImpl(
        mitarbeiterCodeToBeModifiedOptional, mitarbeiterCodeService);
  }

  @Override
  public CreateOrUpdateElternmithilfeCodeModel createCreateOrUpdateElternmithilfeCodeModel(
      Optional<ElternmithilfeCode> elternmithilfeCodeToBeModifiedOptional) {
    return new CreateOrUpdateElternmithilfeCodeModelImpl(
        elternmithilfeCodeToBeModifiedOptional, elternmithilfeCodeService);
  }

  @Override
  public CreateOrUpdateSemesterrechnungCodeModel createCreateOrUpdateSemesterrechnungCodeModel(
      Optional<SemesterrechnungCode> semesterrechnungCodeToBeModifiedOptional) {
    return new CreateOrUpdateSemesterrechnungCodeModelImpl(
        semesterrechnungCodeToBeModifiedOptional, semesterrechnungCodeService);
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
  public KursortListModel createKursortListModel() {
    return new KursortListModel(kursortService);
  }

  @Override
  public CreateOrUpdateKursortModel createCreateOrUpdateKursortModel(
      Optional<Kursort> kursortToBeModifiedOptional) {
    return new CreateOrUpdateKursortModelImpl(kursortToBeModifiedOptional, kursortService);
  }

  @Override
  public KurstypListModel createKurstypListModel() {
    return new KurstypListModel(kurstypService);
  }

  @Override
  public CreateOrUpdateKurstypModel createCreateOrUpdateKurstypModel(
      Optional<Kurstyp> kurstypToBeModifiedOptional) {
    return new CreateOrUpdateKurstypModelImpl(kurstypToBeModifiedOptional, kurstypService);
  }

  @Override
  public SemesterListModel createSemesterListModel() {
    return new SemesterListModel(kursService, semesterService, semesterrechnungService);
  }

  @Override
  public CreateOrUpdateSemesterModel createCreateOrUpdateSemesterModel(
      Optional<Semester> semesterToBeModifiedOptional) {
    return new CreateOrUpdateSemesterModelImpl(semesterToBeModifiedOptional, semesterService);
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
  public KursListModel createKursListModel(Semester semester) {
    return new KursListModel(kursService, semester);
  }

  @Override
  public KursErfassenModel createKursErfassenModel() {
    return new KursErfassenModelImpl();
  }

  @Override
  public CreateOrUpdateKursModel createCreateOrUpdateKursModel(
      Optional<Kurs> kursToBeModifiedOptional, Semester semester) {
    return new CreateOrUpdateKursModelImpl(
        kursToBeModifiedOptional,
        semester,
        kursService,
        kurstypService,
        kursortService,
        mitarbeiterService,
        kursLehrkraftService);
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
  public MaerchenListModel createMaerchenListModel() {
    return new MaerchenListModel(maerchenService);
  }

  @Override
  public CreateOrUpdateMaerchenModel createCreateOrUpdateMaerchenModel(
      Optional<Maerchen> maerchenToBeModifiedOptional) {
    return new CreateOrUpdateMaerchenModelImpl(maerchenToBeModifiedOptional, maerchenService);
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
  public LektionsgebuehrenListModel createLektionsgebuehrenListModel() {
    return new LektionsgebuehrenListModel(lektionsgebuehrenService);
  }

  @Override
  public CreateOrUpdateLektionsgebuehrenModel createCreateOrUpdateLektionsgebuehrenModel(
      Optional<Lektionsgebuehren> lektionsgebuehrenToBeModifiedOptional) {
    return new CreateOrUpdateLektionsgebuehrenModelImpl(
        lektionsgebuehrenToBeModifiedOptional, lektionsgebuehrenService);
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
