// OtpEntity.java
package sm.central.security.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data

public class OtpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;       // Link to user by email
    private String otp;         // 6-digit OTP
    private Long otpExpiry;    // Expiry timestamp
}