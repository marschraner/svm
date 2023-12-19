package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Code;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckCodeKuerzelBereitsInVerwendungCommand implements Command {

    // input
    private final String kuerzel;
    private final Code codeOrigin;
    private final List<? extends Code> bereitsErfassteCodes;

    // output
    private boolean bereitsInVerwendung;

    public CheckCodeKuerzelBereitsInVerwendungCommand(String kuerzel, Code codeOrigin, List<? extends Code> bereitsErfassteCodes) {
        this.kuerzel = kuerzel;
        this.codeOrigin = codeOrigin;
        this.bereitsErfassteCodes = bereitsErfassteCodes;
    }

    @Override
    public void execute() {
        for (Code bereitsErfassterCode : bereitsErfassteCodes) {
            if (!bereitsErfassterCode.isIdenticalWith(codeOrigin) && bereitsErfassterCode.getKuerzel().equals(kuerzel)) {
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
