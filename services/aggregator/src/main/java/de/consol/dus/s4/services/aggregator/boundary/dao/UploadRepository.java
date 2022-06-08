package de.consol.dus.s4.services.aggregator.boundary.dao;

import de.consol.dus.s4.services.aggregator.boundary.dao.entity.UploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadRepository extends JpaRepository<UploadEntity, Long> {
}
