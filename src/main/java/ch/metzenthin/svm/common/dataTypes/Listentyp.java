package ch.metzenthin.svm.common.dataTypes;

import ch.metzenthin.svm.common.utils.SvmProperties;

/**
 * @author Martin Schraner
 */
public enum Listentyp {
    SCHUELER_ADRESSLISTE("Adressliste", null, Filetyp.DOCX),
    SCHUELER_ABSENZENLISTE("Absenzenliste", SvmProperties.KEY_ABSENZENLISTE_TEMPLATE, Filetyp.DOCX),
    ROLLENLISTE("Rollenliste", null, Filetyp.DOCX),
    ELTERNMITHILFE_LISTE("Eltern-Mithilfe-Liste", null, Filetyp.DOCX),
    SCHUELER_ADRESSETIKETTEN("Schüler-Adressetiketten", null, Filetyp.CSV),
    RECHNUNGSEMPFAENGER_ADRESSETIKETTEN("Rechnungsempfaenger-Adressetiketten", null, Filetyp.CSV),
    MUTTER_ODER_VATER_ADRESSETIKETTEN("Mutter- oder Vater-Adressetiketten", null, Filetyp.CSV),
    ELTERNMITHILFE_ADRESSETIKETTEN("Eltern-Mithilfe-Adressetiketten", null, Filetyp.CSV),
    LEHRKRAEFTE_ADRESSLISTE("Adressliste", null, Filetyp.DOCX),
    LEHRKRAEFTE_ADRESSETIKETTEN("Adressetiketten", null, Filetyp.CSV),
    KURSLISTE("Kursliste", null, Filetyp.DOCX),
    VORRECHNUNGEN_SERIENBRIEF("Vorrechnungen-Serienbrief", null, Filetyp.CSV),
    NACHRECHNUNGEN_SERIENBRIEF("Nachrechnungen-Serienbrief", null, Filetyp.CSV),
    MAHNUNGEN_SERIENBRIEF("Mahnungen-Serienbrief", null, Filetyp.CSV),
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
