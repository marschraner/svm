package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import java.util.Calendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class MitarbeiterTableData extends AbstractTableData<Mitarbeiter> {

  private static final Field[] COLUMNS = {
    Field.SELEKTIERT,
    Field.NACHNAME,
    Field.VORNAME,
    Field.STRASSE_HAUSNUMMER,
    Field.PLZ,
    Field.ORT,
    Field.FESTNETZ,
    Field.NATEL,
    Field.EMAIL,
    Field.GEBURTSDATUM,
    Field.AHV_NUMMER,
    Field.IBAN_NUMMER,
    Field.LEHRKRAFT,
    Field.CODES,
    Field.VERTRETUNGSMOEGLICHKEITEN,
    Field.BEMERKUNGEN,
    Field.AKTIV
  };

  public MitarbeiterTableData(List<Mitarbeiter> mitarbeiterList) {
    super(COLUMNS, mitarbeiterList);
  }

  @SuppressWarnings("java:S3776")
  public Object getValueAt(int rowIndex, int columnIndex) {
    Mitarbeiter mitarbeiter = data.get(rowIndex);
    Object value = null;
    switch (COLUMNS[columnIndex]) {
      case SELEKTIERT -> value = mitarbeiter.isSelektiert();
      case NACHNAME -> value = mitarbeiter.getNachname();
      case VORNAME -> value = mitarbeiter.getVorname();
      case STRASSE_HAUSNUMMER ->
          value =
              (mitarbeiter.getAdresse() == null ? "" : mitarbeiter.getAdresse().getStrHausnummer());
      case PLZ ->
          value = (mitarbeiter.getAdresse() == null ? "" : mitarbeiter.getAdresse().getPlz());
      case ORT ->
          value = (mitarbeiter.getAdresse() == null ? "" : mitarbeiter.getAdresse().getOrt());
      case FESTNETZ ->
          value = (!checkNotEmpty(mitarbeiter.getFestnetz()) ? "" : mitarbeiter.getFestnetz());
      case NATEL -> value = (!checkNotEmpty(mitarbeiter.getNatel()) ? "" : mitarbeiter.getNatel());
      case EMAIL -> value = (!checkNotEmpty(mitarbeiter.getEmail()) ? "" : mitarbeiter.getEmail());
      case GEBURTSDATUM -> value = mitarbeiter.getGeburtsdatum();
      case AHV_NUMMER ->
          value = (!checkNotEmpty(mitarbeiter.getAhvNummer()) ? "" : mitarbeiter.getAhvNummer());
      case IBAN_NUMMER ->
          value = (!checkNotEmpty(mitarbeiter.getIbanNummer()) ? "" : mitarbeiter.getIbanNummer());
      case LEHRKRAFT -> value = (mitarbeiter.isLehrkraft() ? "ja" : "nein");
      case AKTIV -> value = (mitarbeiter.isAktiv()) ? "ja" : "nein";
      // case CODES -> value = mitarbeiter.getMitarbeiterCodesAsStr();
      case CODES -> value = "TODO";
      case VERTRETUNGSMOEGLICHKEITEN ->
          value = mitarbeiter.getVertretungsmoeglichkeitenLineBreaksReplacedBySemicolonOrPeriod();
      case BEMERKUNGEN -> value = mitarbeiter.getBemerkungenLineBreaksReplacedBySemicolonOrPeriod();
      default -> {
        // Nothing to do
      }
    }
    return value;
  }

  public Class<?> getColumnClass(int columnIndex) {
    return switch (COLUMNS[columnIndex]) {
      case GEBURTSDATUM -> Calendar.class;
      case SELEKTIERT -> Boolean.class;
      default -> String.class;
    };
  }
}
