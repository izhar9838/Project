package sm.central.model.timetable;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String className; // e.g., "Nursery", "1st", "10th"
    private String period; // e.g., "8:00-8:45"
    private String subject;
    private String teacher;
}