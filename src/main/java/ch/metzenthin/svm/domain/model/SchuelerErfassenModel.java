package ch.metzenthin.svm.domain.model;

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
    void abbrechen();
}
