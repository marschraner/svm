package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.Dispensation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Hans Stamm
 */
@Repository
public interface DispensationRepository extends JpaRepository<Dispensation, Integer> {

  @Query(
      "SELECT d FROM Dispensation d "
          + "WHERE d.schueler.personId = :schuelerId "
          + "ORDER BY d.dispensationsbeginn DESC")
  List<Dispensation> findBySchuelerIdOrderByDispensationsbeginnDesc(
      @Param("schuelerId") int schuelerId);
}
