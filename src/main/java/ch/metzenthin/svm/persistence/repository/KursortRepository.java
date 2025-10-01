package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.Kursort;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Martin Schraner
 */
@Repository
public interface KursortRepository extends JpaRepository<Kursort, Integer> {

  @Query("SELECT COUNT(k) FROM Kursort k WHERE k.bezeichnung = :bezeichnung")
  int countByBezeichnung(@Param("bezeichnung") String bezeichnung);

  @Query("SELECT k FROM Kursort k ORDER BY k.bezeichnung")
  List<Kursort> findAllOrderByBezeichnung();
}
