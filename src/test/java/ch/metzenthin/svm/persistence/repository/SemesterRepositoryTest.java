package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

/**
 * @author Hans Stamm
 */
@DataJpaTest
@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@Sql(scripts = "classpath:SemesterRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:SemesterRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class SemesterRepositoryTest {

  @Autowired private SemesterRepository semesterRepository;

  @Test
  void testCountBySemesterId() {
    int numberOfSemesters = semesterRepository.countBySemesterId(101);
    assertEquals(1, numberOfSemesters);

    numberOfSemesters = semesterRepository.countBySemesterId(999);
    assertEquals(0, numberOfSemesters);
  }

  @Test
  void testCountBySchuljahrAndSemesterbezeichnung() {
    int numberOfSemesters =
        semesterRepository.countBySchuljahrAndSemesterbezeichnung(
            "2017/2018", Semesterbezeichnung.ERSTES_SEMESTER);
    assertEquals(1, numberOfSemesters);

    numberOfSemesters =
        semesterRepository.countBySchuljahrAndSemesterbezeichnung(
            "2015/2016", Semesterbezeichnung.ERSTES_SEMESTER);
    assertEquals(0, numberOfSemesters);
  }

  @Test
  void testCountBySchuljahrAndSemesterbezeichnungAndIdNe() {
    int numberOfSemesters =
        semesterRepository.countBySchuljahrAndSemesterbezeichnungAndIdNe(
            "2017/2018", Semesterbezeichnung.ERSTES_SEMESTER, 102);
    assertEquals(1, numberOfSemesters);

    numberOfSemesters =
        semesterRepository.countBySchuljahrAndSemesterbezeichnungAndIdNe(
            "2017/2018", Semesterbezeichnung.ZWEITES_SEMESTER, 102);
    assertEquals(0, numberOfSemesters);
  }

  @Test
  void testFindAllOrderBySemesterbeginnAndSemesterendeDesc() {
    List<Semester> semestersFound =
        semesterRepository.findAllOrderBySemesterbeginnAndSemesterendeDesc();

    assertEquals(3, semestersFound.size());
    assertEquals("2018/2019", semestersFound.get(0).getSchuljahr());
    assertEquals(
        Semesterbezeichnung.ERSTES_SEMESTER, semestersFound.get(0).getSemesterbezeichnung());
    assertEquals("2017/2018", semestersFound.get(1).getSchuljahr());
    assertEquals(
        Semesterbezeichnung.ZWEITES_SEMESTER, semestersFound.get(1).getSemesterbezeichnung());
    assertEquals("2017/2018", semestersFound.get(2).getSchuljahr());
    assertEquals(
        Semesterbezeichnung.ERSTES_SEMESTER, semestersFound.get(2).getSemesterbezeichnung());
  }

  @Test
  void testFindBySchuljahrAndSemesterbezeichnung() {
    Optional<Semester> semesterOptional;
    semesterOptional =
        semesterRepository.findBySchuljahrAndSemesterbezeichnung(
            "2018/2019", Semesterbezeichnung.ERSTES_SEMESTER);
    assertTrue(semesterOptional.isPresent());
    assertEquals("2018/2019", semesterOptional.get().getSchuljahr());
    assertEquals(
        Semesterbezeichnung.ERSTES_SEMESTER, semesterOptional.get().getSemesterbezeichnung());

    semesterOptional =
        semesterRepository.findBySchuljahrAndSemesterbezeichnung(
            "2018/2019", Semesterbezeichnung.ZWEITES_SEMESTER);
    assertTrue(semesterOptional.isEmpty());

    semesterOptional =
        semesterRepository.findBySchuljahrAndSemesterbezeichnung(
            "2019/2020", Semesterbezeichnung.ERSTES_SEMESTER);
    assertTrue(semesterOptional.isEmpty());
  }
}
