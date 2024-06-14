package ch.metzenthin.svm.common.dataTypes;

/**
 * @author Martin Schraner
 */
public record RechnungSerienbrief(String anrede, String vorname, String nachname, String strasse,
                                  String plz, String ort, String email, String rechnungsdatum,
                                  String bemerkungen, String anzahlWochen, String wochenbetrag,
                                  String schulgeld, String ermaessigung, String zuschlag,
                                  String stipendium, String rechnungsbetrag, String schueler) {
}
