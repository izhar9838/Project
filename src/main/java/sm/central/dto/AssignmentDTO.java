package sm.central.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentDTO {
    private Long id;
    private String title;
    private String classNo;
    private String mimeType;
    private byte [] assignment;
    private boolean submitted;
}
