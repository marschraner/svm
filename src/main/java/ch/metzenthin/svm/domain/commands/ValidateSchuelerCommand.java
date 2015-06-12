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
        },
        SPEICHERUNG_ERFOLGREICH,
        UNERWARTETER_FEHLER;

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

    public enum AngehoerigenArt {
        MUTTER("Mutter"),
        VATER("Vater"),
        RECHNUNGSEMPFAENGER_DRITTPERSON("Rechnungsempfänger Drittperson");

        private String name;

        AngehoerigenArt(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
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
    private Angehoeriger mutterFoundInDatabase;
    private Angehoeriger vaterFoundInDatabase;
    private Angehoeriger rechnungsempfaengerDrittpersonFoundInDatabase;
    private boolean isMutterNeu;
    private boolean isVaterNeu;
    private boolean isRechnungsempfaengerDrittpersonNeu;

    // skip-Variablen
    private boolean skipMutterVaterDrittpersonAusGuiUebernehmen = false;
    private boolean skipCheckSchuelerBereitsInDatenbank = false;
    private boolean skipCheckMutterBereitsInDatenbank = false;
    private boolean skipCheckVaterBereitsInDatenbank = false;
    private boolean skipCheckRechungsempfaengerDrittpersonBereitsInDatenbank = false;
    private boolean skipPrepareSummary = false;

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
            if (mutter != null && !mutter.isEmpty()) {
                schueler.setMutter(mutter);
                if (adresseMutter != null && !adresseMutter.isEmpty()) {
                    mutter.setAdresse(adresseMutter);
                }
                if (isRechnungsempfaengerMutter) {
                    schueler.setRechnungsempfaenger(mutter);
                }
            }
            if (vater != null && !vater.isEmpty()) {
                schueler.setVater(vater);
                if (adresseVater != null && !adresseVater.isEmpty()) {
                    vater.setAdresse(adresseVater);
                }
                if (isRechnungsempfaengerVater) {
                    schueler.setRechnungsempfaenger(vater);
                }
            }
            if (!isRechnungsempfaengerMutter && !isRechnungsempfaengerVater) {
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
                Schueler schuelerFoundInDatabase = checkSchuelerBereitsInDatenbankCommand.getSchuelerFound();
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
                    isMutterNeu = true;
                    break;
                case EIN_EINTRAG_PASST:
                    mutterFoundInDatabase = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
                    result = new AngehoerigerEinEintragPasstResult(mutterFoundInDatabase, AngehoerigenArt.MUTTER, Result.MUTTER_EIN_EINTRAG_PASST);
                    return;
                case MEHRERE_EINTRAEGE_PASSEN:
                    result = new AngehoerigerMehrereEintraegePassenResult(checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList(), AngehoerigenArt.MUTTER, Result.MUTTER_MEHRERE_EINTRAEGE_PASSEN);
                    return;
                case EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE:
                    mutterFoundInDatabase = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
                    result = new AngehoerigerEinEintragGleicherNameAndereAttributeResult(mutterFoundInDatabase, AngehoerigenArt.MUTTER, Result.MUTTER_EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE);
                    return;
                case MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE:
                    result = new AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult(checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList(), AngehoerigenArt.MUTTER, Result.MUTTER_MEHRERE_EINTAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE);
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
                    isVaterNeu = true;
                    break;
                case EIN_EINTRAG_PASST:
                    vaterFoundInDatabase = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
                    result = new AngehoerigerEinEintragPasstResult(vaterFoundInDatabase, AngehoerigenArt.VATER, Result.VATER_EIN_EINTRAG_PASST);
                    return;
                case MEHRERE_EINTRAEGE_PASSEN:
                    result = new AngehoerigerMehrereEintraegePassenResult(checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList(), AngehoerigenArt.VATER, Result.VATER_MEHRERE_EINTRAEGE_PASSEN);
                    return;
                case EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE:
                    vaterFoundInDatabase = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
                    result = new AngehoerigerEinEintragGleicherNameAndereAttributeResult(vaterFoundInDatabase, AngehoerigenArt.VATER, Result.VATER_EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE);
                    return;
                case MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE:
                    result = new AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult(checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList(), AngehoerigenArt.VATER, Result.VATER_MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE);
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
                    isRechnungsempfaengerDrittpersonNeu = true;
                    break;
                case EIN_EINTRAG_PASST:
                    rechnungsempfaengerDrittpersonFoundInDatabase = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
                    result = new AngehoerigerEinEintragPasstResult(rechnungsempfaengerDrittpersonFoundInDatabase, AngehoerigenArt.RECHNUNGSEMPFAENGER_DRITTPERSON, Result.RECHNUNGSEMPFAENGER_DRITTPERSON_EIN_EINTRAG_PASST);
                    return;
                case MEHRERE_EINTRAEGE_PASSEN:
                    result = new AngehoerigerMehrereEintraegePassenResult(checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList(), AngehoerigenArt.RECHNUNGSEMPFAENGER_DRITTPERSON, Result.RECHNUNGSEMPFAENGER_DRITTPERSON_MEHRERE_EINTRAEGE_PASSEN);
                    return;
                case EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE:
                    rechnungsempfaengerDrittpersonFoundInDatabase = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
                    result = new AngehoerigerEinEintragGleicherNameAndereAttributeResult(rechnungsempfaengerDrittpersonFoundInDatabase, AngehoerigenArt.RECHNUNGSEMPFAENGER_DRITTPERSON, Result.RECHNUNGSEMPFAENGER_DRITTPERSON_EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE);
                    return;
                case MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE:
                    result = new AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult(checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList(), AngehoerigenArt.RECHNUNGSEMPFAENGER_DRITTPERSON, Result.RECHNUNGSEMPFAENGER_DRITTPERSON_MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE);
                    return;
            }
        }

        if (!skipPrepareSummary) {
            skipPrepareSummary = true;
            // 4. Identische Adressen?
            CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
            checkIdentischeAdressenCommand.execute();
            String identischeAdressen = checkIdentischeAdressenCommand.getIdentischeAdressen();
            String abweichendeAdressen = checkIdentischeAdressenCommand.getAbweichendeAdressen();

            // 5. Nach Geschwistern suchen
            CheckGeschwisterSchuelerRechnungempfaengerCommand checkGeschwisterSchuelerRechnungempfaengerCommand = new CheckGeschwisterSchuelerRechnungempfaengerCommand(schueler);
            checkGeschwisterSchuelerRechnungempfaengerCommand.execute();
            List<Schueler> angemeldeteGeschwisterList = checkGeschwisterSchuelerRechnungempfaengerCommand.getAngemeldeteGeschwisterList();
            List<Schueler> andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList = checkGeschwisterSchuelerRechnungempfaengerCommand.getAndereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList();
            result = new ValidateSchuelerSummaryResult(schueler, isRechnungsempfaengerMutter, isRechnungsempfaengerVater, angemeldeteGeschwisterList, andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList, identischeAdressen, abweichendeAdressen, isMutterNeu, isVaterNeu, isRechnungsempfaengerDrittpersonNeu);
            return;   // -> Summary-Dialog
        }

        // 6. Schüler speichern
        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler);
        saveSchuelerCommand.setEntityManager(entityManager);
        saveSchuelerCommand.execute();
        result = new SchuelerErfassenSaveOkResult(Result.SPEICHERUNG_ERFOLGREICH, schueler.getGeschlecht());

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

}

