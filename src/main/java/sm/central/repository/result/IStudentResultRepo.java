package sm.central.repository.result;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sm.central.model.result.Examination;
import sm.central.model.result.StudentResult;
import sm.central.model.result.Subject;
import sm.central.model.student.Student;

import java.util.List;

@Repository
public interface IStudentResultRepo extends JpaRepository<StudentResult,Integer> {
    StudentResult findByStudentAndSubjectAndExamination(Student student, Subject subject, Examination examination);
    @Query("SELECT new map(s.subject as subjectName, e.type as examinationType, sr.marks as marks) " +
            "FROM Subject s CROSS JOIN Examination e " +
            "LEFT JOIN StudentResult sr ON sr.subject.id = s.id AND sr.examination.id = e.id AND sr.student.id = :studentId")
    List<Object> findResultsByStudentId(String studentId);

}
