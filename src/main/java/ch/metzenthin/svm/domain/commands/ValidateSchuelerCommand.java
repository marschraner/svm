package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.domain.model.*;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.List;

/**
 * @author Hans Stamm
 */
public class ValidateSchuelerCommand extends GenericDaoCommand {

    public enum Result {
        SCHUELER_BEREITS_IN_DATENBANK,
        MUTTER_NICHT_IN_DATENBANK,
        MUTTER_EIN_EINTRAG_PASST {
            @Override
            public Entry proceedUebernehmen() {
                return Entry.MUTTER_AUS_DATENBANK_UEBERNEHMEN;
            }
        },
        MUTTER_MEHRERE_EINTRAEGE_PASSEN,
        MUTTER_EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE {
            @Override
            public Entry proceedUebernehmen() {
                return Entry.MUTTER_AUS_DATENBANK_UEBERNEHMEN;
            }
            @Override
            public Entry proceedWeiterfahren() {
                return Entry.MIT_BISHERIGER_MUTTER_WEITERFAHREN;
            }
        },
        MUTTER_MEHRERE_EINTAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE {
            @Override
            public Entry proceedWeiterfahren() {
                return Entry.MIT_BISHERIGER_MUTTER_WEITERFAHREN;
            }
        },
        VATER_NICHT_IN_DATENBANK,
        VATER_EIN_EINTRAG_PASST {
            @Override
            public Entry proceedUebernehmen() {
                return Entry.VATER_AUS_DATENBANK_UEBERNEHMEN;
            }
        },
        VATER_MEHRERE_EINTRAEGE_PASSEN,
        VATER_EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE {
            @Override
            public Entry proceedUebernehmen() {
                return Entry.VATER_AUS_DATENBANK_UEBERNEHMEN;
            }
            @Override
            public Entry proceedWeiterfahren() {
                return Entry.MIT_BISHERIGEM_VATER_WEITERFAHREN;
            }
        },
        VATER_MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE {
            @Override
            public Entry proceedWeiterfahren() {
                return Entry.MIT_BISHERIGEM_VATER_WEITERFAHREN;
            }
        },
        RECHNUNGSEMPFAENGER_DRITTPERSON_NICHT_IN_DATENBANK,
        RECHNUNGSEMPFAENGER_DRITTPERSON_EIN_EINTRAG_PASST {
            @Override
            public Entry proceedUebernehmen() {
                return Entry.RECHNUNGSEMPFAENGER_DRITTPERSON_AUS_DATENBANK_UEBERNEHMEN;
            }
        },
        RECHNUNGSEMPFAENGER_DRITTPERSON_MEHRERE_EINTRAEGE_PASSEN,
        RECHNUNGSEMPFAENGER_DRITTPERSON_EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE {
            @Override
            public Entry proceedUebernehmen() {
                return Entry.RECHNUNGSEMPFAENGER_DRITTPERSON_AUS_DATENBANK_UEBERNEHMEN;
            }
            @Override
            public Entry proceedWeiterfahren() {
                return Entry.MIT_BISHERIGEM_RECHNUNGSEMPFAENGER_DRITTPERSON_WEITERFAHREN;
            }
        },
        RECHNUNGSEMPFAENGER_DRITTPERSON_MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE {
            @Override
            public Entry proceedWeiterfahren() {
                return Entry.MIT_BISHERIGEM_RECHNUNGSEMPFAENGER_DRITTPERSON_WEITERFAHREN;
            }
        },
        CHECK_GESCHWISTER_SCHUELER_RECHNUGSEMFPAENGER_COMMAND_FINISHED {
            @Override
            public Entry proceedWeiterfahren() {
                return Entry.SUMMARY_BESTAETIGT;
            }
        };

        public Entry proceedUebernehmen() {
            return null;
        }
        public Entry proceedWeiterfahren() {
            return null;
        }
    }

    public enum Entry {
        NEU_ERFASSTEN_SCHUELER_VALIDIEREN,
        BEARBEITETEN_SCHUELER_VALIDIEREN,
        MUTTER_AUS_DATENBANK_UEBERNEHMEN,
        MIT_BISHERIGER_MUTTER_WEITERFAHREN,
        VATER_AUS_DATENBANK_UEBERNEHMEN,
        MIT_BISHERIGEM_VATER_WEITERFAHREN,
        RECHNUNGSEMPFAENGER_DRITTPERSON_AUS_DATENBANK_UEBERNEHMEN,
        MIT_BISHERIGEM_RECHNUNGSEMPFAENGER_DRITTPERSON_WEITERFAHREN,
        SUMMARY_BESTAETIGT
    }

    // input
    private Schueler schueler;
    private Adresse adresseSchueler;
    private Angehoeriger mutter;
    private Adresse adresseMutter;
    private boolean isRechnungsempfaengerMutter;
    private Angehoeriger vater;
    private Adresse adresseVater;
    private boolean isRechnungsempfaengerVater;
    private Angehoeriger rechnungsempfaengerDrittperson;
    private Adresse adresseRechnungsempfaengerDrittperson;
    private Entry entry;

    // output
    private SchuelerErfassenSaveResult result = null;
    private Result resultCheckMutterBereitsInDatenbank;
    private Result resultCheckVaterBereitsInDatenbank;
    private Result resultCheckRechnungsempfaengerDrittpersonBereitsInDatenbank;
    private Schueler schuelerFoundInDatabase;
    private Angehoeriger mutterFoundInDatabase;
    private List<Angehoeriger> mutterFoundInDatabaseList;
    private Angehoeriger vaterFoundInDatabase;
    private List<Angehoeriger> vaterFoundInDatabaseList;
    private Angehoeriger rechnungsempfaengerDrittpersonFoundInDatabase;
    private List<Angehoeriger> rechnungsempfaengerDrittpersonFoundInDatabaseList;
    private String identischeAdressen;
    private String abweichendeAdressen;
    private List<Schueler> angemeldeteGeschwisterList;
    private List<Schueler> andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList;
    private boolean isMutterNeu;
    private boolean isVaterNeu;
    private boolean isRechnungsempfaengerDrittpersonNeu;

    // skip-Variablen
    private boolean skipMutterVaterDrittpersonAusGuiUebernehmen = false;
    private boolean skipCheckSchuelerBereitsInDatenbank = false;
    private boolean skipCheckMutterBereitsInDatenbank = false;
    private boolean skipCheckVaterBereitsInDatenbank = false;
    private boolean skipCheckRechungsempfaengerDrittpersonBereitsInDatenbank = false;
    private boolean skipCheckIdentischeAdressen = false;
    private boolean skipCheckGeschwisterSchuelerRechnungsempfaenger = false;

    public ValidateSchuelerCommand(ValidateSchuelerModel validateSchuelerModel) {
        this.schueler = validateSchuelerModel.getSchueler();
        this.adresseSchueler = validateSchuelerModel.getAdresseSchueler();
        this.mutter = validateSchuelerModel.getMutter();
        this.adresseMutter = validateSchuelerModel.getAdresseMutter();
        this.isRechnungsempfaengerMutter = validateSchuelerModel.isRechnungsempfaengerMutter();
        this.vater = validateSchuelerModel.getVater();
        this.adresseVater = validateSchuelerModel.getAdresseVater();
        this.isRechnungsempfaengerVater = validateSchuelerModel.isRechnungsempfaengerVater();
        this.rechnungsempfaengerDrittperson = validateSchuelerModel.getRechnungsempfaengerDrittperson();
        this.adresseRechnungsempfaengerDrittperson= validateSchuelerModel.getAdresseRechnungsempfaengerDrittperson();
    }

    @Override
    public void execute() {

        determineHowToProceed();

        // 1. Mutter, Vater und Drittperson aus GUI übernehmen
        if (!skipMutterVaterDrittpersonAusGuiUebernehmen) {
            skipMutterVaterDrittpersonAusGuiUebernehmen = true;
            schueler.setAdresse(adresseSchueler);
            if (mutter != null) {
                schueler.setMutter(mutter);
                if (adresseMutter != null) {
                    mutter.setAdresse(adresseMutter);
                }
                if (isRechnungsempfaengerMutter) {
                    schueler.setRechnungsempfaenger(mutter);
                }
            }
            if (vater != null) {
                schueler.setVater(vater);
                if (adresseVater != null) {
                    vater.setAdresse(adresseVater);
                }
                if (isRechnungsempfaengerVater) {
                    schueler.setRechnungsempfaenger(vater);
                }
            }
            if (rechnungsempfaengerDrittperson != null) {
                schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson);
                rechnungsempfaengerDrittperson.setAdresse(adresseRechnungsempfaengerDrittperson);
            }
        }

        // 2. Schüler bereits in Datenbank?
        if (!skipCheckSchuelerBereitsInDatenbank) {
            skipCheckSchuelerBereitsInDatenbank = true;
            CheckSchuelerBereitsInDatenbankCommand checkSchuelerBereitsInDatenbankCommand = new CheckSchuelerBereitsInDatenbankCommand(schueler);
            checkSchuelerBereitsInDatenbankCommand.setEntityManager(entityManager);
            checkSchuelerBereitsInDatenbankCommand.execute();
            if (checkSchuelerBereitsInDatenbankCommand.isInDatenbank()) {
                schuelerFoundInDatabase = checkSchuelerBereitsInDatenbankCommand.getSchuelerFound();
                result = new SchuelerBereitsInDatenbankResult(schuelerFoundInDatabase);
                return;
            }
        }

        // 3.a Mutter bereits in Datenbank?
        if (schueler.getMutter() != null && !skipCheckMutterBereitsInDatenbank) {
            skipCheckMutterBereitsInDatenbank = true;
            CheckAngehoerigerBereitsInDatenbankCommand checkAngehoerigerBereitsInDatenbankCommand = new CheckAngehoerigerBereitsInDatenbankCommand(schueler.getMutter());
            checkAngehoerigerBereitsInDatenbankCommand.setEntityManager(entityManager);
            checkAngehoerigerBereitsInDatenbankCommand.execute();
            switch (checkAngehoerigerBereitsInDatenbankCommand.getResult()) {
                case NICHT_IN_DATENBANK:
                    mutterFoundInDatabase = null;
                    mutterFoundInDatabaseList = null;
                    resultCheckMutterBereitsInDatenbank = Result.MUTTER_NICHT_IN_DATENBANK;
                    isMutterNeu = true;
                    break;
                case EIN_EINTRAG_PASST:
                    mutterFoundInDatabase = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
                    mutterFoundInDatabaseList = null;
                    resultCheckMutterBereitsInDatenbank = Result.MUTTER_EIN_EINTRAG_PASST;
                    result = new AngehoerigerEinEintragPasstResult(mutterFoundInDatabase, resultCheckMutterBereitsInDatenbank);
                    return;
                case MEHRERE_EINTRAEGE_PASSEN:
                    mutterFoundInDatabase = null;
                    mutterFoundInDatabaseList = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList();
                    resultCheckMutterBereitsInDatenbank = Result.MUTTER_MEHRERE_EINTRAEGE_PASSEN;
                    result = new AngehoerigerMehrereEintraegePassenResult(mutterFoundInDatabaseList, resultCheckMutterBereitsInDatenbank);
                    return;
                case EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE:
                    mutterFoundInDatabase = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
                    mutterFoundInDatabaseList = null;
                    resultCheckMutterBereitsInDatenbank = Result.MUTTER_EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE;
                    result = new AngehoerigerEinEintragGleicherNameAndereAttributeResult(mutterFoundInDatabase, resultCheckMutterBereitsInDatenbank);
                    return;
                case MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE:
                    mutterFoundInDatabase = null;
                    mutterFoundInDatabaseList = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList();
                    resultCheckMutterBereitsInDatenbank = Result.MUTTER_MEHRERE_EINTAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE;
                    result = new AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult(mutterFoundInDatabaseList, resultCheckMutterBereitsInDatenbank);
                    return;
            }
        }

        // 3.b Vater bereits in Datenbank?
        if (schueler.getVater() != null && !skipCheckVaterBereitsInDatenbank) {
            skipCheckVaterBereitsInDatenbank = true;
            CheckAngehoerigerBereitsInDatenbankCommand checkAngehoerigerBereitsInDatenbankCommand = new CheckAngehoerigerBereitsInDatenbankCommand(schueler.getVater());
            checkAngehoerigerBereitsInDatenbankCommand.setEntityManager(entityManager);
            checkAngehoerigerBereitsInDatenbankCommand.execute();
            switch (checkAngehoerigerBereitsInDatenbankCommand.getResult()) {
                case NICHT_IN_DATENBANK:
                    vaterFoundInDatabase = null;
                    vaterFoundInDatabaseList = null;
                    resultCheckVaterBereitsInDatenbank = Result.VATER_NICHT_IN_DATENBANK;
                    isVaterNeu = true;
                    break;
                case EIN_EINTRAG_PASST:
                    vaterFoundInDatabase = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
                    vaterFoundInDatabaseList = null;
                    resultCheckVaterBereitsInDatenbank = Result.VATER_EIN_EINTRAG_PASST;
                    // result = resultCheckVaterBereitsInDatenbank;
                    return;
                case MEHRERE_EINTRAEGE_PASSEN:
                    vaterFoundInDatabase = null;
                    vaterFoundInDatabaseList = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList();
                    resultCheckVaterBereitsInDatenbank = Result.VATER_MEHRERE_EINTRAEGE_PASSEN;
                    // result = resultCheckVaterBereitsInDatenbank;
                    return;
                case EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE:
                    vaterFoundInDatabase = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
                    vaterFoundInDatabaseList = null;
                    resultCheckVaterBereitsInDatenbank = Result.VATER_EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE;
                    // result = resultCheckVaterBereitsInDatenbank;
                    return;
                case MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE:
                    vaterFoundInDatabase = null;
                    vaterFoundInDatabaseList = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList();
                    resultCheckVaterBereitsInDatenbank = Result.VATER_MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE;
                    // result = resultCheckVaterBereitsInDatenbank;
                    return;
            }
        }

        // 3.c Rechnungsempfänger Drittperson bereits in Datenbank?
        if (!isRechnungsempfaengerMutter && !isRechnungsempfaengerVater && !skipCheckRechungsempfaengerDrittpersonBereitsInDatenbank) {
            skipCheckRechungsempfaengerDrittpersonBereitsInDatenbank = true;
            CheckAngehoerigerBereitsInDatenbankCommand checkAngehoerigerBereitsInDatenbankCommand = new CheckAngehoerigerBereitsInDatenbankCommand(schueler.getRechnungsempfaenger());
            checkAngehoerigerBereitsInDatenbankCommand.setEntityManager(entityManager);
            checkAngehoerigerBereitsInDatenbankCommand.execute();
            switch (checkAngehoerigerBereitsInDatenbankCommand.getResult()) {
                case NICHT_IN_DATENBANK:
                    rechnungsempfaengerDrittpersonFoundInDatabase = null;
                    rechnungsempfaengerDrittpersonFoundInDatabaseList = null;
                    resultCheckRechnungsempfaengerDrittpersonBereitsInDatenbank = Result.RECHNUNGSEMPFAENGER_DRITTPERSON_NICHT_IN_DATENBANK;
                    isRechnungsempfaengerDrittpersonNeu = true;
                case EIN_EINTRAG_PASST:
                    rechnungsempfaengerDrittpersonFoundInDatabase = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
                    rechnungsempfaengerDrittpersonFoundInDatabaseList = null;
                    resultCheckRechnungsempfaengerDrittpersonBereitsInDatenbank = Result.RECHNUNGSEMPFAENGER_DRITTPERSON_EIN_EINTRAG_PASST;
                    // result = resultCheckRechnungsempfaengerDrittpersonBereitsInDatenbank;
                    return;
                case MEHRERE_EINTRAEGE_PASSEN:
                    rechnungsempfaengerDrittpersonFoundInDatabase = null;
                    rechnungsempfaengerDrittpersonFoundInDatabaseList = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList();
                    resultCheckRechnungsempfaengerDrittpersonBereitsInDatenbank = Result.RECHNUNGSEMPFAENGER_DRITTPERSON_MEHRERE_EINTRAEGE_PASSEN;
                    // result = resultCheckRechnungsempfaengerDrittpersonBereitsInDatenbank;
                    return;
                case EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE:
                    rechnungsempfaengerDrittpersonFoundInDatabase = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
                    rechnungsempfaengerDrittpersonFoundInDatabaseList = null;
                    resultCheckRechnungsempfaengerDrittpersonBereitsInDatenbank = Result.RECHNUNGSEMPFAENGER_DRITTPERSON_EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE;
                    // result = resultCheckRechnungsempfaengerDrittpersonBereitsInDatenbank;
                    return;
                case MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE:
                    rechnungsempfaengerDrittpersonFoundInDatabase = null;
                    rechnungsempfaengerDrittpersonFoundInDatabaseList = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList();
                    resultCheckRechnungsempfaengerDrittpersonBereitsInDatenbank = Result.RECHNUNGSEMPFAENGER_DRITTPERSON_MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE;
                    // result = resultCheckRechnungsempfaengerDrittpersonBereitsInDatenbank;
                    return;
            }
        }

        // 4. Identische Adressen?
        if (!skipCheckIdentischeAdressen) {
            skipCheckIdentischeAdressen = true;
            CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
            checkIdentischeAdressenCommand.execute();
            identischeAdressen = checkIdentischeAdressenCommand.getIdentischeAdressen();
            abweichendeAdressen = checkIdentischeAdressenCommand.getAbweichendeAdressen();
        }

        // 5. Nach Geschwistern suchen
        if (!skipCheckGeschwisterSchuelerRechnungsempfaenger) {
            skipCheckGeschwisterSchuelerRechnungsempfaenger = true;
            CheckGeschwisterSchuelerRechnungempfaengerCommand checkGeschwisterSchuelerRechnungempfaengerCommand = new CheckGeschwisterSchuelerRechnungempfaengerCommand(schueler);
            checkGeschwisterSchuelerRechnungempfaengerCommand.execute();
            angemeldeteGeschwisterList = checkGeschwisterSchuelerRechnungempfaengerCommand.getAngemeldeteGeschwisterList();
            andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList = checkGeschwisterSchuelerRechnungempfaengerCommand.getAndereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList();
            result = new ValidateSchuelerSummaryResult(schueler, angemeldeteGeschwisterList, andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList, identischeAdressen, abweichendeAdressen, isMutterNeu, isVaterNeu, isRechnungsempfaengerDrittpersonNeu);
            return;   // -> Summary-Dialog
        }

        // 6. Schüler speichern
        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler);
        saveSchuelerCommand.setEntityManager(entityManager);
        saveSchuelerCommand.execute();

    }

    /**
     * Aktionen, die am Beginn durchgeführt werden sollen, abhängig vom Wert von entry.
     */
    private void determineHowToProceed() {
        if (entry == null) {
           throw new RuntimeException("Eintrittspunkt nicht gesetzt!");
        }

        switch (entry) {

            case NEU_ERFASSTEN_SCHUELER_VALIDIEREN:
                break;

            case BEARBEITETEN_SCHUELER_VALIDIEREN:
                skipCheckSchuelerBereitsInDatenbank = true;
                break;

            case MUTTER_AUS_DATENBANK_UEBERNEHMEN:
                schueler.setMutter(mutterFoundInDatabase);
                if (isRechnungsempfaengerMutter) {
                    schueler.setRechnungsempfaenger(mutterFoundInDatabase);
                }
                mutter = null;
                break;

            case MIT_BISHERIGER_MUTTER_WEITERFAHREN:
                break;

            case VATER_AUS_DATENBANK_UEBERNEHMEN:
                schueler.setVater(vaterFoundInDatabase);
                if (isRechnungsempfaengerVater) {
                    schueler.setRechnungsempfaenger(vaterFoundInDatabase);
                }
                vater = null;
                break;

            case MIT_BISHERIGEM_VATER_WEITERFAHREN:
                break;

            case RECHNUNGSEMPFAENGER_DRITTPERSON_AUS_DATENBANK_UEBERNEHMEN:
                schueler.setRechnungsempfaenger(rechnungsempfaengerDrittpersonFoundInDatabase);
                rechnungsempfaengerDrittperson = null;
                break;

            case MIT_BISHERIGEM_RECHNUNGSEMPFAENGER_DRITTPERSON_WEITERFAHREN:
                break;

            case SUMMARY_BESTAETIGT:
                break;

            default:
                throw new RuntimeException("Verhalten des Eintrittspunkts " + entry + " nicht definiert.");
        }
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public SchuelerErfassenSaveResult getResult() {
        return result;
    }

    public Schueler getSchueler() {
        return schueler;
    }

    public Result getResultCheckMutterBereitsInDatenbank() {
        return resultCheckMutterBereitsInDatenbank;
    }

    public Result getResultCheckVaterBereitsInDatenbank() {
        return resultCheckVaterBereitsInDatenbank;
    }

    public Result getResultCheckRechnungsempfaengerDrittpersonBereitsInDatenbank() {
        return resultCheckRechnungsempfaengerDrittpersonBereitsInDatenbank;
    }

    public Schueler getSchuelerFoundInDatabase() {
        return schuelerFoundInDatabase;
    }

    public Angehoeriger getMutterFoundInDatabase() {
        return mutterFoundInDatabase;
    }

    public List<Angehoeriger> getMutterFoundInDatabaseList() {
        return mutterFoundInDatabaseList;
    }

    public Angehoeriger getVaterFoundInDatabase() {
        return vaterFoundInDatabase;
    }

    public List<Angehoeriger> getVaterFoundInDatabaseList() {
        return vaterFoundInDatabaseList;
    }

    public Angehoeriger getRechnungsempfaengerDrittpersonFoundInDatabase() {
        return rechnungsempfaengerDrittpersonFoundInDatabase;
    }

    public List<Angehoeriger> getRechnungsempfaengerDrittpersonFoundInDatabaseList() {
        return rechnungsempfaengerDrittpersonFoundInDatabaseList;
    }

    public String getIdentischeAdressen() {
        return identischeAdressen;
    }

    public String getAbweichendeAdressen() {
        return abweichendeAdressen;
    }

    public List<Schueler> getAngemeldeteGeschwisterList() {
        return angemeldeteGeschwisterList;
    }

    public List<Schueler> getAndereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList() {
        return andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList;
    }

    public boolean isMutterNeu() {
        return isMutterNeu;
    }

    public boolean isVaterNeu() {
        return isVaterNeu;
    }

    public boolean isRechnungsempfaengerDrittpersonNeu() {
        return isRechnungsempfaengerDrittpersonNeu;
    }

}

