package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.utils.SvmProperties;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Listentyp;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import ch.metzenthin.svm.ui.componentmodel.LehrkraefteTableModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;

import java.io.File;
import java.util.Properties;

/**
 * @author Martin Schraner
 */
public class ListenExportModelImpl extends AbstractModel implements ListenExportModel {
    
    private Listentyp listentyp;
    private String titel;
    private File templateFile;
    
    public ListenExportModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public Listentyp getListentyp() {
        return listentyp;
    }

    @Override
    public void setListentyp(Listentyp listentyp) throws SvmRequiredException {
        Listentyp oldValue = this.listentyp;
        this.listentyp = listentyp;
        firePropertyChange(Field.LISTENTYP, oldValue, this.listentyp);
        if (listentyp == null) {
            invalidate();
            throw new SvmRequiredException(Field.LISTENTYP);
        }
    }

    private final StringModelAttribute titelModelAttribute = new StringModelAttribute(
            this,
            Field.TITEL, 2, 80,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return titel;
                }

                @Override
                public void setValue(String value) {
                    titel = value;
                }
            }
    );

    @Override
    public String getTitel() {
        return titelModelAttribute.getValue();
    }

    @Override
    public File getTemplateFile() {
        return templateFile;
    }

    @Override
    public void setTitel(String titel) throws SvmValidationException {
        titelModelAttribute.setNewValue(true, titel, isBulkUpdate());
    }

    @Override
    public File getSaveFileInit() {
        Properties prop = SvmProperties.getSvmProperties();
        File listenDirectoryInit = new File(prop.getProperty(SvmProperties.KEY_DEFAULT_OUTPUT_DIRECTORY) + File.separator);
        if (!listenDirectoryInit.exists()) {
            boolean success = listenDirectoryInit.mkdirs();
            if (!success) {
                return null;
            }
        }
        String outputFile = listentyp + "." + listentyp.getFiletyp().getFileExtension();
        outputFile = outputFile.replaceAll("\\p{Blank}", "_");
        outputFile = outputFile.replaceAll("ä", "ae");
        outputFile = outputFile.replaceAll("ö", "oe");
        outputFile = outputFile.replaceAll("ü", "ue");
        return new File(listenDirectoryInit.getAbsolutePath() + File.separator + outputFile);
    }

    @Override
    public CreateListeCommand.Result createListenFile(File outputFile, SchuelerSuchenTableModel schuelerSuchenTableModel, LehrkraefteTableModel lehrkraefteTableModel, KurseTableModel kurseTableModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        CreateListeCommand.Result result = null;
        switch (listentyp) {
            case SCHUELER_ADRESSLISTE:
                CreateSchuelerAdresslisteCommand createSchuelerAdresslisteCommand = new CreateSchuelerAdresslisteCommand(schuelerSuchenTableModel, titel, outputFile);
                commandInvoker.executeCommand(createSchuelerAdresslisteCommand);
                result = createSchuelerAdresslisteCommand.getResult();
                break;
            case SCHUELER_ABSENZENLISTE:
                CreateListeFromTemplateCommand createListeFromTemplateCommand = new CreateListeFromTemplateCommand(schuelerSuchenTableModel, titel, listentyp, outputFile);
                commandInvoker.executeCommand(createListeFromTemplateCommand);
                templateFile = createListeFromTemplateCommand.getTemplateFile();
                result = createListeFromTemplateCommand.getResult();
                break;
            case SCHUELER_ADRESSETIKETTEN:
                CreateAdressenCsvFileCommand createAdressenCsvFileCommandSchueler = new CreateAdressenCsvFileCommand(schuelerSuchenTableModel.getSchuelerList(), outputFile);
                commandInvoker.executeCommand(createAdressenCsvFileCommandSchueler);
                result = createAdressenCsvFileCommandSchueler.getResult();
                break;
            case LEHRKRAEFTE_ADRESSLISTE:
                CreateLehrkraefteAdresslisteCommand createLehrkraefteAdresslisteCommand = new CreateLehrkraefteAdresslisteCommand(lehrkraefteTableModel, titel, outputFile);
                commandInvoker.executeCommand(createLehrkraefteAdresslisteCommand);
                result = createLehrkraefteAdresslisteCommand.getResult();
                break;
            case LEHRKRAEFTE_ADRESSETIKETTEN:
                CreateAdressenCsvFileCommand createAdressenCsvFileCommandLehrkraefte = new CreateAdressenCsvFileCommand(lehrkraefteTableModel.getLehrkraefte(), outputFile);
                commandInvoker.executeCommand(createAdressenCsvFileCommandLehrkraefte);
                result = createAdressenCsvFileCommandLehrkraefte.getResult();
                break;
            case KURSELISTE:
                CreateKurselisteCommand kurselisteCommand = new CreateKurselisteCommand(kurseTableModel, titel, outputFile);
                commandInvoker.executeCommand(kurselisteCommand);
                result = kurselisteCommand.getResult();
                break;
        }
        return result;
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    void doValidate() throws SvmValidationException {}
}
