package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;

public record KursIdAndLehrkraft(int kursId, Mitarbeiter lehrkraft) {}
