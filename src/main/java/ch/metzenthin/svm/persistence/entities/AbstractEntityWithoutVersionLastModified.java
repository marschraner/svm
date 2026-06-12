package ch.metzenthin.svm.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Martin Schraner
 */
@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntityWithoutVersionLastModified {

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "creation_date", nullable = false)
  private Date creationDate;

  @PrePersist
  public void onPersist() {
    Date now = new Date();
    setCreationDate(now);
  }
}
