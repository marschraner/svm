package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

/**
 * @author Martin Schraner
 */
public record MitarbeiterAndMitarbeiterCode(
    Mitarbeiter mitarbeiter, MitarbeiterCode mitarbeiterCode) {}
