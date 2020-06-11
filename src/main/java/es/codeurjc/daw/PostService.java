package es.codeurjc.daw;

import es.codeurjc.daw.repository.CommentRepository;
import es.codeurjc.daw.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CommentRepository commentRepository;

	public List<Post> getPostsList() {
		return postRepository.findAll();
	}

	public Post getPost(long id) {
		return postRepository.findById(id).get();
	}

	public void addPost(Post post) {
		postRepository.save(post);
	}

	public Comment addComment(long postId, Comment comment) {
		Post post = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);

		/*

		Comentario para que salte sonar

		 */
		Comment savedComment = commentRepository.save(comment);

		post.getComments().add(savedComment);

		postRepository.save(post);

		return savedComment;
	}

	public void deleteComment(long postId, long commentId) {

		Post post = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);

		post.deleteComment(commentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new));
		postRepository.save(post);
		commentRepository.deleteById(commentId);
	}
}
