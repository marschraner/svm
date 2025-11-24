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
@Table(name = "Mitarbeiter_MitarbeiterCode")
@IdClass(MitarbeiterMitarbeiterCode.PrimaryKey.class)
@Getter
@Setter
public class MitarbeiterMitarbeiterCode {

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "person_id")
  private Mitarbeiter mitarbeiter;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "code_id")
  private MitarbeiterCode mitarbeiterCode;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MitarbeiterMitarbeiterCode that = (MitarbeiterMitarbeiterCode) o;
    return Objects.equals(mitarbeiter, that.mitarbeiter)
        && Objects.equals(mitarbeiterCode, that.mitarbeiterCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mitarbeiter, mitarbeiterCode);
  }

  @Getter
  @Setter
  public static class PrimaryKey implements Serializable {

    private Integer mitarbeiter;
    private Integer mitarbeiterCode;

    // Used by JPA
    @SuppressWarnings("unused")
    public PrimaryKey() {}

    @SuppressWarnings("unused")
    public PrimaryKey(Integer mitarbeiter, Integer mitarbeiterCode) {
      this.mitarbeiter = mitarbeiter;
      this.mitarbeiterCode = mitarbeiterCode;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      PrimaryKey that = (PrimaryKey) o;

      return mitarbeiter.equals(that.mitarbeiter) && mitarbeiterCode.equals(that.mitarbeiterCode);
    }

    @Override
    public int hashCode() {
      int result = mitarbeiter.hashCode();
      result = 31 * result + mitarbeiterCode.hashCode();
      return result;
    }
  }
}
