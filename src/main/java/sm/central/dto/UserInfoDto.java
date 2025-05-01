package sm.central.dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
	private String id;
	private String fullName;
	private String username;
	private String password;
	private String email;
	@Lob
	private byte[] image;
	private String role;
	private Long phoneNumber;
	
	
}
