package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.Schueler;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Hans Stamm
 */
@Repository
public interface SchuelerRepository extends JpaRepository<Schueler, Integer> {

  @Query(
      "SELECT (s) "
          + "FROM Schueler s "
          + "WHERE s.rechnungsempfaenger.personId = :rechnungsempfaengerId")
  List<Schueler> findSchuelerByRechnungsempfaengerId(
      @Param("rechnungsempfaengerId") int rechnungsempfaengerId);
}
