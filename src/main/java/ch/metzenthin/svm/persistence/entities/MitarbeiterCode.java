package ch.metzenthin.svm.persistence.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("java:S2160") // equals / hash definiert für Code
@Entity
@Table(name = "MitarbeiterCode")
@DiscriminatorValue("Mitarbeiter")
@Getter
@Setter
public class MitarbeiterCode extends Code {

  @ManyToMany(mappedBy = "mitarbeiterCodes")
  private final List<Mitarbeiter> mitarbeiters = new ArrayList<>();

  public MitarbeiterCode() {}

  public MitarbeiterCode(String kuerzel, String beschreibung, boolean selektierbar) {
    super(kuerzel, beschreibung, selektierbar);
  }
}
