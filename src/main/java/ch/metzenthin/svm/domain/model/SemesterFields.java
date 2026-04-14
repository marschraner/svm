package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;

/**
 * @author Martin Schraner
 */
public record SemesterFields(
    String schuljahr,
    Semesterbezeichnung semesterbezeichnung,
    String semesterbeginn,
    String semesterende,
    String ferienbeginn1,
    String ferienende1,
    String ferienbeginn2,
    String ferienende2) {}
