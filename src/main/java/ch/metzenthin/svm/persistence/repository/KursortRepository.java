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

  @Query("SELECT COUNT(ko) FROM Kursort ko WHERE ko.bezeichnung = :bezeichnung")
  int countByBezeichnung(@Param("bezeichnung") String bezeichnung);

  @Query(
      "SELECT COUNT(ko) FROM Kursort ko "
          + "WHERE ko.bezeichnung = :bezeichnung "
          + "AND ko.kursortId <> :id")
  int countByBezeichnungAndIdNe(@Param("bezeichnung") String bezeichnung, @Param("id") int id);

  @Query("SELECT ko FROM Kursort ko ORDER BY ko.bezeichnung")
  List<Kursort> findAllOrderByBezeichnung();
}
