package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Code;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckCodeKuerzelBereitsInVerwendungCommand implements Command {

    // input
    private Code code;
    private Code codeOrigin;
    private List<Code> bereitsErfassteCodes;

    // output
    private boolean bereitsInVerwendung;

    public CheckCodeKuerzelBereitsInVerwendungCommand(Code code, Code codeOrigin, List<Code> bereitsErfassteCodes) {
        this.code = code;
        this.codeOrigin = codeOrigin;
        this.bereitsErfassteCodes = bereitsErfassteCodes;
    }

    @Override
    public void execute() {
        for (Code bereitsErfassterCode : bereitsErfassteCodes) {
            if (!bereitsErfassterCode.isIdenticalWith(codeOrigin) && bereitsErfassterCode.getKuerzel().equals(code.getKuerzel())) {
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
