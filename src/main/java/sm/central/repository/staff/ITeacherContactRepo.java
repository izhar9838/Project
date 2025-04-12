package sm.central.repository.staff;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.staff.Teacher_Contact_Details;

public interface ITeacherContactRepo extends JpaRepository<Teacher_Contact_Details, Integer> {

}
