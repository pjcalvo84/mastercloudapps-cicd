package es.codeurjc.daw;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@RunWith(SpringRunner.class)
@WebMvcTest(BlogRestController.class)
@DisplayName("Unitary tests")
public class UnitaryTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	PostService postService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("Crear un post y verificar que se crea correctamente")
	public void createPostTest() throws Exception{

		// CREAMOS UN NUEVO POST

		Post post = new Post("Mi titulo", "Mi contenido");
		Mockito.doNothing().when(postService).addPost(post);
		when(postService.getPost(post.getId())).thenReturn(post);

		//TODO:prueba

		MvcResult result = mvc.perform(
			post("/api/post")
				.content(objectMapper.writeValueAsString(post))
				.contentType(MediaType.APPLICATION_JSON)
		  )
		  .andExpect(status().isCreated())
		  .andExpect(jsonPath("$.title", equalTo(post.getTitle())))
		  .andReturn();

		Post resultPost = objectMapper.readValue(
			result.getResponse().getContentAsString(), 
			Post.class
		);

		// COMPROBAMOS QUE EL POST SE HA CREADO CORRECTAMENTE
		  
		mvc.perform(
			get("/api/post/"+resultPost.getId())
				.content(objectMapper.writeValueAsString(post))
				.contentType(MediaType.APPLICATION_JSON)
		  )
	      .andExpect(status().isOk())
	      .andExpect(jsonPath("$.title", equalTo(post.getTitle())));
		
	}

	@Test
	@DisplayName("Añadir un comentario a un post y verificar que se añade el comentario")
	public void createCommentTest() throws Exception {

		// CREAMOS UN NUEVO POST

		Post post = new Post("Mi titulo", "Mi contenido con comentarios");

		Mockito.doNothing().when(postService).addPost(post);

		when(postService.getPost(post.getId())).thenReturn(post);

		MvcResult createPostResult = mvc.perform(
			post("/api/post")
				.content(objectMapper.writeValueAsString(post))
				.contentType(MediaType.APPLICATION_JSON)
		  )
		  .andReturn();

		Post resultPost = objectMapper.readValue(
			createPostResult.getResponse().getContentAsString(), 
			Post.class
		);

		// CREAMOS UN NUEVO COMENTARIO
		long id = resultPost.getId();
		Comment comment = new Comment("Juan", "Buen post");
		when(postService.addComment(eq(id), eq(comment))).thenReturn(comment);


		mvc.perform(
			post("/api/post/"+id+"/comment")
				.content(objectMapper.writeValueAsString(comment))
				.contentType(MediaType.APPLICATION_JSON)
		  )
	      .andExpect(status().isCreated())
		  .andExpect(jsonPath("$.author", equalTo(comment.getAuthor())))
		  .andExpect(jsonPath("$.message", equalTo(comment.getMessage())));

		// COMPROBAMOS QUE EL COMENTARIO EXISTE

		post.getComments().add(comment);
		when(postService.getPost(post.getId())).thenReturn(post);

		mvc.perform(
			get("/api/post/"+resultPost.getId())
				.content(objectMapper.writeValueAsString(post))
				.contentType(MediaType.APPLICATION_JSON)
		  )
	      .andExpect(status().isOk())
		  .andExpect(jsonPath("$.comments[0].author", equalTo(comment.getAuthor())))
		  .andExpect(jsonPath("$.comments[0].message", equalTo(comment.getMessage())));
		  ;	
	}

	@Test
	@DisplayName("Borrar un comentario de un post y verificar que no aparece el comentario")
	public void deleteCommentTest() throws Exception {

		// CREAMOS UN NUEVO POST

		Post post = new Post("Mi titulo", "Mi contenido con comentarios");

		Mockito.doNothing().when(postService).addPost(post);


		MvcResult createPostResult = mvc.perform(
			post("/api/post")
				.content(objectMapper.writeValueAsString(post))
				.contentType(MediaType.APPLICATION_JSON)
		  )
		  .andReturn();

		Post postCreated = objectMapper.readValue(
			createPostResult.getResponse().getContentAsString(), 
			Post.class
		);

		// CREAMOS UN NUEVO COMENTARIO

		Comment comment = new Comment("Juan", "Buen post");
		long id = postCreated.getId();
		when(postService.addComment(eq(id), eq(comment))).thenReturn(comment);

		MvcResult createCommentResult = mvc.perform(
			post("/api/post/"+postCreated.getId()+"/comment")
				.content(objectMapper.writeValueAsString(comment))
				.contentType(MediaType.APPLICATION_JSON)
		).andReturn();

		Comment commentCreated = objectMapper.readValue(
			createCommentResult.getResponse().getContentAsString(), 
			Comment.class
		);

		// BORRAMOS EL COMENTARIO
		doNothing().when(postService).deleteComment(postCreated.getId(), commentCreated.getId());
		mvc.perform(
			delete("/api/post/"+postCreated.getId()+"/comment/"+commentCreated.getId())
				.content(objectMapper.writeValueAsString(comment))
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isNoContent());

		// COMPROBAMOS QUE EL COMENTARIO YA NO EXISTE

		when(postService.getPost(post.getId())).thenReturn(post);

		mvc.perform(
			get("/api/post/"+postCreated.getId())
				.content(objectMapper.writeValueAsString(post))
				.contentType(MediaType.APPLICATION_JSON)
		  )
	      .andExpect(status().isOk())
		  .andExpect(jsonPath("$.comments",  IsEmptyCollection.empty()))
		  ;		

	}

}
