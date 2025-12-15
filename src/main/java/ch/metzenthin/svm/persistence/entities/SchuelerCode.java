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
@Table(name = "SchuelerCode")
@DiscriminatorValue("Schueler")
@Getter
@Setter
public class SchuelerCode extends Code {

  @ManyToMany(mappedBy = "schuelerCodes")
  private final List<Schueler> schueler = new ArrayList<>();

  public SchuelerCode() {}

  public SchuelerCode(String kuerzel, String beschreibung, boolean selektierbar) {
    super(kuerzel, beschreibung, selektierbar);
  }
}
