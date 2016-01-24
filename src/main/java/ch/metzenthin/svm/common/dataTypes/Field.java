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
    CODE("SchuelerCode"),
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
    ANZAHL_KINDER("Kinder"),
    BULK_UPDATE("Bulk Update"), 
    MAERCHEN("Märchen"), 
    GRUPPE("Gruppe"),
    ROLLEN("Rollen"),
    ROLLE1("Rolle 1"), 
    BILDER_ROLLE1("Bilder Rolle 1"), 
    ROLLE2("Rolle 2"), 
    BILDER_ROLLE2("Bilder Rolle 2"), 
    ROLLE3("Rolle 3"), 
    BILDER_ROLLE3("Bilder Rolle 3"),
    ELTERNMITHILFE("Eltern-Mithilfe"),
    SCHUELER_CODE("Code"),
    ELTERNMITHILFE_CODE("Code"),
    KUCHEN_VORSTELLUNGEN("Vorst. m. Kuchen"),
    ANZAHL_VORSTELLUNGEN("Vorstellungen"),
    KUCHEN_VORSTELLUNG("Vorstellung mit Kuchen"),
    KUCHEN_VORSTELLUNG1("1. Vorstellung mit Kuchen"),
    KUCHEN_VORSTELLUNG2("2. Vorstellung mit Kuchen"),
    KUCHEN_VORSTELLUNG3("3. Vorstellung mit Kuchen"),
    KUCHEN_VORSTELLUNG4("4. Vorstellung mit Kuchen"),
    KUCHEN_VORSTELLUNG5("5. Vorstellung mit Kuchen"),
    KUCHEN_VORSTELLUNG6("6. Vorstellung mit Kuchen"),
    KUCHEN_VORSTELLUNG7("7. Vorstellung mit Kuchen"),
    KUCHEN_VORSTELLUNG8("8. Vorstellung mit Kuchen"),
    KUCHEN_VORSTELLUNG9("9. Vorstellung mit Kuchen"),
    EMAIL_EMPFAENGER("E-Mail-Empfänger"),
    MAERCHEN_FUER_SUCHE_BERUECKSICHTIGEN("Märchen für Suche berücksichtigen"),
    ZUSATZATTRIBUT_MAERCHEN("Zusatzattribut"),
    GEBURTSDATUM_SHORT("Geb.Datum"),
    SEMESTER_KURS("Semester"),
    SELEKTIERBAR("selektierbar"),
    LEKTIONSLAENGE("Lektionslänge"),
    BETRAG_1_KIND("1 Kind"),
    BETRAG_2_KINDER("2 Kinder"),
    BETRAG_3_KINDER("3 Kinder"),
    BETRAG_4_KINDER("4 Kinder"),
    BETRAG_5_KINDER("5 Kinder"),
    BETRAG_6_KINDER("6 Kinder"),
    RECHNUNGSDATUM_SELECTED("Rechnungsdatum"),
    RECHNUNGSSTATUS("Rechnungsstatus"),
    SEMESTERRECHNUNG_CODE("Semesterrechnung-Code"),
    STIPENDIUM("Stipendium"),
    GRATISKINDER("Gratiskinder"),
    SECHS_JAHRES_RABATT_VORRECHNUNG("6-Jahres-Rabatt V"),
    RECHNUNGSDATUM_VORRECHNUNG("R.Datum V"),
    ERMAESSIGUNG_VORRECHNUNG("Ermäss. V"),
    ERMAESSIGUNGSGRUND_VORRECHNUNG("Ermässigungsgrund V"),
    ZUSCHLAG_VORRECHNUNG("Zuschl. V"),
    ZUSCHLAGSGRUND_VORRECHNUNG("Zuschlagsgrund V"),
    ANZAHL_WOCHEN_VORRECHNUNG("Anz. V"),
    WOCHENBETRAG_VORRECHNUNG("Wochenb. V"),
    RECHNUNGSBETRAG_VORRECHNUNG("Rechn.b. V"),
    DATUM_ZAHLUNG_1_VORRECHNUNG("Datum 1. Zahlung V"),
    BETRAG_ZAHLUNG_1_VORRECHNUNG("1. Zahlung V"),
    DATUM_ZAHLUNG_2_VORRECHNUNG("Datum 2. Zahlung V"),
    BETRAG_ZAHLUNG_2_VORRECHNUNG("2. Zahlung V"),
    DATUM_ZAHLUNG_3_VORRECHNUNG("Datum 3. Zahlung V"),
    BETRAG_ZAHLUNG_3_VORRECHNUNG("3. Zahlung V"),
    RESTBETRAG_VORRECHNUNG("Restbetrag V"),
    RECHNUNGSDATUM_NACHRECHNUNG("R.Datum N"),
    ERMAESSIGUNG_NACHRECHNUNG("Ermäss. N"),
    ERMAESSIGUNGSGRUND_NACHRECHNUNG("Ermässigungsgrund N"),
    ZUSCHLAG_NACHRECHNUNG("Zuschl. N"),
    ZUSCHLAGSGRUND_NACHRECHNUNG("Zuschlagsgrund N"),
    ANZAHL_WOCHEN_NACHRECHNUNG("Anz. N"),
    WOCHENBETRAG_NACHRECHNUNG("Wochenb. N"),
    RECHNUNGSBETRAG_NACHRECHNUNG("Rechn.b. N"),
    DIFFERENZ_SCHULGELD("Diff. Schulgeld"),
    DATUM_ZAHLUNG_1_NACHRECHNUNG("Datum 1. Zahlung N"),
    BETRAG_ZAHLUNG_1_NACHRECHNUNG("1. Zahlung N"),
    DATUM_ZAHLUNG_2_NACHRECHNUNG("Datum 2. Zahlung N"),
    BETRAG_ZAHLUNG_2_NACHRECHNUNG("2. Zahlung N"),
    DATUM_ZAHLUNG_3_NACHRECHNUNG("Datum 3. Zahlung N"),
    BETRAG_ZAHLUNG_3_NACHRECHNUNG("3. Zahlung N"),
    RESTBETRAG_NACHRECHNUNG("Restbetrag N"),
    RECHNUNGSDATUM_GESETZT_VORRECHNUNG("Vollständigkeit V"),
    PRAEZISIERUNG_RECHNUNGSDATUM_VORRECHNUNG("Präzisierung Rechnungsdatum V"),
    AM_VORRECHNUNG("Am V"),
    VOR_VORRECHNUNG("Vor V"),
    NACH_VORRECHNUNG("Nach V"),
    PRAEZISIERUNG_ERMAESSIGUNG_VORRECHNUNG("Präzisierung Ermässigung V"),
    PRAEZISIERUNG_ZUSCHLAG_VORRECHNUNG("Präzisierung Zuschlag V"),
    PRAEZISIERUNG_ANZAHL_WOCHEN_VORRECHNUNG("Präzisierung Anzahl Wochen V"),
    PRAEZISIERUNG_WOCHENBETRAG_VORRECHNUNG("Präzisierung Wochenbetrag V"),
    PRAEZISIERUNG_RECHNUNGSBETRAG_VORRECHNUNG("Präzisierung Rechnungsbetrag V"),
    RECHNUNGSDATUM_GESETZT_NACHRECHNUNG("Vollständigkeit N"),
    PRAEZISIERUNG_RECHNUNGSDATUM_NACHRECHNUNG("Präzisierung Rechnungsdatum N"),
    PRAEZISIERUNG_RESTBETRAG_VORRECHNUNG("Präzisierung Restbetrag V"),
    AM_NACHRECHNUNG("Am N"),
    VOR_NACHRECHNUNG("Vor N"),
    NACH_NACHRECHNUNG("Nach N"),
    PRAEZISIERUNG_ERMAESSIGUNG_NACHRECHNUNG("Präzisierung Ermässigung N"),
    PRAEZISIERUNG_ZUSCHLAG_NACHRECHNUNG("Präzisierung Zuschlag N"),
    PRAEZISIERUNG_ANZAHL_WOCHEN_NACHRECHNUNG("Präzisierung Anzahl Wochen N"),
    PRAEZISIERUNG_WOCHENBETRAG_NACHRECHNUNG("Präzisierung Wochenbetrag N"),
    PRAEZISIERUNG_RECHNUNGSBETRAG_NACHRECHNUNG("Präzisierung Rechnungsbetrag N"),
    PRAEZISIERUNG_RESTBETRAG_NACHRECHNUNG("Präzisierung Restbetrag N"),
    PRAEZISIERUNG_DIFFERENZ_SCHULGELD("Präzisierung Differenz Schulgeld N-V"),
    SECHS_JAHRES_RABATT_NACHRECHNUNG("6-Jahres-Rabatt N"),
    RECHNUNGSDATUM("Rechnungsdatum"),
    SEMESTERRECHNUNG_CODE_JA_NEIN("Semesterrechnung-Code ja/nein"),
    STIPENDIUM_JA_NEIN("Stipendium ja/nein"),
    CODES("Codes"),
    MITARBEITER_CODE("Code"),
    LEHRKRAFT_JA_NEIN("Lehrkraft"),
    STATUS("Status");

    private String name;

    Field(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
