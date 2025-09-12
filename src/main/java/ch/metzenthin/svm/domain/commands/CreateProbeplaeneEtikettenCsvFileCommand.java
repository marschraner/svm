package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.SvmRuntimeException;
import ch.metzenthin.svm.common.utils.MaercheneinteilungenSorter;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Martin Schraner
 */
public class CreateProbeplaeneEtikettenCsvFileCommand extends CreateListeCommand {

  private final MaercheneinteilungenSorter maercheneinteilungenSorter =
      new MaercheneinteilungenSorter();

  // input
  private final SchuelerSuchenTableModel schuelerSuchenTableModel;
  private final File outputFile;

  public CreateProbeplaeneEtikettenCsvFileCommand(
      SchuelerSuchenTableModel schuelerSuchenTableModel, File outputFile) {
    this.schuelerSuchenTableModel = schuelerSuchenTableModel;
    this.outputFile = outputFile;
  }

  @SuppressWarnings("DuplicatedCode")
  @Override
  public void execute() {

    char separator = ';';

    try {
      Writer out =
          new BufferedWriter(
              new OutputStreamWriter(
                  new FileOutputStream(outputFile), StandardCharsets.ISO_8859_1));

      // Header
      out.write("Vorname");
      out.write(separator);
      out.write("Nachname");
      out.write(separator);
      out.write("Rolle 1");
      out.write(separator);
      out.write("Rolle 2");
      out.write(separator);
      out.write("Rolle 3");
      out.write(separator);
      out.write("Alle Rollen");
      out.write(separator);
      out.write("Gruppe");
      out.write('\n');

      // Daten
      Map<Schueler, Maercheneinteilung> maercheneinteilungen =
          schuelerSuchenTableModel.getMaercheneinteilungen();
      // Wenn nach Rollen gesucht wurde, muss nach Rollen sortiert werden, sonst nach Schülern
      Set<Schueler> keys;
      if (schuelerSuchenTableModel.isNachRollenGesucht()) {
        // Wenn nach Rollen gesucht wurde, gibt es keine Keys mit leeren Values
        maercheneinteilungen =
            maercheneinteilungenSorter.sortMaercheneinteilungenByGruppeAndRolle(
                maercheneinteilungen);
        keys = maercheneinteilungen.keySet();
      } else {
        // Sortierung nach Keys
        keys = new TreeSet<>(maercheneinteilungen.keySet());
      }
      for (Schueler schueler : keys) {
        Maercheneinteilung maercheneinteilung = maercheneinteilungen.get(schueler);
        if (maercheneinteilung == null || !schueler.isSelektiert()) {
          continue;
        }
        out.write(schueler.getVorname());
        out.write(separator);
        out.write(schueler.getNachname());
        out.write(separator);
        out.write(maercheneinteilung.getRolle1WithoutSorterCharacters());
        out.write(separator);
        String rolle2AsStr =
            (maercheneinteilung.getRolle2() == null ? "" : maercheneinteilung.getRolle2());
        out.write(rolle2AsStr);
        out.write(separator);
        String rolle3AsStr =
            (maercheneinteilung.getRolle3() == null ? "" : maercheneinteilung.getRolle3());
        out.write(rolle3AsStr);
        out.write(separator);
        out.write(maercheneinteilung.getRollenAllWithoutSorterCharacters());
        out.write(separator);
        out.write(maercheneinteilung.getGruppe().toString());
        out.write('\n');
      }

      out.close();

      result = Result.LISTE_ERFOLGREICH_ERSTELLT;

    } catch (IOException e) {
      throw new SvmRuntimeException("Fehler beim Erstellen der csv-Datei", e);
    }
  }
}
