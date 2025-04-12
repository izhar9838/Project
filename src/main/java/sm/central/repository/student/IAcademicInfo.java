package sm.central.repository.student;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.student.Academic_Info;

public interface IAcademicInfo extends JpaRepository<Academic_Info, Integer> {

}
