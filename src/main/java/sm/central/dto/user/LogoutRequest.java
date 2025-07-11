package sm.central.dto.user;

import lombok.Data;

@Data
public class LogoutRequest {
    private String refreshToken;
}
