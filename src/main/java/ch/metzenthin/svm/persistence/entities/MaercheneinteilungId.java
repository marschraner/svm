package ch.metzenthin.svm.persistence.entities;

import java.io.Serializable;

/**
 * @author Martin Schraner
 */
public class MaercheneinteilungId implements Serializable {

    private Integer schueler;
    private Integer maerchen;

    @SuppressWarnings("unused")  // used by JPA
    public MaercheneinteilungId() {
    }

    public MaercheneinteilungId(Integer schueler, Integer maerchen) {
        this.schueler = schueler;
        this.maerchen = maerchen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MaercheneinteilungId that = (MaercheneinteilungId) o;

        return schueler.equals(that.schueler) && maerchen.equals(that.maerchen);

    }

    @Override
    public int hashCode() {
        int result = schueler.hashCode();
        result = 31 * result + maerchen.hashCode();
        return result;
    }
}
