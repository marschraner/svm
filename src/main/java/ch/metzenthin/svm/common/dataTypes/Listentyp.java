package ch.metzenthin.svm.common.dataTypes;

import ch.metzenthin.svm.common.utils.SvmProperties;

/**
 * @author Martin Schraner
 */
public enum Listentyp {
    SCHUELER_ADRESSLISTE("Schüler-Adressliste", "Schüler-Adressliste", Filetyp.DOCX, null),
    ABSENZENLISTE_GANZES_SEMESTER("Absenzenliste ganzes Semester", "Absenzenliste", Filetyp.DOCX, SvmProperties.KEY_ABSENZENLISTEN_TEMPLATE_GANZES_SEMESTER),
    ABSENZENLISTE_OKTOBER_FEBRUAR("Absenzenliste Oktober - Februar", "Absenzenliste_Oktober-Februar", Filetyp.DOCX, SvmProperties.KEY_ABSENZENLISTEN_TEMPLATE_OKTOBER_FEBRUAR),
    SPEZIELLE_ABSENZENLISTE("Spezielle Absenzenliste", "Absenzenliste", Filetyp.DOCX, SvmProperties.KEY_SPEZIELLES_ABSENZENLISTEN_TEMPLATE),
    ROLLENLISTE("Rollenliste", "Rollenliste", Filetyp.DOCX, null),
    ELTERNMITHILFE_LISTE("Eltern-Mithilfe-Liste", "Eltern-Mithilfe-Liste", Filetyp.DOCX, null),
    SCHUELER_ADRESSETIKETTEN("Schüler-Adressetiketten", "Schüler-Adressetiketten", Filetyp.CSV, null),
    RECHNUNGSEMPFAENGER_ADRESSETIKETTEN("Rechnungsempfänger-Adressetiketten", "Rechnungsempfänger-Adressetiketten", Filetyp.CSV, null),
    MUTTER_ODER_VATER_ADRESSETIKETTEN("Mutter- oder Vater-Adressetiketten", "Mutter-_oder_Vater-Adressetiketten", Filetyp.CSV, null),
    ELTERNMITHILFE_ADRESSETIKETTEN("Eltern-Mithilfe-Adressetiketten", "Eltern-Mithilfe-Adressetiketten", Filetyp.CSV, null),
    PROBEPLAENE_ETIKETTEN("Probepläne-Etiketten", "Probepläne-Etiketten", Filetyp.CSV, null),
    SCHUELERLISTE_CSV("Schülerliste (CSV)", "Schülerliste", Filetyp.CSV, null),
    MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM("Mitarbeiter-Adressliste mit Geburtsdatum", "Mitarbeiter-Adressliste", Filetyp.DOCX, null),
    MITARBEITER_ADRESSLISTE_OHNE_GEBURTSDATUM("Mitarbeiter-Adressliste ohne Geburtsdatum", "Mitarbeiter-Adressliste", Filetyp.DOCX, null),
    MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM_AHV_VERTRETUNGSMOEGLICHKEITEN("Mitarbeiter-Adressliste mit Geb.Datum, AHV-Nr., Vertr.mögl.", "Mitarbeiter-Adressliste", Filetyp.DOCX, null),
    VERTRETUNGSLISTE("Vertretungsliste", "Vertretungsliste", Filetyp.DOCX, null),
    MITARBEITER_ADRESSETIKETTEN("Mitarbeiter-Adressetiketten", "Mitarbeiter-Adressetiketten", Filetyp.CSV, null),
    MITARBEITER_LISTE_NAME_ZWEISPALTIG_CSV("Mitarbeiter-Liste Name zweispaltig (CSV)", "Mitarbeiter-Liste", Filetyp.CSV, null),
    MITARBEITER_LISTE_NAME_EINSPALTIG_CSV("Mitarbeiter-Liste Name einspaltig (CSV)", "Mitarbeiter-Liste", Filetyp.CSV, null),
    KURSLISTE_WORD("Kursliste (Word)", "Kursliste", Filetyp.DOCX, null),
    KURSLISTE_CSV("Kursliste (CSV)", "Kursliste", Filetyp.CSV, null),
    VORRECHNUNGEN_SERIENBRIEF("Vorrechnungen-Serienbrief", "Vorrechnungen-Serienbrief", Filetyp.CSV, null),
    NACHRECHNUNGEN_SERIENBRIEF("Nachrechnungen-Serienbrief", "Nachrechnungen-Serienbrief", Filetyp.CSV, null),
    MAHNUNGEN_VORRECHNUNGEN_SERIENBRIEF("Mahnungen Vorrechnungen-Serienbrief", "Mahnungen_Vorrechnungen-Serienbrief", Filetyp.CSV, null),
    MAHNUNGEN_NACHRECHNUNGEN_SERIENBRIEF("Mahnungen Nachrechnungen-Serienbrief", "Mahnungen_Nachrechnungen-Serienbrief", Filetyp.CSV, null),
    SEMESTERRECHNUNGEN_ADRESSETIKETTEN("Rechnungsempfänger-Adressetiketten", "Rechnungsempfänger-Adressetiketten", Filetyp.CSV, null),
    RECHNUNGSLISTE("Rechnungsliste", "Rechnungsliste", Filetyp.CSV, null);

    private String name;
    private String filenameOhneFileExtension;
    private Filetyp filetyp;
    private String svmPropertiesKey;

    Listentyp(String name, String filenameOhneFileExtension, Filetyp filetyp, String svmPropertiesKey) {
        this.name = name;
        this.filenameOhneFileExtension = filenameOhneFileExtension;
        this.filetyp = filetyp;
        this.svmPropertiesKey = svmPropertiesKey;
    }

    public String getFilenameOhneFileExtension() {
        return filenameOhneFileExtension;
    }

    public Filetyp getFiletyp() {
        return filetyp;
    }

    public String getSvmPropertiesKey() {
        return svmPropertiesKey;
    }

    @Override
    public String toString() {
        return name;
    }
}
