package sm.central.model.staff;

import java.time.LocalDate;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Teacher {
	@SuppressWarnings("deprecation")
	@Id
	@GenericGenerator(name = "teacherIdGenerator",strategy = "sm.central.idgenerator.TeacherIdGenerator")
	@GeneratedValue(generator = "teacherIdGenerator")
	private String teacherId;
	private String firstName;
	private String lastName;
	@JsonProperty("DOB")
	private LocalDate DOB;
	private String gender;
	@Lob
	private byte [] image;
	
	@OneToOne(mappedBy = "teacher",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private Teacher_Contact_Details teacher_contact;
	
	@OneToOne(mappedBy = "teacher",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private Teacher_Professional_Details professional_Details;
	
	@OneToOne(mappedBy = "teacher",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private Teacher_User_Pass teacher_user_pass;
}
