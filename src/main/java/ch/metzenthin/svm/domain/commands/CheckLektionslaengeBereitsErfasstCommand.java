package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import java.util.List;
import lombok.Getter;

/**
 * @author Martin Schraner
 */
public class CheckLektionslaengeBereitsErfasstCommand implements Command {

  // input
  private final Lektionsgebuehren lektionsgebuehren;
  private final Lektionsgebuehren lektionsgebuehrenOrigin;
  private final List<Lektionsgebuehren> bereitsErfassteLektionsgebuehrenListe;

  // output
  @Getter private boolean bereitsErfasst;

  public CheckLektionslaengeBereitsErfasstCommand(
      Lektionsgebuehren lektionsgebuehren,
      Lektionsgebuehren lektionsgebuehrenOrigin,
      List<Lektionsgebuehren> bereitsErfassteLektionsgebuehrenListe) {
    this.lektionsgebuehren = lektionsgebuehren;
    this.lektionsgebuehrenOrigin = lektionsgebuehrenOrigin;
    this.bereitsErfassteLektionsgebuehrenListe = bereitsErfassteLektionsgebuehrenListe;
  }

  @Override
  public void execute() {
    for (Lektionsgebuehren bereitsErfassteLektionsgebuehren :
        bereitsErfassteLektionsgebuehrenListe) {
      if (!bereitsErfassteLektionsgebuehren.isIdenticalWith(lektionsgebuehrenOrigin)
          && bereitsErfassteLektionsgebuehren.getLektionslaenge()
              == lektionsgebuehren.getLektionslaenge()) {
        bereitsErfasst = true;
        return;
      }
    }
    bereitsErfasst = false;
  }
}
