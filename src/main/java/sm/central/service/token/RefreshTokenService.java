package sm.central.service.token;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sm.central.model.token.RefreshToken;
import sm.central.repository.token.IRefreshTokenRepo;
import sm.central.security.JwtUtil;

import java.time.Instant;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class RefreshTokenService {
    private static final Logger logger = Logger.getLogger(RefreshTokenService.class.getName());

    @Autowired
    private IRefreshTokenRepo refreshTokenRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public RefreshToken createRefreshToken(String username) {
        logger.info("Creating refresh token for username: " + username);
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUsername(username);
        if (existingToken.isPresent()) {
            logger.info("Deleting existing refresh token for username: " + username);
            refreshTokenRepository.deleteByUsername(username);
        }
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setToken(jwtUtil.generateRefreshToken(username));
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtUtil.getRefreshTokenExpirationMs()));
        refreshToken.setRevoked(false);
        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        logger.info("Created refresh token for username: " + username);
        return savedToken;
    }

    public Optional<RefreshToken> findByToken(String token) {
        logger.info("Finding refresh token: " + token);
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            logger.warning("Refresh token expired for username: " + token.getUsername());
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token has expired");
        }
        if (token.isRevoked()) {
            logger.warning("Refresh token revoked for username: " + token.getUsername());
            throw new RuntimeException("Refresh token is revoked");
        }
        return token;
    }

    public boolean validateRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = findByToken(token);
        if (refreshToken.isPresent()) {
            RefreshToken tokenEntity = refreshToken.get();
            boolean isValid = !tokenEntity.isRevoked() && tokenEntity.getExpiryDate().isAfter(Instant.now());
            logger.info("Validating refresh token for username: " + tokenEntity.getUsername() + ", valid: " + isValid);
            return isValid;
        }
        logger.warning("Refresh token not found: " + token);
        return false;
    }

    public void revokeRefreshToken(String username) {
        logger.info("Revoking refresh token for username: " + username);
        refreshTokenRepository.deleteByUsername(username);
    }

    @Transactional
    public RefreshToken rotateRefreshToken(RefreshToken oldToken) {
        logger.info("Rotating refresh token for username: " + oldToken.getUsername());
        refreshTokenRepository.delete(oldToken);
        String username = oldToken.getUsername();
        return createRefreshToken(username);
    }

    @Transactional
    public void deleteByToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if (refreshToken.isPresent()) {
            logger.info("Deleting refresh token: " + token);
            refreshTokenRepository.delete(refreshToken.get());
        } else {
            logger.warning("Attempted to delete non-existent refresh token: " + token);
            throw new IllegalArgumentException("Invalid refresh token");
        }
    }
}