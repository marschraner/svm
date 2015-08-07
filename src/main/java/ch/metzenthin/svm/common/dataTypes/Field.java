package ch.metzenthin.svm.common.dataTypes;

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
    LEHRKRAFT1("Lehrkraft 1"),
    LEHRKRAFT2("Lehrkraft 2"),
    WOCHENTAG("Wochentag"),
    VON("von"),
    BIS("bis"),
    CODE("Code"),
    DISPENSATIONSBEGINN("Dispensationsbeginn"),
    DISPENSATIONSENDE("Dispensationsende"),
    VORAUSSICHTLICHE_DAUER("Voraussichtliche Dauer"),
    GRUND("Grund"),
    KUERZEL("Kürzel"),
    BESCHREIBUNG("Beschreibung"),
    AHV_NUMMER("AHV-Nr."),
    VERTRETUNGSMOEGLICHKEITEN("Vertretungsmöglichkeiten"),
    AKTIV("aktiv"),
    BEZEICHNUNG("Bezeichnung"),
    SCHULJAHR("Schuljahr"),
    SEMESTERBEZEICHNUNG("Semester"),
    SEMESTERBEGINN("Semesterbeginn"),
    SEMESTERENDE("Semesterende"),
    ANZAHL_SCHULWOCHEN("Schulwochen"),
    KURSTYP_BEZEICHNUNG("Kurstyp"),
    KURSTYP("Kurstyp"),
    KURSORT("Kursort"),
    ALTERSBEREICH("Alter"),
    STUFE("Stufe"),
    TAG("Tag"),
    ZEIT_BEGINN("Von"),
    ZEIT_ENDE("Bis"),
    LEITUNG("Leitung"),
    ANZAHL_KURSE("Kurse"),
    ANZAHL_SCHUELER("Schüler"),
    SEMESTER("Semester"),
    SCHULJAHR_KURS("Schuljahr"),
    KURS_FUER_SUCHE_BERUECKSICHTIGEN("Kurs für Suche berücksichtigen"),
    KURS1("1. Kurs"),
    TITEL("Titel"),
    LISTENTYP("Listentyp"),
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
