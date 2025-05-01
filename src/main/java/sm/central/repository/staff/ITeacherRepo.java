package sm.central.repository.staff;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sm.central.model.staff.Teacher;

public interface ITeacherRepo extends JpaRepository<Teacher, String> {
	@Query("SELECT t FROM Teacher t " +
	           "JOIN FETCH t.teacher_user_pass " +
	           "JOIN FETCH t.teacher_contact " +
	           "JOIN FETCH t.professional_Details " +
	           "WHERE t.teacher_user_pass.username = :username")
	    Optional<Teacher> findByUsernameWithDetails(@Param("username") String username);

//	Optional<Teacher> findByUsernameWithDetails(String username);

}
