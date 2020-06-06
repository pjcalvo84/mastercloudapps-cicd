package es.codeurjc.daw.repository;

import es.codeurjc.daw.Comment;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CommentRepository  extends JpaRepository<Comment, Long>{

}
