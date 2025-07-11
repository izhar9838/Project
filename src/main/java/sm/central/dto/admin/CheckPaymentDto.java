package sm.central.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckPaymentDto {
    private String paymentId;
    private Long amount;
    private String studentName;
    private String studentId;
    private String [] fee_type;
    private LocalDateTime feeSubmitTime;
    private String payment_mode;
}
