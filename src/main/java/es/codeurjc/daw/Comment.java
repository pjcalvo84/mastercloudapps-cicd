package es.codeurjc.daw;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id = -1;

	private String author;

	private String message;

	public Comment() {
	}
	
	public Comment(String author, String message) {
		this.author = author;
		this.message = message;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return this.author + ": " + this.message;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Comment comment = (Comment) o;
		return id == comment.id &&
				author.equals(comment.author) &&
				message.equals(comment.message);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, author, message);
	}
}
