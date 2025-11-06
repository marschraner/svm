package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.Kurstyp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Hans Stamm
 */
@Repository
public interface KurstypRepository extends JpaRepository<Kurstyp, Integer> {

  @Query("SELECT COUNT(kt) FROM Kurstyp kt WHERE kt.bezeichnung = :bezeichnung")
  int countByBezeichnung(@Param("bezeichnung") String bezeichnung);

  @Query(
      "SELECT COUNT(kt) FROM Kurstyp kt "
          + "WHERE kt.bezeichnung = :bezeichnung "
          + "AND kt.kurstypId <> :id")
  int countByBezeichnungAndIdNe(@Param("bezeichnung") String bezeichnung, @Param("id") int id);

  @Query("SELECT kt FROM Kurstyp kt ORDER BY kt.bezeichnung")
  List<Kurstyp> findAllOrderByBezeichnung();
}
