package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.Kurs;
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
}
