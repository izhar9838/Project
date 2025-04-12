package sm.central.repository.staff;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.staff.Teacher_User_Pass;

public interface ITeacherUserPass extends JpaRepository<Teacher_User_Pass, Integer> {

}
