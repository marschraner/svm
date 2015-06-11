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
     * In der DB nach Angehörigen suchen. Sämtliche Attribute sind optional.
     * Es werden alle Angehörigen ausgegeben, deren Attribute mit den in der Suche gesetzten übereinstimmen.
     *
     * @param angehoeriger (not null)
     * @return angehoerigeFound
     */
    public List<Angehoeriger> findAngehoerige(Angehoeriger angehoeriger) {

        StringBuilder selectStatementSb = new StringBuilder("select a from Angehoeriger a where");

        if (angehoeriger != null) {

            if (angehoeriger.getVorname() != null) {
                selectStatementSb.append(" a.vorname = :vorname and");
            }
            if (angehoeriger.getNachname() != null) {
                selectStatementSb.append(" a.nachname = :nachname and");
            }
            if (angehoeriger.getNatel() != null) {
                selectStatementSb.append(" a.natel = :natel and");
            }
            if (angehoeriger.getEmail() != null) {
                selectStatementSb.append(" a.email = :email and");
            }
            if (angehoeriger.getAdresse() != null) {
                if (angehoeriger.getAdresse().getStrasse() != null) {
                    selectStatementSb.append(" a.adresse.strasse = :strasse and");
                }
                if (angehoeriger.getAdresse().getHausnummer() != null) {
                    selectStatementSb.append(" a.adresse.hausnummer = :hausnummer and");
                }
                if (angehoeriger.getAdresse().getPlz() != null) {
                    selectStatementSb.append(" a.adresse.plz = :plz and");
                }
                if (angehoeriger.getAdresse().getOrt() != null) {
                    selectStatementSb.append(" a.adresse.ort = :ort and");
                }
                if (angehoeriger.getAdresse().getFestnetz() != null) {
                    selectStatementSb.append(" a.adresse.festnetz = :festnetz and");
                }
            }

            // Letztes " and" löschen
            if (selectStatementSb.substring(selectStatementSb.length() - 4).equals(" and")) {
                selectStatementSb.setLength(selectStatementSb.length() - 4);
            }

        }

        // "where" löschen, falls dieses am Schluss steht
        if (selectStatementSb.substring(selectStatementSb.length() - 5).equals("where")) {
            selectStatementSb.setLength(selectStatementSb.length() - 5);
        }

        // Sortierung
        selectStatementSb.append(" order by a.nachname, a.vorname, a.adresse.ort, a.adresse.strasse");

        TypedQuery<Angehoeriger> typedQuery = entityManager.createQuery(selectStatementSb.toString(), Angehoeriger.class);

        if (angehoeriger != null) {
            if (angehoeriger.getVorname() != null) {
                typedQuery.setParameter("vorname", angehoeriger.getVorname());
            }
            if (angehoeriger.getVorname() != null) {
                typedQuery.setParameter("nachname", angehoeriger.getNachname());
            }
            if (angehoeriger.getNatel() != null) {
                typedQuery.setParameter("natel", angehoeriger.getNatel());
            }
            if (angehoeriger.getEmail() != null) {
                typedQuery.setParameter("email", angehoeriger.getEmail());
            }
            if (angehoeriger.getAdresse() != null) {
                if (angehoeriger.getAdresse().getStrasse() != null) {
                    typedQuery.setParameter("strasse", angehoeriger.getAdresse().getStrasse());
                }
                if (angehoeriger.getAdresse().getHausnummer() != null) {
                    typedQuery.setParameter("hausnummer", angehoeriger.getAdresse().getHausnummer());
                }
                if (angehoeriger.getAdresse().getPlz() != null) {
                    typedQuery.setParameter("plz", angehoeriger.getAdresse().getPlz());
                }
                if (angehoeriger.getAdresse().getOrt() != null) {
                    typedQuery.setParameter("ort", angehoeriger.getAdresse().getOrt());
                }
                if (angehoeriger.getAdresse().getFestnetz() != null) {
                    typedQuery.setParameter("festnetz", angehoeriger.getAdresse().getFestnetz());
                }
            }
        }

        List<Angehoeriger> angehoerigeFound = typedQuery.getResultList();

        if (angehoerigeFound == null || angehoerigeFound.size() == 0) {
            return null;
        }

        return angehoerigeFound;
    }

}