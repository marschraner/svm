package ch.metzenthin.svm.persistence.entities;

import java.io.Serializable;

/**
 * @author Martin Schraner
 */
public class KursanmeldungId implements Serializable {

    private Integer schueler;
    private Integer kurs;

    @SuppressWarnings("unused")  // used by JPA
    public KursanmeldungId() {
    }

    public KursanmeldungId(Integer schueler, Integer kurs) {
        this.schueler = schueler;
        this.kurs = kurs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KursanmeldungId that = (KursanmeldungId) o;

        return schueler.equals(that.schueler) && kurs.equals(that.kurs);

    }

    @Override
    public int hashCode() {
        int result = schueler.hashCode();
        result = 31 * result + kurs.hashCode();
        return result;
    }
}
