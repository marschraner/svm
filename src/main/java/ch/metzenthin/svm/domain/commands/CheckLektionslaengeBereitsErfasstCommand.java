package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckLektionslaengeBereitsErfasstCommand implements Command {

    // input
    private Lektionsgebuehren lektionsgebuehren;
    private Lektionsgebuehren lektionsgebuehrenOrigin;
    private List<Lektionsgebuehren> bereitsErfassteLektionsgebuehren;

    // output
    private boolean bereitsErfasst;

    public CheckLektionslaengeBereitsErfasstCommand(Lektionsgebuehren lektionsgebuehren, Lektionsgebuehren lektionsgebuehrenOrigin, List<Lektionsgebuehren> bereitsErfassteLektionsgebuehren) {
        this.lektionsgebuehren = lektionsgebuehren;
        this.lektionsgebuehrenOrigin = lektionsgebuehrenOrigin;
        this.bereitsErfassteLektionsgebuehren = bereitsErfassteLektionsgebuehren;
    }

    @Override
    public void execute() {
        for (Lektionsgebuehren bereitsErfassteLektionsgebuehren : this.bereitsErfassteLektionsgebuehren) {
            if (!bereitsErfassteLektionsgebuehren.isIdenticalWith(lektionsgebuehrenOrigin) && bereitsErfassteLektionsgebuehren.getLektionslaenge().equals(lektionsgebuehren.getLektionslaenge())) {
                bereitsErfasst = true;
                return;
            }
        }
        bereitsErfasst = false;
    }

    public boolean isBereitsErfasst() {
        return bereitsErfasst;
    }
}
