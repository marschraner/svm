package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

import ch.metzenthin.svm.common.datatypes.Field;
import java.util.Calendar;
import java.util.Iterator;
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
      case SELEKTIERT -> value = mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.isSelektiert();
      case NACHNAME ->
          value =
              mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.getMitarbeiter().getNachname();
      case VORNAME ->
          value = mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.getMitarbeiter().getVorname();
      case STRASSE_HAUSNUMMER ->
          value =
              (mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.getMitarbeiter().getAdresse()
                      == null
                  ? ""
                  : mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                      .getMitarbeiter()
                      .getAdresse()
                      .getStrHausnummer());
      case PLZ ->
          value =
              (mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.getMitarbeiter().getAdresse()
                      == null
                  ? ""
                  : mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                      .getMitarbeiter()
                      .getAdresse()
                      .getPlz());
      case ORT ->
          value =
              (mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.getMitarbeiter().getAdresse()
                      == null
                  ? ""
                  : mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                      .getMitarbeiter()
                      .getAdresse()
                      .getOrt());
      case FESTNETZ ->
          value =
              (!checkNotEmpty(
                      mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                          .getMitarbeiter()
                          .getFestnetz())
                  ? ""
                  : mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                      .getMitarbeiter()
                      .getFestnetz());
      case NATEL ->
          value =
              (!checkNotEmpty(
                      mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                          .getMitarbeiter()
                          .getNatel())
                  ? ""
                  : mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                      .getMitarbeiter()
                      .getNatel());
      case EMAIL ->
          value =
              (!checkNotEmpty(
                      mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                          .getMitarbeiter()
                          .getEmail())
                  ? ""
                  : mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                      .getMitarbeiter()
                      .getEmail());
      case GEBURTSDATUM ->
          value =
              mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                  .getMitarbeiter()
                  .getGeburtsdatum();
      case AHV_NUMMER ->
          value =
              (!checkNotEmpty(
                      mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                          .getMitarbeiter()
                          .getAhvNummer())
                  ? ""
                  : mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                      .getMitarbeiter()
                      .getAhvNummer());
      case IBAN_NUMMER ->
          value =
              (!checkNotEmpty(
                      mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                          .getMitarbeiter()
                          .getIbanNummer())
                  ? ""
                  : mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                      .getMitarbeiter()
                      .getIbanNummer());
      case LEHRKRAFT ->
          value =
              (mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.getMitarbeiter().isLehrkraft()
                  ? "ja"
                  : "nein");
      case AKTIV ->
          value =
              (mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.getMitarbeiter().isAktiv())
                  ? "ja"
                  : "nein";
      case CODES ->
          value = mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.getMitarbeiterCodesAsString();
      case VERTRETUNGSMOEGLICHKEITEN ->
          value =
              mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                  .getMitarbeiter()
                  .getVertretungsmoeglichkeitenLineBreaksReplacedBySemicolonOrPeriod();
      case BEMERKUNGEN ->
          value =
              mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
                  .getMitarbeiter()
                  .getBemerkungenLineBreaksReplacedBySemicolonOrPeriod();
      default -> {
        // Nothing to do
      }
    }
    return value;
  }

  public void setValueAt(Object value, int rowIndex, int columnIndex) {
    if (COLUMNS[columnIndex] == Field.SELEKTIERT) {
      MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
          mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert = data.get(rowIndex);
      mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.setSelektiert((boolean) value);
    }
  }

  public boolean isCellEditable(int columnIndex) {
    return COLUMNS[columnIndex] == Field.SELEKTIERT;
  }

  public int getAnzahlSelektiert() {
    return (int)
        data.stream()
            .filter(MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert::isSelektiert)
            .count();
  }

  public void alleMitarbeiterSelektieren() {
    forEachRow(
        mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert -> {
          if (!mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.isSelektiert()) {
            mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.setSelektiert(true);
          }
        });
  }

  public void alleMitarbeiterDeselektieren() {
    forEachRow(
        mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert -> {
          if (mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.isSelektiert()) {
            mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.setSelektiert(false);
          }
        });
  }

  public Iterator<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert>
      getRowIteratorSelektierteMitarbeiter() {
    return data.stream()
        .filter(MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert::isSelektiert)
        .iterator();
  }

  public Class<?> getColumnClass(int columnIndex) {
    return switch (COLUMNS[columnIndex]) {
      case GEBURTSDATUM -> Calendar.class;
      case SELEKTIERT -> Boolean.class;
      default -> String.class;
    };
  }
}
