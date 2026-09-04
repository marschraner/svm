package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.Optional;

/**
 * @author Martin Schraner
 */
public record PreviousCurrentNextSemester(
    Optional<Semester> previousSemesterOptional,
    Optional<Semester> currentSemesterOptional,
    Optional<Semester> nextSemesterOptional) {}
