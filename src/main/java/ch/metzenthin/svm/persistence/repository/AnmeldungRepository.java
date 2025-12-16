package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.Anmeldung;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Martin Schraner
 */
@Repository
public interface AnmeldungRepository extends JpaRepository<Anmeldung, Integer> {

  @Query(
      "SELECT a FROM Anmeldung a WHERE a.schueler.personId = :schuelerId ORDER BY a.anmeldedatum DESC")
  List<Anmeldung> findBySchuelerIdOrderByAnmeldedatumDesc(@Param("schuelerId") int schuelerId);
}
