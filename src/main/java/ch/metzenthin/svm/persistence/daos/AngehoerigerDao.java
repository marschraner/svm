package ch.metzenthin.svm.persistence.daos;

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
                selectStatementSb.append(" lower(a.vorname) = lower(:vorname) and");
            }
            if (angehoeriger.getNachname() != null) {
                selectStatementSb.append(" lower(a.nachname) = lower(:nachname) and");
            }
            if (angehoeriger.getFestnetz() != null) {
                selectStatementSb.append(" replace(a.festnetz, ' ', '') = replace(:festnetz, ' ', '') and");
            }
            if (angehoeriger.getNatel() != null) {
                selectStatementSb.append(" replace(a.natel, ' ', '') = replace(:natel, ' ', '') and");
            }
            if (angehoeriger.getEmail() != null) {
                selectStatementSb.append(" lower(a.email) = lower(:email) and");
            }
            if (angehoeriger.getAdresse() != null) {
                if (angehoeriger.getAdresse().getStrasse() != null) {
                    selectStatementSb.append(" lower(a.adresse.strasse) = lower(:strasse) and");
                }
                if (angehoeriger.getAdresse().getHausnummer() != null) {
                    selectStatementSb.append(" lower(a.adresse.hausnummer) = lower(:hausnummer) and");
                }
                if (angehoeriger.getAdresse().getPlz() != null) {
                    selectStatementSb.append(" a.adresse.plz = :plz and");
                }
                if (angehoeriger.getAdresse().getOrt() != null) {
                    selectStatementSb.append(" lower(a.adresse.ort) = lower(:ort) and");
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
            if (angehoeriger.getFestnetz() != null) {
                typedQuery.setParameter("festnetz", angehoeriger.getFestnetz());
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
            }
        }

        List<Angehoeriger> angehoerigeFound = typedQuery.getResultList();

        if (angehoerigeFound == null || angehoerigeFound.size() == 0) {
            return null;
        }

        return angehoerigeFound;
    }

}
