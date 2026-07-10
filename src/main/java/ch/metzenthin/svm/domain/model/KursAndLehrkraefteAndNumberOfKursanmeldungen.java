package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import java.util.List;

public record KursAndLehrkraefteAndNumberOfKursanmeldungen(
    Kurs kurs, List<Mitarbeiter> lehrkraefte, long numberOfKursanmeldungen) {}
