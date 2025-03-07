package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Elternmithilfe;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Listentyp;
import ch.metzenthin.svm.common.datatypes.Rechnungstyp;
import ch.metzenthin.svm.common.utils.SvmProperties;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.*;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;

import java.io.File;
import java.util.*;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Martin Schraner
 */
class ListenExportModelImpl extends AbstractModel implements ListenExportModel {

    private static final String RECHNUNGSDATUM_NICHT_UEBERALL_GESETZT
            = "Rechnungsdatum nicht überall gesetzt";

    private Listentyp listentyp;
    private String titel;
    private File templateFile;

    @Override
    public Listentyp getListentyp() {
        return listentyp;
    }

    @Override
    public void setListentyp(Listentyp listentyp) throws SvmRequiredException {
        Listentyp oldValue = this.listentyp;
        this.listentyp = listentyp;
        firePropertyChange(Field.LISTENTYP, oldValue, this.listentyp);
        if (listentyp == null) {
            invalidate();
            throw new SvmRequiredException(Field.LISTENTYP);
        }
    }

    private final StringModelAttribute titelModelAttribute = new StringModelAttribute(
            this,
            Field.TITEL, 2, 110,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return titel;
                }

                @Override
                public void setValue(String value) {
                    titel = value;
                }
            }
    );

    @Override
    public String getTitel() {
        return titelModelAttribute.getValue();
    }

    @Override
    public File getTemplateFile() {
        return templateFile;
    }

    @Override
    public void setTitel(String titel) throws SvmValidationException {
        titelModelAttribute.setNewValue(true, titel, isBulkUpdate());
    }

    @Override
    public String[] getListenErstellenWarning(SemesterrechnungenTableModel semesterrechnungenTableModel) {
        String[] listenErstellenWarning = null;
        switch (listentyp) {
            case VORRECHNUNGEN_SERIENBRIEF -> {
                if (!checkIfRechnungsdatumVorrechnungUeberallGesetzt(semesterrechnungenTableModel)) {
                    String listenErstellenWarningMessage = "Die Rechnungsauswahl enthält Vorrechnungen ohne Rechnungsdatum. \n" +
                            "Es werden nur Rechnungen mit gesetztem Rechnungsdatum exportiert. Fortfahren?";
                    listenErstellenWarning = new String[]{
                            listenErstellenWarningMessage, RECHNUNGSDATUM_NICHT_UEBERALL_GESETZT};
                }
            }
            case NACHRECHNUNGEN_SERIENBRIEF -> {
                if (!checkIfRechnungsdatumNachrechnungUeberallGesetzt(semesterrechnungenTableModel)) {
                    String listenErstellenWarningMessage = "Die Rechnungsauswahl enthält Nachrechnungen ohne Rechnungsdatum. \n" +
                            "Es werden nur Rechnungen mit gesetztem Rechnungsdatum exportiert. Fortfahren?";
                    listenErstellenWarning = new String[]{
                            listenErstellenWarningMessage, RECHNUNGSDATUM_NICHT_UEBERALL_GESETZT};
                }
            }
            case MAHNUNGEN_VORRECHNUNGEN_SERIENBRIEF -> {
                if (!checkIfRechnungsdatumVorrechnungUeberallGesetzt(semesterrechnungenTableModel)) {
                    String listenErstellenWarningMessage = "Die Rechnungsauswahl enthält Vorrechnungen ohne Rechnungsdatum. \n" +
                            "Es werden nur Rechnungen mit gesetztem Rechnungsdatum als Mahnung exportiert. Fortfahren?";
                    listenErstellenWarning = new String[]{
                            listenErstellenWarningMessage, RECHNUNGSDATUM_NICHT_UEBERALL_GESETZT};
                }
            }
            case MAHNUNGEN_NACHRECHNUNGEN_SERIENBRIEF -> {
                if (!checkIfRechnungsdatumNachrechnungUeberallGesetzt(semesterrechnungenTableModel)) {
                    String listenErstellenWarningMessage = "Die Rechnungsauswahl enthält Nachrechnungen ohne Rechnungsdatum. \n" +
                            "Es werden nur Rechnungen mit gesetztem Rechnungsdatum als Mahnung exportiert. Fortfahren?";
                    listenErstellenWarning = new String[]{
                            listenErstellenWarningMessage, RECHNUNGSDATUM_NICHT_UEBERALL_GESETZT};
                }
            }
            default -> {
                // Nothing to do
            }
        }
        return listenErstellenWarning;
    }

    @Override
    public File getSaveFileInit() {
        Properties prop = SvmProperties.getSvmProperties();
        File listenDirectoryInit = new File(prop.getProperty(SvmProperties.KEY_DEFAULT_OUTPUT_DIRECTORY) + File.separator);
        if (!listenDirectoryInit.exists()) {
            boolean success = listenDirectoryInit.mkdirs();
            if (!success) {
                return null;
            }
        }
        String outputFile = listentyp.getFilenameOhneFileExtension() + "." + listentyp.getFiletyp().getFileExtension();
        outputFile = outputFile.replaceAll(",[ \\t]", "_");
        outputFile = outputFile.replaceAll("[ \\t]", "_");
        outputFile = outputFile.replace("ä", "ae");
        outputFile = outputFile.replace("ö", "oe");
        outputFile = outputFile.replace("ü", "ue");
        return new File(listenDirectoryInit.getAbsolutePath() + File.separator + outputFile);
    }

    @SuppressWarnings({"java:S3776", "java:S6541", "ExtractMethodRecommender"})
    @Override
    public CreateListeCommand.Result createListenFile(File outputFile, SchuelerSuchenTableModel schuelerSuchenTableModel, MitarbeitersTableModel mitarbeitersTableModel, KurseTableModel kurseTableModel, SemesterrechnungenTableModel semesterrechnungenTableModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        CreateListeCommand.Result result = null;
        switch (listentyp) {
            case SCHUELER_ADRESSLISTE -> {
                CreateSchuelerAdresslisteCommand createSchuelerAdresslisteCommand = new CreateSchuelerAdresslisteCommand(schuelerSuchenTableModel, titel, outputFile);
                commandInvoker.executeCommand(createSchuelerAdresslisteCommand);
                result = createSchuelerAdresslisteCommand.getResult();
            }
            case ABSENZENLISTE_GANZES_SEMESTER,
                 ABSENZENLISTE_OKTOBER_FEBRUAR,
                 SPEZIELLE_ABSENZENLISTE -> {
                CreateListeFromTemplateCommand createListeFromTemplateCommand = new CreateListeFromTemplateCommand(schuelerSuchenTableModel, titel, listentyp, outputFile);
                commandInvoker.executeCommand(createListeFromTemplateCommand);
                templateFile = createListeFromTemplateCommand.getTemplateFile();
                result = createListeFromTemplateCommand.getResult();
            }
            case ROLLENLISTE -> {
                CreateRollenlisteCommand createRollenlisteCommand = new CreateRollenlisteCommand(schuelerSuchenTableModel, titel, outputFile);
                commandInvoker.executeCommand(createRollenlisteCommand);
                result = createRollenlisteCommand.getResult();
            }
            case ELTERNMITHILFE_LISTE -> {
                CreateElternmithilfeListeCommand createElternmithilfeListeCommand = new CreateElternmithilfeListeCommand(schuelerSuchenTableModel, titel, outputFile);
                commandInvoker.executeCommand(createElternmithilfeListeCommand);
                result = createElternmithilfeListeCommand.getResult();
            }
            case SCHUELER_ADRESSETIKETTEN -> {
                CreateAdressenCsvFileCommand createAdressenCsvFileCommandSchueler = new CreateAdressenCsvFileCommand(schuelerSuchenTableModel.getSelektierteSchuelerList(), outputFile);
                commandInvoker.executeCommand(createAdressenCsvFileCommandSchueler);
                result = createAdressenCsvFileCommandSchueler.getResult();
            }
            case RECHNUNGSEMPFAENGER_ADRESSETIKETTEN -> {
                Set<Person> rechnungsempfaengerSet = new HashSet<>();
                for (Schueler schueler : schuelerSuchenTableModel.getSelektierteSchuelerList()) {
                    rechnungsempfaengerSet.add(schueler.getRechnungsempfaenger());
                }
                List<Person> rechnungsempfaengerList = new ArrayList<>(rechnungsempfaengerSet);
                Collections.sort(rechnungsempfaengerList);
                CreateAdressenCsvFileCommand createAdressenCsvFileCommandRechnungsempfaenger = new CreateAdressenCsvFileCommand(rechnungsempfaengerList, outputFile);
                commandInvoker.executeCommand(createAdressenCsvFileCommandRechnungsempfaenger);
                result = createAdressenCsvFileCommandRechnungsempfaenger.getResult();
            }
            case MUTTER_ODER_VATER_ADRESSETIKETTEN -> {
                Set<Person> mutterOderVaterSet = new HashSet<>();
                for (Schueler schueler : schuelerSuchenTableModel.getSelektierteSchuelerList()) {
                    if (schueler.getMutter() != null && schueler.getMutter().getAdresse() != null) {
                        mutterOderVaterSet.add(schueler.getMutter());
                    } else if (schueler.getVater() != null && schueler.getVater().getAdresse() != null) {
                        mutterOderVaterSet.add(schueler.getVater());
                    } else {
                        mutterOderVaterSet.add(schueler.getRechnungsempfaenger());
                    }
                }
                List<Person> mutterOderVaterList = new ArrayList<>(mutterOderVaterSet);
                Collections.sort(mutterOderVaterList);
                CreateAdressenCsvFileCommand createAdressenCsvFileCommandMutterOderVater = new CreateAdressenCsvFileCommand(mutterOderVaterList, outputFile);
                commandInvoker.executeCommand(createAdressenCsvFileCommandMutterOderVater);
                result = createAdressenCsvFileCommandMutterOderVater.getResult();
            }
            case ELTERNMITHILFE_ADRESSETIKETTEN -> {
                Set<Person> elternmithilfeSet = new HashSet<>();
                Map<Schueler, Maercheneinteilung> maercheneinteilungen = schuelerSuchenTableModel.getMaercheneinteilungen();
                for (Map.Entry<Schueler, Maercheneinteilung> maercheneinteilungenEntry : maercheneinteilungen.entrySet()) {
                    Schueler schueler = maercheneinteilungenEntry.getKey();
                    Maercheneinteilung maercheneinteilung = maercheneinteilungenEntry.getValue();
                    if (maercheneinteilung == null || maercheneinteilung.getElternmithilfe() == null || !schueler.isSelektiert()) {
                        continue;
                    }
                    Person elternmithilfe;
                    if (maercheneinteilung.getElternmithilfe() == Elternmithilfe.MUTTER) {
                        elternmithilfe = schueler.getMutter();
                    } else if (maercheneinteilung.getElternmithilfe() == Elternmithilfe.VATER) {
                        elternmithilfe = schueler.getVater();
                    } else {
                        elternmithilfe = maercheneinteilung.getElternmithilfeDrittperson();
                    }
                    elternmithilfeSet.add(elternmithilfe);
                }
                List<Person> elternmithilfeList = new ArrayList<>(elternmithilfeSet);
                Collections.sort(elternmithilfeList);
                CreateAdressenCsvFileCommand createAdressenCsvFileCommandElternmithilfe = new CreateAdressenCsvFileCommand(elternmithilfeList, outputFile);
                commandInvoker.executeCommand(createAdressenCsvFileCommandElternmithilfe);
                result = createAdressenCsvFileCommandElternmithilfe.getResult();
            }
            case PROBEPLAENE_ETIKETTEN -> {
                CreateProbeplaeneEtikettenCsvFileCommand createProbeplaeneEtikettenCsvFileCommand = new CreateProbeplaeneEtikettenCsvFileCommand(schuelerSuchenTableModel, outputFile);
                commandInvoker.executeCommand(createProbeplaeneEtikettenCsvFileCommand);
                result = createProbeplaeneEtikettenCsvFileCommand.getResult();
            }
            case SCHUELERLISTE_CSV -> {
                CreateSchuelerlisteCsvFileCommand createSchuelerlisteCsvFileCommand = new CreateSchuelerlisteCsvFileCommand(schuelerSuchenTableModel, outputFile);
                commandInvoker.executeCommand(createSchuelerlisteCsvFileCommand);
                result = createSchuelerlisteCsvFileCommand.getResult();
            }
            case MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM,
                 MITARBEITER_ADRESSLISTE_OHNE_GEBURTSDATUM -> {
                CreateMitarbeiterAdresslisteCommand createMitarbeiterAdresslisteMitGeburtsdatumCommand = new CreateMitarbeiterAdresslisteCommand(mitarbeitersTableModel, titel, outputFile, listentyp);
                commandInvoker.executeCommand(createMitarbeiterAdresslisteMitGeburtsdatumCommand);
                result = createMitarbeiterAdresslisteMitGeburtsdatumCommand.getResult();
            }
            case VERTRETUNGSLISTE -> {
                CreateVertretungslisteCommand createVertretungslisteCommand = new CreateVertretungslisteCommand(mitarbeitersTableModel, titel, outputFile);
                commandInvoker.executeCommand(createVertretungslisteCommand);
                result = createVertretungslisteCommand.getResult();
            }
            case MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM_AHV_IBAN_VERTRETUNGSMOEGLICHKEITEN -> {
                CreateMitarbeiterAdresslisteAlleAttributeCommand createMitarbeiterAdresslisteAlleAttributeCommand = new CreateMitarbeiterAdresslisteAlleAttributeCommand(mitarbeitersTableModel, titel, outputFile);
                commandInvoker.executeCommand(createMitarbeiterAdresslisteAlleAttributeCommand);
                result = createMitarbeiterAdresslisteAlleAttributeCommand.getResult();
            }
            case MITARBEITER_ADRESSETIKETTEN -> {
                CreateAdressenCsvFileCommand createAdressenCsvFileCommandMitarbeiter = new CreateAdressenCsvFileCommand(mitarbeitersTableModel.getSelektierteMitarbeiters(), outputFile);
                commandInvoker.executeCommand(createAdressenCsvFileCommandMitarbeiter);
                result = createAdressenCsvFileCommandMitarbeiter.getResult();
            }
            case MITARBEITER_LISTE_NAME_ZWEISPALTIG_CSV -> {
                CreateMitarbeiterlisteCsvFileCommand createMitarbeiterlisteNameZweispaltigCsvFileCommand = new CreateMitarbeiterlisteCsvFileCommand(mitarbeitersTableModel.getSelektierteMitarbeiters(), outputFile, false);
                commandInvoker.executeCommand(createMitarbeiterlisteNameZweispaltigCsvFileCommand);
                result = createMitarbeiterlisteNameZweispaltigCsvFileCommand.getResult();
            }
            case MITARBEITER_LISTE_NAME_EINSPALTIG_CSV -> {
                CreateMitarbeiterlisteCsvFileCommand createMitarbeiterlisteNameEinspaltigCsvFileCommand = new CreateMitarbeiterlisteCsvFileCommand(mitarbeitersTableModel.getSelektierteMitarbeiters(), outputFile, true);
                commandInvoker.executeCommand(createMitarbeiterlisteNameEinspaltigCsvFileCommand);
                result = createMitarbeiterlisteNameEinspaltigCsvFileCommand.getResult();
            }
            case KURSLISTE_WORD -> {
                CreateKurslisteWordFileCommand createKurslisteWordFileCommand = new CreateKurslisteWordFileCommand(kurseTableModel, titel, outputFile);
                commandInvoker.executeCommand(createKurslisteWordFileCommand);
                result = createKurslisteWordFileCommand.getResult();
            }
            case KURSLISTE_CSV -> {
                CreateKurslisteCsvFileCommand createKurslisteCsvFileCommand = new CreateKurslisteCsvFileCommand(kurseTableModel.getKurse(), outputFile);
                commandInvoker.executeCommand(createKurslisteCsvFileCommand);
                result = createKurslisteCsvFileCommand.getResult();
            }
            case VORRECHNUNGEN_SERIENBRIEF -> {
                Semester previousSemester1 = findPreviousSemester(semesterrechnungenTableModel);
                CreateRechnungenSerienbriefCsvFileCommand createVorrechnungenSerienbriefCsvFileCommand = new CreateRechnungenSerienbriefCsvFileCommand(semesterrechnungenTableModel.getSelektierteSemesterrechnungen(), previousSemester1, Rechnungstyp.VORRECHNUNG, outputFile);
                commandInvoker.executeCommand(createVorrechnungenSerienbriefCsvFileCommand);
                result = createVorrechnungenSerienbriefCsvFileCommand.getResult();
            }
            case NACHRECHNUNGEN_SERIENBRIEF -> {
                Semester previousSemester2 = findPreviousSemester(semesterrechnungenTableModel);
                CreateRechnungenSerienbriefCsvFileCommand createNachrechnungenSerienbriefCsvFileCommand = new CreateRechnungenSerienbriefCsvFileCommand(semesterrechnungenTableModel.getSelektierteSemesterrechnungen(), previousSemester2, Rechnungstyp.NACHRECHNUNG, outputFile);
                commandInvoker.executeCommand(createNachrechnungenSerienbriefCsvFileCommand);
                result = createNachrechnungenSerienbriefCsvFileCommand.getResult();
            }
            case MAHNUNGEN_VORRECHNUNGEN_SERIENBRIEF -> {
                CreateMahnungenSerienbriefCsvFileCommand createMahnungenVorrechnungenSerienbriefCsvFileCommand = new CreateMahnungenSerienbriefCsvFileCommand(semesterrechnungenTableModel.getSelektierteSemesterrechnungen(), Rechnungstyp.VORRECHNUNG, outputFile);
                commandInvoker.executeCommand(createMahnungenVorrechnungenSerienbriefCsvFileCommand);
                result = createMahnungenVorrechnungenSerienbriefCsvFileCommand.getResult();
            }
            case MAHNUNGEN_NACHRECHNUNGEN_SERIENBRIEF -> {
                CreateMahnungenSerienbriefCsvFileCommand createMahnungenNachrechnungenSerienbriefCsvFileCommand = new CreateMahnungenSerienbriefCsvFileCommand(semesterrechnungenTableModel.getSelektierteSemesterrechnungen(), Rechnungstyp.NACHRECHNUNG, outputFile);
                commandInvoker.executeCommand(createMahnungenNachrechnungenSerienbriefCsvFileCommand);
                result = createMahnungenNachrechnungenSerienbriefCsvFileCommand.getResult();
            }
            case SEMESTERRECHNUNGEN_ADRESSETIKETTEN -> {
                Set<Person> rechnungsempfaengerSemesterrechnungSet = new HashSet<>();
                for (Semesterrechnung semesterrechnung : semesterrechnungenTableModel.getSelektierteSemesterrechnungen()) {
                    rechnungsempfaengerSemesterrechnungSet.add(semesterrechnung.getRechnungsempfaenger());
                }
                List<Person> rechnungsempfaengerSemesterrechnungList = new ArrayList<>(rechnungsempfaengerSemesterrechnungSet);
                Collections.sort(rechnungsempfaengerSemesterrechnungList);
                CreateAdressenCsvFileCommand createAdressenCsvFileCommandRechnungsempfaengerSemesterrechnung = new CreateAdressenCsvFileCommand(rechnungsempfaengerSemesterrechnungList, outputFile);
                commandInvoker.executeCommand(createAdressenCsvFileCommandRechnungsempfaengerSemesterrechnung);
                result = createAdressenCsvFileCommandRechnungsempfaengerSemesterrechnung.getResult();
            }
            case RECHNUNGSLISTE -> {
                Semester previousSemester3 = findPreviousSemester(semesterrechnungenTableModel);
                CreateRechnungslisteCsvFileCommand createRechnungslisteCsvFileCommand = new CreateRechnungslisteCsvFileCommand(semesterrechnungenTableModel.getSelektierteSemesterrechnungen(), previousSemester3, outputFile);
                commandInvoker.executeCommand(createRechnungslisteCsvFileCommand);
                result = createRechnungslisteCsvFileCommand.getResult();
            }
        }
        return result;
    }

    private Semester findPreviousSemester(SemesterrechnungenTableModel semesterrechnungenTableModel) {
        Semester currentSemester;
        if (semesterrechnungenTableModel.getSelektierteSemesterrechnungen().isEmpty()) {
            currentSemester = null;
        } else {
            currentSemester = semesterrechnungenTableModel.getSelektierteSemesterrechnungen().get(0).getSemester();
        }
        FindPreviousSemesterCommand findPreviousSemesterCommand = new FindPreviousSemesterCommand(currentSemester);
        getCommandInvoker().executeCommand(findPreviousSemesterCommand);
        return findPreviousSemesterCommand.getPreviousSemester();
    }

    @Override
    public String getTitleInit(SchuelerSuchenTableModel schuelerSuchenTableModel) {
        String titleInit = "";
        switch (listentyp) {
            case SCHUELER_ADRESSLISTE -> {
                if (schuelerSuchenTableModel.isKursFuerSucheBeruecksichtigen() && schuelerSuchenTableModel.getLehrkraft() != null) {
                    titleInit = getTitleSpecificKurs(schuelerSuchenTableModel);
                } else {
                    titleInit = "Adressliste";
                }
            }
            case ABSENZENLISTE_GANZES_SEMESTER,
                 ABSENZENLISTE_OKTOBER_FEBRUAR,
                 SPEZIELLE_ABSENZENLISTE ->
                    titleInit = getTitleSpecificKurs(schuelerSuchenTableModel);
            case ROLLENLISTE -> {
                titleInit = getTitleMaerchen(schuelerSuchenTableModel) + ": Rollenliste";
                if (schuelerSuchenTableModel.getGruppe() != null) {
                    titleInit = titleInit + " Gruppe " + schuelerSuchenTableModel.getGruppe().toString();
                }
            }
            case ELTERNMITHILFE_LISTE -> {
                titleInit = getTitleMaerchen(schuelerSuchenTableModel) + ": Eltern-Mithilfe";
                if (schuelerSuchenTableModel.getElternmithilfeCode() != null) {
                    titleInit = titleInit + " " + schuelerSuchenTableModel.getElternmithilfeCode().getBeschreibung();
                }
                if (schuelerSuchenTableModel.getGruppe() != null) {
                    titleInit = titleInit + " Gruppe " + schuelerSuchenTableModel.getGruppe().toString();
                }
            }
            case MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM,
                 MITARBEITER_ADRESSLISTE_OHNE_GEBURTSDATUM,
                 MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM_AHV_IBAN_VERTRETUNGSMOEGLICHKEITEN ->
                    titleInit = "Mitarbeitende";
            case VERTRETUNGSLISTE -> titleInit = "Vertretungsliste";
            case KURSLISTE_WORD -> titleInit = "Kurse";
            default -> {
                // Nothing to do
            }
        }
        return titleInit;
    }

    private String getTitleSpecificKurs(SchuelerSuchenTableModel schuelerSuchenTableModel) {
        if (schuelerSuchenTableModel.getWochentag() != null && schuelerSuchenTableModel.getZeitBeginn() != null) {
            CommandInvoker commandInvoker = getCommandInvoker();
            FindKursCommand findKursCommand = new FindKursCommand(schuelerSuchenTableModel.getSemester(), schuelerSuchenTableModel.getWochentag(), schuelerSuchenTableModel.getZeitBeginn(), schuelerSuchenTableModel.getLehrkraft());
            commandInvoker.executeCommand(findKursCommand);
            if (findKursCommand.getResult() == FindKursCommand.Result.KURS_EXISTIERT_NICHT) {
                return "";
            }
            Kurs kursFound = findKursCommand.getKursFound();
            return kursFound.getLehrkraefteAsStr() + " (" + kursFound.getWochentag() + " " + asString(kursFound.getZeitBeginn()) + "-" + asString(kursFound.getZeitEnde()) + ", " + kursFound.getKursort().getBezeichnung() + ", " + kursFound.getStufe() + ")";
        } else {
            return schuelerSuchenTableModel.getLehrkraft().toString();
        }
    }

    private String getTitleMaerchen(SchuelerSuchenTableModel schuelerSuchenTableModel) {
        return schuelerSuchenTableModel.getMaerchen().getBezeichnung();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean checkIfRechnungsdatumVorrechnungUeberallGesetzt(SemesterrechnungenTableModel semesterrechnungenTableModel) {
        for (Semesterrechnung semesterrechnung : semesterrechnungenTableModel.getSelektierteSemesterrechnungen()) {
            if (semesterrechnung.getRechnungsdatumVorrechnung() == null) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean checkIfRechnungsdatumNachrechnungUeberallGesetzt(SemesterrechnungenTableModel semesterrechnungenTableModel) {
        for (Semesterrechnung semesterrechnung : semesterrechnungenTableModel.getSelektierteSemesterrechnungen()) {
            if (semesterrechnung.getRechnungsdatumNachrechnung() == null) {
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    void doValidate() throws SvmValidationException {
        // Keine feldübergreifende Validierung notwendig
    }
}
