package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.Schueler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Hans Stamm
 */
@Repository
public interface SchuelerSchuelerCodeRepository extends JpaRepository<Schueler, Integer> {

  @Query(
      "SELECT COUNT(ssc) FROM SchuelerSchuelerCode ssc "
          + "WHERE ssc.schuelerCode.codeId = :schuelerCodeId")
  int countBySchuelerSchuelerCodeCodeId(@Param("schuelerCodeId") int schuelerCodeId);
}
