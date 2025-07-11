package sm.central.service.admin;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.zxing.WriterException;
import sm.central.dto.admin.CheckPaymentDto;
import sm.central.dto.admin.FeeRecieptDto;
import sm.central.dto.admin.FeesDetailsDTO;
import sm.central.model.classes.Class;

import sm.central.model.content.Announcement;
import sm.central.model.content.HallOfFameEntity;
import sm.central.model.content.Timetable;
import sm.central.model.staff.Teacher;
import sm.central.model.student.Student;
import sm.central.security.model.UserEntity;

public interface IAdminService {
	public String enrollStudent(Student student);
	public FeeRecieptDto feesSubmission(FeesDetailsDTO feeDto);
	public String enrollStaff(Teacher teacher);
	public List<Class> getClassese();
	public String saveHallOfFame(HallOfFameEntity hallOfFame);
	public Timetable saveTimetable(Timetable timetable);
	public void deleteTimetable(Long id);
	public String addHallOfFame(HallOfFameEntity hallofFame);
	public String addAdmin(UserEntity userEntity);
	public boolean checkUsernameExists(String username);
	public Announcement createAnnouncement(Announcement annoucement);
	public String upiPayment(String studentId,double amount) throws WriterException, IOException;

	CheckPaymentDto checkPayment(String paymentId);


	Map<String, Object> findUserByQuery(String query) throws Exception;
	Map<String,Object> fetchFeesDetailsPerPage(String studentId,int page);

	FeeRecieptDto confirmPayment(String paymentId, FeesDetailsDTO feesDetailsDTO);

	String cancelPayment(String paymentId);

    void addSubject(String subject);
}
