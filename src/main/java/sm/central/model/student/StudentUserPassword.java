package sm.central.model.student;

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
import lombok.ToString;

@Entity
@Data
@ToString(exclude = "student")
public class StudentUserPassword {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(unique = true)
	private String username;
	private String password;
	private String role;
	private String email;
	@Lob
	private byte [] profileImage;
	private Long phoneNumber;
	@OneToOne
	@JoinColumn(name = "student_id")
	@JsonBackReference
	@JsonIgnore
	private Student student;
}
