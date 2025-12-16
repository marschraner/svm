package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Hans Stamm
 */
@Repository
public interface KursanmeldungRepository extends JpaRepository<Kursanmeldung, Integer> {

  @Query(
      "SELECT (ka) "
          + "FROM Kursanmeldung ka "
          + "JOIN Kurs k ON k.kursId = ka.kurs.kursId "
          + "WHERE ka.schueler.personId = :schuelerId "
          + "AND k.semester.semesterId = :semesterId")
  List<Kursanmeldung> findKursanmeldungenBySchuelerIdAndSemesterId(
      @Param("schuelerId") int schuelerId, @Param("semesterId") int semesterId);
}
