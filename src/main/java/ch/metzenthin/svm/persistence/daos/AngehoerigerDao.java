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

    public Angehoeriger findAngehoeriger(Angehoeriger angehoeriger) {

        String selectStatement = "select a from Angehoeriger a" +
                        " where a.anrede = :anrede" +
                        " and a.vorname = :vorname" +
                        " and a.nachname = :nachname" +
                        " and a.adresse.strasse = :strasse" +
                        " and a.adresse.plz = :plz" +
                        " and a.adresse.ort = :ort";

        // Optional attributes
        if (angehoeriger.getBeruf() != null) selectStatement = selectStatement + " and a.beruf = :beruf";
        if (angehoeriger.getNatel() != null) selectStatement = selectStatement + " and a.natel = :natel";
        if (angehoeriger.getEmail() != null) selectStatement = selectStatement + " and a.email = :email";
        if (angehoeriger.getAdresse().getHausnummer() != null) selectStatement = selectStatement + " and a.adresse.hausnummer = :hausnummer";
        if (angehoeriger.getAdresse().getFestnetz() != null) selectStatement = selectStatement + " and a.adresse.festnetz = :festnetz";

        TypedQuery<Angehoeriger> typedQuery = entityManager.createQuery(selectStatement, Angehoeriger.class);

        typedQuery.setParameter("anrede", angehoeriger.getAnrede());
        typedQuery.setParameter("vorname", angehoeriger.getVorname());
        typedQuery.setParameter("nachname", angehoeriger.getNachname());
        typedQuery.setParameter("strasse", angehoeriger.getAdresse().getStrasse());
        typedQuery.setParameter("plz", angehoeriger.getAdresse().getPlz());
        typedQuery.setParameter("ort", angehoeriger.getAdresse().getOrt());

        // Optional attributes
        if (angehoeriger.getBeruf() != null) typedQuery.setParameter("beruf", angehoeriger.getBeruf());
        if (angehoeriger.getNatel() != null) typedQuery.setParameter("natel", angehoeriger.getNatel());
        if (angehoeriger.getEmail() != null) typedQuery.setParameter("email", angehoeriger.getEmail());
        if (angehoeriger.getAdresse().getHausnummer() != null) typedQuery.setParameter("hausnummer", angehoeriger.getAdresse().getHausnummer());
        if (angehoeriger.getAdresse().getFestnetz() != null) typedQuery.setParameter("festnetz", angehoeriger.getAdresse().getFestnetz());

        List<Angehoeriger> angehoerigeFound = typedQuery.getResultList();

        if (angehoerigeFound == null || angehoerigeFound.size() == 0) {
            return null;
        }

        else if (angehoerigeFound.size() == 1) {
            Angehoeriger angehoerigerFound = angehoerigeFound.get(0);

            // Check optional attributes
            if (angehoeriger.getBeruf() == null && angehoerigerFound.getBeruf() != null) return null;
            if (angehoeriger.getNatel() == null && angehoerigerFound.getNatel() != null) return null;
            if (angehoeriger.getEmail() == null && angehoerigerFound.getEmail() != null) return null;
            if (angehoeriger.getAdresse().getHausnummer() == null && angehoerigerFound.getAdresse().getHausnummer() != null) return null;
            if (angehoeriger.getAdresse().getFestnetz() == null && angehoerigerFound.getAdresse().getFestnetz() != null) return null;
            return angehoerigerFound;
        }

        else {
           throw new NullPointerException("Mehr als ein Anghehöriger gefunden");
        }

    }

}
