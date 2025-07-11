package sm.central.repository.student;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sm.central.model.student.Academic_Info;
import sm.central.model.student.Student;

import java.util.List;

public interface IAcademicInfo extends JpaRepository<Academic_Info, Integer> {
    @Query("SELECT a.student FROM Academic_Info a WHERE a.standard = :standard AND a.section = :section")
    List<Student> findStudentsByStandardAndSection(@Param("standard") String standard, @Param("section") String section);

}
