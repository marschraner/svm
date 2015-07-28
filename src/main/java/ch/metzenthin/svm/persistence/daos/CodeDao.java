package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.persistence.entities.Schueler;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class CodeDao extends GenericDao<Code, Integer> {

    public CodeDao(EntityManager entityManager) {
        super(entityManager);
    }

    public Schueler addToSchuelerAndSave(Code code, Schueler schueler) {
        schueler.addCode(code);
        entityManager.persist(schueler);
        return schueler;
    }

    public Schueler removeFromSchuelerAndUpdate(Code code, Schueler schueler) {
        // Neu laden, da bei n:m-Beziehungen in der Zwischentabelle beim Hinzufügen eines Codes sämtliche Einträge
        // des Schülers upgedated wurden (-> Fehler, dass Eintrag seit letztem Lesen verändert wurde!)
        // Refresh funktioniert nicht für Test.
        SchuelerDao schuelerDao = new SchuelerDao(entityManager);
        Schueler schuelerReloaded = schuelerDao.findById(schueler.getPersonId());
        schuelerReloaded.deleteCode(code);
        entityManager.persist(schuelerReloaded);
        return schuelerReloaded;
    }

    public List<Code> findAll() {
        TypedQuery<Code> typedQuery = entityManager.createQuery("select c from Code c order by c.kuerzel", Code.class);
        return typedQuery.getResultList();
    }

}

