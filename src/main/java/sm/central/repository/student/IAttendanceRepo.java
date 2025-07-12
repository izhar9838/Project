package sm.central.repository.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sm.central.model.student.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface IAttendanceRepo extends JpaRepository<Attendance,Integer> {
    // Get the earliest attendance date
    @Query("SELECT MIN(a.date) FROM Attendance a")
    LocalDate findEarliestDate();

    // Count total school days (distinct dates) in a period
    @Query("SELECT COUNT(DISTINCT a.date) FROM Attendance a WHERE a.date BETWEEN :startDate AND :endDate")
    long countDistinctSchoolDays(LocalDate startDate, LocalDate endDate);

    // Count present days for a student in a period
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.studentId = :studentId AND a.present = true AND a.date BETWEEN :startDate AND :endDate")
    long countPresentDays(String studentId, LocalDate startDate, LocalDate endDate);

    // Get all attendance records for a student in a period (optional, for detailed reporting)
    List<Attendance> findByStudent_StudentIdAndDateBetween(String studentId, LocalDate startDate, LocalDate endDate);

}
