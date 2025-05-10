package sm.central.dto;

import lombok.Data;

@Data
public class SubmitAssignDto {
    private Long assignmentId;
    private byte[] submitAssignment;
    private String mimeType;
}
