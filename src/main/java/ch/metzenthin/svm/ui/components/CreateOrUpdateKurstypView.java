package ch.metzenthin.svm.ui.components;

/**
 * @author Hans Stamm
 */
public class CreateOrUpdateKurstypView
    extends CreateOrUpdateBezeichnungAndSelektierbarView<CreateOrUpdateKurstypDialog> {

  public CreateOrUpdateKurstypView(String title) {
    super(new CreateOrUpdateKurstypDialog(title));
  }
}
