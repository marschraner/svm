package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Listentyp;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import java.io.File;

/**
 * @author Hans Stamm
 */
public interface ListenExportModel {

  ValidationResult validateListentyp(Listentyp listentyp);

  ValidationResult validateTitel(String titel);

  void setExportFile(File exportFile);

  ValidationResultsAndSubmitResult submit(Listentyp listentyp, String titel);

  Listentyp getListentyp();

  String getTitel();

  File getExportFile();
}
