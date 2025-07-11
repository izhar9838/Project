package sm.central.repository.student;

import org.springframework.data.jpa.repository.JpaRepository;
import sm.central.model.student.Attendance;

public interface IAttendanceRepo extends JpaRepository<Attendance,Integer> {
}
