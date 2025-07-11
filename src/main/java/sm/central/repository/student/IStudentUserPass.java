package sm.central.repository.student;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.student.StudentUserPassword;

import java.util.Optional;

public interface IStudentUserPass extends JpaRepository<StudentUserPassword, Integer> {
    Optional<StudentUserPassword> findByUsername(String username);

}
