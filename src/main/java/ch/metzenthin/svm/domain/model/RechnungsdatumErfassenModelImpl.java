package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.common.utils.Converter.getNMonthsAfterNow;
import static ch.metzenthin.svm.common.utils.Converter.getNYearsBeforeNow;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Rechnungstyp;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.ReplaceRechnungsdatumAndUpdateSemesterrechnungenCommand;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;
import java.util.Calendar;
import java.util.Set;

/**
 * @author Martin Schraner
 */
public class RechnungsdatumErfassenModelImpl extends AbstractModel
    implements RechnungsdatumErfassenModel {

  private Calendar rechnungsdatum;

  private final CalendarModelAttribute rechnungsdatumModelAttribute =
      new CalendarModelAttribute(
          this,
          Field.RECHNUNGSDATUM,
          getNYearsBeforeNow(10),
          getNMonthsAfterNow(2),
          new AttributeAccessor<>() {
            @Override
            public Calendar getValue() {
              return rechnungsdatum;
            }

            @Override
            public void setValue(Calendar value) {
              rechnungsdatum = value;
            }
          });

  @Override
  public Calendar getRechnungsdatum() {
    return rechnungsdatumModelAttribute.getValue();
  }

  @Override
  public void setRechnungsdatum(String rechnungsdatum) throws SvmValidationException {
    rechnungsdatumModelAttribute.setNewValue(false, rechnungsdatum, isBulkUpdate());
  }

  @Override
  public void replaceRechnungsdatumAndUpdateSemesterrechnung(
      SemesterrechnungenTableModel semesterrechnungenTableModel, Rechnungstyp rechnungstyp) {

    CommandInvoker commandInvoker = getCommandInvoker();
    ReplaceRechnungsdatumAndUpdateSemesterrechnungenCommand
        replaceRechnungsdatumAndUpdateSemesterrechnungenCommand =
            new ReplaceRechnungsdatumAndUpdateSemesterrechnungenCommand(
                semesterrechnungenTableModel.getSelektierteSemesterrechnungen(),
                rechnungstyp,
                rechnungsdatum);
    commandInvoker.executeCommandAsTransaction(
        replaceRechnungsdatumAndUpdateSemesterrechnungenCommand);

    // Semesterrechnungen Table-Model mit aktuellen Werten updaten
    Set<Semesterrechnung> updatedSemesterrechnungen =
        replaceRechnungsdatumAndUpdateSemesterrechnungenCommand.getUpdatedSemesterrechnungen();
    semesterrechnungenTableModel.updateSemesterrechnungen(updatedSemesterrechnungen);
  }

  @Override
  public boolean isCompleted() {
    return true;
  }

  @Override
  void doValidate() throws SvmValidationException {
    // Keine feldübergreifende Validierung notwendig
  }
}
