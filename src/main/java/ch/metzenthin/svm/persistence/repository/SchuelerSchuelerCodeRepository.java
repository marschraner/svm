package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.persistence.entities.SchuelerSchuelerCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Hans Stamm
 */
@Repository
public interface SchuelerSchuelerCodeRepository
    extends JpaRepository<SchuelerSchuelerCode, Integer> {

  @Query(
      "SELECT COUNT(ssc) FROM SchuelerSchuelerCode ssc "
          + "WHERE ssc.schuelerCode.codeId = :schuelerCodeId")
  int countBySchuelerSchuelerCodeCodeId(@Param("schuelerCodeId") int schuelerCodeId);

  @Query(
      "SELECT ssc.schuelerCode FROM SchuelerSchuelerCode ssc "
          + "WHERE ssc.schueler.personId = :schuelerId "
          + "AND ssc.schuelerCode.codeId = :schuelerCodeId")
  Optional<SchuelerCode> findAssignedSchuelerCodeBySchuelerIdAndSchuelerCodeId(
      @Param("schuelerId") int schuelerId, @Param("schuelerCodeId") int schuelerCodeId);

  @Query(
      "SELECT ssc FROM SchuelerSchuelerCode ssc "
          + "WHERE ssc.schueler.personId = :schuelerId "
          + "AND ssc.schuelerCode.codeId = :schuelerCodeId")
  Optional<SchuelerSchuelerCode> findBySchuelerIdAndSchuelerCodeId(
      @Param("schuelerId") int schuelerId, @Param("schuelerCodeId") int schuelerCodeId);

  @Query(
      "SELECT ssc.schuelerCode FROM SchuelerSchuelerCode ssc "
          + "WHERE ssc.schueler.personId = :schuelerId "
          + "ORDER BY ssc.schuelerCode.kuerzel")
  List<SchuelerCode> findAssignedSchuelerCodesBySchuelerId(@Param("schuelerId") int schuelerId);

  @Query(
      "SELECT sc FROM SchuelerCode sc "
          + "WHERE sc.selektierbar = TRUE "
          + "AND NOT EXISTS ("
          + "  SELECT 1 FROM SchuelerSchuelerCode ssc "
          + "    WHERE ssc.schuelerCode.codeId = sc.codeId "
          + "    AND ssc.schueler.personId = :schuelerId) "
          + "ORDER BY sc.kuerzel")
  List<SchuelerCode> findNotAssignedSelektierbareSchuelerCodesBySchuelerId(
      @Param("schuelerId") int schuelerId);

  @Modifying
  @Query(
      "DELETE FROM SchuelerSchuelerCode ssc "
          + "WHERE ssc.schueler.personId = :schuelerId "
          + "AND ssc.schuelerCode.codeId = :schuelerCodeId")
  void deleteBySchuelerIdAndSchuelerCodeId(
      @Param("schuelerId") int schuelerId, @Param("schuelerCodeId") int schuelerCodeId);
}
