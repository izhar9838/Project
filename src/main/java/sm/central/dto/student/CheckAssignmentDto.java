package sm.central.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckAssignmentDto {
    private Long submissionId;
    private String studentId;
    private String studentName;
    private String assignmentTitle;
    private boolean isChecked;
    private String section;
    private Long assignmentId;
    private String classNo;
    private byte[] submitAssignment;
    private String mimeType;
    private boolean thisTeacher;

}
