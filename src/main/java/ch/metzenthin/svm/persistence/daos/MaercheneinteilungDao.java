package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.persistence.entities.MaercheneinteilungId;
import ch.metzenthin.svm.persistence.entities.Schueler;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class MaercheneinteilungDao extends GenericDao<Maercheneinteilung, MaercheneinteilungId> {

    public MaercheneinteilungDao(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Maercheneinteilung save(Maercheneinteilung maercheneinteilung) {
        maercheneinteilung.getSchueler().getMaercheneinteilungen().add(maercheneinteilung);
        maercheneinteilung.getMaerchen().getMaercheneinteilungen().add(maercheneinteilung);
        entityManager.persist(maercheneinteilung);
        entityManager.flush();
        entityManager.refresh(maercheneinteilung);
        return maercheneinteilung;
    }

    @Override
    public void remove(Maercheneinteilung maercheneinteilung) {
        maercheneinteilung.getSchueler().getMaercheneinteilungen().remove(maercheneinteilung);
        maercheneinteilung.getMaerchen().getMaercheneinteilungen().remove(maercheneinteilung);
        entityManager.remove(maercheneinteilung);
    }

    public List<Maercheneinteilung> findMaercheneinteilungenSchueler(Schueler schueler) {
        TypedQuery<Maercheneinteilung> typedQuery = entityManager.createQuery("select m from Maercheneinteilung m where m.schueler.personId = :personId", Maercheneinteilung.class);
        typedQuery.setParameter("personId", schueler.getPersonId());
        List<Maercheneinteilung> maercheneinteilungenFound = typedQuery.getResultList();
        // Sortieren gemäss compareTo in Maercheneinteilungen
        Collections.sort(maercheneinteilungenFound);
        return maercheneinteilungenFound;
    }

}

