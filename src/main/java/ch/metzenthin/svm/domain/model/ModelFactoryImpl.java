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
    public MonatsstatistikModel createMonatsstatistikModel() {
        return new MonatsstatistikModelImpl(commandInvoker);
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
    public CodeSchuelerHinzufuegenModel createCodeSchuelerHinzufuegenModel() {
        return new CodeSchuelerHinzufuegenModelImpl(commandInvoker);
    }

    @Override
    public LehrkraefteModel createLehrkraefteModel() {
        return new LehrkraefteModelImpl(commandInvoker);
    }

    @Override
    public LehrkraftErfassenModel createLehrkraftErfassenModel() {
        return new LehrkraftErfassenModelImpl(commandInvoker);
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
    public KurseSuchenModel createKurseSuchenModel() {
        return new KurseSuchenModelImpl(commandInvoker);
    }

}
