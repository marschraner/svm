package ch.metzenthin.svm.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Schueler_SchuelerCode")
@IdClass(SchuelerSchuelerCode.PrimaryKey.class)
@Getter
@Setter
public class SchuelerSchuelerCode {

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "person_id")
  private Schueler schueler;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "code_id")
  private SchuelerCode schuelerCode;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SchuelerSchuelerCode that = (SchuelerSchuelerCode) o;
    return Objects.equals(schueler, that.schueler)
        && Objects.equals(schuelerCode, that.schuelerCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(schueler, schuelerCode);
  }

  @Getter
  @Setter
  public static class PrimaryKey implements Serializable {

    private Integer schueler;
    private Integer schuelerCode;

    // Used by JPA
    @SuppressWarnings("unused")
    public PrimaryKey() {}

    @SuppressWarnings("unused")
    public PrimaryKey(Integer schueler, Integer schuelerCode) {
      this.schueler = schueler;
      this.schuelerCode = schuelerCode;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      PrimaryKey that = (PrimaryKey) o;

      return schueler.equals(that.schueler) && schuelerCode.equals(that.schuelerCode);
    }

    @Override
    public int hashCode() {
      int result = schueler.hashCode();
      result = 31 * result + schuelerCode.hashCode();
      return result;
    }
  }
}
