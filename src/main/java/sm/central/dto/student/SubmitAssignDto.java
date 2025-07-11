package sm.central.dto.student;

import lombok.Data;

@Data
public class SubmitAssignDto {
    private Long assignmentId;
    private byte[] submitAssignment;
    private String mimeType;
}
