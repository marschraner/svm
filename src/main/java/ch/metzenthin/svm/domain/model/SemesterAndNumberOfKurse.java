package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Semester;

public record SemesterAndNumberOfKurse(Semester semester, long numberOfKurse) {}
