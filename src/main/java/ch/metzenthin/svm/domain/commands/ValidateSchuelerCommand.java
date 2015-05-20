package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.domain.model.AngehoerigerModel;
import ch.metzenthin.svm.domain.model.SchuelerModel;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.List;

/**
 * @author Hans Stamm
 */
public class ValidateSchuelerCommand extends GenericDaoCommand {

    private SchuelerModel schuelerModel;
    private AngehoerigerModel vaterModel;
    private AngehoerigerModel mutterModel;
    private AngehoerigerModel rechnungsempfaengerModel;

    // Output
    private Schueler savedSchueler;
    private List<String> validationInformations;

    // Adressen von .... identisch
    // Adressen von ... verschieden
    // Vater, Mutter, ... bereits in Datenbank vorhanden
    // Vater, .... wird neu angelegt
    // In DB erfasste Geschwister gefunden: ...
    // Keine andere Geschwister erfasst.


    public ValidateSchuelerCommand(SchuelerModel schuelerModel, AngehoerigerModel vaterModel, AngehoerigerModel mutterModel, AngehoerigerModel rechnungsempfaengerModel) {
        this.schuelerModel = schuelerModel;
        this.mutterModel = mutterModel;
        this.rechnungsempfaengerModel = rechnungsempfaengerModel;
    }

    public Schueler getSavedSchueler() {
        return savedSchueler;
    }

    @Override
    public void execute() {
        Schueler schueler = schuelerModel.getSchueler();
        Adresse adresseSchueler = schuelerModel.getAdresse();

        Angehoeriger vater = vaterModel.getAngehoeriger();
        Adresse adresseVater = vaterModel.getAdresse();



    }

}
