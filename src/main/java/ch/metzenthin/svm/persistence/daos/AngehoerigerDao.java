package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class AngehoerigerDao extends GenericDao<Angehoeriger, Integer> {

    public AngehoerigerDao(EntityManager entityManager) {
        super(entityManager);
    }

    public void remove(Angehoeriger angehoeriger) {

        // Remove angehoeriger from adresse
        Adresse adresse = angehoeriger.getAdresse();
        adresse.getPersonen().remove(angehoeriger);

        // Remove angehoeriger from db
        entityManager.remove(angehoeriger);

        // Remove adresse from db if it is not referenced any more
        if (adresse.getPersonen().size() == 0) {
            AdresseDao adresseDao = new AdresseDao(entityManager);
            adresseDao.remove(adresse);
        }
    }

    /**
     * In der DB nach einem Angehörigen suchen. Zur Suche sind mindestens Vorname und Nachname erforderlich, die übrigen Attribute
     * sind optional. Es werden alle Angehörigen ausgegeben, deren Attribute mit den in der Suche gesetzten übereinstimmen. Für die
     * nicht gesetzten Attribute können beliebige Werte zurückgegeben werden.
     *
     * @param angehoeriger (not null)
     * @return angehoerigeFound
     */
    public List<Angehoeriger> findAngehoerige(Angehoeriger angehoeriger) {

        String selectStatement = "select a from Angehoeriger a" +
                        " where a.vorname = :vorname" +
                        " and a.nachname = :nachname";

        // Optional attributes
        if (angehoeriger.getNatel() != null) selectStatement = selectStatement + " and a.natel = :natel";
        if (angehoeriger.getEmail() != null) selectStatement = selectStatement + " and a.email = :email";
        if (angehoeriger.getAdresse() != null) {
            selectStatement = selectStatement + " and a.adresse.strasse = :strasse";
            selectStatement = selectStatement + " and a.adresse.plz = :plz";
            selectStatement = selectStatement + " and a.adresse.ort = :ort";
            if (angehoeriger.getAdresse().getHausnummer() != null) selectStatement = selectStatement + " and a.adresse.hausnummer = :hausnummer";
            if (angehoeriger.getAdresse().getFestnetz() != null) selectStatement = selectStatement + " and a.adresse.festnetz = :festnetz";
        }

        TypedQuery<Angehoeriger> typedQuery = entityManager.createQuery(selectStatement, Angehoeriger.class);

        typedQuery.setParameter("vorname", angehoeriger.getVorname());
        typedQuery.setParameter("nachname", angehoeriger.getNachname());

        // Optional attributes
        if (angehoeriger.getNatel() != null) typedQuery.setParameter("natel", angehoeriger.getNatel());
        if (angehoeriger.getEmail() != null) typedQuery.setParameter("email", angehoeriger.getEmail());
        if (angehoeriger.getAdresse() != null) {
            typedQuery.setParameter("strasse", angehoeriger.getAdresse().getStrasse());
            typedQuery.setParameter("plz", angehoeriger.getAdresse().getPlz());
            typedQuery.setParameter("ort", angehoeriger.getAdresse().getOrt());
            if (angehoeriger.getAdresse().getHausnummer() != null) typedQuery.setParameter("hausnummer", angehoeriger.getAdresse().getHausnummer());
            if (angehoeriger.getAdresse().getFestnetz() != null) typedQuery.setParameter("festnetz", angehoeriger.getAdresse().getFestnetz());
        }

        List<Angehoeriger> angehoerigeFound = typedQuery.getResultList();

        if (angehoerigeFound == null || angehoerigeFound.size() == 0) {
            return null;
        }

        return angehoerigeFound;
    }

}
