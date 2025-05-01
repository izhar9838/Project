package sm.central.model.staff;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Teacher_Professional_Details {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer professionalId;
	private String position;//teacher headmaster ,principle
	private String status;//part time ,full time
	private String qualification;
	private String specialization;
	private Integer experience;
	private Integer classTeacher;
	@OneToOne
	@JoinColumn(name = "teacher_id")
	@JsonBackReference
	@JsonIgnore
	private Teacher teacher;
}
