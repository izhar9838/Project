package sm.central.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendenceStudent {
    private String studentId;
    private String firstName;
    private String lastName;
    private Integer rollNo;
}
