package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Maerchen;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Schraner
 */
public class FindMaercheneinteilungenMapSchuelerSemesterCommand implements Command {

    // input
    private final List<Schueler> schuelerList;
    private final Maerchen maerchen;

    // output
    private final Map<Schueler, Maercheneinteilung> maercheneinteilungenMap = new HashMap<>();

    public FindMaercheneinteilungenMapSchuelerSemesterCommand(List<Schueler> schuelerList, Maerchen maerchen) {
        this.schuelerList = schuelerList;
        this.maerchen = maerchen;
    }

    @Override
    public void execute() {
        if (maerchen == null) {
            return;
        }
        for (Schueler schueler : schuelerList) {
            for (Maercheneinteilung maercheneinteilung : schueler.getMaercheneinteilungen()) {
                if (maercheneinteilung.getMaerchen().isIdenticalWith(maerchen)) {
                    maercheneinteilungenMap.put(schueler, maercheneinteilung);
                    break;
                }
            }
        }

    }

    public Map<Schueler, Maercheneinteilung> getMaercheneinteilungenMap() {
        return maercheneinteilungenMap;
    }
}
