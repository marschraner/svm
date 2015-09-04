package ch.metzenthin.svm.common.dataTypes;

/**
 * @author Martin Schraner
 */
public enum Stipendium {
    KEINES("", 1.),
    STIPENDIUM_10("10 %", 0.1),
    STIPENDIUM_20("20 %", 0.2),
    STIPENDIUM_30("30 %", 0.3),
    STIPENDIUM_40("40 %", 0.4),
    STIPENDIUM_50("50 %", 0.5),
    STIPENDIUM_60("60 %", 0.6),
    STIPENDIUM_70("70 %", 0.7),
    STIPENDIUM_80("80 %", 0.8),
    STIPENDIUM_90("90 %", 0.9);

    private String name;
    private double faktor;

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