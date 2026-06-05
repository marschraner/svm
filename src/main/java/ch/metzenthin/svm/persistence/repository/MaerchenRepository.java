package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.Maerchen;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Hans Stamm
 */
@Repository
public interface MaerchenRepository extends JpaRepository<Maerchen, Integer> {

  @Query(
      "SELECT COUNT(m) FROM Maerchen m "
          + "WHERE m.schuljahr = :schuljahr "
          + "AND m.bezeichnung = :bezeichnung")
  int countBySchuljahrAndBezeichnung(
      @Param("schuljahr") String schuljahr, @Param("bezeichnung") String bezeichnung);

  @Query(
      "SELECT COUNT(m) FROM Maerchen m "
          + "WHERE m.schuljahr = :schuljahr "
          + "AND m.bezeichnung = :bezeichnung "
          + "AND m.maerchenId <> :id")
  int countBySchuljahrAndBezeichnungAndIdNe(
      @Param("schuljahr") String schuljahr,
      @Param("bezeichnung") String bezeichnung,
      @Param("id") int id);

  @Query("SELECT m FROM Maerchen m ORDER BY m.schuljahr DESC")
  List<Maerchen> findAllOrderBySchuljahrDesc();
}
