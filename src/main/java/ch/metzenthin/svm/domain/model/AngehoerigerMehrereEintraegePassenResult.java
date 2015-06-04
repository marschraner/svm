package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans Stamm
 */
public class AngehoerigerMehrereEintraegePassenResult extends SchuelerErfassenSaveResult {

    private List<String> angehoerigeStrings = new ArrayList<>();
    private final static String BESCHREIBUNG = "Mehrere Einträge passen"; // todo

    public AngehoerigerMehrereEintraegePassenResult(List<Angehoeriger> angehoerige, ValidateSchuelerCommand.Result result) {
        super(result);
        for (Angehoeriger angehoeriger : angehoerige) {
            this.angehoerigeStrings.add(angehoeriger.toString());
        }
    }

    public List<String> getAngehoerigeStrings() {
        return angehoerigeStrings;
    }

    public static String getBESCHREIBUNG() {
        return BESCHREIBUNG;
    }

}
