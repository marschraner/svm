package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;

/**
 * @author Martin Schraner
 */
public class KurseSuchenModelImpl extends AbstractModel implements KurseSuchenModel {

    private String schuljahr;
    private Semesterbezeichnung semesterbezeichnung;

    public KurseSuchenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    private final StringModelAttribute schuljahrModelAttribute = new StringModelAttribute(
            this,
            Field.SCHULJAHR, 9, 9,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return schuljahr;
                }

                @Override
                public void setValue(String value) {
                    schuljahr = value;
                }
            }
    );

    @Override
    public String getSchuljahr() {
        return schuljahrModelAttribute.getValue();
    }

    @Override
    public void setSchuljahr(String schuljahr) throws SvmValidationException {
        schuljahrModelAttribute.setNewValue(true, schuljahr, isBulkUpdate());
    }

    @Override
    public Semesterbezeichnung getSemesterbezeichnung() {
        return semesterbezeichnung;
    }

    @Override
    public void setSemesterbezeichnung(Semesterbezeichnung semesterbezeichnung) {
        Semesterbezeichnung oldValue = this.semesterbezeichnung;
        this.semesterbezeichnung = semesterbezeichnung;
        firePropertyChange(Field.SEMESTERBEZEICHNUNG, oldValue, this.semesterbezeichnung);
    }

    @Override
    public KurseTableData suchen() {
        //TODO
//        KurseSuchenCommand kurseSuchenCommand = new KurseSuchenCommand(this);
//        CommandInvoker commandInvoker = getCommandInvoker();
//        commandInvoker.executeCommand(kurseSuchenCommand);
//        List<Kurs> kurseFound = kurseSuchenCommand.getKurseFound();
//        return new KurseTableData(kurseFound);
        return null;
    }


    @Override
    void doValidate() throws SvmValidationException {}

    @Override
    public boolean isCompleted() {
        return false;
    }
}
