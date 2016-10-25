package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.common.dataTypes.Stipendium;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.*;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;

import java.math.BigDecimal;
import java.util.*;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
final class SemesterrechnungBearbeitenModelImpl extends SemesterrechnungModelImpl implements SemesterrechnungBearbeitenModel {

    private Semesterrechnung semesterrechnungOrigin;
    private Semester previousSemester;

    SemesterrechnungBearbeitenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
        semesterrechnung.setDeleted(false);
    }

    @Override
    public void setSemesterrechnungOrigin(Semesterrechnung semesterrechnungOrigin) {
        this.semesterrechnungOrigin = semesterrechnungOrigin;
        semesterrechnung.setRechnungsempfaenger(semesterrechnungOrigin.getRechnungsempfaenger());
        semesterrechnung.setSemester(semesterrechnungOrigin.getSemester());
        determinePreviousSemester();
    }

    private void determinePreviousSemester() {
        FindPreviousSemesterCommand findPreviousSemesterCommand = new FindPreviousSemesterCommand(semesterrechnungOrigin.getSemester());
        getCommandInvoker().executeCommand(findPreviousSemesterCommand);
        previousSemester = findPreviousSemesterCommand.getPreviousSemester();
    }

    @Override
    public String getRechnungsempfaengerAnrede() {
        return semesterrechnung.getRechnungsempfaenger().getAnrede().toString();
    }

    @Override
    public String getRechnungsempfaengerNachname() {
        return semesterrechnung.getRechnungsempfaenger().getNachname();
    }

    @Override
    public String getRechnungsempfaengerVorname() {
        return semesterrechnung.getRechnungsempfaenger().getVorname();
    }

    @Override
    public String getRechnungsempfaengerStrasseNr() {
        return (checkNotEmpty(semesterrechnung.getRechnungsempfaenger().getAdresse().getStrasseHausnummer()) ? semesterrechnung.getRechnungsempfaenger().getAdresse().getStrasseHausnummer() : "-");
    }

    @Override
    public String getRechnungsempfaengerPlz() {
        return semesterrechnung.getRechnungsempfaenger().getAdresse().getPlz();
    }

    @Override
    public String getRechnungsempfaengerOrt() {
        return semesterrechnung.getRechnungsempfaenger().getAdresse().getOrt();
    }

    @Override
    public String getRechnungsempfaengerFestnetz() {
        return (checkNotEmpty(semesterrechnung.getRechnungsempfaenger().getFestnetz()) ? semesterrechnung.getRechnungsempfaenger().getFestnetz() : "-");
    }

    @Override
    public String getRechnungsempfaengerNatel() {
        return (checkNotEmpty(semesterrechnung.getRechnungsempfaenger().getNatel()) ? semesterrechnung.getRechnungsempfaenger().getNatel() : "-");
    }

    @Override
    public String getRechnungsempfaengerEmail() {
        return (checkNotEmpty(semesterrechnung.getRechnungsempfaenger().getEmail()) ? semesterrechnung.getRechnungsempfaenger().getEmail() : "-");
    }

    @Override
    public String getRechnungsempfaengerSchuelersVorrechnung() {
        return getRechnungsempfaengerSchuelers(Rechnungstyp.VORRECHNUNG);
    }

    @Override
    public String getRechnungsempfaengerKurseVorrechnung() {
        return getRechnungsempfaengerKurse(Rechnungstyp.VORRECHNUNG);
    }

    @Override
    public void setErmaessigungVorrechnung(String ermaessigungVorrechnung) throws SvmValidationException {
        if (!isBulkUpdate() && !checkNotEmpty(ermaessigungVorrechnung)) {
            invalidate();
            throw new SvmRequiredException(Field.ERMAESSIGUNG_VORRECHNUNG);
        }
        super.setErmaessigungVorrechnung(ermaessigungVorrechnung);
    }

    @Override
    public void setZuschlagVorrechnung(String zuschlagVorrechnung) throws SvmValidationException {
        if (!isBulkUpdate() && !checkNotEmpty(zuschlagVorrechnung)) {
            invalidate();
            throw new SvmRequiredException(Field.ZUSCHLAG_VORRECHNUNG);
        }
        super.setZuschlagVorrechnung(zuschlagVorrechnung);
    }

    @Override
    public void setAnzahlWochenVorrechnung(String anzahlWochenVorrechnung) throws SvmValidationException {
        if (!isBulkUpdate() && !checkNotEmpty(anzahlWochenVorrechnung)) {
            invalidate();
            throw new SvmRequiredException(Field.ANZAHL_WOCHEN_VORRECHNUNG);
        }
        super.setAnzahlWochenVorrechnung(anzahlWochenVorrechnung);
    }

    @Override
    public void setWochenbetragVorrechnung(String wochenbetragVorrechnung) throws SvmValidationException {
        if (!isBulkUpdate() && !checkNotEmpty(wochenbetragVorrechnung)) {
            invalidate();
            throw new SvmRequiredException(Field.WOCHENBETRAG_VORRECHNUNG);
        }
        super.setWochenbetragVorrechnung(wochenbetragVorrechnung);
    }

    @Override
    public String getSechsJahresRabattVorrechnung() {
        CheckIfSemesterrechnungContainsSechsJahresRabattCommand checkIfSemesterrechnungContainsSechsJahresRabattCommand = new CheckIfSemesterrechnungContainsSechsJahresRabattCommand(semesterrechnungOrigin, previousSemester, Rechnungstyp.VORRECHNUNG);
        getCommandInvoker().executeCommand(checkIfSemesterrechnungContainsSechsJahresRabattCommand);
        boolean result = checkIfSemesterrechnungContainsSechsJahresRabattCommand.isSemesterrechnungContainsSechsJahresRabatt();
        return (result ? "ja" : "nein");
    }

    @Override
    public String getRechnungsbetragVorrechnung() {
        return (semesterrechnung.getRechnungsbetragVorrechnung() == null ? "-" : semesterrechnung.getRechnungsbetragVorrechnung().toString());
    }

    @Override
    public String getRestbetragVorrechnung() {
        return (semesterrechnung.getRestbetragVorrechnung() == null ? "-" :  semesterrechnung.getRestbetragVorrechnung().toString());
    }

    @Override
    public String getRechnungsempfaengerSchuelersNachrechnung() {
        return getRechnungsempfaengerSchuelers(Rechnungstyp.NACHRECHNUNG);
    }

    @Override
    public String getRechnungsempfaengerKurseNachrechnung() {
        return getRechnungsempfaengerKurse(Rechnungstyp.NACHRECHNUNG);
    }

    @Override
    public void setErmaessigungNachrechnung(String ermaessigungNachrechnung) throws SvmValidationException {
        if (!isBulkUpdate() && !checkNotEmpty(ermaessigungNachrechnung)) {
            invalidate();
            throw new SvmRequiredException(Field.ERMAESSIGUNG_NACHRECHNUNG);
        }
        super.setErmaessigungNachrechnung(ermaessigungNachrechnung);
    }

    @Override
    public void setZuschlagNachrechnung(String zuschlagNachrechnung) throws SvmValidationException {
        if (!isBulkUpdate() && !checkNotEmpty(zuschlagNachrechnung)) {
            invalidate();
            throw new SvmRequiredException(Field.ZUSCHLAG_NACHRECHNUNG);
        }
        super.setZuschlagNachrechnung(zuschlagNachrechnung);
    }

    @Override
    public void setAnzahlWochenNachrechnung(String anzahlWochenNachrechnung) throws SvmValidationException {
        if (!isBulkUpdate() && !checkNotEmpty(anzahlWochenNachrechnung)) {
            invalidate();
            throw new SvmRequiredException(Field.ANZAHL_WOCHEN_NACHRECHNUNG);
        }
        super.setAnzahlWochenNachrechnung(anzahlWochenNachrechnung);
    }

    @Override
    public void setWochenbetragNachrechnung(String wochenbetragNachrechnung) throws SvmValidationException {
        if (!isBulkUpdate() && !checkNotEmpty(wochenbetragNachrechnung)) {
            invalidate();
            throw new SvmRequiredException(Field.WOCHENBETRAG_NACHRECHNUNG);
        }
        super.setWochenbetragNachrechnung(wochenbetragNachrechnung);
    }

    @Override
    public String getSechsJahresRabattNachrechnung() {
        CheckIfSemesterrechnungContainsSechsJahresRabattCommand checkIfSemesterrechnungContainsSechsJahresRabattCommand = new CheckIfSemesterrechnungContainsSechsJahresRabattCommand(semesterrechnungOrigin, previousSemester, Rechnungstyp.NACHRECHNUNG);
        getCommandInvoker().executeCommand(checkIfSemesterrechnungContainsSechsJahresRabattCommand);
        boolean result = checkIfSemesterrechnungContainsSechsJahresRabattCommand.isSemesterrechnungContainsSechsJahresRabatt();
        return (result ? "ja" : "nein");
    }

    @Override
    public String getRechnungsbetragNachrechnung() {
        return (semesterrechnung.getRechnungsbetragNachrechnung() == null ? "-" : semesterrechnung.getRechnungsbetragNachrechnung().toString());
    }

    @Override
    public String getRestbetragNachrechnung() {
        return (semesterrechnung.getRestbetragNachrechnung() == null ? "-" :  semesterrechnung.getRestbetragNachrechnung().toString());
    }

    @Override
    public String getRabattFaktor() {
        if (!isSetGratiskinder() && !isSetStipendium()) {
            return "";
        }
        if (isSetGratiskinder()) {
            return "0";
        } else {
            return Double.toString(semesterrechnung.getStipendium().getFaktor());
        }
    }

    private String getRechnungsempfaengerSchuelers(Rechnungstyp rechnungstyp) {
        List<Schueler> schuelersRechnungsempfaenger = new ArrayList<>(semesterrechnung.getRechnungsempfaenger().getSchuelerRechnungsempfaenger());
        if (schuelersRechnungsempfaenger.isEmpty()) {
            return "-";
        }
        Collections.sort(schuelersRechnungsempfaenger);
        Semester relevantesSemester = (rechnungstyp == Rechnungstyp.VORRECHNUNG ? previousSemester : semesterrechnung.getSemester());
        if (relevantesSemester == null) {
            return "-";
        }
        StringBuilder schuelersSb = new StringBuilder("<html>");
        boolean neuerSchueler = true;
        for (Schueler schueler : schuelersRechnungsempfaenger) {
            for (Kursanmeldung kursanmeldung : schueler.getKursanmeldungenAsList()) {
                Kurs kurs = kursanmeldung.getKurs();
                if (!kurs.getSemester().getSemesterId().equals(relevantesSemester.getSemesterId()) || (rechnungstyp == Rechnungstyp.VORRECHNUNG && kursanmeldung.getAbmeldedatum() != null)) {
                    // Nicht passendes Semester oder abgemeldeter Kurs bei einer Vorrechnung
                    continue;
                }
                if (!neuerSchueler || schuelersSb.length() <= 6) {
                    schuelersSb.append("<p>");
                } else {
                    schuelersSb.append("<p style='margin-top:12'>");
                }
                if (neuerSchueler) {
                    String mehrAlsEineAnmeldungenStern = "";
                    if (schueler.getAnmeldungen().size() > 1) {
                        mehrAlsEineAnmeldungenStern = "*";
                    }
                    schuelersSb.append(schueler.getVorname()).append(" ").append(schueler.getNachname()).append("&nbsp &nbsp (Eintritt: ").append(asString(schueler.getAnmeldungen().get(0).getAnmeldedatum())).append(mehrAlsEineAnmeldungenStern).append("):");
                }
                if (!neuerSchueler) {
                    // Hack damit Leerzeile nicht ignoriert wird (<p></html>: <p> wird ignoriert)
                    schuelersSb.append("<p>");
                }
                neuerSchueler = false;
            }
            neuerSchueler = true;
        }
        schuelersSb.append("</html>");
        if (schuelersSb.length() > 13) {
            return schuelersSb.toString();
        }
        return "-";
    }

    private String getRechnungsempfaengerKurse(Rechnungstyp rechnungstyp) {
        List<Schueler> schuelersRechnungsempfaenger = new ArrayList<>(semesterrechnung.getRechnungsempfaenger().getSchuelerRechnungsempfaenger());
        if (schuelersRechnungsempfaenger.isEmpty()) {
            return "-";
        }
        Collections.sort(schuelersRechnungsempfaenger);
        Semester relevantesSemester = (rechnungstyp == Rechnungstyp.VORRECHNUNG ? previousSemester : semesterrechnung.getSemester());
        if (relevantesSemester == null) {
            return "-";
        }
        StringBuilder kurseSb = new StringBuilder("<html>");
        boolean neuerSchueler = true;
        for (Schueler schueler : schuelersRechnungsempfaenger) {
            for (Kursanmeldung kursanmeldung : schueler.getKursanmeldungenAsList()) {
                Kurs kurs = kursanmeldung.getKurs();
                if (!kurs.getSemester().getSemesterId().equals(relevantesSemester.getSemesterId()) || (rechnungstyp == Rechnungstyp.VORRECHNUNG && kursanmeldung.getAbmeldedatum() != null)) {
                    // Nicht passendes Semester oder abgemeldeter Kurs bei einer Vorrechnung
                    continue;
                }
                if (!neuerSchueler || kurseSb.length() <= 6) {
                    kurseSb.append("<p>");
                } else {
                    kurseSb.append("<p style='margin-top:12'>");
                }
                if (rechnungstyp == Rechnungstyp.VORRECHNUNG) {
                    kurseSb.append(kurs.toString());
                } else {
                    kurseSb.append(kursanmeldung.toString());
                }
                neuerSchueler = false;
            }
            neuerSchueler = true;
        }
        kurseSb.append("</html>");
        if (kurseSb.length() > 13) {
            return kurseSb.toString();
        }
        return "-";
    }

    @Override
    public boolean isVorrechnungEnabled() {
        // Nur enablen, falls
        // ... Rechnungsdatum gesetzt ist (oder Wochenbetrag, Ermässigung oder Zuschlag nicht null ist)
        if (semesterrechnung.getRechnungsdatumVorrechnung() != null
                || (semesterrechnung.getWochenbetragVorrechnung() != null && semesterrechnung.getWochenbetragVorrechnung().compareTo(BigDecimal.ZERO) != 0)
                || (semesterrechnung.getErmaessigungVorrechnung() != null && semesterrechnung.getErmaessigungVorrechnung().compareTo(BigDecimal.ZERO) != 0)
                || (semesterrechnung.getZuschlagVorrechnung() != null && semesterrechnung.getZuschlagVorrechnung().compareTo(BigDecimal.ZERO) != 0)) {
            return true;
        }
        // ... oder es im Vorsemester Kurse gibt (-> korrekter, aber teurerer Check als für Semesterrechnungsliste)
        List<Schueler> schuelersRechnungsempfaenger = new ArrayList<>(semesterrechnung.getRechnungsempfaenger().getSchuelerRechnungsempfaenger());
        if (schuelersRechnungsempfaenger.isEmpty()) {
            return false;
        }
        if (previousSemester == null) {
            return false;
        }
        for (Schueler schueler : schuelersRechnungsempfaenger) {
            for (Kursanmeldung kursanmeldung : schueler.getKursanmeldungenAsList()) {
                if (kursanmeldung.getKurs().getSemester().getSemesterId().equals(previousSemester.getSemesterId()) && kursanmeldung.getAbmeldedatum() == null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public SemesterrechnungCode[] getSelectableSemesterrechnungCodes(SvmModel svmModel) {
        List<SemesterrechnungCode> codesList = svmModel.getSelektierbareSemesterrechnungCodesAll();
        // SemesterrechnungCode keiner auch erlaubt
        if (codesList.isEmpty() || !codesList.get(0).isIdenticalWith(SEMESTERRECHNUNG_CODE_KEINER)) {
            codesList.add(0, SEMESTERRECHNUNG_CODE_KEINER);
        }
        return codesList.toArray(new SemesterrechnungCode[codesList.size()]);
    }

    @Override
    public Stipendium[] getSelectableStipendien() {
        Stipendium[] selectableStipendien = new Stipendium[Stipendium.values().length - 1];
        int j = 0;
        for (int i = 0; i < Stipendium.values().length; i++) {
            if (Stipendium.values()[i] != Stipendium.ALLE) {
                selectableStipendien[j] = Stipendium.values()[i];
                j++;
            }
        }
        return selectableStipendien;
    }

    @Override
    public boolean checkIfRechnungsempfaengerHasEmail() {
        return checkNotEmpty(semesterrechnung.getRechnungsempfaenger().getEmail());
    }

    @Override
    public CallDefaultEmailClientCommand.Result callEmailClient() {
        CommandInvoker commandInvoker = getCommandInvoker();
        CallDefaultEmailClientCommand callDefaultEmailClientCommand = new CallDefaultEmailClientCommand(semesterrechnung.getRechnungsempfaenger().getEmail(), false);
        commandInvoker.executeCommand(callDefaultEmailClientCommand);
        return callDefaultEmailClientCommand.getResult();
    }

    @Override
    public void calculateWochenbetrag(Rechnungstyp rechnungstyp) {
        FindAllLektionsgebuehrenCommand findAllLektionsgebuehrenCommand = new FindAllLektionsgebuehrenCommand();
        getCommandInvoker().executeCommand(findAllLektionsgebuehrenCommand);
        Map<Integer, BigDecimal[]> lektionsgebuehrenMap = findAllLektionsgebuehrenCommand.getLektionsgebuehrenAllMap();
        Semester relevantesSemester = (rechnungstyp == Rechnungstyp.VORRECHNUNG ? previousSemester : semesterrechnung.getSemester());
        BigDecimal wochenbetrag;
        if (relevantesSemester == null) {
            wochenbetrag = BigDecimal.ZERO;
        } else {
            CalculateWochenbetragCommand calculateWochenbetragCommand = new CalculateWochenbetragCommand(semesterrechnung, relevantesSemester, rechnungstyp, lektionsgebuehrenMap);
            getCommandInvoker().executeCommand(calculateWochenbetragCommand);
            wochenbetrag = calculateWochenbetragCommand.getWochenbetrag();
            if (wochenbetrag == null) {
                wochenbetrag = BigDecimal.ZERO;
            }
        }
        try {
            switch (rechnungstyp) {
                case VORRECHNUNG:
                    setWochenbetragVorrechnung(wochenbetrag == null ? null : wochenbetrag.toString());
                    break;
                case NACHRECHNUNG:
                    setWochenbetragNachrechnung(wochenbetrag == null ? null : wochenbetrag.toString());
                    break;
            }
        } catch (SvmValidationException ignore) {
        }
    }

    @Override
    public void speichern(SemesterrechnungenTableModel semesterrechnungenTableModel) {
        SaveOrUpdateSemesterrechnungCommand saveOrUpdateSemesterrechnungCommand = new SaveOrUpdateSemesterrechnungCommand(semesterrechnung, semesterrechnungCode, semesterrechnungOrigin, semesterrechnungenTableModel.getSemesterrechnungen());
        getCommandInvoker().executeCommandAsTransaction(saveOrUpdateSemesterrechnungCommand);
    }

    @Override
    public void initializeCompleted() {
        setBulkUpdate(true);
        try {
            setSemesterrechnungCode(semesterrechnungOrigin.getSemesterrechnungCode());
            setStipendium(semesterrechnungOrigin.getStipendium());
            setGratiskinder(semesterrechnungOrigin.getGratiskinder());
            setRechnungsdatumVorrechnung(asString(semesterrechnungOrigin.getRechnungsdatumVorrechnung()));
            setErmaessigungVorrechnung(semesterrechnungOrigin.getErmaessigungVorrechnung() == null ? null : semesterrechnungOrigin.getErmaessigungVorrechnung().toString());
            setErmaessigungsgrundVorrechnung(semesterrechnungOrigin.getErmaessigungsgrundVorrechnung());
            setZuschlagVorrechnung(semesterrechnungOrigin.getZuschlagVorrechnung() == null ? null : semesterrechnungOrigin.getZuschlagVorrechnung().toString());
            setZuschlagsgrundVorrechnung(semesterrechnungOrigin.getZuschlagsgrundVorrechnung());
            setAnzahlWochenVorrechnung(semesterrechnungOrigin.getAnzahlWochenVorrechnung() == null ? null : semesterrechnungOrigin.getAnzahlWochenVorrechnung().toString());
            setWochenbetragVorrechnung(semesterrechnungOrigin.getWochenbetragVorrechnung() == null ? null : semesterrechnungOrigin.getWochenbetragVorrechnung().toString());
            setBetragZahlung1Vorrechnung(semesterrechnungOrigin.getBetragZahlung1Vorrechnung() == null ? null : semesterrechnungOrigin.getBetragZahlung1Vorrechnung().toString());
            setDatumZahlung1Vorrechnung(semesterrechnungOrigin.getDatumZahlung1Vorrechnung() == null ? null : asString(semesterrechnungOrigin.getDatumZahlung1Vorrechnung()));
            setBetragZahlung2Vorrechnung(semesterrechnungOrigin.getBetragZahlung2Vorrechnung() == null ? null : semesterrechnungOrigin.getBetragZahlung2Vorrechnung().toString());
            setDatumZahlung2Vorrechnung(semesterrechnungOrigin.getDatumZahlung2Vorrechnung() == null ? null : asString(semesterrechnungOrigin.getDatumZahlung2Vorrechnung()));
            setBetragZahlung3Vorrechnung(semesterrechnungOrigin.getBetragZahlung3Vorrechnung() == null ? null : semesterrechnungOrigin.getBetragZahlung3Vorrechnung().toString());
            setDatumZahlung3Vorrechnung(semesterrechnungOrigin.getDatumZahlung3Vorrechnung() == null ? null : asString(semesterrechnungOrigin.getDatumZahlung3Vorrechnung()));
            setRechnungsdatumNachrechnung(asString(semesterrechnungOrigin.getRechnungsdatumNachrechnung()), false);
            setErmaessigungNachrechnung(semesterrechnungOrigin.getErmaessigungNachrechnung() == null ? null : semesterrechnungOrigin.getErmaessigungNachrechnung().toString());
            setErmaessigungsgrundNachrechnung(semesterrechnungOrigin.getErmaessigungsgrundNachrechnung());
            setZuschlagNachrechnung(semesterrechnungOrigin.getZuschlagNachrechnung() == null ? null : semesterrechnungOrigin.getZuschlagNachrechnung().toString());
            setZuschlagsgrundNachrechnung(semesterrechnungOrigin.getZuschlagsgrundNachrechnung());
            setAnzahlWochenNachrechnung(semesterrechnungOrigin.getAnzahlWochenNachrechnung() == null ? null : semesterrechnungOrigin.getAnzahlWochenNachrechnung().toString());
            setWochenbetragNachrechnung(semesterrechnungOrigin.getWochenbetragNachrechnung() == null ? null : semesterrechnungOrigin.getWochenbetragNachrechnung().toString());
            setBetragZahlung1Nachrechnung(semesterrechnungOrigin.getBetragZahlung1Nachrechnung() == null ? null : semesterrechnungOrigin.getBetragZahlung1Nachrechnung().toString());
            setDatumZahlung1Nachrechnung(semesterrechnungOrigin.getDatumZahlung1Nachrechnung() == null ? null : asString(semesterrechnungOrigin.getDatumZahlung1Nachrechnung()));
            setBetragZahlung2Nachrechnung(semesterrechnungOrigin.getBetragZahlung2Nachrechnung() == null ? null : semesterrechnungOrigin.getBetragZahlung2Nachrechnung().toString());
            setDatumZahlung2Nachrechnung(semesterrechnungOrigin.getDatumZahlung2Nachrechnung() == null ? null : asString(semesterrechnungOrigin.getDatumZahlung2Nachrechnung()));
            setBetragZahlung3Nachrechnung(semesterrechnungOrigin.getBetragZahlung3Nachrechnung() == null ? null : semesterrechnungOrigin.getBetragZahlung3Nachrechnung().toString());
            setDatumZahlung3Nachrechnung(semesterrechnungOrigin.getDatumZahlung3Nachrechnung() == null ? null : asString(semesterrechnungOrigin.getDatumZahlung3Nachrechnung()));
            setBemerkungen(semesterrechnungOrigin.getBemerkungen());
        } catch (SvmValidationException ignore) {
            ignore.printStackTrace();
        }
        setBulkUpdate(false);
    }

    @Override
    public boolean isCompleted() {
        return !(isSetBetragZahlung1Vorrechnung() && !isSetDatumZahlung1Vorrechnung()) &&
                !(!isSetBetragZahlung1Vorrechnung() && isSetDatumZahlung1Vorrechnung()) &&
                !(isSetBetragZahlung2Vorrechnung() && !isSetDatumZahlung2Vorrechnung()) &&
                !(!isSetBetragZahlung2Vorrechnung() && isSetDatumZahlung2Vorrechnung()) &&
                !(isSetBetragZahlung3Vorrechnung() && !isSetDatumZahlung3Vorrechnung()) &&
                !(!isSetBetragZahlung3Vorrechnung() && isSetDatumZahlung3Vorrechnung()) &&
                !(isSetBetragZahlung1Nachrechnung() && !isSetDatumZahlung1Nachrechnung()) &&
                !(!isSetBetragZahlung1Nachrechnung() && isSetDatumZahlung1Nachrechnung()) &&
                !(isSetBetragZahlung2Nachrechnung() && !isSetDatumZahlung2Nachrechnung()) &&
                !(!isSetBetragZahlung2Nachrechnung() && isSetDatumZahlung2Nachrechnung()) &&
                !(isSetBetragZahlung3Nachrechnung() && !isSetDatumZahlung3Nachrechnung()) &&
                !(!isSetBetragZahlung3Nachrechnung() && isSetDatumZahlung3Nachrechnung()) &&
                !(isSetStipendium() && isSetGratiskinder());
    }

    @Override
    void doValidate() throws SvmValidationException {
        if (isSetBetragZahlung1Vorrechnung() && !isSetDatumZahlung1Vorrechnung()) {
            throw new SvmValidationException(3051, "Datum nicht gesetzt", Field.DATUM_ZAHLUNG_1_VORRECHNUNG);
        }
        if (!isSetBetragZahlung1Vorrechnung() && isSetDatumZahlung1Vorrechnung()) {
            throw new SvmValidationException(3052, "Betrag nicht gesetzt", Field.BETRAG_ZAHLUNG_1_VORRECHNUNG);
        }
        if (isSetBetragZahlung2Vorrechnung() && !isSetDatumZahlung2Vorrechnung()) {
            throw new SvmValidationException(3053, "Datum nicht gesetzt", Field.DATUM_ZAHLUNG_2_VORRECHNUNG);
        }
        if (!isSetBetragZahlung2Vorrechnung() && isSetDatumZahlung2Vorrechnung()) {
            throw new SvmValidationException(3054, "Betrag nicht gesetzt", Field.BETRAG_ZAHLUNG_2_VORRECHNUNG);
        }
        if (isSetBetragZahlung3Vorrechnung() && !isSetDatumZahlung3Vorrechnung()) {
            throw new SvmValidationException(3055, "Datum nicht gesetzt", Field.DATUM_ZAHLUNG_3_VORRECHNUNG);
        }
        if (!isSetBetragZahlung3Vorrechnung() && isSetDatumZahlung3Vorrechnung()) {
            throw new SvmValidationException(3056, "Betrag nicht gesetzt", Field.BETRAG_ZAHLUNG_3_VORRECHNUNG);
        }
        if (isSetBetragZahlung1Nachrechnung() && !isSetDatumZahlung1Nachrechnung()) {
            throw new SvmValidationException(3057, "Datum nicht gesetzt", Field.DATUM_ZAHLUNG_1_NACHRECHNUNG);
        }
        if (!isSetBetragZahlung1Nachrechnung() && isSetDatumZahlung1Nachrechnung()) {
            throw new SvmValidationException(3058, "Betrag nicht gesetzt", Field.BETRAG_ZAHLUNG_1_NACHRECHNUNG);
        }
        if (isSetBetragZahlung2Nachrechnung() && !isSetDatumZahlung2Nachrechnung()) {
            throw new SvmValidationException(3059, "Datum nicht gesetzt", Field.DATUM_ZAHLUNG_2_NACHRECHNUNG);
        }
        if (!isSetBetragZahlung2Nachrechnung() && isSetDatumZahlung2Nachrechnung()) {
            throw new SvmValidationException(3060, "Betrag nicht gesetzt", Field.BETRAG_ZAHLUNG_2_NACHRECHNUNG);
        }
        if (isSetBetragZahlung3Nachrechnung() && !isSetDatumZahlung3Nachrechnung()) {
            throw new SvmValidationException(3061, "Datum nicht gesetzt", Field.DATUM_ZAHLUNG_3_NACHRECHNUNG);
        }
        if (!isSetBetragZahlung3Nachrechnung() && isSetDatumZahlung3Nachrechnung()) {
            throw new SvmValidationException(3062, "Betrag nicht gesetzt", Field.BETRAG_ZAHLUNG_3_NACHRECHNUNG);
        }
        if (isSetStipendium() && isSetGratiskinder()) {
            throw new SvmValidationException(3063, "Stipendium und Gratiskinder können nicht gleichzeitig gesetzt sein", Field.STIPENDIUM);
        }
    }

    private boolean isSetBetragZahlung1Vorrechnung() {
        return semesterrechnung.getBetragZahlung1Vorrechnung() != null;
    }

    private boolean isSetDatumZahlung1Vorrechnung() {
        return semesterrechnung.getDatumZahlung1Vorrechnung() != null;
    }

    private boolean isSetBetragZahlung2Vorrechnung() {
        return semesterrechnung.getBetragZahlung2Vorrechnung() != null;
    }

    private boolean isSetDatumZahlung2Vorrechnung() {
        return semesterrechnung.getDatumZahlung2Vorrechnung() != null;
    }

    private boolean isSetBetragZahlung3Vorrechnung() {
        return semesterrechnung.getBetragZahlung3Vorrechnung() != null;
    }

    private boolean isSetDatumZahlung3Vorrechnung() {
        return semesterrechnung.getDatumZahlung3Vorrechnung() != null;
    }

    private boolean isSetBetragZahlung1Nachrechnung() {
        return semesterrechnung.getBetragZahlung1Nachrechnung() != null;
    }

    private boolean isSetDatumZahlung1Nachrechnung() {
        return semesterrechnung.getDatumZahlung1Nachrechnung() != null;
    }

    private boolean isSetBetragZahlung2Nachrechnung() {
        return semesterrechnung.getBetragZahlung2Nachrechnung() != null;
    }

    private boolean isSetDatumZahlung2Nachrechnung() {
        return semesterrechnung.getDatumZahlung2Nachrechnung() != null;
    }

    private boolean isSetBetragZahlung3Nachrechnung() {
        return semesterrechnung.getBetragZahlung3Nachrechnung() != null;
    }

    private boolean isSetDatumZahlung3Nachrechnung() {
        return semesterrechnung.getDatumZahlung3Nachrechnung() != null;
    }

    private boolean isSetGratiskinder() {
        return semesterrechnung.getGratiskinder() != null && semesterrechnung.getGratiskinder();
    }

    private boolean isSetStipendium() {
        return semesterrechnung.getStipendium() != null && semesterrechnung.getStipendium() != Stipendium.KEINES;
    }

}
