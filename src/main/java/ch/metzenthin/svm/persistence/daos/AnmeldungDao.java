package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;

import javax.persistence.EntityManager;

/**
 * @author Martin Schraner
 */
public class AnmeldungDao extends GenericDao<Anmeldung, Integer> {

    @Override
    public Anmeldung save(Anmeldung anmeldung) {
        throw new NullPointerException("Operation not supported");
    }

    @Override
    public void remove(Anmeldung anmeldung) {
        throw new NullPointerException("Operation not supported");
    }

    Schueler addToSchuelerAndSave(Anmeldung anmeldung, Schueler schueler) {
        schueler.addAnmeldung(anmeldung);
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.persist(schueler);
        entityManager.flush();
        entityManager.refresh(anmeldung);
        entityManager.refresh(schueler);
        return schueler;
    }

    public Schueler removeFromSchuelerAndUpdate(Anmeldung anmeldung, Schueler schueler) {
        schueler.deleteAnmeldung(anmeldung);
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.persist(schueler);
        entityManager.flush();
        entityManager.refresh(schueler);
        return schueler;
    }

}

