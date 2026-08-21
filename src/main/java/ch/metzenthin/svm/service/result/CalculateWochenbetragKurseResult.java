package ch.metzenthin.svm.service.result;

import java.math.BigDecimal;

/**
 * @author Hans Stamm
 */
public record CalculateWochenbetragKurseResult(
    BigDecimal wochenbetragKurse, boolean allLektionsgebuehrenForKurslaengenFound) {}
