package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import java.util.List;

/**
 * @author Martin Schraner
 */
public record MitarbeiterAndMitarbeiterCodes(
    Mitarbeiter mitarbeiter, List<MitarbeiterCode> mitarbeiterCodes) {}
