package ch.metzenthin.svm.persistence.entities;

import java.io.Serializable;

/**
 * @author Martin Schraner
 */
public class SemesterrechnungId implements Serializable {

    private Integer semester;
    private Integer rechnungsempfaenger;

    @SuppressWarnings("unused")  // used by JPA
    public SemesterrechnungId() {
    }

    public SemesterrechnungId(Integer semester, Integer rechnungsempfaenger) {
        this.semester = semester;
        this.rechnungsempfaenger = rechnungsempfaenger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SemesterrechnungId that = (SemesterrechnungId) o;

        return semester.equals(that.semester) && rechnungsempfaenger.equals(that.rechnungsempfaenger);

    }

    @Override
    public int hashCode() {
        int result = semester.hashCode();
        result = 31 * result + rechnungsempfaenger.hashCode();
        return result;
    }
}
