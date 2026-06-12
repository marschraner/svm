package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.persistence.entities.KursLehrkraft.PrimaryKey;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name = "Kurs_Lehrkraft")
@IdClass(PrimaryKey.class)
@Setter
@Getter
public class KursLehrkraft extends AbstractEntityWithoutVersionLastModified
    implements Comparable<KursLehrkraft> {

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "kurs_id")
  private Kurs kurs;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "person_id")
  private Mitarbeiter lehrkraft;

  @Column(name = "lehrkraefte_ORDER", nullable = false)
  private int lehrkraefteOrder;

  public KursLehrkraft() {}

  public KursLehrkraft(Kurs kurs, Mitarbeiter lehrkraft, int lehrkraefteOrder) {
    this.kurs = kurs;
    this.lehrkraft = lehrkraft;
    this.lehrkraefteOrder = lehrkraefteOrder;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    KursLehrkraft that = (KursLehrkraft) o;
    return Objects.equals(kurs, that.kurs) && Objects.equals(lehrkraft, that.lehrkraft);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kurs, lehrkraft);
  }

  @Override
  public int compareTo(KursLehrkraft otherLehrkraft) {
    return Integer.compare(lehrkraefteOrder, otherLehrkraft.lehrkraefteOrder);
  }

  @Getter
  @Setter
  public static class PrimaryKey implements Serializable {

    private Integer kurs;
    private Integer lehrkraft;

    // Used by JPA
    @SuppressWarnings("unused")
    public PrimaryKey() {
      // Nothing to do
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }
      PrimaryKey other = (PrimaryKey) obj;
      return Objects.equals(kurs, other.kurs) && Objects.equals(lehrkraft, other.lehrkraft);
    }

    @Override
    public int hashCode() {
      return Objects.hash(kurs, lehrkraft);
    }
  }
}
