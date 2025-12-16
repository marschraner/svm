package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Hans Stamm
 */
@Repository
public interface SemesterRepository extends JpaRepository<Semester, Integer> {

  @Query(
      "SELECT COUNT(s) FROM Semester s "
          + "WHERE s.schuljahr = :schuljahr AND s.semesterbezeichnung = :semesterbezeichnung")
  int countBySchuljahrAndSemesterbezeichnung(
      @Param("schuljahr") String schuljahr,
      @Param("semesterbezeichnung") Semesterbezeichnung semesterbezeichnung);

  @Query(
      "SELECT COUNT(s) FROM Semester s "
          + "WHERE s.schuljahr = :schuljahr AND s.semesterbezeichnung = :semesterbezeichnung "
          + "AND s.semesterId <> :id")
  int countBySchuljahrAndSemesterbezeichnungAndIdNe(
      @Param("schuljahr") String schuljahr,
      @Param("semesterbezeichnung") Semesterbezeichnung semesterbezeichnung,
      @Param("id") int id);

  @Query("SELECT s FROM Semester s ORDER BY s.semesterbeginn DESC, s.semesterende DESC")
  List<Semester> findAllOrderBySemesterbeginnAndSemesterendeDesc();
}
