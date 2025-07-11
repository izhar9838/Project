package sm.central.repository.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sm.central.model.token.RefreshToken;

import java.util.Optional;

public interface IRefreshTokenRepo extends JpaRepository<RefreshToken,Integer> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUsername(String username);
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.username = :username")
    void deleteByUsername(@Param("username") String username);
}

