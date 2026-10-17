package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.domain.model.MitarbeiterAndMitarbeiterCode;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.repository.custom.MitarbeiterRepositoryCustom;
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
public interface MitarbeiterRepository
    extends JpaRepository<Mitarbeiter, Integer>, MitarbeiterRepositoryCustom {

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
  int countByNachnameAndVornameAndGeburtsdatumAndMitarbeiterIdNe(
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
      "SELECT new ch.metzenthin.svm.domain.model.MitarbeiterAndMitarbeiterCode(m, mc) "
          + "FROM Mitarbeiter m "
          + "LEFT JOIN MitarbeiterMitarbeiterCode mmc ON mmc.mitarbeiter = m "
          + "LEFT JOIN MitarbeiterCode mc ON mc = mmc.mitarbeiterCode "
          + "WHERE m.personId = :personId")
  List<MitarbeiterAndMitarbeiterCode> findByMitarbeiterAndMitarbeiterCodesByMitarbeiterId(
      @Param("personId") int personId);
}
