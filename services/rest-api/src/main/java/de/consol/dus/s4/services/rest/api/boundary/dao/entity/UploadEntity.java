package de.consol.dus.s4.services.rest.api.boundary.dao.entity;

import de.consol.dus.s4.services.rest.api.usecases.api.responses.UploadStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Entity
@Table(name = "upload")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UploadEntity {
  @Id
  @SequenceGenerator(
      name = "UploadIdGenerator",
      sequenceName = "upload__seq__id",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UploadIdGenerator")
  @Column(name = "id")
  private Long id;

  @Column(name = "file_name", nullable = false, length = 127)
  private String fileName;

  @OneToMany(
      mappedBy = "upload",
      fetch = FetchType.EAGER,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<UploadPartEntity> parts = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private UploadStatus status = UploadStatus.UPLOAD_IN_PROGRESS;

  @Column(name = "content")
  private byte[] content;

  @Column(name = "total_parts")
  private Integer totalParts;

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
    final UploadEntity that = (UploadEntity) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}