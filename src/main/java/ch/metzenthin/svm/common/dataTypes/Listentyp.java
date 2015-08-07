package ch.metzenthin.svm.common.dataTypes;

import ch.metzenthin.svm.common.utils.SvmProperties;

/**
 * @author Martin Schraner
 */
public enum Listentyp {
    SCHUELER_ADRESSLISTE("Adressliste", null, Filetyp.DOCX),
    SCHUELER_ABSENZENLISTE("Absenzenliste", SvmProperties.KEY_ABSENZENLISTE_TEMPLATE, Filetyp.DOCX),
    SCHUELER_ADRESSETIKETTEN("Schüler-Adressetiketten", null, Filetyp.CSV),
    LEHRKRAEFTE_ADRESSLISTE("Adressliste", null, Filetyp.DOCX),
    LEHRKRAEFTE_ADRESSETIKETTEN("Adressetiketten", null, Filetyp.CSV),
    KURSELISTE("Kurseliste", null, Filetyp.DOCX);

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
