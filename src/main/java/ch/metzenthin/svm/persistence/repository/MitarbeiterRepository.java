package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Hans Stamm
 */
@Repository
public interface MitarbeiterRepository extends JpaRepository<Mitarbeiter, Integer> {

  @Query(
      "SELECT m FROM Mitarbeiter m "
          + "WHERE m.lehrkraft = TRUE AND m.aktiv = TRUE "
          + "ORDER BY m.nachname ASC, m.vorname ASC")
  List<Mitarbeiter> findByLehrkraftTrueAndAktivTrueOrderByNachnameVorname();
}
