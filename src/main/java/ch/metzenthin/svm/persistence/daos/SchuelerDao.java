package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SchuelerDao extends GenericDao<Schueler, Integer> {

    public SchuelerDao(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void remove(Schueler schueler) {

        // Remove schueler from adresse, vater, mutter and rechnungsempaenger
        Adresse adresse = schueler.getAdresse();
        adresse.getPersonen().remove(schueler);

        Angehoeriger vater = schueler.getVater();
        if (vater != null) {
            vater.getKinderVater().remove(schueler);
        }

        Angehoeriger mutter = schueler.getMutter();
        if (mutter != null) {
            mutter.getKinderMutter().remove(schueler);
        }

        Angehoeriger rechnungsempfaenger = schueler.getRechnungsempfaenger();
        rechnungsempfaenger.getSchuelerRechnungsempfaenger().remove(schueler);

        // Remove schueler from db
        entityManager.remove(schueler);

        // Remove adresse, vater, mutter and rechnungsempfaenger if they are not referenced any more
        if (adresse.getPersonen().size() == 0) {
            AdresseDao adresseDao = new AdresseDao(entityManager);
            adresseDao.remove(adresse);
        }

        AngehoerigerDao angehoerigerDao = new AngehoerigerDao(entityManager);
        if (vater != null && entityManager.contains(vater) && vater.getKinderVater().size() == 0 && vater.getSchuelerRechnungsempfaenger().size() == 0) {
            angehoerigerDao.remove(vater);
        }

        if (mutter != null && entityManager.contains(mutter) && mutter.getKinderMutter().size() == 0 && mutter.getSchuelerRechnungsempfaenger().size() == 0) {
            angehoerigerDao.remove(mutter);
        }

        if (entityManager.contains(rechnungsempfaenger) && rechnungsempfaenger.getKinderVater().size() == 0 && rechnungsempfaenger.getKinderMutter().size() == 0 && rechnungsempfaenger.getSchuelerRechnungsempfaenger().size() == 0) {
            angehoerigerDao.remove(rechnungsempfaenger);
        }
    }

    /**
     * In der DB nach Schüler suchen. Sämtliche Attribute sind optional.
     * Es werden alle Schüler ausgegeben, deren Attribute mit den in der Suche gesetzten übereinstimmen.
     *
     * @param schueler (not null)
     * @return schuelerFound
     */
    public List<Schueler> findSchueler(Schueler schueler) {

        StringBuilder selectStatementSb = new StringBuilder("select s from Schueler s where");

        if (schueler != null) {

            if (schueler.getVorname() != null) {
                selectStatementSb.append(" s.vorname = :vorname and");
            }
            if (schueler.getNachname() != null) {
                selectStatementSb.append(" s.nachname = :nachname and");
            }
            if (schueler.getNatel() != null) {
                selectStatementSb.append(" s.natel = :natel and");
            }
            if (schueler.getEmail() != null) {
                selectStatementSb.append(" s.email = :email and");
            }
            if (schueler.getAdresse() != null) {
                if (schueler.getAdresse().getStrasse() != null) {
                    selectStatementSb.append(" s.adresse.strasse = :strasse and");
                }
                if (schueler.getAdresse().getHausnummer() != null) {
                    selectStatementSb.append(" s.adresse.hausnummer = :hausnummer and");
                }
                if (schueler.getAdresse().getPlz() != null) {
                    selectStatementSb.append(" s.adresse.plz = :plz and");
                }
                if (schueler.getAdresse().getOrt() != null) {
                    selectStatementSb.append(" s.adresse.ort = :ort and");
                }
                if (schueler.getAdresse().getFestnetz() != null) {
                    selectStatementSb.append(" s.adresse.festnetz = :festnetz and");
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

        TypedQuery<Schueler> typedQuery = entityManager.createQuery(selectStatementSb.toString(), Schueler.class);

        if (schueler != null) {
            if (schueler.getVorname() != null) {
                typedQuery.setParameter("vorname", schueler.getVorname());
            }
            if (schueler.getVorname() != null) {
                typedQuery.setParameter("nachname", schueler.getNachname());
            }
            if (schueler.getNatel() != null) {
                typedQuery.setParameter("natel", schueler.getNatel());
            }
            if (schueler.getEmail() != null) {
                typedQuery.setParameter("email", schueler.getEmail());
            }
            if (schueler.getAdresse() != null) {
                if (schueler.getAdresse().getStrasse() != null) {
                    typedQuery.setParameter("strasse", schueler.getAdresse().getStrasse());
                }
                if (schueler.getAdresse().getHausnummer() != null) {
                    typedQuery.setParameter("hausnummer", schueler.getAdresse().getHausnummer());
                }
                if (schueler.getAdresse().getPlz() != null) {
                    typedQuery.setParameter("plz", schueler.getAdresse().getPlz());
                }
                if (schueler.getAdresse().getOrt() != null) {
                    typedQuery.setParameter("ort", schueler.getAdresse().getOrt());
                }
                if (schueler.getAdresse().getFestnetz() != null) {
                    typedQuery.setParameter("festnetz", schueler.getAdresse().getFestnetz());
                }
            }
        }

        List<Schueler> schuelerFound = typedQuery.getResultList();

        if (schuelerFound == null || schuelerFound.size() == 0) {
            return null;
        }

        return schuelerFound;
    }


}
