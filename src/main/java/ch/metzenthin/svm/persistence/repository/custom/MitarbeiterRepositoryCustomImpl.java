package ch.metzenthin.svm.persistence.repository.custom;

import ch.metzenthin.svm.domain.model.MitarbeiterAndMitarbeiterCode;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Martin Schraner
 */
@SuppressWarnings({"SqlSourceToSinkFlow", "LoggingSimilarMessage"})
@Slf4j
@Repository
@Transactional(readOnly = true)
public class MitarbeiterRepositoryCustomImpl implements MitarbeiterRepositoryCustom {

  private static final String JPQL_SELECT_STATEMENT = "JPQL Select-Statement: {}";

  @PersistenceContext private EntityManager entityManager;

  @Override
  public List<Mitarbeiter> findMitarbeitersWithoutMitarbeiterCode(
      Optional<String> nachnameOptional,
      Optional<String> vornameOptional,
      Optional<Boolean> lehrkraftOptional,
      Optional<Boolean> aktivOptional,
      Optional<List<Integer>> mitarbeiterIdsOptional) {

    if (mitarbeiterIdsOptional.isPresent() && mitarbeiterIdsOptional.get().isEmpty()) {
      return List.of();
    }

    StringBuilder queryStringBuilder = new StringBuilder();

    createSelectStatement1(queryStringBuilder);

    createWhereConditions1(
        nachnameOptional,
        vornameOptional,
        lehrkraftOptional,
        aktivOptional,
        mitarbeiterIdsOptional,
        queryStringBuilder);

    log.trace(JPQL_SELECT_STATEMENT, queryStringBuilder);

    TypedQuery<Mitarbeiter> typedQuery =
        entityManager.createQuery(queryStringBuilder.toString(), Mitarbeiter.class);

    setSearchParameters1(
        nachnameOptional,
        vornameOptional,
        lehrkraftOptional,
        aktivOptional,
        mitarbeiterIdsOptional,
        typedQuery);

    return typedQuery.getResultList();
  }

  @Override
  public List<MitarbeiterAndMitarbeiterCode>
      findMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode(
          Optional<String> nachnameOptional,
          Optional<String> vornameOptional,
          Optional<Boolean> lehrkraftOptional,
          Optional<Boolean> aktivOptional,
          Optional<List<Integer>> mitarbeiterIdsOptional) {

    if (mitarbeiterIdsOptional.isPresent() && mitarbeiterIdsOptional.get().isEmpty()) {
      return List.of();
    }

    StringBuilder queryStringBuilder = new StringBuilder();

    createSelectStatement2(queryStringBuilder);

    createJoins2(queryStringBuilder);

    createWhereConditions2(
        nachnameOptional,
        vornameOptional,
        lehrkraftOptional,
        aktivOptional,
        mitarbeiterIdsOptional,
        queryStringBuilder);

    log.trace(JPQL_SELECT_STATEMENT, queryStringBuilder);

    TypedQuery<MitarbeiterAndMitarbeiterCode> typedQuery =
        entityManager.createQuery(
            queryStringBuilder.toString(), MitarbeiterAndMitarbeiterCode.class);

    setSearchParameters1(
        nachnameOptional,
        vornameOptional,
        lehrkraftOptional,
        aktivOptional,
        mitarbeiterIdsOptional,
        typedQuery);

    return typedQuery.getResultList();
  }

  private static void createSelectStatement1(StringBuilder queryStringBuilder) {
    queryStringBuilder.append("SELECT m FROM Mitarbeiter m ");
  }

  private static void createSelectStatement2(StringBuilder queryStringBuilder) {
    queryStringBuilder.append(
        "SELECT new ch.metzenthin.svm.domain.model.MitarbeiterAndMitarbeiterCode("
            + "m, mmc.mitarbeiterCode) FROM Mitarbeiter m ");
  }

  private static void createJoins2(StringBuilder queryStringBuilder) {
    queryStringBuilder.append(
        "JOIN MitarbeiterMitarbeiterCode mmc ON mmc.mitarbeiter.personId = m.personId ");
  }

  private static void createWhereConditions1(
      Optional<String> nachnameOptional,
      Optional<String> vornameOptional,
      Optional<Boolean> lehrkraftOptional,
      Optional<Boolean> aktivOptional,
      Optional<List<Integer>> mitarbeiterIdsOptional,
      StringBuilder queryStringBuilder) {
    queryStringBuilder.append(
        "WHERE NOT EXISTS ("
            + "  SELECT mmc FROM MitarbeiterMitarbeiterCode mmc "
            + "  WHERE mmc.mitarbeiter.personId = m.personId) AND ");
    createWhereConditions12(
        nachnameOptional,
        vornameOptional,
        lehrkraftOptional,
        aktivOptional,
        mitarbeiterIdsOptional,
        queryStringBuilder);
  }

  private static void createWhereConditions2(
      Optional<String> nachnameOptional,
      Optional<String> vornameOptional,
      Optional<Boolean> lehrkraftOptional,
      Optional<Boolean> aktivOptional,
      Optional<List<Integer>> mitarbeiterIdsOptional,
      StringBuilder queryStringBuilder) {
    queryStringBuilder.append("WHERE ");
    createWhereConditions12(
        nachnameOptional,
        vornameOptional,
        lehrkraftOptional,
        aktivOptional,
        mitarbeiterIdsOptional,
        queryStringBuilder);
  }

  private static void createWhereConditions12(
      Optional<String> nachnameOptional,
      Optional<String> vornameOptional,
      Optional<Boolean> lehrkraftOptional,
      Optional<Boolean> aktivOptional,
      Optional<List<Integer>> mitarbeiterIdsOptional,
      StringBuilder queryStringBuilder) {

    if (nachnameOptional.isPresent()) {
      queryStringBuilder.append("LOWER(m.nachname) LIKE :nachname AND ");
    }
    if (vornameOptional.isPresent()) {
      queryStringBuilder.append("LOWER(m.vorname) LIKE :vorname AND ");
    }
    if (lehrkraftOptional.isPresent()) {
      queryStringBuilder.append("m.lehrkraft = :lehrkraft AND ");
    }
    if (aktivOptional.isPresent()) {
      queryStringBuilder.append("m.aktiv = :aktiv AND ");
    }
    if (mitarbeiterIdsOptional.isPresent()) {
      queryStringBuilder.append("m.personId IN (:mitarbeiterIds) AND ");
    }

    // Letztes "AND " löschen
    if (queryStringBuilder.substring(queryStringBuilder.length() - 4).equals("AND ")) {
      queryStringBuilder.setLength(queryStringBuilder.length() - 4);
    }

    // "WHERE " löschen, falls dieses am Schluss steht
    if (queryStringBuilder.substring(queryStringBuilder.length() - 6).equals("WHERE ")) {
      queryStringBuilder.setLength(queryStringBuilder.length() - 6);
    }
  }

  private static void setSearchParameters1(
      Optional<String> nachnameOptional,
      Optional<String> vornameOptional,
      Optional<Boolean> lehrkraftOptional,
      Optional<Boolean> aktivOptional,
      Optional<List<Integer>> mitarbeiterIdsOptional,
      TypedQuery<?> typedQuery) {

    nachnameOptional.ifPresent(s -> typedQuery.setParameter("nachname", s.toLowerCase() + "%"));
    vornameOptional.ifPresent(s -> typedQuery.setParameter("vorname", s.toLowerCase() + "%"));
    lehrkraftOptional.ifPresent(b -> typedQuery.setParameter("lehrkraft", b));
    aktivOptional.ifPresent(b -> typedQuery.setParameter("aktiv", b));
    mitarbeiterIdsOptional.ifPresent(l -> typedQuery.setParameter("mitarbeiterIds", l));
  }
}
