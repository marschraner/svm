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
    PLZ("PLZ"),
    ORT("Ort"),
    FESTNETZ("Festnetz"),
    GEBURTSDATUM("Geburtsdatum"),
    GEBURTSDATUM_SUCHPERIODE("Geburtsdatum Suchperiode"),
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
    MUTTER("Mutter"),
    VATER("Vater"),
    ROLLE_ALLE("alle"),
    STICHTAG("Stichtag"),
    MONAT_JAHR("Monat/Jahr"),
    AN_ABMELDUNGEN_DISPENSATIONEN("An-/Abmeldungen/Dispensationen"),
    ANMELDUNGEN("Anmeldungen"),
    ABMELDUNGEN("Abmeldungen"),
    DISPENSATIONEN("Dispensationen"),
    BEMERKUNGEN("Bemerkungen"),
    LEHRKRAFT("Lehrkraft"),
    WOCHENTAG("Wochentag"),
    VON("von"),
    BIS("bis"),
    CODE("Code"),
    DISPENSATIONSBEGINN("Dispensationsbeginn"),
    DISPENSATIONSENDE("Dispensationsende"),
    VORAUSSICHTLICHE_DAUER("voraussichtliche Dauer"),
    GRUND("Grund"),
    KUERZEL("Kürzel"),
    BESCHREIBUNG("Beschreibung"),
    AHV_NUMMER("AHV-Nr."),
    VERTRETUNGSMOEGLICHKEITEN("Vertretungsmöglichkeiten"),
    AKTIV("aktiv"),
    BULK_UPDATE("Bulk Update");

    private String name;

    Field(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
