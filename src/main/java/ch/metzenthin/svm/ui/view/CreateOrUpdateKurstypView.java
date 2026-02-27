package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.components.CreateOrUpdateKurstypDialog;

/**
 * @author Hans Stamm
 */
public class CreateOrUpdateKurstypView
    extends CreateOrUpdateBezeichnungAndSelektierbarDialogView<CreateOrUpdateKurstypDialog> {

  public CreateOrUpdateKurstypView(String title) {
    super(new CreateOrUpdateKurstypDialog(title));
  }
}
