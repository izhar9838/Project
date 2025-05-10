package sm.central.repository.content;

import org.springframework.data.jpa.repository.JpaRepository;
import sm.central.model.content.SubmitAssignment;

public interface ISubmitAssignment extends JpaRepository<SubmitAssignment,Long> {
    public boolean existsByAssignmentIdAndStudentId(Long id,String stuId);
}
