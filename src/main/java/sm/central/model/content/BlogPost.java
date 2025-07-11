package sm.central.model.content;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogPost {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String author;
	private String category;
	@Lob
	private String content;
//	@Lob for mysql
	@Column(columnDefinition = "BYTEA")//for postgresql
	private byte [] image;
	
}
