package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.domain.model.MitarbeiterAndMitarbeiterCode;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import java.util.Calendar;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Hans Stamm
 */
@Repository
public interface MitarbeiterRepository extends JpaRepository<Mitarbeiter, Integer> {

  @Query(
      "SELECT COUNT(m) FROM Mitarbeiter m "
          + "WHERE m.nachname = :nachname "
          + "AND m.vorname = :vorname "
          + "AND ("
          + "   (m.geburtsdatum IS NULL AND :geburtsdatum IS NULL) "
          + "   OR (m.geburtsdatum = :geburtsdatum))")
  int countByNachnameAndVornameAndGeburtsdatum(
      @Param("nachname") String nachname,
      @Param("vorname") String vorname,
      @Param("geburtsdatum") Calendar geburtsdatum);

  @Query(
      "SELECT COUNT(m) FROM Mitarbeiter m "
          + "WHERE m.nachname = :nachname "
          + "AND m.vorname = :vorname "
          + "AND ("
          + "   (m.geburtsdatum IS NULL AND :geburtsdatum IS NULL) "
          + "   OR (m.geburtsdatum = :geburtsdatum)) "
          + "AND m.personId <> :personId")
  int countByNachnameAndVornameAndGeburtsdatumAndPersonIdNe(
      @Param("nachname") String nachname,
      @Param("vorname") String vorname,
      @Param("geburtsdatum") Calendar geburtsdatum,
      @Param("personId") int personId);

  @Query(
      "SELECT m FROM Mitarbeiter m "
          + "WHERE m.lehrkraft = TRUE AND m.aktiv = TRUE "
          + "ORDER BY m.nachname ASC, m.vorname ASC")
  List<Mitarbeiter> findByLehrkraftTrueAndAktivTrueOrderByNachnameVorname();

  @Query(
      "SELECT DISTINCT m FROM Mitarbeiter m "
          + "LEFT JOIN MitarbeiterMitarbeiterCode mmc ON mmc.mitarbeiter.personId = m.personId "
          + "WHERE (:nachname IS NULL OR LOWER(m.nachname) LIKE LOWER(CONCAT(:nachname, '%'))) "
          + "AND (:vorname IS NULL OR LOWER(m.vorname) LIKE LOWER(CONCAT(:vorname, '%'))) "
          + "AND (:lehrkraft IS NULL OR m.lehrkraft = :lehrkraft) "
          + "AND (:aktiv IS NULL OR m.aktiv = :aktiv) "
          + "AND (:codeId IS NULL OR mmc.mitarbeiterCode.codeId = :codeId) "
          + "ORDER BY m.nachname ASC, m.vorname ASC, m.geburtsdatum ASC")
  List<Mitarbeiter>
      findByNachnameLikeAndVornameLikeAndLehrkraftAndAktivAndCodeIdOrderByNachnameVornameGeburtsdatumAsc(
          @Param("nachname") String nachname,
          @Param("vorname") String vorname,
          @Param("lehrkraft") Boolean lehrkraft,
          @Param("aktiv") Boolean aktiv,
          @Param("codeId") Integer codeId);

  @Query(
      "SELECT DISTINCT m FROM Mitarbeiter m "
          + "WHERE NOT EXISTS ("
          + "   SELECT mmc FROM MitarbeiterMitarbeiterCode mmc "
          + "   WHERE mmc.mitarbeiter.personId = m.personId) "
          + "AND (:nachname IS NULL OR LOWER(m.nachname) LIKE LOWER(CONCAT(:nachname, '%'))) "
          + "AND (:vorname IS NULL OR LOWER(m.vorname) LIKE LOWER(CONCAT(:vorname, '%'))) "
          + "AND (:lehrkraft IS NULL OR m.lehrkraft = :lehrkraft) "
          + "AND (:aktiv IS NULL OR m.aktiv = :aktiv) "
          + "ORDER BY m.nachname ASC, m.vorname ASC, m.geburtsdatum ASC")
  List<Mitarbeiter>
      findMitarbeiterWithoutCodesByNachnameNullOrLikeAndVornameNullOrLikeAndLehrkraftNullOrEqAndAktivNullOrEq(
          @Param("nachname") String nachname,
          @Param("vorname") String vorname,
          @Param("lehrkraft") Boolean lehrkraft,
          @Param("aktiv") Boolean aktiv);

  @Query(
      "SELECT new ch.metzenthin.svm.domain.model.MitarbeiterAndMitarbeiterCode("
          + "m, mmc.mitarbeiterCode) FROM Mitarbeiter m "
          + "JOIN MitarbeiterMitarbeiterCode mmc ON mmc.mitarbeiter.personId = m.personId "
          + "WHERE (:nachname IS NULL OR LOWER(m.nachname) LIKE LOWER(CONCAT(:nachname, '%'))) "
          + "AND (:vorname IS NULL OR LOWER(m.vorname) LIKE LOWER(CONCAT(:vorname, '%'))) "
          + "AND (:lehrkraft IS NULL OR m.lehrkraft = :lehrkraft) "
          + "AND (:aktiv IS NULL OR m.aktiv = :aktiv) "
          + "AND (:codeId IS NULL OR mmc.mitarbeiterCode.codeId = :codeId) "
          + "ORDER BY m.nachname ASC, m.vorname ASC, m.geburtsdatum ASC")
  List<MitarbeiterAndMitarbeiterCode>
      findMitarbeiterAndMitarbeiterCodeOfMitarbeiterWithCodesByNachnameNullOrLikeAndVornameNullOrLikeAndLehrkraftNullOrEqAndAktivNullOrEqAndCodeIdNullOrEq(
          @Param("nachname") String nachname,
          @Param("vorname") String vorname,
          @Param("lehrkraft") Boolean lehrkraft,
          @Param("aktiv") Boolean aktiv,
          @Param("codeId") Integer codeId);
}
