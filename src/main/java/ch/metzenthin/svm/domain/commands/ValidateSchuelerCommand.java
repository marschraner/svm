package ch.metzenthin.svm.domain.commands;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.domain.model.*;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;

/**
 * @author Hans Stamm
 */
public class ValidateSchuelerCommand implements Command {

  public enum Result {
    SCHUELER_BEREITS_IN_DATENBANK,
    DRITTPERSON_IDENTISCH_MIT_ELTERNTEIL,
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
    GESCHWISTER_OHNE_WUENSCHT_EMAILS,
    CHECK_GESCHWISTER_SCHUELER_RECHNUGSEMFPAENGER_COMMAND_FINISHED {
      @Override
      public Entry proceedWeiterfahren() {
        return Entry.SUMMARY_BESTAETIGT;
      }
    },
    SPEICHERUNG_ERFOLGREICH,
    ABBRECHEN,
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

    private final String name;

    AngehoerigenArt(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  private final DB db = DBFactory.getInstance();

  private final Schueler schueler;
  private final Adresse adresseSchueler;
  private final Anmeldung anmeldung;
  private Angehoeriger mutter;
  private final Adresse adresseMutter;
  private final boolean isRechnungsempfaengerMutter;
  private Angehoeriger vater;
  private final Adresse adresseVater;
  private final boolean isRechnungsempfaengerVater;
  private Angehoeriger rechnungsempfaengerDrittperson;
  private final Adresse adresseRechnungsempfaengerDrittperson;
  private Entry entry;

  // Originaler Schüler von Datenbank (Schüler bearbeiten). Wenn null, isBearbeiten() returns true
  private final Schueler schuelerOrigin;
  private final Angehoeriger mutterOrigin;
  private final Angehoeriger vaterOrigin;
  private final Angehoeriger rechnungsempfaengerOrigin;

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
  private boolean skipCheckRechungsempfaengerDrittpersonIdentischMitElternteil = false;
  private boolean skipCheckSchuelerBereitsInDatenbank = false;
  private boolean skipCheckMutterBereitsInDatenbank = false;
  private boolean skipCheckVaterBereitsInDatenbank = false;
  private boolean skipCheckRechungsempfaengerDrittpersonBereitsInDatenbank = false;
  private boolean skipPrepareSummary = false;

  public ValidateSchuelerCommand(ValidateSchuelerModel validateSchuelerModel) {
    this.schueler = validateSchuelerModel.getSchueler();
    this.adresseSchueler = validateSchuelerModel.getAdresseSchueler();
    this.anmeldung = validateSchuelerModel.getAnmeldung();
    this.mutter = validateSchuelerModel.getMutter();
    this.adresseMutter = validateSchuelerModel.getAdresseMutter();
    this.isRechnungsempfaengerMutter = validateSchuelerModel.isRechnungsempfaengerMutter();
    this.vater = validateSchuelerModel.getVater();
    this.adresseVater = validateSchuelerModel.getAdresseVater();
    this.isRechnungsempfaengerVater = validateSchuelerModel.isRechnungsempfaengerVater();
    this.rechnungsempfaengerDrittperson = validateSchuelerModel.getRechnungsempfaengerDrittperson();
    this.adresseRechnungsempfaengerDrittperson =
        validateSchuelerModel.getAdresseRechnungsempfaengerDrittperson();
    this.schuelerOrigin = validateSchuelerModel.getSchuelerOrigin();
    this.mutterOrigin = (this.schuelerOrigin == null ? null : this.schuelerOrigin.getMutter());
    this.vaterOrigin = (this.schuelerOrigin == null ? null : this.schuelerOrigin.getVater());
    this.rechnungsempfaengerOrigin =
        (this.schuelerOrigin == null ? null : this.schuelerOrigin.getRechnungsempfaenger());
  }

  private boolean isBearbeiten() {
    return schuelerOrigin != null;
  }

  @Override
  @SuppressWarnings({"DuplicatedCode", "ExtractMethodRecommender", "java:S3776", "java:S6541"})
  public void execute() {

    determineHowToProceed();

    // 1. Mutter, Vater und Drittperson aus GUI übernehmen
    if (!skipMutterVaterDrittpersonAusGuiUebernehmen) {
      skipMutterVaterDrittpersonAusGuiUebernehmen = true;
      schueler.setAdresse(adresseSchueler);
      resetAnmeldung(); // Falls der Schüler bereits in einem vorangehenden, abgebrochenen Command
      // verwendet wurde
      schueler.addAnmeldung(anmeldung);
      if (mutter != null && !mutter.isEmpty()) {
        schueler.setMutter(mutter);
        if (adresseMutter != null && !adresseMutter.isEmpty()) {
          mutter.setAdresse(adresseMutter);
        }
        if (isRechnungsempfaengerMutter) {
          schueler.setRechnungsempfaenger(mutter);
        }
        if (!checkNotEmpty(mutter.getEmail())) {
          mutter.setWuenschtEmails(null);
        } else if (mutter.getWuenschtEmails() == null) {
          mutter.setWuenschtEmails(false);
        }
      } else {
        schueler.setMutter(
            null); // Falls der Schüler bereits in einem vorangehenden, abgebrochenen Command
        // verwendet wurde
      }
      if (vater != null && !vater.isEmpty()) {
        schueler.setVater(vater);
        if (adresseVater != null && !adresseVater.isEmpty()) {
          vater.setAdresse(adresseVater);
        }
        if (isRechnungsempfaengerVater) {
          schueler.setRechnungsempfaenger(vater);
        }
        if (!checkNotEmpty(vater.getEmail())) {
          vater.setWuenschtEmails(null);
        } else if (vater.getWuenschtEmails() == null) {
          vater.setWuenschtEmails(false);
        }
      } else {
        schueler.setVater(
            null); // Falls der Schüler bereits in einem vorangehenden, abgebrochenen Command
        // verwendet wurde
      }
      if (isRechnungsempfaengerDrittperson()) {
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson);
        rechnungsempfaengerDrittperson.setAdresse(adresseRechnungsempfaengerDrittperson);
        rechnungsempfaengerDrittperson.setWuenschtEmails(null);
      }
    }

    // 2. Schüler bereits in Datenbank?
    if (!skipCheckSchuelerBereitsInDatenbank) {
      skipCheckSchuelerBereitsInDatenbank = true;
      CheckSchuelerBereitsInDatenbankCommand checkSchuelerBereitsInDatenbankCommand =
          new CheckSchuelerBereitsInDatenbankCommand(schueler);
      checkSchuelerBereitsInDatenbankCommand.execute();
      Schueler schuelerFoundInDatabase =
          checkSchuelerBereitsInDatenbankCommand.getSchuelerFound(schuelerOrigin);
      if (schuelerFoundInDatabase != null) {
        result = new SchuelerBereitsInDatenbankResult(schuelerFoundInDatabase);
        return;
      }
    }

    // 3. Rechnungsempfänger Drittperson identisch mit Mutter oder Vater?
    if (isRechnungsempfaengerDrittperson()
        && !skipCheckRechungsempfaengerDrittpersonIdentischMitElternteil) {
      skipCheckRechungsempfaengerDrittpersonIdentischMitElternteil = true;
      CheckDrittpersonIdentischMitElternteilCommand checkDrittpersonIdentischMitElternteilCommand =
          new CheckDrittpersonIdentischMitElternteilCommand(
              schueler.getMutter(), schueler.getVater(), schueler.getRechnungsempfaenger());
      checkDrittpersonIdentischMitElternteilCommand.execute();
      if (checkDrittpersonIdentischMitElternteilCommand.isIdentical()) {
        result =
            new DrittpersonIdentischMitElternteilResult(
                checkDrittpersonIdentischMitElternteilCommand.getErrorMessage());
        return;
      }
    }

    // 4.a Mutter bereits in Datenbank?
    if ((schueler.getMutter() != null)
        && (!isBearbeiten() || !schueler.getMutter().isIdenticalWith(schuelerOrigin.getMutter()))
        && !skipCheckMutterBereitsInDatenbank) {
      skipCheckMutterBereitsInDatenbank = true;
      CheckAngehoerigerBereitsInDatenbankCommand checkAngehoerigerBereitsInDatenbankCommand =
          new CheckAngehoerigerBereitsInDatenbankCommand(
              schueler.getMutter(), (isBearbeiten()) ? schuelerOrigin.getMutter() : null);
      checkAngehoerigerBereitsInDatenbankCommand.execute();
      switch (checkAngehoerigerBereitsInDatenbankCommand.getResult()) {
        case EINTRAG_WIRD_MUTIERT, VORNAME_NACHNAME_FEHLEN -> {
          // Nothing to do
        }
        case NICHT_IN_DATENBANK -> isMutterNeu = true;
        case EIN_EINTRAG_PASST -> {
          mutterFoundInDatabase = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
          result =
              new AngehoerigerEinEintragPasstResult(
                  mutterFoundInDatabase, AngehoerigenArt.MUTTER, Result.MUTTER_EIN_EINTRAG_PASST);
          return;
        }
        case MEHRERE_EINTRAEGE_PASSEN -> {
          result =
              new AngehoerigerMehrereEintraegePassenResult(
                  checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList(),
                  AngehoerigenArt.MUTTER,
                  Result.MUTTER_MEHRERE_EINTRAEGE_PASSEN);
          return;
        }
        case EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE -> {
          mutterFoundInDatabase = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
          result =
              new AngehoerigerEinEintragGleicherNameAndereAttributeResult(
                  mutterFoundInDatabase,
                  AngehoerigenArt.MUTTER,
                  Result.MUTTER_EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE);
          return;
        }
        case MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE -> {
          result =
              new AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult(
                  checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList(),
                  AngehoerigenArt.MUTTER,
                  Result.MUTTER_MEHRERE_EINTAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE);
          return;
        }
      }
    }

    // 4.b Vater bereits in Datenbank?
    if ((schueler.getVater() != null)
        && (!isBearbeiten() || !schueler.getVater().isIdenticalWith(schuelerOrigin.getVater()))
        && !skipCheckVaterBereitsInDatenbank) {
      skipCheckVaterBereitsInDatenbank = true;
      CheckAngehoerigerBereitsInDatenbankCommand checkAngehoerigerBereitsInDatenbankCommand =
          new CheckAngehoerigerBereitsInDatenbankCommand(
              schueler.getVater(), (isBearbeiten()) ? schuelerOrigin.getVater() : null);
      checkAngehoerigerBereitsInDatenbankCommand.execute();
      switch (checkAngehoerigerBereitsInDatenbankCommand.getResult()) {
        case EINTRAG_WIRD_MUTIERT, VORNAME_NACHNAME_FEHLEN -> {
          // Nothing to do
        }
        case NICHT_IN_DATENBANK -> isVaterNeu = true;
        case EIN_EINTRAG_PASST -> {
          vaterFoundInDatabase = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
          result =
              new AngehoerigerEinEintragPasstResult(
                  vaterFoundInDatabase, AngehoerigenArt.VATER, Result.VATER_EIN_EINTRAG_PASST);
          return;
        }
        case MEHRERE_EINTRAEGE_PASSEN -> {
          result =
              new AngehoerigerMehrereEintraegePassenResult(
                  checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList(),
                  AngehoerigenArt.VATER,
                  Result.VATER_MEHRERE_EINTRAEGE_PASSEN);
          return;
        }
        case EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE -> {
          vaterFoundInDatabase = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
          result =
              new AngehoerigerEinEintragGleicherNameAndereAttributeResult(
                  vaterFoundInDatabase,
                  AngehoerigenArt.VATER,
                  Result.VATER_EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE);
          return;
        }
        case MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE -> {
          result =
              new AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult(
                  checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList(),
                  AngehoerigenArt.VATER,
                  Result.VATER_MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE);
          return;
        }
      }
    }

    // 4.c Rechnungsempfänger Drittperson bereits in Datenbank?
    if (isRechnungsempfaengerDrittperson()
        && (!isBearbeiten()
            || !schueler
                .getRechnungsempfaenger()
                .isIdenticalWith(schuelerOrigin.getRechnungsempfaenger()))
        && !skipCheckRechungsempfaengerDrittpersonBereitsInDatenbank) {
      skipCheckRechungsempfaengerDrittpersonBereitsInDatenbank = true;
      CheckAngehoerigerBereitsInDatenbankCommand checkAngehoerigerBereitsInDatenbankCommand =
          new CheckAngehoerigerBereitsInDatenbankCommand(
              schueler.getRechnungsempfaenger(),
              (isBearbeiten()) ? schuelerOrigin.getRechnungsempfaenger() : null);
      checkAngehoerigerBereitsInDatenbankCommand.execute();
      switch (checkAngehoerigerBereitsInDatenbankCommand.getResult()) {
        case EINTRAG_WIRD_MUTIERT, VORNAME_NACHNAME_FEHLEN -> {
          // Nothing to do
        }
        case NICHT_IN_DATENBANK -> isRechnungsempfaengerDrittpersonNeu = true;
        case EIN_EINTRAG_PASST -> {
          rechnungsempfaengerDrittpersonFoundInDatabase =
              checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
          result =
              new AngehoerigerEinEintragPasstResult(
                  rechnungsempfaengerDrittpersonFoundInDatabase,
                  AngehoerigenArt.RECHNUNGSEMPFAENGER_DRITTPERSON,
                  Result.RECHNUNGSEMPFAENGER_DRITTPERSON_EIN_EINTRAG_PASST);
          return;
        }
        case MEHRERE_EINTRAEGE_PASSEN -> {
          result =
              new AngehoerigerMehrereEintraegePassenResult(
                  checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList(),
                  AngehoerigenArt.RECHNUNGSEMPFAENGER_DRITTPERSON,
                  Result.RECHNUNGSEMPFAENGER_DRITTPERSON_MEHRERE_EINTRAEGE_PASSEN);
          return;
        }
        case EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE -> {
          rechnungsempfaengerDrittpersonFoundInDatabase =
              checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
          result =
              new AngehoerigerEinEintragGleicherNameAndereAttributeResult(
                  rechnungsempfaengerDrittpersonFoundInDatabase,
                  AngehoerigenArt.RECHNUNGSEMPFAENGER_DRITTPERSON,
                  Result
                      .RECHNUNGSEMPFAENGER_DRITTPERSON_EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE);
          return;
        }
        case MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE -> {
          result =
              new AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult(
                  checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList(),
                  AngehoerigenArt.RECHNUNGSEMPFAENGER_DRITTPERSON,
                  Result
                      .RECHNUNGSEMPFAENGER_DRITTPERSON_MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE);
          return;
        }
      }
    }

    // 5. Nach Geschwistern suchen (für 9., 10. und 12. benötigt)
    CheckGeschwisterSchuelerRechnungempfaengerCommand
        checkGeschwisterSchuelerRechnungempfaengerCommand =
            new CheckGeschwisterSchuelerRechnungempfaengerCommand(
                (isBearbeiten() ? schuelerOrigin : schueler),
                mutterFoundInDatabase,
                vaterFoundInDatabase,
                rechnungsempfaengerDrittpersonFoundInDatabase,
                isRechnungsempfaengerDrittperson());
    checkGeschwisterSchuelerRechnungempfaengerCommand.execute();
    List<Schueler> geschwisterList =
        checkGeschwisterSchuelerRechnungempfaengerCommand.getGeschwisterList();

    // 6. Ist für Eltern des Geschwisters "wünscht E-Mails" noch gesetzt mit aktuellen Änderungen?
    //    (nur relevant, falls Geschwister nur einen Elternteil gemeinsam haben)
    for (Schueler geschwister : geschwisterList) {
      boolean gemeinsameMutterOderBeideOhneMutter =
          hasGeschwisterGemeinsameMutterOderBeideOhneMutter(geschwister);
      boolean gemeinsamenVaterOderBeideOhneVater =
          hasGeschwisterGemeinsamenVaterOderBeideOhneVater(geschwister);
      if (doElternOfGeschwisterHaveEmailAfterCurrentModifications(
              geschwister, gemeinsameMutterOderBeideOhneMutter, gemeinsamenVaterOderBeideOhneVater)
          && !isForElternOfGeschwisterWuenschtEmailsSetAfterCurrentModifications(
              geschwister,
              gemeinsameMutterOderBeideOhneMutter,
              gemeinsamenVaterOderBeideOhneVater)) {
        result =
            new GeschwisterOhneWuenschtEmailsResult(
                geschwister, gemeinsameMutterOderBeideOhneMutter);
        return;
      }
    }

    // 7. Warnung, falls Anrede für Mutter Herr oder Anrede für Vater Frau
    //    oder kein E-Mail für Eltern erfasst
    if (!skipPrepareSummary) {
      skipPrepareSummary = true;

      // 7.a Anrede Mutter ist Herr
      if (mutter != null && mutter.getAnrede() == Anrede.HERR) {
        Object[] options = {"Anrede übernehmen", "Anrede korrigieren"};
        int n =
            JOptionPane.showOptionDialog(
                null,
                "Die Mutter hat als Anrede \"Herr\".",
                "Mutter mit Anrede \"Herr\"",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options, // the titles of buttons
                options[1]); // default button title
        if (n == 1) {
          // Abbruch
          result = new AbbrechenResult();
          return;
        }
      }

      // 7.b Anrede Vater ist Frau
      if (vater != null && vater.getAnrede() == Anrede.FRAU) {
        Object[] options = {"Anrede übernehmen", "Anrede korrigieren"};
        int n =
            JOptionPane.showOptionDialog(
                null,
                "Der Vater hat als Anrede \"Frau\".",
                "Vater mit Anrede \"Frau\"",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options, // the titles of buttons
                options[1]); // default button title
        if (n == 1) {
          // Abbruch
          result = new AbbrechenResult();
          return;
        }
      }

      // 7.c Keine E-Mail erfasst
      if ((mutter != null
              && vater != null
              && !checkNotEmpty(mutter.getEmail())
              && !checkNotEmpty(vater.getEmail()))
          || (mutter != null && vater == null && !checkNotEmpty(mutter.getEmail()))
          || (vater != null && mutter == null && !checkNotEmpty(vater.getEmail()))) {
        Object[] options = {"Eltern ohne E-Mail erfassen", "Eingabe korrigieren"};
        int n =
            JOptionPane.showOptionDialog(
                null,
                "Weder für die Mutter noch den Vater wurde eine E-Mail erfasst.",
                "Warnung: Keine E-Mail für Eltern erfasst",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options, // the titles of buttons
                options[1]); // default button title
        if (n == 1) {
          // Abbruch
          result = new AbbrechenResult();
          return;
        }
      }

      // 7.d Keine E-Mail für Geschwister mit aktuellen Änderungen
      //     (nur relevant, falls Geschwister nur einen Elternteil gemeinsam haben)
      for (Schueler geschwister : geschwisterList) {
        boolean gemeinsameMutterOderBeideOhneMutter =
            hasGeschwisterGemeinsameMutterOderBeideOhneMutter(geschwister);
        boolean gemeinsamenVaterOderBeideOhneVater =
            hasGeschwisterGemeinsamenVaterOderBeideOhneVater(geschwister);
        if (!doElternOfGeschwisterHaveEmailAfterCurrentModifications(
            geschwister, gemeinsameMutterOderBeideOhneMutter, gemeinsamenVaterOderBeideOhneVater)) {
          String mutterOderVater =
              (gemeinsameMutterOderBeideOhneMutter) ? "die Mutter" : "den Vater";
          String message =
              "Bitte E-Mail für "
                  + mutterOderVater
                  + " erfassen!\n\n"
                  + "Wird keine E-Mail erfasst, hat dies zur Folge, dass für das "
                  + "Geschwister "
                  + geschwister.getVorname()
                  + " "
                  + geschwister.getNachname()
                  + "\nfür keinen Elternteil eine E-Mail "
                  + "erfasst ist.";
          String emailErfassen =
              (gemeinsameMutterOderBeideOhneMutter)
                  ? "Email für Mutter erfassen"
                  : "Email für Vater erfassen";
          Object[] options = {"Warnung ignorieren", emailErfassen};
          int n =
              JOptionPane.showOptionDialog(
                  null,
                  message,
                  "Warnung",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.WARNING_MESSAGE,
                  null,
                  options, // the titles of buttons
                  options[1]); // default button title
          if (n == 1) {
            // Abbruch
            result = new AbbrechenResult();
            return;
          }
        }
      }

      // 8. Identische Adressen?
      CheckIdentischeAdressenCommand checkIdentischeAdressenCommand =
          new CheckIdentischeAdressenCommand(
              schueler,
              mutterFoundInDatabase,
              vaterFoundInDatabase,
              rechnungsempfaengerDrittpersonFoundInDatabase,
              isRechnungsempfaengerDrittperson());
      checkIdentischeAdressenCommand.execute();
      String identischeAdressen = checkIdentischeAdressenCommand.getIdentischeAdressen();
      String abweichendeAdressen = checkIdentischeAdressenCommand.getAbweichendeAdressen();

      // 9. Geschwister
      List<Schueler> andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList =
          checkGeschwisterSchuelerRechnungempfaengerCommand
              .getAndereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList();
      result =
          new ValidateSchuelerSummaryResult(
              schueler,
              mutterFoundInDatabase,
              vaterFoundInDatabase,
              rechnungsempfaengerDrittpersonFoundInDatabase,
              isRechnungsempfaengerMutter,
              isRechnungsempfaengerVater,
              geschwisterList,
              andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList,
              identischeAdressen,
              abweichendeAdressen,
              isMutterNeu,
              isVaterNeu,
              isRechnungsempfaengerDrittpersonNeu);
      return; // -> Summary-Dialog
    }

    // 10. Sollen Adress- und Festnetzänderungen auch auf Geschwister angewendet werden?
    boolean isAdressaenderungenAufGeschwisterAnwenden = false;
    if (isBearbeiten() && hasAdresseOrFestnetzChanged() && !geschwisterList.isEmpty()) {
      Object[] options = {"Ja", "Nein"};
      int n =
          JOptionPane.showOptionDialog(
              null,
              "Soll die Adress- und/oder Festnetzänderung auch für Geschwister angewendet werden?",
              "Adress- und/oder Festnetzänderung auch für Geschwister anwenden?",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.QUESTION_MESSAGE,
              null,
              options, // the titles of buttons
              options[0]); // default button title
      if (n == 0) {
        isAdressaenderungenAufGeschwisterAnwenden = true;
      }
    }

    // 11. Schüler speichern
    Schueler schuelerToSave = prepareSchuelerForSave();
    SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schuelerToSave);
    saveSchuelerCommand.execute();

    // 12. Ggf. Adress- und Festnetzänderungen für Geschwister speichern
    if (isAdressaenderungenAufGeschwisterAnwenden) {
      for (Schueler geschwister : geschwisterList) {
        // Adress- und Festnetzänderungen anwenden
        geschwister.getAdresse().copyAttributesFrom(schueler.getAdresse());
        geschwister.setFestnetz(schueler.getFestnetz());
        saveSchuelerCommand = new SaveSchuelerCommand(geschwister);
        saveSchuelerCommand.execute();
      }
    }

    // 13. Lösche ggf. verwaiste Angehörige
    if (isBearbeiten()) {
      db.getCurrentEntityManager().flush();
      checkIfAngehoerigeVerwaistAndDelete();
    }

    result = new SchuelerErfassenSaveOkResult(Result.SPEICHERUNG_ERFOLGREICH);
  }

  private boolean hasAdresseOrFestnetzChanged() {
    if (!isBearbeiten()) {
      return false;
    }
    if (!schueler.getAdresse().isIdenticalWith(schuelerOrigin.getAdresse())) {
      return true;
    }
    if (schueler.getFestnetz() != null || schuelerOrigin.getFestnetz() != null) {
      if (schueler.getFestnetz() == null
          && schuelerOrigin.getFestnetz() != null
          && !schuelerOrigin.getFestnetz().isEmpty()) {
        return true;
      }
      if (schuelerOrigin.getFestnetz() == null
          && schueler.getFestnetz() != null
          && !schueler.getFestnetz().isEmpty()) {
        return true;
      }
      return schueler.getFestnetz() != null
          && !schueler.getFestnetz().equals(schuelerOrigin.getFestnetz());
    }
    return false;
  }

  private void checkIfAngehoerigeVerwaistAndDelete() {
    if (rechnungsempfaengerOrigin != null
        && (mutterOrigin == null || !mutterOrigin.isIdenticalWith(rechnungsempfaengerOrigin))
        && (vaterOrigin == null || !vaterOrigin.isIdenticalWith(rechnungsempfaengerOrigin))) {
      CheckIfAngehoerigerVerwaistAndDeleteCommand checkIfAngehoerigerVerwaistAndDeleteCommand =
          new CheckIfAngehoerigerVerwaistAndDeleteCommand(rechnungsempfaengerOrigin);
      checkIfAngehoerigerVerwaistAndDeleteCommand.execute();
    }
    if (mutterOrigin != null) {
      CheckIfAngehoerigerVerwaistAndDeleteCommand checkIfAngehoerigerVerwaistAndDeleteCommand =
          new CheckIfAngehoerigerVerwaistAndDeleteCommand(mutterOrigin);
      checkIfAngehoerigerVerwaistAndDeleteCommand.execute();
    }
    if (vaterOrigin != null) {
      CheckIfAngehoerigerVerwaistAndDeleteCommand checkIfAngehoerigerVerwaistAndDeleteCommand =
          new CheckIfAngehoerigerVerwaistAndDeleteCommand(vaterOrigin);
      checkIfAngehoerigerVerwaistAndDeleteCommand.execute();
    }
  }

  private void resetAnmeldung() {
    Iterator<Anmeldung> iterator = schueler.getAnmeldungen().iterator();
    while (iterator.hasNext()) {
      iterator.next();
      iterator.remove();
    }
  }

  private boolean isRechnungsempfaengerDrittperson() {
    return !isRechnungsempfaengerMutter && !isRechnungsempfaengerVater;
  }

  private static boolean isRechnungsempfaenger(Schueler schueler, Angehoeriger angehoeriger) {
    return schueler.getRechnungsempfaenger() == angehoeriger;
  }

  private boolean hasGeschwisterGemeinsameMutterOderBeideOhneMutter(Schueler geschwister) {
    return (mutter == null && mutterFoundInDatabase != null)
        || (mutter != null
            && mutterOrigin != null
            && geschwister.getMutter() != null
            && geschwister.getMutter().getPersonId().equals(mutterOrigin.getPersonId()))
        || (mutter == null && geschwister.getMutter() == null);
  }

  private boolean hasGeschwisterGemeinsamenVaterOderBeideOhneVater(Schueler geschwister) {
    return (vater == null && vaterFoundInDatabase != null)
        || (vater != null
            && vaterOrigin != null
            && geschwister.getVater() != null
            && geschwister.getVater().getPersonId().equals(vaterOrigin.getPersonId()))
        || (vater == null && geschwister.getVater() == null);
  }

  private boolean doElternOfGeschwisterHaveEmailAfterCurrentModifications(
      Schueler geschwister,
      boolean gemeinsameMutterOderBeideOhneMutter,
      boolean gemeinsamenVaterOderBeideOhneVater) {

    Angehoeriger mutterGeschwisterNeu =
        getMutterGeschwisterNeu(geschwister, gemeinsameMutterOderBeideOhneMutter);
    Angehoeriger vaterGeschwisterNeu =
        getVaterGeschwisterNeu(geschwister, gemeinsamenVaterOderBeideOhneVater);

    if (mutterGeschwisterNeu != null && mutterGeschwisterNeu.getEmail() != null) {
      return true;
    }

    return vaterGeschwisterNeu != null && vaterGeschwisterNeu.getEmail() != null;
  }

  private Angehoeriger getMutterGeschwisterNeu(
      Schueler geschwister, boolean gemeinsameMutterOderBeideOhneMutter) {
    if (gemeinsameMutterOderBeideOhneMutter) {
      return (mutter == null && mutterFoundInDatabase != null) ? mutterFoundInDatabase : mutter;
    } else {
      return geschwister.getMutter();
    }
  }

  private Angehoeriger getVaterGeschwisterNeu(
      Schueler geschwister, boolean gemeinsamenVaterOderBeideOhneVater) {
    if (gemeinsamenVaterOderBeideOhneVater) {
      return (vater == null && vaterFoundInDatabase != null) ? vaterFoundInDatabase : vater;
    } else {
      return geschwister.getVater();
    }
  }

  private boolean isForElternOfGeschwisterWuenschtEmailsSetAfterCurrentModifications(
      Schueler geschwister,
      boolean gemeinsameMutterOderBeideOhneMutter,
      boolean gemeinsamenVaterOderBeideOhneVater) {

    Angehoeriger mutterGeschwisterNeu =
        getMutterGeschwisterNeu(geschwister, gemeinsameMutterOderBeideOhneMutter);
    Angehoeriger vaterGeschwisterNeu =
        getVaterGeschwisterNeu(geschwister, gemeinsamenVaterOderBeideOhneVater);

    if (mutterGeschwisterNeu != null
        && (mutterGeschwisterNeu.getWuenschtEmails() != null
            && mutterGeschwisterNeu.getWuenschtEmails())) {
      return true;
    }

    return vaterGeschwisterNeu != null
        && vaterGeschwisterNeu.getWuenschtEmails() != null
        && vaterGeschwisterNeu.getWuenschtEmails();
  }

  /**
   * Wenn "Neu Erfassen" werden die Benutzereingaben (schueler) bzw. Mutter, Vater oder
   * Rechnungsempfänger von der Datenbank übernommen, es muss sonst nichts getan werden. Wenn
   * "Bearbeiten" werden die Benutzereingaben (schueler) in den "original" Schüler (schuelerOrigin)
   * übernommen.
   *
   * <p>Angehörige werden immer mutiert, das heisst, die Änderungen gelten für alle abhängigen
   * Objekte, die am Angehörigen hängen! Wird z.B. eine Mutter fälschlicherweise an einen Schüler
   * gehängt, muss dieser gelöscht und neu erfasst werden!
   *
   * @return zu speichernder Schüler
   */
  @SuppressWarnings("java:S3776")
  private Schueler prepareSchuelerForSave() {
    if (!isBearbeiten()) {
      setMutterFoundInDatabase(schueler);
      setVaterFoundInDatabase(schueler);
      setRechnungsempfaengerFoundInDatabase(schueler);
      return schueler;
    }
    // Schüler, Adresse und Anmeldung kopieren
    schuelerOrigin.copyFieldValuesFrom(schueler);
    schuelerOrigin.getAdresse().copyAttributesFrom(schueler.getAdresse());
    prepareAnmeldungForSave();
    // Mutter von Datenbank übernehmen ...
    if (!setMutterFoundInDatabase(schuelerOrigin)) {
      // ... oder kopieren
      Angehoeriger mutterPrepared = prepareAngehoerigerForSave(schuelerOrigin.getMutter(), mutter);
      if (schuelerOrigin.getMutter() != mutterPrepared) {
        schuelerOrigin.setMutter(mutterPrepared);
      }
    }
    // Vater von Datenbank übernehmen ...
    if (!setVaterFoundInDatabase(schuelerOrigin)) {
      // ... oder kopieren
      Angehoeriger vaterPrepared = prepareAngehoerigerForSave(schuelerOrigin.getVater(), vater);
      if (schuelerOrigin.getVater() != vaterPrepared) {
        schuelerOrigin.setVater(vaterPrepared);
      }
    }
    // Rechnungsempfänger von Datenbank übernehmen ...
    if (!setRechnungsempfaengerFoundInDatabase(schuelerOrigin)) {
      if (isRechnungsempfaenger(schueler, schueler.getMutter())) {
        if (!isRechnungsempfaenger(schuelerOrigin, schuelerOrigin.getMutter())) {
          // ... oder Mutter ist neue Rechnungsempfängerin
          schuelerOrigin.setRechnungsempfaenger(schuelerOrigin.getMutter());
        }
      } else if (isRechnungsempfaenger(schueler, schueler.getVater())) {
        if (!isRechnungsempfaenger(schuelerOrigin, schuelerOrigin.getVater())) {
          // ... oder Vater ist neuer Rechnungsempfänger
          schuelerOrigin.setRechnungsempfaenger(schuelerOrigin.getVater());
        }
      } else {
        Angehoeriger rechnungsempfaengerOrigin1;
        if (isRechnungsempfaenger(schuelerOrigin, schuelerOrigin.getMutter())
            || isRechnungsempfaenger(schuelerOrigin, schuelerOrigin.getVater())) {
          // ... oder Drittperson ist neuer Rechnungsempfänger
          rechnungsempfaengerOrigin1 = null;
        } else {
          // ... oder Drittperson war schon Rechnungsempfänger
          rechnungsempfaengerOrigin1 = schuelerOrigin.getRechnungsempfaenger();
        }
        // ... oder Drittperson kopieren
        Angehoeriger rechnungsempfaengerDrittpersonPrepared =
            prepareAngehoerigerForSave(rechnungsempfaengerOrigin1, rechnungsempfaengerDrittperson);
        if (schuelerOrigin.getRechnungsempfaenger() != rechnungsempfaengerDrittpersonPrepared) {
          schuelerOrigin.setRechnungsempfaenger(rechnungsempfaengerDrittpersonPrepared);
        }
      }
    }
    return schuelerOrigin;
  }

  private void prepareAnmeldungForSave() {
    Anmeldung anmeldungOrigin = schuelerOrigin.getAnmeldungen().get(0);
    Anmeldung anmeldungNew = schueler.getAnmeldungen().get(0);
    Anmeldung anmeldungPrepared = anmeldungOrigin;
    if (anmeldungOrigin.isInPast()
        && anmeldungNew.getAnmeldedatum().after(anmeldungOrigin.getAbmeldedatum())) {
      anmeldungPrepared = new Anmeldung();
      anmeldungPrepared.copyFieldValuesFrom(anmeldungNew); // muss vor dem add sein
      schuelerOrigin.addAnmeldung(anmeldungPrepared);
    } else {
      anmeldungPrepared.copyFieldValuesFrom(anmeldungNew);
    }
  }

  @SuppressWarnings("java:S3776")
  private static Angehoeriger prepareAngehoerigerForSave(
      Angehoeriger angehoerigerOrigin, Angehoeriger angehoerigerNew) {
    Angehoeriger angehoerigerPrepared = angehoerigerOrigin;
    if (angehoerigerOrigin != null) {
      if ((angehoerigerNew != null) && !angehoerigerNew.isEmpty()) {
        angehoerigerOrigin.copyFieldValuesFrom(angehoerigerNew);
        Adresse adresseOrigin = angehoerigerOrigin.getAdresse();
        if (adresseOrigin != null) {
          Adresse adresse = angehoerigerNew.getAdresse();
          if ((adresse != null) && !adresse.isEmpty()) {
            adresseOrigin.copyAttributesFrom(adresse);
          } else {
            angehoerigerOrigin.setAdresse(null);
          }
        } else {
          setNewAngehoerigerAdresse(angehoerigerOrigin, angehoerigerNew.getAdresse());
        }
      } else {
        angehoerigerPrepared = null;
      }
    } else {
      if ((angehoerigerNew != null) && !angehoerigerNew.isEmpty()) {
        angehoerigerPrepared = new Angehoeriger();
        angehoerigerPrepared.copyFieldValuesFrom(angehoerigerNew);
        setNewAngehoerigerAdresse(angehoerigerPrepared, angehoerigerNew.getAdresse());
      }
    }
    return angehoerigerPrepared;
  }

  private static void setNewAngehoerigerAdresse(
      Angehoeriger angehoerigerOrigin, Adresse adresseNew) {
    if ((adresseNew != null) && !adresseNew.isEmpty()) {
      Adresse adressePrepared = new Adresse();
      adressePrepared.copyAttributesFrom(adresseNew);
      angehoerigerOrigin.setAdresse(adressePrepared);
    }
  }

  private boolean setMutterFoundInDatabase(Schueler schueler) {
    if (mutterFoundInDatabase != null) {
      schueler.setMutter(mutterFoundInDatabase);
      if (isRechnungsempfaengerMutter) {
        schueler.setRechnungsempfaenger(mutterFoundInDatabase);
      }
      return true;
    }
    return false;
  }

  private boolean setVaterFoundInDatabase(Schueler schueler) {
    if (vaterFoundInDatabase != null) {
      schueler.setVater(vaterFoundInDatabase);
      if (isRechnungsempfaengerVater) {
        schueler.setRechnungsempfaenger(vaterFoundInDatabase);
      }
      return true;
    }
    return false;
  }

  private boolean setRechnungsempfaengerFoundInDatabase(Schueler schueler) {
    if (rechnungsempfaengerDrittpersonFoundInDatabase != null) {
      schueler.setRechnungsempfaenger(rechnungsempfaengerDrittpersonFoundInDatabase);
      return true;
    }
    return false;
  }

  /** Aktionen, die am Beginn durchgeführt werden sollen, abhängig vom Wert von entry. */
  @SuppressWarnings("java:S112")
  private void determineHowToProceed() {
    if (entry == null) {
      throw new RuntimeException("Eintrittspunkt nicht gesetzt!");
    }

    switch (entry) {
      case NEU_ERFASSTEN_SCHUELER_VALIDIEREN, SUMMARY_BESTAETIGT -> {
        // Nothing to do
      }
      case BEARBEITETEN_SCHUELER_VALIDIEREN -> skipCheckSchuelerBereitsInDatenbank = true;
      case MUTTER_AUS_DATENBANK_UEBERNEHMEN -> mutter = null;
      case MIT_BISHERIGER_MUTTER_WEITERFAHREN -> mutterFoundInDatabase = null;
      case VATER_AUS_DATENBANK_UEBERNEHMEN -> vater = null;
      case MIT_BISHERIGEM_VATER_WEITERFAHREN -> vaterFoundInDatabase = null;
      case RECHNUNGSEMPFAENGER_DRITTPERSON_AUS_DATENBANK_UEBERNEHMEN ->
          rechnungsempfaengerDrittperson = null;
      case MIT_BISHERIGEM_RECHNUNGSEMPFAENGER_DRITTPERSON_WEITERFAHREN ->
          rechnungsempfaengerDrittpersonFoundInDatabase = null;
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
