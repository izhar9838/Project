package sm.central.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeRecieptDto {
    private String studentId;
    private Long amount;
    private String paymentMode;
    private String [] fee_Type;
    private String paymentId;
    private LocalDateTime dateTime;
}
