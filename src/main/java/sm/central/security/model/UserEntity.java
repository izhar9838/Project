package sm.central.security.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(unique = true)
	@NonNull
	private String username;
	@NonNull
	private String password;
	@NonNull
	private String role;
	@NonNull
	private String email;
	@Lob
	@NonNull
	private byte[] profileImage;
	@NonNull
	private Long phoneNumber;

}
