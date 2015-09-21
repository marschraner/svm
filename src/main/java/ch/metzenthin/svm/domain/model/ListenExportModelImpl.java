package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Elternteil;
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
public class ListenExportModelImpl extends AbstractModel implements ListenExportModel {
    
    private Listentyp listentyp;
    private String titel;
    private File templateFile;
    
    public ListenExportModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

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
            case SCHUELER_ABSENZENLISTE:
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
            case LEHRKRAEFTE_ADRESSLISTE:
                break;
            case MITARBEITER_LISTE:
                break;
            case MITARBEITER_ADRESSETIKETTEN:
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
            case MAHNUNGEN_SERIENBRIEF:
                if (!checkIfRechnungsdatumVorrechnungUeberallGesetzt(semesterrechnungenTableModel) && !checkIfRechnungsdatumNachrechnungUeberallGesetzt(semesterrechnungenTableModel)) {
                    String listenErstellenWarningMessage = "Die Rechnungsauswahl enthält Rechnungen ohne Rechnungsdatum. \n" +
                            "Es werden nur Rechnungen mit gesetztem Rechnungsdatum als Mahnung exportiert. Forfahren?";
                    String listenErstellenWarningTitle = "Rechnungsdatum nicht überall gesetzt";
                    listenErstellenWarning = new String[]{listenErstellenWarningMessage, listenErstellenWarningTitle};
                }
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
        String outputFile = listentyp + "." + listentyp.getFiletyp().getFileExtension();
        outputFile = outputFile.replaceAll("\\p{Blank}\\(Word\\)", "");
        outputFile = outputFile.replaceAll("\\p{Blank}\\(CSV\\)", "");
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
            case SCHUELER_ABSENZENLISTE:
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
                CreateAdressenCsvFileCommand createAdressenCsvFileCommandSchueler = new CreateAdressenCsvFileCommand(schuelerSuchenTableModel.getSchuelerList(), outputFile);
                commandInvoker.executeCommand(createAdressenCsvFileCommandSchueler);
                result = createAdressenCsvFileCommandSchueler.getResult();
                break;
            case RECHNUNGSEMPFAENGER_ADRESSETIKETTEN:
                Set<Person> rechnungsempfaengerSet = new HashSet<>();
                for (Schueler schueler : schuelerSuchenTableModel.getSchuelerList()) {
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
                for (Schueler schueler : schuelerSuchenTableModel.getSchuelerList()) {
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
                    if (maercheneinteilung == null || maercheneinteilung.getElternmithilfe() == null) {
                        continue;
                    }
                    elternmithilfeSet.add((maercheneinteilung.getElternmithilfe() == Elternteil.MUTTER ? schueler.getMutter() : schueler.getVater()));
                }
                List<Person> elternmithilfeList = new ArrayList<>(elternmithilfeSet);
                Collections.sort(elternmithilfeList);
                CreateAdressenCsvFileCommand createAdressenCsvFileCommandElternmithilfe = new CreateAdressenCsvFileCommand(elternmithilfeList, outputFile);
                commandInvoker.executeCommand(createAdressenCsvFileCommandElternmithilfe);
                result = createAdressenCsvFileCommandElternmithilfe.getResult();
                break;
            case LEHRKRAEFTE_ADRESSLISTE:
                CreateLehrkraefteAdresslisteCommand createLehrkraefteAdresslisteCommand = new CreateLehrkraefteAdresslisteCommand(mitarbeitersTableModel, titel, outputFile);
                commandInvoker.executeCommand(createLehrkraefteAdresslisteCommand);
                result = createLehrkraefteAdresslisteCommand.getResult();
                break;
            case MITARBEITER_LISTE:
                CreateMitarbeiterlisteCsvFileCommand createMitarbeiterlisteCsvFileCommand = new CreateMitarbeiterlisteCsvFileCommand(mitarbeitersTableModel.getMitarbeiters(), outputFile);
                commandInvoker.executeCommand(createMitarbeiterlisteCsvFileCommand);
                result = createMitarbeiterlisteCsvFileCommand.getResult();
                break;
            case MITARBEITER_ADRESSETIKETTEN:
                CreateAdressenCsvFileCommand createAdressenCsvFileCommandLehrkraefte = new CreateAdressenCsvFileCommand(mitarbeitersTableModel.getMitarbeiters(), outputFile);
                commandInvoker.executeCommand(createAdressenCsvFileCommandLehrkraefte);
                result = createAdressenCsvFileCommandLehrkraefte.getResult();
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
                CreateRechnungenSerienbriefCsvFileCommand createVorrechnungenSerienbriefCsvFileCommand = new CreateRechnungenSerienbriefCsvFileCommand(semesterrechnungenTableModel.getSemesterrechnungen(), Rechnungstyp.VORRECHNUNG, outputFile);
                commandInvoker.executeCommand(createVorrechnungenSerienbriefCsvFileCommand);
                result = createVorrechnungenSerienbriefCsvFileCommand.getResult();
                break;
            case NACHRECHNUNGEN_SERIENBRIEF:
                CreateRechnungenSerienbriefCsvFileCommand createNachrechnungenSerienbriefCsvFileCommand = new CreateRechnungenSerienbriefCsvFileCommand(semesterrechnungenTableModel.getSemesterrechnungen(), Rechnungstyp.NACHRECHNUNG, outputFile);
                commandInvoker.executeCommand(createNachrechnungenSerienbriefCsvFileCommand);
                result = createNachrechnungenSerienbriefCsvFileCommand.getResult();
                break;
            case MAHNUNGEN_SERIENBRIEF:
                CreateMahnungenSerienbriefCsvFileCommand createMahnungenSerienbriefCsvFileCommand = new CreateMahnungenSerienbriefCsvFileCommand(semesterrechnungenTableModel.getSemesterrechnungen(), outputFile);
                commandInvoker.executeCommand(createMahnungenSerienbriefCsvFileCommand);
                result = createMahnungenSerienbriefCsvFileCommand.getResult();
                break;
            case RECHNUNGSLISTE:
                CreateRechnungslisteCsvFileCommand createRechnungslisteCsvFileCommand = new CreateRechnungslisteCsvFileCommand(semesterrechnungenTableModel.getSemesterrechnungen(), outputFile);
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
                if (schuelerSuchenTableModel.getLehrkraft() != null) {
                    titleInit = getTitleSpecificKurs(schuelerSuchenTableModel);
                } else {
                    titleInit = "Adressliste";
                }
                break;
            case SCHUELER_ABSENZENLISTE:
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
            case LEHRKRAEFTE_ADRESSLISTE:
                titleInit = "Lehrkräfte";
                break;
            case MITARBEITER_LISTE:
                break;
            case MITARBEITER_ADRESSETIKETTEN:
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
            case MAHNUNGEN_SERIENBRIEF:
                break;
            case RECHNUNGSLISTE:
                break;
        }
        return titleInit;
    }

    private String getTitleSpecificKurs(SchuelerSuchenTableModel schuelerSuchenTableModel) {
        if (schuelerSuchenTableModel.getWochentag() != null && schuelerSuchenTableModel.getZeitBeginn() != null) {
            if (schuelerSuchenTableModel.getSchuelerList().size() > 0) {
                String lehrkraefte = schuelerSuchenTableModel.getSchuelerList().get(0).getKursanmeldungenAsList().get(0).getKurs().getLehrkraefteAsStr();
                String zeitEnde = asString(schuelerSuchenTableModel.getSchuelerList().get(0).getKursanmeldungenAsList().get(0).getKurs().getZeitEnde());
                String kursort = schuelerSuchenTableModel.getSchuelerList().get(0).getKursanmeldungenAsList().get(0).getKurs().getKursort().getBezeichnung();
                return lehrkraefte + " (" + schuelerSuchenTableModel.getWochentag() + " " + asString(schuelerSuchenTableModel.getZeitBeginn()) + "-" + zeitEnde + ", " + kursort + ")";
            } else {
                CommandInvoker commandInvoker = getCommandInvoker();
                FindKursCommand findKursCommand = new FindKursCommand(schuelerSuchenTableModel.getSemester(), schuelerSuchenTableModel.getWochentag(), schuelerSuchenTableModel.getZeitBeginn(), schuelerSuchenTableModel.getLehrkraft());
                commandInvoker.executeCommand(findKursCommand);
                if (findKursCommand.getResult() == FindKursCommand.Result.KURS_EXISTIERT_NICHT) {
                    return "";
                }
                Kurs kursFound = findKursCommand.getKursFound();
                return kursFound.getLehrkraefteAsStr() + " (" + kursFound.getWochentag() +  " " + asString(kursFound.getZeitBeginn()) + "-" + asString(kursFound.getZeitEnde()) + ", " + kursFound.getKursort() + ")";
            }
        } else {
            return schuelerSuchenTableModel.getLehrkraft().toString();
        }
    }

    private String getTitleMaerchen(SchuelerSuchenTableModel schuelerSuchenTableModel) {
        return schuelerSuchenTableModel.getMaerchen().getBezeichnung();
    }

    private boolean checkIfRechnungsdatumVorrechnungUeberallGesetzt(SemesterrechnungenTableModel semesterrechnungenTableModel) {
        for (Semesterrechnung semesterrechnung : semesterrechnungenTableModel.getSemesterrechnungen()) {
            if (semesterrechnung.getRechnungsdatumVorrechnung() == null) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfRechnungsdatumNachrechnungUeberallGesetzt(SemesterrechnungenTableModel semesterrechnungenTableModel) {
        for (Semesterrechnung semesterrechnung : semesterrechnungenTableModel.getSemesterrechnungen()) {
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
