package sm.central.dto;

import lombok.Data;

@Data
public class ResetPasswordDto {
	private String otp;
	private String email;
	private String newPassword;
}
