package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SchuelerDao extends GenericDao<Schueler, Integer> {

  private final KursanmeldungDao kursanmeldungDao = new KursanmeldungDao();
  private final MaercheneinteilungDao maercheneinteilungDao = new MaercheneinteilungDao();
  private final AngehoerigerDao angehoerigerDao = new AngehoerigerDao();

  @Override
  public Schueler save(Schueler schueler) {
    super.save(schueler);
    EntityManager entityManager = db.getCurrentEntityManager();
    entityManager.refresh(schueler.getAdresse());
    if (schueler.getMutter() != null) {
      entityManager.refresh(schueler.getMutter());
      if (schueler.getMutter().getAdresse() != null) {
        entityManager.refresh(schueler.getMutter().getAdresse());
      }
    }
    if (schueler.getVater() != null) {
      entityManager.refresh(schueler.getVater());
      if (schueler.getVater().getAdresse() != null) {
        entityManager.refresh(schueler.getVater().getAdresse());
      }
    }
    entityManager.refresh(schueler.getRechnungsempfaenger());
    entityManager.refresh(schueler.getRechnungsempfaenger().getAdresse());
    for (Anmeldung anmeldung : schueler.getAnmeldungen()) {
      entityManager.refresh(anmeldung);
    }
    return schueler;
  }

  @Override
  public void remove(Schueler schueler) {

    // Entferne Schüler von Vater, Mutter und Rechnungsempfänger
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

    // Lösche zugewiesene Codes
    EntityManager entityManager = db.getCurrentEntityManager();
    for (SchuelerCode schuelerCode : new HashSet<>(schueler.getSchuelerCodes())) {
      schueler.deleteCode(schuelerCode);
      entityManager.refresh(schuelerCode);
    }

    // Lösche zugewiesene Kursanmeldungen
    List<Kursanmeldung> kurseinteilungenSchueler =
        kursanmeldungDao.findKursanmeldungenSchueler(schueler);
    for (Kursanmeldung kursanmeldung : new ArrayList<>(kurseinteilungenSchueler)) {
      kursanmeldungDao.remove(kursanmeldung);
    }

    // Lösche zugewiesene Märcheneinteilungen
    List<Maercheneinteilung> maercheneinteilungenSchueler =
        maercheneinteilungDao.findMaercheneinteilungenSchueler(schueler);
    for (Maercheneinteilung maercheneinteilung : new ArrayList<>(maercheneinteilungenSchueler)) {
      maercheneinteilungDao.remove(maercheneinteilung);
    }

    // Lösche Schüler aus DB
    entityManager.remove(schueler);

    // Lösche Vater, Mutter und Rechnungsempfänger aus DB, falls diese nicht mehr referenziert
    // werden
    // Achtung: Dies muss NACH dem Löschen des Schülers erfolgen!
    if (vater != null
        && entityManager.contains(vater)
        && vater.getKinderVater().isEmpty()
        && vater.getSchuelerRechnungsempfaenger().isEmpty()
        && (!isAngehoerigerInSemesterrechnung(vater))) {
      angehoerigerDao.remove(vater);
    }

    if (mutter != null
        && entityManager.contains(mutter)
        && mutter.getKinderMutter().isEmpty()
        && mutter.getSchuelerRechnungsempfaenger().isEmpty()
        && (!isAngehoerigerInSemesterrechnung(mutter))) {
      angehoerigerDao.remove(mutter);
    }

    if (entityManager.contains(rechnungsempfaenger)
        && rechnungsempfaenger.getKinderVater().isEmpty()
        && rechnungsempfaenger.getKinderMutter().isEmpty()
        && rechnungsempfaenger.getSchuelerRechnungsempfaenger().isEmpty()
        && (!isAngehoerigerInSemesterrechnung(rechnungsempfaenger))) {
      angehoerigerDao.remove(rechnungsempfaenger);
    }
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean isAngehoerigerInSemesterrechnung(Angehoeriger angehoeriger) {
    // Kommt Angehoeriger als Rechnungsempfänger in irgendeiner (früheren) Semesterrechnung vor?
    TypedQuery<Long> typedQuery =
        db.getCurrentEntityManager()
            .createQuery(
                "select count(s) from Semesterrechnung s where s.rechnungsempfaenger.personId = :angehoerigerPersonId",
                Long.class);
    typedQuery.setParameter("angehoerigerPersonId", angehoeriger.getPersonId());
    return typedQuery.getSingleResult() > 0;
  }

  /**
   * In der DB nach Schüler suchen. Sämtliche Attribute sind optional. Es werden alle Schüler
   * ausgegeben, deren Attribute mit den in der Suche gesetzten übereinstimmen.
   *
   * @param schueler (not null)
   * @return schuelerFound
   */
  @SuppressWarnings({"DuplicatedCode", "ExtractMethodRecommender", "java:S3776"})
  public List<Schueler> findSchueler(Schueler schueler) {

    StringBuilder selectStatementSb = new StringBuilder("select s from Schueler s where");

    if (schueler != null) {

      if (schueler.getVorname() != null) {
        selectStatementSb.append(" lower(s.vorname) = lower(:vorname) and");
      }
      if (schueler.getNachname() != null) {
        selectStatementSb.append(" lower(s.nachname) = lower(:nachname) and");
      }
      if (schueler.getAdresse() != null) {
        if (schueler.getAdresse().getStrasse() != null) {
          selectStatementSb.append(" lower(s.adresse.strasse) = lower(:strasse) and");
        }
        if (schueler.getAdresse().getHausnummer() != null) {
          selectStatementSb.append(" lower(s.adresse.hausnummer) = lower(:hausnummer) and");
        }
        if (schueler.getAdresse().getPlz() != null) {
          selectStatementSb.append(" s.adresse.plz = :plz and");
        }
        if (schueler.getAdresse().getOrt() != null) {
          selectStatementSb.append(" lower(s.adresse.ort) = lower(:ort) and");
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
    selectStatementSb.append(" order by s.nachname, s.vorname, s.adresse.ort, s.adresse.strasse");

    TypedQuery<Schueler> typedQuery =
        db.getCurrentEntityManager().createQuery(selectStatementSb.toString(), Schueler.class);

    if (schueler != null) {
      if (schueler.getVorname() != null) {
        typedQuery.setParameter("vorname", schueler.getVorname());
      }
      if (schueler.getVorname() != null) {
        typedQuery.setParameter("nachname", schueler.getNachname());
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
      }
    }

    return typedQuery.getResultList();
  }
}
