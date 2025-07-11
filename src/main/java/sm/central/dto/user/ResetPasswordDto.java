package sm.central.dto.user;

import lombok.Data;

@Data
public class ResetPasswordDto {
	private String otp;
	private String email;
	private String newPassword;
}
