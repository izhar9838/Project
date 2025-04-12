package sm.central.repository.staff;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.staff.Teacher_Professional_Details;

public interface ITeacherProfessional extends JpaRepository<Teacher_Professional_Details, Integer> {

}
