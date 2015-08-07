package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.*;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;

import java.sql.Time;
import java.util.*;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Hans Stamm
 */
public class SchuelerDatenblattModelImpl implements SchuelerDatenblattModel {

    private Schueler schueler;

    public SchuelerDatenblattModelImpl(Schueler schueler) {
        this.schueler = schueler;
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
            return mutter.toString();
        }
        return "-";
    }

    @Override
    public String getVaterAsString() {
        Angehoeriger vater = schueler.getVater();
        if (vater != null) {
            return vater.toString();
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
            if (rechnungsempfaenger == angehoeriger) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getGeschwisterAsString() {
        CheckGeschwisterSchuelerRechnungempfaengerCommand command = new CheckGeschwisterSchuelerRechnungempfaengerCommand(schueler, isRechnungsempfaengerDrittperson());
        command.execute();
        List<Schueler> angemeldeteGeschwister = command.getAngemeldeteGeschwisterList();
        if (!angemeldeteGeschwister.isEmpty()) {
            StringBuilder infoGeschwisterStb = new StringBuilder("<html>");
            for (Schueler geschwister : angemeldeteGeschwister) {
                if (infoGeschwisterStb.length() > 6) {
                    infoGeschwisterStb.append("<br>");
                }
                infoGeschwisterStb.append(geschwister.toString());
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
            for (Schueler schueler : schuelerList) {
                if (infoSchuelerGleicherRechnungsempfaenger.length() > 6) {
                    infoSchuelerGleicherRechnungsempfaenger.append("<br>");
                }
                infoSchuelerGleicherRechnungsempfaenger.append(schueler.toString());
            }
            infoSchuelerGleicherRechnungsempfaenger.append("</html>");
            return infoSchuelerGleicherRechnungsempfaenger.toString();
        }
        return "-";
    }

    @Override
    public String getSchuelerGeburtsdatumAsString() {
        return asString(schueler.getGeburtsdatum());
    }

    @Override
    public String getAnmeldedatumAsString() {
        List<Anmeldung> anmeldungen = schueler.getAnmeldungen();
        if (!anmeldungen.isEmpty()) {
            if (anmeldungen.get(0).getAnmeldedatum() != null) {
                return asString(anmeldungen.get(0).getAnmeldedatum());
            }
        }
        return "-";
    }

    @Override
    public String getAbmeldedatumAsString() {
        List<Anmeldung> anmeldungen = schueler.getAnmeldungen();
        if (!anmeldungen.isEmpty()) {
            if (anmeldungen.get(0).getAbmeldedatum() != null) {
                return asString(anmeldungen.get(0).getAbmeldedatum());
            }
        }
        return "";
    }

    @Override
    public String getFruehereAnmeldungenAsString() {
        List<Anmeldung> anmeldungen = schueler.getAnmeldungen();
        if (anmeldungen.size() > 1) {
            StringBuilder fruehereAnmeldungen = new StringBuilder("<html>");
            for (int i = 1; i < anmeldungen.size(); i++) {
                if (fruehereAnmeldungen.length() > 6) {
                    fruehereAnmeldungen.append("<br>");
                }
                fruehereAnmeldungen.append(anmeldungen.get(i).toString());
            }
            fruehereAnmeldungen.append("</html>");
            return fruehereAnmeldungen.toString();
        }
        return "";
    }

    @Override
    public String getBemerkungen() {
        if (schueler.getBemerkungen() != null && !schueler.getBemerkungen().isEmpty()) {
            return schueler.getBemerkungen();
        } else {
            return "-";
        }
    }

    @Override
    public String getDispensationsdauerAsString() {
        List<Dispensation> dispensationen = schueler.getDispensationen();
        if (!dispensationen.isEmpty()) {
            Dispensation dispensation = dispensationen.get(0);
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
            Dispensation dispensation = dispensationen.get(0);
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
        int MAX_MONTHS_IN_PAST = 48;
        Calendar minSemesterEnde = new GregorianCalendar();
        minSemesterEnde.add(Calendar.MONTH, -MAX_MONTHS_IN_PAST);
        return minSemesterEnde.before(dispensation.getDispensationsende()) || minSemesterEnde.equals(dispensation.getDispensationsende());
    }

    @Override
    public String getCodesAsString() {
        if (!schueler.getCodes().isEmpty()) {
            StringBuilder codesSb = new StringBuilder("<html>");
            for (Code code : schueler.getCodesAsList()) {
                if (codesSb.length() > 6) {
                    codesSb.append("<br>");
                }
                codesSb.append(code);
            }
            codesSb.append("</html>");
            if (codesSb.length() > 13) {
                return codesSb.toString();
            }
        }
        return "-";
    }

    @Override
    public String getSemesterKurseAsString(SvmModel svmModel) {
        if (!schueler.getKurse().isEmpty()) {
            StringBuilder semesterSb = new StringBuilder("<html>");
            String previousSchuljahr = null;
            Semesterbezeichnung previousSemesterbezeichnung = null;
            boolean gleichesSemester = false;
            for (Kurs kurs : schueler.getKurseAsList()) {
                if (!isKursToBeDisplayed(svmModel, kurs)) {
                    break;
                }
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
            System.out.println(semesterSb);
        }
        return "-";
    }

    @Override
    public String getKurseAsString(SvmModel svmModel) {
        String previousSchuljahr = null;
        Semesterbezeichnung previousSemesterbezeichnung = null;
        if (!schueler.getKurse().isEmpty()) {
            StringBuilder kurseSb = new StringBuilder("<html>");
            for (Kurs kurs : schueler.getKurseAsList()) {
                if (!isKursToBeDisplayed(svmModel, kurs)) {
                    break;
                }
                boolean gleichesSemester = false;
                if (previousSchuljahr != null && previousSemesterbezeichnung != null
                        && kurs.getSemester().getSchuljahr().equals(previousSchuljahr)
                        && kurs.getSemester().getSemesterbezeichnung() == previousSemesterbezeichnung) {
                    gleichesSemester = true;
                }
                if (gleichesSemester || kurseSb.length() <= 6) {
                    kurseSb.append("<p>");
                } else {
                    kurseSb.append("<p style='margin-top:12'>");
                }
                kurseSb.append(kurs);
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

    private boolean isKursToBeDisplayed(SvmModel svmModel, Kurs kurs) {
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(svmModel.getSemestersAll());
        findSemesterForCalendarCommand.execute();
        Semester previousSemester = findSemesterForCalendarCommand.getPreviousSemester();
        Semester currentSemester = findSemesterForCalendarCommand.getCurrentSemester();
        Semester nextSemester = findSemesterForCalendarCommand.getNextSemester();
        return (currentSemester != null && kurs.getSemester().isIdenticalWith(currentSemester))
                // bereits erfasstes nächstes Semester
                || (nextSemester != null && kurs.getSemester().isIdenticalWith(nextSemester))
                // zwischen zwei Semestern
                || (currentSemester == null && previousSemester != null && kurs.getSemester().isIdenticalWith(previousSemester));
    }

    @Override
    public DispensationenTableData getDispensationenTableData() {
        return new DispensationenTableData(schueler.getDispensationen());
    }

    @Override
    public CodesTableData getCodesTableData() {
        List<Code> codes = new ArrayList<>(schueler.getCodes());
        Collections.sort(codes);
        return new CodesTableData(new ArrayList<>(schueler.getCodes()));
    }

    @Override
    public KurseTableData getKurseTableData() {
        return new KurseTableData(schueler.getKurseAsList(), true);
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
        schuelerModel.setSchueler(schueler);
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

    @Override
    public void refreshSchuelerSuchenTableData(SvmContext svmContext, SchuelerSuchenTableModel schuelerSuchenTableModel) {
        // KursMap neu setzen
        CommandInvoker commandInvoker = svmContext.getCommandInvoker();
        List<Schueler> schuelerList = schuelerSuchenTableModel.getSchuelerList();
        Semester semester = schuelerSuchenTableModel.getSemester();
        Wochentag wochentag = schuelerSuchenTableModel.getWochentag();
        Time zeitBeginn = schuelerSuchenTableModel.getZeitBeginn();
        Lehrkraft lehrkraft = schuelerSuchenTableModel.getLehrkraft();
        FindKurseMapSchuelerSemesterCommand findKurseMapSchuelerSemesterCommand = new FindKurseMapSchuelerSemesterCommand(schuelerList, semester, wochentag, zeitBeginn, lehrkraft);
        commandInvoker.executeCommand(findKurseMapSchuelerSemesterCommand);
        schuelerSuchenTableModel.getSchuelerSuchenTableData().setKurse(findKurseMapSchuelerSemesterCommand.getKurseMap());
    }

    private boolean isGleicheAdresseWieSchueler(Angehoeriger angehoeriger) {
        return (angehoeriger != null) && (schueler.getAdresse().isIdenticalWith(angehoeriger.getAdresse()));
    }

}
