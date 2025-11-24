package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Hans Stamm
 */
@Repository
public interface MitarbeiterMitarbeiterCodeRepository extends JpaRepository<Mitarbeiter, Integer> {

  @Query(
      "SELECT COUNT(mmc) FROM MitarbeiterMitarbeiterCode mmc "
          + "WHERE mmc.mitarbeiterCode.codeId = :mitarbeiterCodeId")
  int countByMitarbeiterMitarbeiterCodeCodeId(@Param("mitarbeiterCodeId") int mitarbeiterCodeId);
}
