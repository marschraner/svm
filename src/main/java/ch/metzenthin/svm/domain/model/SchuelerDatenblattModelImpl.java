package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Geschlecht;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.common.utils.Converter;
import ch.metzenthin.svm.common.utils.SvmProperties;
import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.*;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;

import java.sql.Time;
import java.util.*;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Hans Stamm
 */
public class SchuelerDatenblattModelImpl implements SchuelerDatenblattModel {

    private final Schueler schueler;
    private final Maercheneinteilung aktuelleMaercheneinteilung;
    private final boolean neusteZuoberst;

    public SchuelerDatenblattModelImpl(Schueler schueler) {
        this.schueler = schueler;
        this.aktuelleMaercheneinteilung = determineAktuelleMaercheneinteilung(
                schueler.getSortedMaercheneinteilungen());
        Properties svmProperties = SvmProperties.getSvmProperties();
        neusteZuoberst = !svmProperties.getProperty(SvmProperties.KEY_NEUSTE_ZUOBERST).equals("false");
    }

    @Override
    public String getSchuelerNachname() {
        return schueler.getNachname();
    }

    @Override
    public String getSchuelerVorname() {
        return schueler.getVorname();
    }

    @Override
    public String getLabelSchueler() {
        return (schueler.getGeschlecht() == Geschlecht.W ? "Schülerin:" : "Schüler:");
    }

    @Override
    public String getSchuelerAsString() {
        return schueler.toString();
    }

    @Override
    public String getMutterAsString() {
        Angehoeriger mutter = schueler.getMutter();
        if (mutter != null) {
            if (checkNotEmpty(mutter.getEmail())) {
                // Falls Mutter keine E-Mails wünscht (d.h. Abweichung von Default-Einstellung) dies
                // speziell vermerken
                return mutter.toStringIncludingWuenschtKeineEmailsIfWuenschtEmailsFalse();
            } else {
                // Mutter ohne E-Mail
                return mutter.toString();
            }
        }
        return "-";
    }

    @Override
    public String getVaterAsString() {
        Angehoeriger vater = schueler.getVater();
        if (vater != null) {
            if (checkNotEmpty(vater.getEmail())) {
                // Falls Vater E-Mails wünscht (d.h. Abweichung von Default-Einstellung) dies
                // speziell vermerken
                // Ausnahme: Keine Mutter oder Mutter ohne E-Mail
                // In diesem Fall vermerken, falls der Vater KEINE E-Mails wünscht
                if (schueler.getMutter() == null || !checkNotEmpty(schueler.getMutter().getEmail())) {
                    return vater.toStringIncludingWuenschtKeineEmailsIfWuenschtEmailsFalse();
                } else {
                    return vater.toStringIncludingWuenschtEmailsIfWuenschtEmailsTrue();
                }
            } else {
                // Vater ohne E-Mail
                return vater.toString();
            }
        }
        return "-";
    }

    @Override
    public String getLabelRechnungsempfaenger() {
        return (schueler.getRechnungsempfaenger().getAnrede() == Anrede.FRAU ? "Rechnungsempfängerin:" : "Rechnungsempfänger:");
    }

    @Override
    public String getRechnungsempfaengerAsString() {
        Angehoeriger rechnungsempfaenger = schueler.getRechnungsempfaenger();
        if (rechnungsempfaenger != null) {
            if (isRechnungsempfaengerMutter()) {
                return "Mutter";
            }
            if (isRechnungsempfaengerVater()) {
                return "Vater";
            }
            return rechnungsempfaenger.toString();
        }
        return "-";
    }

    private boolean isRechnungsempfaengerMutter() {
        return isRechnungsempfaenger(schueler.getMutter());
    }

    private boolean isRechnungsempfaengerVater() {
        return isRechnungsempfaenger(schueler.getVater());
    }

    private boolean isRechnungsempfaengerDrittperson() {
        return !isRechnungsempfaengerMutter() && !isRechnungsempfaengerVater();
    }

    private boolean isRechnungsempfaenger(Angehoeriger angehoeriger) {
        Angehoeriger rechnungsempfaenger = schueler.getRechnungsempfaenger();
        if (rechnungsempfaenger != null) {
            return rechnungsempfaenger == angehoeriger;
        }
        return false;
    }

    @SuppressWarnings("java:S1192")
    @Override
    public String getGeschwisterAsString() {
        CheckGeschwisterSchuelerRechnungempfaengerCommand command = new CheckGeschwisterSchuelerRechnungempfaengerCommand(schueler, isRechnungsempfaengerDrittperson());
        command.execute();
        List<Schueler> geschwisters = command.getGeschwisterList();
        if (!geschwisters.isEmpty()) {
            StringBuilder infoGeschwisterStb = new StringBuilder("<html>");
            for (Schueler geschwister : geschwisters) {
                if (infoGeschwisterStb.length() > 6) {
                    infoGeschwisterStb.append("<br>");
                }
                infoGeschwisterStb.append(geschwister.toStringForGuiWithAbgemeldetInfo());
            }
            infoGeschwisterStb.append("</html>");
            return infoGeschwisterStb.toString();
        }
        return "-";
    }

    @Override
    public String getLabelSchuelerGleicherRechnungsempfaenger1() {
        return "Andere Schüler mit " + (schueler.getRechnungsempfaenger().getAnrede() == Anrede.FRAU ? "gleicher" : "gleichem");
    }

    @Override
    public String getLabelSchuelerGleicherRechnungsempfaenger2() {
        return (schueler.getRechnungsempfaenger().getAnrede() == Anrede.FRAU ? "Rechnungsempfängerin:" : "Rechnungsempfänger:");
    }

    @Override
    public String getSchuelerGleicherRechnungsempfaengerAsString() {
        CheckGeschwisterSchuelerRechnungempfaengerCommand command = new CheckGeschwisterSchuelerRechnungempfaengerCommand(schueler);
        command.execute();
        List<Schueler> schuelerList = command.getAndereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList();
        if (!schuelerList.isEmpty()) {
            StringBuilder infoSchuelerGleicherRechnungsempfaenger = new StringBuilder("<html>");
            for (Schueler schueler1 : schuelerList) {
                if (infoSchuelerGleicherRechnungsempfaenger.length() > 6) {
                    infoSchuelerGleicherRechnungsempfaenger.append("<br>");
                }
                infoSchuelerGleicherRechnungsempfaenger.append(schueler1.toString());
            }
            infoSchuelerGleicherRechnungsempfaenger.append("</html>");
            return infoSchuelerGleicherRechnungsempfaenger.toString();
        }
        return "";
    }

    @Override
    public String getSchuelerGeburtsdatumAsString() {
        return asString(schueler.getGeburtsdatum());
    }

    @Override
    public String getAnmeldedatumAsString() {
        List<Anmeldung> anmeldungen = schueler.getAnmeldungen();
        if (!anmeldungen.isEmpty() && anmeldungen.get(0).getAnmeldedatum() != null) {
            return asString(anmeldungen.get(0).getAnmeldedatum());
        }
        return "-";
    }

    @Override
    public String getAbmeldedatumAsString() {
        List<Anmeldung> anmeldungen = schueler.getAnmeldungen();
        if (!anmeldungen.isEmpty() && anmeldungen.get(0).getAbmeldedatum() != null) {
            return asString(anmeldungen.get(0).getAbmeldedatum());
        }
        return "";
    }

    @SuppressWarnings("java:S3776")
    @Override
    public String getFruehereAnmeldungenAsString() {
        List<Anmeldung> anmeldungen = schueler.getAnmeldungen();
        if (anmeldungen.size() > 1) {
            StringBuilder fruehereAnmeldungen = new StringBuilder("<html>");
            if (neusteZuoberst) {
                for (int i = 1; i < anmeldungen.size(); i++) {
                    if (fruehereAnmeldungen.length() > 6) {
                        fruehereAnmeldungen.append("<br>");
                    }
                    fruehereAnmeldungen.append(anmeldungen.get(i).toString());
                }
            } else {
                for (int i = anmeldungen.size() - 1; i >= 1; i--) {
                    if (fruehereAnmeldungen.length() > 6) {
                        fruehereAnmeldungen.append("<br>");
                    }
                    fruehereAnmeldungen.append(anmeldungen.get(i).toString());
                }
            }
            fruehereAnmeldungen.append("</html>");
            return fruehereAnmeldungen.toString();
        }
        return "";
    }

    @Override
    public String getBemerkungen() {
        if (schueler.getBemerkungen() != null && !schueler.getBemerkungen().isEmpty()) {
            return schueler.getBemerkungenLineBreaksReplacedByHtmlBr();
        } else {
            return "-";
        }
    }

    @SuppressWarnings("java:S3776")
    @Override
    public String getDispensationsdauerAsString() {
        List<Dispensation> dispensationen = schueler.getDispensationen();
        if (!dispensationen.isEmpty()) {
            Dispensation dispensation = (neusteZuoberst ? dispensationen.get(0) : dispensationen.get(dispensationen.size() - 1));
            if (isDispensationAktuell(dispensation)) {
                String dauer;
                if (dispensation.getDispensationsende() == null) {
                    if (dispensation.getDispensationsbeginn().after(new GregorianCalendar())) {
                        dauer = "ab " + asString(dispensation.getDispensationsbeginn());
                    } else {
                        dauer = "seit " + asString(dispensation.getDispensationsbeginn());
                    }

                    if (dispensation.getVoraussichtlicheDauer() != null) {
                        dauer = dauer + ", voraussichtlich " + dispensation.getVoraussichtlicheDauer();
                    }
                } else {
                    dauer = asString(dispensation.getDispensationsbeginn()) + " - " + asString(dispensation.getDispensationsende());
                }
                return dauer;
            }
        }
        return "";
    }

    @Override
    public String getDispensationsgrund() {
        List<Dispensation> dispensationen = schueler.getDispensationen();
        if (!dispensationen.isEmpty()) {
            Dispensation dispensation = (neusteZuoberst ? dispensationen.get(0) : dispensationen.get(dispensationen.size() - 1));
            if (isDispensationAktuell(dispensation)) {
                return dispensation.getGrund();
            }
        }
        return "";
    }

    @Override
    public String getFruehereDispensationenAsString() {
        List<Dispensation> dispensationen = schueler.getDispensationen();
        StringBuilder fruehereDispensationen = new StringBuilder("<html>");
        for (Dispensation dispensation : dispensationen) {
            if (!isDispensationAktuell(dispensation) && isDispensationToBeDisplayed(dispensation)) {
                if (fruehereDispensationen.length() > 6) {
                    fruehereDispensationen.append("<br>");
                }
                fruehereDispensationen.append(asString(dispensation.getDispensationsbeginn())).append(" - ").append(asString(dispensation.getDispensationsende())).append(" (").append(dispensation.getGrund()).append(")");
            }
        }
        fruehereDispensationen.append("</html>");
        if (fruehereDispensationen.length() > 13) {
            return fruehereDispensationen.toString();
        }
        return "-";
    }

    private boolean isDispensationToBeDisplayed(Dispensation dispensation) {
        int maxMonthsInPast = 48;
        Calendar minSemesterEnde = Converter.getNMonthsBeforeNow(maxMonthsInPast);
        return minSemesterEnde.before(dispensation.getDispensationsende()) || minSemesterEnde.equals(dispensation.getDispensationsende());
    }

    @Override
    public String getSchuelerCodesAsString() {
        if (!schueler.getSchuelerCodes().isEmpty()) {
            StringBuilder codesSb = new StringBuilder("<html>");
            for (SchuelerCode schuelerCode : schueler.getSortedSchuelerCodes()) {
                if (codesSb.length() > 6) {
                    codesSb.append("<br>");
                }
                codesSb.append(schuelerCode);
            }
            codesSb.append("</html>");
            if (codesSb.length() > 13) {
                return codesSb.toString();
            }
        }
        return "-";
    }

    @SuppressWarnings("java:S3776")
    @Override
    public String getSemesterKurseAsString(SvmModel svmModel) {
        if (!schueler.getKursanmeldungen().isEmpty()) {
            FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(svmModel.getSemestersAll());
            findSemesterForCalendarCommand.execute();
            Semester previousSemester = findSemesterForCalendarCommand.getPreviousSemester();
            Semester currentSemester = findSemesterForCalendarCommand.getCurrentSemester();
            Semester nextSemester = findSemesterForCalendarCommand.getNextSemester();
            StringBuilder semesterSb = new StringBuilder("<html>");
            String previousSchuljahr = null;
            Semesterbezeichnung previousSemesterbezeichnung = null;
            boolean gleichesSemester = false;
            for (Kursanmeldung kursanmeldung : schueler.getSortedKursanmeldungen()) {
                Kurs kurs = kursanmeldung.getKurs();
                if (!isKursToBeDisplayed(kurs, previousSemester, currentSemester, nextSemester)) {
                    continue;
                }
                //noinspection PointlessNullCheck
                gleichesSemester = previousSchuljahr != null && previousSemesterbezeichnung != null
                        && kurs.getSemester().getSchuljahr().equals(previousSchuljahr)
                        && kurs.getSemester().getSemesterbezeichnung() == previousSemesterbezeichnung;
                if (gleichesSemester || semesterSb.length() <= 6) {
                    semesterSb.append("<p>");
                } else {
                    semesterSb.append("<p style='margin-top:12'>");
                }
                if (!gleichesSemester) {
                    semesterSb.append(kurs.getSemester().getSchuljahr()).append(", ").append(kurs.getSemester().getSemesterbezeichnung()).append(":");
                }
                previousSchuljahr = kurs.getSemester().getSchuljahr();
                previousSemesterbezeichnung = kurs.getSemester().getSemesterbezeichnung();
            }
            if (gleichesSemester) {
                // Hack damit Leerzeile nicht ignoriert wird (<p></html>: <p> wird ignoriert)
                semesterSb.append("<p>");
            }
            semesterSb.append("</html>");
            if (semesterSb.length() > 13) {
                return semesterSb.toString();
            }
        }
        return "-";
    }

    @Override
    public String getKurseAsString(SvmModel svmModel) {
        if (!schueler.getKursanmeldungen().isEmpty()) {
            FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(svmModel.getSemestersAll());
            findSemesterForCalendarCommand.execute();
            Semester previousSemester = findSemesterForCalendarCommand.getPreviousSemester();
            Semester currentSemester = findSemesterForCalendarCommand.getCurrentSemester();
            Semester nextSemester = findSemesterForCalendarCommand.getNextSemester();
            String previousSchuljahr = null;
            Semesterbezeichnung previousSemesterbezeichnung = null;
            StringBuilder kurseSb = new StringBuilder("<html>");
            for (Kursanmeldung kursanmeldung : schueler.getSortedKursanmeldungen()) {
                Kurs kurs = kursanmeldung.getKurs();
                if (!isKursToBeDisplayed(kurs, previousSemester, currentSemester, nextSemester)) {
                    continue;
                }
                boolean gleichesSemester = previousSemesterbezeichnung != null
                        && kurs.getSemester().getSchuljahr().equals(previousSchuljahr)
                        && kurs.getSemester().getSemesterbezeichnung() == previousSemesterbezeichnung;
                if (gleichesSemester || kurseSb.length() <= 6) {
                    kurseSb.append("<p>");
                } else {
                    kurseSb.append("<p style='margin-top:12'>");
                }
                kurseSb.append(kursanmeldung);
                previousSchuljahr = kurs.getSemester().getSchuljahr();
                previousSemesterbezeichnung = kurs.getSemester().getSemesterbezeichnung();
            }
            kurseSb.append("</html>");
            if (kurseSb.length() > 13) {
                return kurseSb.toString();
            }
        }
        return "";
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isKursToBeDisplayed(Kurs kurs, Semester previousSemester, Semester currentSemester, Semester nextSemester) {
        return (currentSemester != null && kurs.getSemester().getSemesterId().equals(currentSemester.getSemesterId()))
                // bereits erfasstes nächstes Semester
                || (nextSemester != null && kurs.getSemester().getSemesterId().equals(nextSemester.getSemesterId()))
                // zwischen zwei Semestern
                || (currentSemester == null && previousSemester != null && kurs.getSemester().getSemesterId().equals(previousSemester.getSemesterId()));
    }

    @Override
    public String getMaerchen() {
        if (aktuelleMaercheneinteilung == null) {
            return "-";
        }
        return aktuelleMaercheneinteilung.getMaerchen().toString();
    }

    @Override
    public String getGruppe() {
        if (aktuelleMaercheneinteilung == null) {
            return "-";
        }
        return aktuelleMaercheneinteilung.getGruppe().toString();
    }

    @Override
    public String getRolle1() {
        if (aktuelleMaercheneinteilung == null || aktuelleMaercheneinteilung.getRolle1() == null) {
            return "-";
        }
        return aktuelleMaercheneinteilung.getRolle1();
    }

    @Override
    public String getBilderRolle1() {
        if (aktuelleMaercheneinteilung == null || aktuelleMaercheneinteilung.getBilderRolle1() == null) {
            return "-";
        }
        return aktuelleMaercheneinteilung.getBilderRolle1();
    }

    @Override
    public String getRolle2() {
        if (aktuelleMaercheneinteilung == null || aktuelleMaercheneinteilung.getRolle2() == null) {
            return "-";
        }
        return aktuelleMaercheneinteilung.getRolle2();
    }

    @Override
    public String getBilderRolle2() {
        if (aktuelleMaercheneinteilung == null || aktuelleMaercheneinteilung.getBilderRolle2() == null) {
            return "-";
        }
        return aktuelleMaercheneinteilung.getBilderRolle2();
    }

    @Override
    public String getRolle3() {
        if (aktuelleMaercheneinteilung == null || aktuelleMaercheneinteilung.getRolle3() == null) {
            return "-";
        }
        return aktuelleMaercheneinteilung.getRolle3();
    }

    @Override
    public String getBilderRolle3() {
        if (aktuelleMaercheneinteilung == null || aktuelleMaercheneinteilung.getBilderRolle3() == null) {
            return "-";
        }
        return aktuelleMaercheneinteilung.getBilderRolle3();
    }

    @Override
    public String getElternmithilfe() {
        if (aktuelleMaercheneinteilung == null || aktuelleMaercheneinteilung.getElternmithilfe() == null) {
            return "-";
        }
        return aktuelleMaercheneinteilung.getElternmithilfe().toString();
    }

    @Override
    public String getElternmithilfeCode() {
        if (aktuelleMaercheneinteilung == null || aktuelleMaercheneinteilung.getElternmithilfeCode() == null) {
            return "-";
        }
        return aktuelleMaercheneinteilung.getElternmithilfeCode().toString();
    }

    @Override
    public String getKuchenVorstellungenAsString() {
        if (aktuelleMaercheneinteilung == null || aktuelleMaercheneinteilung.getKuchenVorstellungenAsString().isEmpty()) {
            return "-";
        }
        return aktuelleMaercheneinteilung.getKuchenVorstellungenAsString();
    }

    @Override
    public String getZusatzattribut() {
        if (aktuelleMaercheneinteilung == null || aktuelleMaercheneinteilung.getZusatzattribut() == null) {
            return "-";
        }
        return aktuelleMaercheneinteilung.getZusatzattribut();
    }

    @Override
    public String getBemerkungenMaerchen() {
        if (aktuelleMaercheneinteilung == null || aktuelleMaercheneinteilung.getBemerkungen() == null) {
            return "-";
        }
        return aktuelleMaercheneinteilung.getBemerkungen();
    }

    @SuppressWarnings("DuplicatedCode")
    private Maercheneinteilung determineAktuelleMaercheneinteilung(List<Maercheneinteilung> maercheneinteilungen) {
        Calendar today = new GregorianCalendar();
        int schuljahr1;
        if (today.get(Calendar.MONTH) == Calendar.JANUARY) {
            schuljahr1 = today.get(Calendar.YEAR) - 1;
        } else {
            schuljahr1 = today.get(Calendar.YEAR);
        }
        int schuljahr2 = schuljahr1 + 1;
        String anzuzeigendesSchuljahr = schuljahr1 + "/" + schuljahr2;
        for (Maercheneinteilung maercheneinteilung : maercheneinteilungen) {
            if (maercheneinteilung.getMaerchen().getSchuljahr().equals(anzuzeigendesSchuljahr)) {
                return maercheneinteilung;
            }
        }
        return null;
    }

    @Override
    public DispensationenTableData getDispensationenTableData() {
        return new DispensationenTableData(schueler.getDispensationen());
    }

    @Override
    public CodesTableData getCodesTableData() {
        List<SchuelerCode> schuelerCodes = new ArrayList<>(schueler.getSchuelerCodes());
        Collections.sort(schuelerCodes);
        return new CodesTableData(schuelerCodes, true);
    }

    @Override
    public KursanmeldungenTableData getKurseinteilungenTableData() {
        return new KursanmeldungenTableData(schueler.getSortedKursanmeldungen());
    }

    @Override
    public MaercheneinteilungenTableData getMaercheneinteilungenTableData() {
        return new MaercheneinteilungenTableData(schueler.getSortedMaercheneinteilungen());
    }

    private boolean isDispensationAktuell(Dispensation dispensation) {
        Calendar today = new GregorianCalendar();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return dispensation.getDispensationsende() == null || dispensation.getDispensationsende().equals(today) || dispensation.getDispensationsende().after(today);
    }

    @Override
    public SchuelerModel getSchuelerModel(SvmContext svmContext) {
        SchuelerModel schuelerModel = svmContext.getModelFactory().createSchuelerModel();
        schuelerModel.setSchuelerOrigin(schueler);
        return schuelerModel;
    }

    @Override
    public AngehoerigerModel getMutterModel(SvmContext svmContext) {
        AngehoerigerModel mutterModel = svmContext.getModelFactory().createAngehoerigerModel();
        mutterModel.setAngehoeriger(schueler.getMutter(), isGleicheAdresseWieSchueler(schueler.getMutter()), isRechnungsempfaengerMutter());
        return mutterModel;
    }

    @Override
    public AngehoerigerModel getVaterModel(SvmContext svmContext) {
        AngehoerigerModel vaterModel = svmContext.getModelFactory().createAngehoerigerModel();
        vaterModel.setAngehoeriger(schueler.getVater(), isGleicheAdresseWieSchueler(schueler.getVater()), isRechnungsempfaengerVater());
        return vaterModel;
    }

    @Override
    public AngehoerigerModel getRechnungsempfaengerModel(SvmContext svmContext) {
        AngehoerigerModel drittempfaengerModel = svmContext.getModelFactory().createAngehoerigerModel();
        if (!isRechnungsempfaengerMutter() && !isRechnungsempfaengerVater()) {
            drittempfaengerModel.setAngehoeriger(schueler.getRechnungsempfaenger(), false, true);
        }
        return drittempfaengerModel;
    }

    @Override
    public Schueler getSchueler() {
        return schueler;
    }

    @SuppressWarnings("ExtractMethodRecommender")
    @Override
    public void refreshSchuelerSuchenTableData(SvmContext svmContext, SchuelerSuchenTableModel schuelerSuchenTableModel) {
        // Kurse-Map neu setzen
        CommandInvoker commandInvoker = new CommandInvokerImpl();
        List<Schueler> schuelerList = schuelerSuchenTableModel.getSchuelerList();
        Semester semester = schuelerSuchenTableModel.getSemester();
        Wochentag wochentag = schuelerSuchenTableModel.getWochentag();
        Time zeitBeginn = schuelerSuchenTableModel.getZeitBeginn();
        Mitarbeiter mitarbeiter = schuelerSuchenTableModel.getLehrkraft();
        Calendar stichtag = schuelerSuchenTableModel.getStichtag();
        boolean keineAbgemeldetenKurseAnzeigen = schuelerSuchenTableModel.isKeineAbgemeldetenKurseAnzeigen();
        Calendar anmeldemonat = schuelerSuchenTableModel.getAnmeldemonat();
        Calendar abmeldemonat = schuelerSuchenTableModel.getAbmeldemonat();
        FindKurseMapSchuelerSemesterCommand findKurseMapSchuelerSemesterCommand = new FindKurseMapSchuelerSemesterCommand(schuelerList, semester, wochentag, zeitBeginn, mitarbeiter, stichtag, keineAbgemeldetenKurseAnzeigen, anmeldemonat, abmeldemonat);
        commandInvoker.executeCommand(findKurseMapSchuelerSemesterCommand);
        schuelerSuchenTableModel.getSchuelerSuchenTableData().setKurse(findKurseMapSchuelerSemesterCommand.getKurseMap());
        // Maercheneinteilungen-Map neu setzen
        Maerchen maerchen = schuelerSuchenTableModel.getMaerchen();
        FindMaercheneinteilungenMapSchuelerSemesterCommand findMaercheneinteilungenMapSchuelerSemesterCommand = new FindMaercheneinteilungenMapSchuelerSemesterCommand(schuelerList, maerchen);
        findMaercheneinteilungenMapSchuelerSemesterCommand.execute();
        schuelerSuchenTableModel.getSchuelerSuchenTableData().setMaercheneinteilungen(findMaercheneinteilungenMapSchuelerSemesterCommand.getMaercheneinteilungenMap());
    }

    private boolean isGleicheAdresseWieSchueler(Angehoeriger angehoeriger) {
        return (angehoeriger != null) && (schueler.getAdresse().isIdenticalWith(angehoeriger.getAdresse()));
    }

    @Override
    public boolean checkIfStammdatenMitEmail() {
        return checkNotEmpty(schueler.getEmail()) || schueler.getMutter() != null && checkNotEmpty(schueler.getMutter().getEmail()) || schueler.getVater() != null && checkNotEmpty(schueler.getVater().getEmail()) || checkNotEmpty(schueler.getRechnungsempfaenger().getEmail());
    }

}
