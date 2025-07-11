package sm.central.repository.result;

import org.springframework.data.jpa.repository.JpaRepository;
import sm.central.model.result.Subject;

import java.util.Optional;

public interface ISubjectRepo extends JpaRepository<Subject,Integer> {
    Optional<Subject> findBySubject(String subject);
}
