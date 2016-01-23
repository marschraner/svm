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
        return semesterrechnung.getRechnungsempfaenger().getAdresse().getStrasseHausnummer();
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
    public String getRestbetrag() {
        return (semesterrechnung.getRestbetrag() == null ? "-" :  semesterrechnung.getRestbetrag().toString());
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
        CallDefaultEmailClientCommand callDefaultEmailClientCommand = new CallDefaultEmailClientCommand(semesterrechnung.getRechnungsempfaenger().getEmail());
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
            setRechnungsdatumNachrechnung(asString(semesterrechnungOrigin.getRechnungsdatumNachrechnung()));
            setErmaessigungNachrechnung(semesterrechnungOrigin.getErmaessigungNachrechnung() == null ? null : semesterrechnungOrigin.getErmaessigungNachrechnung().toString());
            setErmaessigungsgrundNachrechnung(semesterrechnungOrigin.getErmaessigungsgrundNachrechnung());
            setZuschlagNachrechnung(semesterrechnungOrigin.getZuschlagNachrechnung() == null ? null : semesterrechnungOrigin.getZuschlagNachrechnung().toString());
            setZuschlagsgrundNachrechnung(semesterrechnungOrigin.getZuschlagsgrundNachrechnung());
            setAnzahlWochenNachrechnung(semesterrechnungOrigin.getAnzahlWochenNachrechnung() == null ? null : semesterrechnungOrigin.getAnzahlWochenNachrechnung().toString());
            setWochenbetragNachrechnung(semesterrechnungOrigin.getWochenbetragNachrechnung() == null ? null : semesterrechnungOrigin.getWochenbetragNachrechnung().toString());
            setBetragZahlung1(semesterrechnungOrigin.getBetragZahlung1() == null ? null : semesterrechnungOrigin.getBetragZahlung1().toString());
            setDatumZahlung1(semesterrechnungOrigin.getDatumZahlung1() == null ? null : asString(semesterrechnungOrigin.getDatumZahlung1()));
            setBetragZahlung2(semesterrechnungOrigin.getBetragZahlung2() == null ? null : semesterrechnungOrigin.getBetragZahlung2().toString());
            setDatumZahlung2(semesterrechnungOrigin.getDatumZahlung2() == null ? null : asString(semesterrechnungOrigin.getDatumZahlung2()));
            setBetragZahlung3(semesterrechnungOrigin.getBetragZahlung3() == null ? null : semesterrechnungOrigin.getBetragZahlung3().toString());
            setDatumZahlung3(semesterrechnungOrigin.getDatumZahlung3() == null ? null : asString(semesterrechnungOrigin.getDatumZahlung3()));
            setBemerkungen(semesterrechnungOrigin.getBemerkungen());
        } catch (SvmValidationException ignore) {
            ignore.printStackTrace();
        }
        setBulkUpdate(false);
    }

    @Override
    public boolean isCompleted() {
        return !(isSetBetragZahlung1() && !isSetDatumZahlung1()) &&
                !(!isSetBetragZahlung1() && isSetDatumZahlung1()) &&
                !(isSetBetragZahlung2() && !isSetDatumZahlung2()) &&
                !(!isSetBetragZahlung2() && isSetDatumZahlung2()) &&
                !(isSetBetragZahlung3() && !isSetDatumZahlung3()) &&
                !(!isSetBetragZahlung3() && isSetDatumZahlung3()) &&
                !(isSetStipendium() && isSetGratiskinder());
    }

    @Override
    void doValidate() throws SvmValidationException {
        if (isSetBetragZahlung1() && !isSetDatumZahlung1()) {
            throw new SvmValidationException(3051, "Datum nicht gesetzt", Field.DATUM_ZAHLUNG_1);
        }
        if (!isSetBetragZahlung1() && isSetDatumZahlung1()) {
            throw new SvmValidationException(3052, "Betrag nicht gesetzt", Field.BETRAG_ZAHLUNG_1);
        }
        if (isSetBetragZahlung2() && !isSetDatumZahlung2()) {
            throw new SvmValidationException(3053, "Datum nicht gesetzt", Field.DATUM_ZAHLUNG_2);
        }
        if (!isSetBetragZahlung2() && isSetDatumZahlung2()) {
            throw new SvmValidationException(3054, "Betrag nicht gesetzt", Field.BETRAG_ZAHLUNG_2);
        }
        if (isSetBetragZahlung3() && !isSetDatumZahlung3()) {
            throw new SvmValidationException(3055, "Datum nicht gesetzt", Field.DATUM_ZAHLUNG_3);
        }
        if (!isSetBetragZahlung3() && isSetDatumZahlung3()) {
            throw new SvmValidationException(3056, "Betrag nicht gesetzt", Field.BETRAG_ZAHLUNG_3);
        }
        if (isSetStipendium() && isSetGratiskinder()) {
            throw new SvmValidationException(3057, "Stipendium und Gratiskinder können nicht gleichzeitig gesetzt sein", Field.STIPENDIUM);
        }
    }

    private boolean isSetBetragZahlung1() {
        return semesterrechnung.getBetragZahlung1() != null;
    }

    private boolean isSetDatumZahlung1() {
        return semesterrechnung.getDatumZahlung1() != null;
    }

    private boolean isSetBetragZahlung2() {
        return semesterrechnung.getBetragZahlung2() != null;
    }

    private boolean isSetDatumZahlung2() {
        return semesterrechnung.getDatumZahlung2() != null;
    }

    private boolean isSetBetragZahlung3() {
        return semesterrechnung.getBetragZahlung3() != null;
    }

    private boolean isSetDatumZahlung3() {
        return semesterrechnung.getDatumZahlung3() != null;
    }

    private boolean isSetGratiskinder() {
        return semesterrechnung.getGratiskinder() != null && semesterrechnung.getGratiskinder();
    }

    private boolean isSetStipendium() {
        return semesterrechnung.getStipendium() != null && semesterrechnung.getStipendium() != Stipendium.KEINES;
    }

}
