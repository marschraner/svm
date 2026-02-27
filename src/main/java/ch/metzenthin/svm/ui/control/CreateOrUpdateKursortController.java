package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.model.CreateOrUpdateKursortModel;
import ch.metzenthin.svm.service.result.SaveKursortResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateKursortView;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateKursortController
    extends CreateOrUpdateBezeichnungAndSelektierbarController<
        CreateOrUpdateKursortModel, CreateOrUpdateKursortView, SaveKursortResult> {

  CreateOrUpdateKursortController(
      CreateOrUpdateKursortModel createOrUpdateKursortModel, boolean isBearbeiten, String title) {
    super(createOrUpdateKursortModel, new CreateOrUpdateKursortView(title), isBearbeiten);
  }
}
