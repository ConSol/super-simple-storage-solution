package de.consol.dus.s4.services.rest.api.boundary.dao;

import de.consol.dus.s4.services.rest.api.boundary.dao.entity.UploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

@SuppressWarnings("unused")
public interface UploadRepository extends JpaRepository<UploadEntity, Long> {
}
