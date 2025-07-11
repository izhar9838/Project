package sm.central.repository.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.student.Fees_Details;

public interface IFeesRepo extends JpaRepository<Fees_Details, String> {
    Page<Fees_Details> findByStudentStudentId(String studentId, Pageable pageable);

}
