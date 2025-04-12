package sm.central.model.student;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = "student")
public class Academic_Info {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	private Integer rollNo;
	private String standard;
	private String section;
	private String academic_year;
	@OneToOne
	@JoinColumn(name = "student_id")
	@JsonBackReference
	@JsonIgnore

	private Student student;
	
	public void setStudent(Student student) {
		this.student = student;
	}
	
}
