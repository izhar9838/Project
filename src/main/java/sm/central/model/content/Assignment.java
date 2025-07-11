package sm.central.model.content;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String teacherId;
    private String classNo;
    private String title;
    private String mimeType;
//    @Lob for mysql
    @Column(columnDefinition = "BYTEA")  //for postgresql
    private byte [] assignment;
}
