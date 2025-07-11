package sm.central.model.content;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class SubmitAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;
    private String studentId;
    private Long assignmentId;
    private String classNo;
    private Integer marks;
    private boolean isChecked;
    private byte[] submitAssignment;
    private String mimeType;
    @ManyToOne
    @JoinColumn(name = "assignmentId",referencedColumnName = "id",insertable = false, updatable = false)
    private Assignment assignment;


}
