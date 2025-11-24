package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Hans Stamm
 */
@Repository
public interface SemesterrechnungRepository extends JpaRepository<Semesterrechnung, Integer> {

  @Query(
      "SELECT COUNT(sr) FROM Semesterrechnung sr "
          + "WHERE sr.semesterrechnungCode.codeId = :semesterrechnungCodeId")
  int countBySemesterrechnungCodeId(@Param("semesterrechnungCodeId") int semesterrechnungCodeId);
}
