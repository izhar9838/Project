package sm.central.dto;

import lombok.Data;

@Data
public class FeesDetailsDTO {
    private Long amount;
    private String[] fee_type;
    private String payment_mode;
    private String studentId; // Map to student_id
}
