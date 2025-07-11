package sm.central.dto.user;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserDto {
	private String firstName;
	private String lastName;
	private String email;
	private Long phoneNumber;
	@Lob
	private byte[] profileImage;
}
