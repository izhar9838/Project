package sm.central.repository.student;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sm.central.model.student.Student;

public interface IStudentRepo extends JpaRepository<Student, String> {
	@Query("SELECT s FROM Student s " +
	           "JOIN FETCH s.userPass up " +
	           "JOIN FETCH s.contact_details cd " +
	           "JOIN FETCH s.academic_info ai " +
	           "LEFT JOIN FETCH s.fees_details fd " +
	           "LEFT JOIN FETCH s.results r " +
	           "WHERE up.username = :username")
	    Optional<Student> findByUsernameWithDetails(@Param("username") String username);
}
