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
public class Teacher_Contact_Details {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer contactId;
	private String address;
	private String email;
	private Long phoneNumber; 
	@OneToOne
	@JoinColumn(name = "teacher_id")
	@JsonBackReference
	@JsonIgnore
	private Teacher teacher;
}
