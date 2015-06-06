package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans Stamm
 */
public class AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult extends SchuelerErfassenSaveResult {

    private List<String> angehoerigeStrings = new ArrayList<>();
    private final static String BESCHREIBUNG = "Mehrere Einträge passen teilweise"; // todo

    public AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult(List<Angehoeriger> angehoerige, ValidateSchuelerCommand.Result result) {
        super(result);
        for (Angehoeriger angehoeriger : angehoerige) {
            this.angehoerigeStrings.add(angehoeriger.toString());
        }
    }

    public List<String> getAngehoerigeStrings() {
        return angehoerigeStrings;
    }

    public String getBeschreibung() {
        return BESCHREIBUNG;
    }

    @Override
    public void accept(SchuelerErfassenSaveResultVisitor visitor) {
        visitor.visit(this);
    }

}
