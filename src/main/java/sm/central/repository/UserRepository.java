package sm.central.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import sm.central.security.model.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
		UserEntity findByUsername(String username);
		boolean existsByUsername(String username);
		UserEntity findByEmail(String email);
		@Query("SELECT u FROM UserEntity u WHERE u.username = :query")
		Optional<UserEntity> findByQuery(String query);
}
