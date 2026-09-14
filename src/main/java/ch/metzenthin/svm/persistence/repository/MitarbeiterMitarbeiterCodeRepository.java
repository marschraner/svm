package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.persistence.entities.MitarbeiterMitarbeiterCode;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Hans Stamm
 */
@Repository
public interface MitarbeiterMitarbeiterCodeRepository
    extends JpaRepository<MitarbeiterMitarbeiterCode, Integer> {

  @Query(
      "SELECT COUNT(mmc) FROM MitarbeiterMitarbeiterCode mmc "
          + "WHERE mmc.mitarbeiterCode.codeId = :mitarbeiterCodeId")
  int countByMitarbeiterCodeId(@Param("mitarbeiterCodeId") int mitarbeiterCodeId);

  @Query(
      "SELECT mmc.mitarbeiterCode FROM MitarbeiterMitarbeiterCode mmc "
          + "WHERE mmc.mitarbeiter.personId = :mitarbeiterId")
  List<MitarbeiterCode> findMitarbeiterCodesByMitarbeiterId(
      @Param("mitarbeiterId") int mitarbeiterId);

  @Modifying
  @Query(
      "DELETE FROM MitarbeiterMitarbeiterCode mmc "
          + "WHERE mmc.mitarbeiter.personId = :mitarbeiterId "
          + "AND mmc.mitarbeiterCode.codeId IN (:mitarbeiterCodeIds)")
  void deleteByMitabeiterIdEqAndMitarbeiterCodeIdIn(
      @Param("mitarbeiterId") int mitarbeiterId,
      @Param("mitarbeiterCodeIds") List<Integer> mitarbeiterCodeIds);
}
