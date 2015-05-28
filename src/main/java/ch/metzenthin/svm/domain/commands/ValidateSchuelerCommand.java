package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.AngehoerigerDao;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Hans Stamm
 */
public class ValidateSchuelerCommand extends GenericDaoCommand {

    private Schueler schueler;
    private Angehoeriger mutter;
    private boolean mutterIsRechnungsempfaenger;
    private Angehoeriger vater;
    private boolean vaterIsRechnungsempfaenger;
    private Angehoeriger rechnungsempfaengerDrittperson;

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
        switch (checkSchuelerBereitsInDatenbankCommand.getResult()) {
            case EIN_EINTRAG_PASST:
                // GUI + Abbruch
                break;
            case EIN_EINTRAG_PASST_TEILWEISE:
                // GUI + Abbruch
                break;
            case MEHRERE_EINTRAEGE_PASSEN_TEILWEISE:
                // GUI + Abbruch
                break;
        }


    // 2. Rechnungsempfänger Drittperson identisch? (-> CheckIfRechnungsempfaengerDrittpersonIdenticalWithMutterOrVaterCommand)
    // ************************************************************************************************************************

    // Input:  Angehoerige
    // Output: boolean result;
    //         String message;   siehe unten


    // if (!skipCheck...)

    // Command erzeugen
    // command.setEntityManager(em) //eigenen Em übergeben
    // command.execute()    // nicht mit Invoker aufrufen

    // ...execute()
    // if (....command.result) {
    //    GUI-Model erzeugen und mit Output befüllen (u.a. this, d.h. SchuelerValidator-Instanz und em)
    //    GUI aufrufen
    //    return;
    // else
    //    skip...= true


    // 3. Angehörige bereits in DB? (-> CheckIfAngehoerigerAlreadyinDatabaseCommand)
    // *****************************************************************************

    // 3.a Mutter
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


    // 4. Adressen identisch? (-> CheckIfAdressenAreIdentical)
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
}

