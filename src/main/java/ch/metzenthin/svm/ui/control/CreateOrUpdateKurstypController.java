package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.model.CreateOrUpdateKurstypModel;
import ch.metzenthin.svm.service.result.SaveKurstypResult;
import ch.metzenthin.svm.ui.components.CreateOrUpdateKurstypView;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateKurstypController
    extends CreateOrUpdateBezeichnungAndSelektierbarController<
        CreateOrUpdateKurstypModel, CreateOrUpdateKurstypView, SaveKurstypResult> {

  CreateOrUpdateKurstypController(
      CreateOrUpdateKurstypModel createOrUpdateKurstypModel, boolean isBearbeiten, String title) {
    super(createOrUpdateKurstypModel, new CreateOrUpdateKurstypView(title), isBearbeiten);
  }
}
