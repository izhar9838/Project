package sm.central.repository;

//OtpRepository.java
import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.security.model.OtpEntity;

public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
 OtpEntity findByEmail(String email);
}