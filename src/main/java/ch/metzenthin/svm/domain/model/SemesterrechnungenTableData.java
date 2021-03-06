package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.utils.SvmListUtils;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungId;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * @author Martin Schraner
 */
public class SemesterrechnungenTableData {

    private List<Semesterrechnung> semesterrechnungen;
    private Semester semester;
    private Semester previousSemester;
    private boolean nachGeloeschtenGesucht;
    private List<Field> columns = new ArrayList<>();

    SemesterrechnungenTableData(List<Semesterrechnung> semesterrechnungen, Semester semester, Semester previousSemester, boolean nachGeloeschtenGesucht) {
        this.semesterrechnungen = semesterrechnungen;
        this.semester = semester;
        this.previousSemester = previousSemester;
        this.nachGeloeschtenGesucht = nachGeloeschtenGesucht;
        initColumns();
    }

    private void initColumns() {
        if (!nachGeloeschtenGesucht) {
            columns.add(Field.SELEKTIERT);
        }
        columns.add(Field.RECHNUNGSEMPFAENGER);
        columns.add(Field.SCHUELER);
        columns.add(Field.RECHNUNGSDATUM_VORRECHNUNG);
        columns.add(Field.ANZAHL_WOCHEN_VORRECHNUNG);
        columns.add(Field.WOCHENBETRAG_VORRECHNUNG);
        columns.add(Field.SCHULGELD_VORRECHNUNG);
        columns.add(Field.ERMAESSIGUNG_VORRECHNUNG);
        columns.add(Field.ZUSCHLAG_VORRECHNUNG);
        columns.add(Field.ERMAESSIGUNG_STIPENDIUM_VORRECHNUNG);
        columns.add(Field.RECHNUNGSBETRAG_VORRECHNUNG);
        columns.add(Field.RESTBETRAG_VORRECHNUNG);
        columns.add(Field.RECHNUNGSDATUM_NACHRECHNUNG);
        columns.add(Field.ANZAHL_WOCHEN_NACHRECHNUNG);
        columns.add(Field.WOCHENBETRAG_NACHRECHNUNG);
        columns.add(Field.SCHULGELD_NACHRECHNUNG);
        columns.add(Field.ERMAESSIGUNG_NACHRECHNUNG);
        columns.add(Field.ZUSCHLAG_NACHRECHNUNG);
        columns.add(Field.ERMAESSIGUNG_STIPENDIUM_NACHRECHNUNG);
        columns.add(Field.RECHNUNGSBETRAG_NACHRECHNUNG);
        columns.add(Field.RESTBETRAG_NACHRECHNUNG);
        columns.add(Field.DIFFERENZ_SCHULGELD);
    }

    public int getColumnCount() {
        return columns.size();
    }

    public int size() {
        return semesterrechnungen.size();
    }

    public int getAnzSelektiert() {
        int anzSelektiert = 0;
        for (Semesterrechnung semesterrechnung : semesterrechnungen) {
            if (semesterrechnung.isSelektiert()) {
                anzSelektiert++;
            }
        }
        return anzSelektiert;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Semesterrechnung semesterrechnung = semesterrechnungen.get(rowIndex);
        Object value = null;
        boolean showVorrechnung = semesterrechnung.getRechnungsdatumVorrechnung() != null
                || (semesterrechnung.getWochenbetragVorrechnung() != null && semesterrechnung.getWochenbetragVorrechnung().compareTo(BigDecimal.ZERO) != 0)
                || (semesterrechnung.getErmaessigungVorrechnung() != null && semesterrechnung.getErmaessigungVorrechnung().compareTo(BigDecimal.ZERO) != 0)
                || (semesterrechnung.getZuschlagVorrechnung() != null && semesterrechnung.getZuschlagVorrechnung().compareTo(BigDecimal.ZERO) != 0);
        switch (columns.get(columnIndex)) {
            case SELEKTIERT:
                value = semesterrechnung.isSelektiert();
                break;
            case RECHNUNGSEMPFAENGER:
                value = semesterrechnung.getRechnungsempfaenger().getNachname() + " " + semesterrechnung.getRechnungsempfaenger().getVorname();
                break;
            case SCHUELER:
                value = semesterrechnung.getAktiveSchuelerRechnungsempfaengerAsStr(previousSemester);
                break;
            case RECHNUNGSDATUM_VORRECHNUNG:
                value = semesterrechnung.getRechnungsdatumVorrechnung();
                break;
            case ANZAHL_WOCHEN_VORRECHNUNG:
                if (showVorrechnung) {
                    value = semesterrechnung.getAnzahlWochenVorrechnung();
                }
                break;
            case WOCHENBETRAG_VORRECHNUNG:
                if (showVorrechnung) {
                    value = semesterrechnung.getWochenbetragVorrechnung();
                }
                break;
            case SCHULGELD_VORRECHNUNG:
                if (showVorrechnung) {
                    value = semesterrechnung.getSchulgeldVorrechnung();
                }
                break;
            case ERMAESSIGUNG_VORRECHNUNG:
                if (showVorrechnung && semesterrechnung.getErmaessigungVorrechnung() != null && semesterrechnung.getErmaessigungVorrechnung().compareTo(BigDecimal.ZERO) != 0) {
                    value = semesterrechnung.getErmaessigungVorrechnung();
                }
                break;
            case ZUSCHLAG_VORRECHNUNG:
                if (showVorrechnung && semesterrechnung.getZuschlagVorrechnung() != null && semesterrechnung.getZuschlagVorrechnung().compareTo(BigDecimal.ZERO) != 0) {
                    value = semesterrechnung.getZuschlagVorrechnung();
                }
                break;
            case ERMAESSIGUNG_STIPENDIUM_VORRECHNUNG:
                BigDecimal ermaessigungStipendiumVorrechnung = semesterrechnung.getErmaessigungStipendiumVorrechnung();
                if (showVorrechnung && ermaessigungStipendiumVorrechnung != null && ermaessigungStipendiumVorrechnung.compareTo(BigDecimal.ZERO) != 0) {
                    value = ermaessigungStipendiumVorrechnung.toString();
                }
                break;
            case RECHNUNGSBETRAG_VORRECHNUNG:
                if (showVorrechnung) {
                    value = semesterrechnung.getRechnungsbetragVorrechnung();
                }
                break;
            case RESTBETRAG_VORRECHNUNG:
                if (showVorrechnung) {
                    value = semesterrechnung.getRestbetragVorrechnung();
                }
                break;
            case RECHNUNGSDATUM_NACHRECHNUNG:
                value = semesterrechnung.getRechnungsdatumNachrechnung();
                break;
            case ANZAHL_WOCHEN_NACHRECHNUNG:
                value = semesterrechnung.getAnzahlWochenNachrechnung();
                break;
            case WOCHENBETRAG_NACHRECHNUNG:
                value = semesterrechnung.getWochenbetragNachrechnung();
                break;
            case SCHULGELD_NACHRECHNUNG:
                value = semesterrechnung.getSchulgeldNachrechnung();
                break;
            case ERMAESSIGUNG_NACHRECHNUNG:
                if (semesterrechnung.getErmaessigungNachrechnung() != null && semesterrechnung.getErmaessigungNachrechnung().compareTo(BigDecimal.ZERO) != 0) {
                    value = semesterrechnung.getErmaessigungNachrechnung();
                }
                break;
            case ZUSCHLAG_NACHRECHNUNG:
                if (semesterrechnung.getZuschlagNachrechnung() != null && semesterrechnung.getZuschlagNachrechnung().compareTo(BigDecimal.ZERO) != 0) {
                    value = semesterrechnung.getZuschlagNachrechnung();
                }
                break;
            case ERMAESSIGUNG_STIPENDIUM_NACHRECHNUNG:
                BigDecimal ermaessigungStipendiumNachrechnung = semesterrechnung.getErmaessigungStipendiumNachrechnung();
                if (ermaessigungStipendiumNachrechnung != null && ermaessigungStipendiumNachrechnung.compareTo(BigDecimal.ZERO) != 0) {
                    value = ermaessigungStipendiumNachrechnung.toString();
                }
                break;
            case RECHNUNGSBETRAG_NACHRECHNUNG:
                value = semesterrechnung.getRechnungsbetragNachrechnung();
                break;
            case RESTBETRAG_NACHRECHNUNG:
                value = semesterrechnung.getRestbetragNachrechnung();
                break;
            case DIFFERENZ_SCHULGELD:
                value = semesterrechnung.getDifferenzSchulgeld();
                break;
            default:
                break;
        }
        return value;
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (columns.get(columnIndex) == Field.SELEKTIERT) {
            Semesterrechnung semesterrechnung = semesterrechnungen.get(rowIndex);
            semesterrechnung.setSelektiert((boolean) value);
            semesterrechnungen.set(rowIndex, semesterrechnung);
        }
    }

    public boolean isCellEditable(int columnIndex) {
        return columns.get(columnIndex) == Field.SELEKTIERT;
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (columns.get(columnIndex)) {
            case SELEKTIERT:
                return Boolean.class;
            case RECHNUNGSDATUM_VORRECHNUNG:
                return Calendar.class;
            case ANZAHL_WOCHEN_VORRECHNUNG:
                return Integer.class;
            case WOCHENBETRAG_VORRECHNUNG:
                return BigDecimal.class;
            case ERMAESSIGUNG_VORRECHNUNG:
                return BigDecimal.class;
            case ZUSCHLAG_VORRECHNUNG:
                return BigDecimal.class;
            case ERMAESSIGUNG_STIPENDIUM_VORRECHNUNG:
                return BigDecimal.class;
            case RECHNUNGSBETRAG_VORRECHNUNG:
                return BigDecimal.class;
            case RESTBETRAG_VORRECHNUNG:
                return BigDecimal.class;
            case RECHNUNGSDATUM_NACHRECHNUNG:
                return Calendar.class;
            case ANZAHL_WOCHEN_NACHRECHNUNG:
                return Integer.class;
            case WOCHENBETRAG_NACHRECHNUNG:
                return BigDecimal.class;
            case ERMAESSIGUNG_NACHRECHNUNG:
                return BigDecimal.class;
            case ZUSCHLAG_NACHRECHNUNG:
                return BigDecimal.class;
            case ERMAESSIGUNG_STIPENDIUM_NACHRECHNUNG:
                return BigDecimal.class;
            case RECHNUNGSBETRAG_NACHRECHNUNG:
                return BigDecimal.class;
            case RESTBETRAG_NACHRECHNUNG:
                return BigDecimal.class;
            case DIFFERENZ_SCHULGELD:
                return BigDecimal.class;
            default:
                return String.class;
        }
    }

    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).toString();
    }

    public Semester getSememester() {
        return semester;
    }

    public Semesterrechnung getSemesterrechnungSelected(int rowIndex) {
        return semesterrechnungen.get(rowIndex);
    }

    public List<Semesterrechnung> getSemesterrechnungen() {
        return semesterrechnungen;
    }

    public void setSemesterrechnungen(List<Semesterrechnung> semesterrechnungen) {
        this.semesterrechnungen = semesterrechnungen;
    }

    public List<Semesterrechnung> getSelektierteSemesterrechnungen() {
        List<Semesterrechnung> selektierteSemesterrechnungen = new ArrayList<>();
        for (Semesterrechnung semesterrechnung : semesterrechnungen) {
            if (semesterrechnung.isSelektiert()) {
                selektierteSemesterrechnungen.add(semesterrechnung);
            }
        }
        return selektierteSemesterrechnungen;
    }

    public boolean isAlleSelektiert() {
        for (Semesterrechnung semesterrechnung : semesterrechnungen) {
            if (!semesterrechnung.isSelektiert()) {
                return false;
            }
        }
        return true;
    }

    public void alleSemesterrechnungenSelektieren() {
        for (Semesterrechnung semesterrechnung : semesterrechnungen) {
            semesterrechnung.setSelektiert(true);
        }
    }

    public void alleSemesterrechnungenDeselektieren() {
        for (Semesterrechnung semesterrechnung : semesterrechnungen) {
            semesterrechnung.setSelektiert(false);
        }
    }

    public void updateSemesterrechnungen(Set<Semesterrechnung> subsetOfUpdatedSemesterrechnungen) {
        SvmListUtils.updateList(semesterrechnungen, subsetOfUpdatedSemesterrechnungen);
    }

    public void loadSelektierteSemesterrechnungenNotContainedInPersistenceContext() {
        EntityManager entityManager = DBFactory.getInstance().getCurrentEntityManager();
        for (int i = 0; i < semesterrechnungen.size(); i++) {
            if (semesterrechnungen.get(i).isSelektiert()
                    && !entityManager.contains(semesterrechnungen.get(i))) {
                semesterrechnungen.set(i, entityManager.find(Semesterrechnung.class, new SemesterrechnungId(
                        semesterrechnungen.get(i).getSemester().getSemesterId(),
                        semesterrechnungen.get(i).getRechnungsempfaenger().getPersonId())));
            }
        }
    }

    public void reloadSemesterrechnungenNotContainedInPersistenceContext() {
        EntityManager entityManager = DBFactory.getInstance().getCurrentEntityManager();
        for (int i = 0; i < semesterrechnungen.size(); i++) {
            if (!entityManager.contains(semesterrechnungen.get(i))) {
                semesterrechnungen.set(i, entityManager.find(Semesterrechnung.class, new SemesterrechnungId(
                        semesterrechnungen.get(i).getSemester().getSemesterId(),
                        semesterrechnungen.get(i).getRechnungsempfaenger().getPersonId())));
                // Notwendig, andernfalls werden die Daten nicht von der DB geladen und sind veraltet
                entityManager.refresh(semesterrechnungen.get(i));
            }
        }
    }
}
