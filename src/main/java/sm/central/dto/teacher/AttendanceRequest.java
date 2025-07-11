package sm.central.dto.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceRequest {
    private String classId;
    private String section;
    private List<AttendanceEntry> attendance;

    public static class AttendanceEntry {
        private String studentId;
        private boolean present;

        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        public boolean isPresent() { return present; }
        public void setPresent(boolean present) { this.present = present; }
    }


}