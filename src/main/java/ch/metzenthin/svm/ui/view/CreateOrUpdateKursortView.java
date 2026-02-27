package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.components.CreateOrUpdateKursortDialog;

/**
 * @author Hans Stamm
 */
public class CreateOrUpdateKursortView
    extends CreateOrUpdateBezeichnungAndSelektierbarDialogView<CreateOrUpdateKursortDialog> {

  public CreateOrUpdateKursortView(String title) {
    super(new CreateOrUpdateKursortDialog(title));
  }
}
