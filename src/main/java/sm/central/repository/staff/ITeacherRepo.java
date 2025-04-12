package sm.central.repository.staff;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.staff.Teacher;

public interface ITeacherRepo extends JpaRepository<Teacher, String> {

}
