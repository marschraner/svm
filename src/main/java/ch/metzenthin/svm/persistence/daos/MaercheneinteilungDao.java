package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.persistence.entities.MaercheneinteilungId;
import ch.metzenthin.svm.persistence.entities.Schueler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class MaercheneinteilungDao extends GenericDao<Maercheneinteilung, MaercheneinteilungId> {

    @Override
    public Maercheneinteilung save(Maercheneinteilung maercheneinteilung) {
        maercheneinteilung.getSchueler().getMaercheneinteilungen().add(maercheneinteilung);
        maercheneinteilung.getMaerchen().getMaercheneinteilungen().add(maercheneinteilung);
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.persist(maercheneinteilung);
        entityManager.flush();
        entityManager.refresh(maercheneinteilung);
        return maercheneinteilung;
    }

    @Override
    public void remove(Maercheneinteilung maercheneinteilung) {
        maercheneinteilung.getSchueler().getMaercheneinteilungen().remove(maercheneinteilung);
        maercheneinteilung.getMaerchen().getMaercheneinteilungen().remove(maercheneinteilung);
        db.getCurrentEntityManager().remove(maercheneinteilung);
    }

    public List<Maercheneinteilung> findMaercheneinteilungenSchueler(Schueler schueler) {
        TypedQuery<Maercheneinteilung> typedQuery = db.getCurrentEntityManager().createQuery(
                "select m from Maercheneinteilung m where m.schueler.personId = :personId", Maercheneinteilung.class);
        typedQuery.setParameter("personId", schueler.getPersonId());
        List<Maercheneinteilung> maercheneinteilungenFound = typedQuery.getResultList();
        // Sortieren gemäss compareTo in Märcheneinteilungen
        Collections.sort(maercheneinteilungenFound);
        return maercheneinteilungenFound;
    }

}

