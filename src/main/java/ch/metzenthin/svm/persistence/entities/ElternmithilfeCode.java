package ch.metzenthin.svm.persistence.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
@Table(name = "ElternmithilfeCode")
@DiscriminatorValue("Elternmithilfe")
@Getter
@Setter
public class ElternmithilfeCode extends Code {

  @OneToMany(mappedBy = "elternmithilfeCode")
  private final List<Maercheneinteilung> maercheneinteilungen = new ArrayList<>();

  public ElternmithilfeCode() {}

  public ElternmithilfeCode(String kuerzel, String beschreibung, boolean selektierbar) {
    super(kuerzel, beschreibung, selektierbar);
  }
}
