package sm.central.resultRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.result.Subject;

public interface SubjectRepo extends JpaRepository<Subject, Long> {

}
