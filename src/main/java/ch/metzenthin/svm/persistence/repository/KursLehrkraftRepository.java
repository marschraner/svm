package ch.metzenthin.svm.persistence.repository;

import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.persistence.entities.KursLehrkraft;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import java.sql.Time;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Martin Schraner
 */
@Repository
public interface KursLehrkraftRepository extends JpaRepository<KursLehrkraft, Integer> {

  @Query(
      "SELECT COUNT(kl) FROM KursLehrkraft kl "
          + "WHERE kl.kurs.semester.semesterId = :semesterId "
          + "AND kl.kurs.wochentag = :wochentag "
          + "AND kl.kurs.zeitBeginn = :zeitBeginn "
          + "AND kl.lehrkraft.personId IN (:mitarbeiterIds)")
  int countKurseBySemesterIdAndWochentagAndZeitBeginnAndMitarbeiterIdIn(
      @Param("semesterId") int semesterId,
      @Param("wochentag") Wochentag wochentag,
      @Param("zeitBeginn") Time zeitBeginn,
      @Param("mitarbeiterIds") List<Integer> mitarbeiterIds);

  @Query(
      "SELECT COUNT(kl) FROM KursLehrkraft kl "
          + "WHERE kl.kurs.semester.semesterId = :semesterId "
          + "AND kl.kurs.wochentag = :wochentag "
          + "AND kl.kurs.zeitBeginn = :zeitBeginn "
          + "AND kl.lehrkraft.personId IN (:mitarbeiterIds) "
          + "AND kl.kurs.kursId <> :kursId")
  int countKurseBySemesterIdAndWochentagAndZeitBeginnAndMitarbeiterIdInAndKursIdNe(
      @Param("semesterId") int semesterId,
      @Param("wochentag") Wochentag wochentag,
      @Param("zeitBeginn") Time zeitBeginn,
      @Param("mitarbeiterIds") List<Integer> mitarbeiterIds,
      @Param("kursId") int kursId);

  @Query(
      "SELECT kl.lehrkraft FROM KursLehrkraft kl "
          + "WHERE kl.kurs.kursId = :kursId "
          + "ORDER BY kl.lehrkraefteOrder")
  List<Mitarbeiter> findLehrkraefteByKursIdOrderByLehrkraefteOrder(@Param("kursId") int kursId);
}
