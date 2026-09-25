package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;

/**
 * @author Martin Schraner
 */
public record MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert(
    Mitarbeiter mitarbeiter, String mitarbeiterCodesAsString, boolean selektiert) {}
