package sm.central.repository.student;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.student.StudentUserPassword;

public interface IStudentUserPass extends JpaRepository<StudentUserPassword, Integer> {

}
