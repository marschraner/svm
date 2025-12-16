package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
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
public interface SemesterrechnungRepository extends JpaRepository<Semesterrechnung, Integer> {

  @Query("SELECT COUNT(sr) FROM Semesterrechnung sr WHERE sr.semester.semesterId = :semesterId")
  int countBySemesterId(@Param("semesterId") int semesterId);

  @Query(
      "SELECT COUNT(sr) FROM Semesterrechnung sr "
          + "WHERE sr.semesterrechnungCode.codeId = :semesterrechnungCodeId")
  int countBySemesterrechnungCodeId(@Param("semesterrechnungCodeId") int semesterrechnungCodeId);

  @Query("SELECT (sr) FROM Semesterrechnung sr WHERE sr.semester.semesterId = :semesterId")
  List<Semesterrechnung> findSemesterrechnungenBySemesterId(@Param("semesterId") int semesterId);

  @Modifying
  @Query("DELETE FROM Semesterrechnung sr WHERE sr.semester.semesterId = :semesterId")
  void deleteBySemesterId(@Param("semesterId") int semesterId);
}
