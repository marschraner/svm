package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.domain.model.IdAndCount;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Hans Stamm
 */
@Repository
public interface MaercheneinteilungRepository extends JpaRepository<Maercheneinteilung, Integer> {

  @Query("SELECT COUNT(me) FROM Maercheneinteilung me WHERE me.maerchen.maerchenId= :maerchenId")
  int countByMaerchenId(@Param("maerchenId") int maerchenId);

  @Query(
      "SELECT COUNT(me) FROM Maercheneinteilung me "
          + "WHERE me.elternmithilfeCode.codeId = :elternmithilfeCodeId")
  int countByElternmithilfeCodeId(@Param("elternmithilfeCodeId") int elternmithilfeCodeId);

  @Query(
      "SELECT new ch.metzenthin.svm.domain.model.IdAndCount("
          + "me.maerchen.maerchenId, COUNT(me)) FROM Maercheneinteilung me "
          + "GROUP BY me.maerchen.maerchenId "
          + "ORDER BY me.maerchen.maerchenId ASC")
  List<IdAndCount> countMaercheneinteilungenGroupByMaerchenId();
}
