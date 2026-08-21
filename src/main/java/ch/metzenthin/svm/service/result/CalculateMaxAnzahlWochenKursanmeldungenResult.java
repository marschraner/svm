package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public record CalculateMaxAnzahlWochenKursanmeldungenResult(
    int maxAnzahlWochen, boolean kursanmeldungenWithDifferentAnzahlWochen) {}
