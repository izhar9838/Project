package sm.central.model.staff;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Data;
import sm.central.customfilter.ByteArrayListDeserializer;

import java.util.List;

@Entity
@Data
public class Teacher_Contact_Details {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer contactId;
	private String address;
	private String email;
	private Long phoneNumber;
	@Column(columnDefinition = "BYTEA")
	@ElementCollection
	@JsonDeserialize(using = ByteArrayListDeserializer.class)
	private List<byte[]> aadhaarImages;

	@OneToOne
	@JoinColumn(name = "teacher_id")
	@JsonBackReference
	@JsonIgnore
	private Teacher teacher;
}
