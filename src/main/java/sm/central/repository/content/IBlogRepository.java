package sm.central.repository.content;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.content.BlogPost;

public interface IBlogRepository extends JpaRepository<BlogPost, Long> {

}
