package ch.metzenthin.svm.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Martin Schraner
 */
@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity {

  @Version private int version;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "creation_date", nullable = false)
  private Date creationDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "last_modified", nullable = false)
  private Date lastModified;

  @PrePersist
  public void onPersist() {
    Date now = new Date();
    setCreationDate(now);
    setLastModified(now);
  }

  @PreUpdate
  public void onUpdate() {
    setLastModified(new Date());
  }
}
