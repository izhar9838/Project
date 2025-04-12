package sm.central.repository.student;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.student.Student;

public interface IStudentRepo extends JpaRepository<Student, String> {

}
