package ch.metzenthin.svm.common.utils;

import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.*;

/**
 * @author Martin Schraner
 */
public class MaercheneinteilungenSorter {

    public Map<Schueler, Maercheneinteilung> sortMaercheneinteilungenByGruppeAndRolle(Map<Schueler, Maercheneinteilung> maercheneinteilungen) {
        List<Map.Entry<Schueler, Maercheneinteilung>> list = new LinkedList<>(maercheneinteilungen.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Schueler, Maercheneinteilung>>() {
            @Override
            public int compare(Map.Entry<Schueler, Maercheneinteilung> o1, Map.Entry<Schueler, Maercheneinteilung> o2) {
                int result = (o1.getValue().getGruppe().compareTo(o2.getValue().getGruppe()));
                if (result == 0) {
                    Comparator<String> stringNumberComparator = new StringNumberComparator();
                    result = stringNumberComparator.compare(o1.getValue().getRolle1(), o2.getValue().getRolle1());
                }
                return result;
            }
        });

        Map<Schueler, Maercheneinteilung> result = new LinkedHashMap<>();
        for (Map.Entry<Schueler, Maercheneinteilung> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
