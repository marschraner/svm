package ch.metzenthin.svm.common.dataTypes;

import ch.metzenthin.svm.common.utils.SvmProperties;

/**
 * @author Martin Schraner
 */
public enum Listentyp {
    SCHUELER_ADRESSLISTE("Schüler-Adressliste", null, Filetyp.DOCX),
    SCHUELER_ABSENZENLISTE("Absenzenliste", SvmProperties.KEY_ABSENZENLISTE_TEMPLATE, Filetyp.DOCX),
    ROLLENLISTE("Rollenliste", null, Filetyp.DOCX),
    ELTERNMITHILFE_LISTE("Eltern-Mithilfe-Liste", null, Filetyp.DOCX),
    SCHUELER_ADRESSETIKETTEN("Schüler-Adressetiketten", null, Filetyp.CSV),
    RECHNUNGSEMPFAENGER_ADRESSETIKETTEN("Rechnungsempfaenger-Adressetiketten", null, Filetyp.CSV),
    MUTTER_ODER_VATER_ADRESSETIKETTEN("Mutter- oder Vater-Adressetiketten", null, Filetyp.CSV),
    ELTERNMITHILFE_ADRESSETIKETTEN("Eltern-Mithilfe-Adressetiketten", null, Filetyp.CSV),
    SCHUELERLISTE_CSV("Schülerliste (CSV)", null, Filetyp.CSV),
    MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM("Mitarbeiter-Adressliste mit Geburtsdatum", null, Filetyp.DOCX),
    MITARBEITER_ADRESSLISTE_OHNE_GEBURTSDATUM("Mitarbeiter-Adressliste ohne Geburtsdatum", null, Filetyp.DOCX),
    MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM_AHV_VERTRETUNGSMOEGLICHKEITEN("Mitarbeiter-Adressliste mit Geb.Datum, AHV-Nr., Vertr.mögl.", null, Filetyp.DOCX),
    VERTRETUNGSLISTE("Vertretungsliste", null, Filetyp.DOCX),
    MITARBEITER_ADRESSETIKETTEN("Mitarbeiter-Adressetiketten", null, Filetyp.CSV),
    MITARBEITER_LISTE_NAME_ZWEISPALTIG_CSV("Mitarbeiter-Liste Name zweispaltig (CSV)", null, Filetyp.CSV),
    MITARBEITER_LISTE_NAME_EINSPALTIG_CSV("Mitarbeiter-Liste Name einspaltig (CSV)", null, Filetyp.CSV),
    KURSLISTE_WORD("Kursliste (Word)", null, Filetyp.DOCX),
    KURSLISTE_CSV("Kursliste (CSV)", null, Filetyp.CSV),
    VORRECHNUNGEN_SERIENBRIEF("Vorrechnungen-Serienbrief", null, Filetyp.CSV),
    NACHRECHNUNGEN_SERIENBRIEF("Nachrechnungen-Serienbrief", null, Filetyp.CSV),
    MAHNUNGEN_SERIENBRIEF("Mahnungen-Serienbrief", null, Filetyp.CSV),
    SEMESTERRECHNUNGEN_ADRESSETIKETTEN("Rechnungsempfänger-Adressetiketten", null, Filetyp.CSV),
    RECHNUNGSLISTE("Rechnungsliste", null, Filetyp.CSV);

    private String name;
    private String svmPropertiesKey;
    private Filetyp filetyp;

    Listentyp(String name, String svmPropertiesKey, Filetyp filetyp) {
        this.name = name;
        this.svmPropertiesKey = svmPropertiesKey;
        this.filetyp = filetyp;
    }

    public String getSvmPropertiesKey() {
        return svmPropertiesKey;
    }

    public Filetyp getFiletyp() {
        return filetyp;
    }

    @Override
    public String toString() {
        return name;
    }
}
