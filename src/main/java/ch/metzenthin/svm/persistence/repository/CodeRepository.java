package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.persistence.entities.Code;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

/**
 * @author Hans Stamm
 */
@NoRepositoryBean
public interface CodeRepository<T extends Code> extends JpaRepository<T, Integer> {

  @Query("SELECT COUNT(c) FROM #{#entityName} c WHERE c.kuerzel = :kuerzel")
  int countByKuerzel(@Param("kuerzel") String kuerzel);

  @Query(
      "SELECT COUNT(c) FROM #{#entityName} c "
          + "WHERE c.kuerzel = :kuerzel "
          + "AND c.codeId <> :id")
  int countByKuerzelAndIdNe(@Param("kuerzel") String kuerzel, @Param("id") int id);

  @Query("SELECT c FROM #{#entityName} c ORDER BY c.kuerzel")
  List<T> findAllOrderByKuerzel();
}
