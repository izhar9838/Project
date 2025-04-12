package sm.central.security.model;

import lombok.Data;

@Data
public class LoginResponse {

	private Long id;
	private String username;
	private String passwrod;
	private String role;
	private String email;
	private String profileImage;
}
