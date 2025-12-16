package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.domain.model.IdAndCount;
import ch.metzenthin.svm.persistence.entities.Kurs;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Martin Schraner
 */
@Repository
public interface KursRepository extends JpaRepository<Kurs, Integer> {

  @Query("SELECT COUNT(k) FROM Kurs k WHERE k.kursort.kursortId = :kursortId")
  int countByKursortId(@Param("kursortId") int kursortId);

  @Query("SELECT COUNT(k) FROM Kurs k WHERE k.kurstyp.kurstypId = :kurstypId")
  int countByKurstypId(@Param("kurstypId") int kurstypId);

  @Query("SELECT COUNT(k) FROM Kurs k WHERE k.semester.semesterId = :semesterId")
  int countBySemesterId(@Param("semesterId") int semesterId);

  @Query(
      "SELECT new ch.metzenthin.svm.domain.model.IdAndCount("
          + "k.semester.semesterId, COUNT(k.kursId)) "
          + "FROM Kurs k "
          + "WHERE k.semester.semesterId IN (:semesterIds)"
          + "GROUP BY k.semester.semesterId "
          + "ORDER BY k.semester.semesterId ASC")
  List<IdAndCount> countKurseGroupBySemesterId(@Param("semesterIds") List<Integer> semesterIds);
}
