package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Maerchen;

public record MaerchenAndNumberOfMaercheneinteilungen(
    Maerchen maerchen, long numberOfMaercheneinteilungen) {}
