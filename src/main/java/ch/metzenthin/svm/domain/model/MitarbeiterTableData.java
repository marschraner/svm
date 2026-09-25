package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

import ch.metzenthin.svm.common.datatypes.Field;
import java.util.Calendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class MitarbeiterTableData
    extends AbstractTableData<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert> {

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

  public MitarbeiterTableData(
      List<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert>
          mitarbeiterAndMitarbeiterCodesAsStringAndSelektiertList) {
    super(COLUMNS, mitarbeiterAndMitarbeiterCodesAsStringAndSelektiertList);
  }

  @SuppressWarnings("java:S3776")
  public Object getValueAt(int rowIndex, int columnIndex) {
    MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
        mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert = data.get(rowIndex);
    Object value = null;
    switch (COLUMNS[columnIndex]) {
      case SELEKTIERT -> value = mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.selektiert();
      case NACHNAME ->
          value = mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.mitarbeiter().getNachname();
      case VORNAME ->
          value = mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.mitarbeiter().getVorname();
      case STRASSE_HAUSNUMMER ->
          value =
              (mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.mitarbeiter().getAdresse()
                      == null
                  ? ""
                  : mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                      .mitarbeiter()
                      .getAdresse()
                      .getStrHausnummer());
      case PLZ ->
          value =
              (mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.mitarbeiter().getAdresse()
                      == null
                  ? ""
                  : mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                      .mitarbeiter()
                      .getAdresse()
                      .getPlz());
      case ORT ->
          value =
              (mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.mitarbeiter().getAdresse()
                      == null
                  ? ""
                  : mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                      .mitarbeiter()
                      .getAdresse()
                      .getOrt());
      case FESTNETZ ->
          value =
              (!checkNotEmpty(
                      mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                          .mitarbeiter()
                          .getFestnetz())
                  ? ""
                  : mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                      .mitarbeiter()
                      .getFestnetz());
      case NATEL ->
          value =
              (!checkNotEmpty(
                      mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.mitarbeiter().getNatel())
                  ? ""
                  : mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.mitarbeiter().getNatel());
      case EMAIL ->
          value =
              (!checkNotEmpty(
                      mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.mitarbeiter().getEmail())
                  ? ""
                  : mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.mitarbeiter().getEmail());
      case GEBURTSDATUM ->
          value =
              mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.mitarbeiter().getGeburtsdatum();
      case AHV_NUMMER ->
          value =
              (!checkNotEmpty(
                      mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                          .mitarbeiter()
                          .getAhvNummer())
                  ? ""
                  : mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                      .mitarbeiter()
                      .getAhvNummer());
      case IBAN_NUMMER ->
          value =
              (!checkNotEmpty(
                      mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                          .mitarbeiter()
                          .getIbanNummer())
                  ? ""
                  : mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                      .mitarbeiter()
                      .getIbanNummer());
      case LEHRKRAFT ->
          value =
              (mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.mitarbeiter().isLehrkraft()
                  ? "ja"
                  : "nein");
      case AKTIV ->
          value =
              (mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.mitarbeiter().isAktiv())
                  ? "ja"
                  : "nein";
      case CODES ->
          value = mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.mitarbeiterCodesAsString();
      case VERTRETUNGSMOEGLICHKEITEN ->
          value =
              mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                  .mitarbeiter()
                  .getVertretungsmoeglichkeitenLineBreaksReplacedBySemicolonOrPeriod();
      case BEMERKUNGEN ->
          value =
              mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                  .mitarbeiter()
                  .getBemerkungenLineBreaksReplacedBySemicolonOrPeriod();
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
