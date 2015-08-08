package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.SchuelerCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckCodeKuerzelBereitsInVerwendungCommand implements Command {

    // input
    private SchuelerCode schuelerCode;
    private SchuelerCode schuelerCodeOrigin;
    private List<SchuelerCode> bereitsErfassteSchuelerCodes;

    // output
    private boolean bereitsInVerwendung;

    public CheckCodeKuerzelBereitsInVerwendungCommand(SchuelerCode schuelerCode, SchuelerCode schuelerCodeOrigin, List<SchuelerCode> bereitsErfassteSchuelerCodes) {
        this.schuelerCode = schuelerCode;
        this.schuelerCodeOrigin = schuelerCodeOrigin;
        this.bereitsErfassteSchuelerCodes = bereitsErfassteSchuelerCodes;
    }

    @Override
    public void execute() {
        for (SchuelerCode bereitsErfassterSchuelerCode : bereitsErfassteSchuelerCodes) {
            if (!bereitsErfassterSchuelerCode.isIdenticalWith(schuelerCodeOrigin) && bereitsErfassterSchuelerCode.getKuerzel().equals(schuelerCode.getKuerzel())) {
                bereitsInVerwendung = true;
                return;
            }
        }
        bereitsInVerwendung = false;
    }

    public boolean isBereitsInVerwendung() {
        return bereitsInVerwendung;
    }
}
