package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Listentyp;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.common.utils.SvmProperties;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.io.File;
import java.util.Properties;

/**
 * @author Martin Schraner
 */
public class FindTemplateFileCommand implements Command {

    public enum Result {
        KEIN_TEMPLATE_FILE_BENOETIGT,
        TEMPLATE_FILE_EXISTIERT,
        TEMPLATE_FILE_EXISTIERT_NICHT_ODER_NICHT_LESBAR,
        FEHLER_BEIM_LESEN_DES_PROPERTY_FILE
    }

    // input
    private final Listentyp listentyp;
    private final Semester semester;
    private final Wochentag wochentag;

    // output
    private File templateFile;
    private Result result;

    FindTemplateFileCommand(Listentyp listentyp, Semester semester, Wochentag wochentag) {
        this.listentyp = listentyp;
        this.semester = semester;
        this.wochentag = wochentag;
    }

    @Override
    public void execute() {

        String templateFileAsStr;

        if (listentyp.getSvmPropertiesKey() == null) {
            result = Result.KEIN_TEMPLATE_FILE_BENOETIGT;
            return;
        }

        try {
            Properties prop = SvmProperties.getSvmProperties();
            String templatesDirectory = prop.getProperty(SvmProperties.KEY_TEMPLATES_DIRECTORY);
            String templateFileName = prop.getProperty(listentyp.getSvmPropertiesKey());
            if (templatesDirectory == null || templateFileName == null) {
                throw new RuntimeException();
            }
            templateFileAsStr = templatesDirectory + File.separator + templateFileName;
        } catch (Throwable e) {
            result = Result.FEHLER_BEIM_LESEN_DES_PROPERTY_FILE;
            return;
        }

        // Schuljahr, Semester und Wochentag ersetzen
        String schuljahrStr = semester.getSchuljahr().substring(0, 4) + "_" + semester.getSchuljahr().substring(5, 9);
        String semesterbezeichnungStr = semester.getSemesterbezeichnung().getKuerzelInTemplateFile();
        templateFileAsStr = templateFileAsStr.replaceAll("<Schuljahr>", schuljahrStr);
        templateFileAsStr = templateFileAsStr.replaceAll("<Semester>", semesterbezeichnungStr);
        if (wochentag != null) {
            templateFileAsStr = templateFileAsStr.replaceAll("<Wochentag>", wochentag.toString().substring(0, 2));
        }

        // Prüfen, ob File existiert
        templateFile = new File(templateFileAsStr);
        if (!templateFile.exists() || !templateFile.canRead()) {
            result = Result.TEMPLATE_FILE_EXISTIERT_NICHT_ODER_NICHT_LESBAR;
            return;
        }

        result = Result.TEMPLATE_FILE_EXISTIERT;
    }

    File getTemplateFile() {
        return templateFile;
    }

    public Result getResult() {
        return result;
    }
}
