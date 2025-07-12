package sm.central.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceSummary {
    private long totalSchoolDays;
    private long presentDays;
    private double attendancePercentage;

}
