package es.codeurjc.daw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class BlogWebController {

	public static final String ERROR_MESSAGE = "errorMessage";
	public static final String NO_EXISTE_UN_POST_CON_ID = "No existe un post con id ";
	public static final String ERROR = "error";
	public static final String USER_NAME = "userName";
	public static final String REDIRECT_POST = "redirect:/post/";
	@Autowired
	private PostService postService;

	@GetMapping("/")
	public String blog(Model model) {

		model.addAttribute("posts", this.postService.getPostsList());
		return "blog";
	}

	@GetMapping("/post/{id}")
	public String post(HttpSession session, @PathVariable long id, Model model) {
		Post post = this.postService.getPost(id);
		if (post == null) {
			model.addAttribute(ERROR_MESSAGE, NO_EXISTE_UN_POST_CON_ID + id);
			return ERROR;
		}
		Object userName = session.getAttribute(USER_NAME);
		model.addAttribute(USER_NAME, userName != null ? userName : "");
		model.addAttribute("post", post);
		return "post";
	}

	@GetMapping("/post/new")
	public String post(Model model) {
		return "newPost";
	}

	@PostMapping("/post")
	public String post(Model model, Post post) {
		this.postService.addPost(post);
		return REDIRECT_POST + post.getId();
	}

	@PostMapping("/post/{id}/comment")
	public String post(HttpSession session, @PathVariable long id, Model model, Comment comment) {
		session.setAttribute(USER_NAME, comment.getAuthor());
		Post post = this.postService.getPost(id);
		if (post == null) {
			model.addAttribute(ERROR_MESSAGE, NO_EXISTE_UN_POST_CON_ID + id);
			return ERROR;
		}
		this.postService.addComment(id,comment);
		return REDIRECT_POST + post.getId();
	}

	@PostMapping("/post/{postId}/comment/{commentId}/delete")
	public String post(@PathVariable long postId, @PathVariable long commentId, Model model) {
		Post post = this.postService.getPost(postId);
		if (post == null) {
			model.addAttribute(ERROR_MESSAGE, NO_EXISTE_UN_POST_CON_ID + postId);
			return ERROR;
		}
		postService.deleteComment(postId, commentId);
		return REDIRECT_POST + post.getId();
	}

}
