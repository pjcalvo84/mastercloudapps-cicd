package es.codeurjc.daw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BlogRestController {

    @Autowired
    private PostService postService;

    @GetMapping("/post")
    public ResponseEntity<List<Post>> listPosts() {
        List<Post> listPost = postService.getPostsList();
        return new ResponseEntity<>(listPost, HttpStatus.OK);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<Post> getPost(@PathVariable long id) {
        Post post = this.postService.getPost(id);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(this.postService.getPost(id), HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<Post> newPost(@RequestBody Post post) {
        this.postService.addPost(post);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<Comment> newComment(@PathVariable long postId, @RequestBody Comment comment) {
		comment = this.postService.addComment(postId, comment);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
        //TODO: refactor
    }

    @DeleteMapping("/post/{postId}/comment/{commentId}")
    public ResponseEntity<Comment> deleteComment(@PathVariable long postId, @PathVariable long commentId) {
        try {
            this.postService.deleteComment(postId, commentId);
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
