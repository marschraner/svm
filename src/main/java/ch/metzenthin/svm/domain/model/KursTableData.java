package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.common.utils.Converter.asString;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.common.utils.StringNumber;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class KursTableData extends AbstractTableData<KursAndLehrkraefteAndNumberOfKursanmeldungen> {

  private static final Field[] COLUMNS = {
    Field.KURSTYP_BEZEICHNUNG,
    Field.ALTERSBEREICH,
    Field.STUFE,
    Field.TAG,
    Field.ZEIT_BEGINN,
    Field.ZEIT_ENDE,
    Field.ORT,
    Field.LEITUNG,
    Field.BEMERKUNGEN,
    Field.ANZAHL_SCHUELER
  };

  public KursTableData(
      List<KursAndLehrkraefteAndNumberOfKursanmeldungen>
          kursAndLehrkraefteAndNumberOfKursanmeldungenList) {
    super(COLUMNS, kursAndLehrkraefteAndNumberOfKursanmeldungenList);
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    KursAndLehrkraefteAndNumberOfKursanmeldungen kursAndLehrkraefteAndNumberOfKursAnmeldungen =
        data.get(rowIndex);
    Kurs kurs = kursAndLehrkraefteAndNumberOfKursAnmeldungen.kurs();
    Object value = null;
    switch (COLUMNS[columnIndex]) {
      case KURSTYP_BEZEICHNUNG -> value = kurs.getKurstyp().getBezeichnung();
      case ALTERSBEREICH ->
          // Korrekte Sortierung
          value = new StringNumber(kurs.getAltersbereich());
      case STUFE ->
          // Korrekte Sortierung
          value = new StringNumber(kurs.getStufe());
      case TAG -> value = kurs.getWochentag();
      case ZEIT_BEGINN -> value = asString(kurs.getZeitBeginn());
      case ZEIT_ENDE -> value = asString(kurs.getZeitEnde());
      case ORT -> value = kurs.getKursort().getBezeichnung();
      case LEITUNG -> {
        StringBuilder leitung = new StringBuilder();
        for (Mitarbeiter mitarbeiter : kursAndLehrkraefteAndNumberOfKursAnmeldungen.lehrkraefte()) {
          if (!leitung.isEmpty()) {
            leitung.append(" / ");
          }
          leitung.append(mitarbeiter.getVorname()).append(" ").append(mitarbeiter.getNachname());
        }
        value = leitung.toString();
      }
      case BEMERKUNGEN -> value = kurs.getBemerkungen();
      case ANZAHL_SCHUELER ->
          value = kursAndLehrkraefteAndNumberOfKursAnmeldungen.numberOfKursanmeldungen();
      default -> {
        // Nothing to do
      }
    }
    return value;
  }

  public Class<?> getColumnClass(int columnIndex) {
    return switch (COLUMNS[columnIndex]) {
      case TAG -> Wochentag.class;
      case ANZAHL_SCHUELER -> Integer.class;
      case ALTERSBEREICH, STUFE -> StringNumber.class;
      default -> String.class;
    };
  }
}
