package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.entityfields.AdresseFields;
import ch.metzenthin.svm.domain.model.entityfields.MitarbeiterFields;

/**
 * @author Martin Schraner
 */
public record MitarbeiterFieldsAndAdresseFields(
    MitarbeiterFields mitarbeiterFields, AdresseFields adresseFields) {}
