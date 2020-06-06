package es.codeurjc.daw.repository;

import es.codeurjc.daw.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
