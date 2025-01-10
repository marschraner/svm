package ch.metzenthin.svm.common.datatypes;

/**
 * @author Martin Schraner
 */
public enum Stipendium {
    ALLE("", 1),
    KEINES("", 1.),
    STIPENDIUM_10("10 %", 0.9),
    STIPENDIUM_20("20 %", 0.8),
    STIPENDIUM_30("30 %", 0.7),
    STIPENDIUM_40("40 %", 0.6),
    STIPENDIUM_50("50 %", 0.5),
    STIPENDIUM_60("60 %", 0.4),
    STIPENDIUM_70("70 %", 0.3),
    STIPENDIUM_80("80 %", 0.2),
    STIPENDIUM_90("90 %", 0.1);

    private final String name;
    private final double faktor;

    Stipendium(String name, double faktor) {
        this.name = name;
        this.faktor = faktor;
    }

    @Override
    public String toString() {
        return name;
    }

    public double getFaktor() {
        return faktor;
    }
}
