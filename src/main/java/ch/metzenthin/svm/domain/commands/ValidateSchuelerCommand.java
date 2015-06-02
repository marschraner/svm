package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.List;

/**
 * @author Hans Stamm
 */
public class ValidateSchuelerCommand extends GenericDaoCommand {

    enum Result {
        SCHUELER_IN_DATENBANK,
        MUTTER_NICHT_IN_DATENBANK,
        MUTTER_EIN_EINTRAG_PASST,
        MUTTER_MEHRERE_EINTRAEGE_PASSEN,
        MUTTER_EIN_EINTRAG_PASST_TEILWEISE,
        MUTTER_MEHRERE_EINTRAEGE_PASSEN_TEILWEISE,
        CHECK_IDENTISCHE_ADRESSEN_COMMAND_FINISHED,
        CHECK_IDENTIFY_GESCHWISTER_COMMAND_FINISHED
    }

    enum Proceed {
        MUTTER_NEU_ERFASSEN,
        MUTTER_AUS_DATENBANK_UEBERNEHMEN
    }

    // input
    private Schueler schueler;
    private Angehoeriger mutter;
    private boolean mutterIsRechnungsempfaenger;
    private Angehoeriger vater;
    private boolean vaterIsRechnungsempfaenger;
    private Angehoeriger rechnungsempfaengerDrittperson;



    private Proceed proceed;

    private boolean skipCheckMutterBereitsInDatenbank = false;

    // output:
    private Result result;
    private Angehoeriger mutterFound;
    private List<Angehoeriger> mutterFoundList;
    private String infoIdentischeAdressen = "";
    private String infoAbweichendeAdressen = "";
    private String infoGeschwister;
    private String infoSchuelerRechnungsempfaenger;

    public ValidateSchuelerCommand(Schueler schueler, Angehoeriger mutter, boolean mutterIsRechnungsempfaenger, Angehoeriger vater, boolean vaterIsRechnungsempfaenger, Angehoeriger rechnungsempfaengerDrittperson) {
        this.schueler = schueler;
        this.mutter = mutter;
        this.mutterIsRechnungsempfaenger = mutterIsRechnungsempfaenger;
        this.vater = vater;
        this.vaterIsRechnungsempfaenger = vaterIsRechnungsempfaenger;
        this.rechnungsempfaengerDrittperson = rechnungsempfaengerDrittperson;
    }

    // Rechnungsempfänger identisch mit...
    // Separater Rechnungsempfänger
    // Adressen von .... identisch
    // Adressen von ... verschieden
    // Vater, Mutter, ... bereits in Datenbank vorhanden
    // Vater, .... wird neu angelegt
    // In DB erfasste Geschwister gefunden: ...
    // Keine andere Geschwister erfasst.


    @Override
    public void execute() {

        howToProceed();

        if (mutter != null) {
            schueler.setMutter(mutter);
            if (mutterIsRechnungsempfaenger) {
                schueler.setRechnungsempfaenger(mutter);
            }
        }

        if (vater != null) {
            schueler.setVater(vater);
            if (vaterIsRechnungsempfaenger) {
                schueler.setRechnungsempfaenger(vater);
            }
        }

        if (rechnungsempfaengerDrittperson != null) {
            schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson);
        }
        
// todo Wenn Attribute null sind, gibt es NullPointerException in den Vergleichen (...isIdentical...)
    //
    // !!! NICHT durch commandInvoker aufrufen, weil immer derselbe EntityManager verwendet werden soll
    // Aufruf:
    // (0. Konstruktor)
    // (1. SchuelerValidatorCommand.setEntityManager)


    // Instanzvariablen:

    // EntityManager entityMangager;
    // SchuelerModel schuelerModel
    // AngehoerigeModel mutterModel
    // AngehoerigeModel vaterModel
    // AngehoerigeModel rechnungsempfaengerDrittpersonModel
    // Schueler schueler
    // Angehoeriger mutter
    // Angehoeriger vater
    // Angehoeriger rechnungsempfaenger
    // skip.... = false;
    // skip...
    // ...
    //
    // Konstruktor:
    //
    // Argumente: EntityManagerFactory emf, SchuelerModel schuelerModel, AngehoerigerModel mutterModel, AngehoerigerModel vaterModel, AngehoerigerModel rechnungsempfaengerDrittpersonModel
    // setzt EntityManager und

    // Methoden:

    // execute() //aufgerufen bei onValidateClicked und untenstehende GUIs

    // setAngehoeriger()   // um diese von Gui aus zu aktualisieren
    // setSchueler()
    //
    // ruft 1.-6. auf (mit <commandName>.execute(), NICHT vom Invoker


    // 1. Schüler schon in DB? (-> CheckIfSchuelerAlreadyinDatabaseCommand extends GenericDaoCommand)
    // ********************************************************************
    //
    // if (!skipCheckIfSchuelerAlreadyinDatabase)...  -> SchuelerModelImpl)

    // Command erzeugen
    // command.setEntityManager(em) //eigenen Em übergeben
    // command.execute()    // nicht mit Invoker aufrufen

    // analoge Checks wie unten

    // Input:  Schueler
    // Output: DatabaseSearchResult result;
    //         String message;

    // falls !no-entry_fits
    //    em close
    //    show Dialog
    //    return;
    // else
    //    skipCheckIfSchuelerAlreadyinDatabase = true;
    //
        CheckSchuelerBereitsInDatenbankCommand checkSchuelerBereitsInDatenbankCommand = new CheckSchuelerBereitsInDatenbankCommand(schueler);
        checkSchuelerBereitsInDatenbankCommand.setEntityManager(entityManager);
        checkSchuelerBereitsInDatenbankCommand.execute();

        // Bereits in DB
        if (checkSchuelerBereitsInDatenbankCommand.isInDatenbank()) {
            result = Result.SCHUELER_IN_DATENBANK;
            schueler = checkSchuelerBereitsInDatenbankCommand.getSchuelerFound();
            return;
        }



    // 2. Angehörige bereits in DB? (-> CheckIfAngehoerigerAlreadyinDatabaseCommand)
    // *****************************************************************************

    // 2.a Mutter

        if (!skipCheckMutterBereitsInDatenbank) {
            CheckAngehoerigerBereitsInDatenbankCommand checkAngehoerigerBereitsInDatenbankCommand = new CheckAngehoerigerBereitsInDatenbankCommand(mutter);
            checkSchuelerBereitsInDatenbankCommand.setEntityManager(entityManager);
            checkAngehoerigerBereitsInDatenbankCommand.execute();

            switch (checkAngehoerigerBereitsInDatenbankCommand.getResult()) {
                case NICHT_IN_DATENBANK:
                    mutterFound = null;
                    mutterFoundList = null;
                    result = Result.MUTTER_NICHT_IN_DATENBANK;
                    break;
                case EIN_EINTRAG_PASST:
                    result = Result.MUTTER_EIN_EINTRAG_PASST;
                    mutterFound = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
                    mutterFoundList = null;
                    break;
                case MEHRERE_EINTRAEGE_PASSEN:
                    result = Result.MUTTER_MEHRERE_EINTRAEGE_PASSEN;
                    mutterFound = null;
                    mutterFoundList = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList();
                    break;
                case EIN_EINTRAG_PASST_TEILWEISE:
                    result = Result.MUTTER_EIN_EINTRAG_PASST_TEILWEISE;
                    mutterFound = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
                    mutterFoundList = null;
                    break;
                case MEHRERE_EINTRAEGE_PASSEN_TEILWEISE:
                    result = Result.MUTTER_MEHRERE_EINTRAEGE_PASSEN_TEILWEISE;
                    mutterFound = null;
                    mutterFoundList = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList();
                    break;
            }
        }


        // 3.b Vater
    // 3.c Drittperson

    // Input:  Angehoerige
    // Output: DatabaseSearchResult result;
    //         String message;
    //         Angehoeriger angehoerigerAnswer1;
    //         Angehoeriger angehoerigerAnswer2;
    //         Angehoeriger angehoerigerAnswer3;

    // if (!skipCheckIfMutterAlreadyinDatabase) {

    // Command erzeugen
    // command.setEntityManager(em) //eigenen Em übergeben
    // command.execute()    // nicht mit Invoker aufrufen

    //    CheckIfAngehoerigerAlreadyinDatabaseCommand checkIfMutterAlreadyInDatabaseCommand = new CheckIfAngehoerigerAlreadyinDatabaseCommand(mutter);
    //
    //    GUI-Model erzeugen und mit Output befüllen (u.a. this, d.h. SchuelerValidator-Instanz)
    //    In jedem Fall eines der GUIs aufgerufen
    // }

    // ditto für Vater und Rechnungsempfänger Drittperson

    // TITEL: Mutter bereits in der Datenbank erfasst? (vgl. Ubuntu-Buch S.47)
    // Mögliche Resultate (Key / Value) und Reaktionen im GUI:
    // MULTIPLE_ENTRIES_FIT_PARTIALLY: In der Datenbank wurden mehrere Einträge gefunden, die mit den erfassten Angaben teilweise übereinstimmen: ...
    // - Keinen dieser Einträge verwenden und einen neuen Datenbank-Eintrag gemäss der erfassten Angaben erzeugen (-> mutter = ...command.getAngehoerigerAnswer1(); skipCheckIfMutterAlreadyinDatabase = True, Fenster schliessen und wieder onSaveClicked des GUI aufrufen)
    // - Zurück und Mutter genauer erfassen und/oder erfasste Einträge korrigieren (-> mutter = ...command.getAngehoerigerAnswer2(), skipCheckIfMutterAlreadyinDatabase = False und Fenster schliessen (-> Gui))
    // ONE_ENTRY_FITS_PARTIALLY: In der Datenbank wurde ein Eintrag gefunden, der mit den erfassten Angaben teilweise übereinstimmt: ...
    // - Diesen Eintrag verwenden und mit geänderten Angaben aktualisieren (angehoeriger = angehoerigerUpdated)
    // - Nicht diesen Eintrag verwenden und einen neuen Datenbank-Eintrag gemäss der erfassten Angaben erzeugen
    // - Erfasste Einträge korrigieren
    // ONE_ENTRY_FIT: In der Datenbank wurde ein Eintrag gefunden, der auf die erfassten Angaben passt: ...
    // - Diesen Eintrag verwenden (angehoeriger = angehoerigerUpdated)
    // - Erfasste Einträge korrigieren
    // MULTIPLE_ENTRIES_FIT: In der Datenbank wurden mehrere Eintäge gefunden, die auf die erfassten Angaben der Mutter passen: ...
    // Mutter muss genauer erfasst werden.
    // - Ok
    // NO_ENTRY_FITS;
    // Mutter wird neu erfasst (noch nicht in Datenbank)
    // - Ok
    // - Abbrechen


    // 3. Adressen identisch? (-> CheckIfAdressenAreIdentical)
    // *******************************************************

    // Input:  SchuelerModel, Angehoerige
    // Output: String message;
    //         Schueler schueler;

    // if (!skipCheck...)
    // GUI: zeigt Message an
    // - Zurück (skipCheckMutter,..Vater,.. Ange AlreadInDb =false)
    // - Weiter (skipCheckIfAdressenAreIdentical = true)


    // 5. gefundene Geschwister / Kinder des Rechnungsempfängers
    // *********

    // Input/Output: schueler
    // Output: boolean result (gefunden / nicht gefunden)
    //         String message

    // Gefunden:

    // zurück (skipCheckAdressen = false)
    // weiter (skipCheckIfAdressenAreIdentical = true)


    // 6. Zusammenfassung  weglassen!!!!!!!!!!!!!!!¨
    // ******************

    // - zurück / Beenden und speichern


    // 7. SaveCommand
    // ***************

    // em.getTransaction().begin()
    // em.persist(schueler)
    // em.getTransaction().commit
    // em.close();

    // catch {
    // rollback und Entity Manager schliessen


    }

    private void howToProceed() {
        if (proceed == null) {
            return;
        }

        switch (proceed) {
            case MUTTER_NEU_ERFASSEN:
                skipCheckMutterBereitsInDatenbank = true;
                break;
            case MUTTER_AUS_DATENBANK_UEBERNEHMEN:
                skipCheckMutterBereitsInDatenbank = true;
                schueler.setMutter(mutterFound);
                mutter = null;
                break;
        }
    }

    public void setProceed(Proceed proceed) {
        this.proceed = proceed;
    }
}

