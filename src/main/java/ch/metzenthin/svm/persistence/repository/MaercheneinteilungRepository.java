package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Hans Stamm
 */
@Repository
public interface MaercheneinteilungRepository extends JpaRepository<Maercheneinteilung, Integer> {

  @Query(
      "SELECT COUNT(me) FROM Maercheneinteilung me "
          + "WHERE me.elternmithilfeCode.codeId = :elternmithilfeCodeId")
  int countByElternmithilfeCodeId(@Param("elternmithilfeCodeId") int elternmithilfeCodeId);
}
