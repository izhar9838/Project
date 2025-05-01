package sm.central.service.admin;

import java.util.List;

import sm.central.dto.FeesDetailsDTO;
import sm.central.model.classes.Class;

import sm.central.model.content.Announcement;
import sm.central.model.content.HallOfFameEntity;
import sm.central.model.content.Timetable;
import sm.central.model.staff.Teacher;
import sm.central.model.student.Fees_Details;
import sm.central.model.student.Student;
import sm.central.security.model.UserEntity;

public interface IAdminService {
	public String enrollStudent(Student student);
	public Fees_Details feesSubmission(FeesDetailsDTO feeDto);
	public String enrollStaff(Teacher teacher);
	public List<Class> getClassese();
	public String saveHallOfFame(HallOfFameEntity hallOfFame);
	public Timetable saveTimetable(Timetable timetable);
	public void deleteTimetable(Long id);
	public String addHallOfFame(HallOfFameEntity hallofFame);
	public String addAdmin(UserEntity userEntity);
	public boolean checkUsernameExists(String username);
	public Announcement createAnnouncement(Announcement annoucement);
}
