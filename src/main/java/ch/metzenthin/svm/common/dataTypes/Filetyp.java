package ch.metzenthin.svm.common.dataTypes;

/**
 * @author Martin Schraner
 */
public enum Filetyp {
    DOCX("Microsoft Word", "docx"),
    CSV("CSV", "csv");

    private final String bezeichnung;
    private final String fileExtension;

    Filetyp(String bezeichnung, String fileExtension) {
        this.bezeichnung = bezeichnung;
        this.fileExtension = fileExtension;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}
