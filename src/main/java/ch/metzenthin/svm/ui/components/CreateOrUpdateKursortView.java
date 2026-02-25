package ch.metzenthin.svm.ui.components;

/**
 * @author Hans Stamm
 */
public class CreateOrUpdateKursortView
    extends CreateOrUpdateBezeichnungAndSelektierbarView<CreateOrUpdateKursortDialog> {

  public CreateOrUpdateKursortView(String title) {
    super(new CreateOrUpdateKursortDialog(title));
  }
}
