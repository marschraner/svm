package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class AngehoerigerDao extends GenericDao<Angehoeriger, Integer> {

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
        selectStatementSb.append(" order by a.nachname, a.vorname");
        if ((angehoeriger != null) && (angehoeriger.getAdresse() != null)) {
            selectStatementSb.append(", a.adresse.ort, a.adresse.strasse");
        }

        TypedQuery<Angehoeriger> typedQuery = db.getCurrentEntityManager().createQuery(selectStatementSb.toString(), Angehoeriger.class);

        if (angehoeriger != null) {
            if (angehoeriger.getVorname() != null) {
                typedQuery.setParameter("vorname", angehoeriger.getVorname());
            }
            if (angehoeriger.getNachname() != null) {
                typedQuery.setParameter("nachname", angehoeriger.getNachname());
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

        return typedQuery.getResultList();

    }

}
