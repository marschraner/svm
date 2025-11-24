package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Hans Stamm
 */
@Repository
public interface LektionsgebuehrenRepository extends JpaRepository<Lektionsgebuehren, Integer> {

  @Query("SELECT COUNT(l) FROM Lektionsgebuehren l WHERE l.lektionslaenge = :lektionslaenge")
  int countByLektionslaenge(@Param("lektionslaenge") int lektionslaenge);

  @Query(
      "SELECT COUNT(l) FROM Lektionsgebuehren l "
          + "WHERE l.lektionslaenge = :lektionslaenge "
          + "AND l.id <> :id")
  int countByLektionslaengeAndIdNe(
      @Param("lektionslaenge") int lektionslaenge, @Param("id") int id);

  @Query("SELECT l FROM Lektionsgebuehren l ORDER BY l.lektionslaenge")
  List<Lektionsgebuehren> findAllOrderByLektionslaenge();
}
