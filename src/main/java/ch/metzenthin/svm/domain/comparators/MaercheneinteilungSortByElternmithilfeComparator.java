package ch.metzenthin.svm.domain.comparators;

import ch.metzenthin.svm.common.dataTypes.Elternmithilfe;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.persistence.entities.Person;

import java.util.Comparator;

/**
 * @author Martin Schraner
 */
public class MaercheneinteilungSortByElternmithilfeComparator implements Comparator<Maercheneinteilung> {

    @Override
    public int compare(Maercheneinteilung maercheneinteilung1, Maercheneinteilung maercheneinteilung2) {

        // 1. Sortierung nach Elternmithilfe
        Person elternmithilfe1 = getElternmithilfe(maercheneinteilung1);
        Person elternmithilfe2 = getElternmithilfe(maercheneinteilung2);
        if (elternmithilfe1 == null || elternmithilfe2 == null) {
            if (elternmithilfe1 != null) {
                return 1;
            } else if (elternmithilfe2 != null) {
                return -1;
            } else {
                return 0;
            }
        }
        PersonComparator personComparator = new PersonComparator();
        int result = personComparator.compare(elternmithilfe1, elternmithilfe2);

        if (result == 0) {
           // 2. Sortierung nach Gruppe
           result =  maercheneinteilung1.getGruppe().name().compareTo(maercheneinteilung2.getGruppe().name());
        }

        if (result == 0) {
            // 3. Sortierung nach Schueler
            result = personComparator.compare(maercheneinteilung1.getSchueler(), maercheneinteilung2.getSchueler());
        }

        return result;
    }

    private Person getElternmithilfe(Maercheneinteilung maercheneinteilung) {
        if (maercheneinteilung == null || maercheneinteilung.getElternmithilfe() == null) {
            return null;
        }
        if (maercheneinteilung.getElternmithilfe() == Elternmithilfe.MUTTER) {
            return maercheneinteilung.getSchueler().getMutter();
        } else if (maercheneinteilung.getElternmithilfe() == Elternmithilfe.VATER) {
            return  maercheneinteilung.getSchueler().getVater();
        } else {
            return  maercheneinteilung.getElternmithilfeDrittperson();
        }
    }
}
