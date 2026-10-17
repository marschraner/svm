package ch.metzenthin.svm.domain.model;

import java.util.Optional;

/**
 * @author Martin Schraner
 */
public record SaveSuccessfulAndEntityIdOptional(
    boolean saveSuccessful, Optional<Integer> entityIdOptional) {}
