package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.domain.model.IdAndCount;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
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
public interface KursanmeldungRepository extends JpaRepository<Kursanmeldung, Integer> {

  @Query("SELECT COUNT(ka) FROM Kursanmeldung ka WHERE ka.kurs.kursId = :kursId ")
  int countByKursId(@Param("kursId") int kursId);

  @Query(
      "SELECT new ch.metzenthin.svm.domain.model.IdAndCount("
          + "ka.kurs.kursId, COUNT(ka)) FROM Kursanmeldung ka "
          + "WHERE ka.kurs.semester.semesterId = :semesterId "
          + "GROUP BY ka.kurs.kursId "
          + "ORDER BY ka.kurs.kursId ASC")
  List<IdAndCount> countKursanmeldungenBySemesterIdGroupByKursId(
      @Param("semesterId") int semesterId);

  @Query("SELECT ka FROM Kursanmeldung ka WHERE ka.kurs.kursId = :kursId")
  List<Kursanmeldung> findByKursId(@Param("kursId") int kursId);

  @Query(
      "SELECT ka "
          + "FROM Kursanmeldung ka "
          + "WHERE ka.schueler.personId = :schuelerId "
          + "AND ka.kurs.semester.semesterId = :semesterId")
  List<Kursanmeldung> findBySchuelerIdAndSemesterId(
      @Param("schuelerId") int schuelerId, @Param("semesterId") int semesterId);

  @Query(
      "SELECT ka FROM Kursanmeldung ka "
          + "WHERE ka.kurs.semester.semesterId = :semesterId "
          + "AND ka.schueler.rechnungsempfaenger.personId = :rechnungsempfaengerId "
          + "ORDER BY ka.schueler.personId")
  List<Kursanmeldung> findBySemesterIdAndRechnungsempfaengerIdOrderBySchuelerId(
      @Param("semesterId") int semesterId,
      @Param("rechnungsempfaengerId") int rechnungsempfaengerId);

  @Modifying
  @Query("DELETE FROM Kursanmeldung ka WHERE ka.kurs.kursId = :kursId")
  void deleteByKursId(@Param("kursId") int kursId);
}
