package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Elternmithilfe;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Listentyp;
import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
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
            new AttributeAccessor<String>() {
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
            case SCHUELER_ADRESSLISTE:
                break;
            case ABSENZENLISTE_GANZES_SEMESTER:
                break;
            case ABSENZENLISTE_OKTOBER_FEBRUAR:
                break;
            case SPEZIELLE_ABSENZENLISTE:
                break;
            case ROLLENLISTE:
                break;
            case ELTERNMITHILFE_LISTE:
                break;
            case SCHUELER_ADRESSETIKETTEN:
                break;
            case RECHNUNGSEMPFAENGER_ADRESSETIKETTEN:
                break;
            case MUTTER_ODER_VATER_ADRESSETIKETTEN:
                break;
            case ELTERNMITHILFE_ADRESSETIKETTEN:
                break;
            case PROBEPLAENE_ETIKETTEN:
                break;
            case SCHUELERLISTE_CSV:
                break;
            case MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM:
                break;
            case MITARBEITER_ADRESSLISTE_OHNE_GEBURTSDATUM:
                break;
            case VERTRETUNGSLISTE:
                break;
            case MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM_AHV_IBAN_VERTRETUNGSMOEGLICHKEITEN:
                break;
            case MITARBEITER_ADRESSETIKETTEN:
                break;
            case MITARBEITER_LISTE_NAME_ZWEISPALTIG_CSV:
                break;
            case MITARBEITER_LISTE_NAME_EINSPALTIG_CSV:
                break;
            case KURSLISTE_WORD:
                break;
            case KURSLISTE_CSV:
                break;
            case VORRECHNUNGEN_SERIENBRIEF:
                if (!checkIfRechnungsdatumVorrechnungUeberallGesetzt(semesterrechnungenTableModel)) {
                    String listenErstellenWarningMessage = "Die Rechnungsauswahl enthält Vorrechnungen ohne Rechnungsdatum. \n" +
                            "Es werden nur Rechnungen mit gesetztem Rechnungsdatum exportiert. Forfahren?";
                    String listenErstellenWarningTitle = "Rechnungsdatum nicht überall gesetzt";
                    listenErstellenWarning = new String[]{listenErstellenWarningMessage, listenErstellenWarningTitle};
                }
                break;
            case NACHRECHNUNGEN_SERIENBRIEF:
                if (!checkIfRechnungsdatumNachrechnungUeberallGesetzt(semesterrechnungenTableModel)) {
                    String listenErstellenWarningMessage = "Die Rechnungsauswahl enthält Nachrechnungen ohne Rechnungsdatum. \n" +
                            "Es werden nur Rechnungen mit gesetztem Rechnungsdatum exportiert. Forfahren?";
                    String listenErstellenWarningTitle = "Rechnungsdatum nicht überall gesetzt";
                    listenErstellenWarning = new String[]{listenErstellenWarningMessage, listenErstellenWarningTitle};
                }
                break;
            case MAHNUNGEN_VORRECHNUNGEN_SERIENBRIEF:
                if (!checkIfRechnungsdatumVorrechnungUeberallGesetzt(semesterrechnungenTableModel)) {
                    String listenErstellenWarningMessage = "Die Rechnungsauswahl enthält Vorrechnungen ohne Rechnungsdatum. \n" +
                            "Es werden nur Rechnungen mit gesetztem Rechnungsdatum als Mahnung exportiert. Forfahren?";
                    String listenErstellenWarningTitle = "Rechnungsdatum nicht überall gesetzt";
                    listenErstellenWarning = new String[]{listenErstellenWarningMessage, listenErstellenWarningTitle};
                }
                break;
            case MAHNUNGEN_NACHRECHNUNGEN_SERIENBRIEF:
                if (!checkIfRechnungsdatumNachrechnungUeberallGesetzt(semesterrechnungenTableModel)) {
                    String listenErstellenWarningMessage = "Die Rechnungsauswahl enthält Nachrechnungen ohne Rechnungsdatum. \n" +
                            "Es werden nur Rechnungen mit gesetztem Rechnungsdatum als Mahnung exportiert. Forfahren?";
                    String listenErstellenWarningTitle = "Rechnungsdatum nicht überall gesetzt";
                    listenErstellenWarning = new String[]{listenErstellenWarningMessage, listenErstellenWarningTitle};
                }
                break;
            case SEMESTERRECHNUNGEN_ADRESSETIKETTEN:
                break;
            case RECHNUNGSLISTE:
                break;
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
        outputFile = outputFile.replaceAll(",\\p{Blank}", "_");
        outputFile = outputFile.replaceAll("\\p{Blank}", "_");
        outputFile = outputFile.replaceAll("ä", "ae");
        outputFile = outputFile.replaceAll("ö", "oe");
        outputFile = outputFile.replaceAll("ü", "ue");
        return new File(listenDirectoryInit.getAbsolutePath() + File.separator + outputFile);
    }

    @Override
    public CreateListeCommand.Result createListenFile(File outputFile, SchuelerSuchenTableModel schuelerSuchenTableModel, MitarbeitersTableModel mitarbeitersTableModel, KurseTableModel kurseTableModel, SemesterrechnungenTableModel semesterrechnungenTableModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        CreateListeCommand.Result result = null;
        switch (listentyp) {
            case SCHUELER_ADRESSLISTE:
                CreateSchuelerAdresslisteCommand createSchuelerAdresslisteCommand = new CreateSchuelerAdresslisteCommand(schuelerSuchenTableModel, titel, outputFile);
                commandInvoker.executeCommand(createSchuelerAdresslisteCommand);
                result = createSchuelerAdresslisteCommand.getResult();
                break;
            case ABSENZENLISTE_GANZES_SEMESTER:
            case ABSENZENLISTE_OKTOBER_FEBRUAR:
            case SPEZIELLE_ABSENZENLISTE:
                CreateListeFromTemplateCommand createListeFromTemplateCommand = new CreateListeFromTemplateCommand(schuelerSuchenTableModel, titel, listentyp, outputFile);
                commandInvoker.executeCommand(createListeFromTemplateCommand);
                templateFile = createListeFromTemplateCommand.getTemplateFile();
                result = createListeFromTemplateCommand.getResult();
                break;
            case ROLLENLISTE:
                CreateRollenlisteCommand createRollenlisteCommand = new CreateRollenlisteCommand(schuelerSuchenTableModel, titel, outputFile);
                commandInvoker.executeCommand(createRollenlisteCommand);
                result = createRollenlisteCommand.getResult();
                break;
            case ELTERNMITHILFE_LISTE:
                CreateElternmithilfeListeCommand createElternmithilfeListeCommand = new CreateElternmithilfeListeCommand(schuelerSuchenTableModel, titel, outputFile);
                commandInvoker.executeCommand(createElternmithilfeListeCommand);
                result = createElternmithilfeListeCommand.getResult();
                break;
            case SCHUELER_ADRESSETIKETTEN:
                CreateAdressenCsvFileCommand createAdressenCsvFileCommandSchueler = new CreateAdressenCsvFileCommand(schuelerSuchenTableModel.getSelektierteSchuelerList(), outputFile);
                commandInvoker.executeCommand(createAdressenCsvFileCommandSchueler);
                result = createAdressenCsvFileCommandSchueler.getResult();
                break;
            case RECHNUNGSEMPFAENGER_ADRESSETIKETTEN:
                Set<Person> rechnungsempfaengerSet = new HashSet<>();
                for (Schueler schueler : schuelerSuchenTableModel.getSelektierteSchuelerList()) {
                    rechnungsempfaengerSet.add(schueler.getRechnungsempfaenger());
                }
                List<Person> rechnungsempfaengerList = new ArrayList<>(rechnungsempfaengerSet);
                Collections.sort(rechnungsempfaengerList);
                CreateAdressenCsvFileCommand createAdressenCsvFileCommandRechnungsempfaenger = new CreateAdressenCsvFileCommand(rechnungsempfaengerList, outputFile);
                commandInvoker.executeCommand(createAdressenCsvFileCommandRechnungsempfaenger);
                result = createAdressenCsvFileCommandRechnungsempfaenger.getResult();
                break;
            case MUTTER_ODER_VATER_ADRESSETIKETTEN:
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
                break;
            case ELTERNMITHILFE_ADRESSETIKETTEN:
                Set<Person> elternmithilfeSet = new HashSet<>();
                Map<Schueler, Maercheneinteilung> maercheneinteilungen = schuelerSuchenTableModel.getMaercheneinteilungen();
                for (Schueler schueler : maercheneinteilungen.keySet()) {
                    Maercheneinteilung maercheneinteilung = maercheneinteilungen.get(schueler);
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
                break;
            case PROBEPLAENE_ETIKETTEN:
                CreateProbeplaeneEtikettenCsvFileCommand createProbeplaeneEtikettenCsvFileCommand = new CreateProbeplaeneEtikettenCsvFileCommand(schuelerSuchenTableModel, outputFile);
                commandInvoker.executeCommand(createProbeplaeneEtikettenCsvFileCommand);
                result = createProbeplaeneEtikettenCsvFileCommand.getResult();
                break;
            case SCHUELERLISTE_CSV:
                CreateSchuelerlisteCsvFileCommand createSchuelerlisteCsvFileCommand = new CreateSchuelerlisteCsvFileCommand(schuelerSuchenTableModel, outputFile);
                commandInvoker.executeCommand(createSchuelerlisteCsvFileCommand);
                result = createSchuelerlisteCsvFileCommand.getResult();
                break;
            case MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM:
                CreateMitarbeiterAdresslisteCommand createMitarbeiterAdresslisteMitGeburtsdatumCommand = new CreateMitarbeiterAdresslisteCommand(mitarbeitersTableModel, titel, outputFile, listentyp);
                commandInvoker.executeCommand(createMitarbeiterAdresslisteMitGeburtsdatumCommand);
                result = createMitarbeiterAdresslisteMitGeburtsdatumCommand.getResult();
                break;
            case MITARBEITER_ADRESSLISTE_OHNE_GEBURTSDATUM:
                CreateMitarbeiterAdresslisteCommand createMitarbeiterAdresslisteCommand = new CreateMitarbeiterAdresslisteCommand(mitarbeitersTableModel, titel, outputFile, listentyp);
                commandInvoker.executeCommand(createMitarbeiterAdresslisteCommand);
                result = createMitarbeiterAdresslisteCommand.getResult();
                break;
            case VERTRETUNGSLISTE:
                CreateVertretungslisteCommand createVertretungslisteCommand = new CreateVertretungslisteCommand(mitarbeitersTableModel, titel, outputFile);
                commandInvoker.executeCommand(createVertretungslisteCommand);
                result = createVertretungslisteCommand.getResult();
                break;
            case MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM_AHV_IBAN_VERTRETUNGSMOEGLICHKEITEN:
                CreateMitarbeiterAdresslisteAlleAttributeCommand createMitarbeiterAdresslisteAlleAttributeCommand = new CreateMitarbeiterAdresslisteAlleAttributeCommand(mitarbeitersTableModel, titel, outputFile);
                commandInvoker.executeCommand(createMitarbeiterAdresslisteAlleAttributeCommand);
                result = createMitarbeiterAdresslisteAlleAttributeCommand.getResult();
                break;
            case MITARBEITER_ADRESSETIKETTEN:
                CreateAdressenCsvFileCommand createAdressenCsvFileCommandMitarbeiter = new CreateAdressenCsvFileCommand(mitarbeitersTableModel.getSelektierteMitarbeiters(), outputFile);
                commandInvoker.executeCommand(createAdressenCsvFileCommandMitarbeiter);
                result = createAdressenCsvFileCommandMitarbeiter.getResult();
                break;
            case MITARBEITER_LISTE_NAME_ZWEISPALTIG_CSV:
                CreateMitarbeiterlisteCsvFileCommand createMitarbeiterlisteNameZweispaltigCsvFileCommand = new CreateMitarbeiterlisteCsvFileCommand(mitarbeitersTableModel.getSelektierteMitarbeiters(), outputFile, false);
                commandInvoker.executeCommand(createMitarbeiterlisteNameZweispaltigCsvFileCommand);
                result = createMitarbeiterlisteNameZweispaltigCsvFileCommand.getResult();
                break;
            case MITARBEITER_LISTE_NAME_EINSPALTIG_CSV:
                CreateMitarbeiterlisteCsvFileCommand createMitarbeiterlisteNameEinspaltigCsvFileCommand = new CreateMitarbeiterlisteCsvFileCommand(mitarbeitersTableModel.getSelektierteMitarbeiters(), outputFile, true);
                commandInvoker.executeCommand(createMitarbeiterlisteNameEinspaltigCsvFileCommand);
                result = createMitarbeiterlisteNameEinspaltigCsvFileCommand.getResult();
                break;
            case KURSLISTE_WORD:
                CreateKurslisteWordFileCommand createKurslisteWordFileCommand = new CreateKurslisteWordFileCommand(kurseTableModel, titel, outputFile);
                commandInvoker.executeCommand(createKurslisteWordFileCommand);
                result = createKurslisteWordFileCommand.getResult();
                break;
            case KURSLISTE_CSV:
                CreateKurslisteCsvFileCommand createKurslisteCsvFileCommand = new CreateKurslisteCsvFileCommand(kurseTableModel.getKurse(), outputFile);
                commandInvoker.executeCommand(createKurslisteCsvFileCommand);
                result = createKurslisteCsvFileCommand.getResult();
                break;
            case VORRECHNUNGEN_SERIENBRIEF:
                CreateRechnungenSerienbriefCsvFileCommand createVorrechnungenSerienbriefCsvFileCommand = new CreateRechnungenSerienbriefCsvFileCommand(semesterrechnungenTableModel.getSelektierteSemesterrechnungen(), Rechnungstyp.VORRECHNUNG, outputFile);
                commandInvoker.executeCommand(createVorrechnungenSerienbriefCsvFileCommand);
                result = createVorrechnungenSerienbriefCsvFileCommand.getResult();
                break;
            case NACHRECHNUNGEN_SERIENBRIEF:
                CreateRechnungenSerienbriefCsvFileCommand createNachrechnungenSerienbriefCsvFileCommand = new CreateRechnungenSerienbriefCsvFileCommand(semesterrechnungenTableModel.getSelektierteSemesterrechnungen(), Rechnungstyp.NACHRECHNUNG, outputFile);
                commandInvoker.executeCommand(createNachrechnungenSerienbriefCsvFileCommand);
                result = createNachrechnungenSerienbriefCsvFileCommand.getResult();
                break;
            case MAHNUNGEN_VORRECHNUNGEN_SERIENBRIEF:
                CreateMahnungenSerienbriefCsvFileCommand createMahnungenVorrechnungenSerienbriefCsvFileCommand = new CreateMahnungenSerienbriefCsvFileCommand(semesterrechnungenTableModel.getSelektierteSemesterrechnungen(), Rechnungstyp.VORRECHNUNG, outputFile);
                commandInvoker.executeCommand(createMahnungenVorrechnungenSerienbriefCsvFileCommand);
                result = createMahnungenVorrechnungenSerienbriefCsvFileCommand.getResult();
                break;
            case MAHNUNGEN_NACHRECHNUNGEN_SERIENBRIEF:
                CreateMahnungenSerienbriefCsvFileCommand createMahnungenNachrechnungenSerienbriefCsvFileCommand = new CreateMahnungenSerienbriefCsvFileCommand(semesterrechnungenTableModel.getSelektierteSemesterrechnungen(), Rechnungstyp.NACHRECHNUNG, outputFile);
                commandInvoker.executeCommand(createMahnungenNachrechnungenSerienbriefCsvFileCommand);
                result = createMahnungenNachrechnungenSerienbriefCsvFileCommand.getResult();
                break;
            case SEMESTERRECHNUNGEN_ADRESSETIKETTEN:
                Set<Person> rechnungsempfaengerSemesterrechnungSet = new HashSet<>();
                for (Semesterrechnung semesterrechnung : semesterrechnungenTableModel.getSelektierteSemesterrechnungen()) {
                    rechnungsempfaengerSemesterrechnungSet.add(semesterrechnung.getRechnungsempfaenger());
                }
                List<Person> rechnungsempfaengerSemesterrechnungList = new ArrayList<>(rechnungsempfaengerSemesterrechnungSet);
                Collections.sort(rechnungsempfaengerSemesterrechnungList);
                CreateAdressenCsvFileCommand createAdressenCsvFileCommandRechnungsempfaengerSemesterrechnung = new CreateAdressenCsvFileCommand(rechnungsempfaengerSemesterrechnungList, outputFile);
                commandInvoker.executeCommand(createAdressenCsvFileCommandRechnungsempfaengerSemesterrechnung);
                result = createAdressenCsvFileCommandRechnungsempfaengerSemesterrechnung.getResult();
                break;
            case RECHNUNGSLISTE:
                Semester currentSemester;
                if (semesterrechnungenTableModel.getSelektierteSemesterrechnungen().isEmpty()) {
                    currentSemester = null;
                } else {
                    currentSemester = semesterrechnungenTableModel.getSelektierteSemesterrechnungen().get(0).getSemester();
                }
                FindPreviousSemesterCommand findPreviousSemesterCommand = new FindPreviousSemesterCommand(currentSemester);
                getCommandInvoker().executeCommand(findPreviousSemesterCommand);
                Semester previousSemester = findPreviousSemesterCommand.getPreviousSemester();
                CreateRechnungslisteCsvFileCommand createRechnungslisteCsvFileCommand = new CreateRechnungslisteCsvFileCommand(semesterrechnungenTableModel.getSelektierteSemesterrechnungen(), previousSemester, outputFile);
                commandInvoker.executeCommand(createRechnungslisteCsvFileCommand);
                result = createRechnungslisteCsvFileCommand.getResult();
                break;
        }
        return result;
    }

    @Override
    public String getTitleInit(SchuelerSuchenTableModel schuelerSuchenTableModel) {
        String titleInit = "";
        switch (listentyp) {
            case SCHUELER_ADRESSLISTE:
                if (schuelerSuchenTableModel.isKursFuerSucheBeruecksichtigen() && schuelerSuchenTableModel.getLehrkraft() != null) {
                    titleInit = getTitleSpecificKurs(schuelerSuchenTableModel);
                } else {
                    titleInit = "Adressliste";
                }
                break;
            case ABSENZENLISTE_GANZES_SEMESTER:
            case ABSENZENLISTE_OKTOBER_FEBRUAR:
            case SPEZIELLE_ABSENZENLISTE:
                titleInit = getTitleSpecificKurs(schuelerSuchenTableModel);
                break;
            case ROLLENLISTE:
                titleInit = getTitleMaerchen(schuelerSuchenTableModel) + ": Rollenliste";
                if (schuelerSuchenTableModel.getGruppe() != null) {
                    titleInit = titleInit + " Gruppe " + schuelerSuchenTableModel.getGruppe().toString();
                }
                break;
            case ELTERNMITHILFE_LISTE:
                titleInit = getTitleMaerchen(schuelerSuchenTableModel) + ": Eltern-Mithilfe";
                if (schuelerSuchenTableModel.getElternmithilfeCode() != null) {
                    titleInit = titleInit + " " + schuelerSuchenTableModel.getElternmithilfeCode().getBeschreibung();
                }
                if (schuelerSuchenTableModel.getGruppe() != null) {
                    titleInit = titleInit + " Gruppe " + schuelerSuchenTableModel.getGruppe().toString();
                }
                break;
            case SCHUELER_ADRESSETIKETTEN:
                break;
            case RECHNUNGSEMPFAENGER_ADRESSETIKETTEN:
                break;
            case MUTTER_ODER_VATER_ADRESSETIKETTEN:
                break;
            case ELTERNMITHILFE_ADRESSETIKETTEN:
                break;
            case PROBEPLAENE_ETIKETTEN:
                break;
            case MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM:
                titleInit = "Mitarbeitende";
                break;
            case MITARBEITER_ADRESSLISTE_OHNE_GEBURTSDATUM:
                titleInit = "Mitarbeitende";
                break;
            case VERTRETUNGSLISTE:
                titleInit = "Vertretungsliste";
                break;
            case MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM_AHV_IBAN_VERTRETUNGSMOEGLICHKEITEN:
                titleInit = "Mitarbeitende";
                break;
            case MITARBEITER_ADRESSETIKETTEN:
                break;
            case MITARBEITER_LISTE_NAME_ZWEISPALTIG_CSV:
                break;
            case MITARBEITER_LISTE_NAME_EINSPALTIG_CSV:
                break;
            case KURSLISTE_WORD:
                titleInit = "Kurse";
                break;
            case KURSLISTE_CSV:
                break;
            case VORRECHNUNGEN_SERIENBRIEF:
                break;
            case NACHRECHNUNGEN_SERIENBRIEF:
                break;
            case MAHNUNGEN_VORRECHNUNGEN_SERIENBRIEF:
                break;
            case MAHNUNGEN_NACHRECHNUNGEN_SERIENBRIEF:
                break;
            case SEMESTERRECHNUNGEN_ADRESSETIKETTEN:
                break;
            case RECHNUNGSLISTE:
                break;
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
            return kursFound.getLehrkraefteAsStr() + " (" + kursFound.getWochentag() +  " " + asString(kursFound.getZeitBeginn()) + "-" + asString(kursFound.getZeitEnde()) + ", " + kursFound.getKursort().getBezeichnung() + ", " + kursFound.getStufe() + ")";
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
    void doValidate() throws SvmValidationException {}
}
