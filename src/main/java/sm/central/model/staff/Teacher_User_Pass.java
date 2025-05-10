package sm.central.model.staff;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Teacher_User_Pass {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userpassId;
	@Column(unique = true)
	private String username;
	private String password;
	private String role;
	private String email;
//	@Lob for mysql
	@Column(columnDefinition = "BYTEA") //for postgresql
	private byte [] porfileImage;
	private Long phoneNumber;
	@OneToOne
	@JoinColumn(name = "teacher_id")
	@JsonBackReference
	@JsonIgnore
	private Teacher teacher;
}
