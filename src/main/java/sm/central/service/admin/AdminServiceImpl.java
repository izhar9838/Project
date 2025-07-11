package sm.central.service.admin;



import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import sm.central.dto.admin.CheckPaymentDto;
import sm.central.dto.admin.FeeRecieptDto;
import sm.central.dto.admin.FeesDetailsDTO;
import sm.central.exception.custom.StudentNotFoundException;
import sm.central.exception.custom.TeacherNotFoundException;
import sm.central.exception.custom.UserNotFoundException;
import sm.central.customfilter.ExcludeFeesDetailsFilter;
import sm.central.model.classes.Class;

import sm.central.model.content.Announcement;
import sm.central.model.content.HallOfFameEntity;
import sm.central.model.content.Timetable;
import sm.central.model.staff.Teacher;
import sm.central.model.staff.Teacher_Contact_Details;
import sm.central.model.staff.Teacher_User_Pass;
import sm.central.model.student.Contact_Details;
import sm.central.model.student.Fees_Details;
import sm.central.model.student.Student;
import sm.central.model.student.StudentUserPassword;
import sm.central.repository.UserRepository;
import sm.central.repository.classes.ClassRepository;
import sm.central.repository.content.IAnnoucementRepo;
import sm.central.repository.content.IHallOfFameRepo;
import sm.central.repository.content.TimetableRepository;
import sm.central.repository.staff.ITeacherRepo;
import sm.central.repository.staff.ITeacherUserPass;
import sm.central.repository.student.IContactDetailsRepo;
import sm.central.repository.student.IFeesRepo;
import sm.central.repository.student.IStudentRepo;
import sm.central.repository.student.IStudentUserPass;
import sm.central.security.model.UserEntity;

import javax.imageio.ImageIO;

@Service
public class AdminServiceImpl implements IAdminService {
	@Autowired
	private ITeacherRepo teaRepo;
	@Autowired
    private TimetableRepository repository;
	@Autowired
	private IStudentRepo sturepo;
	@Autowired
	private IContactDetailsRepo contrepo;
	@Autowired
	private ClassRepository classRepo;
	@Autowired
	private IFeesRepo feerepo;
	@Autowired
	private UserRepository userrepo;
	@Autowired
	private IHallOfFameRepo hallOfFameRepo;
	@Autowired
	private IAnnoucementRepo annoucRepo;
	@Autowired
	private IStudentUserPass studentUserPass;
	@Autowired
	private ITeacherUserPass iTeacherUserPass;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Value("${upi.admin.vpa}")
	private String adminUpiId;

	@Value("${upi.admin.name}")
	private String adminName;
	 // Add this import for Base64 decoding



	public String enrollStudent(Student student) {
		String msg = null;
		try {
			if (student == null) {
				throw new IllegalArgumentException("Student cannot be null");
			}

			// Set bidirectional relationship for userPass
			if (student.getUserPass() != null) {
				student.getUserPass().setStudent(student);
			} else {
				throw new IllegalArgumentException("StudentUserPassword is required");
			}

			// Set bidirectional relationship for contact_details
			if (student.getContact_details() != null) {
				student.getContact_details().setStudent(student);
				if (student.getContact_details().getAadhaarImages() == null || student.getContact_details().getAadhaarImages().isEmpty()) {
					throw new IllegalArgumentException("Aadhaar images are required");
				}
			} else {
				throw new IllegalArgumentException("Contact_Details is required");
			}

			// Set bidirectional relationship for academic_info
			if (student.getAcademic_info() != null) {
				student.getAcademic_info().setStudent(student);
			} else {
				throw new IllegalArgumentException("Academic_Info is required");
			}

			// Set bidirectional relationship for fees_details
			if (student.getFees_details() != null && !student.getFees_details().isEmpty()) {
				for (Fees_Details fee : student.getFees_details()) {
					fee.setStudent(student);
				}
			} else {
				throw new IllegalArgumentException("Fees_Details is required");
			}

			// Save user entity for authentication
			UserEntity userEntity = new UserEntity();
			StudentUserPassword userPass = student.getUserPass();
			Contact_Details details = student.getContact_details();
			userPass.setEmail(details.getEmail());
			userPass.setPhoneNumber(details.getPhoneNumber());
			userPass.setProfileImage(student.getImage());
			userPass.setPassword(passwordEncoder.encode(userPass.getPassword()));

			// Save the student entity
			Student saved = sturepo.save(student);

			userEntity.setEmail(userPass.getEmail());
			userEntity.setPassword(passwordEncoder.encode(userPass.getPassword()));
			userEntity.setPhoneNumber(userPass.getPhoneNumber());
			userEntity.setProfileImage(userPass.getProfileImage());
			userEntity.setRole(userPass.getRole());
			userEntity.setUsername(userPass.getUsername());
			userrepo.save(userEntity);

			msg = "Student saved with student ID " + saved.getStudentId();
		} catch (Exception e) {
			throw new RuntimeException("Internal Server Error: " + e.getMessage(), e);
		}
		return msg;
	}	@Override


	@Transactional
	public FeeRecieptDto feesSubmission(FeesDetailsDTO feeDto) {
		// Fetch the Student entity by ID
		Student student = sturepo.findById(feeDto.getStudentId())
				.orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + feeDto.getStudentId()));

		// Create a new Fees_Details entity
		Fees_Details feesDetails = new Fees_Details();
		feesDetails.setAmount(feeDto.getAmount());
		feesDetails.setFee_type(feeDto.getFee_type());
		feesDetails.setPayment_mode(feeDto.getPayment_mode());
		feesDetails.setFeeSubmitTime(LocalDateTime.now());
		feesDetails.setStudent(student); // Set the Student in Fees_Details
		feesDetails = feerepo.save(feesDetails);
		if (student.getFees_details() == null) {
			student.setFees_details(new HashSet<>());
		}
		student.getFees_details().add(feesDetails);
		sturepo.save(student);
		FeeRecieptDto receiptDto = new FeeRecieptDto();
		receiptDto.setPaymentId(feesDetails.getPaymentId()); // Now populated
		receiptDto.setAmount(feesDetails.getAmount());
		receiptDto.setFee_Type(feesDetails.getFee_type());
		receiptDto.setPaymentMode(feesDetails.getPayment_mode());
		receiptDto.setDateTime(feesDetails.getFeeSubmitTime()); // Now populated
		receiptDto.setStudentId(student.getStudentId());

		return receiptDto;
	}
	@Transactional
    public String enrollStaff(Teacher teacher) {
        try {
            if (teacher == null) {

                throw new IllegalArgumentException("Teacher cannot be null");
            }

            // Ensure bidirectional relationships are set
            if (teacher.getTeacher_user_pass() != null) {
                teacher.getTeacher_user_pass().setTeacher(teacher);
            } else {

                throw new IllegalArgumentException("Teacher_User_Pass is required");
            }
            if (teacher.getTeacher_contact() != null) {
                teacher.getTeacher_contact().setTeacher(teacher);
            } else {
                throw new IllegalArgumentException("Teacher_Contact_Details is required");
            }
            if (teacher.getProfessional_Details() != null) {
                teacher.getProfessional_Details().setTeacher(teacher);
            } else {
                throw new IllegalArgumentException("Teacher_Professional_Details is required");
            }

            // Copy data to UserEntity
            UserEntity userEntity = new UserEntity();
            Teacher_User_Pass teacher_user_pass = teacher.getTeacher_user_pass();
            Teacher_Contact_Details details = teacher.getTeacher_contact();
            userEntity.setUsername(teacher_user_pass.getUsername());
            userEntity.setEmail(details.getEmail());
            userEntity.setPassword(passwordEncoder.encode(teacher_user_pass.getPassword()));
            userEntity.setRole(teacher_user_pass.getRole());
            userEntity.setProfileImage(teacher.getImage());
            userEntity.setPhoneNumber(details.getPhoneNumber());
            teacher_user_pass.setEmail(details.getEmail());
            teacher_user_pass.setPorfileImage(teacher.getImage());
			teacher_user_pass.setPassword(passwordEncoder.encode(teacher_user_pass.getPassword()));

            // Save Teacher (cascades to related entities) and UserEntity
            Teacher savedTeacher = teaRepo.save(teacher);
            userrepo.save(userEntity);

            return "Teacher is saved with id " + savedTeacher.getTeacherId();
        } catch (Exception e) {

            throw new RuntimeException("Error enrolling teacher: " );
        }
    }
	@Override
	public List<Class> getClassese() {
		List<Class> all = classRepo.findAll();

		return all;
	}
	@Override
	public String saveHallOfFame(HallOfFameEntity hallOfFame) {
		// TODO Auto-generated method stub
		HallOfFameEntity fameEntity = hallOfFameRepo.save(hallOfFame);
		if(fameEntity!=null) {
			return "Person with name of "+fameEntity.getName()+"saved";
		}
		throw new RuntimeException("Internal Server Error");
	}
	public Timetable saveTimetable(Timetable timetable) {
    	Timetable byPeriodAndClassName = repository.findByPeriodAndClassName(timetable.getPeriod(), timetable.getClassName());
    	if(byPeriodAndClassName==null) {
    			return repository.save(timetable);
    		}
    	throw new RuntimeException("That Entry is already in TimeTable");
    	}

    public void deleteTimetable(Long id) {
        repository.deleteById(id);
    }
    public String addHallOfFame(HallOfFameEntity hallofFame) {
		HallOfFameEntity save = hallOfFameRepo.save(hallofFame);
		if (save!=null) {
			return hallofFame.getName()+" saved in Database";
		}
		throw new RuntimeException("Some Error occured");
	}
	@Override
	public String addAdmin(UserEntity userEntity) {
		try {
			userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
			UserEntity save = userrepo.save(userEntity);
			return "Admin Save";
		} catch (Exception e) {
			throw new RuntimeException("Some Error Occured");
		}
	}
	@Override
	public boolean checkUsernameExists(String username) {
		return userrepo.existsByUsername(username);
		
	}
	@Override
	public Announcement  createAnnouncement(Announcement annoucement) {
		
		return annoucRepo.save(annoucement);
	}



	public String upiPayment(String studentId,double amount) throws WriterException, IOException {
		Optional<Student> studentOptional=sturepo.findById(studentId);
		if (studentOptional.isPresent()){
			if (amount <= 0) {
				throw new IllegalArgumentException("Amount must be greater than zero");
			}

			// Format UPI deep link
			String upiLink = String.format(
					"upi://pay?pa=%s&pn=%s&am=%.2f&cu=INR",
					adminUpiId, // e.g., admin@upi
					adminName.replace(" ", "%20"), // URL-encode spaces
					amount
			);

			// Generate QR code with ZXing
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			BitMatrix bitMatrix = qrCodeWriter.encode(upiLink, BarcodeFormat.QR_CODE, 250, 250, hints);

			// Convert to BufferedImage
			BufferedImage qrImage = new BufferedImage(250, 250, BufferedImage.TYPE_INT_RGB);
			for (int x = 0; x < 250; x++) {
				for (int y = 0; y < 250; y++) {
					qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
				}
			}

			// Convert to base64
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(qrImage, "PNG", outputStream);
			String base64Image = Base64.getEncoder().encodeToString(outputStream.toByteArray());

			return "data:image/png;base64," + base64Image;

		}

		throw new StudentNotFoundException("Student not found with ID "+studentId);

			}


	@Override
	public CheckPaymentDto checkPayment(String paymentId) {

		Optional<Fees_Details> optional=feerepo.findById(paymentId);
		if (optional.isEmpty()){
			throw new RuntimeException("Nothing Found with Payment ID" +paymentId);
		}
		Fees_Details feesDetails=optional.get();
		String firstName=sturepo.findById(feesDetails.getStudent().getStudentId()).get().getFirstName();
		String lastName =sturepo.findById(feesDetails.getStudent().getStudentId()).get().getLastName();
		CheckPaymentDto checkPaymentDto=new CheckPaymentDto();
		BeanUtils.copyProperties(feesDetails,checkPaymentDto);
		checkPaymentDto.setStudentId(feesDetails.getStudent().getStudentId());
		checkPaymentDto.setStudentName(String.format("%s %s", firstName, lastName));
		return checkPaymentDto;
	}

	@Override
	public Map<String, Object> findUserByQuery(String query) throws Exception {
		if (query == null || query.trim().isEmpty()) {
			throw new RuntimeException("Query cannot be empty");
		}
		if (Character.isLowerCase(query.charAt(0))){
			//find in Student table first
			Optional<StudentUserPassword> studentUserPassword= studentUserPass.findByUsername(query);
			if (studentUserPassword.isPresent()){
				Student student=studentUserPassword.get().getStudent();
				Map<String ,Object> response=Map.of("student",student);
				String serialized=objectMapper.writer(ExcludeFeesDetailsFilter.excludeFeesDetails()).writeValueAsString(response);
				return objectMapper.readValue(serialized,Map.class);
			}
			//find in teacher repo
			Optional<Teacher_User_Pass> teacherUserPass=iTeacherUserPass.findByUsername(query);
			if (teacherUserPass.isPresent()){
				Teacher teacher=teacherUserPass.get().getTeacher();
				return Map.of("teacher",teacher);
			}
			Optional<UserEntity> optional=userrepo.findByQuery(query);
			if (optional.isPresent()){
				UserEntity userEntity=optional.get();
				return Map.of("admin",userEntity);
			}
			throw new UserNotFoundException("User not found with username "+query);


		} else   {
			if (query.length() >= 4 && query.substring(0, 4).equals(query.substring(0, 4).toUpperCase())) {
				String prefix = query.substring(0, 4);
				String searchTerm = query.length() > 4 ? query.substring(4) : query;
				if ("STUD".equals(prefix)){
					Optional<Student> student=sturepo.findById(query);
					if (student.isPresent()){
						Student stu=student.get();
						Map<String ,Object> response=Map.of("student",student);
						String serialized=objectMapper.writer(ExcludeFeesDetailsFilter.excludeFeesDetails()).writeValueAsString(response);
						return objectMapper.readValue(serialized,Map.class);
					}
					else {
						throw new StudentNotFoundException("Student not found with ID "+query);
					}
				} else if ("STAF".equals(prefix)) {
					Optional<Teacher> teacherOptional=teaRepo.findById(query);
					if (teacherOptional.isPresent()){
						Teacher teacher=teacherOptional.get();
						return Map.of("teacher",teacher);
					}
					else {
						throw new TeacherNotFoundException("Staff not found with ID "+query);
					}

				}
			}
			
		}
		throw new RuntimeException("No results found for query: " + query);

	}

	@Override
	public Map<String, Object> fetchFeesDetailsPerPage(String studentId, int page) {
		Pageable pageable = PageRequest.of(page, 5);
		Page<Fees_Details> feesDetails = feerepo.findByStudentStudentId(studentId, pageable);

		if (feesDetails.isEmpty()) {
			return Map.of("message", "No fees details found for student ID: " + studentId, "currentPage", 0, "totalPage", 0, "totalElement", 0);
		}
		return Map.of("feesDetails", feesDetails.getContent(), "currentPage", feesDetails.getNumber(), "totalPage", feesDetails.getTotalPages(), "totalElement", feesDetails.getTotalElements());
	}

	@Override
	public FeeRecieptDto confirmPayment(String paymentId, FeesDetailsDTO feeDto) {
		Student student = sturepo.findById(feeDto.getStudentId())
				.orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + feeDto.getStudentId()));

		// Create a new Fees_Details entity
		Fees_Details feesDetails = new Fees_Details();
		feesDetails.setPaymentId(paymentId);
		feesDetails.setAmount(feeDto.getAmount());
		feesDetails.setFee_type(feeDto.getFee_type());
		feesDetails.setPayment_mode(feeDto.getPayment_mode());
		feesDetails.setFeeSubmitTime(LocalDateTime.now());
		feesDetails.setStudent(student); // Set the Student in Fees_Details
		feesDetails = feerepo.save(feesDetails);
		if (student.getFees_details() == null) {
			student.setFees_details(new HashSet<>());
		}
		student.getFees_details().add(feesDetails);
		sturepo.save(student);
		FeeRecieptDto receiptDto = new FeeRecieptDto();
		receiptDto.setPaymentId(feesDetails.getPaymentId()); // Now populated
		receiptDto.setAmount(feesDetails.getAmount());
		receiptDto.setFee_Type(feesDetails.getFee_type());
		receiptDto.setPaymentMode(feesDetails.getPayment_mode());
		receiptDto.setDateTime(feesDetails.getFeeSubmitTime()); // Now populated
		receiptDto.setStudentId(student.getStudentId());

		return receiptDto;
	}

	@Override
	public String cancelPayment(String paymentId) {
		Optional<Fees_Details> feesDetailsOptional=feerepo.findById(paymentId);
		if (feesDetailsOptional.isPresent()){
			feerepo.deleteById(paymentId);
			return "Delete Success";
		}
		return "No Fees Details to Delete";
	}

	@Override
	public void addSubject(String subject) {

	}

	@Scheduled(fixedRate = 86400000) // Run daily (24 hours = 86,400,000 ms)
    public void deleteOldAnnouncements() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        List<Announcement> oldAnnouncements = annoucRepo.findAll().stream()
                .filter(announcement -> announcement.getCreatedDate().isBefore(oneWeekAgo))
                .toList();
        annoucRepo.deleteAll(oldAnnouncements);
    }
	


}
