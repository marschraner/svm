package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.datatypes.Filetyp;
import ch.metzenthin.svm.common.datatypes.Listentyp;
import ch.metzenthin.svm.common.utils.SvmProperties;
import ch.metzenthin.svm.ui.view.AbstractView;
import java.awt.Component;
import java.io.File;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Hans Stamm
 */
public class ListenExportFileChooser extends AbstractView<Component> {

  private final JFileChooser fileChooser = new JFileChooser();

  public ListenExportFileChooser(Listentyp listentyp, Component parent) {
    super(parent);
    File exportFileInit = getSaveFileInit(listentyp);
    if (exportFileInit != null) {
      fileChooser.setSelectedFile(exportFileInit);
    }
    final Filetyp filetyp = listentyp.getFiletyp();
    fileChooser.addChoosableFileFilter(
        new FileNameExtensionFilter(
            filetyp.getBezeichnung() + "-Dateien (*." + filetyp.getFileExtension() + ")",
            filetyp.getFileExtension()));
    fileChooser.setAcceptAllFileFilterUsed(true);
  }

  public File getSelectedFile() {
    int returnVal = fileChooser.showSaveDialog(component);
    if (returnVal != JFileChooser.APPROVE_OPTION) {
      return null;
    }
    File outputFile = fileChooser.getSelectedFile();
    if (outputFile.exists()) {
      int n =
          showYesNoDialog(
              "Die Datei '"
                  + outputFile.getName()
                  + "' existiert bereits. Soll sie überschrieben werden?",
              "Datei existiert bereits");
      if (n != 0) {
        return null;
      }
    }
    return outputFile;
  }

  private File getSaveFileInit(Listentyp listentyp) {
    Properties prop = SvmProperties.getSvmProperties();
    File listenDirectoryInit =
        new File(prop.getProperty(SvmProperties.KEY_DEFAULT_OUTPUT_DIRECTORY) + File.separator);
    if (!listenDirectoryInit.exists()) {
      boolean success = listenDirectoryInit.mkdirs();
      if (!success) {
        return null;
      }
    }
    String outputFile =
        listentyp.getFilenameOhneFileExtension() + "." + listentyp.getFiletyp().getFileExtension();
    outputFile = outputFile.replaceAll(",[ \\t]", "_");
    outputFile = outputFile.replaceAll("[ \\t]", "_");
    outputFile = outputFile.replace("ä", "ae");
    outputFile = outputFile.replace("ö", "oe");
    outputFile = outputFile.replace("ü", "ue");
    return new File(listenDirectoryInit.getAbsolutePath() + File.separator + outputFile);
  }
}
