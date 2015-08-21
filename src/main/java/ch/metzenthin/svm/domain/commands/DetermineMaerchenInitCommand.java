package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.domain.model.SvmModel;
import ch.metzenthin.svm.persistence.entities.Maerchen;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class DetermineMaerchenInitCommand implements Command {

    // input
    private List<Maerchen> selectableMaerchens;

    // output
    private Maerchen maerchenInit;

    public DetermineMaerchenInitCommand(SvmModel svmModel) {
        this.selectableMaerchens = svmModel.getMaerchensAll();
    }

    public DetermineMaerchenInitCommand(List<Maerchen> selectableMaerchens) {
        this.selectableMaerchens = selectableMaerchens;
    }

    @Override
    public void execute() {
        Calendar today = new GregorianCalendar();
        int schuljahr1;
        if (today.get(Calendar.MONTH) <= Calendar.JANUARY) {
            schuljahr1 = today.get(Calendar.YEAR) - 1;
        } else {
            schuljahr1 = today.get(Calendar.YEAR);
        }
        int schuljahr2 = schuljahr1 + 1;
        String anzuzeigendesSchuljahr = schuljahr1 + "/" + schuljahr2;
        for (Maerchen maerchen : selectableMaerchens) {
            if (maerchen.getSchuljahr().equals(anzuzeigendesSchuljahr)) {
                maerchenInit = maerchen;
                return;
            }
        }
        // Neustes erfasstes Märchen, falls für gewünschtes Schuljahr noch kein Märchen erfasst
        if (selectableMaerchens.size() > 1) {
            maerchenInit = selectableMaerchens.get(0);
            return;
        }
        maerchenInit = null;
    }

    public Maerchen getMaerchenInit() {
        return maerchenInit;
    }
}
