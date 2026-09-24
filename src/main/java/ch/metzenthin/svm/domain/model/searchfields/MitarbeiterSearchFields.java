package ch.metzenthin.svm.domain.model.searchfields;

import ch.metzenthin.svm.domain.model.SearchMitarbeiterModel.LehrkraftJaNeinSelected;
import ch.metzenthin.svm.domain.model.SearchMitarbeiterModel.StatusSelected;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

/**
 * @author Hans Stamm
 */
public record MitarbeiterSearchFields(
    String nachname,
    String vorname,
    MitarbeiterCode mitarbeiterCode,
    LehrkraftJaNeinSelected lehrkraftJaNeinSelected,
    StatusSelected statusSelected) {}
