package ch.metzenthin.svm.domain.model;

/**
 * @author Hans Stamm
 */
public interface SchuelerErfassenModel extends Model {
    void setSchuelerModel(SchuelerModel schuelerModel);
    void setMutterModel(AngehoerigerModel mutterModel);
    void setVaterModel(AngehoerigerModel vaterModel);
    void setDrittempfaengerModel(AngehoerigerModel drittempfaengerModel);
    void save();
}
