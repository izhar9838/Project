package sm.central.service.admin;

import java.util.List;
import sm.central.model.classes.Class;
import sm.central.model.hallofFame.HallOfFameEntity;
import sm.central.model.staff.Teacher;
import sm.central.model.student.Fees_Details;
import sm.central.model.student.Student;
import sm.central.model.timetable.Timetable;

public interface IAdminService {
	public String enrollStudent(Student student);
	public Fees_Details feesSubmission(Fees_Details fees_details);
	public String enrollStaff(Teacher teacher);
	public List<Class> getClassese();
	public String saveHallOfFame(HallOfFameEntity hallOfFame);
	public Timetable saveTimetable(Timetable timetable);
	public void deleteTimetable(Long id);
	public String addHallOfFame(HallOfFameEntity hallofFame);
}
