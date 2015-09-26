package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.DeleteSchuelerCommand;

/**
 * @author Hans Stamm
 */
public interface SchuelerErfassenModel extends Model {
    void setSchuelerModel(SchuelerModel schuelerModel);
    void setMutterModel(AngehoerigerModel mutterModel);
    void setVaterModel(AngehoerigerModel vaterModel);
    void setDrittempfaengerModel(AngehoerigerModel drittempfaengerModel);
    SchuelerErfassenSaveResult validieren();
    SchuelerErfassenSaveResult speichern(SchuelerErfassenSaveResult schuelerErfassenSaveResult);
    SchuelerErfassenSaveResult proceedUebernehmen(SchuelerErfassenSaveResult schuelerErfassenSaveResult);
    SchuelerErfassenSaveResult proceedWeiterfahren(SchuelerErfassenSaveResult schuelerErfassenSaveResult);
    DeleteSchuelerCommand.Result schuelerLoeschen();
    void fruehereAnmeldungenLoeschen();
    boolean hasFruehereAnmeldungen();
    void abbrechen();
    SchuelerSuchenTableData getSchuelerSuchenTableData();
}
