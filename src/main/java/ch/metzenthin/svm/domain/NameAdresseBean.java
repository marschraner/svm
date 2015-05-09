package ch.metzenthin.svm.domain;

public class NameAdresseBean implements Person {
    private String nachname;
    private String vorname;
    private String strasse;
    private String plz;
    private String ort;

    public NameAdresseBean() {
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(final String nachname) {
        this.nachname = nachname;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(final String vorname) {
        this.vorname = vorname;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(final String strasse) {
        this.strasse = strasse;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(final String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(final String ort) {
        this.ort = ort;
    }
}