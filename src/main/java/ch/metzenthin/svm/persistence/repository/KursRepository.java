package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.domain.model.IdAndCount;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
          + "k.semester.semesterId, COUNT(k.kursId)) FROM Kurs k "
          + "GROUP BY k.semester.semesterId "
          + "ORDER BY k.semester.semesterId ASC")
  List<IdAndCount> countKurseGroupBySemesterId();

  @Query("SELECT k FROM Kurs k where k.semester.semesterId = :semesterId ORDER BY k.kursId ASC")
  List<Kurs> findAllBySemesterId(@Param("semesterId") int semesterId);

  @Query("SELECT k.semester FROM Kurs k where k.kursId = :kursId")
  Optional<Semester> findSemesterByKursId(@Param("kursId") int kursId);

  @Modifying
  @Query("DELETE FROM Kurs k WHERE k.kursId = :kursId")
  void deleteByKursId(@Param("kursId") int kursId);
}
