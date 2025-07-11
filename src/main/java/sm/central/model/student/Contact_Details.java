package sm.central.model.student;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sm.central.customfilter.ByteArrayListDeserializer;

@Entity
@Setter
@Getter

@ToString(exclude = "student")
public class Contact_Details implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String address;
	private Long phoneNumber;
	private String email;
	private String guardianName;
	private Long guardianNumber;
    @Column(columnDefinition = "BYTEA")
    @ElementCollection
	@JsonDeserialize(using = ByteArrayListDeserializer.class)
    private List<byte[]> aadhaarImages;

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
