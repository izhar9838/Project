package sm.central.model.content;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HallOfFameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String achievement;
//    @Lob // Use @Lob for large objects like Base64 strings for mysql
    @Column(columnDefinition = "BYTEA")//for postgresql
    private String image;
}