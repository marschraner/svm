package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.CommandInvoker;

/**
 * @author Hans Stamm
 */
public class ModelFactoryImpl implements ModelFactory {

    private final CommandInvoker commandInvoker;

    public ModelFactoryImpl(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    @Override
    public SvmModel createSvmModel() {
        return new SvmModelImpl(commandInvoker);
    }

    @Override
    public SchuelerModel createSchuelerModel() {
        return new SchuelerModelImpl(commandInvoker);
    }

    @Override
    public AngehoerigerModel createAngehoerigerModel() {
        return new AngehoerigerModelImpl(commandInvoker);
    }

    @Override
    public SchuelerErfassenModel createSchuelerErfassenModel() {
        return new SchuelerErfassenModelImpl(commandInvoker);
    }

    @Override
    public SchuelerSuchenModel createSchuelerSuchenModel() {
        return new SchuelerSuchenModelImpl(commandInvoker);
    }

    @Override
    public MonatsstatistikSchuelerModel createMonatsstatistikSchuelerModel() {
        return new MonatsstatistikSchuelerModelImpl(commandInvoker);
    }

    @Override
    public MonatsstatistikKurseModel createMonatsstatistikKurseModel() {
        return new MonatsstatistikKurseModelImpl(commandInvoker);
    }

    @Override
    public DispensationenModel createDispensationenModel() {
        return new DispensationenModelImpl(commandInvoker);
    }

    @Override
    public DispensationErfassenModel createDispensationErfassenModel() {
        return new DispensationErfassenModelImpl(commandInvoker);
    }

    @Override
    public CodesModel createCodesModel() {
        return new CodesModelImpl(commandInvoker);
    }

    @Override
    public CodeErfassenModel createCodeErfassenModel() {
        return new CodeErfassenModelImpl(commandInvoker);
    }

    @Override
    public SchuelerCodeSchuelerHinzufuegenModel createCodeSchuelerHinzufuegenModel() {
        return new SchuelerCodeSchuelerHinzufuegenModelImpl(commandInvoker);
    }

    @Override
    public MitarbeitersModel createLehrkraefteModel() {
        return new MitarbeitersModelImpl(commandInvoker);
    }

    @Override
    public MitarbeiterErfassenModel createMitarbeiterErfassenModel() {
        return new MitarbeiterErfassenModelImpl(commandInvoker);
    }

    @Override
    public KursorteModel createKursorteModel() {
        return new KursorteModelImpl(commandInvoker);
    }

    @Override
    public KursortErfassenModel createKursortErfassenModel() {
        return new KursortErfassenModelImpl(commandInvoker);
    }

    @Override
    public KurstypenModel createKurstypenModel() {
        return new KurstypenModelImpl(commandInvoker);
    }

    @Override
    public KurstypErfassenModel createKurstypErfassenModel() {
        return new KurstypErfassenModelImpl(commandInvoker);
    }

    @Override
    public SemestersModel createSemestersModel() {
        return new SemestersModelImpl(commandInvoker);
    }

    @Override
    public SemesterErfassenModel createSemesterErfassenModel() {
        return new SemesterErfassenModelImpl(commandInvoker);
    }

    @Override
    public KurseSemesterwahlModel createKurseSemesterwahlModel() {
        return new KurseSemesterwahlModelImpl(commandInvoker);
    }

    @Override
    public KurseModel createKurseModel() {
        return new KurseModelImpl(commandInvoker);
    }

    @Override
    public KursErfassenModel createKursErfassenModel() {
        return new KursErfassenModelImpl(commandInvoker);
    }

    @Override
    public KursanmeldungenModel createKursanmeldungenModel() {
        return new KursanmeldungenModelImpl(commandInvoker);
    }

    @Override
    public KursanmeldungErfassenModel createKursanmeldungErfassenModel() {
        return new KursanmeldungErfassenModelImpl(commandInvoker);
    }

    @Override
    public ListenExportModel createListenExportModel() {
        return new ListenExportModelImpl(commandInvoker);
    }

    @Override
    public MaerchensModel createMaerchensModel() {
        return new MaerchensModelImpl(commandInvoker);
    }

    @Override
    public MaerchenErfassenModel createMaerchenErfassenModel() {
        return new MaerchenErfassenModelImpl(commandInvoker);
    }

    @Override
    public MaercheneinteilungenModel createMaercheneinteilungenModel() {
        return new MaercheneinteilungenModelImpl(commandInvoker);
    }

    @Override
    public MaercheneinteilungErfassenModel createMaercheneinteilungErfassenModel() {
        return new MaercheneinteilungErfassenModelImpl(commandInvoker);
    }

    @Override
    public EmailModel createEmailModel() {
        return new EmailModelImpl(commandInvoker);
    }

    @Override
    public LektionsgebuehrenModel createLektionsgebuehrenModel() {
        return new LektionsgebuehrenModelImpl(commandInvoker);
    }

    @Override
    public LektionsgebuehrenErfassenModel createLektionsgebuehrenErfassenModel() {
        return new LektionsgebuehrenErfassenModelImpl(commandInvoker);
    }

    @Override
    public SemesterrechnungenSuchenModel createSemesterrechnungenSuchenModel() {
        return new SemesterrechnungenSuchenModelImpl(commandInvoker);
    }

    @Override
    public SemesterrechnungenModel createSemesterrechnungenModel() {
        return new SemesterrechnungenModelImpl(commandInvoker);
    }

    @Override
    public SemesterrechnungBearbeitenModel createSemesterrechnungBearbeitenModel() {
        return new SemesterrechnungBearbeitenModelImpl(commandInvoker);
    }

    @Override
    public RechnungsdatumErfassenModel createRechnungsdatumErfassenModel() {
        return new RechnungsdatumErfassenModelImpl(commandInvoker);
    }

}
