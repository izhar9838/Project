package sm.central.resultRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.result.StudentResult;

public interface StudentResultRepo extends JpaRepository<StudentResult, Long> {

}
