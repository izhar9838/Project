package sm.central.model.staff;

import java.time.LocalDate;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Entity
@Data
public class Teacher {
    @SuppressWarnings("deprecation")
    @Id
    @GenericGenerator(name = "teacherIdGenerator", strategy = "sm.central.idgenerator.TeacherIdGenerator")
    @GeneratedValue(generator = "teacherIdGenerator")
    private String teacherId;
    private String firstName;
    private String lastName;
    @JsonProperty("DOB")
    private LocalDate DOB;
    private String gender;
    //    @Lob for mysql
    @Column(columnDefinition = "BYTEA")
    private byte[] image;

    @OneToOne(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Teacher_Contact_Details teacher_contact;

    @OneToOne(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Teacher_Professional_Details professional_Details;

    @OneToOne(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Teacher_User_Pass teacher_user_pass;

    // Helper method to set Teacher_User_Pass and maintain bidirectional relationship
    public void setTeacher_user_pass(Teacher_User_Pass teacher_user_pass) {
        this.teacher_user_pass = teacher_user_pass;
        if (teacher_user_pass != null) {
            teacher_user_pass.setTeacher(this);
        }
    }

    // Helper method for Teacher_Contact_Details
    public void setTeacher_contact(Teacher_Contact_Details teacher_contact) {
        this.teacher_contact = teacher_contact;
        if (teacher_contact != null) {
            teacher_contact.setTeacher(this);
        }
    }

    // Helper method for Teacher_Professional_Details
    public void setProfessional_Details(Teacher_Professional_Details professional_Details) {
        this.professional_Details = professional_Details;
        if (professional_Details != null) {
            professional_Details.setTeacher(this);
        }
    }
}