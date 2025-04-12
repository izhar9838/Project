package sm.central.model.student;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@Setter
@ToString(exclude = "student")
public class Contact_Details implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	private String address;
	private Long phoneNumber;
	private String email;
	private String guardianName;
	private Long guardianNumber;
	@OneToOne
	@JoinColumn(name = "student_id")
	@JsonBackReference
	@JsonIgnore
	private Student student;
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
}
