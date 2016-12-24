package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.common.utils.StringNumber;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.*;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Martin Schraner
 */
public class KurseTableData {

    private List<Kurs> kurse;
    private List<Field> columns = new ArrayList<>();
    private Calendar today = new GregorianCalendar();

    public KurseTableData(List<Kurs> kurse) {
        this.kurse = kurse;
        columns.add(Field.KURSTYP_BEZEICHNUNG);
        columns.add(Field.ALTERSBEREICH);
        columns.add(Field.STUFE);
        columns.add(Field.TAG);
        columns.add(Field.ZEIT_BEGINN);
        columns.add(Field.ZEIT_ENDE);
        columns.add(Field.ORT);
        columns.add(Field.LEITUNG);
        columns.add(Field.BEMERKUNGEN);
        columns.add(Field.ANZAHL_SCHUELER);
    }

    public int getColumnCount() {
        return columns.size();
    }

    public int size() {
        return kurse.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Kurs kurs = kurse.get(rowIndex);
        Object value = null;
        switch (columns.get(columnIndex)) {
            case SCHULJAHR:
                value = kurs.getSemester().getSchuljahr();
                break;
            case SEMESTERBEZEICHNUNG:
                value = kurs.getSemester().getSemesterbezeichnung();
                break;
            case KURSTYP_BEZEICHNUNG:
                value = kurs.getKurstyp().getBezeichnung();
                break;
            case ALTERSBEREICH:
                // Korrekte Sortierung
                value = new StringNumber(kurs.getAltersbereich());
                break;
            case STUFE:
                // Korrekte Sortierung
                value = new StringNumber(kurs.getStufe());
                break;
            case TAG:
                value = kurs.getWochentag();
                break;
            case ZEIT_BEGINN:
                value = asString(kurs.getZeitBeginn());
                break;
            case ZEIT_ENDE:
                value = asString(kurs.getZeitEnde());
                break;
            case ORT:
                value = kurs.getKursort().getBezeichnung();
                break;
            case LEITUNG:
                StringBuilder leitung = new StringBuilder();
                for (Mitarbeiter mitarbeiter : kurs.getLehrkraefte()) {
                    if (leitung.length() > 0) {
                        leitung.append(" / ");
                    }
                    leitung.append(mitarbeiter.getVorname()).append(" ").append(mitarbeiter.getNachname());
                }
                value = leitung.toString();
                break;
            case BEMERKUNGEN:
                value = kurs.getBemerkungen();
                break;
            case ANZAHL_SCHUELER:
                value = getAnzahlKursanmeldungenOhneVorzeitigeAbmeldungen(kurs.getSemester(), kurs.getKursanmeldungen());
                break;
            default:
                break;
        }
        return value;
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (columns.get(columnIndex)) {
            case TAG:
                return Wochentag.class;
            case ANZAHL_SCHUELER:
                return Integer.class;
            case ALTERSBEREICH:
                return StringNumber.class;
            case STUFE:
                return StringNumber.class;
            default:
                return String.class;
        }
    }

    public void setKurse(List<Kurs> kurse) {
        this.kurse = kurse;
    }

    public String getColumnName(int column) {
        return columns.get(column).toString();
    }

    public List<Kurs> getKurse() {
        return kurse;
    }

    public Kurs getKursSelected(int rowIndex) {
        return kurse.get(rowIndex);
    }

    public int getAnzahlSchueler() {
        int anzahlSchueler = 0;
        for (Kurs kurs : kurse) {
            anzahlSchueler += getAnzahlKursanmeldungenOhneVorzeitigeAbmeldungen(kurs.getSemester(), kurs.getKursanmeldungen());
        }
        return anzahlSchueler;
    }

    public Kurs getKursAt(int rowIndex) {
        return kurse.get(rowIndex);
    }

    private int getAnzahlKursanmeldungenOhneVorzeitigeAbmeldungen(Semester semester, Collection<Kursanmeldung> kursanmeldungen) {
        int anzahl = 0;
        Calendar stichdatumKursabmeldung = semester.getStichdatumVorzeitigeAbmeldung();
        // Kursabmeldungen später als das aktuelle Datum sollen alle angezeigt werden
        // -> Falls der Stichtag für Schülersuche früher ist als das Stichdatum für vorzeitige Abmeldung ersteres verwenden
        if (today.before(stichdatumKursabmeldung)) {
            stichdatumKursabmeldung = today;
        }
        for (Kursanmeldung kursanmeldung : kursanmeldungen) {
            if (kursanmeldung.getAbmeldedatum() == null || kursanmeldung.getAbmeldedatum().after(stichdatumKursabmeldung)) {
                anzahl++;
            }
        }
        return anzahl;
    }

}
