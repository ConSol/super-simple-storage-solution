package de.consol.dus.s4.services.aggregator.boundary.dao.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Entity
@Table(
    name = "upload_part",
    uniqueConstraints = {
        @UniqueConstraint(
            name = UploadPartEntity.UNIQUE_CONSTRAINT_UPLOAD_PART_NAME,
            columnNames = {UploadPartEntity.UPLOAD_COLUMN_NAME, UploadPartEntity.PART_COLUMN_NAME})
    })
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UploadPartEntity {
  public static final String UNIQUE_CONSTRAINT_UPLOAD_PART_NAME =
      "upload_part__unique__fk_upload__and__part_number";
  static final String UPLOAD_COLUMN_NAME = "fk_upload";
  static final String PART_COLUMN_NAME = "part_number";

  @Id
  @SequenceGenerator(
      name = "UploadPartIdGenerator",
      sequenceName = "upload_part__seq__id",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UploadPartIdGenerator")
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "fk_upload", nullable = false)
  private UploadEntity upload;

  @Column(name = "part_number", nullable = false)
  private int partNumber;

  @Column(name = "content", nullable = false)
  private byte[] content;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (Hibernate.getClass(this) != Hibernate.getClass(o) && getClass() != o.getClass()) {
      return false;
    }
    final UploadPartEntity that = (UploadPartEntity) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}