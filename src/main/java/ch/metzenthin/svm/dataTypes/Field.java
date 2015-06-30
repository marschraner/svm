package ch.metzenthin.svm.dataTypes;

/**
 * @author Martin Schraner
 */
public enum Field {
    ALLE("Alle"),
    ANREDE("Anrede"),
    NACHNAME("Nachname"),
    VORNAME("Vorname"),
    NATEL("Natel"),
    EMAIL("Email"),
    STRASSE_HAUSNUMMER("Strasse/Nr."),
    PLZ("Plz"),
    ORT("Ort"),
    FESTNETZ("Festnetz"),
    GEBURTSDATUM("Geburtsdatum"),
    RECHNUNGSEMPFAENGER("Rechnungsempfänger"),
    GLEICHE_ADRESSE_WIE_SCHUELER("Gleiche Adresse wie Schüler"),
    GESCHLECHT("Geschlecht"),
    ANMELDESTATUS("Anmeldestatus"),
    ANMELDEDATUM("Anmeldedatum"),
    ABMELDEDATUM("Abmeldedatum"),
    ANGEMELDET("angemeldet"),
    ABGEMELDET("abgemeldet"),
    ANMELDESTATUS_ALLE("alle"),
    DISPENSATION("Dispensation"),
    DISPENSIERT("dispensiert"),
    NICHT_DISPENSIERT("nicht dispensiert"),
    DISPENSATION_ALLE("alle"),
    WEIBLICH("weiblich"),
    MAENNLICH("männlich"),
    GESCHLECHT_ALLE("alle"),
    ROLLE("Rolle"),
    SCHUELER("Schüler"),
    ELTERN("Eltern"),
    ROLLE_ALLE("alle"),
    STICHTAG("Stichtag"),
    AN_ABMELDEMONAT("Monat/Jahr"),
    AN_ABMELDUNGEN("An-/Abmeldungen"),
    ANMELDUNGEN("Anmeldungen"),
    ABMELDUNGEN("Abmeldungen"),
    BEMERKUNGEN("Bemerkungen"),
    LEHRKRAFT("Lehrkraft"),
    WOCHENTAG("Wochentag"),
    VON("von"),
    BIS("bis"),
    CODES("Codes"),
    STAMMDATEN_BERUECKSICHTIGEN("Stammdaten berücksichtigen"),
    KURS_BERUECKSICHTIGEN("Kurs berücksichtigen"),
    CODES_BERUECKSICHTIGEN("Codes berücksichtigen"),
    AN_ABMELDESTATISTIK_BERUECKSICHTIGEN("An-/Abmeldestatistik berücksichtigen");

    private String name;

    Field(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
